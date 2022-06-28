package com.feredback.feredback_backend.entity.vo;

import com.feredback.feredback_backend.entity.Comment;
import lombok.Data;

/**
 * @program: FE-Redback
 * @description:
 * @author: Xun Zhang (854776)
 * @date: 2022/5/8
 **/
@Data
public class CommentVo {
    /**
     * id of comment instance
     */
    private Integer id;

    /**
     * content of comment instance
     */
    private String content;

    /**
     * level of comment instance
     */
    private String level;

    public CommentVo() {
    }

    public CommentVo(Comment comment) {
        id = comment.getId();
        content = comment.getContent();

        switch (comment.getLevel()) {
            case 1:
                level = "positive";
                break;
            case 0:
                level = "neutral";
                break;
            case -1:
                level = "negative";
                break;
        }
    }
}
