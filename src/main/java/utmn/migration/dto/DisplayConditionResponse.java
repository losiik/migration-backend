package utmn.migration.dto;

import java.util.List;

public record DisplayConditionResponse(
        Long id,
        Integer deadlineDays,
        List<ConditionRuleResponse> rules
) {
}
