package com.project.Seoul.repository;

import com.project.Seoul.domain.AttractionsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionsRepository extends JpaRepository<AttractionsInfo,Long> {
}
