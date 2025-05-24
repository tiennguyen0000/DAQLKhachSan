package controllers.DVPhong;

import dao.DVPhongDAO;
import entities.DVPhong;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RManagerController {


    private Stage dialogStage;
    private Runnable onSuccess;
    @FXML private TableView<DVPhong> dvPhongTable;
    @FXML private TableColumn<DVPhong, String> maDVColumn;
    @FXML private TableColumn<DVPhong, String> loaiPhongColumn;
    @FXML private TableColumn<DVPhong, String> moTaColumn;
    @FXML private TableColumn<DVPhong, Double> donGiaColumn;
    @FXML private TableColumn<DVPhong, String> trangThaiColumn;
    @FXML private ComboBox<String> phongFilterCombo;
    @FXML private TextField searchField;


    // Info customer register and edit

    @FXML private TextField txtLoaiPhong;
    @FXML private TextField txtMoTa;
    @FXML private TextField txtDonGia;
    @FXML private ComboBox<String> comboTrangThai;
    @FXML private Button btnThemSua;
    @FXML private TitledPane infoFormID;
    @FXML private Text txtStatusND;


    private DVPhongHandle dvPhongHandler;
    private final ContextMenu contextMenu = new ContextMenu();
    Map<String, Boolean> roomsts = new HashMap<>();
    private final ObservableList<DVPhong> listPhongCurrent = FXCollections.observableArrayList();
    private final DVPhongDAO phongDAO = new DVPhongDAO();

    private DVPhong dVPhongSelected;



    public void initialize() {
        setupParameter();
        setupFilter();
        dvPhongHandler.init(phongDAO, phongFilterCombo, dvPhongTable, listPhongCurrent, roomsts, searchField);
        setupTableColumnsP();
        initContextMenu();

    }


    private void setupTableColumnsP() {
        maDVColumn.setCellValueFactory(new PropertyValueFactory<>("maDVP"));
        loaiPhongColumn.setCellValueFactory(new PropertyValueFactory<>("loaiPhong"));
        moTaColumn.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        donGiaColumn.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        trangThaiColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));



    }
    private void setupParameter() {
        roomsts.put("trống", false); roomsts.put("đã được đặt", false); roomsts.put("bảo trì", false);
        dVPhongSelected = new DVPhong();
    }

    private void setupFilter() {
        Platform.runLater(() -> phongFilterCombo.getSelectionModel().select("Tất cả"));
        phongFilterCombo.valueProperty().addListener((obs, oldVal, newVal) -> dvPhongHandler.updateFilterPredicate());

        searchField.textProperty().addListener((obs, oldVal, newVal) -> dvPhongHandler.updateFilterPredicate());
    }
    private void initContextMenu() {
        MenuItem suaItem = new MenuItem("Sửa thông tin phòng");
        suaItem.setOnAction(event -> {
            try {
                handle_Sua();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        MenuItem xoaItem = new MenuItem("Xóa phòng");
        xoaItem.setOnAction(event -> {
            try {
                handle_Xoa();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        contextMenu.getItems().addAll(suaItem, xoaItem);

        dvPhongTable.setOnContextMenuRequested(event ->
                contextMenu.show(dvPhongTable, event.getScreenX(), event.getScreenY()));
    }

    private void setupContextKHManager() {

    }

    private void handle_Xoa() throws SQLException {
        dVPhongSelected = dvPhongTable.getSelectionModel().getSelectedItem();
        try {
            phongDAO.xoaDVPhong(dVPhongSelected.getMaDVP());
        }
        catch (Exception e) {
            System.out.println(e);
            showAlert(Alert.AlertType.WARNING, e.getMessage() + " : Lỗi ràng buộc nha ae");
        }

        // reload data
        dvPhongHandler.loadDatafromDB();
        dvPhongHandler.updateFilterPredicate();
    }

    private void handle_Sua() throws SQLException {
        infoFormID.setExpanded(true);
        btnThemSua.setText("Lưu thay đổi");

        // điền vào form dữ liệu hiện tại của khách hàng
        dVPhongSelected = dvPhongTable.getSelectionModel().getSelectedItem();
        infoFormID.setText("Sửa thông tin phòng: " + dVPhongSelected.getMaDVP());
        txtLoaiPhong.setText(dVPhongSelected.getLoaiPhong());
        txtMoTa.setText(dVPhongSelected.getMoTa());
        txtDonGia.setText(String.format("%.0f", dVPhongSelected.getDonGia()));
        comboTrangThai.setValue(dVPhongSelected.getTrangThai());


    }



    @FXML
    private void handleLuu() throws SQLException {

        if (!validateInput()) {
            return;
        }


        dVPhongSelected.setLoaiPhong(txtLoaiPhong.getText());
        dVPhongSelected.setMoTa(txtMoTa.getText());
        dVPhongSelected.setDonGia(Double.parseDouble(txtDonGia.getText()));
        dVPhongSelected.setTrangThai(comboTrangThai.getValue());

        // cái này lỏ vl nhưng cho nhanh )))
        if (infoFormID.getText().equals("Thêm phòng mới")) {
            phongDAO.themDVPhong(dVPhongSelected);
            showStatusNoidung("Thêm thành công!");
        } else {
            phongDAO.suaDVPhong(dVPhongSelected);
            showStatusNoidung("Sửa thành công!");
        }
        // set up ve ban dau
        clearForm();
        infoFormID.setText("Thêm phòng mới");
        // reload data
        dvPhongHandler.loadDatafromDB();
        dvPhongHandler.updateFilterPredicate();
    }

    @FXML
    private void handleHuy() {
        dialogStage.close();
    }





    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    // để callback
    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void handleSearchInput(KeyEvent keyEvent) {
    }

    // Hàm hiển thị hộp thoại cảnh báo
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private boolean validateInput() {
        String loaiPhong = txtLoaiPhong.getText().trim();
        String moTa = txtMoTa.getText().trim();
        String donGiaStr = txtDonGia.getText().trim();
        String trangThai = comboTrangThai.getValue();

        // Kiểm tra trường rỗng
        if (loaiPhong.isEmpty() || moTa.isEmpty() || donGiaStr.isEmpty() || trangThai == null) {
            showStatusNoidung("Vui lòng nhập đầy đủ thông tin phòng.");
            return false;
        }

        // Kiểm tra định dạng đơn giá
        double donGia;
        try {
            donGia = Double.parseDouble(donGiaStr);
            if (donGia <= 0) {
                showStatusNoidung("Đơn giá phải lớn hơn 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showStatusNoidung("Đơn giá không hợp lệ (phải là số).");
            return false;
        }

        // Hợp lệ
        return true;
    }


    private void clearForm() {
        txtLoaiPhong.clear();
        txtMoTa.clear();
        txtDonGia.clear();
        comboTrangThai.getSelectionModel().clearSelection();
    }

    private void showStatusNoidung(String status) {
        txtStatusND.setText(status);
        // doi mau =)
        txtStatusND.setFill(Color.RED);
        if (status.equals("Đăng ký thành công!") || status.equals("Sửa thành công!")) {
            txtStatusND.setFill(Color.GREEN);
        }
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> txtStatusND.setText(""))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }


}
