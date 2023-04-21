package com.file.uploaddownload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.file.uploaddownload.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Long>{

}
