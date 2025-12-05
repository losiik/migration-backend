package utmn.migration.dto;

import utmn.migration.entity.Citizenship;

import java.time.LocalDate;

public record CitizenResponse(
        Long id,
        Citizenship citizenship,
        String otherCitizenship,
        String qualification,
        Boolean wasRegistered,
        Boolean resettlementProgram,
        LocalDate entryDate
) {}