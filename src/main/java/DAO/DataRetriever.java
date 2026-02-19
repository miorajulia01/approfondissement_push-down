package DAO;

import classe.InvoiceTotal;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private DatabaseConnection dbConnection;

    public DataRetriever() {
        this.dbConnection = new DatabaseConnection();
    }

    public List<InvoiceTotal> findInvoiceTotals() {
        List<InvoiceTotal> results = new ArrayList<>();
        String sql = "SELECT invoice_id, customer_name, status, SUM(quantity * unit_price) as total\n" +
                "FROM invoice i\n" +
                "JOIN invoice_line l ON i.id = l.invoice_id\n" +
                "GROUP BY invoice_id, customer_name, status\n" +
                "order by invoice_id;";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery(sql)) {

            while (rs.next()) {
                InvoiceTotal invoiceTotal = new InvoiceTotal();
                invoiceTotal.setId(rs.getInt("id"));
                invoiceTotal.setCustomerName(rs.getString("customer_name"));
                invoiceTotal.setTotal(rs.getDouble("total"));
                results.add(invoiceTotal);
            }
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
        return results;
    }
}
