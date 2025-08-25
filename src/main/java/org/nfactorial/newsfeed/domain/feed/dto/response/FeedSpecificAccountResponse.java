package org.nfactorial.newsfeed.domain.feed.dto.response;

import lombok.Getter;

@Getter
public class FeedSpecificAccountResponse {
	private FeedProfileInfoResponse feedProfileInfoResponse;
	private PageResponseDto<FeedSpecificResponse> feedSpecificResponses;

	public FeedSpecificAccountResponse(FeedProfileInfoResponse feedProfileInfoResponse,
		PageResponseDto<FeedSpecificResponse> feedSpecificResponses) {
		this.feedProfileInfoResponse = feedProfileInfoResponse;
		this.feedSpecificResponses = feedSpecificResponses;
	}

	public static FeedSpecificAccountResponse of(FeedProfileInfoResponse profileInfo,
		PageResponseDto<FeedSpecificResponse> feedProfileInfoResponse) {
		return new FeedSpecificAccountResponse(profileInfo, feedProfileInfoResponse);
	}
}
