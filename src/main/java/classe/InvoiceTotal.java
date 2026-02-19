package classe;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceTotal {
    private int id;
    private String customerName;
    private InvoiceStatus status;
    private double total;
    private BigDecimal unitPrice;

    public InvoiceTotal() {}

    public InvoiceTotal(int id, String customerName, InvoiceStatus status, double total, BigDecimal unitPrice) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.total = total;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceTotal that = (InvoiceTotal) o;
        return id == that.id && Double.compare(total, that.total) == 0 && Objects.equals(customerName, that.customerName) && status == that.status && Objects.equals(unitPrice, that.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, status, total, unitPrice);
    }
}
