package org.nfactorial.newsfeed.domain.interaction.mock;

import org.nfactorial.newsfeed.common.entity.BaseTimeEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
public class ITMockPost extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private ITMockProfile profile;

	private String content;

	private int likeCount;

	public void incrementLikeCount() {
		this.likeCount++;
	}

	public void decrementLikeCount() {
		if (this.likeCount > 0) {
			this.likeCount--;
		}
	}
}
