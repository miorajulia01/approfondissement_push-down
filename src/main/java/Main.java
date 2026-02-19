import DAO.DataRetriever;
import classe.InvoiceTotal;

public class Main {
    public static void main(String[] args) {
        DataRetriever dr = new DataRetriever();

        System.out.println("=== Q1 : TOTAL PAR FACTURE ===");
        for (InvoiceTotal it : dr.findInvoiceTotals()) {
            System.out.println(it.getId() + " | " + it.getCustomerName() + " | " + it.getTotal());
        }
        System.out.println();

        System.out.println("=== Q2 : FACTURES CONFIRMED & PAID ===");
        for (InvoiceTotal it : dr.findConfirmedAndPaidInvoiceTotals()) {
            System.out.println(it.getId() + " | " + it.getCustomerName() + " | " + it.getStatus() + " | " + it.getTotal());
        }
        System.out.println();

        System.out.println("=== Q3 : TOTAUX PAR STATUT ===");
        System.out.println(dr.computeStatusTotals());
        System.out.println();

        System.out.println("Q4 : CHIFFRE PONDÉRÉ ===");
        System.out.println(dr.computeWeightedTurnover());
        System.out.println();

        System.out.println("=== Q5-A : DÉTAIL TAXES ===");
        dr.findInvoiceTaxSummaries().stream().forEach(System.out::println);
        System.out.println();

        System.out.println("=== Q5-B : TOTAL TTC PONDÉRÉ ===");
        System.out.println(dr.computeWeightedTurnoverTtc());
    }
}
