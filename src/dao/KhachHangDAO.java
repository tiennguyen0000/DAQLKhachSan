package dao;

import entities.KhachHang;
import javafx.scene.control.Alert;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public boolean themKhachHang(KhachHang kh) throws SQLException {
        String sql = "THEM_KHACHHANG (?, ?, ?, ?, ?, ?, ?);";
        sql = "BEGIN " + sql + " END;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kh.getHoTen());
            stmt.setString(2, kh.getCccd());
            stmt.setString(3, kh.getSdt());
            stmt.setDate(4, kh.getNgaySinh());
            stmt.setString(5, kh.getGioiTinh());
            stmt.setString(6, kh.getDiaChi());
            stmt.setString(7, kh.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    public boolean xoaKhachHang(String maKH) throws SQLException {
        String sql = "XOA_KHACHHANG(?);";
        sql = "BEGIN " + sql + " END;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    public boolean suaKhachHang(KhachHang kh) throws SQLException {
        String sql = "CAPNHAT_KHACHHANG(?,?,?,?,?,?,?,?);";
        sql = "BEGIN " + sql + " END;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(2, kh.getHoTen());
            stmt.setString(3, kh.getCccd());
            stmt.setString(4, kh.getSdt());
            stmt.setDate(5, kh.getNgaySinh());
            stmt.setString(6, kh.getGioiTinh());
            stmt.setString(7, kh.getDiaChi());
            stmt.setString(8, kh.getEmail());
            stmt.setString(1, kh.getMaKH());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    public List<KhachHang> traCuuKhachHang(String maKH) throws SQLException {
        List<KhachHang> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANG WHERE MAKH LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
