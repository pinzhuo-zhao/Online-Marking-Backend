package com.feredback.feredback_backend.controller;

import com.feredback.feredback_backend.entity.User;
import com.feredback.feredback_backend.entity.vo.CoordinatorVo;
import com.feredback.feredback_backend.entity.vo.EmailVo;
import com.feredback.feredback_backend.entity.vo.UserVo;
import com.feredback.feredback_backend.service.IEmailService;
import com.feredback.feredback_backend.service.ISubjectService;
import com.feredback.feredback_backend.service.IUserService;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.service.ex.UserNotFoundException;
import com.feredback.feredback_backend.util.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-10 00:53
 **/

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private IEmailService emailService;

    @ApiOperation("Form needs firstName, middleName(optional), lastName, isHeadTutor, email, path variable needs id")
    @PostMapping("coordinator/addTutor/{subjectId}")
    public JsonResult addTutor(@RequestBody User user, @PathVariable Integer subjectId) {
        userService.addTutor(user, subjectId);
        return JsonResult.ok();
    }

    @PostMapping("coordinator/addTutorByCsv/{subjectId}")
    public JsonResult addTutorByCsv(@RequestParam("file") MultipartFile file, @PathVariable Integer subjectId) {
        int tutorsAdded = userService.addTutorByCsv(file, subjectId);
        return JsonResult.ok().data("tutorsAdded", tutorsAdded);
    }


    @ApiOperation(value = " form needs firstName, middleName(optional), lastName, email" +
            "(let the user choose subject code from a dropdown menu" +
            "(still sending subject id in actual)")
    @PostMapping("admin/addCoordinator/{subjectId}")
    public JsonResult addCoordinator(@RequestBody User user, @PathVariable Integer subjectId) {
        userService.addCoordinator(user, subjectId);
        return JsonResult.ok();
    }

    @ApiOperation(value = "", notes = "{\n" +
            "  \"subject\": {\n" +
            "    \"subjectCode\": \"SWEN10001\",\n" +
            "    \"subjectName\": \"Software development\"\n" +
            "  },\n" +
            "  \"user\": {\n" +
            "    \"email\": \"useremail@xmail.com\",\n" +
            "    \"firstName\": \"John\",\n" +
            "    \"lastName\": \"SdAmin\"\n" +
            "  }\n" +
            "}")
    @PostMapping("admin/addCoordinatorAndSubject")
    public JsonResult addCoordinatorAndSubject(@RequestBody CoordinatorVo coordinatorVo) {
        subjectService.addSubject(coordinatorVo.getSubject());
        Integer subjectIdInserted = coordinatorVo.getSubject().getId();
        userService.addCoordinator(coordinatorVo.getUser(),subjectIdInserted);
        return JsonResult.ok();
    }

    @GetMapping("coordinator/getAllTutors/{subjectId}")
    public JsonResult getAllTutors(@PathVariable Integer subjectId) {
        List<UserVo> tutors = userService.getAllTutors(subjectId);
        JsonResult result = JsonResult.ok();
        result.data("tutors",tutors);
        return result;
    }

    @PutMapping("coordinator/deleteTutorById/{userId}")
    public JsonResult deleteTutorsById(@PathVariable Integer userId) {
        userService.deleteUserById(userId);
        JsonResult result = JsonResult.ok();
        return result;
    }

    @GetMapping("findUserByEmail")
    public JsonResult findUserByEmail(@RequestParam String emailAddress) {
        User userByEmail = userService.findUserByEmail(emailAddress);
        if (userByEmail == null) {
            throw new UserNotFoundException("This email does not exist in the system yet");
        }
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(userByEmail,vo);
        JsonResult result = JsonResult.ok();
        result.data("userByEmail",vo);
        return result;
    }

    @GetMapping("admin/getAllCoordinators")
    public JsonResult getAllCoordinators() {
        List<UserVo> coordinators = userService.getAllCoordinators();
        JsonResult result = JsonResult.ok();
        result.data("coordinators",coordinators);
        return result;
    }



    @PostMapping("sendVerificationCode")
    public JsonResult sendVerificationCode(@RequestParam String emailAddress) {
        User userFound = userService.findUserByEmail(emailAddress);
        if (userFound == null) {
            throw new UserNotFoundException("This email does not exist");
        }
        String uuid = UUID.randomUUID().toString();
        String verificationCode = uuid.substring(0, 4);
        userService.setVerificationCode(userFound.getId(),verificationCode);

        EmailVo email = new EmailVo();
        email.setTittle("Your Verification Code");
        email.setTo(new String[]{emailAddress});
        StringBuilder builder = new StringBuilder();
        builder.append("Your password reset verification code is: ");
        builder.append(verificationCode);
        builder.append(". The code will expire in 10 minutes");
        email.setBody(builder.toString());
        emailService.sendSimpleMail(email);
        return JsonResult.ok();
    }

    @PostMapping("changePassword")
    @ApiOperation(value = "Needs email, password and verificationCode", notes = "{\n" +
            "  \"email\": \"xxxxx@gmail.com\",\n" +
            "  \"password\": \"newpassword\",\n" +
            "  \"verificationCode\": \"1383\"\n" +
            "}")
    public JsonResult changePassword(@RequestBody User user) {
        boolean bool = userService.verifyCode(user);
        if (bool) {
            userService.changePassword(user);
            return  JsonResult.ok();
        }
        return JsonResult.error().message("There is a problem occurred when updating your password");
    }



}
