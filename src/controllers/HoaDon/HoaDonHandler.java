package controllers.HoaDon;

import dao.HoaDonDAO;
import entities.HoaDon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;

public final class HoaDonHandler {
    private static HoaDonDAO hoaDonDAO;
    private static ObservableList<HoaDon> listHoaDonCurrent;
    private static FilteredList<HoaDon> filteredListHD;
    private static TableView<HoaDon> hoaDonTable;
    private static ComboBox<String> filterCombo;
    private static TextField searchField;

    // Private constructor to prevent instantiation
    private HoaDonHandler() { }

    public static void init(HoaDonDAO dao,
                            TextField searchFieldParam,
                            ComboBox<String> filterComboParam,
                            TableView<HoaDon> table,
                            ObservableList<HoaDon> passedList) {
        // Assign dependencies
        hoaDonDAO = dao;
        searchField = searchFieldParam;
        filterCombo = filterComboParam;
        hoaDonTable = table;
        listHoaDonCurrent = passedList;

        loadDatafromDB();
    }

    public static void loadDatafromDB() {
        try {
            listHoaDonCurrent.setAll(hoaDonDAO.traCuuHoaDon(""));
            FXCollections.sort(listHoaDonCurrent, Comparator.comparing(HoaDon::getNgayTao).reversed());
            filteredListHD = new FilteredList<>(listHoaDonCurrent, p -> true);
            hoaDonTable.setItems(filteredListHD);
        } catch (SQLException e) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể lấy danh sách hd: " + e.getMessage());
        }
    }

    public static void updateFilterPredicate() {
        String keyword = searchField.getText().trim().toUpperCase();
        String filterValue = filterCombo.getValue();

        filteredListHD.setPredicate(hd -> {
            boolean matchText = keyword.isEmpty()
                    || hd.getMaHD().toUpperCase().contains(keyword)
                    || hd.getMaKH().toUpperCase().contains(keyword)
                    || Double.toString(hd.getTongTien()).contains(keyword);

            boolean matchFilter = (filterValue == null || "Tất cả".equals(filterValue)
                    || hd.getTinhTrangTT().trim().equals(filterValue)) ||
                    (hd.getNgayTao().toLocalDate().equals(LocalDate.now()) && filterValue.equals("Gần đây"));

            return matchText && matchFilter;
        });
    }
}


