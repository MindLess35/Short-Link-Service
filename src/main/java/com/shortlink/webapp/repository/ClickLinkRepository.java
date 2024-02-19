package com.shortlink.webapp.repository;

import com.shortlink.webapp.entity.ClickLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickLinkRepository extends JpaRepository<ClickLink, Long> {
}
