package com.campusconnect.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendResultVO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 推荐类型: USER-匹配用户, POST-匹配帖子
    private String type;
    
    // 数据主体(UserVO 或 PostVO)
    private Object entity;

    // 匹配度 (0.00 ~ 1.00)
    private Double matchRate;

    // 推荐原因说明
    private String recommendReason;
}