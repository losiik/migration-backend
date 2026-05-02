package utmn.migration.dto;

import java.time.LocalDate;

public record ProfileDataResponse(
        Long id,
        String citizenshipCode,
        String otherCitizenship,
        String qualification,
        Boolean wasRegistered,
        Boolean resettlementProgram,
        LocalDate entryDate,
        String visitPurpose,
        Boolean plansEmployment,
        String patentNumber
) {
}
