package till.edu.dictionary_app.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Lớp này đại diện cho một mục từ điển (từ, phiên âm, các nghĩa)
public class DictionaryEntry {
    @SerializedName("word")
    private String word; // Từ chính
    @SerializedName("phonetics")
    private List<Phonetic> phonetics; // Danh sách các phiên âm
    @SerializedName("meanings")
    private List<Meaning> meanings; // Danh sách các nghĩa (danh từ, động từ, v.v.)

    // Constructor (không bắt buộc nếu chỉ dùng Gson, nhưng tốt để có)
    public DictionaryEntry(String word, List<Phonetic> phonetics, List<Meaning> meanings) {
        this.word = word;
        this.phonetics = phonetics;
        this.meanings = meanings;
    }

    // Getters (để truy cập dữ liệu từ các thuộc tính)
    public String getWord() {
        return word;
    }

    public List<Phonetic> getPhonetics() {
        return phonetics;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    // Lớp nội bộ đại diện cho thông tin phiên âm
    public static class Phonetic {
        @SerializedName("text")
        private String text; // Chuỗi phiên âm (ví dụ: /həˈloʊ/)
        @SerializedName("audio")
        private String audio; // URL file âm thanh phát âm

        // Constructor
        public Phonetic(String text, String audio) {
            this.text = text;
            this.audio = audio;
        }

        // Getters
        public String getText() {
            return text;
        }

        public String getAudio() {
            return audio;
        }
    }
}