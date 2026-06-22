package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("post_view")
public class PostView {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 浏览帖子id
     */
    private Long postId;

    /**
     * 浏览用户id
     */
    private Long userId;

    /**
     * 浏览时间
     */
    private LocalDateTime viewTime;
}
