package com.file.uploaddownload.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.file.uploaddownload.dto.DocumentRequestDto;
import com.file.uploaddownload.dto.Response;
import com.file.uploaddownload.service.DocumentService;

@RestController
@RequestMapping("document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	@PostMapping("/v1/uploadFile")
	public ResponseEntity<?> uploadFileInDataBase(@ModelAttribute DocumentRequestDto documentRequestDto)
			throws Exception {

		Response<?> respone = documentService.uploadDocumentAndStoreInDBV1(documentRequestDto);

		return new ResponseEntity<>(respone, HttpStatus.valueOf(respone.getResponseCode()));

	}

	@PostMapping("/v2/uploadFile")
	public ResponseEntity<?> uploadFileInLocal(@ModelAttribute MultipartFile file) throws Exception {

		Response<?> respone = documentService.storeFileInLocalDirectory(file, System.currentTimeMillis());

		return new ResponseEntity<>(respone, HttpStatus.valueOf(respone.getResponseCode()));

	}

	@PostMapping("/v3/uploadFile")
	public ResponseEntity<?> uploadFileInLocalAndResponseAsDownloadUrl(@ModelAttribute MultipartFile file)
			throws Exception {

		Response<?> respone = documentService.storeFileInLocalDirectoryResponseIsDownloadUrl(file,
				System.currentTimeMillis());

		return new ResponseEntity<>(respone, HttpStatus.valueOf(respone.getResponseCode()));

	}

	@GetMapping("/download/{filename}")
	public ResponseEntity<?> downloadFile(@PathVariable("filename") String filename) {

		Resource resource = documentService.downloadDocument(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

	}

	@PostMapping("/compress/files")
	public ResponseEntity<?> compressFile(@ModelAttribute MultipartFile file)
			throws Exception {

		Response<?> respone = documentService.compressTheFile(file, System.currentTimeMillis(), 0.5f);

		return new ResponseEntity<>(respone, HttpStatus.valueOf(respone.getResponseCode()));

	}

}
