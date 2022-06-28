package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.mapper.CandidateMapper;
import com.feredback.feredback_backend.mapper.SubjectMapper;
import com.feredback.feredback_backend.mapper.UserMapper;
import com.feredback.feredback_backend.service.ICandidateService;
import com.feredback.feredback_backend.service.ex.*;
import com.feredback.feredback_backend.util.CsvUtils;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
* @author pinzhuozhao
* @description implementation of the service layer ICandidateService
* @createDate 2022-04-03 23:22:50
*/
@Service
public class CandidateServiceImpl implements ICandidateService {

@Autowired
private CandidateMapper candidateMapper;

@Autowired
private SubjectMapper subjectMapper;

@Autowired
private UserMapper userMapper;

    @Override
    public void addCandidate(Candidate candidate, Integer subjectId) {
        Candidate candidateFoundByEmail = findCandidateByEmail(candidate.getEmail());
        Candidate candidateFoundById = findCandidateById(candidate.getId());
        Date now = new Date();
        //Check if the user has the access to the subject
        Integer userId = SecurityContextUtil.getCurrentUser().getId();
        List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
        if (!subjectIds.contains(subjectId)) {
            throw new DataModificationException("You are not allowed to access this subject");
        }

        if (candidateFoundByEmail == null && candidateFoundById == null) {
            candidate.setCreateTime(new Date());
            candidate.setUpdateTime(new Date());
            Integer rowsAffected = candidateMapper.insertCandidate(candidate);

            subjectMapper.insertCandidateToSubject(candidate.getId(),subjectId, now, now);
            if (rowsAffected != 1) {
                throw new DataModificationException("Insertion Error");
            }
        } else {
            Candidate candidateByEmailAndId = candidateMapper.findCandidateByEmailAndId(candidate.getEmail(), candidate.getId());
            if (candidateByEmailAndId == null) {
                throw new DataModificationException("This user's Email And ID do not match, cannot add to subject");
            }
            Integer candidateId = candidateMapper.findDuplicatedCandidateInSubject(candidateByEmailAndId.getId(),subjectId);
            if (candidateId != null) {
                throw new InsertionDuplicatedException("This student is already in this subject");
            }
            Integer rowsAffected = subjectMapper.insertCandidateToSubject(
                    candidateByEmailAndId.getId(), subjectId, now, now);
            if (rowsAffected != 1) {
                throw new DataModificationException("Insertion Error");
            }
        }
    }

    @Override
    public Candidate findCandidateByEmail(String email) {
        return candidateMapper.findByEmail(email);
    }

    @Override
    public Candidate findCandidateById(Integer id) {
        return candidateMapper.findCandidateById(id);
    }

    @Override
    public void deleteCandidateById(Integer id) {
        Candidate candidateFoundById = findCandidateById(id);

        if (candidateFoundById == null) {
            throw new DataModificationException("This candidate cannot be found");
        }
        Date updateTime = new Date();
        Integer rowsAffected = candidateMapper.deleteCandidateById(id, updateTime);
        candidateMapper.removeCandidateFromAllSubjects(id,updateTime);
        candidateMapper.removeCandidateFromAllProjects(id,updateTime);
        candidateMapper.removeCandidateFromAllTeams(id,updateTime);

        if (rowsAffected != 1) {
            throw new DataModificationException("There is an error deleting this candidate");
        }

    }


    @Override
    public List<Candidate> getAllUsers(Integer subjectId) {
        return candidateMapper.findAllCandidates(subjectId);
    }

    @Override
    public Integer addCandidateByCsv(MultipartFile multipartFile, Integer subjectId) {
        if (CsvUtils.isCsvFile(multipartFile)) {
            CsvContainer csvContainer = CsvUtils.readFromMultipartFile(multipartFile);
            List<Candidate> candidates = new LinkedList<>();
            for (CsvRow row : csvContainer.getRows()) {
                String idString = row.getField("Student Id").trim();
                String emailAddress = row.getField("Student Email").trim();
                String studentName = row.getField("Student Name").trim();
                //Checking if any cell from this row is empty
                if (idString.isEmpty() || emailAddress.isEmpty() || studentName.isEmpty()) {
                    throw new EmptyColumnException("Row " + row.getOriginalLineNumber()
                            + " contains empty cell, please check again");
                    }
                Integer id = Integer.valueOf(idString);
                int firstIndex = studentName.indexOf(' ');
                String firstName = studentName.substring(0, firstIndex).trim();
                int lastIndex = studentName.lastIndexOf(' ');
                String middleName = studentName.substring(firstIndex + 1, lastIndex).trim();
                String lastName = studentName.substring(lastIndex + 1).trim();

                //Encapsulate data in a row to a Candidate Object and store it to the database
                Candidate candidate = new Candidate();
                candidate.setId(id);
                candidate.setFirstName(firstName);
                candidate.setMiddleName(middleName);
                candidate.setLastName(lastName);
                candidate.setEmail(emailAddress);
                candidates.add(candidate);

            }
            int count = 0;
            for (Candidate candidate:candidates) {
                Date now = new Date();
                //Check if the user has the access to the subject
                Integer userId = SecurityContextUtil.getCurrentUser().getId();
                List<Integer> subjectIds = userMapper.findSubjectIdsByUserId(userId);
                if (!subjectIds.contains(subjectId)) {
                    throw new DataModificationException("You are not allowed to access this subject");
                }
                //Checking if the email or id provided is already existed in the database
                Candidate candidateFoundByEmail = findCandidateByEmail(candidate.getEmail());
                Candidate candidateFoundById = findCandidateById(candidate.getId());
                if (candidateFoundByEmail == null && candidateFoundById == null) {
                    candidate.setCreateTime(new Date());
                    candidate.setUpdateTime(new Date());
                    Integer rowsAffected = candidateMapper.insertCandidate(candidate);
                    subjectMapper.insertCandidateToSubject(candidate.getId(),subjectId, now, now);
                    if (rowsAffected != 1) {
                        throw new DataModificationException("Insertion Error");
                    }
                    count++;
                } else if (candidateFoundByEmail != null){
                    Integer candidateId = candidateMapper.findDuplicatedCandidateInSubject(candidateFoundById.getId(), subjectId);
                    if (candidateId != null) {
                        continue;
                    }
                    Integer rowsAffected = subjectMapper.insertCandidateToSubject(
                            candidateFoundByEmail.getId(), subjectId, now, now);
                    if (rowsAffected != 1) {
                        throw new DataModificationException("Insertion Error");
                    }
                    count++;
                }
            }
            return count;
        }
        //if the file type is not csv, notify the user
        else {
            throw new FileTypeException("Please ensure you're submitting a csv file");
        }
    }

}
