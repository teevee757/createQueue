package com.flyingj.jmssqs.createQueue;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.flyingj.jmssqs.createQueue.config.ApplicationContextProvider;
import com.flyingj.jmssqs.createQueue.config.SqsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.jms.JMSException;
import javax.jms.Session;

@SpringBootApplication
@EnableConfigurationProperties(SqsProperties.class)
public class CreateQueueApplication implements CommandLineRunner {

	@Autowired
	ApplicationContextProvider applicationContextProvider;

	@Autowired
	SQSConnection sqsConnection;

	@Autowired
	SqsProperties sqsProperties;

	public static void main(String[] args) {
		SpringApplication.run(CreateQueueApplication.class, args);
	}

	@Override
	public void run(String...args) throws Exception {
		AmazonSQSMessagingClientWrapper client = sqsConnection.getWrappedAmazonSQSClient();

		if (!client.queueExists(sqsProperties.getQueueName())) {
			client.createQueue(sqsProperties.getQueueName());

//			sqsConnection.getAmazonSQSClient().setQueueAttributes(
//				new SetQueueAttributesRequest().withQueueUrl(sqsProperties.getQueueName())
//						.addAttributesEntry("MessageRetentionPeriod", sqsProperties.getMessageRetentionPeriod()));
		}
		System.exit(0);
	}

	@Bean
	SQSConnectionFactory sqsClient(SqsProperties sqsProperties) {
		SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
				new ProviderConfiguration(), AmazonSQSClient.builder().withEndpointConfiguration(
						new AwsClientBuilder.EndpointConfiguration(sqsProperties.getEndpointURI(),sqsProperties.getRegion()))
						.build());
		return connectionFactory;
	}

	@Bean
	SQSConnection sqsConnection(SQSConnectionFactory sqsConnectionFactory) throws JMSException {
		return sqsConnectionFactory.createConnection();
	}

	@Bean
	Session sqsSession(SQSConnection sqsConnection) throws JMSException {
		return sqsConnection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
	}
}
