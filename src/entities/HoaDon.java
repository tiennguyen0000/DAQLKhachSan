package entities;

import java.sql.Date;

public class HoaDon {
    private String maHD;
    private String maNV;
    private String maKH;
    private Date ngayTao;
    private Date ngayThanhToan;
    private double tongTien;
    private String tinhTrangTT;
    private Date ngayBDSD;
    private Date ngayKTSD;
    private int soNgaySuDung;
    private String note;
    private String maDVP;

    public HoaDon() {}

    public HoaDon(String maHD, String maNV, String maKH,
                  Date ngayTao, Date ngayThanhToan, double tongTien, String tinhTrangTT,
                  Date ngayBDSD, Date ngayKTSD, int soNgaySuDung, String note, String maDVP) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.ngayTao = ngayTao;
        this.ngayThanhToan = ngayThanhToan;
        this.tongTien = tongTien;
        this.tinhTrangTT = tinhTrangTT;
        this.ngayBDSD = ngayBDSD;
        this.ngayKTSD = ngayKTSD;
        this.soNgaySuDung = soNgaySuDung;
        this.note = note;
        this.maDVP = maDVP;
    }

    public HoaDon(String maNV, String maKH, Date ngayTao, double tongTien, String tinhTrangTT, Date ngayBDSD, Date ngayKTSD, String note, String maDVP) {
        this.maNV = maNV;
        this.maKH = maKH;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
        this.tinhTrangTT = tinhTrangTT;
        this.ngayBDSD = ngayBDSD;
        this.ngayKTSD = ngayKTSD;
        this.note = note;
        this.maDVP = maDVP;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }


    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Date getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(Date ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTinhTrangTT() {
        return tinhTrangTT;
    }

    public void setTinhTrangTT(String tinhTrangTT) {
        this.tinhTrangTT = tinhTrangTT;
    }

    public Date getNgayBDSD() {
        return ngayBDSD;
    }

    public void setNgayBDSD(Date ngayBDSD) {
        this.ngayBDSD = ngayBDSD;
    }

    public Date getNgayKTSD() {
        return ngayKTSD;
    }

    public void setNgayKTSD(Date ngayKTSD) {
        this.ngayKTSD = ngayKTSD;
    }

    public int getSoNgaySuDung() {
        return soNgaySuDung;
    }

    public void setSoNgaySuDung(int soNgaySuDung) {
        this.soNgaySuDung = soNgaySuDung;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMaDVP() {
        return maDVP;
    }

    public void setMaDVP(String maDVP) {
        this.maDVP = maDVP;
    }
}
