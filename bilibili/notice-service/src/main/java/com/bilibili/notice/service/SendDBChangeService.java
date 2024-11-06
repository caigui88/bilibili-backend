package com.bilibili.notice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface SendDBChangeService {
     void sendDBChangeNotice(Map<String, Object> map) throws JsonProcessingException;
}
