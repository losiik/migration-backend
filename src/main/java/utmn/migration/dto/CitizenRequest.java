package utmn.migration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import utmn.migration.entity.Citizenship;

import java.time.LocalDate;

public record CitizenRequest(
        @NotNull(message = "Гражданство обязательно")
        Citizenship citizenship,

        String otherCitizenship, // Обязательно только если citizenship = OTHER

        @NotBlank(message = "Квалификация обязательна")
        String qualification,

        @NotNull(message = "Укажите, стояли ли на учете")
        Boolean wasRegistered,

        @NotNull(message = "Укажите участие в программе переселения")
        Boolean resettlementProgram,

        @NotNull(message = "Дата въезда обязательна")
        @PastOrPresent(message = "Дата въезда не может быть в будущем")
        LocalDate entryDate
) {}