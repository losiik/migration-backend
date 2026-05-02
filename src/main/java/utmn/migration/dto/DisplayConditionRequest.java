package utmn.migration.dto;

import jakarta.validation.Valid;

import java.util.List;

public record DisplayConditionRequest(
        Integer deadlineDays,
        @Valid List<ConditionRuleRequest> rules
) {
}
