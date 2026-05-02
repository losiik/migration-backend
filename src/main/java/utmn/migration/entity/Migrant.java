package utmn.migration.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "migrants")
public class Migrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizenship_id", nullable = false)
    private Citizenship citizenship;

    @Column(name = "other_citizenship")
    private String otherCitizenship;

    @Column(nullable = false)
    private String qualification;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "was_registered", nullable = false)
    private Boolean wasRegistered;

    @Column(name = "resettlement_program", nullable = false)
    private Boolean resettlementProgram;

    @Column(name = "visit_purpose", nullable = false)
    private String visitPurpose;

    @Column(name = "plans_employment", nullable = false)
    private Boolean plansEmployment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Citizenship getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(Citizenship citizenship) {
        this.citizenship = citizenship;
    }

    public String getOtherCitizenship() {
        return otherCitizenship;
    }

    public void setOtherCitizenship(String otherCitizenship) {
        this.otherCitizenship = otherCitizenship;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public Boolean getWasRegistered() {
        return wasRegistered;
    }

    public void setWasRegistered(Boolean wasRegistered) {
        this.wasRegistered = wasRegistered;
    }

    public Boolean getResettlementProgram() {
        return resettlementProgram;
    }

    public void setResettlementProgram(Boolean resettlementProgram) {
        this.resettlementProgram = resettlementProgram;
    }

    public String getVisitPurpose() {
        return visitPurpose;
    }

    public void setVisitPurpose(String visitPurpose) {
        this.visitPurpose = visitPurpose;
    }

    public Boolean getPlansEmployment() {
        return plansEmployment;
    }

    public void setPlansEmployment(Boolean plansEmployment) {
        this.plansEmployment = plansEmployment;
    }
}
