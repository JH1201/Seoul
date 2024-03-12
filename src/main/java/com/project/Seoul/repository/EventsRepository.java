package com.project.Seoul.repository;

import com.project.Seoul.domain.CultureInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends JpaRepository<CultureInfo,Long> {



}
