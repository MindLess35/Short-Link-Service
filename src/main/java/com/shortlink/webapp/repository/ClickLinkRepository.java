package com.shortlink.webapp.repository;

import com.shortlink.webapp.dto.projection.ClickLinkStatProjection;
import com.shortlink.webapp.entity.ClickLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickLinkRepository extends JpaRepository<ClickLink, Long> {
    @Query(value = """
            SELECT TO_CHAR(cl.usageTime, 'yyyy-mm-dd') dateOfUses, COUNT(*) countOfUses
            FROM ClickLink cl
            WHERE cl.link.id = :linkId
            GROUP BY TO_CHAR(cl.usageTime, 'yyyy-mm-dd')
            ORDER BY TO_CHAR(cl.usageTime, 'yyyy-mm-dd')
            """
    )
    List<ClickLinkStatProjection> getStatByDays(Long linkId);

//    @Query(value = """
//            SELECT new com.shortlink.webapp.dto.response.ClickLinkStatDto(TO_CHAR(cl.usageTime, :timeUnits), COUNT (*))
//            FROM ClickLink cl
//            WHERE cl.link.id = :linkId AND TO_CHAR(cl.usageTime, 'yyyy-mm-dd') = :onDate
//            GROUP BY TO_CHAR(cl.usageTime, :timeUnits)
//            ORDER BY TO_CHAR(cl.usageTime, :timeUnits)
//            """
//    )
//    List<ClickLinkStatDto> getStatByTimeUnitsOnDate1(Long linkId, String timeUnits, String onDate); //YYYY-MM-DD HH24:MI:SS
    @Query(value = """
            SELECT TO_CHAR(cl.usage_time, :timeUnits) dateOfUses, COUNT(*) countOfUses
            FROM click_links cl
            WHERE cl.link_id = :linkId AND TO_CHAR(cl.usage_time, 'yyyy-mm-dd') = :onDate
            GROUP BY dateOfUses
            ORDER BY dateOfUses
            """, nativeQuery = true)
    List<ClickLinkStatProjection> getStatByTimeUnitsOnDate(Long linkId, String timeUnits, String onDate);
}
