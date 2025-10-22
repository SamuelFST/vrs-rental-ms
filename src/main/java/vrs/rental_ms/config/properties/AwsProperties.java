package vrs.rental_ms.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("rental-ms.aws.s3")
public class AwsProperties {

    private String accessKey;
    private String secretKey;
    private String region;
    private Boolean auto;
    private String endpoint;
    private String bucketName;
    private String exposureEndpoint;

}