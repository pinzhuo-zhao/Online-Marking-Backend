package com.feredback.feredback_backend.service.impl;


import com.feredback.feredback_backend.entity.vo.*;
import com.feredback.feredback_backend.service.IFeedbackService;
import com.feredback.feredback_backend.service.IPdfService;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Hanlin Guo, StudentID:872416
 * @create: 2022-05-06 16:11
 **/

@Service
public class PdfServiceImpl implements IPdfService {
    @Resource
    IFeedbackService iFeedbackService;

    private Font.FontFamily bf = Font.FontFamily.TIMES_ROMAN;
    private Font tiF = new Font(bf,32);
    private Font subTiF = new Font(bf, 20);
    private Font textF_B = new Font(bf, 14,Font.BOLD);
    private Font textF = new Font(bf, 14);

    public void createPDF() {
        PdfServiceImpl ob = new PdfServiceImpl();

        //设置纸张大小
        Document doc = new Document(PageSize.A4);
        doc.setMargins(50, 50, 50, 50);

        doc.addAuthor("FastFeedback Official");

        try {
            //设置PDF存放路径
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(System.getProperty("user.dir")+
                    "/src/main/resources/pdf/feedback.pdf"));
            doc.open();

            Rectangle rect= new Rectangle(20,20,575,822);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderColor(new BaseColor(0,51,153));
            rect.setBorderWidth(2);
            doc.add(rect);

            //inputs
            String firName = "Monkey";
            String midName = "D";
            String lstName = "Luffy";
            Integer ID = 123456;

            String subjectID = "MAST10005";
            String subjectName = "Cal1";

            String projDes = "abcd";

            Integer mark = 79;

            String assFirName = "Hanlin";
            String assMidName = "";
            String assLstName = "Guo";

            String date = "Tuesday, 7 September 2021";

            String conclude = "--Name-- good effort";


            // studentInfo : "FeedBack for (name) - (id)"
            String name;
            if (midName == ""){
                name = firName+" "+lstName;
            } else {
                name = firName+" "+midName+" "+lstName;
            }
            Paragraph studentInfo = new Paragraph("Feedback for "+name+" - "+ID, subTiF);
            studentInfo.setSpacingAfter(10);
            studentInfo.setSpacingBefore(10);

            // subjectInfo : (subjectID) - (subjectName)
            Paragraph subjectInfo = new Paragraph(subjectID+" - "+subjectName, textF);
            subjectInfo.setFirstLineIndent(20);
            subjectInfo.setSpacingAfter(10);

            // projectInfo : (projDes)
            Paragraph projectInfo = new Paragraph(projDes, ob.textF);
            projectInfo.setFirstLineIndent(20);
            projectInfo.setSpacingAfter(10);

            // markInfo : Mark Attained - (mark)%
            Paragraph markInfo = new Paragraph("Mark Attained - "+mark+"%", subTiF);
            markInfo.setSpacingAfter(10);

            // assessorInfo : (assessor name)
            String assName;
            if (assMidName == ""){
                assName = assFirName+" "+assLstName;
            } else {
                assName = assFirName+" "+assMidName+" "+assLstName;
            }
            Paragraph assessorInfo = new Paragraph(assName, textF);
            assessorInfo.setFirstLineIndent(20);
            assessorInfo.setSpacingAfter(10);

            // dateInfo : (date)
            Paragraph dateinfo = new Paragraph(date, textF);
            dateinfo.setFirstLineIndent(20);


            // split line
            PdfPTable sp = new PdfPTable(2);
            sp.setWidthPercentage(100f);
            sp.getDefaultCell().setBorderWidthLeft(0);
            sp.getDefaultCell().setBorderWidthRight(0);
            sp.getDefaultCell().setBorderWidthTop(0);
            sp.getDefaultCell().setBorderColor(new BaseColor(255, 153, 51));
            sp.getDefaultCell().setBorderWidth(2);
            sp.addCell(" ");
            sp.addCell(" ");

            // add message
            doc.add(new Paragraph("Oral Presentation Assignment", tiF));
            doc.add(sp);
            doc.add(studentInfo);
            doc.add(new Paragraph("Subject", subTiF));
            doc.add(subjectInfo);
            doc.add(new Paragraph("Project", subTiF));
            doc.add(projectInfo);
            doc.add(markInfo);
            doc.add(new Paragraph("Assessor", subTiF));
            doc.add(assessorInfo);
            doc.add(new Paragraph("Assessment Date", subTiF));
            doc.add(dateinfo);


            //生成一张图片
            //Image png = this.createImage();

            //将图片添加到PDF
            //document.add(png);
            doc.newPage();
            for (int i=0; i<10;++i){
                String stdname = "Presentation Structure (25%)";
                String stdmark = "8 / 10";
                String stddes = "It's important to start any presentation well. You need to speak loudly and clearly and to address the " +
                        "audience. This means facing them and making eye contact when possible. You did this well " +
                        "introducing yourself and your topic. Your presentation ran the right length of time. It can be difficult " +
                        "to judge length like this but you did a good job.\n" +
                        "Your presentation was well structured having a definite introduction, and main section in which key " +
                        "points are made, and a useful set of conclusions.";


                //生成一个表格
                PdfPTable table = ob.createTable(stdname,stdmark,stddes);
                //将表格添加PDF进去
                doc.add(table);
                doc.add(rect);
            }

            //Concluding Remarks
            PdfPTable table = ob.createTable("Concluding Remarks","",conclude);
            doc.add(table);
            doc.add(rect);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
    }
    //生成表格方法
    private PdfPTable createTable(String std, String mark, String des) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(3);//生成一个两列的表格
        table.setWidths(new float[]{10,390,95});
        table.setTotalWidth(495);//设置绝对宽度
        table.setLockedWidth(true);
        PdfPCell cell;

        //按顺序向表格中添加值
        cell = new PdfPCell(new Phrase(std,subTiF));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(2);
        cell.disableBorderSide(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mark,subTiF));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.disableBorderSide(15);
        table.addCell(cell);


        ArrayList<Paragraph> desA = splitStr(des);
        for (Paragraph p:desA){
            cell = new PdfPCell();
            cell.disableBorderSide(15);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.disableBorderSide(15);
            cell.addElement(p);
            cell.setColspan(2);//设置所占列数
            table.addCell(cell);
        }

        table.setSpacingAfter(15);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        return table;
    }


    private PdfPTable createFeedbackTable(String std, String mark, List<FeedbackSubItemVo> subItems,
                                          String additionalComment) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(3);//生成一个两列的表格
        table.setWidths(new float[]{10,390,95});
        table.setTotalWidth(495);//设置绝对宽度
        table.setLockedWidth(true);
        PdfPCell cell;

        //按顺序向表格中添加值
        cell = new PdfPCell(new Phrase(std,subTiF));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(2);
        cell.disableBorderSide(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mark,subTiF));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.disableBorderSide(15);
        table.addCell(cell);

        for (FeedbackSubItemVo subItem:subItems){
            if (subItem.getComment() == null){
                continue;
            }
            String subTi = subItem.getName();

            cell = new PdfPCell();
            cell.disableBorderSide(15);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.disableBorderSide(15);
            cell.addElement(new Phrase(subTi, textF_B));
            cell.setColspan(2);//设置所占列数
            table.addCell(cell);
            //最后一行给合并起来
            ArrayList<Paragraph> desA = splitStr(subItem.getComment());
            for (Paragraph p:desA){
                cell = new PdfPCell();
                cell.disableBorderSide(15);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.disableBorderSide(15);
                cell.addElement(p);
                cell.setColspan(2);//设置所占列数
                table.addCell(cell);
            }
        }

        if (additionalComment!=null){
            String subTi = "Additional Comment";

            cell = new PdfPCell();
            cell.disableBorderSide(15);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.disableBorderSide(15);
            cell.addElement(new Phrase(subTi, textF_B));
            cell.setColspan(2);//设置所占列数
            table.addCell(cell);
            //最后一行给合并起来
            ArrayList<Paragraph> desA = splitStr(additionalComment);
            for (Paragraph p:desA){
                cell = new PdfPCell();
                cell.disableBorderSide(15);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.disableBorderSide(15);
                cell.addElement(p);
                cell.setColspan(2);//设置所占列数
                table.addCell(cell);
            }
        }

        table.setSpacingAfter(15);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        return table;
    }

    private PdfPTable createPersonalTable(String std, String mark, List<FeedbackPersonalVo> personalVos) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(3);//生成一个两列的表格
        table.setWidths(new float[]{10,390,95});
        table.setTotalWidth(495);//设置绝对宽度
        table.setLockedWidth(true);
        PdfPCell cell;

        //按顺序向表格中添加值
        cell = new PdfPCell(new Phrase(std,subTiF));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setColspan(2);
        cell.disableBorderSide(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(mark,subTiF));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.disableBorderSide(15);
        table.addCell(cell);

        for (FeedbackPersonalVo personalVo:personalVos){
            if (personalVo.getPersonal() == null){
                continue;
            }
            String subTi = personalVo.getCandidate().getFirstName()+" "+personalVo.getCandidate().getLastName();

            cell = new PdfPCell();
            cell.disableBorderSide(15);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.disableBorderSide(15);
            cell.addElement(new Phrase(subTi, textF_B));
            cell.setColspan(2);//设置所占列数
            table.addCell(cell);
            //最后一行给合并起来
            ArrayList<Paragraph> desA = splitStr(personalVo.getPersonal());
            for (Paragraph p:desA){
                cell = new PdfPCell();
                cell.disableBorderSide(15);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.disableBorderSide(15);
                cell.addElement(p);
                cell.setColspan(2);//设置所占列数
                table.addCell(cell);
            }
        }

        table.setSpacingAfter(15);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        return table;
    }

    private ArrayList<Paragraph> splitStr(String des){
        ArrayList<Paragraph> op = new ArrayList<>();
        String[] strA = des.split("\n");
        for (String str:strA){
            Paragraph p = new Paragraph(str, textF);
            op.add(p);
        }
        return op;
    }

    public String createCandidatePDF(int projectId, int candidateId) {
        FeedbackCandidateVo can = iFeedbackService.getCandidateFeedback(projectId,candidateId);

        //设置纸张大小
        Document doc = new Document(PageSize.A4);
        doc.setMargins(50, 50, 50, 50);

        doc.addAuthor("FastFeedback Official");
        String emailText = "Undefined";

        try {
            //设置PDF存放路径
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(System.getProperty("user.dir")+
                    "/src/main/resources/pdf/feedback.pdf"));
            doc.open();

            Rectangle rect= new Rectangle(20,20,575,822);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderColor(new BaseColor(0,51,153));
            rect.setBorderWidth(2);
            doc.add(rect);

            //inputs
            String firName = can.getCandidate().getFirstName()==null? "[Not added]":can.getCandidate().getFirstName();
            String lstName = can.getCandidate().getLastName()==null? "[Not added]":can.getCandidate().getLastName();
            Integer ID = can.getCandidate().getId();

            String subjectID = can.getSubject().getSubjectCode()==null? "[Not added]":can.getSubject().getSubjectCode();
            String subjectName = can.getSubject().getSubjectName()==null?
                    "[Not added]":can.getSubject().getSubjectName();

            String projDes = can.getProjectTopic()==null? "[Not added]":can.getProjectTopic();

            String mark = String.format("%.1f",can.getTotalMark());

            String assFirName = can.getMarker().getFirstName()==null? "[Not added]":can.getMarker().getFirstName();
            String assLstName = can.getMarker().getLastName()==null? "[Not added]":can.getMarker().getLastName();

            String date =  can.getMarkDate()==null? "[Not added]":can.getMarkDate().toString();

            String conclude = can.getGeneralFeedback();



            // studentInfo : "FeedBack for (name) - (id)"
            String name = firName+" "+lstName;
            Paragraph studentInfo = new Paragraph("Feedback for "+name+" - "+ID, subTiF);
            studentInfo.setSpacingAfter(10);
            studentInfo.setSpacingBefore(10);

            // subjectInfo : (subjectID) - (subjectName)
            Paragraph subjectInfo = new Paragraph(subjectID+" - "+subjectName, textF);
            subjectInfo.setFirstLineIndent(20);
            subjectInfo.setSpacingAfter(10);

            // projectInfo : (projDes)
            Paragraph projectInfo = new Paragraph(projDes, textF);
            projectInfo.setFirstLineIndent(20);
            projectInfo.setSpacingAfter(10);

            // markInfo : Mark Attained - (mark)%
            Paragraph markInfo = new Paragraph("Mark Attained - "+mark+"%", subTiF);
            markInfo.setSpacingAfter(10);

            // assessorInfo : (assessor name)
            String assName = assFirName+" "+assLstName;
            Paragraph assessorInfo = new Paragraph(assName, textF);
            assessorInfo.setFirstLineIndent(20);
            assessorInfo.setSpacingAfter(10);

            // dateInfo : (date)
            Paragraph dateinfo = new Paragraph(date, textF);
            dateinfo.setFirstLineIndent(20);


            // split line
            PdfPTable sp = new PdfPTable(2);
            sp.setWidthPercentage(100f);
            sp.getDefaultCell().setBorderWidthLeft(0);
            sp.getDefaultCell().setBorderWidthRight(0);
            sp.getDefaultCell().setBorderWidthTop(0);
            sp.getDefaultCell().setBorderColor(new BaseColor(255, 153, 51));
            sp.getDefaultCell().setBorderWidth(2);
            sp.addCell(" ");
            sp.addCell(" ");

            // add message
            doc.add(new Paragraph("Oral Presentation Assignment", tiF));
            doc.add(sp);
            doc.add(studentInfo);
            doc.add(new Paragraph("Subject", subTiF));
            doc.add(subjectInfo);
            doc.add(new Paragraph("Project", subTiF));
            doc.add(projectInfo);
            doc.add(markInfo);
            doc.add(new Paragraph("Assessor", subTiF));
            doc.add(assessorInfo);
            doc.add(new Paragraph("Assessment Date", subTiF));
            doc.add(dateinfo);

            doc.newPage();
            for (FeedbackItemVo fb:can.getFeedbackItems()){
                String stdname = fb.getName()+"("+fb.getWeighting()+"%)";
                String stdmark = String.format("%.1f/%d",fb.getMark(),fb.getMaximum());
                List<FeedbackSubItemVo> subItems = fb.getSubItems();
                String addition = fb.getAdditionalComment();


                //生成一个表格
                PdfPTable table = createFeedbackTable(stdname,stdmark,subItems,addition);
                //将表格添加PDF进去
                doc.add(table);
                doc.add(rect);
            }

            //Concluding Remarks
            if (conclude!=null){
                PdfPTable table= createTable("Concluding Remarks","",conclude);
                doc.add(table);
                doc.add(rect);
            }

            emailText = "Dear "+name+",\n\nThe oral presentation feedback of "+subjectName+"("+subjectID+") is attached, please check it.\n\n" +
                    "Cheers,\nFastFeedback Official";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
        return emailText;
    }

    public String createTeamPDFToTutor(int projectId, int teamId) {
        FeedbackTeamVo team = iFeedbackService.getTeamFeedback(projectId,teamId);

        //设置纸张大小
        Document doc = new Document(PageSize.A4);
        doc.setMargins(50, 50, 50, 50);

        String emailText = "Undefined";
        doc.addAuthor("FastFeedback Official");

        try {
            //设置PDF存放路径
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(System.getProperty("user.dir")+
                    "/src/main/resources/pdf/feedback.pdf"));
            doc.open();

            Rectangle rect= new Rectangle(20,20,575,822);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderColor(new BaseColor(0,51,153));
            rect.setBorderWidth(2);
            doc.add(rect);

            //inputs
            String teamName = team.getTeamName()==null? "[Not added]":team.getTeamName();

            String subjectID = team.getSubject().getSubjectCode()==null? "[Not added]":team.getSubject().getSubjectCode();
            String subjectName = team.getSubject().getSubjectName()==null?
                    "[Not added]":team.getSubject().getSubjectName();

            String projDes = team.getProjectTopic()==null? "[Not added]":team.getProjectTopic();

            String mark = String.format("%.1f",team.getTotalMark());

            String assFirName = team.getMarker().getFirstName()==null? "[Not added]":team.getMarker().getFirstName();
            String assLstName = team.getMarker().getLastName()==null? "[Not added]":team.getMarker().getLastName();

            String date =  team.getMarkDate()==null? "[Not added]":team.getMarkDate().toString();

            List<FeedbackPersonalVo> personalComments = team.getPersonalComments();

            String conclude = team.getGeneralFeedback();



            // studentInfo : "FeedBack for (name) - (id)"
            String name = teamName;
            Paragraph studentInfo = new Paragraph("Feedback for team - "+teamName, subTiF);
            studentInfo.setSpacingAfter(10);
            studentInfo.setSpacingBefore(10);

            // subjectInfo : (subjectID) - (subjectName)
            Paragraph subjectInfo = new Paragraph(subjectID+" - "+subjectName, textF);
            subjectInfo.setFirstLineIndent(20);
            subjectInfo.setSpacingAfter(10);

            // projectInfo : (projDes)
            Paragraph projectInfo = new Paragraph(projDes, textF);
            projectInfo.setFirstLineIndent(20);
            projectInfo.setSpacingAfter(10);

            // markInfo : Mark Attained - (mark)%
            Paragraph markInfo = new Paragraph("Mark Attained - "+mark+"%", subTiF);
            markInfo.setSpacingAfter(10);

            // assessorInfo : (assessor name)
            String assName = assFirName+" "+assLstName;
            Paragraph assessorInfo = new Paragraph(assName, textF);
            assessorInfo.setFirstLineIndent(20);
            assessorInfo.setSpacingAfter(10);

            // dateInfo : (date)
            Paragraph dateinfo = new Paragraph(date, textF);
            dateinfo.setFirstLineIndent(20);


            // split line
            PdfPTable sp = new PdfPTable(2);
            sp.setWidthPercentage(100f);
            sp.getDefaultCell().setBorderWidthLeft(0);
            sp.getDefaultCell().setBorderWidthRight(0);
            sp.getDefaultCell().setBorderWidthTop(0);
            sp.getDefaultCell().setBorderColor(new BaseColor(255, 153, 51));
            sp.getDefaultCell().setBorderWidth(2);
            sp.addCell(" ");
            sp.addCell(" ");

            // add message
            doc.add(new Paragraph("Oral Presentation Assignment", tiF));
            doc.add(sp);
            doc.add(studentInfo);
            doc.add(new Paragraph("Subject", subTiF));
            doc.add(subjectInfo);
            doc.add(new Paragraph("Project", subTiF));
            doc.add(projectInfo);
            doc.add(markInfo);
            doc.add(new Paragraph("Assessor", subTiF));
            doc.add(assessorInfo);
            doc.add(new Paragraph("Assessment Date", subTiF));
            doc.add(dateinfo);

            doc.newPage();
            for (FeedbackItemVo fb:team.getFeedbackItems()){
                String stdname = fb.getName()+"("+fb.getWeighting()+"%)";
                String stdmark = String.format("%.1f/%d",fb.getMark(),fb.getMaximum());
                List<FeedbackSubItemVo> subItems = fb.getSubItems();
                String addition = fb.getAdditionalComment();


                //生成一个表格
                PdfPTable table = createFeedbackTable(stdname,stdmark,subItems,addition);
                //将表格添加PDF进去
                doc.add(table);
                doc.add(rect);
            }

            if (!(personalComments == null||personalComments.size()==0)){
                PdfPTable personalTable = createPersonalTable("Personal Comments", "", personalComments);
                doc.add(personalTable);
            }

            //Concluding Remarks
            if (conclude!=null){
                PdfPTable table= createTable("Concluding Remarks","",conclude);
                doc.add(table);
                doc.add(rect);
            }
            emailText = "Dear User,\n\nThe oral presentation feedback of "+name+"("+teamId+") in "+subjectName+"("+subjectID+") is attached, please check it.\n\n" +
                    "Cheers,\nFastFeedback Official";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
        return emailText;
    }
    public String createTeamPDFToCandidate(FeedbackTeamVo team, CandidateVo can) {

        //设置纸张大小
        Document doc = new Document(PageSize.A4);
        doc.setMargins(50, 50, 50, 50);

        String emailText = "Undefined";
        doc.addAuthor("FastFeedback Official");

        try {
            //设置PDF存放路径
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(System.getProperty("user.dir")+
                    "/src/main/resources/pdf/feedback.pdf"));
            doc.open();

            Rectangle rect= new Rectangle(20,20,575,822);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderColor(new BaseColor(0,51,153));
            rect.setBorderWidth(2);
            doc.add(rect);

            //inputs
            String teamName = team.getTeamName() + " - " +can.getFirstName()+" "+can.getLastName()+" ("+can.getId()+")";

            String subjectID = team.getSubject().getSubjectCode()==null? "[Not added]":team.getSubject().getSubjectCode();
            String subjectName = team.getSubject().getSubjectName()==null?
                    "[Not added]":team.getSubject().getSubjectName();

            String projDes = team.getProjectTopic()==null? "[Not added]":team.getProjectTopic();

            String mark = String.format("%.1f",team.getTotalMark());

            String assFirName = team.getMarker().getFirstName()==null? "[Not added]":team.getMarker().getFirstName();
            String assLstName = team.getMarker().getLastName()==null? "[Not added]":team.getMarker().getLastName();

            String date =  team.getMarkDate()==null? "[Not added]":team.getMarkDate().toString();

            List<FeedbackPersonalVo> personalComments = team.getPersonalComments();

            String conclude = team.getGeneralFeedback();



            // studentInfo : "FeedBack for (name) - (id)"
            String name = teamName;
            Paragraph studentInfo = new Paragraph("Feedback for team - "+teamName, subTiF);
            studentInfo.setSpacingAfter(10);
            studentInfo.setSpacingBefore(10);

            // subjectInfo : (subjectID) - (subjectName)
            Paragraph subjectInfo = new Paragraph(subjectID+" - "+subjectName, textF);
            subjectInfo.setFirstLineIndent(20);
            subjectInfo.setSpacingAfter(10);

            // projectInfo : (projDes)
            Paragraph projectInfo = new Paragraph(projDes, textF);
            projectInfo.setFirstLineIndent(20);
            projectInfo.setSpacingAfter(10);

            // markInfo : Mark Attained - (mark)%
            Paragraph markInfo = new Paragraph("Mark Attained - "+mark+"%", subTiF);
            markInfo.setSpacingAfter(10);

            // assessorInfo : (assessor name)
            String assName = assFirName+" "+assLstName;
            Paragraph assessorInfo = new Paragraph(assName, textF);
            assessorInfo.setFirstLineIndent(20);
            assessorInfo.setSpacingAfter(10);

            // dateInfo : (date)
            Paragraph dateinfo = new Paragraph(date, textF);
            dateinfo.setFirstLineIndent(20);


            // split line
            PdfPTable sp = new PdfPTable(2);
            sp.setWidthPercentage(100f);
            sp.getDefaultCell().setBorderWidthLeft(0);
            sp.getDefaultCell().setBorderWidthRight(0);
            sp.getDefaultCell().setBorderWidthTop(0);
            sp.getDefaultCell().setBorderColor(new BaseColor(255, 153, 51));
            sp.getDefaultCell().setBorderWidth(2);
            sp.addCell(" ");
            sp.addCell(" ");

            // add message
            doc.add(new Paragraph("Oral Presentation Assignment", tiF));
            doc.add(sp);
            doc.add(studentInfo);
            doc.add(new Paragraph("Subject", subTiF));
            doc.add(subjectInfo);
            doc.add(new Paragraph("Project", subTiF));
            doc.add(projectInfo);
            doc.add(markInfo);
            doc.add(new Paragraph("Assessor", subTiF));
            doc.add(assessorInfo);
            doc.add(new Paragraph("Assessment Date", subTiF));
            doc.add(dateinfo);

            doc.newPage();
            for (FeedbackItemVo fb:team.getFeedbackItems()){
                String stdname = fb.getName()+"("+fb.getWeighting()+"%)";
                String stdmark = String.format("%.1f/%d",fb.getMark(),fb.getMaximum());
                List<FeedbackSubItemVo> subItems = fb.getSubItems();
                String addition = fb.getAdditionalComment();


                //生成一个表格
                PdfPTable table = createFeedbackTable(stdname,stdmark,subItems,addition);
                //将表格添加PDF进去
                doc.add(table);
                doc.add(rect);
            }

            if (!(personalComments == null||personalComments.size()==0)){
                for (FeedbackPersonalVo fbpersonal: personalComments){
                    if (fbpersonal.getCandidate().getId()==can.getId()){
                        PdfPTable personalTable = createTable("Personal Comments ","",fbpersonal.getPersonal());
                        doc.add(personalTable);
                        break;
                    }
                }
            }

            //Concluding Remarks
            if (conclude!=null){
                PdfPTable table= createTable("Concluding Remarks","",conclude);
                doc.add(table);
                doc.add(rect);
            }
            emailText = "Dear "+can.getFirstName()+" "+can.getLastName()+",\n\n"
                    +"The oral presentation feedback of "+subjectName+"("+subjectID+") is attached, please check it.\n\n" +
                    "Cheers,\nFastFeedback Official";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
        return emailText;
    }
}
