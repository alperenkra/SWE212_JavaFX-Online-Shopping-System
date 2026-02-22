package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductController {

    @FXML private TextField txtProductId;
    @FXML private TextField txtName;
    @FXML private TextField txtSupplier;
    @FXML private TextField txtPrice;

    private boolean isInputValid() {
        if (txtProductId.getText().isEmpty() || txtName.getText().isEmpty() ||
                txtSupplier.getText().isEmpty() || txtPrice.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Eksik Bilgi", "Lütfen tüm alanları doldurunuz!");
            return false;
        }
        try {
            Integer.parseInt(txtProductId.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hatalı Veri", "Product ID sadece tam sayı olmalıdır!");
            return false;
        }
        try {
            Double.parseDouble(txtPrice.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hatalı Veri", "Fiyat geçerli bir sayı olmalıdır! (Örn: 15.99)");
            return false;
        }
        return true;
    }

    @FXML
    private void handleFetch() {
        if (txtProductId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Lütfen aramak için bir Product ID girin.");
            return;
        }
        String sql = "SELECT * FROM Products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtProductId.getText()));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                txtName.setText(rs.getString("name"));
                txtSupplier.setText(rs.getString("supplier"));
                txtPrice.setText(String.valueOf(rs.getDouble("price")));
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Bulunamadı", "Bu ID'ye ait ürün yok.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Veri çekilirken hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        if (!isInputValid()) return;

        String sql = "INSERT INTO Products (id, name, supplier, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtProductId.getText()));
            pstmt.setString(2, txtName.getText());
            pstmt.setString(3, txtSupplier.getText());
            pstmt.setDouble(4, Double.parseDouble(txtPrice.getText()));

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Ürün veritabanına kaydedildi.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Kaydetme hatası: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        if (!isInputValid()) return;

        String sql = "UPDATE Products SET name = ?, supplier = ?, price = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, txtSupplier.getText());
            pstmt.setDouble(3, Double.parseDouble(txtPrice.getText()));
            pstmt.setInt(4, Integer.parseInt(txtProductId.getText()));

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Ürün bilgileri güncellendi.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Güncelleme hatası: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (txtProductId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Lütfen silmek için bir Product ID girin.");
            return;
        }
        String sql = "DELETE FROM Products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtProductId.getText()));
            pstmt.executeUpdate();

            txtName.clear();
            txtSupplier.clear();
            txtPrice.clear();
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Ürün silindi.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Silme hatası: " + e.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        System.exit(0);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}