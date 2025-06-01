package controllers;

import controllers.DVPhong.DVPhongHandle;
import controllers.DVPhong.RManagerController;
import controllers.HoaDon.HoaDonHandler;
import controllers.KhachHang.KHManagerController;
import controllers.NhanVien.NVManagerController;
import controllers.ThongKe.ReportController;
import dao.DVPhongDAO;
import dao.HoaDonDAO;
import entities.DVPhong;
import entities.HoaDon;
import entities.NhanVien;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainFormController {
    private NhanVien nhanVien;
    @FXML Label namenvID;

    @FXML BorderPane mainBorderPane;
    @FXML HBox mainUI;
    @FXML private TableView<HoaDon> hoaDonTable;
    @FXML private TableColumn<HoaDon, String> maHDColumn;
    @FXML private TableColumn<HoaDon, String> maNVColumn;
    @FXML private TableColumn<HoaDon, String> maKHColumn;
    @FXML private TableColumn<HoaDon, Date> ngayTaoColumn;
    @FXML private TableColumn<HoaDon, Date> ngayTTColumn;
    @FXML private TableColumn<HoaDon, Double> tongTienColumn;
    @FXML private TableColumn<HoaDon, String> tinhTrangColumn;
    @FXML private TableColumn<HoaDon, Date> ngayBDColumn;
    @FXML private TableColumn<HoaDon, Date> ngayKTColumn;
    @FXML private TableColumn<HoaDon, Integer> soNgayColumn;
    @FXML private TableColumn<HoaDon, String> noteColumn;
    @FXML private TableColumn<HoaDon, String> maDVPColumn;

    @FXML private TableView<DVPhong> dvPhongTable;
    @FXML private TableColumn<DVPhong, String> maDVColumn;
    @FXML private TableColumn<DVPhong, String> loaiPhongColumn;
    @FXML private TableColumn<DVPhong, String> moTaColumn;
    @FXML private TableColumn<DVPhong, Double> donGiaColumn;
    @FXML private TableColumn<DVPhong, String> trangThaiColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> phongFilterCombo;
    @FXML private ComboBox<String> hoaDonFilterCombo;


    @FXML private VBox dsp;
    @FXML private Label newPhong;
    @FXML private Label newHoaDon;
    @FXML private Label roomsts1;
    @FXML private Label roomsts2;
    @FXML private Label roomsts3;
    @FXML private Label roomsts4;
    @FXML private ToggleButton btnMain;
    @FXML private ToggleButton btnQLNV;
    @FXML private ToggleButton btnQLKH;
    @FXML private ToggleButton btnQLP;
    @FXML private ToggleButton btnTK;
    Map<String, Boolean> roomsts = new HashMap<>();
    private Boolean isSelectedP = false;
    private final ContextMenu contextMenu = new ContextMenu();
    @FXML private TextField searchFieldfake;

    private HoaDonHandler hoaDonHandler;
    private DVPhongHandle dvPhongHandler;

    private final ObservableList<HoaDon> listHoaDonCurrent = FXCollections.observableArrayList();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ObservableList<DVPhong> listPhongCurrent = FXCollections.observableArrayList();
    private final DVPhongDAO phongDAO = new DVPhongDAO();


    public void initialize() {

        hoaDonHandler.init(hoaDonDAO, searchField, hoaDonFilterCombo, hoaDonTable, listHoaDonCurrent);
        dvPhongHandler.init(phongDAO, phongFilterCombo, dvPhongTable, listPhongCurrent, roomsts, searchFieldfake);
        setupMainForm();
        initContextMenu();
        setupTableColumnsHD();
        setupTableColumnsP();
    }

    private void setupMainForm() {

        dsp.prefWidthProperty().bind(mainBorderPane.widthProperty().multiply(0.4));
        roomsts4.setVisible(false);
        btnMain.setSelected(true);
        Platform.runLater(() -> namenvID.setText("Lễ Tân: " + nhanVien.getHoTen()));


        handle_Status(true, false, false, roomsts1);
        handle_Status(false, true, false, roomsts2);
        handle_Status(false, false, true, roomsts3);
        handle_Status(false, false, false, roomsts4);
        roomsts1.setOnMouseClicked(event -> { handle_Status(true, false, false, roomsts1); });
        roomsts2.setOnMouseClicked(event -> { handle_Status(false, true, false, roomsts2); });
        roomsts3.setOnMouseClicked(event -> { handle_Status(false, false, true, roomsts3); });

        newHoaDon.setOnMouseClicked(event -> {
            hoaDonFilterCombo.getSelectionModel().select(2);
            searchField.clear();
            hoaDonHandler.loadDatafromDB();
            hoaDonHandler.updateFilterPredicate();

        });
        newPhong.setOnMouseClicked(event -> {
            phongFilterCombo.getSelectionModel().select(0);
            handle_Status(false, false, false, roomsts4);
            dvPhongHandler.loadDatafromDB();
            dvPhongHandler.updateFilterPredicate();

        });
    }

    private void setupTableColumnsHD() {
        maHDColumn.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        maNVColumn.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        maKHColumn.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        ngayTaoColumn.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));
        ngayTTColumn.setCellValueFactory(new PropertyValueFactory<>("ngayThanhToan"));
        tongTienColumn.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        tinhTrangColumn.setCellValueFactory(new PropertyValueFactory<>("tinhTrangTT"));
        ngayBDColumn.setCellValueFactory(new PropertyValueFactory<>("ngayBDSD"));
        ngayKTColumn.setCellValueFactory(new PropertyValueFactory<>("ngayKTSD"));
        soNgayColumn.setCellValueFactory(new PropertyValueFactory<>("soNgaySuDung"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        maDVPColumn.setCellValueFactory(new PropertyValueFactory<>("maDVP"));

        searchField.textProperty().addListener((obs, oldVal, newVal) -> hoaDonHandler.updateFilterPredicate());
        hoaDonFilterCombo.valueProperty().addListener((obs, oldVal, newVal) -> hoaDonHandler.updateFilterPredicate());
        Platform.runLater(() -> hoaDonFilterCombo.getSelectionModel().select("Tất cả"));
    }



    private void setupTableColumnsP() {
        maDVColumn.setCellValueFactory(new PropertyValueFactory<>("maDVP"));
        loaiPhongColumn.setCellValueFactory(new PropertyValueFactory<>("loaiPhong"));
        moTaColumn.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        donGiaColumn.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        trangThaiColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        phongFilterCombo.valueProperty().addListener((obs, oldVal, newVal) -> dvPhongHandler.updateFilterPredicate());
        Platform.runLater(() -> phongFilterCombo.getSelectionModel().select("Tất cả"));
    }

    private void handle_Status(Boolean s1, Boolean s2, Boolean s3, Label statusLabel) {
        roomsts.put("Trống", s1); roomsts.put("Đã được đặt", s2); roomsts.put("Bảo trì", s3);
        dvPhongHandler.updateFilterPredicate();
        statusLabel.setText(dvPhongTable.getItems().size() + " Phòng");
    }


    @FXML
    private void handleBooking() {
        DVPhong selected = dvPhongTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn phòng!");
            return;
        } else if (!selected.getTrangThai().equals("Trống")) {
            showAlert(Alert.AlertType.WARNING, "Phòng " + selected.getTrangThai(), "Vui lòng chọn phòng trống!");
            return;
        }
        // điều phối giao diện đặt phòng
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/Khachhang/BookingForm.fxml"));
            Parent root = loader.load();

            KHManagerController controller = loader.getController();
            controller.setPhong(selected);
            controller.setNhanVien(nhanVien);
            controller.checkManHinh(true);

            // Gửi callback để cập nhật và reload lại dữ liệu
            controller.setOnSuccess(() -> {
                hoaDonHandler.loadDatafromDB();
                hoaDonHandler.updateFilterPredicate();
            });

            Stage stage = new Stage();
            stage.setTitle("Thủ tục đặt phòng");
            stage.setScene(new Scene(root, 700, 700));
            stage.initModality(Modality.APPLICATION_MODAL); // chặn tương tác cửa sổ chính
            controller.setDialogStage(stage); // truyền stage vào controller
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Lỗi",  e.getMessage());

        }
    }

    @FXML
    private void handleRoomManagement() {

        // điều phối màn hình mới
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/DVPhong/RManagerForm.fxml"));
            Parent QLKHroot = loader.load();

            RManagerController controller = loader.getController();
            controller.setNhanVien(nhanVien);
            if (mainBorderPane != null) {
                mainBorderPane.setCenter(QLKHroot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: chưa được inject. Kiểm tra fx:id ");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải phan hoi y kien.\nChi tiết: " + e.getMessage());
        }
        setupIsSelectedMenu(false, true, false, false, false);
    }

    @FXML
    private void handleReport() {

        // check role
        if (!nhanVien.isManager()) {
            showAlert(Alert.AlertType.WARNING, "Không có quyền này " , "Chỉ dành cho quản lý");
            btnTK.setSelected(false);
            return;
        }
        // điều phối màn hình mới
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/Thongke/ReportForm.fxml"));
            Parent TKroot = loader.load();

            ReportController controller = loader.getController();
//            controller.setNhanVien(nhanVien);\
            controller.initialize();
            if (mainBorderPane != null) {
                mainBorderPane.setCenter(TKroot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: chưa được inject. Kiểm tra fx:id ");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải phan hoi y kien.\nChi tiết: " + e.getMessage());
        }
        setupIsSelectedMenu(false, false, false, false, true);

    }


    @FXML
    private void handleStaffManager() {

        // check role
        if (!nhanVien.isManager()) {
            showAlert(Alert.AlertType.WARNING, "Không có quyền này " , "Chỉ dành cho quản lý");
            btnQLNV.setSelected(false);
            return;
        }
        // điều phối màn hình mới
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/NhanVien/NVManagerForm.fxml"));
            Parent QLKHroot = loader.load();

            NVManagerController controller = loader.getController();
            controller.setNhanVien(nhanVien);
            if (mainBorderPane != null) {
                mainBorderPane.setCenter(QLKHroot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: chưa được inject. Kiểm tra fx:id ");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải phan hoi y kien.\nChi tiết: " + e.getMessage());
        }
        setupIsSelectedMenu(false, false, false, true, false);

    }


    @FXML
    private void handleCustomerManager() {
        // điều phối màn hình mới
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/Khachhang/KHManagerForm.fxml"));
            Parent QLKHroot = loader.load();

            KHManagerController controller = loader.getController();
            controller.setNhanVien(nhanVien);
            controller.checkManHinh(false);

            if (mainBorderPane != null) {
                mainBorderPane.setCenter(QLKHroot); // Đặt nội dung mới vào vùng center
            } else {
                System.err.println("Lỗi: chưa được inject. Kiểm tra fx:id ");
                showAlert(Alert.AlertType.ERROR, "Lỗi Giao Diện", "Không thể hiển thị ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải phan hoi y kien.\nChi tiết: " + e.getMessage());
        }
        setupIsSelectedMenu(false, false, true, false, false);
    }

    @FXML
    private void handleLogout() {
        Stage currentStage = (Stage) mainBorderPane.getScene().getWindow();
        if (currentStage != null) {
            currentStage.close();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/form/application/DangNhapForm.fxml"));
            Parent loginRoot = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Đăng Nhập Hệ Thống");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setResizable(false);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể quay lại trang đăng nhập.");
        }
    }


    @FXML
    private void handleBookingManager() {
        // điều phối màn hình mới
        if (mainBorderPane != null) {
            if (mainUI != null) {
                mainBorderPane.setCenter(mainUI);
                hoaDonHandler.init(hoaDonDAO, searchField, hoaDonFilterCombo, hoaDonTable, listHoaDonCurrent);
                dvPhongHandler.init(phongDAO, phongFilterCombo, dvPhongTable, listPhongCurrent, roomsts, searchFieldfake);
                hoaDonHandler.loadDatafromDB();
                dvPhongHandler.loadDatafromDB();
                dvPhongHandler.updateFilterPredicate();
                hoaDonHandler.updateFilterPredicate();

                setupMainForm();
                setupTableColumnsHD();
                setupTableColumnsP();
            } else {
                // Fallback nếu centerContentArea không được inject (ví dụ: fx:id bị thiếu trong FXML)
                // Hoặc nếu bạn muốn tạo nội dung mặc định động
                System.out.println("Thông báo: centerContentArea không được inject, tạo nội dung mặc định.");
                Label defaultLabel = new Label("Chào mừng đến với hệ thống!");
                defaultLabel.setFont(new Font("System", 18)); // Sử dụng tên font hợp lệ
                Label subLabel = new Label("Sử dụng menu để điều hướng các chức năng.");
                subLabel.setFont(new Font("System", 14));

                VBox defaultVBox = new VBox(20, defaultLabel, subLabel); // Khoảng cách giữa các label là 20
                defaultVBox.setAlignment(Pos.CENTER); // Căn giữa nội dung của VBox
                defaultVBox.setPadding(new Insets(30)); // Thêm padding
                mainBorderPane.setCenter(defaultVBox);
            }
        } else {
            System.err.println("Lỗi: mainBorderPane chưa được inject");
        }
        setupIsSelectedMenu(true, false, false, false, false);
    }

    // xử lý thao tác hóa đơn
    private void initContextMenu() {
        MenuItem suaItem = new MenuItem("Trả phòng");
        suaItem.setOnAction(event -> {
            handle_traphong();

        });

        MenuItem xoaItem = new MenuItem("Xóa hóa đơn");
        xoaItem.setOnAction(event -> {
            handle_Xoa();
        });

        contextMenu.getItems().addAll(suaItem, xoaItem);

        hoaDonTable.setOnContextMenuRequested(event ->
                contextMenu.show(hoaDonTable, event.getScreenX(), event.getScreenY()));
    }

    private void handle_Xoa() {
        HoaDon hoaDonSelected = hoaDonTable.getSelectionModel().getSelectedItem();
        try {
            hoaDonDAO.xoaHoaDon(hoaDonSelected.getMaHD());
        }
        catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, e.getMessage() , " Lỗi ràng buộc nha ae");
        }

        // reload data
        hoaDonHandler.loadDatafromDB();
        hoaDonHandler.updateFilterPredicate();
    }

    private void handle_traphong() {
        HoaDon hoaDonSelected = hoaDonTable.getSelectionModel().getSelectedItem();
        if (hoaDonSelected.getTinhTrangTT().equals("Hết hiệu lực")) {
            showInfo("Phòng đã được trả");
            return;
        }
        // set tt
        hoaDonSelected.setTinhTrangTT("Hết hiệu lực");
        hoaDonSelected.setNgayThanhToan(java.sql.Date.valueOf(LocalDate.now()));
        try {
            hoaDonDAO.suaHoaDon(hoaDonSelected);
        }
        catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Loi" , e.getMessage());
        }

        // reload data
        hoaDonHandler.loadDatafromDB();
        hoaDonHandler.updateFilterPredicate();
    }

    private void setupIsSelectedMenu(Boolean s1, Boolean s2, Boolean s3, Boolean s4, Boolean s5) {
        btnMain.setSelected(s1);
        btnQLP.setSelected(s2);
        btnQLKH.setSelected(s3);
        btnQLNV.setSelected(s4);
        btnTK.setSelected(s5);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
}