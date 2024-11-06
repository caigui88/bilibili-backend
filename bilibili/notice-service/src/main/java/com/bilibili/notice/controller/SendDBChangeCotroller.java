package com.bilibili.notice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.bilibili.notice.service.SendDBChangeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Map;
@RestController
@RequestMapping("/DBChange")
public class SendDBChangeCotroller {
    @Resource
    SendDBChangeService sendDBChangeService;
    @ApiIgnore
    @PostMapping("/sendDBChangeNotice")
    public void sendDBChangeNotice(@RequestBody Map<String,Object> map) throws JsonProcessingException {
    sendDBChangeService.sendDBChangeNotice(map);
    }
}
