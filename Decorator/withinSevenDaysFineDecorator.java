package Decorator;

public class withinSevenDaysFineDecorator extends fineDecorator {
    private double baseFinePerDay;

    public withinSevenDaysFineDecorator(fine f, double baseFinePerDay) {
        super(f);
        this.baseFinePerDay = baseFinePerDay;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        // set this is a flexible schedule for the user
        return 0;
    }
}
