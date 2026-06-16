package com.campusconnect.controller;

import com.campusconnect.security.UserPrincipal;
import com.campusconnect.service.RecommendationService;
import com.campusconnect.vo.RecommendResultVO;
import com.campusconnect.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendController {

    @Autowired
    @Qualifier("ruleBasedRecommendationService")
    private RecommendationService ruleBasedService;

    @Autowired
    @Qualifier("aiRecommendationService")
    private RecommendationService aiService;

    @GetMapping
    public Result<List<RecommendResultVO>> getRecommendations(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "rule") String strategy,
            @AuthenticationPrincipal UserPrincipal principal) {

        RecommendationService selectedService = "ai".equalsIgnoreCase(strategy) ? aiService : ruleBasedService;
        List<RecommendResultVO> list = selectedService.recommend(principal.getId(), limit);
        return Result.success(list);
    }
}