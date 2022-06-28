package com.feredback.feredback_backend.entity.vo;

/**
 * @program: FE-Redback
 * @description:
 * @author: Hanlin Guo, StudentID:872416
 * @create: 2022-04-20 19:23
 **/

import com.feredback.feredback_backend.entity.BaseEntity;
import lombok.Data;

@Data
public class EmailVo{
    private String tittle;
    private String[] cc; // tutor邮箱，抄送邮件
    private String[] to;
    private String body;

}
