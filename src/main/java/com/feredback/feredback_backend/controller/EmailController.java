package com.feredback.feredback_backend.controller;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.vo.EmailVo;
import com.feredback.feredback_backend.entity.vo.GradeVo;
import com.feredback.feredback_backend.service.ICandidateService;
import com.feredback.feredback_backend.service.IEmailService;
import com.feredback.feredback_backend.service.IPdfService;
import com.feredback.feredback_backend.service.ISubjectService;
import com.feredback.feredback_backend.util.JsonResult;
import com.feredback.feredback_backend.service.impl.PdfServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Hanlin Guo, StudentID:872416
 * @create: 2022-04-18 13:03
 **/


@RestController
@RequestMapping("/api/email")
@CrossOrigin
public class EmailController extends BaseController{
    //注入邮件git发送对象,用@resource test1
    @Resource
    private IEmailService emailService;

    @Resource
    private ICandidateService candidateService;

    @Resource
    private IPdfService iPdfService;

    /**
     * 发送固定邮件
     * @return
     */
    @PostMapping("fixed")
    @ApiOperation("对固定用户发送固定邮件信息，测试用")
    public JsonResult sendFixed () {
        emailService.sendFixedMail();
        return JsonResult.ok();
    }

    /***
     * 简单邮件发送
     */
    @PostMapping("simple")
    @ApiOperation("send text message to a certain user")
    public JsonResult sendSimple(@RequestBody EmailVo email) {
        emailService.sendSimpleMail(email);
        return JsonResult.ok();
    }

    @PostMapping("uploadFile")
    @ApiOperation("upload file, size limit 10mb")
    public JsonResult uploadFile(@RequestParam MultipartFile multipartFile) throws MessagingException, IOException {
        emailService.uploadFile(multipartFile);
        return JsonResult.ok();
    }

    @PostMapping("sendPDFMail/Candidate")
    @ApiOperation("send feedback email to candidate")
    public JsonResult sendCandidatePDFMail(@RequestParam String email, @RequestParam int projectId,
                                  @RequestParam int candidateId) throws MessagingException {
        emailService.sendCandicatePDFAttachedMail(email,projectId,candidateId);
        return  JsonResult.ok();
    }

    @PostMapping("sendPDFMail/Team/ToTutor")
    @ApiOperation("send feedback email to team members")
    public JsonResult sendTeamPDFMailToTutor(@RequestParam String email, @RequestParam int projectId,
                                  @RequestParam int teamId) throws MessagingException {
        emailService.sendTeamPDFAttachedMailToTutor(email,projectId,teamId);
        return  JsonResult.ok();
    }

    @PostMapping("sendPDFMail/Team/ToCandidate")
    @ApiOperation("send feedback email to team members")
    public JsonResult sendTeamPDFMailToCandidate(@RequestParam int projectId, @RequestParam int teamId) throws MessagingException {
        emailService.sendTeamPDFAttachedMailToCandidate(projectId,teamId);
        return  JsonResult.ok();
    }

    @PostMapping("csv/candidateList")
    @ApiOperation("send email with candidate list attachment" +
            ",needs user's emailAccount，subjectId(no special character allowed)")
    public JsonResult sendCandidateMail(@RequestParam String emailAccount, @RequestParam Integer subjectID) throws MessagingException, IOException {
        List<Candidate> candidateList = candidateService.getAllUsers(subjectID);
        emailService.sendCandidateList(candidateList, emailAccount, subjectID);
        return JsonResult.ok();
    }

    @PostMapping("csv/gradeList")
    @ApiOperation("send email with grade list attachment" +
            ",needs user's emailAccount，subjectId(no special character allowed)")
    public JsonResult sendGradeMail(@RequestParam String emailAccount, @RequestParam Integer projectID){
        emailService.sendGradeList(emailAccount, projectID);
        return JsonResult.ok();
    }

}
