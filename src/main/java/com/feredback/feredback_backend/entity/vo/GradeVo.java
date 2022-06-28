package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.BaseEntity;
import lombok.Data;

/**
 * @program: FE-Redback
 * @description:
 * @author: Hanlin Guo, StudentID:872416
 * @create: 2022-05-08 17:27
 **/

@Data
public class GradeVo {
    private Integer studentId;
    private String studentName;
    private String grade;
    private Double mark;

}
