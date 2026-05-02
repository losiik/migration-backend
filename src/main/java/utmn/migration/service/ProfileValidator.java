package utmn.migration.service;

import org.springframework.stereotype.Component;
import utmn.migration.dto.SaveProfileRequest;

import java.time.LocalDate;

@Component
public class ProfileValidator {

    public void validate(SaveProfileRequest request) {
        if (request.citizenshipCode() == null || request.citizenshipCode().isBlank()) {
            throw new IllegalArgumentException("Гражданство обязательно");
        }

        if ("OTHER".equalsIgnoreCase(request.citizenshipCode()) &&
                (request.otherCitizenship() == null || request.otherCitizenship().isBlank())) {
            throw new IllegalArgumentException("Укажите страну гражданства");
        }

        if (request.entryDate() != null && request.entryDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата въезда не может быть в будущем");
        }
    }
}
