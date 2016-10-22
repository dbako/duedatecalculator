package hu.emarsys.duedatecalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

public class TestDueDateCalculatorValidator {

    private static final LocalDate DATE_FRIDAY = LocalDate.of(2016, 10, 21); //2016. 10. 21. is Friday
    private static final LocalDate DATE_SATURDAY = LocalDate.of(2016, 10, 22); //2016. 10. 22. is Saturday
    private static final LocalTime TIME_8AM = LocalTime.of(8, 0);
    private static final LocalTime TIME_10AM = LocalTime.of(10, 0);
    private static final LocalTime TIME_6PM = LocalTime.of(18, 0);

    @Test
    public void testValidateNullStartDate() {
        DueDateCalculatorValidator validator = new DueDateCalculatorValidator();
        try {
            validator.validateCalculateDueDateParameters(null, 2);
            fail();
        } catch (Exception e) {
            assertTrue((e instanceof DateTimeException));
            assertEquals("Null date exception is expected", "Invalid submit date: null", e.getMessage());
        }
    }

    @Test
    public void testValidateStartDateIsWeekend() {
        DueDateCalculatorValidator validator = new DueDateCalculatorValidator();
        try {
           validator.validateCalculateDueDateParameters(LocalDateTime.of(DATE_SATURDAY, TIME_10AM), 2);
        } catch (Exception e) {
            assertTrue((e instanceof DateTimeException));
            assertEquals("Weekend date exception is expected", "Submit date is not a working day: " + DayOfWeek.SATURDAY, e.getMessage());
        }
    }

    @Test
    public void testValidateStartDateBeforeWorkingHours() {
        DueDateCalculatorValidator validator = new DueDateCalculatorValidator();
        try {
           validator.validateCalculateDueDateParameters(LocalDateTime.of(DATE_FRIDAY, TIME_8AM), 2);
        } catch (Exception e) {
            assertTrue((e instanceof DateTimeException));
            assertEquals("Out of working hours exception is expected",
                    "Submit date is not in working hours: " + LocalDateTime.of(DATE_FRIDAY, TIME_8AM), e.getMessage());
        }
    }

    @Test
    public void testValidateStartDateAfterWorkingHours() {
        DueDateCalculatorValidator validator = new DueDateCalculatorValidator();
        try {
            validator.validateCalculateDueDateParameters(LocalDateTime.of(DATE_FRIDAY, TIME_6PM), 2);
         } catch (Exception e) {
             assertTrue((e instanceof DateTimeException));
             assertEquals("Out of working hours exception is expected",
                    "Submit date is not in working hours: " + LocalDateTime.of(DATE_FRIDAY, TIME_6PM), e.getMessage());
         }
    }

    @Test
    public void testValidateTurnaroundTimeIsNegative() {
        DueDateCalculatorValidator validator = new DueDateCalculatorValidator();
        try {
            validator.validateCalculateDueDateParameters(LocalDateTime.of(DATE_FRIDAY, TIME_10AM), -1);
         } catch (Exception e) {
             assertTrue((e instanceof DateTimeException));
             assertEquals("Invalid turnaround time exception is expected", "Invalid turnaround time: " + -1, e.getMessage());
         }
    }

    @Test
    public void testCheckWorkingHours10AM() {
        DueDateCalculatorValidator validator = new DueDateCalculatorValidator();
        assertEquals("10AM is working hour", true, validator.checkIfInWorkingHours(TIME_10AM));
    }

    @Test
    public void testCheckWorkingHours8AM() {
        DueDateCalculatorValidator validator = new DueDateCalculatorValidator();
        assertEquals("8AM is not working hour", false, validator.checkIfInWorkingHours(TIME_8AM));
    }

    @Test
    public void testCheckWorkingHours6PM() {
        DueDateCalculatorValidator validator = new DueDateCalculatorValidator();
        assertEquals("6PM is working hour", false, validator.checkIfInWorkingHours(TIME_6PM));
    }

}
