package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.Candidate;
import lombok.Data;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/17
 **/
@Data
public class FeedbackPersonalVo {
    /**
     * candidate information
     */
    Candidate candidate;

    /**
     * personal feedback
     */
    String personal;
}
