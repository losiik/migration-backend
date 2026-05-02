package utmn.migration.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "display_conditions")
public class DisplayCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_step_id", nullable = false)
    private RoadmapStep roadmapStep;

    @Column(name = "deadline_days")
    private Integer deadlineDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoadmapStep getRoadmapStep() {
        return roadmapStep;
    }

    public void setRoadmapStep(RoadmapStep roadmapStep) {
        this.roadmapStep = roadmapStep;
    }

    public Integer getDeadlineDays() {
        return deadlineDays;
    }

    public void setDeadlineDays(Integer deadlineDays) {
        this.deadlineDays = deadlineDays;
    }
}
