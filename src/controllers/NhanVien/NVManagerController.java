package controllers.NhanVien;


import dao.HoaDonDAO;
import dao.NhanVienDAO;

import entities.DVPhong;
import entities.HoaDon;

import entities.NhanVien;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NVManagerController {
    private NhanVien nhanVien;

    private Stage dialogStage;
    private Runnable onSuccess;
    private DVPhong dvPhong;
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private NhanVienHandle nhanVienHandle;
    private final ObservableList<NhanVien> listnhanVienCurrent = FXCollections.observableArrayList();
    private final ContextMenu contextMenu = new ContextMenu();

    @FXML private TableView<NhanVien> tableNhanVien;
    @FXML private TableColumn<NhanVien, String> colMaNV;
    @FXML private TableColumn<NhanVien, String> colHoTen;
    @FXML private TableColumn<NhanVien, String> colCCCD;
    @FXML private TableColumn<NhanVien, String> colSDT;
    @FXML private TableColumn<NhanVien, Date> colNgaySinh;
    @FXML private TableColumn<NhanVien, String> colGioiTinh;
    @FXML private TableColumn<NhanVien, String> colDiaChi;
    @FXML private TableColumn<NhanVien, String> colEmail;
    @FXML private TableColumn<NhanVien, LocalDate> colNgayVaoLam;
    @FXML private TableColumn<NhanVien, Double> colLuong;
    @FXML private TableColumn<NhanVien, String> colChucVu;
    @FXML private TableColumn<NhanVien, String> colMaQL;
    @FXML private TextField searchField;
    @FXML private Text txtStatusND;
    @FXML private TextArea notesArea;


    // Info customer register and edit
    @FXML private TextField fullNameField;
    @FXML private TextField cccdField;
    @FXML private TextField phoneField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField emailField;
    @FXML private TextField diaChiField;
    @FXML private DatePicker ngayVaoLamPicker;
    @FXML private TextField luongField;
//    @FXML private ComboBox chucvuComboBox;
    @FXML private TextField maQLField;
    @FXML private TextField passwordField;
    @FXML private TitledPane infoFormID;
    @FXML private Button btnThemSua;

    private NhanVien nhanVienSelected = new NhanVien();



    public void initialize() {
        try {
            nhanVienHandle.init(nhanVienDAO, searchField, tableNhanVien, listnhanVienCurrent);
            setupTableColumnsKH();
        } catch (Exception e) {
            System.out.println(e);
        }

        initContextMenu();
    }

    public void setPhong(DVPhong dvPhong) {
        this.dvPhong = dvPhong;
    }


    private void setupTableColumnsKH() {
        colMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        colNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNgayVaoLam.setCellValueFactory(new PropertyValueFactory<>("ngayVaoLam"));
        colLuong.setCellValueFactory(new PropertyValueFactory<>("luong"));
        colChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
        colMaQL.setCellValueFactory(new PropertyValueFactory<>("maQL"));

        searchField.textProperty().addListener((obs, oldVal, newVal) -> nhanVienHandle.updateFilterPredicate());
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

        MenuItem xoaItem = new MenuItem("Xóa nhân viên");
        xoaItem.setOnAction(event -> {
            try {
                handle_Xoa();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        contextMenu.getItems().addAll(suaItem, xoaItem);

        tableNhanVien.setOnContextMenuRequested(event ->
                contextMenu.show(tableNhanVien, event.getScreenX(), event.getScreenY()));
    }

    private void setupContextKHManager() {

    }

    private void handle_Xoa() throws SQLException {
        nhanVienSelected = tableNhanVien.getSelectionModel().getSelectedItem();
        try {
            nhanVienDAO.xoaNhanVien(nhanVienSelected.getMaNV());
        }
        catch (Exception e) {
            System.out.println(e);
            showAlert(Alert.AlertType.WARNING, e.getMessage() + " : Lỗi ràng buộc nha ae");
        }

        // reload data
        nhanVienHandle.loadDataHDfromDB();
        nhanVienHandle.updateFilterPredicate();
    }

    private void handle_Sua() throws SQLException {
        infoFormID.setExpanded(true);
        btnThemSua.setText("Lưu");

        // điền vào form dữ liệu hiện tại của khách hàng
        nhanVienSelected = tableNhanVien.getSelectionModel().getSelectedItem();
        infoFormID.setText("Sửa thông tin khách hàng: " + nhanVienSelected.getMaNV());
        fullNameField.setText(nhanVienSelected.getHoTen());
        cccdField.setText(nhanVienSelected.getCccd());
        phoneField.setText(nhanVienSelected.getSdt());
        dobPicker.setValue(nhanVienSelected.getNgaySinh().toLocalDate());
        genderComboBox.setValue(nhanVienSelected.getGioiTinh());
        emailField.setText(nhanVienSelected.getEmail());
        diaChiField.setText(nhanVienSelected.getDiaChi());
        ngayVaoLamPicker.setValue(nhanVienSelected.getNgayVaoLam().toLocalDate());
        luongField.setText(String.format("%.0f", nhanVienSelected.getLuong()));
//        passwordField.setText(nhanVienSelected.getPassword());
//        chucvuComboBox.setValue(nhanVienSelected.getChucVu());

    }



    @FXML
    private void handleAddAndEdit() throws SQLException {

        if (!validateInput()) {
            return;
        }

        nhanVienSelected.setCccd(cccdField.getText());
        nhanVienSelected.setEmail(emailField.getText());
        nhanVienSelected.setGioiTinh(genderComboBox.getValue());
        nhanVienSelected.setHoTen(fullNameField.getText());
        nhanVienSelected.setNgaySinh(java.sql.Date.valueOf(dobPicker.getValue()));
        nhanVienSelected.setSdt(phoneField.getText());
        nhanVienSelected.setDiaChi("");
        nhanVienSelected.setNgayVaoLam(java.sql.Date.valueOf(ngayVaoLamPicker.getValue()));
        nhanVienSelected.setLuong(Double.parseDouble(luongField.getText().trim()));
        nhanVienSelected.setChucVu(nhanVienSelected.isManager() ? "manager" : "staff" );
        nhanVienSelected.setMaQL(maQLField.getText().trim());
        nhanVienSelected.setPassword(passwordField.getText());

        // này hơi lỏ nhưng cho nhanh )))
        if (btnThemSua.getText().equals("Thêm")) {
            nhanVienDAO.themNhanVien(nhanVienSelected);
            showStatusNoidung("Thêm thành công!");
        } else {
            nhanVienDAO.capNhatNhanVien(nhanVienSelected);
            showStatusNoidung("Sửa thành công!");
        }
        // set up ve ban dau
        clearFormRegister();
        infoFormID.setText("Thêm nhân viên");
        btnThemSua.setText("Thêm");
        // reload data
        nhanVienHandle.loadDataHDfromDB();
        nhanVienHandle.updateFilterPredicate();
    }

    @FXML
    private void handleHuy() {
        dialogStage.close();
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
        String diaChi = diaChiField.getText().trim();
        LocalDate ngayVaoLam = ngayVaoLamPicker.getValue();
        String luongText = luongField.getText().trim();
        String pass = passwordField.getText().trim();
//        String chucVu = chucvuComboBox.getValue().toString();

        if (hoTen.isEmpty() || cccd.isEmpty() || sdt.isEmpty() || dob == null || gioiTinh == null ||
                email.isEmpty() || diaChi.isEmpty() || ngayVaoLam == null || luongText.isEmpty() || pass.isEmpty()) {
            showStatusNoidung("Vui lòng nhập đầy đủ thông tin.");
            return false;
        }

        if (!cccd.matches("\\d{12}")) {
            showStatusNoidung("CCCD phải gồm đúng 12 chữ số.");
            return false;
        }

        if (!sdt.matches("0\\d{9}")) {
            showStatusNoidung("Số điện thoại phải bắt đầu bằng 0 và gồm 10 chữ số.");
            return false;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showStatusNoidung("Email không hợp lệ.");
            return false;
        }

        try {
            double luong = Double.parseDouble(luongText);
            if (luong <= 0) {
                showStatusNoidung("Lương phải là số dương.");
                return false;
            }
        } catch (NumberFormatException e) {
            showStatusNoidung("Lương phải là số hợp lệ.");
            return false;
        }

        return true;
    }


    private void clearFormRegister() {
        fullNameField.clear();
        cccdField.clear();
        phoneField.clear();
        dobPicker.setValue(null);
        genderComboBox.setValue(null);
        emailField.clear();
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


}
