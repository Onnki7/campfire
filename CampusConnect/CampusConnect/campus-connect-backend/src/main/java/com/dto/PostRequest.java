package com.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class PostRequest {
    /**
     * 帖子标题
     */
    @NotBlank(message = "帖子标题不能为空")
    private String title;

    /**
     * 帖子内容
     */
    @NotBlank(message = "帖子内容不能为空")
    private String content;

    /**
     * 封面图
     */
    private String coverImg;

    /**
     * 标签id集合
     */
    @NotEmpty(message = "至少选择一个标签")
    private List<Long> tagIdList;
}
