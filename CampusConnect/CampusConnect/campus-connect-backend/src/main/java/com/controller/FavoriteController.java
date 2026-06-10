package com.controller;

import com.security.UserPrincipal;
import com.service.FavoriteService;
import com.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites/{postId}")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public Result<Map<String, Boolean>> toggleFavorite(@PathVariable Long postId,
                                                       @AuthenticationPrincipal UserPrincipal principal) {
        boolean favorited = favoriteService.toggleFavorite(postId, principal.getId());
        Map<String, Boolean> res = new HashMap<>();
        res.put("favorited", favorited);
        return Result.success(res);
    }
}