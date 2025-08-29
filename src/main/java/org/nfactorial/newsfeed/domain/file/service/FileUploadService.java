package org.nfactorial.newsfeed.domain.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.exception.BusinessException;
import org.nfactorial.newsfeed.domain.file.entity.File;
import org.nfactorial.newsfeed.domain.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

	private final FileRepository fileRepository;

	@Value("${file.upload.dir}")
	private String uploadDir;

	private final List<String> allowedExtensions =
		List.of("jpg", "png", "webp", "pdf", "docx");

	public File saveFile(MultipartFile file) throws IOException {
		// 빈 파일 체크
		if (file.isEmpty()) {
			throw new BusinessException(ErrorCode.FILE_EMPTY);
		}

		// 파일명 처리
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || originalFilename.trim().isEmpty()) {
			throw new BusinessException(ErrorCode.FILE_NAME_MISSING);
		}

		//확장자 처리
		originalFilename = StringUtils.cleanPath(originalFilename);
		String fileExtension = getFileExtension(originalFilename);
		if (!isAllowedExtension(fileExtension)) {
			throw new BusinessException(ErrorCode.FILE_TYPE_NOT_ALLOWED);
		}
		String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

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

		// 파일 저장
		Path filePath = uploadPath.resolve(uniqueFilename);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// 다운로드 URL 생성
		String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/api/files/download/")
			.path(uniqueFilename)
			.toUriString();

		File fileEntity = File.of(uniqueFilename,originalFilename,file.getSize(),file.getContentType(),downloadUrl);

		fileRepository.save(fileEntity);

		return fileEntity;
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

	private boolean isAllowedExtension(String filename) {
		String ext = getFileExtension(filename).toLowerCase().substring(1);
		return allowedExtensions.contains(ext); // List의 contains() 메서드
	}
}