package org.nfactorial.newsfeed.domain.feed.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class PageResponseDto<T> {
	//현재 페이지 번호
	private final long page;

	//한 페이지에 담긴 데이터 개수
	private final long size;

	//전체 데이터 개수
	private final long totalElements;

	//총 페이지 수
	private final int totalPages;

	//조회할 데이터
	private List<T> contents;

	public PageResponseDto(long page, long size, long totalElements, List<T> contents) {
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.totalPages = (int)Math.ceil((double)totalElements / size);
		this.contents = contents;
	}

	public static <T> PageResponseDto<T> of(long page, long size, long totalElements, List<T> contents) {
		return new PageResponseDto<>(page, size, totalElements, contents);
	}
}
