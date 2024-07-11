package com.project.Seoul.repository;

import com.project.Seoul.domain.CultureInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface EventsRepository extends JpaRepository<CultureInfo,Long> {

    Page<CultureInfo> findAll(Pageable pageable);


}
