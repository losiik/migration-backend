package utmn.migration.dto;

import java.util.List;

public record RoadmapRuleResponse(
        Long id,
        String receiveText,
        String actionText,
        Integer deadlineDays,
        Integer orderIndex,
        Boolean isActive,
        List<DisplayConditionResponse> conditions
) {
}
