import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class KayitEkrani extends JFrame {

    private final JTextField txtKullaniciAdi;
    private final JPasswordField txtSifre;
    private final JCheckBox chkSifreGoster;

    public KayitEkrani() {
        setTitle("Yeni Hesap Oluştur");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10));

        txtKullaniciAdi = new JTextField();
        txtSifre = new JPasswordField();
        txtSifre.setEchoChar('*');

        chkSifreGoster = new JCheckBox("Şifreyi Göster");
        chkSifreGoster.addActionListener(e ->
            txtSifre.setEchoChar(chkSifreGoster.isSelected() ? (char) 0 : '*')
        );

        JButton btnKayit = new JButton("Kaydol");
        btnKayit.addActionListener(e -> kullaniciKaydet());

        add(new JLabel("Kullanıcı Adı:"));
        add(txtKullaniciAdi);
        add(new JLabel("Şifre:"));
        add(txtSifre);
        add(chkSifreGoster);
        add(btnKayit);

        setVisible(true);
    }

    private void kullaniciKaydet() {
        String kullaniciAdi = txtKullaniciAdi.getText().trim();
        String sifre = new String(txtSifre.getPassword());

        if (kullaniciAdi.isEmpty() || sifre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:kutuphane.db")) {
            String sql = "INSERT INTO kullanicilar (kullanici_adi, sifre) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, kullaniciAdi);
                pstmt.setString(2, sifre);
                pstmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Kayıt başarılı! Giriş yapabilirsiniz.");
            dispose();
        } catch (SQLException e) {
            String mesaj = e.getMessage().contains("UNIQUE")
                ? "Bu kullanıcı adı zaten alınmış."
                : "Hata: " + e.getMessage();
            JOptionPane.showMessageDialog(this, mesaj);
        }
    }
}
