package utmn.migration.dto;

public record RoadmapStep(
        int stepNumber,
        String title,
        String description,
        String deadline,
        StepStatus status
) {}