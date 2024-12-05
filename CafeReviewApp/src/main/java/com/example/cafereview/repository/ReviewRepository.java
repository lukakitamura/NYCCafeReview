package com.example.cafereview.repository;

import com.example.cafereview.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCafeId(Long cafeId);
    List<Review> findByUserUsername(String username);
    boolean existsByUserUsernameAndCafeId(String username, Long cafeId);
}