import java.util.ArrayList;

public class CT {
 public static void main(String[] args) {
	// khai báo danh sách sản phẩm 
	 ArrayList<SanPham> dsSanPham;
	 // Xin Mới
	 dsSanPham = new ArrayList<SanPham>();
	 // nhập 3 sản phẩm 
	 SanPham sp1 = new SanPham(1,"Bánh Chuối","Đồ ăn nhanh","bc1.jpg");
	 SanPham sp2 = new SanPham(2,"Bánh Quy","Đồ ăn nhanh","bc2.jpg");
	 SanPham sp3 = new SanPham(3,"Bánh Pizza","Đồ ăn nhanh","bc3.jpg");
	 dsSanPham.add(sp1);
	 dsSanPham.add(sp2);
	 dsSanPham.add(sp3);
	 
//	 for (SanPham x: dsSanPham)
//		 System.out.println(x.toString());
	 
	 for (int i = 0; i<dsSanPham.size(); i++) {
		
		 
			SanPham sp = dsSanPham.get(i);
			System.out.println(dsSanPham.get(i));
			 
	 }
 }
 
}
