package till.edu.dictionary_app.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import till.edu.dictionary_app.R; // Đảm bảo import R
import till.edu.dictionary_app.models.WordEntry;

public class FavoriteWordAdapter extends RecyclerView.Adapter<FavoriteWordAdapter.KhungHienThiTu> { // WordViewHolder -> KhungHienThiTu

    // --- Biến đã được đổi tên ---
    private final Context nguCanh; // context -> nguCanh
    private final List<WordEntry> danhSachTu; // wordList -> danhSachTu
    private OnItemClickListener boLangNgheItem; // listener -> boLangNgheItem

    // Constructor cho FavoriteWordAdapter (đã đổi tên biến)
    public FavoriteWordAdapter(Context nguCanh, List<WordEntry> danhSachTu) {
        this.nguCanh = nguCanh;
        this.danhSachTu = danhSachTu;
    }

    public interface OnItemClickListener {
        void onItemClick(WordEntry mucTu); // wordEntry -> mucTu (nhất quán với WordDetailActivity và FavoritesActivity)
    }

    // --- Hàm đã được đổi tên ---
    public void setOnItemClickListener(OnItemClickListener boLangNghe) { // setOnItemClickListener -> datBoLangNgheItem
        this.boLangNgheItem = boLangNghe; // listener -> boLangNgheItem
    }

    @NonNull
    @Override
    public KhungHienThiTu onCreateViewHolder(@NonNull ViewGroup phuHuynh, int kieuXem) { // WordViewHolder -> KhungHienThiTu, parent -> phuHuynh, viewType -> kieuXem
        // Sử dụng item_word_suggestion.xml làm layout cho mỗi item
        // Đảm bảo R.layout.item_word_suggestion tồn tại và có tvWord, tvDescription
        View view = LayoutInflater.from(nguCanh).inflate(R.layout.item_word_suggestion, phuHuynh, false); // context -> nguCanh
        return new KhungHienThiTu(view); // WordViewHolder -> KhungHienThiTu
    }

    @Override
    public void onBindViewHolder(@NonNull KhungHienThiTu khungHienThi, int viTri) { // WordViewHolder -> KhungHienThiTu, holder -> khungHienThi, position -> viTri
        WordEntry tuHienTai = danhSachTu.get(viTri); // currentWord -> tuHienTai, wordList -> danhSachTu
        khungHienThi.tvTuHienThi.setText(tuHienTai.getWord()); // tvWord -> tvTuHienThi

        // Hiển thị phần mô tả. Nếu là HTML, sử dụng Html.fromHtml (đã đổi tên biến)
        String moTa = tuHienTai.getDescription(); // description -> moTa
        if (moTa != null && !moTa.isEmpty()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                khungHienThi.tvMoTa.setText(Html.fromHtml(moTa, Html.FROM_HTML_MODE_LEGACY)); // tvDescription -> tvMoTa, moTa thay cho description
            } else {
                khungHienThi.tvMoTa.setText(Html.fromHtml(moTa)); // tvDescription -> tvMoTa, moTa thay cho description
            }
        } else {
            khungHienThi.tvMoTa.setText("Không có mô tả"); // tvDescription -> tvMoTa
        }

        khungHienThi.itemView.setOnClickListener(v -> {
            if (boLangNgheItem != null) { // listener -> boLangNgheItem
                boLangNgheItem.onItemClick(tuHienTai); // tuHienTai thay cho currentWord
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachTu.size(); // wordList -> danhSachTu
    }
    public static class KhungHienThiTu extends RecyclerView.ViewHolder { // WordViewHolder -> KhungHienThiTu
        TextView tvTuHienThi; // tvWord -> tvTuHienThi
        TextView tvMoTa; // tvDescription -> tvMoTa

        public KhungHienThiTu(@NonNull View itemView) {
            super(itemView);
            tvTuHienThi = itemView.findViewById(R.id.tv_suggestion_word); // <-- Đảm bảo ID này đúng với layout item_word_suggestion
            tvMoTa = itemView.findViewById(R.id.tv_suggestion_meaning_preview); // <-- Đảm bảo ID này đúng
        }
    }

    // Phương thức để cập nhật dữ liệu khi danh sách yêu thích thay đổi
    public void capNhatDuLieu(List<WordEntry> danhSachTuMoi) { // updateData -> capNhatDuLieu, newWordList -> danhSachTuMoi
        danhSachTu.clear(); // wordList -> danhSachTu
        danhSachTu.addAll(danhSachTuMoi); // wordList -> danhSachTu, danhSachTuMoi thay cho newWordList
        notifyDataSetChanged();
    }
}