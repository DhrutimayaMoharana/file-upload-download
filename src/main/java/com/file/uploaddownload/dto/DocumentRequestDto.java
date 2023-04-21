package com.file.uploaddownload.dto;

import org.springframework.web.multipart.MultipartFile;

public class DocumentRequestDto {

	private Long id;

	private String fileName;

	private String fileType;

	private MultipartFile document;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public MultipartFile getDocument() {
		return document;
	}

	public void setDocument(MultipartFile document) {
		this.document = document;
	}

	public DocumentRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DocumentRequestDto(Long id, String fileName, String fileType, MultipartFile document) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.document = document;
	}

}
