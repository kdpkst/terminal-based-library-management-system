package Models.BookCopy;

public class bookCopy {

    private int cid;
    private int bid;
    private int status;

    public bookCopy(int cid, int bid, int status) {
        this.cid = cid;
        this.bid = bid;
        this.status = status;
    }

    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }
    public int getBid() {
        return bid;
    }
    public void setBid(int bid) {
        this.bid = bid;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
