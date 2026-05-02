package utmn.migration.dto;

import java.util.List;

public record RoadmapRuleListResponse(
        List<RoadmapRuleResponse> steps
) {
}
