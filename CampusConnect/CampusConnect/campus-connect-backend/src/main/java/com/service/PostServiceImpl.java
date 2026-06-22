package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.PostRequest;
import com.entity.Post;
import com.entity.PostTag;
import com.entity.PostView;
import com.entity.Tag;
import com.entity.User;
import com.mapper.PostMapper;
import com.mapper.PostTagMapper;
import com.mapper.PostViewMapper;
import com.mapper.TagMapper;
import com.mapper.UserMapper;
import com.service.PostService;
import com.vo.PostVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Resource
    private PostTagMapper postTagMapper;
    @Resource
    private PostViewMapper postViewMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TagMapper tagMapper;

    @Override
    public Long publishPost(PostRequest request, Long loginUserId) {
        // 1. 保存帖子主体
        Post post = new Post();
        post.setUserId(loginUserId);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCoverImg(request.getCoverImg());
        post.setStatus(0);
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(post);
        Long postId = post.getId();

        // 2. 批量插入帖子标签关联
        List<PostTag> tagList = new ArrayList<>();
        for (Long tagId : request.getTagIdList()) {
            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            tagList.add(postTag);
        }
        postTagMapper.insertBatch(tagList);
        return postId;
    }

    @Override
    public PostVO getPostDetail(Long postId, Long userId) {
        Post post = baseMapper.selectById(postId);
        User user = userMapper.selectById(post.getUserId());

        // 查询标签名称
        LambdaQueryWrapper<PostTag> tagWrapper = new LambdaQueryWrapper<>();
        tagWrapper.eq(PostTag::getPostId, postId);
        List<PostTag> postTags = postTagMapper.selectList(tagWrapper);
        List<Long> tagIds = postTags.stream().map(PostTag::getTagId).collect(Collectors.toList());
        List<Tag> tagList = tagMapper.selectBatchIds(tagIds);
        List<String> tagNames = tagList.stream().map(Tag::getName).collect(Collectors.toList());

        // 统计浏览量
        LambdaQueryWrapper<PostView> viewWrapper = new LambdaQueryWrapper<>();
        viewWrapper.eq(PostView::getPostId, postId);
        Integer viewCount = Math.toIntExact(postViewMapper.selectCount(viewWrapper));

        // 组装VO
        PostVO vo = new PostVO();
        vo.setPostId(post.getId());
        vo.setUserId(post.getUserId());
        vo.setUserName(user.getUsername());
        vo.setUserAvatar(user.getAvatar());
        vo.setTitle(post.getTitle());
        vo.setContent(post.getContent());
        vo.setCoverImg(post.getCoverImg());
        vo.setTagNameList(tagNames);
        vo.setViewCount(viewCount);
        vo.setCreateTime(post.getCreateTime());
        return vo;
    }

    @Override
    public List<PostVO> listPost(Long pageNum, Long pageSize) {
        Page<Post> page = new Page<>(pageNum, pageSize);
        Page<Post> postPage = baseMapper.selectPage(page, null);
        List<Post> postList = postPage.getRecords();
        List<PostVO> voList = new ArrayList<>();
        for (Post post : postList) {
            PostVO vo = getPostDetail(post.getId(), null);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public void addViewRecord(Long postId, Long userId) {
        PostView view = new PostView();
        view.setPostId(postId);
        view.setUserId(userId);
        view.setViewTime(LocalDateTime.now());
        postViewMapper.insert(view);
    }
}
