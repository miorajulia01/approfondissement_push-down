package DAO;

import classe.InvoiceStatus;
import classe.InvoiceStatusTotal;
import classe.InvoiceTotal;
import config.DatabaseConnection;

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

        // try-with-resources corrigé
        try (Connection conn = dbConnection.getConnection();  // ← getConnection() pas getConnectionO
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {  // ← executeQuery() sans paramètre

            InvoiceStatusTotal totals = new InvoiceStatusTotal();

            if (rs.next()) {
                totals.setTotalPaid(rs.getDouble("total_paid"));        // ← sans "columnLabel:"
                totals.setTotalConfirmed(rs.getDouble("total_confirmed"));
                totals.setTotalDraft(rs.getDouble("total_draft"));
            }

            return totals;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul des totaux par statut", e);
        }
    }
}
