package entities;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private String cccd;
    private String sdt;
    private java.sql.Date ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String email;

    public KhachHang() {}

    public KhachHang(String maKH, String hoTen, String cccd, String sdt, java.sql.Date ngaySinh,
                     String gioiTinh, String diaChi, String email) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.cccd = cccd;
        this.sdt = sdt;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.email = email;
    }
    public KhachHang(String hoTen, String cccd, String sdt, java.sql.Date ngaySinh, String gioiTinh, String email) {
        this.hoTen = hoTen;
        this.cccd = cccd;
        this.sdt = sdt;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.email = email;
    }



    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public java.sql.Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(java.sql.Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
