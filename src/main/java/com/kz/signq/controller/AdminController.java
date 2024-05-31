package com.kz.signq.controller;

import com.kz.signq.dto.MessageDto;
import com.kz.signq.service.PetitionStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PetitionStatusService petitionStatusService;

    @PostMapping("/process")
    public ResponseEntity<MessageDto> process(@RequestParam("petition_id") UUID petitionId) {
        return ResponseEntity.ok().body(petitionStatusService.process(petitionId));
    }

    @PostMapping("/reject")
    public ResponseEntity<MessageDto> reject(@RequestParam("petition_id") UUID petitionId) {
        return ResponseEntity.ok().body(petitionStatusService.reject(petitionId));
    }
}
