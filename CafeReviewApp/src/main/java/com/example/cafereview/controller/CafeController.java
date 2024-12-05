package com.example.cafereview.controller;

import com.example.cafereview.dto.request.CafeRequest;
import com.example.cafereview.dto.response.CafeResponse;
import com.example.cafereview.dto.response.CafeDetailResponse;
import com.example.cafereview.service.CafeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/cafes")
public class CafeController {

    private final CafeService cafeService;

    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @GetMapping
    public List<CafeResponse> getAllCafes() {
        return cafeService.findAll().stream()
                .map(CafeResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public CafeDetailResponse getCafeById(@PathVariable Long id) {
        return cafeService.findById(id)
                .map(CafeDetailResponse::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cafe not found"));
    }

    @PostMapping
    public ResponseEntity<CafeResponse> createCafe(
            @RequestBody CafeRequest request,
            Authentication authentication) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CafeResponse.fromEntity(
                        cafeService.create(request, authentication.getName())
                ));
    }

    @PutMapping("/{id}")
    public CafeResponse updateCafe(
            @PathVariable Long id,
            @RequestBody CafeRequest request,
            Authentication authentication) {
        return CafeResponse.fromEntity(
                cafeService.update(id, request, authentication.getName())
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCafe(@PathVariable Long id, Authentication authentication) {
        cafeService.delete(id, authentication.getName());
    }
}