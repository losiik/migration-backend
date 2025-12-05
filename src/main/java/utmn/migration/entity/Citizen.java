package utmn.migration.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "citizens")
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Citizenship citizenship;

    @Column(name = "other_citizenship")
    private String otherCitizenship;

    @Column(nullable = false)
    private String qualification;

    @Column(name = "was_registered", nullable = false)
    private Boolean wasRegistered;

    @Column(name = "resettlement_program", nullable = false)
    private Boolean resettlementProgram;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    // Конструкторы
    public Citizen() {}

    public Citizen(User user, Citizenship citizenship, String qualification,
                   Boolean wasRegistered, Boolean resettlementProgram, LocalDate entryDate) {
        this.user = user;
        this.citizenship = citizenship;
        this.qualification = qualification;
        this.wasRegistered = wasRegistered;
        this.resettlementProgram = resettlementProgram;
        this.entryDate = entryDate;
    }

    // Геттеры и сеттеры
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

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
}