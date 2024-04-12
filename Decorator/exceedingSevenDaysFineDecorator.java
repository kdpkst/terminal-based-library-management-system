package Decorator;

public class exceedingSevenDaysFineDecorator extends fineDecorator {
    private double additionalFinePerDay;

    public exceedingSevenDaysFineDecorator(fine f, double additionalFinePerDay) {
        super(f);
        this.additionalFinePerDay = additionalFinePerDay;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        // double base = super.calculateFine(daysOverdue);
        // System.out.println(base);
        double additionalFine = additionalFinePerDay * (daysOverdue - 7);
        return 14 + additionalFine;
    }
}
