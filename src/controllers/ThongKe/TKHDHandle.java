package controllers.ThongKe;

import dao.HoaDonDAO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.sql.SQLException;
import java.util.Map;

public class TKHDHandle {

    @FXML private BarChart<String, Number> invoiceBarChart;
    @FXML private PieChart invoicePieChart;
    @FXML private LineChart<String, Number> invoiceLineChart;

    @FXML private ComboBox<String> invoiceChartType;
    @FXML private ComboBox<String> invoiceTimeRange;

    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    StringProperty Condition = new SimpleStringProperty();

    @FXML
    public void initialize() throws SQLException {
        setupViewCBB();
        ishideBD(true, false, false);
    }

    private void showChartMonthlyProfit() throws SQLException {
        if (!invoiceTimeRange.getValue().equals("Năm này")) {
            showInfo("Biểu đồ này xem theo năm");
            invoiceTimeRange.setValue("Năm này");
            Platform.runLater(() -> { setCondition();});
        }
            // Xóa dữ liệu cũ
        invoiceBarChart.getData().clear();

        // Lấy dữ liệu thống kê từ DAO

        Map<String, Integer> thongKe = hoaDonDAO.tkMonthlyProfit(Condition.getValue());

        // Tạo series mới
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Thêm dữ liệu vào series
        for (Map.Entry<String, Integer> entry : thongKe.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            series.getData().add(data);
        }

        // Thêm series vào biểu đồ
        invoiceBarChart.getData().add(series);
        // hide
        ishideBD(true, false, false);
    }

    public void showInvoicePieChart() throws SQLException {
        Map<String, Integer> thongKe = hoaDonDAO.tkProfitRoom(Condition.getValue());

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : thongKe.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " - " + entry.getValue(), entry.getValue()));
        }

        invoicePieChart.setData(pieChartData);
        invoicePieChart.setTitle("Tỷ lệ các loại phòng");

        ishideBD(false, true, false);
    }

    public void showInvoiceLineChart() throws SQLException {
        invoiceLineChart.getData().clear();
        Map<Integer, Integer> thongKe = hoaDonDAO.tkProfitLine(Condition.getValue());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<Integer, Integer> entry : thongKe.entrySet()) {
            String xLabel =""+ entry.getKey();
            series.getData().add(new XYChart.Data<>(xLabel, entry.getValue()));
        }

        invoiceLineChart.getData().add(series);
        ishideBD(false, false, true);
    }

    private void setupViewCBB() throws SQLException {
        // set up mặc định ban đầu
        invoiceChartType.getSelectionModel().select("Doanh thu theo tháng");
        invoiceTimeRange.getSelectionModel().select("Năm này");
        setCondition();
        showChartMonthlyProfit();
        // set up view theo chế độ xem
        invoiceChartType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadDBtoChart(newVal);
        });
        invoiceTimeRange.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadDBtoChart(invoiceChartType.getValue());
            }
        });
    }
    private void ishideBD(Boolean s1, Boolean s2, Boolean s3) {
        invoiceBarChart.setVisible(s1); invoiceBarChart.setManaged(s1);
        invoicePieChart.setVisible(s2); invoicePieChart.setManaged(s2);
        invoiceLineChart.setVisible(s3); invoiceLineChart.setManaged(s3);
    }

    private void setCondition() {
        String loaiBD = invoiceChartType.getValue();
        String condition = invoiceTimeRange.getValue();
        if (condition != null && !condition.isEmpty()) {
            if (condition.equals("Toàn thời gian")) {
                Condition.setValue("");
                return;
            }
            if (loaiBD.equals("Tỉ trọng doanh thu theo loại phòng")) {
                Condition.setValue("AND ");
            }
            else Condition.setValue("WHERE ");

            if (condition.equals("Tháng này")) {
                Condition.setValue(Condition.getValue() + " EXTRACT(MONTH FROM hd.NGAYTHANHTOAN) = EXTRACT(MONTH FROM SYSDATE) AND EXTRACT(YEAR FROM hd.NGAYTHANHTOAN) = EXTRACT(YEAR FROM SYSDATE)");
            }
            if (condition.equals("Năm này")) {
                Condition.setValue(Condition.getValue() + " EXTRACT(YEAR FROM hd.NGAYTHANHTOAN) = EXTRACT(YEAR FROM SYSDATE)");
            }
        }
    }
    private void loadDBtoChart(String newVal) {
        if (newVal != null) {
            switch (newVal) {
                case "Doanh thu theo tháng trong năm":
                    try {
                        setCondition();
                        showChartMonthlyProfit();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "Tỉ trọng doanh thu theo loại phòng":
                    try {
                        setCondition();
                        showInvoicePieChart();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "Xu hướng doanh thu":
                    try {
                        setCondition();
                        showInvoiceLineChart();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }

        }

    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
