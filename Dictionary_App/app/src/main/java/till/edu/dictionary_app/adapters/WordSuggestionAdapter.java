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

    private Context mContext;
    private int mResource;

    public WordSuggestionAdapter(@NonNull Context context, int resource, @NonNull List<WordEntry> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Lấy đối tượng WordEntry cho vị trí hiện tại
        WordEntry entry = getItem(position);

        // Kiểm tra nếu convertView đã được sử dụng lại, nếu không thì inflate layout mới
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        // Ánh xạ các TextView từ layout tùy chỉnh
        TextView tvWord = convertView.findViewById(R.id.tv_suggestion_word);
        TextView tvMeaningPreview = convertView.findViewById(R.id.tv_suggestion_meaning_preview);

        // Đặt dữ liệu vào các TextView
        if (entry != null) {
            tvWord.setText(entry.getWord());

            // Hiển thị preview nghĩa, ưu tiên HTML nếu có, nếu không thì description
            String previewText = "";
            if (entry.getHtml() != null && !entry.getHtml().isEmpty()) {
                // Cố gắng lấy text thuần từ HTML cho preview
                // Sử dụng FROM_HTML_MODE_COMPACT cho API 24+
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    previewText = Html.fromHtml(entry.getHtml(), Html.FROM_HTML_MODE_COMPACT).toString();
                } else {
                    // API cũ hơn
                    previewText = Html.fromHtml(entry.getHtml()).toString();
                }
            } else if (entry.getDescription() != null && !entry.getDescription().isEmpty()) {
                previewText = entry.getDescription();
            }
            // Giới hạn độ dài preview để tránh tràn màn hình (ví dụ 100 ký tự)
            if (previewText.length() > 100) {
                previewText = previewText.substring(0, 100) + "...";
            }
            tvMeaningPreview.setText(previewText.trim()); // trim() để loại bỏ khoảng trắng đầu/cuối
        }

        return convertView;
    }
}