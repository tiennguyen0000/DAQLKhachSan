package controllers.NhanVien;

import dao.NhanVienDAO;

import entities.NhanVien;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class NhanVienHandle {
    private static NhanVienDAO nhanVienDAO;
    private static ObservableList<NhanVien> listnhanVienCurrent;
    private static FilteredList<NhanVien> filteredListKH;
    private static TableView<NhanVien> nhanVienTable;
    private static TextField searchField;

    // Private constructor to prevent instantiation
    private NhanVienHandle() { }

    public static void init(NhanVienDAO dao,
                            TextField searchFieldParam,
                            TableView<NhanVien> table,
                            ObservableList<NhanVien> passedList) {
        // Assign dependencies
        nhanVienDAO = dao;
        searchField = searchFieldParam;
        nhanVienTable = table;
        listnhanVienCurrent = passedList;

        loadDataHDfromDB();
    }

    public static void loadDataHDfromDB() {
        try {
            listnhanVienCurrent.setAll(nhanVienDAO.traCuuNhanVien(""));
//            FXCollections.sort(listnhanVienCurrent, Comparator.comparing(HoaDon::getNgayTao).reversed());
            filteredListKH = new FilteredList<>(listnhanVienCurrent, p -> true);
            nhanVienTable.setItems(filteredListKH);
        } catch (SQLException e) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể lấy danh sách hd: " + e.getMessage());
        }
    }

    public static void updateFilterPredicate() {
        String keyword = searchField.getText().trim().toUpperCase();

        filteredListKH.setPredicate(nv -> {
            boolean matchText = keyword.isEmpty()
                    || nv.getHoTen().toUpperCase().contains(keyword)
                    || nv.getMaNV().toUpperCase().contains(keyword)
                    || nv.getSdt().toUpperCase().contains(keyword);
            return matchText;
        });
    }
}
