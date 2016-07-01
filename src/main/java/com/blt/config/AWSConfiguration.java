package com.blt.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure our AWS Client.
 */
@Configuration
public class AWSConfiguration {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region}")
    private String region;

    /**
     * Create AWS Credentials.
     *
     * @return An AWS Credential instance.
     */
    @Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    /**
     * Create an S3 client.
     *
     * @param awsCredentials
     *      S3 Credentials.
     *
     * @return An S3 Client.
     */
    @Bean
    public AmazonS3Client amazonS3Client(final AWSCredentials awsCredentials) {

        AmazonS3Client amazonS3Client = new AmazonS3Client(awsCredentials);
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)));
        return amazonS3Client;
    }
}
