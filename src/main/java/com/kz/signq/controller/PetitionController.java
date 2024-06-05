package com.kz.signq.controller;

import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.MessageDto;
import com.kz.signq.dto.eds.EdsDto;
import com.kz.signq.dto.petition.PetitionDto;
import com.kz.signq.dto.petition.PetitionsDto;
import com.kz.signq.dto.petition.response.PetitionResponseDto;
import com.kz.signq.dto.signature.SignXmlDto;
import com.kz.signq.exception.PetitionNotFoundException;
import com.kz.signq.exception.SignException;
import com.kz.signq.model.User;
import com.kz.signq.service.PetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/petition")
@RequiredArgsConstructor
public class PetitionController {

    private final PetitionService petitionService;

    @PostMapping
    public ResponseEntity<EntityIdDto> create(@RequestBody PetitionDto petitionDto) {
        return ResponseEntity.ok().body(petitionService.create(petitionDto));
    }

    @PatchMapping("/{petitionId}")
    public ResponseEntity<EntityIdDto> update(@RequestBody PetitionDto dto, @PathVariable UUID petitionId) {
        return ResponseEntity.ok().body(petitionService.update(dto, petitionId));
    }

    @GetMapping
    public ResponseEntity<List<PetitionResponseDto>> getAll(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ) {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.getAll(user, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetitionResponseDto> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok().body(petitionService.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<PetitionsDto> getCreated() {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.getCreatedPetitions(user));
    }

    @GetMapping("/signed")
    public ResponseEntity<PetitionsDto> getSignedPetition() {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.findSignedPetitions(user));
    }


    @PostMapping("/signEds")
    public ResponseEntity<?> signEds(@RequestBody EdsDto dto) throws PetitionNotFoundException, NoSuchAlgorithmException, SignException {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.signEds(dto, user));
    }

    @PostMapping("/signXml")
    public ResponseEntity<MessageDto> signXml(@RequestBody SignXmlDto signXmlDto) {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.signXml(signXmlDto, user));
    }

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }
}
