package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.vo.EmailVo;
import com.feredback.feredback_backend.entity.vo.GradeVo;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Hanlin Guo, StudentID:872416
 * @create: 2022-04-20 19:16
 **/

public interface IEmailService {
    void sendFixedMail();
    void sendSimpleMail(EmailVo email);
    void uploadFile(MultipartFile multipartFile) throws IOException;
    void sendPDFAttachedMail(EmailVo email, String[] fileName) throws MessagingException;
    void sendCandicatePDFAttachedMail(String email, int projectId, int candidateId);
    void sendTeamPDFAttachedMailToTutor(String email, int projectId, int teamId);
    void sendTeamPDFAttachedMailToCandidate(int projectId, int teamId);
    void sendCandidateList(List<Candidate> can, String emailaccount, Integer subjectID) throws IOException, MessagingException;
    void sendGradeList(String emailAccount, Integer projectID);
}
