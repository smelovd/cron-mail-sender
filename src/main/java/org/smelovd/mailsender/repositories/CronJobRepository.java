package org.smelovd.mailsender.repositories;

import org.smelovd.mailsender.entities.CronJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CronJobRepository extends JpaRepository<CronJob, Integer> {

    Page<CronJob> findAll(Pageable pageable);
}
