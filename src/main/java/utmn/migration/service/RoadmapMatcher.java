package utmn.migration.service;

import org.springframework.stereotype.Component;
import utmn.migration.entity.ConditionRule;
import utmn.migration.entity.DisplayCondition;
import utmn.migration.entity.Migrant;
import utmn.migration.entity.RoadmapStep;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RoadmapMatcher {

    public List<MatchedRoadmapStep> match(
            Migrant migrant,
            List<RoadmapStep> steps,
            List<DisplayCondition> conditions,
            List<ConditionRule> rules
    ) {
        Map<Long, List<DisplayCondition>> conditionsByStep = conditions.stream()
                .collect(Collectors.groupingBy(condition -> condition.getRoadmapStep().getId()));
        Map<Long, List<ConditionRule>> rulesByCondition = rules.stream()
                .collect(Collectors.groupingBy(rule -> rule.getDisplayCondition().getId()));

        List<MatchedRoadmapStep> result = new ArrayList<>();
        for (RoadmapStep step : steps) {
            List<DisplayCondition> stepConditions = conditionsByStep.getOrDefault(step.getId(), List.of());
            if (stepConditions.isEmpty()) {
                result.add(new MatchedRoadmapStep(step, normalizeDeadlineDays(step.getDeadlineDays())));
                continue;
            }

            List<DisplayCondition> matchedConditions = stepConditions.stream()
                    .filter(condition -> matchesCondition(migrant, condition, rulesByCondition.getOrDefault(condition.getId(), List.of())))
                    .sorted(Comparator
                            .comparingInt((DisplayCondition condition) -> rulesByCondition.getOrDefault(condition.getId(), List.of()).size())
                            .reversed()
                            .thenComparing(DisplayCondition::getId))
                    .toList();

            if (!matchedConditions.isEmpty()) {
                DisplayCondition selected = matchedConditions.get(0);
                int deadlineDays = normalizeDeadlineDays(selected.getDeadlineDays() != null ? selected.getDeadlineDays() : step.getDeadlineDays());
                result.add(new MatchedRoadmapStep(step, deadlineDays));
            }
        }

        return result;
    }

    private boolean matchesCondition(Migrant migrant, DisplayCondition condition, List<ConditionRule> rules) {
        if (rules.isEmpty()) {
            return true;
        }

        for (ConditionRule rule : rules) {
            if (!matchesRule(migrant, rule)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesRule(Migrant migrant, ConditionRule rule) {
        String key = rule.getRuleKey();
        String value = rule.getRuleValue();

        return switch (key) {
            case "CITIZENSHIP" -> migrant.getCitizenship() != null && migrant.getCitizenship().getCode().equalsIgnoreCase(value);
            case "WAS_REGISTERED" -> Boolean.toString(Boolean.TRUE.equals(migrant.getWasRegistered())).equalsIgnoreCase(value);
            case "RESETTLEMENT_PROGRAM" -> Boolean.toString(Boolean.TRUE.equals(migrant.getResettlementProgram())).equalsIgnoreCase(value);
            case "PLANS_EMPLOYMENT" -> Boolean.toString(Boolean.TRUE.equals(migrant.getPlansEmployment())).equalsIgnoreCase(value);
            case "VISIT_PURPOSE" -> migrant.getVisitPurpose() != null && migrant.getVisitPurpose().equalsIgnoreCase(value);
            case "QUALIFICATION" -> migrant.getQualification() != null && migrant.getQualification().toLowerCase().contains(value.toLowerCase());
            default -> false;
        };
    }

    private int normalizeDeadlineDays(Integer deadlineDays) {
        return deadlineDays == null ? 7 : deadlineDays;
    }
}
