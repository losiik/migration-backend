package utmn.migration.dto;

import jakarta.validation.constraints.NotBlank;

public record ConditionRuleRequest(
        @NotBlank(message = "Ключ правила обязателен")
        String ruleKey,

        @NotBlank(message = "Значение правила обязательно")
        String ruleValue
) {
}
