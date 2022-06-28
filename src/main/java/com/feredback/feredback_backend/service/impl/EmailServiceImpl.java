package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.Project;
import com.feredback.feredback_backend.entity.Subject;
import com.feredback.feredback_backend.entity.Team;
import com.feredback.feredback_backend.entity.vo.*;
import com.feredback.feredback_backend.mapper.CandidateMapper;
import com.feredback.feredback_backend.mapper.ProjectMapper;
import com.feredback.feredback_backend.mapper.TeamMapper;
import com.feredback.feredback_backend.service.*;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.util.CsvUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Hanlin Guo, StudentID:872416
 * @create: 2022-04-20 19:23
 **/

@Service
public class EmailServiceImpl implements IEmailService {
    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Resource
    private ISubjectService subjectService;

    @Resource
    private IPdfService pdfService;

    @Resource
    private IProjectService projectService;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private IFeedbackService feedbackService;

    @Resource
    private ITeamService teamService;

    @Resource
    private CandidateMapper candidateMapper;

    @Override
    public void sendFixedMail() {
        //创建简单的邮件发送对象
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);           // 设置发件人邮箱（若配置默认邮箱则不用再设置）
        message.setTo("1259625032@qq.com","13395711309@163.com","jimmyguo.hl@foxmail.com", "hanling@student.unimelb.edu.au");            // 设置收件人邮箱
        //message.setCc("13395711309@163.com");            // 设置抄报人邮箱（可以不填写
        //message.setBcc("575814158@qq.com");             // 设置密送人邮箱（可以不填写）
        message.setSubject("Presentation Result");        // 设置邮件主题
        message.setText("Dear student,\n  Congratulation, you get full marks!\n     Your tutor"); // 设置邮件文本内容
        message.setSentDate(new Date());                // 设置邮件发送时间
        //发送
        mailSender.send(message);
    }

    @Override
    public void sendSimpleMail(EmailVo email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email.getTo());
        message.setCc(email.getCc());
        message.setSubject(email.getTittle());
        message.setText(email.getBody());
        message.setSentDate(new Date());
        try{
            mailSender.send(message);
        } catch (Exception e){
            throw new DataModificationException("wrong email address");
        }
    }

    @Override
    public void uploadFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));

        String folder;
        if (fileType.equals(".pdf")) {
            folder = "pdf";
        } else if (fileType.equals(".csv")){
            folder = "csv";
        } else {
            folder = "others";
        }
        File file = new File(System.getProperty("user.dir")+
                "/src/main/resources/"+folder+"/"+fileName);

        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        //删除并创建新文件
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();
        multipartFile.transferTo(file);
    }

    @Override
    public void sendPDFAttachedMail(EmailVo email, String[] fileNames) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject(email.getTittle());
        helper.setFrom(fromEmail);
        helper.setTo(email.getTo());
        helper.setCc(email.getCc());
        helper.setSentDate(new Date());
        helper.setText(email.getBody());
        for (String filename:fileNames){
            File file = new File(System.getProperty("user.dir")+
                        "/src/main/resources/pdf/"+filename);
            if (!file.exists()) throw new DataModificationException("The file [" + filename + "] did not uploaded yet");
            helper.addAttachment(filename,file);
        }
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendCandicatePDFAttachedMail(String email, int projectId, int candidateId){
        String emailText = pdfService.createCandidatePDF(projectId, candidateId);
        Candidate can = candidateMapper.findCandidateById(candidateId);
        String canInfo = String.format("%s_%s_%d",can.getFirstName(),can.getLastName(),candidateId);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setSubject("Oral presentation feedback for ID"+candidateId);
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSentDate(new Date());
            helper.setText(emailText);
            File file = new File(System.getProperty("user.dir")+
                    "/src/main/resources/pdf/feedback.pdf");
            if (!file.exists()) throw new DataModificationException("The feedback did not created yet");
            helper.addAttachment("feedback_"+canInfo+".pdf",file);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new DataModificationException("wrong email address");
        }


    }

    @Override
    public void sendTeamPDFAttachedMailToTutor(String email, int projectId, int teamId){
        String emailText = pdfService.createTeamPDFToTutor(projectId, teamId);


        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setSubject("Oral presentation feedback for team "+teamId);
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSentDate(new Date());
            helper.setText(emailText);
            File file = new File(System.getProperty("user.dir")+
                    "/src/main/resources/pdf/feedback.pdf");
            if (!file.exists()) throw new DataModificationException("The feedback did not created yet");
            helper.addAttachment("feedback_team_"+teamId+".pdf",file);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
           throw new DataModificationException("wrong email address");
        }

    }

    @Override
    public void sendTeamPDFAttachedMailToCandidate(int projectId, int teamId){
        FeedbackTeamVo fb = feedbackService.getTeamFeedback(projectId,teamId);

        List<TeamVo> teams = teamService.getAllTeamsInProject(projectId);
        List<CandidateVo> cans = null;
        for (TeamVo team:teams){
            if (team.getTeam().getId()==teamId) cans = team.getCandidates();
        }

        for (CandidateVo can:cans){
            String emailText = pdfService.createTeamPDFToCandidate(fb,can);

            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
                helper.setSubject("Oral presentation feedback for team "+teamId);
                helper.setFrom(fromEmail);
                helper.setTo(candidateMapper.findCandidateById(can.getId()).getEmail());
                helper.setSentDate(new Date());
                helper.setText(emailText);
                File file = new File(System.getProperty("user.dir")+
                        "/src/main/resources/pdf/feedback.pdf");
                if (!file.exists()) throw new DataModificationException("The feedback did not created yet");
                helper.addAttachment("feedback_team_"+teamId+"_"+ can.getFirstName()+"_"
                        +can.getLastName()+".pdf",file);

                mailSender.send(mimeMessage);
            } catch (Exception e) {
                throw new DataModificationException("wrong email address");
            }
        }




    }

    @Override
    public void sendCandidateList(List<Candidate> can, String emailAccount, Integer subjectID) throws IOException, MessagingException {
        CsvUtils.generateCandidatesCsv(can);

        String subjectCode = subjectService.getSubjectById(subjectID).getSubjectCode();
        String subjecName = subjectService.getSubjectById(subjectID).getSubjectName();
        String info = subjecName + "(" + subjectCode + ")";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
        helper.setSubject(subjectCode +" - Candidate List");
        helper.setFrom(fromEmail);
        helper.setTo(emailAccount);
        helper.setSentDate(new Date());
        helper.setText("Dear User,\n\nThe candidate list of "+info+" is attached, please check it.\n\n" +
                "Cheers,\nFastFeedback Official");
        String filename = "Candidate_List_"+subjectCode+".csv";
        helper.addAttachment(filename,
                new File(System.getProperty("user.dir")+
                        "/src/main/resources/csv/Candidate_List.csv"));
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendGradeList(String emailAccount, Integer projectID){
        List<GradeVo> grades;
        Project project = projectMapper.findProjectById(projectID);
        Integer isIndidual = project.getIsIndividual();
        Integer subjectId = project.getSubjectId();

        if (isIndidual == 1){
            grades = getIndividualCandidateList(projectID);
        } else {
            grades = getTeamCandidateList(projectID);
        }

        try {
            CsvUtils.generateGradesCsv(grades);
        } catch (IOException e) {
            throw new DataModificationException("Invalid inputs");
        }

        String subjectCode = subjectService.getSubjectById(subjectId).getSubjectCode();
        String subjecName = subjectService.getSubjectById(subjectId).getSubjectName();
        String info = subjecName + "(" + subjectCode + ")";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setSubject(subjectCode+" - Grade List");
            helper.setFrom(fromEmail);
            helper.setTo(emailAccount);
            helper.setSentDate(new Date());
            helper.setText("Dear User,\n\nThe grade list of "+info+" is attached, please check it.\n\n" +
                    "Cheers,\nFastFeedback Official");
            String filename = "Grade_List_"+subjectCode+".csv";
            helper.addAttachment(filename,
                    new File(System.getProperty("user.dir")+
                            "/src/main/resources/csv/Grade_List.csv"));
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new DataModificationException("Wrong email address");
        }

    }

    private String getGradeLevel(Double rowMark){
        if (rowMark >= 80){
            return "H1";
        } else if (rowMark >= 75){
            return "H2A";
        } else if (rowMark >= 70){
            return "H2B";
        } else if (rowMark >= 60){
            return "H3";
        } else if (rowMark >= 50){
            return "P";
        } else {
            return "N";
        }
    }

    private List<GradeVo> getIndividualCandidateList(Integer projectID){
        List<GradeVo> grades = new ArrayList<>();
        List<Candidate> cans = projectService.getCandidatesInProject(projectID);

        for (Candidate can:cans){


            GradeVo grade = new GradeVo();
            grade.setStudentId(can.getId());
            grade.setStudentName(can.getFirstName()+" "+ can.getLastName());
            grade.setMark(-1.0);
            grade.setGrade(null);

            if (feedbackService.checkCandidateMark(projectID,can.getId())){
                FeedbackCandidateVo fb = feedbackService.getCandidateFeedback(projectID, can.getId());
                Double mark = fb.getTotalMark();
                grade.setMark(mark);
                grade.setGrade(getGradeLevel(mark));
            }

            grades.add(grade);
        }
        return grades;
    }

    private List<GradeVo> getTeamCandidateList(Integer projectID){
        List<GradeVo> grades = new ArrayList<>();
        List<TeamVo> teams = teamService.getAllTeamsInProject(projectID);

        for (TeamVo team:teams){

            List<CandidateVo> cans = team.getCandidates();
            Double mark = -1.0;
            String gradeLevel = null;
            if (feedbackService.checkTeamMark(projectID,team.getTeam().getId())){
                FeedbackTeamVo fb = feedbackService.getTeamFeedback(projectID,team.getTeam().getId());
                mark = fb.getTotalMark();
                gradeLevel = getGradeLevel(mark);
            }


            for (CandidateVo can:cans){
                GradeVo grade = new GradeVo();
                grade.setStudentId(can.getId());
                grade.setStudentName(can.getFirstName()+" "+ can.getLastName());
                grade.setMark(mark);
                grade.setGrade(gradeLevel);
                grades.add(grade);
            }
        }
        return grades;
    }
}
