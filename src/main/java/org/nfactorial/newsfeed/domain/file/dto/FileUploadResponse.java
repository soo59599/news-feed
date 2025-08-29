package org.nfactorial.newsfeed.domain.file.dto;

public record FileUploadResponse(
	String savedFilename,
	String originalFileName,
	long fileSize,
	String contentType,
	String downloadUrl
) {
	public static FileUploadResponse from(FileUploadResult result) {
		return new FileUploadResponse(
			result.savedFilename(),
			result.originalFilename(),
			result.fileSize(),
			result.contentType(),
			result.downloadUrl()
		);
	}
}