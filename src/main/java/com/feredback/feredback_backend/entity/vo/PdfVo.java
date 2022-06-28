package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.Project;
import com.feredback.feredback_backend.entity.Subject;
import com.feredback.feredback_backend.entity.User;
import lombok.Data;

/**
 * @program: FE-Redback
 * @description:
 * @author: Hanlin Guo, StudentID:872416
 * @create: 2022-05-16 17:42
 **/

@Data
public class PdfVo {
    Candidate candidate;
    Subject subject;
    Project project;
    Integer mark;
    User user;

    String Remarks;
}
