package hu.emarsys.duedatecalculator;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DueDateCalculatorValidator {

    static final LocalTime START_OF_WORKDAY = LocalTime.of(9, 0);
    static final LocalTime END_OF_WORKDAY = LocalTime.of(17, 0);

    public void validateCalculateDueDateParameters(LocalDateTime submitDate, int turnaroundTime) {
        validateSubmitDate(submitDate);
        validateTurnaroundTime(turnaroundTime);
    }

    public boolean checkIfInWorkingHours(LocalTime timeOfDay) {
        return !START_OF_WORKDAY.isAfter(timeOfDay) && !END_OF_WORKDAY.isBefore(timeOfDay);
    }

    private void validateSubmitDate(LocalDateTime submitDate) {
        if (null == submitDate) {
            throw new DateTimeException("Invalid submit date: null");
        }

        if (    DayOfWeek.SATURDAY == submitDate.getDayOfWeek()
             || DayOfWeek.SUNDAY == submitDate.getDayOfWeek()) {
            throw new DateTimeException("Submit date is not a working day: " + submitDate.getDayOfWeek());
        }

        if (!checkIfInWorkingHours(submitDate.toLocalTime())) {
            throw new DateTimeException("Submit date is not in working hours: " + submitDate);
        }
    }

    private void validateTurnaroundTime(int turnaroundTime) {
        if (turnaroundTime < 0) {
            throw new DateTimeException("Invalid turnaround time: " + turnaroundTime);
        }
    }

}
