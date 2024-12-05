package com.example.cafereview.repository;

import com.example.cafereview.model.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {
    List<Cafe> findByOrderByAverageRatingDesc();
    List<Cafe> findByCreatorUsername(String username);
}