package com.whosup.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
@PropertySource(value = { "/WEB-INF/amazon.properties" })
public class S3Storage {

	@Autowired
	private Environment env;

	@Bean
	public AmazonS3 amazonInstance() {
		try{
			AWSCredentials awsCredentials = new BasicAWSCredentials(
					env.getProperty("amazon.accessKey"),
					env.getProperty("amazon.secretKey"));
			AmazonS3 amazonS3 = new AmazonS3Client(awsCredentials);
			return amazonS3;
		}catch(Exception e){
			return null;
		}
		
	}

}
