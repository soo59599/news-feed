package org.nfactorial.newsfeed.domain.file.entity;

import org.nfactorial.newsfeed.domain.post.entity.Post;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String savedFileName;

	private String originalFileName;

	private long fileSize;

	private String contentType;

	private String downloadUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	private File(String savedFileName, String originalFileName, long fileSize, String contentType, String downloadUrl) {
		this.savedFileName = savedFileName;
		this.originalFileName = originalFileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.downloadUrl = downloadUrl;
	}

	public static File of(String savedFileName, String originalFileName, long fileSize, String contentType, String downloadUrl){
		return new File(savedFileName, originalFileName, fileSize, contentType, downloadUrl);
	}

	public void setPost(Post post) {
		this.post = post;
	}
}
