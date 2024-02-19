package com.shortlink.webapp.repository;

import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.LinkStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkStatisticsRepository extends JpaRepository<LinkStatistics, Long> {
    Optional<LinkStatistics> findByLink(Link link);



//    @Param("count")
}
