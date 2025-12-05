package utmn.migration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utmn.migration.dto.CitizenRequest;
import utmn.migration.dto.CitizenResponse;
import utmn.migration.entity.Citizen;
import utmn.migration.entity.Citizenship;
import utmn.migration.entity.User;
import utmn.migration.repository.CitizenRepository;
import utmn.migration.repository.UserRepository;

@Service
public class CitizenService {
    private final CitizenRepository citizenRepository;
    private final UserRepository userRepository;

    public CitizenService(CitizenRepository citizenRepository, UserRepository userRepository) {
        this.citizenRepository = citizenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CitizenResponse createOrUpdateCitizenData(String email, CitizenRequest request) {
        // Валидация для "Другое"
        if (request.citizenship() == Citizenship.OTHER &&
                (request.otherCitizenship() == null || request.otherCitizenship().isBlank())) {
            throw new IllegalArgumentException("Укажите страну гражданства");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        Citizen citizen = citizenRepository.findByUser(user)
                .orElse(new Citizen());

        citizen.setUser(user);
        citizen.setCitizenship(request.citizenship());
        citizen.setOtherCitizenship(request.otherCitizenship());
        citizen.setQualification(request.qualification());
        citizen.setWasRegistered(request.wasRegistered());
        citizen.setResettlementProgram(request.resettlementProgram());
        citizen.setEntryDate(request.entryDate());

        Citizen saved = citizenRepository.save(citizen);
        return mapToResponse(saved);
    }

    public CitizenResponse getCitizenData(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        Citizen citizen = citizenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Данные гражданина не найдены"));

        return mapToResponse(citizen);
    }

    private CitizenResponse mapToResponse(Citizen citizen) {
        return new CitizenResponse(
                citizen.getId(),
                citizen.getCitizenship(),
                citizen.getOtherCitizenship(),
                citizen.getQualification(),
                citizen.getWasRegistered(),
                citizen.getResettlementProgram(),
                citizen.getEntryDate()
        );
    }
}