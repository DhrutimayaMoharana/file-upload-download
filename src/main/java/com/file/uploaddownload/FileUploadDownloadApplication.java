package com.file.uploaddownload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.fasterxml.jackson.annotation.JsonRootName;

@EnableEurekaClient
@SpringBootApplication
public class FileUploadDownloadApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(FileUploadDownloadApplication.class, args);
	}

}
