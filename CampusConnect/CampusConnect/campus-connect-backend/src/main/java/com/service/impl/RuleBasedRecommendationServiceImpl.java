package com.campusconnect.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campusconnect.entity.*;
import com.campusconnect.mapper.*;
import com.campusconnect.service.RecommendationService;
import com.campusconnect.vo.PostVO;
import com.campusconnect.vo.RecommendResultVO;
import com.campusconnect.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("ruleBasedRecommendationService")
@Primary // 默认首选规则推荐策略
public class RuleBasedRecommendationServiceImpl implements RecommendationService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserTagMapper userTagMapper;

    @Autowired
    private PostTagMapper postTagMapper;

    @Override
    public List<RecommendResultVO> recommend(Long userId, int limit) {
        List<RecommendResultVO> results = new ArrayList<>();

        // 获取当前用户的兴趣标签
        List<Tag> userTags = tagMapper.selectTagsByUserId(userId);
        if (userTags.isEmpty()) {
            // 无标签时，默认兜底推荐高热度帖子
            List<Post> hotPosts = postMapper.selectList(new LambdaQueryWrapper<Post>()
                    .orderByDesc(Post::getHeat)
                    .last("LIMIT " + limit));
            for (Post post : hotPosts) {
                results.add(RecommendResultVO.builder()
                        .type("POST")
                        .entity(convertToPostVO(post))
                        .matchRate(0.50)
                        .recommendReason("系统精选热门推荐")
                        .build());
            }
            return results;
        }

        Set<Long> userTagIds = userTags.stream().map(Tag::getId).collect(Collectors.toSet());

        // 1. 匹配度相似的用户推荐 (寻找有相同标签的用户)
        List<User> otherUsers = userMapper.selectList(new LambdaQueryWrapper<User>()
                .ne(User::getId, userId)
                .last("LIMIT 100"));

        for (User other : otherUsers) {
            List<Tag> otherTags = tagMapper.selectTagsByUserId(other.getId());
            Set<Long> otherTagIds = otherTags.stream().map(Tag::getId).collect(Collectors.toSet());

            // 计算交集
            Set<Long> intersection = new HashSet<>(userTagIds);
            intersection.retainAll(otherTagIds);

            if (!intersection.isEmpty()) {
                double matchRate = (double) intersection.size() / Math.max(userTagIds.size(), otherTagIds.size());
                String matchedTagName = otherTags.stream()
                        .filter(t -> userTagIds.contains(t.getId()))
                        .map(Tag::getName)
                        .collect(Collectors.joining("、"));

                results.add(RecommendResultVO.builder()
                        .type("USER")
                        .entity(convertToUserVO(other, otherTags))
                        .matchRate(matchRate)
                        .recommendReason("Ta也对 [" + matchedTagName + "] 同样感兴趣")
                        .build());
            }
        }

        // 2. 相似帖子推荐 (匹配包含用户标签的帖子)
        List<Post> allPosts = postMapper.selectList(new LambdaQueryWrapper<Post>()
                .ne(Post::getUserId, userId)
                .last("LIMIT 100"));

        for (Post post : allPosts) {
            List<Tag> postTags = tagMapper.selectTagsByPostId(post.getId());
            Set<Long> postTagIds = postTags.stream().map(Tag::getId).collect(Collectors.toSet());

            Set<Long> intersection = new HashSet<>(userTagIds);
            intersection.retainAll(postTagIds);

            if (!intersection.isEmpty()) {
                double matchRate = (double) intersection.size() / Math.max(userTagIds.size(), postTagIds.size());
                String matchedTagName = postTags.stream()
                        .filter(t -> userTagIds.contains(t.getId()))
                        .map(Tag::getName)
                        .collect(Collectors.joining("、"));

                results.add(RecommendResultVO.builder()
                        .type("POST")
                        .entity(convertToPostVO(post))
                        .matchRate(matchRate)
                        .recommendReason("该帖子包含了您感兴趣的 [" + matchedTagName + "] 标签")
                        .build());
            }
        }

        // 排序与限流
        return results.stream()
                .sorted((r1, r2) -> Double.compare(r2.getMatchRate(), r1.getMatchRate()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private UserVO convertToUserVO(User user, List<Tag> tags) {
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
    }

    private PostVO convertToPostVO(Post post) {
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
    }
}