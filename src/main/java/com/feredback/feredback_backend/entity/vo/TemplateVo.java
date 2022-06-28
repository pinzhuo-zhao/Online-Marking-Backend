package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.RubricItem;
import com.feredback.feredback_backend.entity.Template;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-19 19:46
 **/
@Data
public class TemplateVo implements Serializable {
    private Template template;
    private List<RubricItemVo> rubricItems;
    private Integer projectId;
    private Integer duration;
    private Integer warningTime;

}
