package entities;

public class NhanVien {
    private String maNV;
    private String hoTen;
    private String password;
    private String cccd;
    private String sdt;
    private java.sql.Date ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String email;
    private java.sql.Date ngayVaoLam;
    private double luong;
    private String chucVu;
    private String maQL; // Mã quản lý - khóa ngoại đến chính bảng này


    public NhanVien() {}

    public NhanVien(String maNV, String hoTen, String password, String cccd, String sdt,
                    java.sql.Date ngaySinh, String gioiTinh, String diaChi, String email,
                    java.sql.Date ngayVaoLam, double luong, String chucVu, String maQL) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.password = password;
        this.cccd = cccd;
        this.sdt = sdt;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.email = email;
        this.ngayVaoLam = ngayVaoLam;
        this.luong = luong;
        this.chucVu = chucVu;
        this.maQL = maQL;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public java.sql.Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(java.sql.Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) {
        this.luong = luong;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getMaQL() {
        return maQL;
    }

    public void setMaQL(String maQL) {
        this.maQL = maQL;
    }

    public Boolean isManager() {
        return this.maNV == "";
    }
}
