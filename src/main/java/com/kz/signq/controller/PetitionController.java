package com.kz.signq.controller;

import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.PetitionDto;
import com.kz.signq.model.User;
import com.kz.signq.service.PetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/petition")
public class PetitionController {

    private final PetitionService petitionService;

    @Autowired
    public PetitionController(PetitionService petitionService) {
        this.petitionService = petitionService;
    }

    @PostMapping
    public ResponseEntity<?> save(
            @RequestBody PetitionDto petitionDto
    ) {
        return ResponseEntity.ok().body(petitionService.save(petitionDto));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(petitionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok().body(petitionService.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getCreated() {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.getCreatedPetitions(user));
    }

    @PostMapping("/sign")
    public ResponseEntity<?> sign(@RequestBody EntityIdDto entityIdDto) {
        try {
            var user = getCurrentUser();
            return ResponseEntity.ok().body(petitionService.sign(user, entityIdDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/signed")
    public ResponseEntity<?> getSignedPetition() {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.findSignedPetitions(user));
    }

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
