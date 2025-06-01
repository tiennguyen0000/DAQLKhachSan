package dao;

import entities.HoaDon;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HoaDonDAO {

    // Thêm hóa đơn
        public Boolean themHoaDon(HoaDon hd) throws SQLException {
        String sql = "INSERT INTO HOADON (MANV, MAKH, NGAYTAO, NGAYTHANHTOAN, " +
                "TONGTIEN, TINHTRANG, NGAYBDSD, NGAYKTSD, SONGAYSUDUNG, NOTE, MADVP) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hd.getMaNV());
            stmt.setString(2, hd.getMaKH());
            stmt.setDate(3, hd.getNgayTao());
            stmt.setDate(4, hd.getNgayThanhToan());
            stmt.setDouble(5, hd.getTongTien());
            stmt.setString(6, hd.getTinhTrangTT());
            stmt.setDate(7, hd.getNgayBDSD());
            stmt.setDate(8, hd.getNgayKTSD());
            stmt.setInt(9, hd.getSoNgaySuDung());
            stmt.setString(10, hd.getNote());
            stmt.setString(11, hd.getMaDVP());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    // Xóa hóa đơn theo MAHD
    public Boolean xoaHoaDon(String maHD) throws SQLException {
        String sql = "DELETE FROM HOADON WHERE MAHD = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHD);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    // Sửa thông tin hóa đơn
    public Boolean suaHoaDon(HoaDon hd) throws SQLException {
        String sql = "UPDATE HOADON SET MANV = ?, MAKH = ?, NGAYTAO = ?, " +
                "NGAYTHANHTOAN = ?, TONGTIEN = ?, TINHTRANG = ?, NGAYBDSD = ?, NGAYKTSD = ?, " +
                "SONGAYSUDUNG = ?, NOTE = ?, MADVP = ? WHERE MAHD = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hd.getMaNV());
            stmt.setString(2, hd.getMaKH());
            stmt.setDate(3, hd.getNgayTao());
            stmt.setDate(4, hd.getNgayThanhToan());
            stmt.setDouble(5, hd.getTongTien());
            stmt.setString(6, hd.getTinhTrangTT());
            stmt.setDate(7, hd.getNgayBDSD());
            stmt.setDate(8, hd.getNgayKTSD());
            stmt.setInt(9, hd.getSoNgaySuDung());
            stmt.setString(10, hd.getNote());
            stmt.setString(11, hd.getMaDVP());
            stmt.setString(12, hd.getMaHD());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi: ", e.getMessage());
            return false;
        }
    }

    // Tìm kiếm hóa đơn theo MAHD
    public List<HoaDon> traCuuHoaDon(String maHD) throws SQLException {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM HOADON WHERE MAHD LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + maHD + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon(
                            rs.getString("MAHD"),
                            rs.getString("MANV"),
                            rs.getString("MAKH"),
                            rs.getDate("NGAYTAO"),
                            rs.getDate("NGAYTHANHTOAN"),
                            rs.getDouble("TONGTIEN"),
                            rs.getString("TINHTRANG"),
                            rs.getDate("NGAYBDSD"),
                            rs.getDate("NGAYKTSD"),
                            rs.getInt("SONGAYSUDUNG"),
                            rs.getString("NOTE"),
                            rs.getString("MADVP")
                    );
                    danhSach.add(hd);
                }
            }
        }

        return danhSach;
    }


    public Map<String, Integer> tkMonthlyProfit(String Condition) throws SQLException {
        Map<String, Integer> thongKe = new LinkedHashMap<>();

        String sql = """
        SELECT  SUM(hd.TONGTIEN) AS tienthang, extract(month from hd.NGAYTHANHTOAN) as thang
        FROM HOADON hd
        """ + Condition + " GROUP BY extract(month from hd.NGAYTHANHTOAN) HAVING extract(month from hd.NGAYTHANHTOAN) is not null "
                        + "ORDER BY extract(month from hd.NGAYTHANHTOAN)";

        Platform.runLater(() -> System.out.println("1111" + sql));
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String thang = rs.getString("thang");
                int soLuong = rs.getInt("tienthang");
                thongKe.put(thang, soLuong);
            }
        }

        return thongKe;
    }

    public Map<String, Integer> tkProfitRoom(String Condition) throws SQLException {
        Map<String, Integer> thongKe = new LinkedHashMap<>();

        String sql = """
                SELECT  SUM(hd.TONGTIEN) AS profroom, p.LOAIPHONG as loaiphong
                FROM HOADON hd, DVPHONG p
                WHERE p.MADVP = hd.MADVP 
        """ + Condition + " GROUP BY p.LOAIPHONG";

        Platform.runLater(() -> System.out.println("2222" + sql));
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String loaiPhong = rs.getString("loaiphong");
                int soLuong = rs.getInt("profroom");
                thongKe.put(loaiPhong, soLuong);
            }
        }

        return thongKe;
    }

    public Map<Integer, Integer> tkProfitLine(String Condition) throws SQLException {
        Map<Integer, Integer> thongKe = new LinkedHashMap<>();

        String sql = """
                SELECT  SUM(hd.TONGTIEN) AS tienthang
                FROM HOADON hd
        """ + Condition + " GROUP BY extract(month from hd.NGAYTHANHTOAN) HAVING extract(month from hd.NGAYTHANHTOAN) is not null"
                + " ORDER BY extract(month from hd.NGAYTHANHTOAN)";
        Platform.runLater(() -> System.out.println("3333" + sql));
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            Integer i = 0;
            while (rs.next()) {
                int soLuong = rs.getInt("tienthang");
                thongKe.put(i, soLuong);
                i++;
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
