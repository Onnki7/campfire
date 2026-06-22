package com.controller;

import com.dto.PostRequest;
import com.result.Result;
import com.service.PostService;
import com.vo.PostVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import com.util.UserUtil;

@RestController
@RequestMapping("/post")
@Api(tags = "帖子模块接口")
public class PostController {

    @Resource
    private PostService postService;

    @PostMapping("/publish")
    @ApiOperation("发布帖子")
    @PreAuthorize("isAuthenticated()")
    public Result<Long> publish(@Valid @RequestBody PostRequest request) {
        Long userId = UserUtil.getLoginUserId();
        Long postId = postService.publishPost(request, userId);
        return Result.success(postId);
    }

    @GetMapping("/detail/{postId}")
    @ApiOperation("获取帖子详情")
    public Result<PostVO> getDetail(@PathVariable Long postId) {
        Long userId = UserUtil.getLoginUserId();
        PostVO postVO = postService.getPostDetail(postId, userId);
        return Result.success(postVO);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询帖子列表")
    public Result<List<PostVO>> list(@RequestParam(defaultValue = "1") Long pageNum,
                                     @RequestParam(defaultValue = "10") Long pageSize) {
        List<PostVO> voList = postService.listPost(pageNum, pageSize);
        return Result.success(voList);
    }

    @PostMapping("/view/{postId}")
    @ApiOperation("记录帖子浏览")
    @PreAuthorize("isAuthenticated()")
    public Result<String> addView(@PathVariable Long postId) {
        Long userId = UserUtil.getLoginUserId();
        postService.addViewRecord(postId, userId);
        return Result.success("浏览记录添加成功");
    }
}
