package com.bilibili.video.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.api.client.SendNoticeClient;
import com.bilibili.common.domain.entity.user.User;
import com.bilibili.common.domain.entity.video.video_production.Video;
import com.bilibili.common.domain.entity.video.video_production.VideoData;
import com.bilibili.common.domain.file.CustomMultipartFile;
import com.bilibili.common.domain.pojo.UploadVideo;
import com.bilibili.common.mapper.user.UserMapper;
import com.bilibili.common.mapper.video.video_production.VideoDataMapper;
import com.bilibili.common.mapper.video.video_production.VideoMapper;
import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.DeleteVideoDTO;
import com.bilibili.video.domain.dto.EditVideoDTO;
import com.bilibili.video.domain.dto.UploadPartDTO;
import com.bilibili.video.domain.dto.UploadVideoDTO;
import com.bilibili.video.domain.pojo.UploadPart;
import com.bilibili.video.service.UploadAndEditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.ScreenExtractor;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.bilibili.video.constant.Constant.*;


@Slf4j
@Service
public class UploadAndEditServiceImpl implements UploadAndEditService {
    @Resource
    MinioServiceImpl minioService;
    @Resource
    VideoMapper videoMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    SendNoticeClient client;
    @Resource
    VideoDataMapper videoDataMapper;
    @Resource
    RedisTemplate objectRedisTemplate;

    /**
     * 判断是否恶意文件、上传视频到minio、新增视频与视频数据记录、推送视频动态、发送数据同步消息
     */
    @Override
    @Transactional
    public Result<Boolean> uploadTotal(UploadVideoDTO uploadVideoDTO) {
        try {
            Video video = uploadVideoDTO.toEntity();
            LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
            String coverFile = uploadVideoDTO.getVideoCover();
            String url = "http://localhost:9000/video/" + uploadVideoDTO.getUrl();
            video.setUrl(url);
            if (coverFile != null && coverFile != "") {
//                hasCover=true;
                String prefixPath = "http://localhost:9000/video-cover/";
                byte[] decodedBytes = java.util.Base64.getDecoder().decode(coverFile);
//                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
                // 创建ImageInputStream
//                ImageInputStream iis = ImageIO.createImageInputStream(bis);
                // 读取图片文件格式
//                String imgContentType = getImageFormat(iis);
//                log.info(imgContentType);
                String imgContentType = "image/jpeg";
                String coverFileName = video.getName() + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
                video.setCover(prefixPath + coverFileName);
                videoMapper.insert(video);
                videoDataMapper.insert(new VideoData().setVideoId(video.getId()));
                CustomMultipartFile coverMultipartFile = new CustomMultipartFile(decodedBytes, coverFileName, imgContentType);
                queryWrapper.eq(Video::getCover, UUID.randomUUID().toString().substring(0, 8) + coverFileName);
                minioService.uploadImgFile(coverFileName, coverMultipartFile.getInputStream(), imgContentType);
//                client.sendUploadNotice(new UploadVideo().setVideoId(video.getId()).setVideoName(video.getName()).setUrl(url).setHasCover(true));
                User user = userMapper.selectById(uploadVideoDTO.getUserId());
                client.dynamicNotice(uploadVideoDTO.toCoverDynamic(user, video));
            } else {
                videoMapper.insert(video);
                videoDataMapper.insert(new VideoData().setVideoId(video.getId()));
                client.sendUploadNotice(new UploadVideo().setVideoId(video.getId()).setVideoName(video.getName()).setUrl(url).setHasCover(false));
                User user = userMapper.selectById(uploadVideoDTO.getUserId());
                client.dynamicNotice(uploadVideoDTO.toNoCoverDynamic(user, video));
            }

//            CompletableFuture<Void> sendDBChangeNotice = CompletableFuture.runAsync(() -> {
//                ObjectMapper objectMapper = new ObjectMapper();
//                JavaTimeModule module = new JavaTimeModule();
//                // 设置LocalDateTime的序列化方式
//                LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//                module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
//                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//                objectMapper.registerModule(module);
//                Map<String, Object> map = objectMapper.convertValue(video, Map.class);
//                map.put(TABLE_NAME, VIDEO_TABLE_NAME);
//                map.put(OPERATION_TYPE, OPERATION_TYPE_ADD);
//                map.put(VIDEO_ID, map.get(TABLE_ID));
//                map.remove(TABLE_ID);
//                client.sendDBChangeNotice(map);
//            });

//            CompletableFuture.allOf(uploadVideoFuture, sendNoticeFuture).join();
//            if (!uploadVideoSuccess.get()) {
//                log.error("lose");
//            } else {
//                // 所有任务成功
//                log.info("ok");
//            }
            return Result.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("寄");
        }
    }

    /**
     * 编辑视频并发送数据同步消息
     */
    @Override
    public Result<Boolean> edit(EditVideoDTO editVideoDTO) {
//        try {
//            Map<String, Object> map = editVideoDTO.toMap();
//            MultipartFile videoFile = editVideoDTO.getFile();
//            ObjectMapper objectMapper = new ObjectMapper();
//            JavaTimeModule module = new JavaTimeModule();
//            LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//            module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
//            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//            objectMapper.registerModule(module);
//            map.put(TABLE_NAME, VIDEO_TABLE_NAME);
//            map.put(OPERATION_TYPE, OPERATION_TYPE_UPDATE);
//            Video video = editVideoDTO.toEntity();
//            if (videoFile != null) {
//                String videoUrl = UUID.randomUUID().toString().substring(0, 10) + editVideoDTO.getName();
//                video.setUrl(videoUrl);
//                map.put(VIDEO_URL, videoUrl);
//                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                    try {
//                        minioService.uploadVideoFile(videoUrl, videoFile.getInputStream(), videoFile.getContentType());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                });
//                CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
//                    while (true) {
//                        String key = "encode-count";
//                        Integer count = (Integer) objectRedisTemplate.opsForValue().get(key);
//                        if (count < 3) {
//                            client.sendUploadNotice(new UploadVideo().setVideoId(video.getId()).setUrl(videoUrl).setVideoName(video.getName()));
//                            count++;
//                            objectRedisTemplate.opsForValue().set(key, count);
//                            break;
//                        } else {
//                            try {
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//
//                    }
//                });
//            }
//            MultipartFile coverFile = editVideoDTO.getCover();
//            if (coverFile != null) {
//                CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//                    String coverUrl = UUID.randomUUID().toString().substring(0, 10) + coverFile.getOriginalFilename();
//                    map.put(VIDEO_COVER, coverUrl);
//                    try {
//                        minioService.uploadImgFile(coverUrl, coverFile.getInputStream(), coverFile.getContentType());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    video.setCover(coverUrl);
//                });
//            }
//            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> client.sendDBChangeNotice(map));
//            videoMapper.updateById(video);
//            return Result.success(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.error("寄");
//        }
        return null;
    }

    /**
     * 删除视频并发送数据同步消息
     */
    @Override
    @Transactional
    public Result<Boolean> delete(DeleteVideoDTO deleteVideoDTO) {
        LambdaQueryWrapper<Video> deleteVideoWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<VideoData> deleteVideoDataWrapper = new LambdaQueryWrapper<>();
        deleteVideoWrapper.eq(Video::getUserId, deleteVideoDTO.getUserId());
        deleteVideoWrapper.eq(Video::getId, deleteVideoDTO.getVideoId());
        deleteVideoDataWrapper.eq(VideoData::getVideoId, deleteVideoDTO.getVideoId());
//        CompletableFuture<Void> sendDBChangeNotice = CompletableFuture.runAsync(() -> {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JavaTimeModule module = new JavaTimeModule();
//            LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//            module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
//            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//            objectMapper.registerModule(module);
//            Map<String, Object> map = new HashMap<>(3);
//            map.put(TABLE_NAME, VIDEO_TABLE_NAME);
//            map.put(OPERATION_TYPE, OPERATION_TYPE_DELETE);
//            map.put(TABLE_ID, deleteVideoDTO.getVideoId());
//            client.sendDBChangeNotice(map);
//        });
        videoMapper.delete(deleteVideoWrapper);
        videoDataMapper.delete(deleteVideoDataWrapper);
        return Result.success(true);
    }

    /**
     * 上传视频时获取视频封面
     */
    @Override
    public Result<List<String>> uploadPart(UploadPartDTO uploadPartDTO) throws IOException, EncoderException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        int commaIndex = uploadPartDTO.getResumableIdentifier().indexOf(',');
        uploadPartDTO.setResumableIdentifier(uploadPartDTO.getResumableIdentifier().substring(0, commaIndex));
        String resumableIdentifier = uploadPartDTO.getResumableIdentifier();
        String videoName = "";
        String videoCover = "";
//        if(uploadPartDTO.getResumableChunkNumber()==1){
//            String path = Files.createTempDirectory(".tmp").toString();
//            File file = new File(path, "test");
//            InputStream videoFileInputStream = uploadPartDTO.getFile().getInputStream();
//            byte[] bytes=IoUtil.readBytes(videoFileInputStream);
//            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
//            Files.copy(byteArrayInputStream, Paths.get(file.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
//            if (!FileTypeUtil.getType(file).equals("mp4")) {
//                file.delete();
//                return Result.error("上传恶意文件");
//            } else {
//                file.delete();
//                log.info("文件无问题");
//            }
//        }
        if (uploadPartMap.get(resumableIdentifier) == null || uploadPartMap.get(resumableIdentifier).getHasCutImg() == false) {
            InputStream videoFileInputStream = uploadPartDTO.getFile().getInputStream();
            byte[] bytes = IoUtil.readBytes(videoFileInputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            String filePath = Files.createTempDirectory(".tmp").toString();
            String coverFileName = UUID.randomUUID().toString().substring(0, 10) + ".jpg";
            String videoFileName = "video";
            File directory = new File(filePath);
            File videoFile = new File(filePath, videoFileName);
            File coverFile = new File(directory, coverFileName);
            Files.copy(byteArrayInputStream, Paths.get(videoFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            ScreenExtractor screenExtractor = new ScreenExtractor();
            MultimediaObject multimediaObject = new MultimediaObject(videoFile);
            screenExtractor.renderOneImage(multimediaObject, -1, -1, 1000, coverFile, 1);
            if (coverFile.exists()) {
                log.info("exist");
                InputStream inputStream = new FileInputStream(coverFile);
                String cover = Base64.encode(IoUtil.readBytes(inputStream));
                log.info(cover.substring(0, 20));
                videoFile.delete();
                coverFile.delete();
                UploadPart uploadPart = uploadPartMap.getOrDefault(resumableIdentifier, new UploadPart());
                uploadPart.setHasCutImg(true);
                uploadPart.setCover(cover);
                uploadPartMap.put(resumableIdentifier, uploadPart);
            }

        }
        String name = resumableIdentifier + UUID.randomUUID().toString().substring(0, 10);
        minioService.uploadVideoFile(name, uploadPartDTO.getFile().getInputStream(), VIDEO_TYPE);
//        log.info(uploadPartMap.toString());
        Map<Integer, String> newUploadPartMap = uploadPartMap.getOrDefault(resumableIdentifier, new UploadPart()).getPartMap();
        newUploadPartMap.put(uploadPartDTO.getResumableChunkNumber(), name);
        UploadPart uploadPart = uploadPartMap.getOrDefault(resumableIdentifier, new UploadPart());
        uploadPart.setPartMap(newUploadPartMap);
        uploadPartMap.put(resumableIdentifier, uploadPart);
        uploadPartMap.get(resumableIdentifier).setTotalCount(uploadPartMap.get(resumableIdentifier).getTotalCount() + 1);
        if (uploadPartMap.get(resumableIdentifier).getTotalCount().equals(uploadPartDTO.getResumableTotalChunks())) {
            log.info("合并");
            videoName = resumableIdentifier + UUID.randomUUID().toString().substring(0, 10);
            videoCover = uploadPartMap.get(resumableIdentifier).getCover();
            minioService.composePart(resumableIdentifier, videoName);
        }
        List<String> list = new ArrayList<>();
        list.add(videoName);
        list.add(videoCover);
        return Result.data(list);
    }

    @Override
    public ResponseEntity<Result<Boolean>> getProcessor(String resumableIdentifier, Integer resumableChunkNumber) {
        log.info("id" + resumableChunkNumber.toString());
        log.info("uploadMap" + uploadPartMap.toString());
        log.info(resumableIdentifier);
        log.info(uploadPartMap.getOrDefault(resumableIdentifier, new UploadPart()).toString());
        for (Map.Entry<Integer, String> entry : uploadPartMap.getOrDefault(resumableIdentifier, new UploadPart()).getPartMap().entrySet()) {
            log.info("键值对" + entry.toString());
            log.info(entry.getKey().intValue() + "和" + resumableChunkNumber.intValue());
            if (entry.getKey().intValue() == (resumableChunkNumber.intValue())) {
                log.info("200");
                return ResponseEntity.ok(Result.data(true));
            }
        }

        return ResponseEntity.noContent().build();
    }

    private static String getImageFormat(ImageInputStream iis) throws IOException {
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

        if (imageReaders.hasNext()) {
            ImageReader reader = imageReaders.next();
            return reader.getFormatName();
        }

        return null;
    }
}
