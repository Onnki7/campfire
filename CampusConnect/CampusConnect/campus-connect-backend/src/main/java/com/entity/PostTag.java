package com.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("post_tag")
public class PostTag {
    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 标签id
     */
    private Long tagId;
}
