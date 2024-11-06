package com.bilibili.notice.consumer;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bilibili.api.client.VideoClient;
import com.bilibili.common.domain.entity.notice.Dynamic;
import com.bilibili.common.domain.entity.video.video_production.Video;
import com.bilibili.common.domain.file.CustomMultipartFile;
import com.bilibili.common.domain.pojo.UploadVideo;
import com.bilibili.common.mapper.notice.DynamicMapper;
import com.bilibili.common.mapper.video.video_production.VideoMapper;
import com.bilibili.notice.service.impl.SendDBChangeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.ScreenExtractor;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.VideoInfo;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bilibili.notice.constant.Constant.*;

/**
 *视频转码消费者
 */
@Service
@RocketMQMessageListener(
        topic = "video-encode",
        consumerGroup = "video-encode-group",
        consumeMode = ConsumeMode.ORDERLY
)
@Slf4j
public class VideoEncodeConsumer implements RocketMQListener<MessageExt> {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    VideoClient videoClient;
    @Autowired
    VideoMapper videoMapper;
    @Autowired
    SendDBChangeServiceImpl sendDBChangeService;
    @Autowired
    DynamicMapper dynamicMapper;
    /**
     *转码视频、如果视频无封面则截取封面、获取视频时长
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        String jsonMessage = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        try {
            UploadVideo uploadVideo = objectMapper.readValue(jsonMessage, UploadVideo.class);
            //远程调用获取已上传minio的视频文件流
            ResponseEntity<Resource> videoInputStream = videoClient.getVideo(uploadVideo);
                        String filePath = Files.createTempDirectory(".tmp").toString();
//            String filePath = "/var/temp";
            Video updateVideo=new Video().setId(uploadVideo.getVideoId());
            String videoFileName="video";
            File file = new File(filePath, videoFileName);
            String coverFileName = uploadVideo.getVideoName() + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
            File coverFile = new File(filePath, coverFileName);
            Files.copy(videoInputStream.getBody().getInputStream(), Paths.get(file.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            MultimediaObject multimediaObject = new MultimediaObject(file);
            VideoInfo videoInfo = multimediaObject.getInfo().getVideo();
            //如果视频没有封面
            if (uploadVideo.getHasCover() == false) {
                String contentType = "image/jpeg";
                ScreenExtractor screenExtractor = new ScreenExtractor();
                screenExtractor.renderOneImage(multimediaObject, -1, -1, 1000, coverFile, 1);
                CustomMultipartFile coverMultipartFile = new CustomMultipartFile(new FileInputStream(coverFile), coverFileName, contentType);
                videoClient.uploadVideoCover(coverMultipartFile);
                String prefixPath="https://labilibili.com/video-cover/";
                String cover=prefixPath+coverFileName;
                updateVideo.setCover(cover);
                LambdaUpdateWrapper<Dynamic> wrapper=new LambdaUpdateWrapper<>();
                wrapper.set(Dynamic::getVideoCover,cover);
                wrapper.eq(Dynamic::getVideoId,updateVideo.getId());
                dynamicMapper.update(null,wrapper);
            }
            //获取视频时长
            Integer totalLength = Math.toIntExact(multimediaObject.getInfo().getDuration()) / 1000;
            String length;
            Map<String, Object> map = new HashMap<>();
            if (totalLength / 60 < 1) {
                if (totalLength % 60 < 10) {
                    length = "00:0" + totalLength;
                } else {
                    length = "00:" + totalLength;
                }
            } else {
                if (totalLength / 60 < 10) {
                    if(totalLength%60<10){
                        length = "0" + totalLength / 60 + ":0" + totalLength % 60;
                    }else {
                        length = "0" + totalLength / 60 + ":" + totalLength % 60;
                    }
                } else {
                    length = totalLength / 60 + ":" + totalLength % 60;
                }
            }
            map.put(OPERATION_TYPE, OPERATION_TYPE_UPDATE);
            map.put(TABLE_NAME, VIDEO_TABLE_NAME);
            map.put(VIDEO_LENGTH, length);
            map.put(VIDEO_ID, uploadVideo.getVideoId());
            sendDBChangeService.sendDBChangeNotice(map);
            videoMapper.updateById(updateVideo.setLength(length));
            String rightFormat = "h264";
            //不符合h264的mp4文件需要转码否则浏览器中只有声音没有图像
            if (!rightFormat.equals(videoInfo.getDecoder())) {
                String contentType = "video/mp4";
                String outPutForMatType = "mp4";
                VideoAttributes videoAttributes = new VideoAttributes();
                String targetFileName = "target";
                File target = new File(filePath, targetFileName);
                videoAttributes.setCodec(rightFormat);
                AudioAttributes audio = new AudioAttributes();
                EncodingAttributes attrs = new EncodingAttributes();
                attrs.setOutputFormat(outPutForMatType);
                attrs.setAudioAttributes(audio);
                attrs.setVideoAttributes(videoAttributes);
                Encoder encoder = new Encoder();
                encoder.encode(multimediaObject, target, attrs);
                FileInputStream inputStream = new FileInputStream(target);
                CustomMultipartFile customMultipartFile = new CustomMultipartFile(inputStream, uploadVideo.getUrl().substring(uploadVideo.getUrl().lastIndexOf("/") + 1)
                        , contentType);
                log.info("转码成功");
                videoClient.uploadVideo(customMultipartFile);
                log.info("上传新视频成功");
                target.delete();
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
