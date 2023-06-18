**Projenin** **Özeti**

Yazılım laboratuvarı 2 3.projesi olarak bizden "Graf Tabanlı Metin
Özetleme" adındaki bir uygulama yapılması istendi. Bize projeyi tanıtan
pdfte açıklanan toplam 5 ana isteri uygulamaya çalıştık. Bunlar Masaüstü
Arayüzü Geliştirilmesi ve Graf Yapısının Oluşturulması , Cümleler Arası
Anlamsal İlişkinin Kurulması, Cümle Skoru Hesaplama Algoritmasının
Geliştirilmesi, Skorlara Göre Metin Özetleme Algoritmasının
Geliştirilmesi ve Özetleme Başarısının ROUGE Skoru ile Hesaplanmasıdır.

Biz bu proje için bizden istenenler doğrultusunda programlama dilleri
C++, C#, Java veya Python arasından Java'yı, graf yapısını oluşturmak
için JUNG Graph kütüphanesini, Web API kullanımı için Jsoup kütüphanesi
ve dil işlemesi için OpenNLP kullanmayı uygun gördük.

Verilen bir dokümandaki cümlelerin graf yapısına dönüştürülmesi ve bu
graf modelinin görselleştirilmesi istenmektedir. Ardından graf
üzerindeki düğümler ile özet oluşturan bir algoritma oluşturulması
beklenmektedir.

> **I.** **GİRİŞ**

Projede temel amaç; cümleleri graf yapısına çevirip Cümle Seçerek
Özetleme (Extractive Summarization) gerçekleştirmektir. Graf yapısına
çevirerek cümlelerin metindeki anlamsal ilişkilerini görselleştirmek ve
bu ilişkileri kullanarak önemli cümleleri belirlemek amaçlanmaktadır.

Masaüstü uygulamada ilk olarak doküman yükleme işlemi
gerçekleştirilecektir. Ardından yüklenen dokümandaki cümleleri graf
yapısı haline getirerek ve bu graf yapısı görselleştirilecektir. Bu
grafta her bir cümle bir düğümü temsil edecektir. Cümleler arasındaki
anlamsal ilişki kurulmalı, aynı zamanda cümleler skorlanmalıdır.

Proje raporu için
[a relative link](Proje Raporu.pdf)
