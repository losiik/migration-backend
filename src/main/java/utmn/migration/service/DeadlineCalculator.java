package utmn.migration.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DeadlineCalculator {

    public LocalDate calculate(LocalDate entryDate, int deadlineDays) {
        return entryDate.plusDays(deadlineDays);
    }
}
