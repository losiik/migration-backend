package utmn.migration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utmn.migration.entity.ConditionRule;
import utmn.migration.entity.DisplayCondition;

import java.util.Collection;
import java.util.List;

@Repository
public interface ConditionRuleRepository extends JpaRepository<ConditionRule, Long> {
    List<ConditionRule> findAllByDisplayConditionIn(Collection<DisplayCondition> conditions);
    List<ConditionRule> findAllByDisplayConditionIdIn(Collection<Long> conditionIds);
    void deleteAllByDisplayConditionIdIn(Collection<Long> conditionIds);
}
