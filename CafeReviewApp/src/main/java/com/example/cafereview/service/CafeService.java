package com.example.cafereview.service;

import com.example.cafereview.model.Cafe;
import com.example.cafereview.model.User;
import com.example.cafereview.dto.response.CafeResponse;
import com.example.cafereview.dto.response.CafeDetailResponse;
import com.example.cafereview.repository.CafeRepository;
import com.example.cafereview.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CafeService {
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;

    public CafeService(CafeRepository cafeRepository, UserRepository userRepository) {
        this.cafeRepository = cafeRepository;
        this.userRepository = userRepository;
    }

    public List<CafeResponse> getAllCafes() {
        return cafeRepository.findAll().stream()
                .map(CafeResponse::fromEntity)
                .toList();
    }

    public CafeDetailResponse getCafeById(Long id) {
        Cafe cafe = cafeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));
        return CafeDetailResponse.fromEntity(cafe);
    }

    public CafeResponse createCafe(String name, String address, boolean hasWifi,
                                   boolean hasRestroom, int seatingCapacity, String username) {
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cafe cafe = new Cafe();
        cafe.setName(name);
        cafe.setAddress(address);
        cafe.setHasWifi(hasWifi);
        cafe.setHasRestroom(hasRestroom);
        cafe.setSeatingCapacity(seatingCapacity);
        cafe.setCreator(creator);

        return CafeResponse.fromEntity(cafeRepository.save(cafe));
    }

    public void deleteCafe(Long id, String username) {
        Cafe cafe = cafeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));

        if (!cafe.getCreator().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to delete this cafe");
        }

        cafeRepository.delete(cafe);
    }
}