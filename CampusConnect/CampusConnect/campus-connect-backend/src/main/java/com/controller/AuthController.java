package com.controller;

import com.dto.LoginRequest;
import com.dto.RegisterRequest;
import com.dto.UserUpdateRequest;
import com.security.UserPrincipal;
import com.service.ImageService;
import com.service.UserService;
import com.vo.Result;
import com.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return Result.success("登录成功", data);
    }

    @GetMapping("/me")
    public Result<UserVO> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        UserVO userVO = userService.getCurrentUser(principal.getId());
        return Result.success(userVO);
    }

    @PutMapping("/me")
    public Result<UserVO> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                        @Valid @RequestBody UserUpdateRequest request) {
        UserVO updated = userService.updateUser(principal.getId(), request);
        return Result.success("个人资料更新成功", updated);
    }

    @PostMapping("/upload")
    public Result<String> uploadAvatar(@AuthenticationPrincipal UserPrincipal principal,
                                       @RequestParam("file") MultipartFile file) {
        String url = imageService.uploadImage(file, principal.getId());
        return Result.success("上传成功", url);
    }
}
