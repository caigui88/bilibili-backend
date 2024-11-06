package com.bilibili.video.service.impl;

import com.bilibili.video.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bilibili.video.constant.Constant.uploadPartMap;
/**
 *minio的api调用封装方法
 */
@Slf4j
@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioClient minioClient;

    /**
     *合成分片
     */
    @Override
    public Boolean composePart(String resumableIdentifier,String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<ComposeSource> composeSourceList=new ArrayList<>();
        //获取分片信息并封装成ComposeSource类的集合，合并到一起
        for(Map.Entry<Integer,String> entry : uploadPartMap.get(resumableIdentifier).getPartMap().entrySet()){
            composeSourceList.add(ComposeSource.builder().bucket("video").object(entry.getValue()).build());
        }
        minioClient.composeObject(ComposeObjectArgs.builder().sources(composeSourceList).object(name).bucket("video").build());
        return true;
    }
    /**
     *上传视频文件
     */
    @Override
    public Boolean uploadVideoFile(String fileName, InputStream stream, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket("video").object(fileName)
                            .stream(stream, -1, 10485760)
                            .contentType(contentType)
                            .build());

            stream.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("上传失败", e);
        }
    }
    /**
     *上传图片文件
     */
    @Override
    public   Boolean uploadImgFile(String fileName, InputStream stream, String contentType){

        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket("video-cover").object(fileName)
                            .stream(stream, -1, 10485760)
                            .contentType(contentType)
                            .build());

            stream.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    /**
     *获取视频文件
     */
    @Override
    public InputStream getObject(String objectName) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket("video")
                    .object(objectName)
                    .build();
            return minioClient.getObject(getObjectArgs);
        } catch (Exception e) {
            log.error("错误：" + e.getMessage());
        }
        return null;
    }
    /**
     *创建桶
     */
    public Boolean createBucket(String name){
        try {

            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            } else {
            }
        } catch (Exception e) {
        }
        return true;
    }
    /**
     *删除桶
     */
    public Boolean deleteBucket(String name){
        try {

            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(name).build());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
