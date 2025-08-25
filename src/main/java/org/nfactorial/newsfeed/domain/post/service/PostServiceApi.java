package org.nfactorial.newsfeed.domain.post.service;

import java.util.List;
import java.util.Map;

import org.nfactorial.newsfeed.domain.post.entity.Post;

public interface PostServiceApi {

	Post getPostById(long postId);

	long countPostsByProfileId(long profileId);

	Map<Long, Long> countPostsByProfileIds(List<Long> profileIds);

	Post getPostByIdWithLock(Long postId);
}
