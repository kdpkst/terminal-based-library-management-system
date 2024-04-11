package Decorator;

public class FineWithinSevenDays implements FineCalculator {
    private double baseFinePerDay;

    public FineWithinSevenDays(double baseFinePerDay) {
        this.baseFinePerDay = baseFinePerDay;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        return baseFinePerDay * daysOverdue;
    }
}
