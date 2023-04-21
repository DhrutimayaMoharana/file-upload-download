package com.file.uploaddownload.dto;

public class DocumentDto {

	private Long id;

	private String fileName;

	private String fileType;

	private String document;

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

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public DocumentDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DocumentDto(Long id, String fileName, String fileType, String document) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.document = document;
	}

}
