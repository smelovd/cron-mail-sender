package org.smelovd.mailsender.repositories;

import org.smelovd.mailsender.entities.Log;
import org.smelovd.mailsender.models.log.LogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {

    Page<Log> findAll(Pageable pageable);

    @Query("SELECT NEW org.smelovd.mailsender.models.log.LogResponse(" +
            "u.username, " +
            "u.email, " +
            "SUM(CASE WHEN l.type = 'REST' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN l.type = 'CRON' THEN 1 ELSE 0 END), " +
            "MIN(l.createdOn), " +
            "MAX(l.createdOn)) " +
            "FROM users u " +
            "LEFT JOIN logs l ON u.id = l.user.id " +
            "GROUP BY u.id " +
            "ORDER BY COUNT(l)")
    Page<LogResponse> findAllLogResponsesOrderByCount(Pageable pageable);
}
