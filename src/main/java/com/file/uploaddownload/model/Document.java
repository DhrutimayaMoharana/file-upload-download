package com.file.uploaddownload.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.file.uploaddownload.dto.DocumentDto;

@Entity
@Table(name = "document")
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName;

	private String fileType;

	private String document;

	private Boolean isInAwsBucket;

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

	public Boolean getIsInAwsBucket() {
		return isInAwsBucket;
	}

	public void setIsInAwsBucket(Boolean isInAwsBucket) {
		this.isInAwsBucket = isInAwsBucket;
	}

	public Document() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Document(Long id, String fileName, String fileType, String document, Boolean isInAwsBucket) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.document = document;
		this.isInAwsBucket = isInAwsBucket;
	}

	public DocumentDto convertToDocumentDto() {
		return new DocumentDto(this.getId(), this.getFileName(), this.getFileType(), this.getDocument());
	}

}
