
public class CT {
 public static void main(String[] args) {
	 SanPham sp1 = new SanPham();
	 SanPham sp2 = new SanPham(2,"Bánh Chuối","Đồ ăn nhanh","bc.jpg");
	 sp1.setMaSP(1);
	 sp1.setTenSP("Bia SG");
	 sp1.setAnhSP("sg.png");
	 String thongtinSP1 = sp1.toString();
	 String thongtinSP2 = "Mã sản phẩm là " + sp2.getMaSP();
	 		thongtinSP2 +=" Tên SP2 là " + sp2.getTenSP();
	 System.out.println(thongtinSP1);
	 System.out.println(thongtinSP2);
 }
 
}
