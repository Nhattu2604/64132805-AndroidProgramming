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

public class FavoriteWordAdapter extends RecyclerView.Adapter<FavoriteWordAdapter.WordViewHolder> {

    private final Context context;
    private final List<WordEntry> wordList;
    private OnItemClickListener listener;

    // Constructor cho FavoriteWordAdapter
    public FavoriteWordAdapter(Context context, List<WordEntry> wordList) {
        this.context = context;
        this.wordList = wordList;
    }

    public interface OnItemClickListener {
        void onItemClick(WordEntry wordEntry);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng item_word_suggestion.xml làm layout cho mỗi item
        // Đảm bảo R.layout.item_word_suggestion tồn tại và có tvWord, tvDescription
        View view = LayoutInflater.from(context).inflate(R.layout.item_word_suggestion, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordEntry currentWord = wordList.get(position);
        holder.tvWord.setText(currentWord.getWord());

        // Hiển thị phần mô tả. Nếu là HTML, sử dụng Html.fromHtml
        String description = currentWord.getDescription();
        if (description != null && !description.isEmpty()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.tvDescription.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.tvDescription.setText(Html.fromHtml(description));
            }
        } else {
            holder.tvDescription.setText("Không có mô tả");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentWord);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord;
        TextView tvDescription;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tv_suggestion_word); // <-- Đảm bảo ID này đúng với layout item_word_suggestion
            tvDescription = itemView.findViewById(R.id.tv_suggestion_meaning_preview); // <-- Đảm bảo ID này đúng
        }
    }

    // Phương thức để cập nhật dữ liệu khi danh sách yêu thích thay đổi
    public void updateData(List<WordEntry> newWordList) {
        wordList.clear();
        wordList.addAll(newWordList);
        notifyDataSetChanged();
    }
}