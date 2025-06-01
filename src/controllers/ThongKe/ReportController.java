package controllers.ThongKe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;

public class ReportController {

    @FXML private ToggleButton btnTKHD;
    @FXML private BorderPane TKBorderPane;

    @FXML
    public void initialize() {
        setupUITK();
    }


    private void setupUITK() {
        btnTKHD.setSelected(true);
    }

    public void handleTKHD(ActionEvent actionEvent) {
        // điều phối màn hình mới
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/Thongke/HoaDonTK/HDChart.fxml"));
            Parent TKHDroot = loader.load();

            TKHDHandle controller = loader.getController();
            if (TKBorderPane != null) {
                TKBorderPane.setCenter(TKHDroot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: chưa được inject. Kiểm tra fx:id ");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải phan hoi y kien.\nChi tiết: " + e.getMessage());
        }
    }

    public void handleTKP(ActionEvent actionEvent) {
    }

    public void handleTKKH(ActionEvent actionEvent) {
    }

    public void handleTKNV(ActionEvent actionEvent) {
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
