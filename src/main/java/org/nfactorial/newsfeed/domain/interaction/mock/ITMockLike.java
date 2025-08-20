package org.nfactorial.newsfeed.domain.interaction.mock;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "itmock_likes", uniqueConstraints = {
	@UniqueConstraint(
		name = "like_uk",
		columnNames = {"post_id", "profile_id"}
	)
})
public class ITMockLike {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private ITMockPost post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private ITMockProfile profile;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	public static ITMockLike of(ITMockPost post, ITMockProfile profile) {
		return new ITMockLike(null, post, profile, null);
	}
}
