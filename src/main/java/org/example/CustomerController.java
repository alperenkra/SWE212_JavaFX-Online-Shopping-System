package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerController {

    @FXML private TextField txtUserId;
    @FXML private TextField txtName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtTelephone;

    private boolean isInputValid() {
        if (txtUserId.getText().isEmpty() || txtName.getText().isEmpty() ||
                txtAddress.getText().isEmpty() || txtTelephone.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Eksik Bilgi", "Lütfen tüm alanları doldurunuz!");
            return false;
        }
        try {
            Integer.parseInt(txtUserId.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hatalı Veri", "User ID sadece tam sayı olmalıdır!");
            return false;
        }
        return true;
    }

    @FXML
    private void handleFetch() {
        if (txtUserId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Lütfen aramak için bir User ID girin.");
            return;
        }
        String sql = "SELECT * FROM Customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtUserId.getText()));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                txtName.setText(rs.getString("name"));
                txtAddress.setText(rs.getString("address"));
                txtTelephone.setText(rs.getString("telephone"));
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Bulunamadı", "Bu ID'ye ait müşteri yok.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Veri çekilirken hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        if (!isInputValid()) return;

        String sql = "INSERT INTO Customers (id, name, address, telephone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtUserId.getText()));
            pstmt.setString(2, txtName.getText());
            pstmt.setString(3, txtAddress.getText());
            pstmt.setString(4, txtTelephone.getText());

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Müşteri veritabanına kaydedildi.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Kaydetme hatası: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        if (!isInputValid()) return;

        String sql = "UPDATE Customers SET name = ?, address = ?, telephone = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, txtAddress.getText());
            pstmt.setString(3, txtTelephone.getText());
            pstmt.setInt(4, Integer.parseInt(txtUserId.getText()));

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Müşteri bilgileri güncellendi.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Güncelleme hatası: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (txtUserId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Lütfen silmek için bir User ID girin.");
            return;
        }
        String sql = "DELETE FROM Customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtUserId.getText()));
            pstmt.executeUpdate();

            txtName.clear();
            txtAddress.clear();
            txtTelephone.clear();
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Müşteri silindi.");
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