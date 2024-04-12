package Decorator;

public class fineDecorator implements fine {

    private fine f;

    protected fineDecorator(fine f) {
        this.f = f;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        return f.calculateFine(daysOverdue);
    }

}
