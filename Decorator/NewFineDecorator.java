package Decorator;

public class NewFineDecorator extends fineDecorator {

    private double baseFinePerDay;
    private int daysOverdueStandard;

    public NewFineDecorator (fine f, double baseFinePerDay, int daysOverdueStandard) {
        super(f);
        this.baseFinePerDay = baseFinePerDay;
        this.daysOverdueStandard = daysOverdueStandard;
        
    }

    @Override
    public double calculateFine(int daysOverdue) {
        
        return baseFinePerDay * daysOverdue;
    }
    
}
