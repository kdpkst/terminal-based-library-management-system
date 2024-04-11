package Decorator;

public class OverdueFine extends Decorator {
    private double additionalFinePerDay;

    public OverdueFine(FineCalculator decoratedFine, double additionalFinePerDay) {
        super(decoratedFine);
        this.additionalFinePerDay = additionalFinePerDay;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        double baseFine = super.calculateFine(daysOverdue);
        double additionalFine = additionalFinePerDay * daysOverdue;
        return baseFine + additionalFine;
    }
}
