package utmn.migration.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utmn.migration.dto.*;
import utmn.migration.entity.Citizenship;
import utmn.migration.entity.ConditionRule;
import utmn.migration.entity.DisplayCondition;
import utmn.migration.entity.RoadmapStep;
import utmn.migration.repository.CitizenshipRepository;
import utmn.migration.repository.ConditionRuleRepository;
import utmn.migration.repository.DisplayConditionRepository;
import utmn.migration.repository.RoadmapStepRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoadmapRuleService {
    private static final List<String> SUPPORTED_RULE_KEYS = List.of(
            "CITIZENSHIP",
            "WAS_REGISTERED",
            "RESETTLEMENT_PROGRAM",
            "VISIT_PURPOSE",
            "PLANS_EMPLOYMENT",
            "QUALIFICATION"
    );

    private final RoadmapStepRepository roadmapStepRepository;
    private final DisplayConditionRepository displayConditionRepository;
    private final ConditionRuleRepository conditionRuleRepository;
    private final CitizenshipRepository citizenshipRepository;

    public RoadmapRuleService(
            RoadmapStepRepository roadmapStepRepository,
            DisplayConditionRepository displayConditionRepository,
            ConditionRuleRepository conditionRuleRepository,
            CitizenshipRepository citizenshipRepository
    ) {
        this.roadmapStepRepository = roadmapStepRepository;
        this.displayConditionRepository = displayConditionRepository;
        this.conditionRuleRepository = conditionRuleRepository;
        this.citizenshipRepository = citizenshipRepository;
    }

    @Transactional(readOnly = true)
    public RoadmapRuleFormResponse getCreateForm() {
        List<CitizenshipOptionResponse> citizenships = citizenshipRepository.findAllByOrderByCountryAsc().stream()
                .map(this::toCitizenshipResponse)
                .toList();
        return new RoadmapRuleFormResponse(roadmapStepRepository.getNextOrderIndex(), SUPPORTED_RULE_KEYS, citizenships);
    }

    @Transactional
    public CreateRoadmapStepResponse createStep(CreateRoadmapStepRequest request) {
        RoadmapStep step = new RoadmapStep();
        step.setReceiveText(request.receiveText().trim());
        step.setActionText(request.actionText().trim());
        step.setDeadlineDays(request.deadlineDays());
        step.setOrderIndex(request.orderIndex());
        step.setIsActive(request.isActive() == null || request.isActive());
        RoadmapStep savedStep = roadmapStepRepository.save(step);

        if (request.conditions() != null) {
            for (DisplayConditionRequest conditionRequest : request.conditions()) {
                DisplayCondition condition = new DisplayCondition();
                condition.setRoadmapStep(savedStep);
                condition.setDeadlineDays(conditionRequest.deadlineDays());
                DisplayCondition savedCondition = displayConditionRepository.save(condition);

                if (conditionRequest.rules() != null && !conditionRequest.rules().isEmpty()) {
                    List<ConditionRule> rules = conditionRequest.rules().stream().map(ruleRequest -> {
                        ConditionRule rule = new ConditionRule();
                        rule.setDisplayCondition(savedCondition);
                        rule.setRuleKey(ruleRequest.ruleKey().trim());
                        rule.setRuleValue(ruleRequest.ruleValue().trim());
                        return rule;
                    }).toList();
                    conditionRuleRepository.saveAll(rules);
                }
            }
        }

        return new CreateRoadmapStepResponse(savedStep.getId(), "Правило успешно создано");
    }

    @Transactional(readOnly = true)
    public RoadmapRuleListResponse getRuleList() {
        List<RoadmapStep> steps = roadmapStepRepository.findAllByOrderByOrderIndexAsc();
        List<DisplayCondition> conditions = displayConditionRepository.findAllByRoadmapStepIn(steps);
        List<ConditionRule> rules = conditionRuleRepository.findAllByDisplayConditionIn(conditions);

        Map<Long, List<DisplayCondition>> conditionsByStepId = conditions.stream()
                .collect(Collectors.groupingBy(condition -> condition.getRoadmapStep().getId()));
        Map<Long, List<ConditionRule>> rulesByConditionId = rules.stream()
                .collect(Collectors.groupingBy(rule -> rule.getDisplayCondition().getId()));

        List<RoadmapRuleResponse> responseSteps = steps.stream()
                .map(step -> new RoadmapRuleResponse(
                        step.getId(),
                        step.getReceiveText(),
                        step.getActionText(),
                        step.getDeadlineDays(),
                        step.getOrderIndex(),
                        step.getIsActive(),
                        conditionsByStepId.getOrDefault(step.getId(), List.of()).stream()
                                .map(condition -> new DisplayConditionResponse(
                                        condition.getId(),
                                        condition.getDeadlineDays(),
                                        rulesByConditionId.getOrDefault(condition.getId(), List.of()).stream()
                                                .map(rule -> new ConditionRuleResponse(rule.getId(), rule.getRuleKey(), rule.getRuleValue()))
                                                .toList()
                                ))
                                .toList()
                ))
                .toList();

        return new RoadmapRuleListResponse(responseSteps);
    }

    @Transactional(readOnly = true)
    public DeleteConfirmationResponse prepareDelete(Long stepId) {
        RoadmapStep step = roadmapStepRepository.findById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Шаг не найден"));
        List<DisplayCondition> conditions = displayConditionRepository.findAllByRoadmapStepId(stepId);
        List<Long> conditionIds = conditions.stream().map(DisplayCondition::getId).toList();
        int ruleCount = conditionIds.isEmpty()
                ? 0
                : conditionRuleRepository.findAllByDisplayConditionIdIn(conditionIds).size();

        return new DeleteConfirmationResponse(step.getId(), step.getReceiveText(), conditions.size(), ruleCount, true);
    }

    @Transactional
    public DeleteRoadmapStepResponse deleteStep(Long stepId) {
        RoadmapStep step = roadmapStepRepository.findById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Шаг не найден"));
        List<Long> conditionIds = displayConditionRepository.findAllByRoadmapStepId(stepId).stream()
                .map(DisplayCondition::getId)
                .toList();

        if (!conditionIds.isEmpty()) {
            conditionRuleRepository.deleteAllByDisplayConditionIdIn(conditionIds);
        }
        displayConditionRepository.deleteAllByRoadmapStepId(stepId);
        roadmapStepRepository.delete(step);

        return new DeleteRoadmapStepResponse(step.getId(), "Шаг удален");
    }

    private CitizenshipOptionResponse toCitizenshipResponse(Citizenship item) {
        return new CitizenshipOptionResponse(item.getId(), item.getCode(), item.getCountry());
    }
}
