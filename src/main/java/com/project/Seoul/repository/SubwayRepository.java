package com.project.Seoul.repository;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.domain.SubwayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubwayRepository extends JpaRepository<SubwayInfo,Long> {
}
