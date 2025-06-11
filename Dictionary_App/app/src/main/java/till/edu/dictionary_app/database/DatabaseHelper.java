package till.edu.dictionary_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import till.edu.dictionary_app.models.WordEntry;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static String DB_NAME = "dict_hh.db"; // Tên database của bạn
    private static String DB_PATH = "";
    private final Context mContext;
    private SQLiteDatabase myDatabase;

    // --- KHAI BÁO TÊN BẢNG VÀ CỘT MỚI CHO FAVORITES ---
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COL_FAV_WORD = "word";
    private static final String COL_FAV_IS_ENG_TO_VIET = "is_english_to_vietnamese"; // Để biết từ đó là Anh-Việt hay Việt-Anh
    // --- KẾT THÚC KHAI BÁO MỚI ---


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/"; // Đường dẫn tới database
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLException e) {
            Log.e(TAG, "Database does not exist: " + e.getMessage());
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDatabase() throws IOException {
        InputStream myInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.d(TAG, "Database copied successfully.");
    }

    public void openDatabase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        // Đảm bảo bảng favorites được tạo sau khi mở database
        createFavoritesTableIfNotExist(myDatabase);
    }

    private void createFavoritesTableIfNotExist(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITES + " ("
                + COL_FAV_WORD + " TEXT PRIMARY KEY,"
                + COL_FAV_IS_ENG_TO_VIET + " INTEGER DEFAULT 1)";
        try {
            db.execSQL(CREATE_FAVORITES_TABLE);
            Log.d(TAG, "Favorites table checked/created.");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating favorites table: " + e.getMessage());
        }
    }

    @Override
    public synchronized void close() {
        if (myDatabase != null) {
            myDatabase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate chỉ được gọi khi database được tạo lần đầu bởi SQLiteOpenHelper.
        // Chúng ta đảm bảo bảng favorites được tạo ở đây.
        createFavoritesTableIfNotExist(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Database onUpgrade called. Old version: " + oldVersion + ", New version: " + newVersion);
    }


    // Hàm tra từ Anh-Việt
    public WordEntry getEnglishToVietnameseWord(String word) {
        WordEntry entry = null;
        Cursor cursor = null;
        try {
            if (myDatabase == null || !myDatabase.isOpen()) {
                openDatabase();
            }
            // THÊM COLLATE NOCASE
            String query = "SELECT word, description, html, pronounce FROM av WHERE word = ? COLLATE NOCASE";
            cursor = myDatabase.rawQuery(query, new String[]{word});

            if (cursor != null && cursor.moveToFirst()) {
                String fetchedWord = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String html = cursor.getString(cursor.getColumnIndexOrThrow("html"));
                String pronounce = cursor.getString(cursor.getColumnIndexOrThrow("pronounce"));
                entry = new WordEntry(0, fetchedWord, html, description, pronounce);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting English to Vietnamese word: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return entry;
    }

    // Hàm tra từ Việt-Anh
    public WordEntry getVietnameseToEnglishWord(String word) {
        WordEntry entry = null;
        Cursor cursor = null;
        try {
            if (myDatabase == null || !myDatabase.isOpen()) {
                openDatabase();
            }
            // THÊM COLLATE NOCASE
            String query = "SELECT word, description, html, pronounce FROM va WHERE word = ? COLLATE NOCASE";
            cursor = myDatabase.rawQuery(query, new String[]{word});

            if (cursor != null && cursor.moveToFirst()) {
                String fetchedWord = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String html = cursor.getString(cursor.getColumnIndexOrThrow("html"));
                String pronounce = cursor.getString(cursor.getColumnIndexOrThrow("pronounce"));
                entry = new WordEntry(0, fetchedWord, html, description, pronounce);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting Vietnamese to English word: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return entry;
    }

    // Hàm lấy gợi ý tiếng Anh
    public List<WordEntry> getEnglishSuggestions(String prefix) {
        List<WordEntry> suggestions = new ArrayList<>();
        Cursor cursor = null;
        try {
            if (myDatabase == null || !myDatabase.isOpen()) {
                openDatabase();
            }
            // THÊM COLLATE NOCASE VÀ SỬ DỤNG prefix + "%" ĐỂ GỢI Ý BẮT ĐẦU BẰNG
            String query = "SELECT word, description, html, pronounce FROM av WHERE word LIKE ? COLLATE NOCASE ORDER BY word ASC LIMIT 20";
            cursor = myDatabase.rawQuery(query, new String[]{prefix + "%"}); // Đã sửa từ "%" + prefix + "%" SANG prefix + "%"
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String html = cursor.getString(cursor.getColumnIndexOrThrow("html"));
                    String pronounce = cursor.getString(cursor.getColumnIndexOrThrow("pronounce"));
                    suggestions.add(new WordEntry(0, word, html, description, pronounce));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting English suggestions: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return suggestions;
    }

    // Hàm lấy gợi ý tiếng Việt
    public List<WordEntry> getVietnameseSuggestions(String prefix) {
        List<WordEntry> suggestions = new ArrayList<>();
        Cursor cursor = null;
        try {
            if (myDatabase == null || !myDatabase.isOpen()) {
                openDatabase();
            }
            // THÊM COLLATE NOCASE VÀ SỬ DỤNG prefix + "%" ĐỂ GỢI Ý BẮT ĐẦU BẰNG
            String query = "SELECT word, description, html, pronounce FROM va WHERE word LIKE ? COLLATE NOCASE ORDER BY word ASC LIMIT 20";
            cursor = myDatabase.rawQuery(query, new String[]{prefix + "%"}); // Đã sửa từ "%" + prefix + "%" SANG prefix + "%"
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String html = cursor.getString(cursor.getColumnIndexOrThrow("html"));
                    String pronounce = cursor.getString(cursor.getColumnIndexOrThrow("pronounce"));
                    suggestions.add(new WordEntry(0, word, html, description, pronounce));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting Vietnamese suggestions: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return suggestions;
    }

    // Lọc gợi ý mặc định
    public List<WordEntry> getDefaultSuggestions(boolean isEngToViet) {
        List<WordEntry> suggestions = new ArrayList<>();
        Cursor cursor = null;
        try {
            if (myDatabase == null || !myDatabase.isOpen()) {
                openDatabase();
            }
            String tableName = isEngToViet ? "av" : "va";
            String query;

            // THÊM COLLATE NOCASE
            query = "SELECT word, description, html, pronounce FROM " + tableName +
                    " WHERE LENGTH(word) > 1 " +
                    " AND word NOT LIKE '''%' " +
                    " AND word NOT GLOB '*[^a-zA-ZáàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵĐđ]*' " +
                    " ORDER BY word ASC LIMIT 20 COLLATE NOCASE"; // Đã thêm COLLATE NOCASE

            cursor = myDatabase.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String html = cursor.getString(cursor.getColumnIndexOrThrow("html"));
                    String pronounce = cursor.getString(cursor.getColumnIndexOrThrow("pronounce"));
                    suggestions.add(new WordEntry(0, word, html, description, pronounce));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting default suggestions: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return suggestions;
    }

    // --- CÁC PHƯƠNG THỨC CHO TÍNH NĂNG YÊU THÍCH ---

    // 1. Thêm từ vào danh sách yêu thích
    public boolean addFavorite(String word, boolean isEnglishToVietnamese) {
        if (myDatabase == null || !myDatabase.isOpen()) {
            try {
                openDatabase();
            } catch (SQLException e) {
                Log.e(TAG, "Cannot open database to add favorite: " + e.getMessage());
                return false;
            }
        }

        ContentValues values = new ContentValues();
        values.put(COL_FAV_WORD, word);
        values.put(COL_FAV_IS_ENG_TO_VIET, isEnglishToVietnamese ? 1 : 0);

        long result = myDatabase.insertWithOnConflict(TABLE_FAVORITES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (result != -1) {
            Log.d(TAG, "Added/Updated favorite: " + word);
            return true;
        } else {
            Log.e(TAG, "Failed to add/update favorite: " + word);
            return false;
        }
    }

    // 2. Xóa từ khỏi danh sách yêu thích
    public boolean removeFavorite(String word) {
        if (myDatabase == null || !myDatabase.isOpen()) {
            try {
                openDatabase();
            } catch (SQLException e) {
                Log.e(TAG, "Cannot open database to remove favorite: " + e.getMessage());
                return false;
            }
        }

        int rowsAffected = myDatabase.delete(TABLE_FAVORITES, COL_FAV_WORD + " = ?", new String[]{word});
        if (rowsAffected > 0) {
            Log.d(TAG, "Removed favorite: " + word);
            return true;
        } else {
            Log.d(TAG, "Favorite not found to remove: " + word);
            return false;
        }
    }

    // 3. Kiểm tra xem một từ có phải là yêu thích hay không
    public boolean isFavorite(String word) {
        if (myDatabase == null || !myDatabase.isOpen()) {
            try {
                openDatabase();
            } catch (SQLException e) {
                Log.e(TAG, "Cannot open database to check favorite: " + e.getMessage());
                return false;
            }
        }

        Cursor cursor = null;
        boolean isFav = false;
        try {
            // THÊM COLLATE NOCASE vào đây để đảm bảo khớp chính xác ký tự có dấu
            String query = "SELECT 1 FROM " + TABLE_FAVORITES + " WHERE " + COL_FAV_WORD + " = ? COLLATE NOCASE";
            cursor = myDatabase.rawQuery(query, new String[]{word});
            if (cursor != null && cursor.moveToFirst()) {
                isFav = true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking favorite status: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isFav;
    }

    // 4. Lấy tất cả các từ yêu thích
    public List<WordEntry> getAllFavorites() {
        List<WordEntry> favoriteWords = new ArrayList<>();
        Cursor cursor = null;
        try {
            if (myDatabase == null || !myDatabase.isOpen()) {
                openDatabase();
            }
            // THÊM COLLATE NOCASE vào ORDER BY nếu muốn sắp xếp tiếng Việt có dấu đúng
            String query = "SELECT " + COL_FAV_WORD + ", " + COL_FAV_IS_ENG_TO_VIET + " FROM " + TABLE_FAVORITES + " ORDER BY " + COL_FAV_WORD + " COLLATE NOCASE ASC";
            cursor = myDatabase.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String word = cursor.getString(cursor.getColumnIndexOrThrow(COL_FAV_WORD));
                    int isEngToVietInt = cursor.getInt(cursor.getColumnIndexOrThrow(COL_FAV_IS_ENG_TO_VIET));
                    boolean isEngToViet = (isEngToVietInt == 1);

                    WordEntry wordDetail = null;
                    if (isEngToViet) {
                        // Truy vấn từ chi tiết, đảm bảo hàm getEnglishToVietnameseWord cũng có COLLATE NOCASE
                        wordDetail = getEnglishToVietnameseWord(word);
                    } else {
                        // Truy vấn từ chi tiết, đảm bảo hàm getVietnameseToEnglishWord cũng có COLLATE NOCASE
                        wordDetail = getVietnameseToEnglishWord(word);
                    }

                    if (wordDetail != null) {
                        favoriteWords.add(wordDetail);
                    } else {
                        favoriteWords.add(new WordEntry(0, word, "", "", ""));
                    }

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all favorite words: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return favoriteWords;
    }

    // --- KẾT THÚC CÁC PHƯƠNG THỨC MỚI CHO TÍNH NĂNG YÊU THÍCH ---
}