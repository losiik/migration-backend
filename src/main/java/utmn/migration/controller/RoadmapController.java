package utmn.migration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utmn.migration.dto.RoadmapResponse;
import utmn.migration.service.RoadmapService;

@RestController
@RequestMapping("/api/roadmap")
public class RoadmapController {
    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @GetMapping
    public ResponseEntity<RoadmapResponse> getRoadmap(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(roadmapService.getRoadmap(userDetails.getUsername()));
    }
}
