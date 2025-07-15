import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class KutuphaneApp extends JFrame {
    private int kullaniciId;
    private JTable kitapTablosu;
    private DefaultTableModel tabloModeli;

    private JTextArea sohbetAlani;
    private JTextField girisAlani;

    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "deepseek/deepseek-chat";

    public KutuphaneApp(int kullaniciId, String kullaniciAdi) {
        this.kullaniciId = kullaniciId;

        setTitle("📚 " + kullaniciAdi + " Kütüphanesi");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        kitapTablosu = new JTable();
        tabloModeli = new DefaultTableModel(new String[]{"ID", "Kitap Adı", "Yazar", "Tür"}, 0);
        kitapTablosu.setModel(tabloModeli);
        add(new JScrollPane(kitapTablosu), BorderLayout.NORTH);

        JPanel butonPanel = new JPanel();
        JButton btnEkle = new JButton("Kitap Ekle");
        JButton btnSil = new JButton("Kitap Sil");
        JButton btnGuncelle = new JButton("Kitap Güncelle");
        JButton btnDisariAktar = new JButton("Dışa Aktar");
        JButton btnTema = new JButton("🌗 Tema Değiştir");

        butonPanel.add(btnEkle);
        butonPanel.add(btnSil);
        butonPanel.add(btnGuncelle);
        butonPanel.add(btnDisariAktar);
        butonPanel.add(btnTema);

        add(butonPanel, BorderLayout.CENTER);

        sohbetAlani = new JTextArea(10, 50);
        sohbetAlani.setEditable(false);
        sohbetAlani.setLineWrap(true);
        sohbetAlani.setWrapStyleWord(true);

        girisAlani = new JTextField();
        JButton gonderBtn = new JButton("Gönder");

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("🤖 Sohbet Botu"));
        chatPanel.add(new JScrollPane(sohbetAlani), BorderLayout.CENTER);

        JPanel girisPanel = new JPanel(new BorderLayout());
        girisPanel.add(girisAlani, BorderLayout.CENTER);
        girisPanel.add(gonderBtn, BorderLayout.EAST);
        chatPanel.add(girisPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.SOUTH);

        btnEkle.addActionListener(e -> kitapEkle());
        btnSil.addActionListener(e -> kitapSil());
        btnGuncelle.addActionListener(e -> kitapGuncelle());
        btnDisariAktar.addActionListener(e -> disariAktar());
        btnTema.addActionListener(e -> temaDegistir());

        ActionListener gonderici = e -> mesajGonder();
        girisAlani.addActionListener(gonderici);
        gonderBtn.addActionListener(gonderici);

        kitaplariListele();
        setVisible(true);
    }

    private void kitaplariListele() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:kutuphane.db")) {
            tabloModeli.setRowCount(0);
            String sql = "SELECT * FROM kitaplar WHERE kullanici_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, kullaniciId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tabloModeli.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("kitap_adi"),
                        rs.getString("yazar"),
                        rs.getString("tur")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Listeleme hatası: " + e.getMessage());
        }
    }

    private void kitapEkle() {
        String ad = JOptionPane.showInputDialog(this, "Kitap Adı:");
        String yazar = JOptionPane.showInputDialog(this, "Yazar:");
        String tur = JOptionPane.showInputDialog(this, "Tür:");

        if (ad == null || yazar == null) return;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:kutuphane.db")) {
            String sql = "INSERT INTO kitaplar (kitap_adi, yazar, tur, kullanici_id) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ad);
            ps.setString(2, yazar);
            ps.setString(3, tur);
            ps.setInt(4, kullaniciId);
            ps.executeUpdate();
            kitaplariListele();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ekleme hatası: " + e.getMessage());
        }
    }

    private void kitapSil() {
        int secili = kitapTablosu.getSelectedRow();
        if (secili == -1) return;

        int id = (int) tabloModeli.getValueAt(secili, 0);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:kutuphane.db")) {
            String sql = "DELETE FROM kitaplar WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            kitaplariListele();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Silme hatası: " + e.getMessage());
        }
    }

    private void kitapGuncelle() {
        int secili = kitapTablosu.getSelectedRow();
        if (secili == -1) return;

        int id = (int) tabloModeli.getValueAt(secili, 0);
        String yeniAd = JOptionPane.showInputDialog("Yeni Kitap Adı:", tabloModeli.getValueAt(secili, 1));
        String yeniYazar = JOptionPane.showInputDialog("Yeni Yazar:", tabloModeli.getValueAt(secili, 2));
        String yeniTur = JOptionPane.showInputDialog("Yeni Tür:", tabloModeli.getValueAt(secili, 3));

        if (yeniAd == null || yeniYazar == null) return;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:kutuphane.db")) {
            String sql = "UPDATE kitaplar SET kitap_adi = ?, yazar = ?, tur = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, yeniAd);
            ps.setString(2, yeniYazar);
            ps.setString(3, yeniTur);
            ps.setInt(4, id);
            ps.executeUpdate();
            kitaplariListele();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Güncelleme hatası: " + e.getMessage());
        }
    }

    private void disariAktar() {
        JFileChooser dosyaSecici = new JFileChooser();
        int sonuc = dosyaSecici.showSaveDialog(this);
        if (sonuc != JFileChooser.APPROVE_OPTION) return;

        try (PrintWriter pw = new PrintWriter(dosyaSecici.getSelectedFile())) {
            for (int i = 0; i < tabloModeli.getRowCount(); i++) {
                for (int j = 0; j < tabloModeli.getColumnCount(); j++) {
                    pw.print(tabloModeli.getValueAt(i, j));
                    if (j < tabloModeli.getColumnCount() - 1) pw.print(",");
                }
                pw.println();
            }
            JOptionPane.showMessageDialog(this, "Kitaplar dışa aktarıldı.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Dosya yazma hatası: " + e.getMessage());
        }
    }

    private boolean koyuTema = false;

    private void temaDegistir() {
        try {
            UIManager.setLookAndFeel(koyuTema ?
                    new com.formdev.flatlaf.FlatLightLaf() :
                    new com.formdev.flatlaf.FlatDarkLaf());
            koyuTema = !koyuTema;
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Tema değiştirilemedi: " + e.getMessage());
        }
    }

    private void mesajGonder() {
        String mesaj = girisAlani.getText().trim();
        if (mesaj.isEmpty()) return;

        sohbetAlani.append("👤 Sen: " + mesaj + "\n");
        girisAlani.setText("");

        new Thread(() -> {
            try {
                String yanit = apiyeGonder(mesaj);
                sohbetAlani.append("🤖 Bot: " + yanit + "\n\n");
            } catch (Exception e) {
                sohbetAlani.append("❌ Hata: " + e.getMessage() + "\n");
            }
        }).start();
    }

    private String apiyeGonder(String mesaj) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String json = """
                {
                  "model": "%s",
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ]
                }
                """.formatted(MODEL, mesaj.replace("\"", "\\\""));

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes("utf-8"));
        }

        StringBuilder yanit = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                yanit.append(satir.trim());
            }
        }

        String response = yanit.toString();
        int index = response.indexOf("\"content\":\"");
        if (index != -1) {
            int start = index + 11;
            int end = response.indexOf("\"", start);
            if (end != -1) {
                return response.substring(start, end).replace("\\n", "\n");
            }
        }
        return "(Yanıt çözümlenemedi)";
    }
}
