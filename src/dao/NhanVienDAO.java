package dao;

import entities.NhanVien;
import javafx.scene.control.Alert;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    public Boolean themNhanVien(NhanVien nv) throws SQLException {
        String sql = "THEM_NHANVIEN(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        sql = "BEGIN " + sql + " END;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nv.getHoTen());
            stmt.setString(2, nv.getPassword());
            stmt.setString(3, nv.getCccd());
            stmt.setString(4, nv.getSdt());
            stmt.setDate(5, nv.getNgaySinh());
            stmt.setString(6, nv.getGioiTinh());
            stmt.setString(7, nv.getDiaChi());
            stmt.setString(8, nv.getEmail());
            stmt.setDate(9, nv.getNgayVaoLam());
            stmt.setDouble(10, nv.getLuong());
            stmt.setString(11, nv.getChucVu());
            stmt.setString(12, nv.getMaQL());
            stmt.executeUpdate();

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    public Boolean capNhatNhanVien(NhanVien nv) throws SQLException {
        String sql = "CAPNHAT_NHANVIEN(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        sql = "BEGIN " + sql + " END;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nv.getHoTen());
            stmt.setString(2, nv.getPassword());
            stmt.setString(3, nv.getCccd());
            stmt.setString(4, nv.getSdt());
            stmt.setDate(5, nv.getNgaySinh());
            stmt.setString(6, nv.getGioiTinh());
            stmt.setString(7, nv.getDiaChi());
            stmt.setString(8, nv.getEmail());
            stmt.setDate(9, nv.getNgayVaoLam());
            stmt.setDouble(10, nv.getLuong());
            stmt.setString(11, nv.getChucVu());
            stmt.setString(12, nv.getMaQL());
            stmt.setString(13, nv.getMaNV());
            stmt.executeUpdate();

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    public Boolean xoaNhanVien(String maNV) throws SQLException {
        String sql = "XOA_NHANVIEN(?);";
        sql = "BEGIN " + sql + " END;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maNV);
            stmt.executeUpdate();

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    public List<NhanVien> traCuuNhanVien(String maNV) throws SQLException {
        List<NhanVien> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM NHANVIEN WHERE MANV LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + maNV + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien(
                            rs.getString("MANV"),
                            rs.getString("HOTEN"),
                            rs.getString("PASSWORD"),
                            rs.getString("CCCD"),
                            rs.getString("SDT"),
                            rs.getDate("NGAYSINH"),
                            rs.getString("GIOITINH"),
                            rs.getString("DIACHI"),
                            rs.getString("EMAIL"),
                            rs.getDate("NGAYVAOLAM"),
                            rs.getDouble("LUONG"),
                            rs.getString("CHUCVU"),
                            rs.getString("MAQL")
                    );
                    danhSach.add(nv);
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
