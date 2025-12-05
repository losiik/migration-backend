package utmn.migration.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import utmn.migration.dto.CitizenRequest;
import utmn.migration.dto.CitizenResponse;
import utmn.migration.service.CitizenService;

@RestController
@RequestMapping("/api/citizen")
public class CitizenController {
    private final CitizenService citizenService;

    public CitizenController(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    @PostMapping
    public ResponseEntity<CitizenResponse> createOrUpdateCitizenData(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CitizenRequest request) {
        CitizenResponse response = citizenService.createOrUpdateCitizenData(
                userDetails.getUsername(),
                request
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CitizenResponse> getCitizenData(
            @AuthenticationPrincipal UserDetails userDetails) {
        CitizenResponse response = citizenService.getCitizenData(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}