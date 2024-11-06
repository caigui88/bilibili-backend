package com.bilibili.api.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "chat-service")
public interface ChatClient {
}
