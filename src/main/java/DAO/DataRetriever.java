package DAO;

import classe.InvoiceStatus;
import classe.InvoiceStatusTotal;
import classe.InvoiceTaxSummary;
import classe.InvoiceTotal;
import config.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.List.*;

public class DataRetriever {
    private DatabaseConnection dbConnection;

    public DataRetriever() {
        this.dbConnection = new DatabaseConnection();
    }

    public List<InvoiceTotal> findInvoiceTotals() throws RuntimeException {
        List<InvoiceTotal> results = new ArrayList<>();
        String sql = "SELECT invoice_id, customer_name, status, SUM(quantity * unit_price) as total\n" +
                "FROM invoice i\n" +
                "JOIN invoice_line l ON i.id = l.invoice_id\n" +
                "GROUP BY invoice_id, customer_name, status\n" +
                "order by invoice_id;";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                InvoiceTotal invoiceTotal = new InvoiceTotal();
                invoiceTotal.setId(rs.getInt("invoice_id"));
                invoiceTotal.setCustomerName(rs.getString("customer_name"));
                invoiceTotal.setTotal(rs.getDouble("total"));
                results.add(invoiceTotal);
            }
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
        return results;
    }

    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals() {
        List<InvoiceTotal> results = new ArrayList<>();
        String sql = "SELECT invoice_id, customer_name, status, SUM(quantity * unit_price) as total\n" +
                "FROM invoice i\n" +
                "         JOIN invoice_line l ON i.id = l.invoice_id\n" +
                "WHERE i.status IN ('CONFIRMED', 'PAID')\n" +
                "GROUP BY invoice_id, i.customer_name, i.status";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                InvoiceTotal invoiceTotal = new InvoiceTotal();
                invoiceTotal.setId(rs.getInt("invoice_id"));
                invoiceTotal.setCustomerName(rs.getString("customer_name"));
                invoiceTotal.setStatus(InvoiceStatus.valueOf(rs.getString("status")));
                invoiceTotal.setTotal(rs.getDouble("total"));
                results.add(invoiceTotal);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public InvoiceStatusTotal computeStatusTotals() {
        String sql = "SELECT\n" +
                "    SUM(quantity * unit_price) FILTER (WHERE i.status = 'PAID') as total_paid,\n" +
                "    SUM(quantity * unit_price) FILTER (WHERE i.status = 'CONFIRMED') as total_confirmed,\n" +
                "    SUM(quantity * unit_price) FILTER (WHERE i.status = 'DRAFT') as total_draft\n" +
                "FROM invoice i\n" +
                "JOIN invoice_line l ON i.id = l.invoice_id";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            InvoiceStatusTotal totals = new InvoiceStatusTotal();
            if (rs.next()) {
                totals.setTotalPaid(rs.getDouble("total_paid"));
                totals.setTotalConfirmed(rs.getDouble("total_confirmed"));
                totals.setTotalDraft(rs.getDouble("total_draft"));
            }
            return totals;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double computeWeightedTurnover()  {
        Double results = 0.0;
        String sql = "SELECT SUM(quantity * unit_price * CASE status\n" +
                "    WHEN 'PAID' THEN 1\n" +
                "    WHEN 'CONFIRMED' THEN 0.5\n" +
                "    ELSE 0\n" +
                "    END) as weighted_total\n" +
                "FROM invoice i\n" +
                "         JOIN invoice_line l ON i.id = l.invoice_id";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                results = rs.getDouble("weighted_total");;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public List<InvoiceTaxSummary> findInvoiceTaxSummaries() {
        List<InvoiceTaxSummary> results = new ArrayList<>();
        String sql = "SELECT i.id,\n" +
                "       SUM(quantity * unit_price) as total_ht,\n" +
                "       SUM(quantity * unit_price * t.rate / 100) as total_tva,\n" +
                "       SUM(quantity * unit_price * (1 + t.rate / 100)) as total_ttc\n" +
                "FROM invoice i\n" +
                "         JOIN invoice_line l ON i.id = l.invoice_id\n" +
                "         CROSS JOIN tax_config t WHERE t.label = 'TVA STANDARD'\n" +
                "GROUP BY i.id ORDER BY i.id";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                InvoiceTaxSummary summary = new InvoiceTaxSummary();
                summary.setId(rs.getInt("id"));
                summary.setTotalHt(rs.getDouble("total_ht"));
                summary.setTotalTva(rs.getDouble("total_tva"));
                summary.setTotalTtc(rs.getDouble("total_ttc"));
                results.add(summary);
            }

        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
        return results;
    }

    public BigDecimal computeWeightedTurnoverTtc() {
        BigDecimal result = BigDecimal.ZERO;
        String sql = "SELECT SUM((quantity * unit_price * (1 + rate / 100)) * CASE  status\n" +
                "        WHEN 'PAID' THEN 1\n" +
                "        WHEN 'CONFIRMED' THEN 0.5\n" +
                "        ELSE 0\n" +
                "     END) as weighted_turnover_ttc\n" +
                "FROM invoice i\n" +
                "        JOIN invoice_line l ON i.id = l.invoice_id\n" +
                "        CROSS JOIN tax_config t WHERE t.label = 'TVA STANDARD'";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                result = rs.getBigDecimal("weighted_turnover_ttc");
            }
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
        return result;
    }
}
