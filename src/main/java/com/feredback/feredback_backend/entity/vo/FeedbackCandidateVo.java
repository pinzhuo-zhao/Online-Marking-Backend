package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.Subject;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: candidate feedback vo class
 * @author: Xun Zhang (854776)
 * @date: 2022/5/17
 **/
@Data
public class FeedbackCandidateVo {
    /**
     * candidate information
     */
    private Candidate candidate;

    /**
     * marker of this feedback
     */
    private UserVo marker;

    /**
     * subject information
     */
    private Subject subject;

    /**
     * project topic
     */
    private String projectTopic;

    /**
     * marking date
     */
    private Date markDate;

    /**
     * total mark of this candidate
     */
    private double totalMark;

    /**
     * feedback for this candidate
     */
    private List<FeedbackItemVo> feedbackItems;

    private String generalFeedback;
}
