package com.bilibili.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bilibili.chat.domain.dto.ChatSessionDTO;
import com.bilibili.chat.domain.vo.*;
import com.bilibili.chat.handler.PPTHandler;
import com.bilibili.chat.mapper.ChatServiceMapper;
import com.bilibili.chat.ppt.ApiAuthAlgorithm;
import com.bilibili.chat.domain.entity.NoticeCount;
import com.bilibili.chat.domain.entity.PPTWord;
import com.bilibili.chat.ppt.ApiClient;
import com.bilibili.chat.ppt.MyUtil;
import com.bilibili.chat.service.ChatService;
import com.bilibili.chat.domain.dto.ChangeChatStatusDTO;
import com.bilibili.common.domain.entity.chat.Chat;
import com.bilibili.common.domain.entity.user.User;
import com.bilibili.common.domain.entity.chat.ChatSession;
import com.bilibili.common.mapper.chat.ChatMapper;
import com.bilibili.common.mapper.chat.ChatSessionMapper;
import com.bilibili.common.mapper.user.UserMapper;
import com.bilibili.common.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.bilibili.chat.constant.Constant.*;


/**
 *聊天service
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    @Resource
    ChatSessionMapper chatSessionMapper;
    @Resource
    ChatMapper chatMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ChatServiceMapper chatServiceMapper;
    /**
     *修改私聊消息状态
     */
    @Override
    public Result<Boolean> changeChatStatus(ChangeChatStatusDTO changeChatStatusDTO) {
        LambdaUpdateWrapper<Chat> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Chat::getStatus, 1);
        wrapper.eq(Chat::getSenderId, changeChatStatusDTO.getReceiverId());
        wrapper.eq(Chat::getReceiverId, changeChatStatusDTO.getUserId());
        chatMapper.update(null, wrapper);
        return Result.success(true);
    }
    /**
     *创建临时会话
     */
    @Override
    public Result<TempSessionVO> createTempSession(Integer receiverId){
        User u=userMapper.selectById(receiverId);
        return Result.data(new TempSessionVO().setCover(u.getCover()).setNickName(u.getNickname()));
    }
    /**
     *修改回话的最后聊天时间和内容
     */
    @Override
    public Result<Boolean> changeChatSessionTime(ChatSessionDTO chatSessionDTO) {
        LambdaUpdateWrapper<ChatSession> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ChatSession::getSenderId, chatSessionDTO.getSenderId());
        wrapper.eq(ChatSession::getReceiverId, chatSessionDTO.getReceiverId());
        wrapper.set(ChatSession::getUpdateTime, LocalDateTime.now());
        wrapper.set(ChatSession::getUpdateContent, chatSessionDTO.getUpdateContent());
        chatSessionMapper.update(null, wrapper);
        return Result.success(true);
    }
    /**
     *获取历史会话列表
     */
    @Override
    public Result<List<ChatSessionVO>> getHistoryChatSession(Integer userId) {
        List<ChatSessionVO> selfResponses= chatServiceMapper.getSelfSession(userId);
        List<ChatSessionVO> otherResponses= chatServiceMapper.getOtherSession(userId);
        selfResponses.addAll(otherResponses);
        List<Integer> idList=new ArrayList<>();
        //获取会话集合的id集合
        for(ChatSessionVO sessionResponse : selfResponses){
            idList.add(sessionResponse.getSessionId());
        }
        if(idList.size()>0){
            //获取每个会话的未读消息数和设置每个会话的未读状态
            List<NoticeCount> noticeCounts= chatServiceMapper.getNoticeCounts(idList,userId);
            for(ChatSessionVO sessionResponse : selfResponses){
                for(NoticeCount noticeCount : noticeCounts){
                    if(noticeCount.getSessionId().equals(sessionResponse.getSessionId())){
                        if(noticeCount.getNoticeCount()>0){
                            sessionResponse.setCount(noticeCount.getNoticeCount());
                            sessionResponse.setStatus(false);
                            break;
                        }
                    }
                }
            }
        }
        //按创建时间倒序排
        if(selfResponses.size()>0){
            selfResponses = selfResponses.stream()
                    .sorted(Comparator.comparing(ChatSessionVO::getUpdateTime).reversed())
                    .collect(Collectors.toList());
        }
        return Result.data(selfResponses);
    }
    /**
     *新增聊天会话和内容
     */
    @Override
    public Result<Boolean> addChatSessionAndContent(ChatSessionDTO chatSessionDTO) {
        //查询之前是否存在自己向他人或他人向自己发起的会话
        LambdaQueryWrapper<ChatSession> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(ChatSession::getSenderId, chatSessionDTO.getSenderId());
        wrapper1.eq(ChatSession::getReceiverId, chatSessionDTO.getReceiverId());
        LambdaQueryWrapper<ChatSession> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(ChatSession::getSenderId, chatSessionDTO.getReceiverId());
        wrapper2.eq(ChatSession::getReceiverId, chatSessionDTO.getSenderId());
        ChatSession c1=chatSessionMapper.selectOne(wrapper1);
        ChatSession c2=chatSessionMapper.selectOne(wrapper2);
        ChatSession chatSession= chatSessionDTO.toSessionEntity();
        chatSession.setUpdateTime(LocalDateTime.now());
        //如果查询过后发现该会话之前并未存在
        if ( c1== null && c2 == null) {
            chatSessionMapper.insert(chatSession);
        }else {
            chatServiceMapper.updateChatSession(chatSessionDTO.getUpdateContent(), LocalDateTime.now(), chatSessionDTO.getSenderId(), chatSessionDTO.getReceiverId());
        }
        chatMapper.insert(chatSessionDTO.toChatEntity().setContent(chatSession.getUpdateContent()));
        return Result.success(true);
    }
    /**
     *获取某会话历史聊天内容
     */
    @Override
    public Result<List<HistoryChatVO>> getHistoryChat(Integer userId, Integer receiverId) {
        LambdaQueryWrapper<Chat> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Chat::getSenderId, userId);
        wrapper1.eq(Chat::getReceiverId, receiverId);
        LambdaQueryWrapper<Chat> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Chat::getReceiverId, userId);
        wrapper2.eq(Chat::getSenderId, receiverId);
        List<Chat> list1 = chatMapper.selectList(wrapper1);
        List<Chat> list2 = chatMapper.selectList(wrapper2);
        list1.addAll(list2);
        List<HistoryChatVO> responses = new ArrayList<>();
        for (Chat chat : list1) {
            responses.add(new HistoryChatVO(chat));
        }
        responses = responses.stream()
                .sorted(Comparator.comparing(HistoryChatVO::getCreateTime).reversed())
                .collect(Collectors.toList());
        return Result.data(responses);
    }
    public PPTVO getPPT(String describe) throws IOException {
        long timestamp = System.currentTimeMillis() / 1000;
        String ts = String.valueOf(timestamp);
        // 获得鉴权信息
        ApiAuthAlgorithm auth = new ApiAuthAlgorithm();
        String signature = auth.getSignature(appId, apiSecret, timestamp);
        ApiClient client = new ApiClient("https://zwapi.xfyun.cn");
        // 大纲生成
        String outlineResp = client.createOutline(appId, ts, signature, describe);
        CreateVO outlineResponse = JSON.parseObject(outlineResp, CreateVO.class);
        ObjectMapper objectMapper = new ObjectMapper();
        OutlineVo outlineVo = objectMapper.readValue(outlineResponse.getData().getOutline(), OutlineVo.class);
        List<String> chapterList = new ArrayList<>();
        Map<String, Object> chapterMap = new HashMap<>();
        int i = 1, j = 1;
        String prefix="主题是"+outlineVo.getTitle()+",副主题是"+outlineVo.getSubTitle()+",大纲是";
        for(OutlineVo.Chapter chapter : outlineVo.getChapters()){
            prefix=prefix+chapter.chapterTitle+",";
            if(chapter.chapterContents.size()>0){
                for(OutlineVo.Chapter chapter1 : chapter.chapterContents){
                    prefix=prefix+chapter1.chapterTitle+",";
                }
            }
        }
        for (OutlineVo.Chapter chapter : outlineVo.getChapters()) {
            //对每个大纲内部调用讯飞星火的提问api进行扩展详情
            chapterMap.put(String.valueOf(i), chapter.chapterTitle);
            chapterList.add(String.valueOf(i));
            for (OutlineVo.Chapter chapter1 : chapter.chapterContents) {
                chapterList.add(i + "." + j);
                PPTHandler pptHandler = new PPTHandler(chapter1.chapterTitle,appId,apiKey,apiSecret);
                chapterMap.put((i + "." + j), pptHandler);
                j++;
                try {
                    pptHandler.send(prefix+"现在的详细解说内容是:"+chapter1.chapterTitle);
                    while (pptHandler.pptDetail.getGenerateEnding() == false) {
                        Thread.sleep(200);
                        log.info(pptHandler.pptDetail.getThemeName() + "还没打印完");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            i++;
            j = 1;
//        }
        }
        //将扩展后的ppt大纲和大纲扩展后的详情统一封装到集合对象里
        List<PPTWord> pptWords = new ArrayList<>();
        Pattern pattern = Pattern.compile("^[1-9]$");
        for (String index : chapterList) {
            if (pattern.matcher(index).matches()) {
                pptWords.add(new PPTWord().setIndex(index).setThemeName((String) chapterMap.get(index)));
            } else {
                PPTHandler pptHandler = (PPTHandler) chapterMap.get(index);
                pptWords.add(new PPTWord().setIndex(index).setThemeName(pptHandler.pptDetail.getThemeName()).setText(pptHandler.pptDetail.getText()));
            }
            if (chapterMap.get(index) instanceof String) {
                log.info((String) chapterMap.get(index));
            } else {
                PPTHandler pptHandler = (PPTHandler) chapterMap.get(index);
                log.info(pptHandler.pptDetail.getText());
            }

        }
        return new PPTVO().setPptWordList(pptWords).setCoverImgSrc(outlineResponse.getData().getCoverImgSrc());
    }
    /**
     *获取讯飞星火图片响应
     */
    public String getImage(String text) throws Exception {
        //绑定图片响应地址
        String url="https://spark-api.cn-huabei-1.xf-yun.com/v2.1/tti";

        String authUrl= MyUtil.getAuthUrl(url,apiKey,apiSecret);
        //特定json格式发送请求到讯飞星火
        String json = "{\n" +
                "  \"header\": {\n" +
                "    \"app_id\": \"" + appId + "\",\n" +
                "    \"uid\": \"" + UUID.randomUUID().toString().substring(0, 15) + "\"\n" +
                "  },\n" +
                "  \"parameter\": {\n" +
                "    \"chat\": {\n" +
                "      \"domain\": \"s291394db\",\n" +
                "      \"temperature\": 0.5,\n" +
                "      \"max_tokens\": 4096,\n" +
                "      \"width\": 1024,\n" +
                "      \"height\": 1024\n" +
                "    }\n" +
                "  },\n" +
                "  \"payload\": {\n" +
                "    \"message\": {\n" +
                "      \"text\": [\n" +
                "        {\n" +
                "          \"role\": \"user\",\n" +
                "           \"content\": \"" + text + "\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        //获取响应的Base64编码
        String res = MyUtil.doPostJson(authUrl, null, json);
        return res;
    }
}
