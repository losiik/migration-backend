package utmn.migration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utmn.migration.dto.RoadmapResponse;
import utmn.migration.dto.RoadmapStepResponse;
import utmn.migration.dto.StepStatus;
import utmn.migration.entity.ConditionRule;
import utmn.migration.entity.DisplayCondition;
import utmn.migration.entity.Migrant;
import utmn.migration.entity.RoadmapStep;
import utmn.migration.entity.User;
import utmn.migration.repository.ConditionRuleRepository;
import utmn.migration.repository.DisplayConditionRepository;
import utmn.migration.repository.MigrantRepository;
import utmn.migration.repository.RoadmapStepRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RoadmapService {
    private static final DateTimeFormatter DEADLINE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final UserService userService;
    private final MigrantRepository migrantRepository;
    private final RoadmapStepRepository roadmapStepRepository;
    private final DisplayConditionRepository displayConditionRepository;
    private final ConditionRuleRepository conditionRuleRepository;
    private final RoadmapMatcher roadmapMatcher;
    private final DeadlineCalculator deadlineCalculator;

    public RoadmapService(
            UserService userService,
            MigrantRepository migrantRepository,
            RoadmapStepRepository roadmapStepRepository,
            DisplayConditionRepository displayConditionRepository,
            ConditionRuleRepository conditionRuleRepository,
            RoadmapMatcher roadmapMatcher,
            DeadlineCalculator deadlineCalculator
    ) {
        this.userService = userService;
        this.migrantRepository = migrantRepository;
        this.roadmapStepRepository = roadmapStepRepository;
        this.displayConditionRepository = displayConditionRepository;
        this.conditionRuleRepository = conditionRuleRepository;
        this.roadmapMatcher = roadmapMatcher;
        this.deadlineCalculator = deadlineCalculator;
    }

    @Transactional(readOnly = true)
    public RoadmapResponse getRoadmap(String email) {
        User user = userService.findByEmail(email);
        Migrant migrant = migrantRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Данные мигранта не найдены. Пожалуйста, заполните анкету."));

        List<RoadmapStep> steps = roadmapStepRepository.findAllByIsActiveTrueOrderByOrderIndexAsc();
        List<DisplayCondition> conditions = displayConditionRepository.findAllByRoadmapStepIn(steps);
        List<ConditionRule> rules = conditionRuleRepository.findAllByDisplayConditionIn(conditions);
        List<MatchedRoadmapStep> matchedSteps = roadmapMatcher.match(migrant, steps, conditions, rules);

        AtomicInteger counter = new AtomicInteger(1);
        List<RoadmapStepResponse> responseSteps = matchedSteps.stream()
                .map(matched -> new RoadmapStepResponse(
                        matched.step().getId(),
                        counter.getAndIncrement(),
                        matched.step().getReceiveText(),
                        matched.step().getActionText(),
                        deadlineCalculator.calculate(migrant.getEntryDate(), matched.deadlineDays()).format(DEADLINE_FORMATTER),
                        StepStatus.PENDING
                ))
                .toList();

        return new RoadmapResponse(responseSteps);
    }
}
