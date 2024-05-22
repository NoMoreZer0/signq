package com.kz.signq.controller;

import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.eds.EdsDto;
import com.kz.signq.dto.petition.PetitionDto;
import com.kz.signq.dto.petition.PetitionsDto;
import com.kz.signq.dto.petition.response.PetitionResponseDto;
import com.kz.signq.dto.signature.SignXmlDto;
import com.kz.signq.exception.EntityNotFoundException;
import com.kz.signq.exception.PetitionNotFoundException;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;
import com.kz.signq.service.PetitionService;
import com.kz.signq.service.PetitionStatusService;
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

    private final PetitionStatusService petitionStatusService;

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
    public ResponseEntity<List<PetitionResponseDto>> getAll(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ) {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(petitionService.getAll(user, page, size));
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

    @PostMapping("/signXml")
    public ResponseEntity<?> signXml(@RequestBody SignXmlDto signXmlDto) {
        var user = getCurrentUser();
        try {
            return ResponseEntity.ok().body(petitionService.signXml(signXmlDto, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ErrorCodeUtil.toExceptionDto(e));
        }
    }

    @PostMapping("/process")
    public ResponseEntity<?> process(@RequestParam("petition_id") UUID petitionId) {
        return ResponseEntity.ok().body(petitionStatusService.process(petitionId));
    }

    @PostMapping("/reject")
    public ResponseEntity<?> reject(@RequestParam("petition_id") UUID petitionId) {
        return ResponseEntity.ok().body(petitionStatusService.reject(petitionId));
    }

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }
}
