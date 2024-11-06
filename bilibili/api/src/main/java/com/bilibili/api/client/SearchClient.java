package com.bilibili.api.client;

import com.bilibili.common.domain.pojo.RecommendVideo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Component
@FeignClient(name = "search-service")
public interface SearchClient {
    @GetMapping("/search/likelyVideoRecommend/{videoId}")
    List<RecommendVideo> getRecommendVideo(@PathVariable String videoId);
}
