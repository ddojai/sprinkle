# Sprinkle

## H2 Database
* url : http://localhost:8080/h2-console
* jdbc-url : jdbc:h2:mem://localhost/~/testdb;MODE=MYSQL

## 문제 해결 전략
* 분배 로직
    * MessageService.sendMoney()
    * 공평하게 배분
    * ex) 10000원을 3명이서 나누면 3333, 3333, 3334 원씩 분배
* token 생성 규칙
    * Message.generateToken();
    * 알파벳+숫자 조합으로 3자리 문자열 생성
    * 해당 방에 메시지 토큰을 모두 조회해서 중복되면 다시 생성 로직 반복

## API

### 공통 headers
* X-USER-ID : 1 (User 엔티티의 id 값)
* X-ROOM-ID : 8fea6164-852d-4fdc-8dc0-b627033e4b1e (Room 엔티티의 code 값 - UUID)

### 뿌리기 API
* method : post
* url : http://localhost:8080/api/messages/money

#### body
``` request
{
	"sendAmount": 10000,
	"numberPeople": 3
}
```

``` response
{
    "token": "vO0"
}
```

### 받기 API
* method : put
* url : http://localhost:8080/api/messages/{token}/money

#### body
``` response
{
    "receivedAmount": 3333
}
```

### 조회하기 API
* method : get
* url : http://localhost:8080/api/messages/{token}/money

#### body
``` response
{
    "sendDate": "2020-11-23T22:32:24.964",
    "totalAmount": 10000,
    "receivedCompletedAmount": 3334,
    "receivedUserDtos": [
        {
            "userId": 2,
            "receivedAmount": 3334
        }
    ]
}
```