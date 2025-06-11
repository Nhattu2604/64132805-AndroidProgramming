package till.edu.dictionary_app.adapters; // Đảm bảo package này đúng

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import till.edu.dictionary_app.models.WordEntry; // Import WordEntry của bạn

// Đảm bảo R là của ứng dụng của bạn, không phải android.R
import till.edu.dictionary_app.R;

public class WordSuggestionAdapter extends ArrayAdapter<WordEntry> {

    // --- Biến đã được đổi tên ---
    private Context nguCanhUngDung; // mContext -> nguCanhUngDung
    private int idBoCuc; // mResource -> idBoCuc

    // Constructor
    public WordSuggestionAdapter(@NonNull Context nguCanh, int taiNguyen, @NonNull List<WordEntry> doiTuong) { // context -> nguCanh, resource -> taiNguyen, objects -> doiTuong
        super(nguCanh, taiNguyen, doiTuong);
        this.nguCanhUngDung = nguCanh; // mContext -> nguCanhUngDung
        this.idBoCuc = taiNguyen; // mResource -> idBoCuc
    }

    @NonNull
    @Override
    public View getView(int viTri, @Nullable View khungChuyenDoi, @NonNull ViewGroup phuHuynh) { // position -> viTri, convertView -> khungChuyenDoi, parent -> phuHuynh
        // Lấy đối tượng WordEntry cho vị trí hiện tại
        WordEntry mucTu = getItem(viTri); // entry -> mucTu

        // Kiểm tra nếu convertView đã được sử dụng lại, nếu không thì inflate layout mới
        if (khungChuyenDoi == null) {
            khungChuyenDoi = LayoutInflater.from(nguCanhUngDung).inflate(idBoCuc, phuHuynh, false); // nguCanhUngDung thay cho mContext, idBoCuc thay cho mResource
        }

        // Ánh xạ các TextView từ layout tùy chỉnh
        TextView tvTuGoiY = khungChuyenDoi.findViewById(R.id.tv_suggestion_word); // tvWord -> tvTuGoiY
        TextView tvXemTruocNghia = khungChuyenDoi.findViewById(R.id.tv_suggestion_meaning_preview); // tvMeaningPreview -> tvXemTruocNghia

        // Đặt dữ liệu vào các TextView
        if (mucTu != null) { // mucTu thay cho entry
            tvTuGoiY.setText(mucTu.getWord()); // tvTuGoiY thay cho tvWord

            // Hiển thị preview nghĩa, ưu tiên HTML nếu có, nếu không thì description
            String vanBanXemTruoc = ""; // previewText -> vanBanXemTruoc
            if (mucTu.getHtml() != null && !mucTu.getHtml().isEmpty()) { // mucTu thay cho entry
                // Cố gắng lấy text thuần từ HTML cho preview
                // Sử dụng FROM_HTML_MODE_COMPACT cho API 24+
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    vanBanXemTruoc = Html.fromHtml(mucTu.getHtml(), Html.FROM_HTML_MODE_COMPACT).toString(); // mucTu thay cho entry
                } else {
                    // API cũ hơn
                    vanBanXemTruoc = Html.fromHtml(mucTu.getHtml()).toString(); // mucTu thay cho entry
                }
            } else if (mucTu.getDescription() != null && !mucTu.getDescription().isEmpty()) { // mucTu thay cho entry
                vanBanXemTruoc = mucTu.getDescription();
            }
            // Giới hạn độ dài preview để tránh tràn màn hình (ví dụ 100 ký tự)
            if (vanBanXemTruoc.length() > 100) {
                vanBanXemTruoc = vanBanXemTruoc.substring(0, 100) + "...";
            }
            tvXemTruocNghia.setText(vanBanXemTruoc.trim()); // tvXemTruocNghia thay cho tvMeaningPreview, vanBanXemTruoc thay cho previewText
        }

        return khungChuyenDoi; // khungChuyenDoi thay cho convertView
    }
}
