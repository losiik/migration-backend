package utmn.migration.service;

import org.springframework.stereotype.Service;
import utmn.migration.dto.RoadmapResponse;
import utmn.migration.dto.RoadmapStep;
import utmn.migration.dto.StepStatus;
import utmn.migration.entity.Citizen;
import utmn.migration.entity.Citizenship;
import utmn.migration.entity.User;
import utmn.migration.repository.CitizenRepository;
import utmn.migration.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoadmapService {
    private final CitizenRepository citizenRepository;
    private final UserRepository userRepository;

    public RoadmapService(CitizenRepository citizenRepository, UserRepository userRepository) {
        this.citizenRepository = citizenRepository;
        this.userRepository = userRepository;
    }

    public RoadmapResponse generateRoadmap(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        Citizen citizen = citizenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Данные гражданина не найдены. Пожалуйста, заполните анкету."));

        List<RoadmapStep> steps = new ArrayList<>();

        steps.add(createMigrationRegistrationStep(citizen));

        return new RoadmapResponse(steps);
    }

    private RoadmapStep createMigrationRegistrationStep(Citizen citizen) {
        String deadline = determineRegistrationDeadline(citizen);

        String description = String.format(
                "%s со дня въезда на территорию РФ вам необходимо обратиться к лицу, у которого проживаете, " +
                        "для того чтобы собственник помещения направил уведомление в УМВД через портал государственных услуг " +
                        "о постановке на миграционный учет иностранного гражданина по месту пребывания",
                deadline
        );

        return new RoadmapStep(
                1,
                "Постановка на миграционный учет",
                description,
                deadline,
                StepStatus.PENDING
        );
    }

    private String determineRegistrationDeadline(Citizen citizen) {
        if (Boolean.TRUE.equals(citizen.getResettlementProgram())) {
            return "в течение 30 дней";
        }

        Citizenship citizenship = citizen.getCitizenship();

        if (citizenship == Citizenship.BELARUS) {
            return "территорию РФ вам необходимо";
        }

        if (citizenship == Citizenship.KYRGYZSTAN ||
                citizenship == Citizenship.KAZAKHSTAN ||
                citizenship == Citizenship.ARMENIA) {
            return "в течение 30 дней";
        }

        if (citizenship == Citizenship.TAJIKISTAN ||
                citizenship == Citizenship.UZBEKISTAN) {
            return "в течение 15 дней";
        }

        return "в течение 7 дней";
    }
}