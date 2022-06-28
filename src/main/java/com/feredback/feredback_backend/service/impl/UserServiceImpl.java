package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.Subject;
import com.feredback.feredback_backend.entity.User;
import com.feredback.feredback_backend.entity.vo.CoordinatorVo;
import com.feredback.feredback_backend.entity.vo.UserVo;
import com.feredback.feredback_backend.mapper.SubjectMapper;
import com.feredback.feredback_backend.mapper.UserMapper;
import com.feredback.feredback_backend.service.IUserService;
import com.feredback.feredback_backend.service.ex.*;
import com.feredback.feredback_backend.util.CsvUtils;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvRow;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-10 15:03
 **/

@Service
public class UserServiceImpl implements IUserService {

@Autowired
private UserMapper userMapper;

@Autowired
private SubjectMapper subjectMapper;

@Value("${user.verificationMinutes.limit}")
private Integer expiryTime;



    @Override
    public User findUserByEmail(String email) {

        User userByEmail = userMapper.findUserByEmail(email);
        return userByEmail;
    }

    @Override
    public void setVerificationCode(Integer userId, String code) {
        Date now = new Date();
        Integer rowsAffected = userMapper.setVerificationCode(userId, code, now, now);
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error sending verification code");
        }
    }

    private Integer getTimeDiff(Date from, Date to) {
        //convert the result into minutes
        return (int) (to.getTime() - from.getTime()) / (60*1000);
    }

    @Override
    public boolean verifyCode(User user) {
        Date now = new Date();
        String email = user.getEmail();
        User userFound = findUserByEmail(email);
        if (userFound == null) {
            throw new UserNotFoundException("This email does not exist");
        }
        Integer timeDiff = getTimeDiff(userFound.getVerificationCodeSentTime(), now);

        //check if the verification code is expired or not
        System.out.println(timeDiff);
        if (timeDiff > expiryTime ) {
            throw new DataModificationException("Your Verification Code has expired, please try again");
        }

        //check if the verification code input by user is correct or not
        if (!user.getVerificationCode().equals(userFound.getVerificationCode())){
            throw new DataModificationException("Wrong Verification Code");
        }

        return true;

    }



    @Override
    public void changePassword(User user) {
        String bCryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(bCryptedPassword);
        user.setUpdateTime(new Date());
        Integer rowsAffected = userMapper.updateUserPassword(user);
        if (rowsAffected != 1) {
            System.out.println(rowsAffected);
            throw new DataModificationException("There is an error changing the password");
        }

    }



    @Override
    public User preProcessUser(User user) {
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        //encrypt the plaintext password
        String bCryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());

        user.setPassword(bCryptedPassword);
        return user;

    }

    @Override
    public List<UserVo> getAllTutors(Integer subjectId) {
        Integer id = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(id);
        if (!subjectIds.contains(subjectId)) {
            throw new DataModificationException("You are not allowed to access this subject");
        }
        List<UserVo> tutorsVo = new ArrayList<>();
        List<User> tutorsBySubjectId = userMapper.getTutorsBySubjectId(subjectId);
        for (User tutor : tutorsBySubjectId) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(tutor, userVo);
            tutorsVo.add(userVo);
        }
        return tutorsVo;
    }



    @Override
    public void addTutor(User user, Integer subjectId) {
        User userFound = findUserByEmail(user.getEmail());
        Date now = new Date();
        /*If the tutor associated with this email already has an account,
          only add this tutor to the subject
         */

        if (userFound != null) {
            Integer recordsFound = userMapper.findDuplicatedStaffInSubject(userFound.getId(), subjectId);
            if (recordsFound != null) {
                throw new InsertionDuplicatedException("This tutor is already in this subject");
            }
            userMapper.addStaffToSubject(userFound.getId(),subjectId,now, now);
            return;
        }
        user.setIsAdmin(0);
        user.setIsSubjectCoordinator(0);
        String initialPassword = UUID.randomUUID().toString().substring(0, 6);
        System.out.println(initialPassword);
        user.setPassword(initialPassword);
        User processedUser = preProcessUser(user);
        Integer rowsAffected = userMapper.insertUser(processedUser);
        Integer id = processedUser.getId();
        userMapper.addStaffToSubject(id,subjectId,now,now);
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error adding this user");
        }
    }

    @Override
    public int addTutorByCsv(MultipartFile file, Integer subjectId) {
        if (CsvUtils.isCsvFile(file)) {
            CsvContainer csvContainer = CsvUtils.readFromMultipartFile(file);
            List<User> users = new LinkedList<>();
            for (CsvRow row : csvContainer.getRows()) {
                String firstName = row.getField("First Name");
                String surname = row.getField("Surname");
                String emailAddress = row.getField("Email Address");
                String isHeadTutor = row.getField("Is Head Tutor");

                /*
                If a tutor already has an account, then only grant the access of a subject
                to the tutor without creating an account for the tutor
                 */
                User userFound = findUserByEmail(emailAddress);
                if (userFound != null) {
                    //skip the row if the tutor is already in the subject
                    Integer recordsFound = userMapper.findDuplicatedStaffInSubject(userFound.getId(), subjectId);
                    if (recordsFound != null) {
                        continue;
                    }
                    Date now = new Date();
                    userMapper.addStaffToSubject(userFound.getId(),subjectId, now, now);
                    continue;
                }

                //Checking if any cell from this row is empty
                if (emailAddress.isEmpty() || firstName.isEmpty() || surname.isEmpty()) {
                    throw new EmptyColumnException("Row " + row.getOriginalLineNumber()
                            + " contains empty cell, please check again");
                }

                //Encapsulate data in a row to a User Object and store it to the database
                User user = new User();
                int isHeadTutorValue = Objects.equals(isHeadTutor, "Yes") ? 1 : 0;
                user.setFirstName(firstName);
                user.setLastName(surname);
                user.setEmail(emailAddress);
                user.setIsHeadTutor(isHeadTutorValue);
                users.add(user);
            }
            for (User user:users) {
               addTutor(user, subjectId);
            }
            return users.size();
        }
        //if the file type is not csv, notify the user
        else {
            throw new FileTypeException("Please ensure you're submitting a csv file");
        }

    }




    @Override
    public void addCoordinator(User user, Integer subjectId) {
        User userFound = findUserByEmail(user.getEmail());
        Date now = new Date();
        if (userFound != null) {
            Integer recordsFound = userMapper.findDuplicatedStaffInSubject(userFound.getId(), subjectId);
            if (recordsFound != null) {
                throw new InsertionDuplicatedException("This user is already the " +
                        "subject coordinator of the subject you selected");
            }
            userMapper.addStaffToSubject(userFound.getId(), subjectId, now, now);
            return;
        }
        String initialPassword = UUID.randomUUID().toString().substring(0, 6);
        user.setPassword(initialPassword);
        User hashedUser = preProcessUser(user);
        hashedUser.setIsAdmin(0);
        hashedUser.setIsSubjectCoordinator(1);
        hashedUser.setIsHeadTutor(1);
        Integer rowsAffected = userMapper.insertUser(hashedUser);
        userMapper.addStaffToSubject(hashedUser.getId(),subjectId,now,now);
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error adding this user");
        }
    }

    @Override
    public List<UserVo> getAllCoordinators() {
        List<User> allCoordinators = userMapper.findAllCoordinators();
        ArrayList<UserVo> vos = new ArrayList<>();
        for (User coordinator: allCoordinators) {
            List<Subject> subjects = subjectMapper.getSubjectsByUserId(coordinator.getId());
            UserVo userVo = new UserVo();
            userVo.setSubjects(subjects);
            BeanUtils.copyProperties(coordinator, userVo);
            vos.add(userVo);
        }
        return vos;
    }

    @Override
    public void deleteUserById(Integer userId) {
        Date now = new Date();
        Integer rowsAffected = userMapper.deleteUserById(userId, now);
        userMapper.removeUserInSubject(userId,now);
        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error deleting this user");
        }
    }


}
