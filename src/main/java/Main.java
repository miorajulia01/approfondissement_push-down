import DAO.DataRetriever;
import classe.InvoiceTotal;

public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();
        System.out.println(dr.findInvoiceTotals());
        System.out.println(dr.findConfirmedAndPaidInvoiceTotals());
        System.out.println(dr.computeStatusTotals());
    }
}
