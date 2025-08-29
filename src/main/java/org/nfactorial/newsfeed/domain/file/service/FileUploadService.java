package org.nfactorial.newsfeed.domain.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.nfactorial.newsfeed.domain.file.dto.FileUploadResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileUploadService {

	@Value("${file.upload.dir}")
	private String uploadDir;

	public FileUploadResult saveFile(MultipartFile file) throws IOException {
		// 빈 파일 체크
		if (file.isEmpty()) {
			throw new IllegalArgumentException("파일이 비어있습니다.");
		}

		// 업로드 디렉토리 처리 (~ 경로 변환)
		String resolvedUploadDir = uploadDir;
		if (uploadDir.startsWith("~/")) {
			resolvedUploadDir = System.getProperty("user.home") + uploadDir.substring(1);
		}

		// 업로드 디렉토리 생성
		Path uploadPath = Paths.get(resolvedUploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		// 파일명 처리
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || originalFilename.trim().isEmpty()) {
			throw new IllegalArgumentException("파일명이 없습니다.");
		}

		originalFilename = StringUtils.cleanPath(originalFilename);
		String fileExtension = getFileExtension(originalFilename);
		String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

		// 파일 저장
		Path filePath = uploadPath.resolve(uniqueFilename);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// 다운로드 URL 생성
		String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/api/files/download/")
			.path(uniqueFilename)
			.toUriString();

		return new FileUploadResult(
			uniqueFilename,
			originalFilename,
			file.getSize(),
			file.getContentType(),
			downloadUrl
		);
	}

	public boolean deleteFile(String filename) {
		try {
			String resolvedUploadDir = uploadDir;
			if (uploadDir.startsWith("~/")) {
				resolvedUploadDir = System.getProperty("user.home") + uploadDir.substring(1);
			}

			Path filePath = Paths.get(resolvedUploadDir).resolve(filename);
			return Files.deleteIfExists(filePath);
		} catch (IOException e) {
			return false;
		}
	}

	public boolean fileExists(String filename) {
		String resolvedUploadDir = uploadDir;
		if (uploadDir.startsWith("~/")) {
			resolvedUploadDir = System.getProperty("user.home") + uploadDir.substring(1);
		}

		Path filePath = Paths.get(resolvedUploadDir).resolve(filename);
		return Files.exists(filePath);
	}

	private String getFileExtension(String filename) {
		if (filename == null || !filename.contains(".")) {
			return "";
		}
		return filename.substring(filename.lastIndexOf("."));
	}
}