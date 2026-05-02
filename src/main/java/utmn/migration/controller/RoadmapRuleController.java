package utmn.migration.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utmn.migration.dto.CreateRoadmapStepRequest;
import utmn.migration.dto.CreateRoadmapStepResponse;
import utmn.migration.dto.DeleteConfirmationResponse;
import utmn.migration.dto.DeleteRoadmapStepResponse;
import utmn.migration.dto.RoadmapRuleFormResponse;
import utmn.migration.dto.RoadmapRuleListResponse;
import utmn.migration.service.RoadmapRuleService;

@RestController
@RequestMapping("/api/admin/roadmap-rules")
public class RoadmapRuleController {
    private final RoadmapRuleService roadmapRuleService;

    public RoadmapRuleController(RoadmapRuleService roadmapRuleService) {
        this.roadmapRuleService = roadmapRuleService;
    }

    @GetMapping("/form")
    public ResponseEntity<RoadmapRuleFormResponse> getCreateForm() {
        return ResponseEntity.ok(roadmapRuleService.getCreateForm());
    }

    @PostMapping
    public ResponseEntity<CreateRoadmapStepResponse> createStep(@Valid @RequestBody CreateRoadmapStepRequest request) {
        return ResponseEntity.ok(roadmapRuleService.createStep(request));
    }

    @GetMapping
    public ResponseEntity<RoadmapRuleListResponse> getRuleList() {
        return ResponseEntity.ok(roadmapRuleService.getRuleList());
    }

    @GetMapping("/{stepId}/delete-preview")
    public ResponseEntity<DeleteConfirmationResponse> prepareDelete(@PathVariable Long stepId) {
        return ResponseEntity.ok(roadmapRuleService.prepareDelete(stepId));
    }

    @DeleteMapping("/{stepId}")
    public ResponseEntity<DeleteRoadmapStepResponse> deleteStep(@PathVariable Long stepId) {
        return ResponseEntity.ok(roadmapRuleService.deleteStep(stepId));
    }
}
