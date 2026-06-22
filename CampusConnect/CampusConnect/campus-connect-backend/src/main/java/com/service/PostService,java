package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dto.PostRequest;
import com.entity.Post;
import com.vo.PostVO;
import java.util.List;

public interface PostService extends IService<Post> {
    /**
     * 发布帖子
     */
    Long publishPost(PostRequest request, Long loginUserId);

    /**
     * 根据id获取帖子详情
     */
    PostVO getPostDetail(Long postId, Long userId);

    /**
     * 分页查询帖子列表
     */
    List<PostVO> listPost(Long pageNum, Long pageSize);

    /**
     * 记录帖子浏览记录
     */
    void addViewRecord(Long postId, Long userId);
}
