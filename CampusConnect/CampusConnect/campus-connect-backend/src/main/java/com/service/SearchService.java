package com.campusconnect.service;

import com.campusconnect.vo.SearchResultVO;

public interface SearchService {
    SearchResultVO searchAll(String keyword, Long currentUserId);
}