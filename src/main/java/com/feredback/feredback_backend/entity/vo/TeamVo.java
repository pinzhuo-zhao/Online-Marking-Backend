package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.Team;
import lombok.Data;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-18 23:37
 **/
@Data
public class TeamVo {
    private Team team;
    private List<CandidateVo> candidates;
}
