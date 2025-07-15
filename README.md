Yapay Zeka Destekli Kütüphane Uygulaması

Bu proje, Java Swing kullanılarak geliştirilmiş kullanıcıya özel yapay zeka destekli kişisel kütüphane yönetim sistemi uygulamasıdır. Her kullanıcı kendi kişisel hesabını oluşturabilir kitaplarını görüntüleyebilir, ekleyebilir, güncelleyebilir veya silebilir. Aynı zamanda yapay zeka destekli sohbet botu sayesinde kullanıcılar aynı arayüz içinde OpenRouter API ile entegre edilmiş bir yapay zeka ile etkileşim kurabilir.

---

 Kullanılan Teknolojiler
- Java (Swing)
- SQLite veritabanı
- FlatLaf (modern tema desteği)
- OpenRouter API (Chatbot için)
- DeepSeek ve Mistral modelleri
- NetBeans IDE (Geliştirme ortamı)

---

 Özellikler

- Giriş yapan kullanıcı sadece kendi kitaplarını görebilir
- Kitap ekleme, silme, güncelleme işlemleri
- Kitapları `.csv` / `.txt` olarak dışa aktarma
- Tema değiştirme (açık / koyu)
- Chatbot entegrasyonu (OpenRouter + DeepSeek)
- Şifre gizleme

---

Chatbot Özelliği
Uygulama içinde entegre bir yapay zeka destekli sohbet botu yer almaktadır. Kullanıcılar bu botu kullanarak:
- Eklediği kitaplar hakkında bilgi alabilir
- Hangi türde kitaplar önerildiğini sorabilir
- Bilgi almak istediği konular hakkında sorular yöneltebilir
- Genel sohbet edebilir

!!!Projeyi çalıştırdığınızda, sohbet botuna mesaj gönderirken bir hata alacaksınız. Bu, sistemde tanımlı olan API anahtarının geçersiz veya sınırlı olmasından kaynaklanır.

Bu sorunu çözmek için:

https://openrouter.ai adresine giderek ücretsiz bir API anahtarı oluşturun.

Proje klasöründe KutuphaneApp.java dosyasını açın.

19. satırdaki şu satırı bulun:

private static final String API_KEY = "sk-or-......";
Tırnak içindeki kısmı kendi API anahtarınızla değiştirin.

Uygulamayı tekrar çalıştırdığınızda sohbet botu sorunsuz şekilde yanıt verecektir. 

---

Kullanıcı Kaydı

Uygulamayı çalıştırdıktan sonra Giriş Ekranı üzerinden kendi hesabınızı oluşturabilirsiniz.  
Yeni Hesap Oluştur butonuna tıklayarak kullanıcı adı ve şifrenizi belirleyip kayıt olabilirsiniz.  
Oluşturduğunuz hesapla giriş yaptıktan sonra sadece size ait kitapları görebilir, ekleyebilir ve silebilirsiniz.


---


Kurulum
Projeyi GitHub'dan indirin veya klonlayın
GitHub sayfasına gidip Code > Download ZIP diyerek ya da terminalden:

git clone https://github.com/meliksahkose/YapayZekaDestekliKutuphane.git

komutuyla indirebilirsiniz.

NetBeans ile projeyi açın
NetBeans IDE’yi açın.
"Open Project" seçeneğiyle klasörü seçin ve projeyi açın.
Gerekli kütüphaneler zaten ekli Projenin içinde lib klasörü bulunmaktadır.
Bu klasörde gerekli .jar dosyaları yer aldığı için sizin ekstra bir şey indirmenize gerek yok.
NetBeans otomatik olarak bu kütüphaneleri tanır. Eğer tanımazsa:
Proje > Sağ tık > Properties > Libraries > Add JAR/Folder > lib klasöründeki .jar dosyasını ekleyin.

VeritabaniOlustur.java dosyasını çalıştırarak manuel oluşturabilirsiniz.



Main.java dosyasını çalıştırarak projeyi başlatabilirsiniz.
