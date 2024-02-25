package be.nicholasmeyers.word.adapter.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "be.nicholasmeyers.word.adapter.storage")
public class StorageTestConfig {

    @Value("${bucket.region}")
    private String region;

    @Value("${bucket.endpoint}")
    private String endpoint;

    @Value("${bucket.access-key}")
    private String accessKey;

    @Value("${bucket.secret-key}")
    private String secretKey;

    @Bean
    public AmazonS3 storageClient() {
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }
}
