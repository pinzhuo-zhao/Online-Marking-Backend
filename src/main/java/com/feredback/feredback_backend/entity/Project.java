package com.feredback.feredback_backend.entity;

import lombok.Data;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-15 20:43
 **/
@Data
public class Project extends BaseEntity {
    private Integer id;
    private String projectDescription;
    private Integer isIndividual;
    private Integer subjectId;
    private Integer templateId;
    private Integer duration;
    private Integer warningTime;



}
