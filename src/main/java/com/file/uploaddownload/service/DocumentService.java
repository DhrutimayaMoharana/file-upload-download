package com.file.uploaddownload.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.file.uploaddownload.dto.DocumentRequestDto;
import com.file.uploaddownload.dto.Response;

public interface DocumentService {

	Response<?> uploadDocumentAndStoreInDBV1(DocumentRequestDto documentRequestDto) throws Exception;

	Response<?> storeFileInLocalDirectory(MultipartFile file, Long currentDate);

	Resource downloadDocument(String fileName);

	Response<?> storeFileInLocalDirectoryResponseIsDownloadUrl(MultipartFile file, Long currentDate);

	Response<?> compressTheFile(MultipartFile file, Long currentDate, Float compressQuality) throws Exception;

	Response<?> storeInAwsS3(MultipartFile file, Long currentDate) throws Exception;

	HttpServletResponse downloadDocumentFromAwsS3Bucket(String fileName, HttpServletResponse response);

}
