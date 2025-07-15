import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class GirisEkrani extends JFrame {
    private JTextField txtKullaniciAdi;
    private JPasswordField txtSifre;
    private JCheckBox chkSifreGoster;

    public GirisEkrani() {
        setTitle("Giriş Ekranı");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 1, 10, 10));

        txtKullaniciAdi = new JTextField();
        txtSifre = new JPasswordField();
        txtSifre.setEchoChar('*');

        chkSifreGoster = new JCheckBox("Şifreyi Göster");
        chkSifreGoster.addActionListener(e -> {
            boolean secili = chkSifreGoster.isSelected();
            txtSifre.setEchoChar(secili ? (char) 0 : '*');
        });

        JButton btnGiris = new JButton("Giriş Yap");
        btnGiris.addActionListener(e -> girisYap());

        JButton btnKayit = new JButton("Yeni Hesap Oluştur");
        btnKayit.addActionListener(e -> new KayitEkrani());

        add(new JLabel("Kullanıcı Adı:"));
        add(txtKullaniciAdi);
        add(new JLabel("Şifre:"));
        add(txtSifre);
        add(chkSifreGoster);
        add(btnGiris);
        add(btnKayit);

        setVisible(true);
    }

    private void girisYap() {
        String kullaniciAdi = txtKullaniciAdi.getText().trim();
        String sifre = new String(txtSifre.getPassword());

        if (kullaniciAdi.isEmpty() || sifre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:kutuphane.db")) {
            String sql = "SELECT * FROM kullanicilar WHERE kullanici_adi = ? AND sifre = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kullaniciAdi);
            pstmt.setString(2, sifre);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int kullaniciId = rs.getInt("id");
                dispose();
                new KutuphaneApp(kullaniciId, kullaniciAdi);
            } else {
                JOptionPane.showMessageDialog(this, "Kullanıcı adı veya şifre hatalı.");
            }
        } catch (Exception ex) {
            System.out.println("Veritabanı hatası: " + ex.getMessage());
        }
    }
}
