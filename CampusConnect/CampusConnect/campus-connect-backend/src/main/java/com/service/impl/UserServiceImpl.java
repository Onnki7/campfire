package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dto.LoginRequest;
import com.dto.RegisterRequest;
import com.dto.UserUpdateRequest;
import com.entity.Tag;
import com.entity.User;
import com.entity.UserTag;
import com.exception.CustomException;
import com.mapper.TagMapper;
import com.mapper.UserMapper;
import com.mapper.UserTagMapper;
import com.security.JwtTokenProvider;
import com.service.UserService;
import com.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserTagMapper userTagMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        return tokenProvider.generateToken(authentication);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername())) > 0) {
            throw new CustomException("用户名已被注册");
        }
        if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getEmail, request.getEmail())) > 0) {
            throw new CustomException("邮箱已被使用");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .nickname(request.getNickname())
                .avatar(request.getAvatar())
                .signature(request.getSignature())
                .role("USER")
                .status(1)
                .build();

        userMapper.insert(user);

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            for (Long tagId : request.getTagIds()) {
                UserTag ut = UserTag.builder().userId(user.getId()).tagId(tagId).build();
                userTagMapper.insert(ut);
            }
        }
    }

    @Override
    public UserVO getCurrentUser(Long currentUserId) {
        User user = userMapper.selectById(currentUserId);
        if (user == null) {
            throw new CustomException("用户不存在");
        }
        List<Tag> tags = tagMapper.selectTagsByUserId(currentUserId);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateUser(Long currentUserId, UserUpdateRequest request) {
        User user = userMapper.selectById(currentUserId);
        if (user == null) {
            throw new CustomException("用户不存在");
        }

        // 校验邮箱冲突
        Long emailCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, request.getEmail())
                .ne(User::getId, currentUserId));
        if (emailCount > 0) {
            throw new CustomException("邮箱已被占用");
        }

        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        user.setSignature(request.getSignature());
        userMapper.updateById(user);

        // 重构标签关系
        userTagMapper.delete(new LambdaQueryWrapper<UserTag>().eq(UserTag::getUserId, currentUserId));
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            for (Long tagId : request.getTagIds()) {
                UserTag ut = UserTag.builder().userId(currentUserId).tagId(tagId).build();
                userTagMapper.insert(ut);
            }
        }

        List<Tag> tags = tagMapper.selectTagsByUserId(currentUserId);
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
}