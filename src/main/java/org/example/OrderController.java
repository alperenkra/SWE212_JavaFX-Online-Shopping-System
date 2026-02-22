package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderController {

    @FXML private TextField txtOrderId;
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtCustomerName;
    @FXML private TextField txtProductId;
    @FXML private TextField txtProductName;
    @FXML private TextField txtOrderDate;
    @FXML private TextField txtCity;
    @FXML private TextField txtDeliveryStatus;

    private boolean isInputValid() {
        if (txtOrderId.getText().isEmpty() || txtCustomerId.getText().isEmpty() ||
                txtProductId.getText().isEmpty() || txtOrderDate.getText().isEmpty() ||
                txtCity.getText().isEmpty() || txtDeliveryStatus.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Eksik Bilgi", "Lütfen tüm zorunlu alanları doldurunuz!");
            return false;
        }
        try {
            Integer.parseInt(txtOrderId.getText());
            Integer.parseInt(txtCustomerId.getText());
            Integer.parseInt(txtProductId.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hatalı Veri", "ID'ler sadece tam sayı olmalıdır!");
            return false;
        }
        try {
            // Tarih formatının YYYY-MM-DD olduğundan emin oluyoruz
            java.sql.Date.valueOf(txtOrderDate.getText());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Hatalı Tarih", "Tarih formatı YYYY-MM-DD olmalıdır! (Örn: 2026-02-22)");
            return false;
        }
        return true;
    }

    @FXML
    private void handleFetch() {
        if (txtOrderId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Lütfen aramak için bir Order ID girin.");
            return;
        }
        // Senin sütun isimlerine göre JOIN işlemi
        String sql = "SELECT o.customer_id, o.product_id, o.order_date, o.city, o.delivery_status, " +
                "c.name AS customer_name, p.name AS product_name " +
                "FROM Orders o " +
                "LEFT JOIN Customers c ON o.customer_id = c.id " +
                "LEFT JOIN Products p ON o.product_id = p.id " +
                "WHERE o.order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtOrderId.getText()));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                txtCustomerId.setText(String.valueOf(rs.getInt("customer_id")));
                txtProductId.setText(String.valueOf(rs.getInt("product_id")));
                txtOrderDate.setText(rs.getDate("order_date").toString());
                txtCity.setText(rs.getString("city"));
                txtDeliveryStatus.setText(rs.getString("delivery_status"));

                txtCustomerName.setText(rs.getString("customer_name"));
                txtProductName.setText(rs.getString("product_name"));
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Bulunamadı", "Bu ID'ye ait sipariş yok.");
                clearFields();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Veri çekilirken hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        if (!isInputValid()) return;

        // Senin sütun isimlerine göre INSERT
        String sql = "INSERT INTO Orders (order_id, customer_id, product_id, order_date, city, delivery_status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtOrderId.getText()));
            pstmt.setInt(2, Integer.parseInt(txtCustomerId.getText()));
            pstmt.setInt(3, Integer.parseInt(txtProductId.getText()));
            pstmt.setDate(4, java.sql.Date.valueOf(txtOrderDate.getText()));
            pstmt.setString(5, txtCity.getText());
            pstmt.setString(6, txtDeliveryStatus.getText());

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Sipariş veritabanına kaydedildi.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Kaydetme hatası: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        if (!isInputValid()) return;

        // Senin sütun isimlerine göre UPDATE
        String sql = "UPDATE Orders SET customer_id = ?, product_id = ?, order_date = ?, city = ?, delivery_status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtCustomerId.getText()));
            pstmt.setInt(2, Integer.parseInt(txtProductId.getText()));
            pstmt.setDate(3, java.sql.Date.valueOf(txtOrderDate.getText()));
            pstmt.setString(4, txtCity.getText());
            pstmt.setString(5, txtDeliveryStatus.getText());
            pstmt.setInt(6, Integer.parseInt(txtOrderId.getText()));

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Sipariş bilgileri güncellendi.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Güncelleme hatası: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (txtOrderId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Lütfen silmek için bir Order ID girin.");
            return;
        }
        String sql = "DELETE FROM Orders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtOrderId.getText()));
            pstmt.executeUpdate();

            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Sipariş silindi.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Silme hatası: " + e.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        System.exit(0);
    }

    private void clearFields() {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtProductId.clear();
        txtProductName.clear();
        txtOrderDate.clear();
        txtCity.clear();
        txtDeliveryStatus.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}