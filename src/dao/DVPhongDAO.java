package dao;

import entities.DVPhong;
import javafx.scene.control.Alert;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DVPhongDAO {

    // Thêm dịch vụ phòng
    public Boolean themDVPhong(DVPhong dv) throws SQLException {
        String sql = "THEM_DVPHONG ( ?, ?, ?, ?);";
        sql = "BEGIN " + sql + " END;";
        System.out.println("THEM_DVPHONG");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dv.getLoaiPhong());
            stmt.setString(2, dv.getMoTa());
            stmt.setDouble(3, dv.getDonGia());
            stmt.setString(4, dv.getTrangThai());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    // Xóa dịch vụ phòng theo mã
    public Boolean xoaDVPhong(String maDVP) throws SQLException {
        String sql = "XOA_DVPHONG(?);";
        sql = "BEGIN " + sql + " END;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maDVP);
            stmt.executeUpdate();

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    // Sửa dịch vụ phòng
    public Boolean suaDVPhong(DVPhong dv) throws SQLException {
        String sql = "CAPNHAT_DVPHONG(?,?,?,?,?);";
        sql = "BEGIN " + sql + " END;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(2, dv.getLoaiPhong());
            stmt.setString(3, dv.getMoTa());
            stmt.setDouble(4, dv.getDonGia());
            stmt.setString(5, dv.getTrangThai());
            stmt.setString(1, dv.getMaDVP());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    // Tra cứu theo mã
    public List<DVPhong> traCuuDVPhong(String maDVP) throws SQLException {
        List<DVPhong> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM DVPHONG WHERE MADVP LIKE ?";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + maDVP + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DVPhong dv = new DVPhong(
                            rs.getString("MADVP"),
                            rs.getString("LOAIPHONG"),
                            rs.getString("MOTA"),
                            rs.getDouble("DONGIA"),
                            rs.getString("TRANGTHAI")
                    );
                    danhSach.add(dv);
                }
            }
        }

        return danhSach;
    }

    public Map<String, Integer> thongKeLoaiPhong() throws SQLException {
        Map<String, Integer> thongKe = new LinkedHashMap<>();

        String sql = """
        SELECT LOAIPHONG, COUNT(*) AS SOLUONG
        FROM DVPHONG
        GROUP BY LOAIPHONG
        ORDER BY LOAIPHONG
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String loaiPhong = rs.getString("LOAIPHONG");
                int soLuong = rs.getInt("SOLUONG");
                thongKe.put(loaiPhong, soLuong);
            }
        }

        return thongKe;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
