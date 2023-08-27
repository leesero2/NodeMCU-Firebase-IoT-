#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <DHT.h>
#include <time.h>

// Config Firebase
#define FIREBASE_HOST "android-project-d82ce.firebaseio.com"
#define FIREBASE_AUTH "9cJ40OqEwXBlawhvNFobYUjeueBUX6xIq000jAwB"
#define WIFI_SSID "iptime"
#define WIFI_PASSWORD "dlwlgns9462" //000002487

// Config DHT
#define DHTPIN D4
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

// Config time
int timezone = 9; // 9 -> 한국시간
int dst = 0;

void setup() {
  Serial.begin(115200);
  dht.begin();

  WiFi.mode(WIFI_STA);  // Wi-Fi 초기화 
  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting......");
  
  while (WiFi.status() != WL_CONNECTED) { //연결이 안되어있다
    Serial.print("."); // 0.5초 간격으로 "."을 생성 
    delay(500);
  }
  Serial.println();
  Serial.print("connected: "); // 그게 아니면 연결되었다 출력 
  Serial.println(WiFi.localIP());

  // NTP 설정(네트워크 시간 설정)
  configTime(timezone * 3600, dst, "kr.pool.ntp.org", "ntp2.kornet.net");
  Serial.println("Waiting for time");
  while (!time(nullptr)) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.println("Now: " + NowString());

  // Firebase 실시간 데이터베이스 인증
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void loop() {
  delay(5000);
  // Read temp & Humidity for DHT22
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  float index; //불쾌지수
  String stat; //네트워크 상태

  if (WiFi.status() != WL_CONNECTED) {
    stat = "OFF"; // 연결이 끊어졌으면 OFF
  }else{
    stat = "ON"; //그게 아니라면 ON
  }

  
  index = 1.8*t-0.55*(1-h/100)*(1.8*t-26)+32; // 불쾌지수 계산식

  if (isnan(h) || isnan(t)) {
    Serial.println("Failed to read from DHT sensor!");
    delay(500);
    return;
  }
  
  Serial.print("Humidity: ");
  Serial.print(h);
  Serial.print(" %\t");
  Serial.print("Temperature: ");
  Serial.print(t);
  Serial.print(" *C ");
  Serial.println();
  if (WiFi.status() != WL_CONNECTED) {
    Serial.print(stat);
    Serial.print("off");
  }
  파이어베이스에 메모리 공간을 만들고 값을 넣음
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();
  root["temperature"] = t;
  root["humidity"] = h;
  root["index"] = index;
  root["time"] = NowString();
  root["status"] = stat;
  
  // append a new value to /logDHT 값을 누적시킴 
  String name = Firebase.push("logDHT", root);
  // handle error
  if (Firebase.failed()) {
      Serial.print("pushing /logs failed:");
      Serial.println(Firebase.error());  
      return;
  }
  Serial.print("pushed: /logDHT/");
  Serial.println(name);
  delay(5000);
}

// 현재 시간 받아오는 함수
String NowString() {
  time_t now = time(nullptr);
  struct tm* newtime = localtime(&now);
  String tmpNow = "";
  tmpNow += String(newtime->tm_year + 1900);
  tmpNow += "-";
  tmpNow += String(newtime->tm_mon + 1);
  tmpNow += "-";
  tmpNow += String(newtime->tm_mday );
  tmpNow += " ";
  tmpNow += String(newtime->tm_hour );
  tmpNow += ":";
  tmpNow += String(newtime->tm_min);
  tmpNow += ":";
  tmpNow += String(newtime->tm_sec);
  return tmpNow;
}
