package com.feredback.feredback_backend.service.impl;

import com.feredback.feredback_backend.entity.*;
import com.feredback.feredback_backend.entity.vo.*;
import com.feredback.feredback_backend.mapper.*;
import com.feredback.feredback_backend.service.IFeedbackService;
import com.feredback.feredback_backend.service.ex.DataModificationException;
import com.feredback.feredback_backend.util.ResultUtils;
import com.feredback.feredback_backend.util.SecurityContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/17
 **/
@Service
public class FeedbackServiceImpl implements IFeedbackService {
    @Resource
    FeedbackMapper feedbackMapper;

    @Resource
    ProjectMapper projectMapper;

    @Resource
    RubricItemMapper rubricItemMapper;

    @Resource
    RubricSubItemMapper rubricSubItemMapper;

    @Resource
    CandidateMapper candidateMapper;

    @Resource
    TeamMapper teamMapper;

    @Resource
    CommentMapper commentMapper;

    @Resource
    SubjectMapper subjectMapper;

    @Resource
    MarkSettingMapper markSettingMapper;

    /**
     * given project id and candidate id, get feedback
     *
     * @param projectId   id of project
     * @param candidateId id of candidate
     * @return candidate feedback
     */
    @Override
    public FeedbackCandidateVo getCandidateFeedback(int projectId, int candidateId) {
        // search related information
        Project project = projectMapper.findProjectById(projectId);
        if (Objects.isNull(project)) {
            throw new DataModificationException("The project cannot be found");
        }

        Subject subject = subjectMapper.findSubjectById(project.getSubjectId());
        if (Objects.isNull(subject)) {
            throw new DataModificationException("The subject cannot be found");
        }

        Candidate candidate = candidateMapper.findCandidateById(candidateId);
        if (Objects.isNull(candidate)) {
            throw new DataModificationException("The candidate cannot be found");
        }

        Integer projectCandidateId = feedbackMapper.getProjectCandidateRelationship(projectId, candidateId);
        if (Objects.isNull(projectCandidateId)) {
            throw new DataModificationException("The relationship between project and candidate cannot be found");
        }

        List<RubricItem> rubricItems = rubricItemMapper.findRubricItemsByTemplateId(project.getTemplateId());
        if (ResultUtils.isEmpty(rubricItems)) {
            throw new DataModificationException("The rubric items of this project cannot be found");
        }

        FeedbackGeneralVo candidateGeneral = feedbackMapper.getCandidateGeneral(projectCandidateId);

        // output
        FeedbackCandidateVo output = new FeedbackCandidateVo();

        // set basic information
        output.setCandidate(candidate);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(SecurityContextUtil.getCurrentUser(), userVo);
        output.setMarker(userVo);
        output.setSubject(subject);
        output.setProjectTopic(project.getProjectDescription());
        output.setMarkDate(candidateGeneral.getAssessmentDate());
        output.setGeneralFeedback(candidateGeneral.getGeneralComment());

        // set marking information
        double totalMark = 0;
        List<FeedbackItemVo> feedbackItems = new LinkedList<>();
        // feedback items
        for (RubricItem rubricItem : rubricItems) {
            int rubricItemId = rubricItem.getId();

            FeedbackCandidateMark candidateMark = feedbackMapper.getCandidateMarkByRubricId(projectCandidateId, rubricItemId);
            if (Objects.isNull(candidateMark)) {
                throw new DataModificationException("The mark of the rubric item cannot be found");
            }

            MarkSetting markSetting = markSettingMapper.findMarkSettingByRubricItemIdAndTemplateId(rubricItemId,project.getTemplateId());
            if (Objects.isNull(markSetting)) {
                throw new DataModificationException("The mark setting of the rubric item cannot be found");
            }

            FeedbackItemVo feedbackItem = new FeedbackItemVo();
            feedbackItem.setId(rubricItemId);
            feedbackItem.setName(rubricItem.getName());
            double mark = candidateMark.getMark();
            feedbackItem.setMark(mark);
            feedbackItem.setMaximum(markSetting.getMaximum());
            int weighting = markSetting.getWeighting();
            feedbackItem.setWeighting(weighting);
            feedbackItem.setAdditionalComment(candidateMark.getAdditionalComment());
            totalMark += ResultUtils.calculateMark(mark, markSetting.getMaximum(), weighting);

            // feedback sub items
            List<FeedbackSubItemVo> feedbackSubItems = new LinkedList<>();
            List<RubricSubItem> rubricSubItems = rubricSubItemMapper.findRubricSubItemsByItemId(rubricItemId);
            for (RubricSubItem rubricSubItem : rubricSubItems) {
                int rubricSubItemId = rubricSubItem.getId();
                FeedbackCandidateComment candidateComment = feedbackMapper.getCandidateCommentByRubricId(projectCandidateId, rubricSubItemId);
                if (Objects.isNull(candidateComment)) {
                    continue;
                }
                Comment comment = commentMapper.findCommentById(candidateComment.getCommentId());
                if (Objects.isNull(comment)) {
                    throw new DataModificationException("The comment cannot be found");
                }

                FeedbackSubItemVo feedbackSubItem = new FeedbackSubItemVo();
                feedbackSubItem.setId(rubricSubItemId);
                feedbackSubItem.setName(rubricSubItem.getName());
                feedbackSubItem.setComment(comment.getContent());
                feedbackSubItem.setCommentId(comment.getId());
                feedbackSubItems.add(feedbackSubItem);
            }
            feedbackItem.setSubItems(feedbackSubItems);
            feedbackItems.add(feedbackItem);
        }
        output.setTotalMark(totalMark);
        output.setFeedbackItems(feedbackItems);

        return output;
    }

    /**
     * given project id and team id, get feedback
     *
     * @param projectId id of project
     * @param teamId    id of team
     * @return team feedback
     */
    @Override
    public FeedbackTeamVo getTeamFeedback(int projectId, int teamId) {
        // search related information
        Project project = projectMapper.findProjectById(projectId);
        if (Objects.isNull(project)) {
            throw new DataModificationException("The project cannot be found");
        }

        Subject subject = subjectMapper.findSubjectById(project.getSubjectId());
        if (Objects.isNull(subject)) {
            throw new DataModificationException("The subject cannot be found");
        }

        Team team= teamMapper.findTeamById(teamId);
        if (Objects.isNull(team)) {
            throw new DataModificationException("The team cannot be found");
        }

        Integer projectTeamId = feedbackMapper.getProjectTeamRelationship(projectId, teamId);
        if (Objects.isNull(projectTeamId)) {
            throw new DataModificationException("The relationship between project and team cannot be found");
        }

        List<Candidate> candidates = candidateMapper.findCandidatesByTeamId(teamId);
        if (ResultUtils.isEmpty(candidates)) {
            throw new DataModificationException("The candidates of this team cannot be found");
        }

        List<RubricItem> rubricItems = rubricItemMapper.findRubricItemsByTemplateId(project.getTemplateId());
        if (ResultUtils.isEmpty(rubricItems)) {
            System.out.println("1");
            throw new DataModificationException("The rubric items of this project cannot be found");
        }

        FeedbackGeneralVo teamGeneral = feedbackMapper.getTeamGeneral(projectTeamId);

        // output
        FeedbackTeamVo output = new FeedbackTeamVo();

        // set basic information
        output.setTeamName(team.getTeamName());
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(SecurityContextUtil.getCurrentUser(), userVo);
        output.setMarker(userVo);
        output.setSubject(subject);
        output.setProjectTopic(project.getProjectDescription());
        output.setMarkDate(teamGeneral.getAssessmentDate());
        output.setGeneralFeedback(teamGeneral.getGeneralComment());

        // set marking information
        double totalMark = 0;
        List<FeedbackItemVo> feedbackItems = new LinkedList<>();
        // feedback items
        for (RubricItem rubricItem : rubricItems) {
            int rubricItemId = rubricItem.getId();

            FeedbackTeamMark teamMark = feedbackMapper.getTeamMarkByRubricId(projectTeamId, rubricItemId);
            if (Objects.isNull(teamMark)) {
                throw new DataModificationException("The mark of the rubric item cannot be found");
            }

            MarkSetting markSetting = markSettingMapper.findMarkSettingByRubricItemIdAndTemplateId(rubricItemId,project.getTemplateId());
            if (Objects.isNull(markSetting)) {
                throw new DataModificationException("The mark setting of the rubric item cannot be found");
            }

            FeedbackItemVo feedbackItem = new FeedbackItemVo();
            feedbackItem.setId(rubricItemId);
            feedbackItem.setName(rubricItem.getName());
            double mark = teamMark.getMark();
            feedbackItem.setMark(mark);
            feedbackItem.setMaximum(markSetting.getMaximum());
            int weighting = markSetting.getWeighting();
            feedbackItem.setWeighting(weighting);
            feedbackItem.setAdditionalComment(teamMark.getAdditionalComment());
            totalMark += ResultUtils.calculateMark(mark, markSetting.getMaximum(), weighting);

            // feedback sub items
            List<FeedbackSubItemVo> feedbackSubItems = new LinkedList<>();
            List<RubricSubItem> rubricSubItems = rubricSubItemMapper.findRubricSubItemsByItemId(rubricItemId);
            for (RubricSubItem rubricSubItem : rubricSubItems) {
                int rubricSubItemId = rubricSubItem.getId();
                FeedbackTeamComment teamComment = feedbackMapper.getTeamCommentByRubricId(projectTeamId, rubricSubItemId);
                if (Objects.isNull(teamComment)) {
                    continue;
                }
                Comment comment = commentMapper.findCommentById(teamComment.getCommentId());
                if (Objects.isNull(comment)) {
                    throw new DataModificationException("The comment cannot be found");
                }

                FeedbackSubItemVo feedbackSubItem = new FeedbackSubItemVo();
                feedbackSubItem.setId(rubricSubItemId);
                feedbackSubItem.setName(rubricSubItem.getName());
                feedbackSubItem.setComment(comment.getContent());
                feedbackSubItem.setCommentId(comment.getId());
                feedbackSubItems.add(feedbackSubItem);
            }
            feedbackItem.setSubItems(feedbackSubItems);
            feedbackItems.add(feedbackItem);
        }
        output.setTotalMark(totalMark);
        output.setFeedbackItems(feedbackItems);

        List<FeedbackPersonalVo> personalComments = new LinkedList<>();
        for (Candidate candidate : candidates) {
            FeedbackTeamPersonal teamPersonal = feedbackMapper.getTeamPersonalByCandidateId(projectTeamId, candidate.getId());
            if (Objects.isNull(teamPersonal)) {
                continue;
            }

            FeedbackPersonalVo feedbackPersonal = new FeedbackPersonalVo();
            feedbackPersonal.setCandidate(candidate);
            feedbackPersonal.setPersonal(teamPersonal.getFeedback());

            personalComments.add(feedbackPersonal);
        }
        output.setPersonalComments(personalComments);

        return output;
    }

    /**
     * back up current candidate feedback
     *
     * @param projectId   id of project
     * @param candidateId id of candidate
     * @param feedbacks   list of feedback item
     */
    @Override
    public void saveCandidateFeedback(int projectId, int candidateId, List<FeedbackItemVo> feedbacks) {
        Integer projectCandidateId = feedbackMapper.getProjectCandidateRelationship(projectId, candidateId);
        if (Objects.isNull(projectCandidateId)) {
            throw new DataModificationException("The relationship between project and candidate cannot be found");
        }

        Date current = new Date();
        Integer rowAffected;

        for (FeedbackItemVo feedbackItem : feedbacks) {
            int rubricItemId = feedbackItem.getId();
            FeedbackCandidateMark candidateMark = feedbackMapper.getCandidateMarkByRubricId(projectCandidateId, rubricItemId);

            if (Objects.isNull(candidateMark)) {
                candidateMark = new FeedbackCandidateMark();
                candidateMark.setProjectCandidateId(projectCandidateId);
                candidateMark.setRubricItemId(rubricItemId);
                candidateMark.setMark(feedbackItem.getMark());
                candidateMark.setAdditionalComment(feedbackItem.getAdditionalComment());
                candidateMark.setCreateTime(current);
                candidateMark.setUpdateTime(current);

                rowAffected = feedbackMapper.insertCandidateMark(candidateMark);

                if (!ResultUtils.singleCheck(rowAffected)) {
                    throw new DataModificationException("There is an error inserting candidate mark");
                }
            } else {
                candidateMark.setMark(feedbackItem.getMark());
                candidateMark.setAdditionalComment(feedbackItem.getAdditionalComment());
                candidateMark.setUpdateTime(current);

                rowAffected = feedbackMapper.updateCandidateMarkById(candidateMark);

                if (!ResultUtils.singleCheck(rowAffected)) {
                    throw new DataModificationException("There is an error updating candidate mark");
                }
            }

            for (FeedbackSubItemVo feedbackSubItem : feedbackItem.getSubItems()) {
                if (feedbackSubItem.getCommentId() < 0) {
                    // no comment skip
                    continue;
                }

                int rubricSubItemId = feedbackSubItem.getId();
                FeedbackCandidateComment candidateComment = feedbackMapper.getCandidateCommentByRubricId(projectCandidateId, rubricSubItemId);

                if (Objects.isNull(candidateComment)) {
                    candidateComment = new FeedbackCandidateComment();
                    candidateComment.setProjectCandidateId(projectCandidateId);
                    candidateComment.setRubricSubItemId(rubricSubItemId);
                    candidateComment.setCommentId(feedbackSubItem.getCommentId());
                    candidateComment.setCreateTime(current);
                    candidateComment.setUpdateTime(current);

                    rowAffected = feedbackMapper.insertCandidateComment(candidateComment);

                    if (!ResultUtils.singleCheck(rowAffected)) {
                        throw new DataModificationException("There is an error inserting candidate comment");
                    }
                } else {
                    candidateComment.setCommentId(feedbackSubItem.getCommentId());
                    candidateComment.setUpdateTime(current);

                    rowAffected = feedbackMapper.updateCandidateCommentById(candidateComment);

                    if (!ResultUtils.singleCheck(rowAffected)) {
                        throw new DataModificationException("There is an error updating candidate comment");
                    }
                }
            }
        }

        feedbackMapper.setCandidateAssessmentDate(projectCandidateId, current, current);
    }

    /**
     * back up candidate general feedback
     *
     * @param projectId       id of project
     * @param candidateId     id of candidate
     * @param generalFeedback general feedback
     */
    @Override
    public void saveCandidateGeneralFeedback(int projectId, int candidateId, String generalFeedback) {
        Integer projectCandidateId = feedbackMapper.getProjectCandidateRelationship(projectId, candidateId);
        if (Objects.isNull(projectCandidateId)) {
            throw new DataModificationException("The relationship between project and candidate cannot be found");
        }

        Date current = new Date();
        int rowAffected = feedbackMapper.saveCandidateGeneralFeedback(projectCandidateId, generalFeedback, current);

        if (!ResultUtils.singleCheck(rowAffected)) {
            throw new DataModificationException("There is an error updating candidate general feedback");
        }
    }

    /**
     * back up current candidate feedback
     *
     * @param projectId id of project
     * @param teamId    id of team
     * @param feedbacks list of feedback item
     * @return save operation success or not
     */
    @Override
    public void saveTeamFeedback(int projectId, int teamId, List<FeedbackItemVo> feedbacks) {
        Integer projectTeamId = feedbackMapper.getProjectTeamRelationship(projectId, teamId);
        if (Objects.isNull(projectTeamId)) {
            throw new DataModificationException("The relationship between project and team cannot be found");
        }

        Date current = new Date();
        Integer rowAffected;

        for (FeedbackItemVo feedbackItem : feedbacks) {
            int rubricItemId = feedbackItem.getId();
            FeedbackTeamMark teamMark = feedbackMapper.getTeamMarkByRubricId(projectTeamId, rubricItemId);

            if (Objects.isNull(teamMark)) {
                teamMark = new FeedbackTeamMark();
                teamMark.setProjectTeamId(projectTeamId);
                teamMark.setRubricItemId(rubricItemId);
                teamMark.setMark(feedbackItem.getMark());
                teamMark.setAdditionalComment(feedbackItem.getAdditionalComment());
                teamMark.setCreateTime(current);
                teamMark.setUpdateTime(current);

                rowAffected = feedbackMapper.insertTeamMark(teamMark);

                if (!ResultUtils.singleCheck(rowAffected)) {
                    throw new DataModificationException("There is an error inserting team mark");
                }
            } else {
                teamMark.setMark(feedbackItem.getMark());
                teamMark.setAdditionalComment(feedbackItem.getAdditionalComment());
                teamMark.setUpdateTime(current);

                rowAffected = feedbackMapper.updateTeamMarkById(teamMark);

                if (!ResultUtils.singleCheck(rowAffected)) {
                    throw new DataModificationException("There is an error updating team mark");
                }
            }

            for (FeedbackSubItemVo feedbackSubItem : feedbackItem.getSubItems()) {
                if (feedbackSubItem.getCommentId() < 0) {
                    // no comment skip
                    continue;
                }

                int rubricSubItemId = feedbackSubItem.getId();
                FeedbackTeamComment teamComment = feedbackMapper.getTeamCommentByRubricId(projectTeamId, rubricSubItemId);

                if (Objects.isNull(teamComment)) {
                    teamComment = new FeedbackTeamComment();
                    teamComment.setProjectTeamId(projectTeamId);
                    teamComment.setRubricSubItemId(rubricSubItemId);
                    teamComment.setCommentId(feedbackSubItem.getCommentId());
                    teamComment.setCreateTime(current);
                    teamComment.setUpdateTime(current);

                    rowAffected = feedbackMapper.insertTeamComment(teamComment);

                    if (!ResultUtils.singleCheck(rowAffected)) {
                        throw new DataModificationException("There is an error inserting team comment");
                    }
                } else {
                    teamComment.setCommentId(feedbackSubItem.getCommentId());
                    teamComment.setUpdateTime(current);

                    rowAffected = feedbackMapper.updateTeamCommentById(teamComment);

                    if (!ResultUtils.singleCheck(rowAffected)) {
                        throw new DataModificationException("There is an error updating team mark");
                    }
                }
            }
        }

        feedbackMapper.setTeamAssessmentDate(projectTeamId, current, current);
    }

    /**
     * back up current personal feedback
     *
     * @param projectId         id of project
     * @param teamId            id of team
     * @param personalFeedbacks list of personal feedback
     */
    @Override
    public void savePersonalFeedback(int projectId, int teamId, List<FeedbackTeamPersonal> personalFeedbacks) {
        Integer projectTeamId = feedbackMapper.getProjectTeamRelationship(projectId, teamId);
        if (Objects.isNull(projectTeamId)) {
            throw new DataModificationException("The relationship between project and team cannot be found");
        }

        Date current = new Date();
        Integer rowAffected;

        for (FeedbackTeamPersonal personalFeedback : personalFeedbacks) {
            int candidateId = personalFeedback.getCandidateId();
            FeedbackTeamPersonal entity = feedbackMapper.getTeamPersonalByCandidateId(projectTeamId, candidateId);

            if (Objects.isNull(entity)) {
                entity = new FeedbackTeamPersonal();
                entity.setProjectTeamId(projectTeamId);
                entity.setCandidateId(candidateId);
                entity.setFeedback(personalFeedback.getFeedback());
                entity.setCreateTime(current);
                entity.setUpdateTime(current);

                rowAffected = feedbackMapper.insertTeamPersonal(entity);
                if (!ResultUtils.singleCheck(rowAffected)) {
                    throw new DataModificationException("There is an error inserting personal feedback");
                }
            } else {
                entity.setFeedback(personalFeedback.getFeedback());
                entity.setUpdateTime(current);

                rowAffected = feedbackMapper.updateTeamPersonalById(entity);
                if (!ResultUtils.singleCheck(rowAffected)) {
                    throw new DataModificationException("There is an error updating personal feedback");
                }
            }
        }
    }

    /**
     * back up team general feedback
     *
     * @param projectId       id of project
     * @param teamId          id of team
     * @param generalFeedback general feedback
     */
    @Override
    public void saveTeamGeneralFeedback(int projectId, int teamId, String generalFeedback) {
        Integer projectTeamId = feedbackMapper.getProjectTeamRelationship(projectId, teamId);
        if (Objects.isNull(projectTeamId)) {
            throw new DataModificationException("The relationship between project and candidate cannot be found");
        }

        Date current = new Date();
        int rowAffected = feedbackMapper.saveTeamGeneralFeedback(projectTeamId, generalFeedback, current);

        if (!ResultUtils.singleCheck(rowAffected)) {
            throw new DataModificationException("There is an error updating team general feedback");
        }
    }

    /**
     * delete all project candidate feedbacks by given id
     *
     * @param projectId   id of project
     * @param candidateId id of candidate
     */
    @Override
    public void clearCandidateFeedback(int projectId, int candidateId) {
        Integer projectCandidateId = feedbackMapper.getProjectCandidateRelationship(projectId, candidateId);
        if (Objects.isNull(projectCandidateId)) {
            throw new DataModificationException("The relationship between project and candidate cannot be found");
        }

        Date current = new Date();

        feedbackMapper.clearCandidateCommentByMidId(projectCandidateId, current);
        feedbackMapper.clearCandidateMarkByMidId(projectCandidateId, current);
        feedbackMapper.saveCandidateGeneralFeedback(projectCandidateId, null, current);
    }

    /**
     * delete all team candidate feedbacks by given id
     *
     * @param projectId id of project
     * @param teamId    id of team
     */
    @Override
    public void clearTeamFeedback(int projectId, int teamId) {
        Integer projectTeamId = feedbackMapper.getProjectTeamRelationship(projectId, teamId);
        if (Objects.isNull(projectTeamId)) {
            throw new DataModificationException("The relationship between project and team cannot be found");
        }

        Date current = new Date();

        feedbackMapper.clearTeamCommentByMidId(projectTeamId, current);
        feedbackMapper.clearTeamMarkByMidId(projectTeamId, current);
        feedbackMapper.clearTeamPersonalByMidId(projectTeamId, current);
        feedbackMapper.saveTeamGeneralFeedback(projectTeamId, null, current);
    }

    @Override
    public boolean checkCandidateMark(int projectId, int candidateId) {
        Integer projectCandidateId = feedbackMapper.getProjectCandidateRelationship(projectId, candidateId);
        if (Objects.isNull(projectCandidateId)) {
            throw new DataModificationException("The relationship between project and candidate cannot be found");
        }
        List<FeedbackCandidateMark> candidateMarks = feedbackMapper.getCandidateMarkByMidId(projectCandidateId);
        List<FeedbackCandidateComment> candidateComments = feedbackMapper.getCandidateCommentByMidId(projectCandidateId);

        return !ResultUtils.isEmpty(candidateMarks) || !ResultUtils.isEmpty(candidateComments);
    }

    @Override
    public boolean checkTeamMark(int projectId, int teamId) {
        Integer projectTeamId = feedbackMapper.getProjectTeamRelationship(projectId, teamId);
        if (Objects.isNull(projectTeamId)) {
            throw new DataModificationException("The relationship between project and team cannot be found");
        }
        List<FeedbackTeamMark> teamMarks = feedbackMapper.getTeamMarkByMidId(projectTeamId);
        List<FeedbackTeamComment> teamComments = feedbackMapper.getTeamCommentByMidId(projectTeamId);
        List<FeedbackTeamPersonal> teamPersonals = feedbackMapper.getTeamPersonalByMidId(projectTeamId);
        return !ResultUtils.isEmpty(teamMarks) || !ResultUtils.isEmpty(teamComments) || !ResultUtils.isEmpty(teamPersonals);
    }
}
