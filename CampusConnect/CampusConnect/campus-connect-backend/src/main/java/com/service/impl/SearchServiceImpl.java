package com.campusconnect.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campusconnect.entity.Post;
import com.campusconnect.entity.Tag;
import com.campusconnect.entity.User;
import com.campusconnect.entity.UserTag;
import com.campusconnect.mapper.PostMapper;
import com.campusconnect.mapper.TagMapper;
import com.campusconnect.mapper.UserMapper;
import com.campusconnect.service.SearchService;
import com.campusconnect.vo.PostVO;
import com.campusconnect.vo.SearchResultVO;
import com.campusconnect.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TagMapper tagMapper;

    @Override
    public SearchResultVO searchAll(String keyword, Long currentUserId) {
        // 1. 搜索帖子 (基于 LIKE)
        List<Post> posts = postMapper.selectList(new LambdaQueryWrapper<Post>()
                .like(Post::getTitle, keyword)
                .or()
                .like(Post::getContent, keyword)
                .orderByDesc(Post::getCreatedAt));

        List<PostVO> postVOs = posts.stream().map(post -> {
            User author = userMapper.selectById(post.getUserId());
            List<Tag> tags = tagMapper.selectTagsByPostId(post.getId());
            return PostVO.builder()
                    .id(post.getId())
                    .userId(post.getUserId())
                    .authorName(author != null ? author.getNickname() : "已注销用户")
                    .authorAvatar(author != null ? author.getAvatar() : null)
                    .title(post.getTitle())
                    .content(post.getContent())
                    .coverImage(post.getCoverImage())
                    .viewCount(post.getViewCount())
                    .likeCount(post.getLikeCount())
                    .commentCount(post.getCommentCount())
                    .favoriteCount(post.getFavoriteCount())
                    .heat(post.getHeat())
                    .tags(tags)
                    .createdAt(post.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());

        // 2. 搜索用户 (基于 LIKE)
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>()
                .like(User::getNickname, keyword)
                .or()
                .like(User::getUsername, keyword));

        List<UserVO> userVOs = users.stream().map(user -> {
            List<Tag> tags = tagMapper.selectTagsByUserId(user.getId());
            return UserVO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .avatar(user.getAvatar())
                    .signature(user.getSignature())
                    .role(user.getRole())
                    .tags(tags)
                    .createdAt(user.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());

        return SearchResultVO.builder()
                .posts(postVOs)
                .users(userVOs)
                .build();
    }
}