package till.edu.dictionary_app.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Lớp này đại diện cho một nghĩa của từ (danh từ, động từ, v.v.)
public class Meaning {
    @SerializedName("partOfSpeech")
    private String partOfSpeech; // Loại từ (ví dụ: noun, verb, interjection)
    @SerializedName("definitions")
    private List<Definition> definitions; // Danh sách các định nghĩa

    // Constructor
    public Meaning(String partOfSpeech, List<Definition> definitions) {
        this.partOfSpeech = partOfSpeech;
        this.definitions = definitions;
    }

    // Getters
    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    // Lớp nội bộ đại diện cho một định nghĩa cụ thể
    public static class Definition {
        @SerializedName("definition")
        private String definition; // Nội dung định nghĩa
        @SerializedName("example")
        private String example; // Ví dụ minh họa

        // Constructor
        public Definition(String definition, String example) {
            this.definition = definition;
            this.example = example;
        }

        // Getters
        public String getDefinition() {
            return definition;
        }

        public String getExample() {
            return example;
        }
    }
}