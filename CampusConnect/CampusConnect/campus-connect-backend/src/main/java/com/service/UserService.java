package com.service;

import com.dto.LoginRequest;
import com.dto.RegisterRequest;
import com.dto.UserUpdateRequest;
import com.vo.UserVO;

public interface UserService {
    String login(LoginRequest request);
    void register(RegisterRequest request);
    UserVO getCurrentUser(Long currentUserId);
    UserVO updateUser(Long currentUserId, UserUpdateRequest request);
}