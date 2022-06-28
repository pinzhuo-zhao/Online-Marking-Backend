package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.Subject;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/17
 **/
@Data
public class FeedbackTeamVo {
    /**
     * name of the team
     */
    private String teamName;

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
     * feedback for this team
     */
    private List<FeedbackItemVo> feedbackItems;

    /**
     * list of personal feedback
     */
    private List<FeedbackPersonalVo> personalComments;

    private String generalFeedback;
}
