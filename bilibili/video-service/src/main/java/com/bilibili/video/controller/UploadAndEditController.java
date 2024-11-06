package com.bilibili.video.controller;

import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.DeleteVideoDTO;
import com.bilibili.video.domain.dto.EditVideoDTO;
import com.bilibili.video.domain.dto.UploadPartDTO;
import com.bilibili.video.domain.dto.UploadVideoDTO;
import com.bilibili.video.service.UploadAndEditService;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ws.schild.jave.EncoderException;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/createCenter")
@Api(tags = "上传视频和编辑视频")
@CrossOrigin(value = "*")
@Slf4j
public class UploadAndEditController {
    @Resource
    UploadAndEditService uploadAndEditService;

    @ApiOperation("上传视频")
    @PostMapping("/uploadTotal")
    public Result<Boolean> uploadTotal(@RequestBody UploadVideoDTO uploadVideoDTO) throws IOException {
        log.info("上传视频");
        return uploadAndEditService.uploadTotal(uploadVideoDTO);
    }
    @ApiOperation("上传分片并获取视频第一帧图片和合并后的路径")
    @PostMapping("/uploadPart")
    public Result<List<String>> uploadPart(@ModelAttribute UploadPartDTO uploadPartDTO)
            throws EncoderException, IOException,
            ServerException, InsufficientDataException,
            ErrorResponseException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        return uploadAndEditService.uploadPart(uploadPartDTO);
    }
    @ApiOperation("编辑视频")
    @PostMapping("edit")
    public Result<Boolean> edit(@ModelAttribute EditVideoDTO editVideoDTO){
        log.info("1");
        return uploadAndEditService.edit(editVideoDTO);
    }
    @ApiOperation("删除视频")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody DeleteVideoDTO deleteVideoDTO){
        log.info("1");
        return uploadAndEditService.delete(deleteVideoDTO);
    }

    @ApiOperation("查询进度")
    @GetMapping("/getProcessor")
    public ResponseEntity<Result<Boolean>> getProcessor(
            @RequestParam("resumableIdentifier") String resumableIdentifier,
            @RequestParam("resumableChunkNumber")Integer resumableChunkNumber) {
        return uploadAndEditService.getProcessor(resumableIdentifier,resumableChunkNumber);
    }
}
