package controllers.DVPhong;

import dao.DVPhongDAO;
import entities.DVPhong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DVPhongHandle {
    private static DVPhongDAO dVPhongDAO;
    private static ObservableList<DVPhong> listdVPhongCurrent;
    private static FilteredList<DVPhong> filteredListHD;
    private static TableView<DVPhong> dVPhongTable;
    private static ComboBox<String> filterCombo;
    private static TextField searchField;
    private static Map<String, Boolean> roomsts;

    // Private constructor to prevent instantiation
    private DVPhongHandle() { }

    public static void init(DVPhongDAO dao,
                            ComboBox<String> filterComboParam,
                            TableView<DVPhong> table,
                            ObservableList<DVPhong> passedList,
                            Map<String, Boolean> roomstS,
                            TextField searchFieldParam) {
        // Assign dependencies
        dVPhongDAO = dao;
        searchField = searchFieldParam;
        filterCombo = filterComboParam;
        dVPhongTable = table;
        listdVPhongCurrent = passedList;
        roomsts = new HashMap<>();
        roomsts = roomstS;

        loadDatafromDB();
    }

    public static void loadDatafromDB() {
        try {
            listdVPhongCurrent.setAll(dVPhongDAO.traCuuDVPhong(""));
            FXCollections.sort(listdVPhongCurrent, Comparator.comparing(DVPhong::getDonGia));
            filteredListHD = new FilteredList<>(listdVPhongCurrent, p -> true);
            dVPhongTable.setItems(filteredListHD);
        } catch (SQLException e) {
//            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể lấy danh sách hd: " + e.getMessage());
        }
    }

    public static void updateFilterPredicate() {
        String keyword = searchField.getText().trim().toUpperCase();
        String filterValue = filterCombo.getValue();

        filteredListHD.setPredicate(p -> {
            boolean matchText = keyword.isEmpty()
                    || p.getMaDVP().toUpperCase().contains(keyword)
                    || p.getLoaiPhong().toUpperCase().contains(keyword)
                    || p.getMoTa().toUpperCase().contains(keyword);

            boolean matchStatus = false;
            int i = 0;
            for (Map.Entry<String, Boolean> entry : roomsts.entrySet()) {
                if (entry.getValue() == true && entry.getKey().equals(p.getTrangThai().trim())) {
                    matchStatus = true;
                    break;
                }
                if (!entry.getValue()) { i++ ;}
            }
            if (i==3) { matchStatus = true; }

            boolean matchFilter = filterValue == null || "Tất cả".equals(filterValue)
                    || p.getLoaiPhong().trim().equals(filterValue);

            return matchFilter && matchStatus && matchText;
        });
    }

}
