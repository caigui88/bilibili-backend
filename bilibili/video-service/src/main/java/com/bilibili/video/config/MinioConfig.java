package com.bilibili.video.config;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "minio")
@ConditionalOnClass(MinioClient.class)
@ConditionalOnProperty("minio.url")
public class MinioConfig {
    @Value("${minio.url}")
    private String endpoint;

    @Value("${minio.access-name}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            return minioClient;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
