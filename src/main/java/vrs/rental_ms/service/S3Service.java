package vrs.rental_ms.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import vrs.rental_ms.config.properties.AwsProperties;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final AwsProperties awsProperties;

    public String uploadFile(String filename, MultipartFile file) {
        var putObjectRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getBucketName())
                .key(filename)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException ex) {
            log.error("Error occurred when uploading image to S3: {}", ex.getLocalizedMessage());
            return "";
        }

        return "%s/%s/%s".formatted(awsProperties.getExposureEndpoint(), awsProperties.getBucketName(), filename);
    }

    public void deleteFile(String fileUrl) {
        var deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(awsProperties.getBucketName())
                .key(fileUrl.substring(fileUrl.lastIndexOf("/") + 1))
                .build();

        try {
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception ex) {
            log.error("Error occurred when deleting image from S3: {}", ex.getMessage());
        }
    }

}
