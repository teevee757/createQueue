package com.flyingj.jmssqs.createQueue.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties("sqs")
@EnableConfigurationProperties(SqsProperties.class)
@Data
public class SqsProperties {
    private String endpointURI;
    private String queueName;
    private String region;
    private String messagePath;
    private String messageFileType;
    private String messageRetentionPeriod;
}
