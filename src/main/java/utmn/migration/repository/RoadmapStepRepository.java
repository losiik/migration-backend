package utmn.migration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utmn.migration.entity.RoadmapStep;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoadmapStepRepository extends JpaRepository<RoadmapStep, Long> {
    List<RoadmapStep> findAllByIsActiveTrueOrderByOrderIndexAsc();
    List<RoadmapStep> findAllByOrderByOrderIndexAsc();
    Optional<RoadmapStep> findByIdAndIsActiveTrue(Long id);

    @Query("select coalesce(max(r.orderIndex), 0) + 1 from RoadmapStep r")
    Integer getNextOrderIndex();
}
