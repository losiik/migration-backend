package utmn.migration.dto;

public record RoadmapStepResponse(
        Long id,
        int stepNumber,
        String title,
        String description,
        String deadline,
        StepStatus status
) {
}
