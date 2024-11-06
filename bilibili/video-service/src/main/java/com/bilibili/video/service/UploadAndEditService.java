package com.bilibili.video.service;

import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.DeleteVideoDTO;
import com.bilibili.video.domain.dto.EditVideoDTO;
import com.bilibili.video.domain.dto.UploadPartDTO;
import com.bilibili.video.domain.dto.UploadVideoDTO;
import io.minio.errors.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UploadAndEditService {
    Result<Boolean> uploadTotal(UploadVideoDTO uploadVideoDTO) throws IOException;

    Result<Boolean> edit(EditVideoDTO editVideoDTO);
    Result<Boolean> delete(DeleteVideoDTO deleteVideoDTO);

    Result<List<String>> uploadPart(@ModelAttribute UploadPartDTO uploadPartDTO) throws IOException, EncoderException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    ResponseEntity<Result<Boolean>> getProcessor(String resumableIdentifier, Integer resumableChunkNumber);
}
