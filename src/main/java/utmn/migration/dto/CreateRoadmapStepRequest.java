package utmn.migration.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateRoadmapStepRequest(
        @NotBlank(message = "Название шага обязательно")
        String receiveText,

        @NotBlank(message = "Описание шага обязательно")
        String actionText,

        Integer deadlineDays,

        @NotNull(message = "Порядок шага обязателен")
        Integer orderIndex,

        Boolean isActive,

        @Valid List<DisplayConditionRequest> conditions
) {
}
