package utmn.migration.dto;

import java.util.List;

public record RoadmapRuleFormResponse(
        Integer nextOrderIndex,
        List<String> supportedRuleKeys,
        List<CitizenshipOptionResponse> citizenships
) {
}
