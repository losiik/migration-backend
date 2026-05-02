package utmn.migration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utmn.migration.dto.*;
import utmn.migration.entity.Citizenship;
import utmn.migration.entity.Migrant;
import utmn.migration.entity.Patent;
import utmn.migration.entity.User;
import utmn.migration.repository.CitizenshipRepository;
import utmn.migration.repository.MigrantRepository;
import utmn.migration.repository.PatentRepository;

import java.util.List;

@Service
public class ProfileService {
    private final UserService userService;
    private final MigrantRepository migrantRepository;
    private final CitizenshipRepository citizenshipRepository;
    private final PatentRepository patentRepository;
    private final ProfileValidator profileValidator;

    public ProfileService(
            UserService userService,
            MigrantRepository migrantRepository,
            CitizenshipRepository citizenshipRepository,
            PatentRepository patentRepository,
            ProfileValidator profileValidator
    ) {
        this.userService = userService;
        this.migrantRepository = migrantRepository;
        this.citizenshipRepository = citizenshipRepository;
        this.patentRepository = patentRepository;
        this.profileValidator = profileValidator;
    }

    @Transactional(readOnly = true)
    public ProfileFormResponse getProfileForm(String email) {
        User user = userService.findByEmail(email);
        ProfileDataResponse profile = migrantRepository.findByUser(user)
                .map(this::toProfileData)
                .orElse(null);

        List<CitizenshipOptionResponse> citizenships = citizenshipRepository.findAllByOrderByCountryAsc().stream()
                .map(item -> new CitizenshipOptionResponse(item.getId(), item.getCode(), item.getCountry()))
                .toList();

        return new ProfileFormResponse(profile, citizenships);
    }

    @Transactional
    public SaveProfileResponse saveProfile(String email, SaveProfileRequest request) {
        profileValidator.validate(request);

        User user = userService.findByEmail(email);
        Citizenship citizenship = citizenshipRepository.findByCodeIgnoreCase(request.citizenshipCode())
                .orElseThrow(() -> new IllegalArgumentException("Гражданство не найдено"));

        Migrant migrant = migrantRepository.findByUser(user).orElseGet(Migrant::new);
        migrant.setUser(user);
        migrant.setCitizenship(citizenship);
        migrant.setOtherCitizenship(blankToNull(request.otherCitizenship()));
        migrant.setQualification(request.qualification().trim());
        migrant.setEntryDate(request.entryDate());
        migrant.setWasRegistered(request.wasRegistered());
        migrant.setResettlementProgram(request.resettlementProgram());
        migrant.setVisitPurpose(request.visitPurpose().trim());
        migrant.setPlansEmployment(request.plansEmployment());
        Migrant saved = migrantRepository.save(migrant);

        String patentNumber = blankToNull(request.patentNumber());
        if (patentNumber != null) {
            Patent patent = patentRepository.findByMigrantId(saved.getId()).orElseGet(Patent::new);
            patent.setMigrant(saved);
            patent.setNumber(patentNumber);
            if (patent.getStatus() == null || patent.getStatus().isBlank()) {
                patent.setStatus("ACTIVE");
            }
            patentRepository.save(patent);
        } else {
            patentRepository.findByMigrantId(saved.getId()).ifPresent(existing -> patentRepository.deleteByMigrantId(saved.getId()));
        }

        ProfileDataResponse profile = toProfileData(saved);
        return new SaveProfileResponse(
                "Анкета сохранена",
                true,
                "/roadmap",
                profile
        );
    }

    private ProfileDataResponse toProfileData(Migrant migrant) {
        String patentNumber = patentRepository.findByMigrantId(migrant.getId())
                .map(Patent::getNumber)
                .orElse(null);

        return new ProfileDataResponse(
                migrant.getId(),
                migrant.getCitizenship() != null ? migrant.getCitizenship().getCode() : null,
                migrant.getOtherCitizenship(),
                migrant.getQualification(),
                migrant.getWasRegistered(),
                migrant.getResettlementProgram(),
                migrant.getEntryDate(),
                migrant.getVisitPurpose(),
                migrant.getPlansEmployment(),
                patentNumber
        );
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
