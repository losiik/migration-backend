package utmn.migration.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utmn.migration.dto.ProfileFormResponse;
import utmn.migration.dto.SaveProfileRequest;
import utmn.migration.dto.SaveProfileResponse;
import utmn.migration.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/form")
    public ResponseEntity<ProfileFormResponse> getProfileForm(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(profileService.getProfileForm(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<SaveProfileResponse> saveProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody SaveProfileRequest request
    ) {
        return ResponseEntity.ok(profileService.saveProfile(userDetails.getUsername(), request));
    }
}
