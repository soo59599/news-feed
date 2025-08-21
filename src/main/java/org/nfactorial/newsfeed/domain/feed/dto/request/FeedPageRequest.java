package org.nfactorial.newsfeed.domain.feed.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedPageRequest {

	//현재 페이지 번호
	@NotNull(message = "페이지 번호 기입은 필수입니다.")
	private Long pageNumber = 1L;

	//한 페이지에 담긴 데이터 개수
	@NotNull(message = "데이터 개수를 조회할 사이즈 기입은 필수입니다.")
	private Long size = 10L;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate endDate;
}
