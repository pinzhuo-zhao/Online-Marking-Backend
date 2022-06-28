package com.feredback.feredback_backend.service;

import com.feredback.feredback_backend.entity.FeedbackTeamPersonal;
import com.feredback.feredback_backend.entity.vo.FeedbackCandidateVo;
import com.feredback.feredback_backend.entity.vo.FeedbackItemVo;
import com.feredback.feredback_backend.entity.vo.FeedbackTeamVo;

import java.util.List;

/**
 * @program: FE-Redback
 * @description: feedback service interface
 * @author: Xun Zhang (854776)
 * @date: 2022/5/15
 **/
public interface IFeedbackService {
    /**
     * given project id and candidate id, get feedback
     * @param projectId id of project
     * @param candidateId id of candidate
     * @return candidate feedback
     */
    FeedbackCandidateVo getCandidateFeedback(int projectId, int candidateId);

    /**
     * given project id and team id, get feedback
     * @param projectId id of project
     * @param teamId id of team
     * @return team feedback
     */
    FeedbackTeamVo getTeamFeedback(int projectId, int teamId);

    /**
     * back up current candidate feedback
     * @param projectId id of project
     * @param candidateId id of candidate
     * @param feedbacks list of feedback item
     */
    void saveCandidateFeedback(int projectId, int candidateId, List<FeedbackItemVo> feedbacks);

    /**
     * back up candidate general feedback
     * @param projectId id of project
     * @param candidateId id of candidate
     * @param generalFeedback general feedback
     */
    void saveCandidateGeneralFeedback(int projectId, int candidateId, String generalFeedback);

    /**
     * back up current candidate feedback
     * @param projectId id of project
     * @param teamId id of team
     * @param feedbacks list of feedback item
     */
    void saveTeamFeedback(int projectId, int teamId, List<FeedbackItemVo> feedbacks);

    /**
     * back up current personal feedback
     * @param projectId id of project
     * @param teamId id of team
     * @param personalFeedbacks list of personal feedback
     */
    void savePersonalFeedback(int projectId, int teamId, List<FeedbackTeamPersonal> personalFeedbacks);

    /**
     * back up team general feedback
     * @param projectId id of project
     * @param teamId id of team
     * @param generalFeedback general feedback
     */
    void saveTeamGeneralFeedback(int projectId, int teamId, String generalFeedback);

    /**
     * delete all project candidate feedbacks by given id
     * @param projectId id of project
     * @param candidateId id of candidate
     */
    void clearCandidateFeedback(int projectId, int candidateId);

    /**
     * delete all team candidate feedbacks by given id
     * @param projectId id of project
     * @param teamId id of team
     */
    void clearTeamFeedback(int projectId, int teamId);

    boolean checkCandidateMark(int projectId, int candidateId);

    boolean checkTeamMark(int projectId, int teamId);
}
