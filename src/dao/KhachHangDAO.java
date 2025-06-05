package dao;

import entities.KhachHang;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KhachHangDAO {

    public boolean themKhachHang(KhachHang kh) throws SQLException {
        String sql = "THEM_KHACHHANG (?, ?, ?, ?, ?, ?, ?);";
        sql = "BEGIN " + sql + " END;";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Thiết lập isolation level SERIALIZABLE
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, kh.getHoTen());
                stmt.setString(2, kh.getCccd());
                stmt.setString(3, kh.getSdt());
                stmt.setDate(4, kh.getNgaySinh());
                stmt.setString(5, kh.getGioiTinh());
                stmt.setString(6, kh.getDiaChi());
                stmt.setString(7, kh.getEmail());

                int rows = stmt.executeUpdate();
                conn.commit();
                return rows > 0;
            }

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            showAlert(Alert.AlertType.ERROR, "Thêm KH thất bại" + e.getMessage());
            return false;
        } finally {
            if (conn != null) conn.close();
        }
    }

    public boolean xoaKhachHang(String maKH) throws SQLException {
        String sql = "XOA_KHACHHANG(?);";
        sql = "BEGIN " + sql + " END;";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, maKH);

                int rowsAffected = stmt.executeUpdate();
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Xác nhận");
                confirm.setHeaderText("Bạn xác nhận xóa ?");
                confirm.setContentText("Chọn OK để commit hoặc Cancel để hủy.");
                Optional<ButtonType> result = confirm.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    conn.commit();
                    return rowsAffected > 0;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
            conn.rollback();
            return false;
        } finally {
            conn.close();
        }
    }

    public boolean suaKhachHang(KhachHang kh) throws SQLException {
        String lockSql = "SELECT 1 FROM KHACHHANG WHERE MAKH = ? FOR UPDATE NOWAIT";
        String updateSql = "BEGIN CAPNHAT_KHACHHANG(?,?,?,?,?,?,?,?); END;";
        Connection conn = null;


        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            // khóa dòng dữ liệu khi vào thao tác, khi có transaction khác thao tác vào dòng này sẽ báo lỗi
            try (PreparedStatement lockStmt = conn.prepareStatement(lockSql)) {
                lockStmt.setString(1, kh.getMaKH());
                lockStmt.executeQuery();
            }

            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setString(1, kh.getMaKH());
                stmt.setString(2, kh.getHoTen());
                stmt.setString(3, kh.getCccd());
                stmt.setString(4, kh.getSdt());
                stmt.setDate(5, kh.getNgaySinh());
                stmt.setString(6, kh.getGioiTinh());
                stmt.setString(7, kh.getDiaChi());
                stmt.setString(8, kh.getEmail());


                int rowsAffected = stmt.executeUpdate();

                // Hộp thoại xác nhận commit
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Xác nhận");
                confirm.setHeaderText("Bạn có muốn lưu thay đổi không?");
                confirm.setContentText("Chọn OK để commit hoặc Cancel để hủy.");
                Optional<ButtonType> result = confirm.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    conn.commit();
                    return rowsAffected > 0;
                } else {
                    conn.rollback();
                    return false;
                }
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
            return false;
        }
    }


    public List<KhachHang> traCuuKhachHang(String maKH) throws SQLException {
        List<KhachHang> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANG WHERE MAKH LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            stmt.setString(1, "%" + maKH + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = new KhachHang(
                            rs.getString("MAKH"),
                            rs.getString("HOTEN"),
                            rs.getString("CCCD"),
                            rs.getString("SDT"),
                            rs.getDate("NGAYSINH"),
                            rs.getString("GIOITINH"),
                            rs.getString("DIACHI"),
                            rs.getString("EMAIL")
                    );
                    danhSach.add(kh);
                }
            }
        }

        return danhSach;
    }
     
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Có người khác đang thao tác dữ liệu này");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
