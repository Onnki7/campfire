package com.campusconnect.controller;

import com.campusconnect.security.UserPrincipal;
import com.campusconnect.service.SearchService;
import com.campusconnect.vo.Result;
import com.campusconnect.vo.SearchResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public Result<SearchResultVO> search(@RequestParam String keyword, 
                                         @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = (principal != null) ? principal.getId() : null;
        SearchResultVO result = searchService.searchAll(keyword, userId);
        return Result.success(result);
    }
}