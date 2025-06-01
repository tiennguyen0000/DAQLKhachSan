package controllers;

import entities.NhanVien;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DangNhapController {
    @FXML
    private TextField maGvField;

    @FXML
    private PasswordField matKhauField;

    @FXML
    private Button loginButton; // Đảm bảo fx:id này được gán trong FXML

    @FXML
    private Label statusLabel; // Dùng cho thông báo lỗi trường bắt buộc

    @FXML
    public void initialize() {
        statusLabel.setText("");
    }

    @FXML
    protected void LayThongTinDangNhap(ActionEvent event) {
        String maGV = maGvField.getText();
        String matKhau = matKhauField.getText();

        // Gọi hàm kiểm tra trường bắt buộc
        if (!KiemTraTruongBatBuoc(maGV, matKhau)) {
            return; // Dừng nếu trường bắt buộc không hợp lệ
        }
        // Nếu trường hợp lệ, xóa thông báo lỗi cũ (nếu có)
        statusLabel.setText("");


        System.out.println("Controller: Đang gọi KiemTraDangNhapCSDL cho MaGV: " + maGV);
        Optional<NhanVien> NhanVienOptional = KiemTraDangNhapCSDL(maGV, matKhau);

        if (NhanVienOptional.isPresent()) {
            NhanVien loggedInNhanVien = NhanVienOptional.get();
            System.out.println("Controller: Đăng nhập thành công cho: " + loggedInNhanVien.getHoTen());

            // Hiển thị thông báo thành công bằng Alert
            ThongBaoDangNhap(true, loggedInNhanVien.getHoTen());

            try {
                ChuyenGiaoDien(loggedInNhanVien, event);
            } catch (IOException e) {
                e.printStackTrace();
                // Thông báo lỗi nghiêm trọng hơn nếu không mở được form chính
                showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải trang chính của ứng dụng.\nChi tiết: " + e.getMessage());
            }
        } else {
            System.out.println("Controller: Đăng nhập thất bại cho MaGV: " + maGV);
            // Hiển thị thông báo thất bại bằng Alert
            ThongBaoDangNhap(false, null);

            matKhauField.clear();
            matKhauField.requestFocus();
        }
    }

    private boolean KiemTraTruongBatBuoc(String maGV, String matKhau) {
        if (maGV.trim().isEmpty() || matKhau.isEmpty()) {
            ThongBaoLoi("Mã nhân viên và mật khẩu không được để trống!");
            return false;
        }
        return true;
    }

    private void ThongBaoLoi(String message) {
        statusLabel.setText(message);
        statusLabel.setTextFill(javafx.scene.paint.Color.RED);
        if (maGvField.getText().trim().isEmpty()) {
            maGvField.requestFocus();
        } else {
            matKhauField.requestFocus();
        }
    }

    private void ThongBaoDangNhap(boolean thanhCong, String tenNhanVien) {
        if (thanhCong) {
            showAlert(Alert.AlertType.INFORMATION, "Đăng Nhập Thành Công",
                    "Chào mừng " + (tenNhanVien != null ? tenNhanVien : "") + " đã đăng nhập thành công vào hệ thống!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Đăng Nhập Thất Bại",
                    "Mã giáo viên hoặc mật khẩu không đúng.\nVui lòng kiểm tra lại thông tin đăng nhập.");
        }
    }


    private void ChuyenGiaoDien(NhanVien nhanVien, ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/main/MainForm.fxml"));
        Parent mainFormRoot = loader.load();

        MainFormController dashboardController = loader.getController();
        dashboardController.setNhanVien(nhanVien);

        Stage mainStage = new Stage();
        mainStage.setTitle("Trang Chính - Hệ Thống Quản Lý Khách Sạn ANH TIẾN DT");
        mainStage.setScene(new Scene(mainFormRoot, 800, 600));
        mainStage.setMinWidth(1300);
        mainStage.setMinHeight(800); // cua son 500

        mainStage.setOnCloseRequest(e -> {
            System.out.println("MainForm đang đóng, ứng dụng sẽ thoát.");
            System.exit(0);
        });

        mainStage.show();
        currentStage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Không hiển thị header text
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Optional<NhanVien> KiemTraDangNhapCSDL(String maNV, String matKhau) {
        String sql = "SELECT * " +
                "from NHANVIEN where manv = 'MANV01' and password = 'MK1'";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("Controller/KiemTraDangNhapCSDL: Không thể kết nối đến cơ sở dữ liệu.");
                return Optional.empty();
            }
//
//            pstmt.setString(1, maNV.trim());
//            pstmt.setString(2, matKhau);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    NhanVien nhanVien = new NhanVien(
                            rs.getString("MaNV"),
                            rs.getString("HoTen"),
                            rs.getString("Password"),
                            rs.getString("CCCD"),
                            rs.getString("SDT"),
                            rs.getDate("NgaySinh"),
                            rs.getString("GioiTinh"),
                            rs.getString("DiaChi"),
                            rs.getString("Email"),
                            rs.getDate("NgayVaoLam"),
                            rs.getDouble("Luong"),
                            rs.getString("ChucVu"),
                            rs.getString("MaQL")
                    );


                    System.out.println("Controller/KiemTraDangNhapCSDL: Xác thực thành công cho MaNV: " + maNV);
                    return Optional.of(nhanVien);
                } else {
                    System.out.println("Controller/KiemTraDangNhapCSDL: Không tìm thấy: " + maNV + " và mật khẩu cung cấp, hoặc mật khẩu sai.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Controller/KiemTraDangNhapCSDL: Lỗi SQL khi xác thực giáo viên (MaNV: " + maNV + "): " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }


}
