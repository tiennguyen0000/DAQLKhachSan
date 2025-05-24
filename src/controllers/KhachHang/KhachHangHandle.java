package controllers.KhachHang;

import dao.KhachHangDAO;
import entities.HoaDon;
import entities.KhachHang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Comparator;

public final class KhachHangHandle {
    private static KhachHangDAO khachHangDAO;
    private static ObservableList<KhachHang> listKhachHangCurrent;
    private static FilteredList<KhachHang> filteredListKH;
    private static TableView<KhachHang> khachHangTable;
    private static TextField searchField;

    // Private constructor to prevent instantiation
    private KhachHangHandle() { }

    public static void init(KhachHangDAO dao,
                            TextField searchFieldParam,
                            TableView<KhachHang> table,
                            ObservableList<KhachHang> passedList) {
        // Assign dependencies
        khachHangDAO = dao;
        searchField = searchFieldParam;
        khachHangTable = table;
        listKhachHangCurrent = passedList;

        loadDataHDfromDB();
    }

    public static void loadDataHDfromDB() {
        try {
            listKhachHangCurrent.setAll(khachHangDAO.traCuuKhachHang(""));
//            FXCollections.sort(listKhachHangCurrent, Comparator.comparing(HoaDon::getNgayTao).reversed());
            filteredListKH = new FilteredList<>(listKhachHangCurrent, p -> true);
            khachHangTable.setItems(filteredListKH);
        } catch (SQLException e) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể lấy danh sách hd: " + e.getMessage());
        }
    }

    public static void updateFilterPredicate() {
        String keyword = searchField.getText().trim().toUpperCase();

        filteredListKH.setPredicate(kh -> {
            boolean matchText = keyword.isEmpty()
                    || kh.getHoTen().toUpperCase().contains(keyword)
                    || kh.getMaKH().toUpperCase().contains(keyword)
                    || kh.getSdt().toUpperCase().contains(keyword);
            return matchText;
        });
    }
}
