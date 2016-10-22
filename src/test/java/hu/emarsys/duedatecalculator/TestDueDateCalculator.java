package hu.emarsys.duedatecalculator;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

public class TestDueDateCalculator {

    private static final LocalDate DATE_THURSDAY = LocalDate.of(2016, 10, 20); //2016. 10. 20. is Thursday
    private static final LocalDate DATE_FRIDAY = LocalDate.of(2016, 10, 21); //2016. 10. 21. is Friday
    private static final LocalDate DATE_MONDAY = LocalDate.of(2016, 10, 24); //2016. 10. 24. is Monday
    private static final LocalTime TIME_9AM = LocalTime.of(9, 0);
    private static final LocalTime TIME_10AM = LocalTime.of(10, 0);
    private static final LocalTime TIME_12PM = LocalTime.of(12, 0);
    private static final LocalTime TIME_4PM = LocalTime.of(16, 0);
    private static final LocalTime TIME_5PM = LocalTime.of(17, 0);

    @Test
    public void testDueDateIsSameDayAsStartDate() {
        DueDateCalculator dueDateCalculator = new DueDateCalculator();
        assertEquals("10AM + 2 hours should be 12PM", LocalDateTime.of(DATE_FRIDAY, TIME_12PM),
                dueDateCalculator.CalculateDueDate(LocalDateTime.of(DATE_FRIDAY, TIME_10AM), 2));
    }

    @Test
    public void testDueDateIsNextDay() {
        DueDateCalculator dueDateCalculator = new DueDateCalculator();
        assertEquals("Thursday 4PM + 2 hours should be Friday 10AM", LocalDateTime.of(DATE_FRIDAY, TIME_10AM),
                dueDateCalculator.CalculateDueDate(LocalDateTime.of(DATE_THURSDAY, TIME_4PM), 2));
    }

    @Test
    public void testStartDateIs9AM() {
        DueDateCalculator dueDateCalculator = new DueDateCalculator();
        assertEquals("9AM + 3 hours should be 12PM", LocalDateTime.of(DATE_FRIDAY, TIME_12PM),
                dueDateCalculator.CalculateDueDate(LocalDateTime.of(DATE_FRIDAY, TIME_9AM), 3));
    }

    @Test
    public void testStartDateIs5PM() {
        DueDateCalculator dueDateCalculator = new DueDateCalculator();
        assertEquals("5PM + 3 hours should be 12PM on the next day", LocalDateTime.of(DATE_FRIDAY, TIME_12PM),
                dueDateCalculator.CalculateDueDate(LocalDateTime.of(DATE_THURSDAY, TIME_5PM), 3));
    }

    @Test
    public void testDueDateIsNextWeek() {
        DueDateCalculator dueDateCalculator = new DueDateCalculator();
        assertEquals("Friday 4PM + 2 hours should be Monday 10AM", LocalDateTime.of(DATE_MONDAY, TIME_10AM),
                dueDateCalculator.CalculateDueDate(LocalDateTime.of(DATE_FRIDAY, TIME_4PM), 2));
    }

    /*
     * 20000 working hours are exactly 2500 working days
     * 2500 working days are exactly 500 weeks
     * 500 weeks are 3500 regular days
     */
    @Test
    public void testWithHugeTurninghoursWithoutReminderHours() {
        DueDateCalculator dueDateCalculator = new DueDateCalculator();
        assertEquals("Friday 4PM + 20000 hours should be 3500 days after the startDate", LocalDateTime.of(DATE_FRIDAY, TIME_4PM).plusDays(3500),
                dueDateCalculator.CalculateDueDate(LocalDateTime.of(DATE_FRIDAY, TIME_4PM), 20000));
    }

    /*
     * 20002 working hours are 3500 regular days + 2 extra hours
     * Friday 16PM + 2hours is Monday 10PM
     */
    @Test
    public void testWithHugeTurninghoursWithReminderHours() {
        DueDateCalculator dueDateCalculator = new DueDateCalculator();
        LocalDateTime dueDate = dueDateCalculator.CalculateDueDate(LocalDateTime.of(DATE_FRIDAY, TIME_4PM), 20002);
        assertEquals("Friday 4PM + 20002 hours should be 10PM 3503 days after the startDate",
                LocalDateTime.of(DATE_FRIDAY, TIME_10AM).plusDays(3503), dueDate);
        assertEquals("Friday 4PM + 20002 hours should be Monday",
                DayOfWeek.MONDAY, dueDate.getDayOfWeek());
    }

}
