package com.project.Seoul.repository;

import com.project.Seoul.domain.FavoriteCultureInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteEventsRepository extends JpaRepository<FavoriteCultureInfo,Long> {


}
