package Models.Fine;

import java.time.LocalDate;

import Decorator.fine;

public class withinSevenDaysFine implements fine{
    private int fid;
    private int cid;
    private double amount;	
    private LocalDate fineDate;

    @Override
    public double calculateFine(int daysOverdue) {
        double base = 2.0;
        return base;
    }

    public int getFid() {
        return fid;
    }
    public void setFid(int fid) {
        this.fid = fid;
    }
    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public LocalDate getFineDate() {
        return fineDate;
    }
    public void setFineDate(LocalDate fineDate) {
        this.fineDate = fineDate;
    }

}
