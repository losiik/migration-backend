package utmn.migration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utmn.migration.entity.DisplayCondition;
import utmn.migration.entity.RoadmapStep;

import java.util.Collection;
import java.util.List;

@Repository
public interface DisplayConditionRepository extends JpaRepository<DisplayCondition, Long> {
    List<DisplayCondition> findAllByRoadmapStepIn(Collection<RoadmapStep> steps);
    List<DisplayCondition> findAllByRoadmapStepId(Long stepId);
    void deleteAllByRoadmapStepId(Long stepId);
}
