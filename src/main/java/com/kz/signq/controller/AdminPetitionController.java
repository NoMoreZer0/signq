package com.kz.signq.controller;

import com.kz.signq.dto.MessageDto;
import com.kz.signq.model.Petition;
import com.kz.signq.service.PetitionService;
import com.kz.signq.service.PetitionStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/petition")
@RequiredArgsConstructor
public class AdminPetitionController {

    private final PetitionStatusService petitionStatusService;
    private final PetitionService petitionService;


    @GetMapping
    @Transactional
    public ResponseEntity<List<Petition>> petitionsForReview(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "status", defaultValue = "", required = false) String status,
            @RequestParam(name = "sort_by", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(name = "order_by", defaultValue = "asc", required = false) String orderBy
    ) {
        return ResponseEntity.ok().body(petitionService.getReviewPetitions(page, size, status, sortBy, orderBy));
    }

    @PostMapping("/process")
    public ResponseEntity<MessageDto> process(@RequestParam("petition_id") UUID petitionId) {
        return ResponseEntity.ok().body(petitionStatusService.process(petitionId));
    }

    @PostMapping("/reject")
    public ResponseEntity<MessageDto> reject(@RequestParam("petition_id") UUID petitionId) {
        return ResponseEntity.ok().body(petitionStatusService.reject(petitionId));
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok().body("You have access");
    }
}
