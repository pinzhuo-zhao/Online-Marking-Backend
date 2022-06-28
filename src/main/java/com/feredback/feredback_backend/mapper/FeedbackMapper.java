package com.feredback.feredback_backend.mapper;

import com.feredback.feredback_backend.entity.*;
import com.feredback.feredback_backend.entity.vo.FeedbackGeneralVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @program: FE-Redback
 * @description: mapper interface of feedback related works
 * @author: Xun Zhang (854776)
 * @date: 2022/5/14
 **/
@Mapper
public interface FeedbackMapper {
    /**
     * get mid table id by given project id and candidate id
     * @param projectId id of project
     * @param candidateId id of candidate
     * @return id of mid table
     */
    Integer getProjectCandidateRelationship(int projectId, int candidateId);

    /**
     * get mid table id by given project id and team id
     * @param projectId id of project
     * @param teamId if of team
     * @return id of mid table
     */
    Integer getProjectTeamRelationship(int projectId, int teamId);

    /**
     * get general feedback items of candidate
     * @param projectCandidateId id of project candidate mid table
     * @return specific general feedback items
     */
    FeedbackGeneralVo getCandidateGeneral(int projectCandidateId);

    /**
     * get general feedback items of team
     * @param projectTeamId id of project team mid table
     * @return specific general feedback items
     */
    FeedbackGeneralVo getTeamGeneral(int projectTeamId);

    /**
     * save candidate general feedback
     *
     * @param projectCandidateId id of project candidate mid table
     * @param generalFeedback general feedback
     * @param updateTime update time
     * @return row affected
     */
    Integer saveCandidateGeneralFeedback(int projectCandidateId, String generalFeedback, Date updateTime);

    /**
     * save team general feedback
     *
     * @param projectTeamId id of project team mid table
     * @param generalFeedback general feedback
     * @param updateTime update time
     * @return row affected
     */
    Integer saveTeamGeneralFeedback(int projectTeamId, String generalFeedback, Date updateTime);

    /**
     * set candidate assessment date
     *
     * @param projectCandidateId id of project candidate mid table
     * @param assessmentDate date of assessment
     * @param updateTime update time
     * @return row affected
     */
    Integer setCandidateAssessmentDate(int projectCandidateId, Date assessmentDate, Date updateTime);

    /**
     * set team assessment date
     *
     * @param projectTeamId id of team candidate mid table
     * @param assessmentDate date of assessment
     * @param updateTime update time
     * @return row affected
     */
    Integer setTeamAssessmentDate(int projectTeamId, Date assessmentDate, Date updateTime);

    /**
     * insert candidate comment data
     * @param entity data entity
     * @return row affected
     */
    Integer insertCandidateComment(FeedbackCandidateComment entity);

    /**
     * insert candidate mark data
     * @param entity data entity
     * @return row affected
     */
    Integer insertCandidateMark(FeedbackCandidateMark entity);

    /**
     * insert team comment data
     * @param entity data entity
     * @return row affected
     */
    Integer insertTeamComment(FeedbackTeamComment entity);

    /**
     * insert team mark data
     * @param entity data entity
     * @return row affected
     */
    Integer insertTeamMark(FeedbackTeamMark entity);

    /**
     * insert team personal feedback
     * @param entity data entity
     * @return row affected
     */
    Integer insertTeamPersonal(FeedbackTeamPersonal entity);

    /**
     * get candidate comment feedback by project candidate relationship and rubric sub item id
     * @param projectCandidateId id of project candidate mid table
     * @param rubricSubItemId id of rubric sub item
     * @return specific candidate comment feedback
     */
    FeedbackCandidateComment getCandidateCommentByRubricId(int projectCandidateId, int rubricSubItemId);

    /**
     * get candidate mark feedback by project candidate relationship and rubric item id
     * @param projectCandidateId id of project candidate mid table
     * @param rubricItemId id of rubric item
     * @return specific candidate mark feedback
     */
    FeedbackCandidateMark getCandidateMarkByRubricId(int projectCandidateId, int rubricItemId);

    /**
     * get team comment feedback by project candidate relationship and rubric sub item id
     * @param projectTeamId id of project team mid table
     * @param rubricSubItemId id of rubric sub item
     * @return specific team comment feedback
     */
    FeedbackTeamComment getTeamCommentByRubricId(int projectTeamId, int rubricSubItemId);

    /**
     * get team mark feedback by project candidate relationship and rubric item id
     * @param projectTeamId id of project candidate mid table
     * @param rubricItemId id of rubric item
     * @return specific candidate comment feedback
     */
    FeedbackTeamMark getTeamMarkByRubricId(int projectTeamId, int rubricItemId);

    /**
     * get team personal feedback by project candidate relationship and candidate id
     * @param projectTeamId id of project candidate mid table
     * @param candidateId id of candidate
     * @return specific team personal feedback
     */
    FeedbackTeamPersonal getTeamPersonalByCandidateId(int projectTeamId, int candidateId);

    /**
     * get candidate comment feedback by project candidate relationship
     * @param projectCandidateId id of project candidate mid table
     * @return list of candidate comment feedback
     */
    List<FeedbackCandidateComment> getCandidateCommentByMidId(int projectCandidateId);

    /**
     * get candidate mark feedback by project candidate relationship
     * @param projectCandidateId id of project candidate mid table
     * @return list of candidate mark feedback
     */
    List<FeedbackCandidateMark> getCandidateMarkByMidId(int projectCandidateId);

    /**
     * get team comment feedback by project team relationship
     * @param projectTeamId id of project team mid table
     * @return list of team comment feedback
     */
    List<FeedbackTeamComment> getTeamCommentByMidId(int projectTeamId);

    /**
     * get team mark feedback by project team relationship
     * @param projectTeamId id of project team mid table
     * @return list of team mark feedback
     */
    List<FeedbackTeamMark> getTeamMarkByMidId(int projectTeamId);

    /**
     *  get team personal feedback by project team relationship
     * @param projectTeamId id of project team mid table
     * @return list of team personal feedback
     */
    List<FeedbackTeamPersonal> getTeamPersonalByMidId(int projectTeamId);

    /**
     * update candidate comment feedback by given id
     * @param entity new information of candidate comment feedback
     * @return row affected
     */
    Integer updateCandidateCommentById(FeedbackCandidateComment entity);

    /**
     * update candidate mark feedback by given id
     * @param entity new information of candidate mark feedback
     * @return row affected
     */
    Integer updateCandidateMarkById(FeedbackCandidateMark entity);

    /**
     * update team comment feedback by given id
     * @param entity new information of team comment feedback
     * @return row affected
     */
    Integer updateTeamCommentById(FeedbackTeamComment entity);

    /**
     * update team mark feedback by given id
     * @param entity new information of team mark feedback
     * @return row affected
     */
    Integer updateTeamMarkById(FeedbackTeamMark entity);

    /**
     * update team personal feedback by given id
     * @param entity new information of team personal feedback
     * @return row affected
     */
    Integer updateTeamPersonalById(FeedbackTeamPersonal entity);

    /**
     * logic delete candidate comment feedback by given id
     * @param id given candidate comment feedback id
     * @param updateTime update time
     * @return row affected
     */
    Integer deleteCandidateCommentById(int id, Date updateTime);

    /**
     * logic delete candidate mark feedback by given id
     * @param id given candidate mark feedback id
     * @param updateTime update time
     * @return row affected
     */
    Integer deleteCandidateMarkById(int id, Date updateTime);

    /**
     * logic delete team comment feedback by given id
     * @param id given team comment feedback id
     * @param updateTime update time
     * @return row affected
     */
    Integer deleteTeamCommentById(int id, Date updateTime);

    /**
     * logic delete team mark feedback by given id
     * @param id given team mark feedback id
     * @param updateTime update time
     * @return row affected
     */
    Integer deleteTeamMarkById(int id, Date updateTime);

    /**
     * logic delete team personal feedback by given id
     * @param id given team personal feedback id
     * @param updateTime update time
     * @return row affected
     */
    Integer deleteTeamPersonalById(int id, Date updateTime);

    /**
     * logic delete candidate comment feedback by given mid table id
     * @param projectCandidateId id of project candidate mid table
     * @param updateTime update time
     * @return row affected
     */
    Integer clearCandidateCommentByMidId(int projectCandidateId, Date updateTime);

    /**
     * logic delete candidate mark feedback by given mid table id
     * @param projectCandidateId id of project candidate mid table
     * @param updateTime update time
     * @return row affected
     */
    Integer clearCandidateMarkByMidId(int projectCandidateId, Date updateTime);

    /**
     * logic delete team comment feedback by given mid table id
     * @param projectTeamId id of project team mid table
     * @param updateTime update time
     * @return row affected
     */
    Integer clearTeamCommentByMidId(int projectTeamId, Date updateTime);

    /**
     * logic delete team mark feedback by given mid table id
     * @param projectTeamId id of project team mid table
     * @param updateTime update time
     * @return row affected
     */
    Integer clearTeamMarkByMidId(int projectTeamId, Date updateTime);

    /**
     * logic delete team personal feedback by given mid table id
     * @param projectTeamId id of project team mid table
     * @param updateTime update time
     * @return row affected
     */
    Integer clearTeamPersonalByMidId(int projectTeamId, Date updateTime);
}
