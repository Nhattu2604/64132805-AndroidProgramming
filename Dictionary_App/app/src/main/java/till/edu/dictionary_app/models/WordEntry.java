package till.edu.dictionary_app.models;

public class WordEntry {
    private int id;
    private String word;
    private String html;
    private String description;
    private String pronounce;
    private boolean isEnglishToVietnamese; // <-- DÒNG NÀY RẤT QUAN TRỌNG: TRƯỜNG MỚI ĐÃ ĐƯỢC THÊM

    // Constructor CŨ (nếu bạn vẫn muốn giữ cho các mục đích khác)
    public WordEntry(int id, String word, String html, String description, String pronounce) {
        this.id = id;
        this.word = word;
        this.html = html;
        this.description = description;
        this.pronounce = pronounce;
        this.isEnglishToVietnamese = true; // Mặc định là Anh-Việt nếu không được chỉ định
    }

    // <-- CONSTRUCTOR MỚI VỚI isEnglishToVietnamese (BẮT BUỘC CHO CHỨC NĂNG FAVORITES) -->
    public WordEntry(int id, String word, String html, String description, String pronounce, boolean isEnglishToVietnamese) {
        this.id = id;
        this.word = word;
        this.html = html;
        this.description = description;
        this.pronounce = pronounce;
        this.isEnglishToVietnamese = isEnglishToVietnamese; // <-- KHỞI TẠO TỪ ĐỐI SỐ
    }
    // <-- KẾT THÚC CONSTRUCTOR MỚI -->

    // --- Getter methods ---
    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getHtml() {
        return html;
    }

    public String getDescription() {
        return description;
    }

    public String getPronounce() {
        return pronounce;
    }

    // <-- GETTER CHO isEnglishToVietnamese (BẮT BUỘC) -->
    public boolean isEnglishToVietnamese() {
        return isEnglishToVietnamese;
    }

    // --- Setter methods (tùy chọn) ---
    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    // **LƯU Ý:** Bạn có lỗi chính tả ở đây: "voidsetHtml" phải là "setHtml"
    public void setHtml(String html) { // <-- ĐÃ SỬA LỖI CHÍNH TẢ
        this.html = html;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

    // <-- SETTER CHO isEnglishToVietnamese (tùy chọn, nhưng nên có) -->
    public void setEnglishToVietnamese(boolean englishToVietnamese) {
        isEnglishToVietnamese = englishToVietnamese;
    }
}