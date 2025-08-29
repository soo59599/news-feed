package org.nfactorial.newsfeed.domain.file.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.nfactorial.newsfeed.common.code.ErrorCode;
import org.nfactorial.newsfeed.common.code.SuccessCode;
import org.nfactorial.newsfeed.common.dto.GlobalApiResponse;
import org.nfactorial.newsfeed.domain.file.dto.FileUploadResponse;
import org.nfactorial.newsfeed.domain.file.dto.FileUploadResult;
import org.nfactorial.newsfeed.domain.file.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

	@Autowired
	private FileUploadService fileUploadService;

	@Value("${file.upload.dir}")
	private String uploadDir;

	@PostMapping("/upload")
	public GlobalApiResponse<FileUploadResponse> uploadFile(
		@RequestParam("file") MultipartFile file) {

		try {
			FileUploadResult result = fileUploadService.saveFile(file);

			FileUploadResponse response = FileUploadResponse.from(result);

			return GlobalApiResponse.of(SuccessCode.CREATED, response);

		} catch (Exception e) {

			return GlobalApiResponse.of(ErrorCode.FILE_FAIL_UPLOAD, null);
		}
	}

	@GetMapping("/download/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
		try {
			String resolvedUploadDir = uploadDir;
			if (uploadDir.startsWith("~/")) {
				resolvedUploadDir = System.getProperty("user.home") + uploadDir.substring(1);
			}

			Path filePath = Paths.get(resolvedUploadDir).resolve(filename);
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() && resource.isReadable()) {
				return ResponseEntity.ok()
					.header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
					.body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@DeleteMapping("/delete/{filename}")
	public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable String filename) {
		Map<String, Object> response = new HashMap<>();

		try {
			boolean deleted = fileUploadService.deleteFile(filename);

			if (deleted) {
				response.put("success", true);
				response.put("message", "파일이 삭제되었습니다.");
			} else {
				response.put("success", false);
				response.put("message", "파일을 찾을 수 없거나 삭제할 수 없습니다.");
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "파일 삭제 실패: " + e.getMessage());
			return ResponseEntity.internalServerError().body(response);
		}
	}
}