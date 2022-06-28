package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.vo.CandidateVo;
import com.feredback.feredback_backend.entity.vo.FeedbackSubItemVo;
import com.feredback.feredback_backend.entity.vo.FeedbackTeamVo;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.IOException;
import java.util.List;

public interface IPdfService {
    void createPDF();
    String createCandidatePDF(int projectId, int candidateId);
    String createTeamPDFToTutor(int projectId, int teamId);
    String createTeamPDFToCandidate(FeedbackTeamVo team, CandidateVo can);
}
