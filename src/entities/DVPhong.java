package entities;

public class DVPhong {
    private String maDVP;
    private String loaiPhong;
    private String moTa;
    private double donGia;
    private String trangThai;

    public DVPhong() {}

    public DVPhong(String maDVP, String loaiPhong, String moTa, double donGia, String trangThai) {
        this.maDVP = maDVP;
        this.loaiPhong = loaiPhong;
        this.moTa = moTa;
        this.donGia = donGia;
        this.trangThai = trangThai;
    }

    public String getMaDVP() {
        return maDVP;
    }

    public void setMaDVP(String maDVP) {
        this.maDVP = maDVP;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
