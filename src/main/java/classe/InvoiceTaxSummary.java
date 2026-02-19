package classe;

import java.util.Objects;

public class InvoiceTaxSummary {
    private int id;
    private double totalHt;
    private double totalTva;
    private double totalTtc;

    public InvoiceTaxSummary() {}

    public InvoiceTaxSummary(int id, double totalHt, double totalTva, double totalTtc) {
        this.id = id;
        this.totalHt = totalHt;
        this.totalTva = totalTva;
        this.totalTtc = totalTtc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalHt() {
        return totalHt;
    }

    public void setTotalHt(double totalHt) {
        this.totalHt = totalHt;
    }

    public double getTotalTva() {
        return totalTva;
    }

    public void setTotalTva(double totalTva) {
        this.totalTva = totalTva;
    }

    public double getTotalTtc() {
        return totalTtc;
    }

    public void setTotalTtc(double totalTtc) {
        this.totalTtc = totalTtc;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceTaxSummary that = (InvoiceTaxSummary) o;
        return id == that.id && Double.compare(totalHt, that.totalHt) == 0 && Double.compare(totalTva, that.totalTva) == 0 && Double.compare(totalTtc, that.totalTtc) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalHt, totalTva, totalTtc);
    }

    @Override
    public String toString() {
        return "InvoiceTaxSummary{" +
                "id=" + id +
                ", totalHt=" + totalHt +
                ", totalTva=" + totalTva +
                ", totalTtc=" + totalTtc +
                '}';
    }
}
