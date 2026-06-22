package com.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostVO {
    private Long postId;
    private Long userId;
    private String userName;
    private String userAvatar;
    private String title;
    private String content;
    private String coverImg;
    private List<String> tagNameList;
    private Integer viewCount;
    private LocalDateTime createTime;
}
