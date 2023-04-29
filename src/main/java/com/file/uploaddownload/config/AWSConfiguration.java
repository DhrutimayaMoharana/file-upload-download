package com.file.uploaddownload.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class AWSConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public AmazonS3 awsS3Client() {
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(env.getRequiredProperty("aws.keyId"), env.getRequiredProperty("aws.accessKey"));
            return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(env.getRequiredProperty("aws.region")))
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
