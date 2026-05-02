package utmn.migration.service;

import utmn.migration.entity.RoadmapStep;

public record MatchedRoadmapStep(
        RoadmapStep step,
        int deadlineDays
) {
}
