package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert; // Import Alert

import java.io.IOException;
import java.net.URL;

public class HTQLKhachSan extends Application {
    @Override
    public void start(Stage primaryStage) {

        try {
            URL fxmlUrl = getClass().getResource("/form/application/DangNhapForm.fxml");

            if (fxmlUrl == null) {
                showAlert(Alert.AlertType.ERROR, "Lỗi Khởi Tạo Ứng Dụng",
                        "Vui lòng liên hệ bộ phận hỗ trợ.");
                return;
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root);

            primaryStage.setTitle("Đăng Nhập - Hệ Thống Quản Lý Khách Sạn");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Lỗi IOException khi tải FXML: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Khởi Tạo Giao Diện",
                    "Có lỗi xảy ra khi tải giao diện người dùng.\nChi tiết: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi không xác định khi khởi chạy ứng dụng: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Không Xác Định",
                    "Đã có lỗi không mong muốn xảy ra trong quá trình khởi chạy.\nVui lòng thử lại hoặc liên hệ hỗ trợ.");
        }
    }

    // Phương thức tiện ích để hiển thị Alert từ phương thức start (nếu cần)
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
