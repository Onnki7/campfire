package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("post")
public class Post {
    /**
     * 帖子主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布用户id
     */
    private Long userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子正文内容
     */
    private String content;

    /**
     * 帖子封面图片
     */
    private String coverImg;

    /**
     * 帖子状态 0正常 1下架
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
