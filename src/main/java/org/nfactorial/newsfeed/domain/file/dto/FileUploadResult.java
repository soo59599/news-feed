package org.nfactorial.newsfeed.domain.file.dto;

public record FileUploadResult(
	String savedFilename,
	String originalFilename,
	long fileSize,
	String contentType,
	String downloadUrl
) {
}
