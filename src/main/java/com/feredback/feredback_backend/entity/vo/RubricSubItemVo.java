package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.RubricSubItem;
import lombok.Data;

import java.util.List;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/11
 **/
@Data
public class RubricSubItemVo {
    private RubricSubItem rubricSubItem;
    private List<CommentVo> comments;
}
