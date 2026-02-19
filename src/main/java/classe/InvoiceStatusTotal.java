package classe;

public class InvoiceStatusTotal {
    private double totalPaid;
    private double totalConfirmed;
    private double totalDraft;

    public InvoiceStatusTotal() {}

    public InvoiceStatusTotal(double totalPaid, double totalConfirmed, double totalDraft) {
        this.totalPaid = totalPaid;
        this.totalConfirmed = totalConfirmed;
        this.totalDraft = totalDraft;
    }

    // Getters et Setters
    public double getTotalPaid() { return totalPaid; }
    public void setTotalPaid(double totalPaid) { this.totalPaid = totalPaid; }

    public double getTotalConfirmed() { return totalConfirmed; }
    public void setTotalConfirmed(double totalConfirmed) { this.totalConfirmed = totalConfirmed; }

    public double getTotalDraft() { return totalDraft; }
    public void setTotalDraft(double totalDraft) { this.totalDraft = totalDraft; }

    @Override
    public String toString() {
        return "total_paid = " + String.format("%.2f", totalPaid) + "\n" +
                "total_confirmed = " + String.format("%.2f", totalConfirmed) + "\n" +
                "total_draft = " + String.format("%.2f", totalDraft);
    }
}

