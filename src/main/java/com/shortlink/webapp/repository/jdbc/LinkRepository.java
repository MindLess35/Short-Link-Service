package com.shortlink.webapp.repository.jdbc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

//@ConditionalOnBean(DataSource.class)
//@ConditionalOnClass(JpaRepository.class)
//@ConditionalOnMissingBean({ JpaRepositoryFactoryBean.class, JpaRepositoryConfigExtension.class })
//@ConditionalOnProperty(prefix = "spring.data.jpa.repositories", name = "enabled", havingValue = "true",
//        matchIfMissing = true)

@Repository
@ConditionalOnProperty(name = "repository", havingValue = "jdbc")
public class LinkRepository {
}
