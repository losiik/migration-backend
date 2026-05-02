package utmn.migration.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import utmn.migration.entity.Citizenship;
import utmn.migration.entity.ConditionRule;
import utmn.migration.entity.DisplayCondition;
import utmn.migration.entity.RoadmapStep;
import utmn.migration.entity.User;
import utmn.migration.entity.UserRole;
import utmn.migration.repository.CitizenshipRepository;
import utmn.migration.repository.ConditionRuleRepository;
import utmn.migration.repository.DisplayConditionRepository;
import utmn.migration.repository.RoadmapStepRepository;
import utmn.migration.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
            CitizenshipRepository citizenshipRepository,
            RoadmapStepRepository roadmapStepRepository,
            DisplayConditionRepository displayConditionRepository,
            ConditionRuleRepository conditionRuleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            seedCitizenships(citizenshipRepository);
            seedRoadmapRules(roadmapStepRepository, displayConditionRepository, conditionRuleRepository);
            backfillUserRoles(userRepository);
            seedAdmin(userRepository, passwordEncoder);
        };
    }

    private void seedCitizenships(CitizenshipRepository repository) {
        if (repository.count() > 0) {
            return;
        }

        repository.save(new Citizenship("UKRAINE", "Украина"));
        repository.save(new Citizenship("BELARUS", "Беларусь"));
        repository.save(new Citizenship("KYRGYZSTAN", "Киргизия"));
        repository.save(new Citizenship("KAZAKHSTAN", "Казахстан"));
        repository.save(new Citizenship("ARMENIA", "Армения"));
        repository.save(new Citizenship("TAJIKISTAN", "Таджикистан"));
        repository.save(new Citizenship("UZBEKISTAN", "Узбекистан"));
        repository.save(new Citizenship("OTHER", "Другое"));
    }

    private void seedRoadmapRules(
            RoadmapStepRepository stepRepository,
            DisplayConditionRepository conditionRepository,
            ConditionRuleRepository ruleRepository
    ) {
        if (stepRepository.count() > 0) {
            return;
        }

        RoadmapStep step = new RoadmapStep();
        step.setReceiveText("Постановка на миграционный учет");
        step.setActionText("После въезда в РФ необходимо оформить постановку на миграционный учет по месту пребывания.");
        step.setDeadlineDays(7);
        step.setOrderIndex(1);
        step.setIsActive(true);
        step = stepRepository.save(step);

        DisplayCondition defaultCondition = createCondition(conditionRepository, step, 7);
        DisplayCondition belarusCondition = createCondition(conditionRepository, step, 90);
        DisplayCondition eaeuCondition = createCondition(conditionRepository, step, 30);
        DisplayCondition tajikCondition = createCondition(conditionRepository, step, 15);
        DisplayCondition uzbekCondition = createCondition(conditionRepository, step, 15);
        DisplayCondition resettlementCondition = createCondition(conditionRepository, step, 30);

        createRule(ruleRepository, defaultCondition, "VISIT_PURPOSE", "Работа");
        createRule(ruleRepository, belarusCondition, "CITIZENSHIP", "BELARUS");
        createRule(ruleRepository, eaeuCondition, "CITIZENSHIP", "KYRGYZSTAN");
        createRule(ruleRepository, eaeuCondition, "CITIZENSHIP", "KAZAKHSTAN");
        createRule(ruleRepository, eaeuCondition, "CITIZENSHIP", "ARMENIA");
        createRule(ruleRepository, tajikCondition, "CITIZENSHIP", "TAJIKISTAN");
        createRule(ruleRepository, uzbekCondition, "CITIZENSHIP", "UZBEKISTAN");
        createRule(ruleRepository, resettlementCondition, "RESETTLEMENT_PROGRAM", "true");
    }

    private void backfillUserRoles(UserRepository userRepository) {
        boolean changed = false;

        for (User user : userRepository.findAll()) {
            if (user.getRole() == null) {
                user.setRole(UserRole.USER);
                changed = true;
            }
        }

        if (changed) {
            userRepository.flush();
        }
    }

    private void seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        User admin = userRepository.findByEmail("admin@example.com").orElse(null);
        if (admin == null) {
            admin = new User();
            admin.setName("Administrator");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
        }

        if (admin.getRole() != UserRole.ADMIN) {
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
        }
    }

    private DisplayCondition createCondition(DisplayConditionRepository repository, RoadmapStep step, Integer deadlineDays) {
        DisplayCondition condition = new DisplayCondition();
        condition.setRoadmapStep(step);
        condition.setDeadlineDays(deadlineDays);
        return repository.save(condition);
    }

    private void createRule(ConditionRuleRepository repository, DisplayCondition condition, String key, String value) {
        ConditionRule rule = new ConditionRule();
        rule.setDisplayCondition(condition);
        rule.setRuleKey(key);
        rule.setRuleValue(value);
        repository.save(rule);
    }
}
