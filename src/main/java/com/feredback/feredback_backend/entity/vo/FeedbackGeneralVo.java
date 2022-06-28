package com.feredback.feredback_backend.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/19
 **/
@Data
public class FeedbackGeneralVo {
    private String generalComment;
    private Date assessmentDate;
}
