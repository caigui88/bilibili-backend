package com.bilibili.video.domain.dto;

import lombok.Data;

@Data
public class DeleteVideoDTO {
    private int userId;
    private int videoId;
}
