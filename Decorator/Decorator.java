package Decorator;

public abstract class Decorator implements FineCalculator {
    protected FineCalculator decoratedFine;

    public Decorator(FineCalculator decoratedFine) {
        this.decoratedFine = decoratedFine;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        return decoratedFine.calculateFine(daysOverdue);
    }
}
