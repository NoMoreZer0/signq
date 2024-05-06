package com.kz.signq.controller;

import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.eds.EdsDto;
import com.kz.signq.dto.petition.PetitionDto;
import com.kz.signq.dto.petition.PetitionsDto;
import com.kz.signq.dto.petition.response.PetitionResponseDto;
import com.kz.signq.exception.EntityNotFoundException;
import com.kz.signq.exception.PetitionNotFoundException;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;
import com.kz.signq.service.PetitionService;
import com.kz.signq.utils.ErrorCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/petition")
@RequiredArgsConstructor
public class PetitionController {

    private final PetitionService petitionService;

    @PostMapping
    public ResponseEntity<EntityIdDto> create(
            @RequestBody PetitionDto petitionDto
    ) {
        return ResponseEntity.ok().body(petitionService.create(petitionDto));
    }

    @PatchMapping("/{petitionId}")
    public ResponseEntity<?> update(@RequestBody PetitionDto dto, @PathVariable UUID petitionId) {
        try {
            return ResponseEntity.ok().body(petitionService.update(dto, petitionId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(ErrorCodeUtil.toExceptionDto(e));
        }
    }

    @GetMapping
    public ResponseEntity<List<PetitionResponseDto>> getAll() {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.getAll(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Petition> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok().body(petitionService.getById(id).orElse(null));
    }

    @GetMapping("/my")
    public ResponseEntity<PetitionsDto> getCreated() {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.getCreatedPetitions(user));
    }

    @PostMapping("/isMy")
    public ResponseEntity<?> isMyPetition(@RequestBody EntityIdDto entityIdDto) {
        try {
            var user = getCurrentUser();
            return ResponseEntity.ok().body(petitionService.isMyPetition(user, entityIdDto));
        } catch (PetitionNotFoundException e) {
            return ResponseEntity.badRequest().body(ErrorCodeUtil.toExceptionDto(e));
        }
    }

    @PostMapping("/sign")
    public ResponseEntity<?> sign(@RequestBody EntityIdDto entityIdDto) {
        try {
            var user = getCurrentUser();
            return ResponseEntity.ok().body(petitionService.sign(user, entityIdDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorCodeUtil.toExceptionDto(e));
        }
    }

    @GetMapping("/signed")
    public ResponseEntity<PetitionsDto> getSignedPetition() {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.findSignedPetitions(user));
    }


    @PostMapping("/signEds")
    public ResponseEntity<?> signEds(@RequestBody EdsDto dto) {
        var user = getCurrentUser();
        try {
            return ResponseEntity.ok().body(petitionService.signEds(dto, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorCodeUtil.toExceptionDto(e));
        }
    }

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }
}
