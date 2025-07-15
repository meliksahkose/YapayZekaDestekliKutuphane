import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Açık tema ile başlatılıyor, isteğe göre tema daha sonra değiştirilebilir
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.out.println("Tema yüklenirken hata oluştu: " + ex.getMessage());
        }

        // Arayüzü başlat
        SwingUtilities.invokeLater(() -> new GirisEkrani());
    }
}