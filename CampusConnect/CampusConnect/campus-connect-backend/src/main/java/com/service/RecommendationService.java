package com.campusconnect.service;

import com.campusconnect.vo.RecommendResultVO;
import java.util.List;

public interface RecommendationService {
    List<RecommendResultVO> recommend(Long userId, int limit);
}