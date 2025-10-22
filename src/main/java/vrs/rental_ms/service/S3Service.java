package vrs.rental_ms.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import vrs.rental_ms.config.properties.AwsProperties;

import java.io.InputStream;

@Slf4j
@Service
@AllArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final AwsProperties awsProperties;

    public String uploadFile(String filename, byte[] fileBytes) {
        var putObjectRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getBucketName())
                .key(filename)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));

        return "%s/%s/%s".formatted(awsProperties.getExposureEndpoint(), awsProperties.getBucketName(), filename);
    }

    public InputStream downloadFile(final String filename) {
        return s3Client.getObject(GetObjectRequest.builder()
                .bucket(awsProperties.getBucketName())
                .key(filename)
                .build());
    }

    public Long getFileSize(final String fileKey) {
        var response = s3Client.headObject(HeadObjectRequest.builder()
                .bucket(awsProperties.getBucketName())
                .key(fileKey)
                .build());

        return response.contentLength();
    }

}
