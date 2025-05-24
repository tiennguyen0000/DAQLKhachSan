package controllers.KhachHang;

import dao.HoaDonDAO;
import dao.KhachHangDAO;
import entities.DVPhong;
import entities.HoaDon;
import entities.KhachHang;
import entities.NhanVien;
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

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class KHManagerController {

    private Boolean isBooking = true;
    private NhanVien nhanVien;

    private Stage dialogStage;
    private Runnable onSuccess;
    private DVPhong dvPhong;
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private KhachHangHandle khachHangHandle;
    private final ObservableList<KhachHang> listKhachHangCurrent = FXCollections.observableArrayList();
    private final ContextMenu contextMenu = new ContextMenu();

    @FXML private TableView<KhachHang> tableKhachHang;
    @FXML private TableColumn<KhachHang, String> colMaKH;
    @FXML private TableColumn<KhachHang, String> colHoTen;
    @FXML private TableColumn<KhachHang, String> colCCCD;
    @FXML private TableColumn<KhachHang, String> colSDT;
    @FXML private TableColumn<KhachHang, Date> colNgaySinh;
    @FXML private TableColumn<KhachHang, String> colGioiTinh;
    @FXML private TableColumn<KhachHang, String> colDiaChi;
    @FXML private TableColumn<KhachHang, String> colEmail;
    @FXML private TextField searchField;
    @FXML private DatePicker checkInDate;
    @FXML private DatePicker checkOutDate;
    @FXML private Label loaiPhongID;
    @FXML private Label donGiaID;
    @FXML private Label tongTienid;
    @FXML private Text txtStatusND;
    @FXML private TextArea notesArea;


    // Info customer register and edit
    @FXML private TextField fullNameField;
    @FXML private TextField cccdField;
    @FXML private TextField phoneField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField emailField;
    @FXML private TitledPane infoFormID;
    @FXML private Button btnThemSua;

    private KhachHang khachHangSelected;



    public void initialize() {

        try {
            khachHangHandle.init(khachHangDAO, searchField, tableKhachHang, listKhachHangCurrent);
            setupTableColumnsKH();
            setupInfoBookingForm();
        } catch (Exception e) {
            System.out.println(e);
        }
        // các thao tác sửa, xóa khách hàng không được dùng trong giao diện đặt phòng
        Platform.runLater(() -> {
            if (!isBooking) {
                initContextMenu();
            }
        });


    }

    public void setPhong(DVPhong dvPhong) {
        this.dvPhong = dvPhong;
    }


    private void setupTableColumnsKH() {
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        colNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        khachHangSelected = new KhachHang();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> khachHangHandle.updateFilterPredicate());
    }

    private void initContextMenu() {
        MenuItem suaItem = new MenuItem("Sửa thông tin");
        suaItem.setOnAction(event -> {
            try {
                handle_Sua();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        MenuItem xoaItem = new MenuItem("Xóa khách hàng");
        xoaItem.setOnAction(event -> {
            try {
                handle_Xoa();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        contextMenu.getItems().addAll(suaItem, xoaItem);

        tableKhachHang.setOnContextMenuRequested(event ->
                contextMenu.show(tableKhachHang, event.getScreenX(), event.getScreenY()));
    }

    private void setupContextKHManager() {
        
    }

    private void handle_Xoa() throws SQLException {
        khachHangSelected = tableKhachHang.getSelectionModel().getSelectedItem();
        try {
            khachHangDAO.xoaKhachHang(khachHangSelected.getMaKH());
        }
        catch (Exception e) {
            System.out.println(e);
            showAlert(Alert.AlertType.WARNING, e.getMessage() + " : Lỗi ràng buộc nha ae");
        }

        // reload data
        khachHangHandle.loadDataHDfromDB();
        khachHangHandle.updateFilterPredicate();
    }

    private void handle_Sua() throws SQLException {
        infoFormID.setExpanded(true);
        btnThemSua.setText("Lưu");

        // điền vào form dữ liệu hiện tại của khách hàng
        khachHangSelected = tableKhachHang.getSelectionModel().getSelectedItem();
        infoFormID.setText("Sửa thông tin khách hàng: " + khachHangSelected.getMaKH());
        fullNameField.setText(khachHangSelected.getHoTen());
        cccdField.setText(khachHangSelected.getCccd());
        phoneField.setText(khachHangSelected.getSdt());
        dobPicker.setValue(khachHangSelected.getNgaySinh().toLocalDate());
        genderComboBox.setValue(khachHangSelected.getGioiTinh());
        emailField.setText(khachHangSelected.getEmail());

    }



    @FXML
    private void handleRegisterAndEdit() throws SQLException {

        if (!validateInput()) {
            return;
        }

        khachHangSelected.setCccd(cccdField.getText());
        khachHangSelected.setEmail(emailField.getText());
        khachHangSelected.setGioiTinh(genderComboBox.getValue());
        khachHangSelected.setHoTen(fullNameField.getText());
        khachHangSelected.setNgaySinh(java.sql.Date.valueOf(dobPicker.getValue()));
        khachHangSelected.setSdt(phoneField.getText());
        khachHangSelected.setDiaChi("");

        // này hơi lỏ nhưng cho nhanh )))
        if (btnThemSua.getText().equals("Đăng ký")) {
            khachHangDAO.themKhachHang(khachHangSelected);
            showStatusNoidung("Đăng ký thành công!");
        } else {
            khachHangDAO.suaKhachHang(khachHangSelected);
            showStatusNoidung("Sửa thành công!");
        }
        // set up ve ban dau
        clearFormRegister();
        infoFormID.setText("Đăng ký khách hàng");
        btnThemSua.setText("Đăng ký");
        // reload data
        khachHangHandle.loadDataHDfromDB();
        khachHangHandle.updateFilterPredicate();
    }

    @FXML
    private void handleHuy() {
        dialogStage.close();
    }

    @FXML
    private void handleXN() throws SQLException {
        KhachHang selected = tableKhachHang.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Hãy chọn khách hàng");
            return;
        }

        String maNV = nhanVien.getMaNV();
        String maKH = selected.getMaKH();
        String maDVP = dvPhong.getMaDVP();

        Date ngayTao = java.sql.Date.valueOf(LocalDate.now());
        Date ngayBDSD = java.sql.Date.valueOf(checkInDate.getValue());
        Date ngayKTSD = java.sql.Date.valueOf(checkOutDate.getValue());


        LocalDate checkIn = checkInDate.getValue();
        LocalDate checkOut = checkOutDate.getValue();
        long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut);
        double tongTien = (daysBetween - 1.0) * dvPhong.getDonGia();
        String tinhTrangTT = "Còn hiệu lực";
        String note = notesArea.getText().trim();

        HoaDon hoaDon = new HoaDon(maNV, maKH, ngayTao, tongTien, tinhTrangTT, ngayBDSD, ngayKTSD, note, maDVP);

        hoaDonDAO.themHoaDon(hoaDon);
        clearFormRegister();
        // gọi callback về controller gốc
        if (onSuccess != null) {
            dialogStage.close();
            onSuccess.run();

        }
    }


    private void clearFormRegister() {
        fullNameField.clear();
        cccdField.clear();
        phoneField.clear();
        dobPicker.setValue(null);
        genderComboBox.setValue(null);
        emailField.clear();
    }
    // tính theo đơn giá * số ngày sd
    private void tongTienCaculation() {
        LocalDate checkIn = checkInDate.getValue();
        LocalDate checkOut = checkOutDate.getValue();
        long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut);
        tongTienid.setText(daysBetween * dvPhong.getDonGia() + " VND");

    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
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

    private void setupInfoBookingForm() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // Đặt mặc định
        checkInDate.setValue(today);
        checkOutDate.setValue(tomorrow);

        // Ràng buộc: check-in >= hôm nay
        checkInDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });

        // Ràng buộc: check-out >= ngày mai
        checkOutDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(tomorrow));
            }
        });

        //set up thong tin phong
        Platform.runLater(() -> loaiPhongID.setText(dvPhong.getLoaiPhong()));
        Platform.runLater(() -> donGiaID.setText(String.format("%.0f", dvPhong.getDonGia())));

        // tính tổng tiền
        checkInDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            tongTienCaculation();
        });
        checkOutDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            tongTienCaculation();
        });
        Platform.runLater(() -> tongTienCaculation());
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
        String hoTen = fullNameField.getText().trim();
        String cccd = cccdField.getText().trim();
        String sdt = phoneField.getText().trim();
        LocalDate dob = dobPicker.getValue();
        String gioiTinh = genderComboBox.getValue();
        String email = emailField.getText().trim();

        if (hoTen.isEmpty() || cccd.isEmpty() || sdt.isEmpty() || dob == null || gioiTinh == null || email.isEmpty()) {
            showStatusNoidung("Vui lòng nhập đầy đủ thông tin.");
            return false;
        }

        if (!cccd.matches("\\d{12}")) {
            showStatusNoidung("CCCD phải gồm đúng 12 chữ số.");
            return false;
        }

        if (!sdt.matches("0\\d{9}")) {
            showStatusNoidung( "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0.");
            return false;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            showStatusNoidung( "Email không hợp lệ.");
            return false;
        }

        if (dob.isAfter(LocalDate.now())) {
            showStatusNoidung( "Ngày sinh không được lớn hơn ngày hiện tại.");
            return false;
        }

        return true;
    }

    // hơi lỏ, cái này để check màn hình, code này lỡ thiết kế v r lười chỉnh lại quá
    public void checkManHinh(Boolean isBooking) {
        this.isBooking = isBooking;
    }
}
