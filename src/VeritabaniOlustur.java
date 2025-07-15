import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class VeritabaniOlustur {

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:kutuphane.db")) {
            Statement stmt = conn.createStatement();

            // Kullanıcılar tablosu
            String kullaniciTablosu = """
                CREATE TABLE IF NOT EXISTS kullanicilar (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    kullanici_adi TEXT UNIQUE NOT NULL,
                    sifre TEXT NOT NULL
                );
            """;
            stmt.execute(kullaniciTablosu);

            // Kitaplar tablosu
            String kitapTablosu = """
                CREATE TABLE IF NOT EXISTS kitaplar (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    kitap_adi TEXT NOT NULL,
                    yazar TEXT NOT NULL,
                    tur TEXT,
                    kullanici_id INTEGER NOT NULL,
                    FOREIGN KEY(kullanici_id) REFERENCES kullanicilar(id)
                );
            """;
            stmt.execute(kitapTablosu);

            // Örnek kullanıcı ekle
            String ornekKullanici = """
                INSERT OR IGNORE INTO kullanicilar (kullanici_adi, sifre)
                VALUES ('melik', '1234');
            """;
            stmt.execute(ornekKullanici);

            System.out.println("Veritabanı ve tablolar oluşturuldu.");
        } catch (SQLException e) {
            System.out.println("Hata oluştu: " + e.getMessage());
        }
    }
}