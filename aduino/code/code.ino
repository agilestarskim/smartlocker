
#include <SoftwareSerial.h> // 소프트웨어 라이브러리 사용

// 릴레이 5개를 디지털핀 3~7번에 연결 
#define relay1 8
#define relay2 7
#define relay3 6
#define relay4 5
#define relay5 4
byte data;

SoftwareSerial BTSerial(11,10); // 소프트웨어 시리얼 블루투스 Rx,Tx설정

void setup() 
{
  Serial.begin(9600);      // 통신속도 9600bps로 시리얼 통신 시작
  BTSerial.begin(9600);    // 시리얼 모니터 시작, 속도는 9600
  
  // 릴레이 연결 핀들을 출력 모드로 설정
  pinMode(relay1,OUTPUT);
  pinMode(relay2,OUTPUT);
  pinMode(relay3,OUTPUT);
  pinMode(relay4,OUTPUT);
  pinMode(relay5,OUTPUT);
  // 초기 값 모든 릴레이 OFF로 설정
  digitalWrite(relay1, HIGH); 
  digitalWrite(relay2, HIGH);
  digitalWrite(relay3, HIGH); 
  digitalWrite(relay4, HIGH);
  digitalWrite(relay5, HIGH);
}

void loop() 
{
  if(BTSerial.available())
  data= BTSerial.read(); // 들어온 데이터를 data변수에 저장

  switch(data) 
  {
  case 'a': // 데이터 값이 a이면
    digitalWrite(relay1, LOW); // 릴레이1 ON
    break;   
    
  case 'b': // 데이터 값이 b이면
    digitalWrite(relay1, HIGH); // 릴레이1 OFF
    break;

  case 'c':
    digitalWrite(relay2, LOW);
    break;
    
  case 'd':
    digitalWrite(relay2, HIGH); 
    break;    
    
  case 'e':
    digitalWrite(relay3, LOW);
    break;
   
  case 'f':
    digitalWrite(relay3, HIGH);
    break;

  case 'g':
    digitalWrite(relay4, LOW);
    break;
    
  case 'h': 
    digitalWrite(relay4, HIGH);
    break;

  case 'i':
    digitalWrite(relay5, LOW);
    break;

  case 'j': 
    digitalWrite(relay5, HIGH); 
    break;
    
  case '0':
    digitalWrite(relay1, LOW);
    digitalWrite(relay2, LOW); 
    digitalWrite(relay3, LOW); 
    digitalWrite(relay4, LOW); 
    digitalWrite(relay5, LOW);
    break;
  case '1':
    digitalWrite(relay1, HIGH);
    digitalWrite(relay2, HIGH); 
    digitalWrite(relay3, HIGH); 
    digitalWrite(relay4, HIGH); 
    digitalWrite(relay5, HIGH);
    break;
  }
}
