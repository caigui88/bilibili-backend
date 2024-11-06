package com.bilibili.search.service;

import com.bilibili.common.domain.pojo.RecommendVideo;
import com.bilibili.common.util.Result;
import com.bilibili.search.domain.dto.EsIndexDTO;
import com.bilibili.search.domain.dto.EsKeywordDTO;
import com.bilibili.search.domain.vo.TotalCountSearchVO;
import com.bilibili.search.domain.vo.UserKeyWordSearchVO;
import com.bilibili.search.domain.vo.VideoKeywordSearchVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface SearchService{
    Result<TotalCountSearchVO> totalKeywordSearch(String keyword);
    Result<Boolean> addKeywordSearchRecord(EsKeywordDTO esKeywordDTO) throws IOException;
    Result<List<VideoKeywordSearchVO>> videoPageKeywordSearch(String keyword, int pageNumber, Integer type) throws JsonProcessingException;
    Result<List<UserKeyWordSearchVO>> userPageKeywordSearch(String keyword, int pageNumber, Integer type, Integer userId) throws JsonProcessingException;
    Result<List<String>> likelyKeywordSearch(String searchWord) throws IOException;
    List<RecommendVideo> likelyVideoRecommend(String videoId) throws IOException;
    Boolean createIndex(EsIndexDTO esIndexDTO);
    Boolean deleteIndex(String indexName) throws IOException;
}
