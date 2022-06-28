package com.feredback.feredback_backend.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName candidate
 */
@Data
public class Candidate extends BaseEntity {
    /**
     * 
     */
    @ApiModelProperty(required = true,notes = "!!")
    private Integer id;

    /**
     * 
     */
    @ApiModelProperty(required = true,notes = "!!")
    private String firstName;

    /**
     * 
     */
    @ApiModelProperty(required = true,notes = "!!")
    private String middleName;

    /**
     * 
     */
    @ApiModelProperty(required = true,notes = "!!")
    private String lastName;

    /**
     * 
     */
    @ApiModelProperty(required = true,notes = "!!")
    private String email;





}