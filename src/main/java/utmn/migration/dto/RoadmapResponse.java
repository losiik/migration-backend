package utmn.migration.dto;

import java.util.List;

public record RoadmapResponse(
        List<RoadmapStep> steps
) {}