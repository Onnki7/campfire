package com.campusconnect.service.impl;

import com.campusconnect.service.RecommendationService;
import com.campusconnect.vo.RecommendResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("aiRecommendationService")
public class AIRecommendationServiceImpl implements RecommendationService {

    @Override
    public List<RecommendResultVO> recommend(Long userId, int limit) {
        log.info("AI 推荐架构预留接口已就绪。未来接入：DeepSeek / OpenAI / Qwen 平台");
        // 返回一个预留提示信息，代表策略模式切换通路可用
        return new ArrayList<>();
    }
}