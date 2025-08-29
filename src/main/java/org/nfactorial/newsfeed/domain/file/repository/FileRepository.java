package org.nfactorial.newsfeed.domain.file.repository;

import org.nfactorial.newsfeed.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
