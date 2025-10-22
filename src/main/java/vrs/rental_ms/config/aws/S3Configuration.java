package vrs.rental_ms.config.aws;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import vrs.rental_ms.config.properties.AwsProperties;

@Configuration
@AllArgsConstructor
public class S3Configuration {

    private final AwsProperties awsProperties;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(java.net.URI.create(awsProperties.getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsProperties.getAccessKey(), awsProperties.getSecretKey())))
                .region(Region.of(awsProperties.getRegion()))
                .forcePathStyle(true)
                .build();
    }

}
