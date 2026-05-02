package utmn.migration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record SaveProfileRequest(
        @NotBlank(message = "Гражданство обязательно")
        String citizenshipCode,

        String otherCitizenship,

        @NotBlank(message = "Квалификация обязательна")
        String qualification,

        @NotNull(message = "Дата въезда обязательна")
        @PastOrPresent(message = "Дата въезда не может быть в будущем")
        LocalDate entryDate,

        @NotNull(message = "Укажите, стояли ли вы на учете")
        Boolean wasRegistered,

        @NotNull(message = "Укажите участие в программе переселения")
        Boolean resettlementProgram,

        @NotBlank(message = "Цель визита обязательна")
        String visitPurpose,

        @NotNull(message = "Укажите, планируете ли вы трудоустройство")
        Boolean plansEmployment,

        String patentNumber
) {
}
