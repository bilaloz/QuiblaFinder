<h2>Kabe Bulucu (Qibla Finder ) </h2>

Uygulamanın amacı gps verilerini kullanarak kullanıcının kıbleyi pusula veya google maps üzerinden bulması. 
(The purpose of the application is to find the direction of the qibla with compass or google map.) <hr>

![maps](https://user-images.githubusercontent.com/25391443/53835864-84179a00-3f9f-11e9-85fb-d16325f4cc64.gif)
![compass](https://user-images.githubusercontent.com/25391443/53835917-a4dfef80-3f9f-11e9-8693-c25443b5401b.gif)<hr>
<h3>Uygulamanın Özellikleri (App Properties)</h3>
     *Google Maps ile Lokasyon Tespiti (My Location with Google Maps)<br>
    *Manyetik Alan Tespiti (Magnetic Field Detection )<br>
    *Uzaklık Hesaplama (Distance Calculation)<br>
    *Sensör Kullanımı (Sensor Usage )<br>
    *Kullanıcının Kıbleye Göre Açı Değerinin Hesaplanması (Calculation of Qibla Angle Value by User Location)<br>  
    *Harita Tipleri ve Yer İşaretcilerinin Kullanımı (Maps type and marker uses)<br><hr>
<h3>Metodların Kullanım Amaçları (Description of Methods and Class)</h3>
    <h5>MainActivity (Class)</h5>  Diğer class ların tek bir çatı altında toplanıp ViewPager class'ına set edildiği sınıftır. Uygulamanın başlangıcı 
 bu sınıf altında yapılmaktadır. (The class in which the Viewpager class is called and other classes are set in the tablayout)<br>
    <h5>ViewPagerAdapter(Class)</h5>  Sınıfların tablayouta eklendiği yer. (The class set in the tablayout)<br>
    <h5>GreatCircleBearing(Class) </h5> Kullanıcı ve kabe konumu alınarak kullanıcı ile kabe arasındaki açının hesaplandıp değerin geri döndürüldüğü sınıftır..
    (User and Qibla location are received. The angle between these positions is calculated.)<br>
    <h5>Maps Fragment </h5> Map işlemlerinin ,gps konumlarının alındığı ve sensör hesaplamalarının yapıldığı sınıftır.(Map operations, GPS positions and calculation of sensors takes place in this class)<br>
    <h5>Compass Fragment </h5> Pusula eklenmesi ve sensör hesaplanması bu sınıfta gerçekleşir (Compass adding and calculating the sensor is this class)<br>
 <hr>
 <h3>Bazı Methodlar (Other Methods)</h3>
  <i><h5>public void onMapReady(GoogleMap googleMap)</h5></i> Google Map oluşturma , kullanıcı izinlerinin alınması ve harita ayarlarının yapıldığı method 'dur.(Google Harita oluşturma ve harita ayarları.) <br>
  <i><h5>private Object DistanceCalculator(Location location, LatLng kabeLocation) </h5></i> Kullanıcının Kıbleye olan uzaklığının kilometre olarak hesaplandığı metotdur.(The distance from the user to the Qibla is calculated in kilometers.)<br>
<i><h5>public  void DrawLine(Location location)</h5></i> Kullanıcı ile kıble arasındaki yolun çizildiği sınıftır.(It is the methods in which the path between the user and the Qibla is drawn.) <br>
<i><h5>private void AddMarker(Location location)</h5></i> Kullanıcı ve kıble konumunun map üzerinden işaretlendiği metotdur.(User and Qibla position is marked in the map.)<br>
<i><h5>public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)</h5></i> GPS izin kontrolü metodu (GPS Permission Granted Method)<br>
 <i><h5>public void onLocationChanged(Location location)</h5></i> Kullanıcı gps değişikliğinde çalışacak olan metot.(The method that will work when the user changes gps.) <br>
 <i><h5>public boolean onMyLocationButtonClick()</h5></i>Kullanıcın butona tıkladığında lokasyonunu bulan metot. (User click button find location method (User clicks the button and locates the user) <br>
 <i><h5> private boolean checkPermission()</h5></i> Kullanıcı GPS izinini sorgular geriye boolean değer döndürür (True veya False) (User GPS permission queries return boolean value back True or False) <br>
 <i><h5> private void askPermission()</h5></i>Kullanıcıya GPS izinini isteği yollar (Request GPS permission to the user) <br>
 <i><h5> public void onSensorChanged(SensorEvent event) </h5></i> Sensör değişiklik olayını dinler ve hesaplamalar yapar. (Listens the sensor planning event and makes calculations)<br>
 <i><h5> public void CalculateQibla(double latitude, double longitude)</h5></i> Kullanıcı ve kıble arasındaki açı hesaplanır.(The angle between the user and the Qibla is calculated.)<br>
 

