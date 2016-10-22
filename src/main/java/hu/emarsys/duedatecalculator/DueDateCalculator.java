package hu.emarsys.duedatecalculator;

import java.time.LocalDateTime;

public class DueDateCalculator {
    private static final int WORKING_HOURS_PER_DAY = DueDateCalculatorValidator.END_OF_WORKDAY.getHour()
                                                   - DueDateCalculatorValidator.START_OF_WORKDAY.getHour();

    private static DueDateCalculatorValidator validator = new DueDateCalculatorValidator();

    public LocalDateTime CalculateDueDate(LocalDateTime submitDate, int turnaroundTime) {
        validator.validateCalculateDueDateParameters(submitDate, turnaroundTime);

        LocalDateTime submitDateIncreasedWithWorkingDays = addWorkingDaysToWeekDay(submitDate, turnaroundTime / WORKING_HOURS_PER_DAY);

        return addWorkingHoursToWeekDay(submitDateIncreasedWithWorkingDays, turnaroundTime % WORKING_HOURS_PER_DAY);
    }

    private LocalDateTime addWorkingDaysToWeekDay(LocalDateTime startDate, int workingdaysToAdd) {
        return startDate.plusDays(getNumberOfReguralDaysToAddToWeekDay(startDate.getDayOfWeek().getValue(), workingdaysToAdd));
    }

    private LocalDateTime addWorkingHoursToWeekDay(LocalDateTime startDate, int workinghoursToAdd) {
        LocalDateTime startDateIncreasedWithHours = startDate.plusHours(workinghoursToAdd);
        if (!validator.checkIfInWorkingHours(startDateIncreasedWithHours.toLocalTime())) {
            return convertToNextWorkingDay(startDateIncreasedWithHours);
        }
        return startDateIncreasedWithHours;
    }

    /*
     * To easily calculate the how many regular days should we add for N working days we should count how many weekends will pass
     * till the end date and add two days for each.
     * It's easy to calculate how many weeks would it take when the starting day is a Monday because there will be a weekend
     * after every 5 working days so N / 5 weekends will pass till the end date.
     * The idea is normalizing every request as it was started on the Monday of the current week to calculate the number of weekends
     * till the end date.
     * If the starting date is the Mth working day of the week (1 <= M <= 5) then we can reach the same end date starting on the
     * 1st day of the week and adding M - 1 + N working days. So (M - 1 + N) / 5 weeks will pass till the end date.
     *
     * Careful! This only works if the starting date is a working day!
     */
    private int getNumberOfReguralDaysToAddToWeekDay(int dayOfWeek, int workingdaysToAdd) {
        return workingdaysToAdd + 2 * ((dayOfWeek - 1 + workingdaysToAdd) / 5);
    }

    private LocalDateTime convertToNextWorkingDay(LocalDateTime startDate) {
        return startDate.plusDays(getNumberOfReguralDaysToAddToWeekDay(startDate.getDayOfWeek().getValue(), 1)).minusHours(WORKING_HOURS_PER_DAY);
    }

}
