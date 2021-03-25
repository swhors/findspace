# findspace

## 소개
- 이것은 사용자의 입력을 받아서 다음과 네이버의 Open-Api를 이용하여 검색을 하여 사용자에게 전달하는 서버입니다.
- 이것은 사용자 관리와 검색을 위하여 다음과 같은 API를 제공합니다.
  - User-Join
  - User-Login
  - Serach-Place
  - Search-History
  - Search-Favorite

## 실행 방법
- 실행 환경
  -  JAVA 1.8 이상 및 Kotlin 1.4
  -  maven 최신

- 실행 방법
  - mvn clean;mvn package
  - java -jar target/findspace-0.0.1-SNAPSHOT.jar

## 상세 기능

- User-Join
  - 사용자를 추가 합니다.
  - 테스트 방법은 다음과 같습니다.
    - curl -H "Content-Type:application/json" -d "{\"userName\":\"a-user@test.com\",\"password\":\"password\"}" -X POST http://192.168.4.26:7060/join
  - 이 명령을 실행 시키고 성공 시에는 DB에 생성 된 사용자 ID를 반환하게 됩니다.

- User-Login
  - 사용자를 인증하고, JWT Tag를 발급하여 검색 서비스 이용시에 사용자 인증을 하도록 합니다.
  - 테스트 방법은 다음과 같습니다.
    - curl -H "Content-Type:application/json" -d "{\"userName\":\"a-user@test.com\",\"password\":\"password\"}" -X POST http://192.168.4.26:7060/login
  - 위 호출이 성공한 경우에는 아래와 같이 JWT 토근을 반환하게 됩니다.
    - eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEA2OTFjZGRlYiIsImlhdCI6MTYxNjY1MTMxOSwiZXhwIjoxNjE2NjUzMTE5fQ.GbbpXhqN37MQwQ5qFE-r3RtsMKWaCCAbnrdN9C62l2E
  - Search API를 호출할 경우에는 헤더의 "X-AUTH-TOKEN"을 추가하고 위의 값을 입력하여야 합니다.

- Search-Place
  - 사용자의 검색어를 입력 받아서, 다음과 네이버의 검색 엔진으로 조회를 한 후에 결과를 합하여 결과를 전송 합니다.
  - 테스트 방법은 다음과 같습니다.
    - curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEAzMzI2MTViYyIsImlhdCI6MTYxNjY1MDEyMywiZXhwIjoxNjE2NjUxOTIzfQ.o4e5M4kWy0lmcGmh2UwQn0hkVTHkW3WiEiWcsG_voDE" -G -X GET http://192.168.4.26:7060/api/search/place --data-urlencode "keyword=seoul"
  - 이것의 실행 결과는 다음과 같이 JSON 형태로 반환합니다.
  - {"keyword":"seoul","length":15,"places":[{"place":"포시즌스호텔서울"},{"place":"롯데호텔서울 무궁화"},{"place":"롯데호텔서울 모모야마"},{"place":"롯데호텔서울 도림"},{"place":"아라리오갤러리 서울"},{"place":"NEO SEOUL GUESTHOUSE"},{"place":"Hoods Seoul"},{"place":"dna SEOUL"},{"place":"I Art Seoul Space"},{"place":"uh suite the seoul"},{"place":"CHERTOI SEOUL"},{"place":"YDP seoul"},{"place":"CGS SEOUL"},{"place":"SVA SEOUL OFFICE"},{"place":"라마다서울호텔"}]}

- Search-History
  - 사용자 본인이 입력한 검색어 히스토리를 전송 합니다.
  - 테스트 방법은 다음과 같습니다.
    - curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEAzMzI2MTViYyIsImlhdCI6MTYxNjY1MDEyMywiZXhwIjoxNjE2NjUxOTIzfQ.o4e5M4kWy0lmcGmh2UwQn0hkVTHkW3WiEiWcsG_voDE" -X GET http://192.168.4.26:7060/api/search/history
  - 이것의 실행 결과는 다음과 같이 JSON 형태로 반환합니다.
    - {"length":7,"histories":[{"keyword":"seoul","created":"2021-03-25T14:52:03.288295"},{"keyword":"seoul","created":"2021-03-25T14:35:23.705477"},{"keyword":"seoul","created":"2021-03-25T14:34:23.291119"},{"keyword":"seoul","created":"2021-03-25T14:34:22.136783"},{"keyword":"seoul","created":"2021-03-25T14:34:21.279446"},{"keyword":"seoul","created":"2021-03-25T14:34:16.018"},{"keyword":"seoul","created":"2021-03-25T14:34:12.805273"}]}

- Search-Favorite
  - 가장 많이 검색되는 키워드 전송 합니다.
  - 테스트 방법은 다음과 같습니다.

  - 결과는 다음과 같이 JSON 형태로 반환합니다.
    - {"length":2,"favorities":[{"keyword":"seoul","hitCount":7},{"keyword":"인천횟집","hitCount":2}]}

## 테스트

- Search-Favorite
    - curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEAzMzI2MTViYyIsImlhdCI6MTYxNjY1MDEyMywiZXhwIjoxNjE2NjUxOTIzfQ.o4e5M4kWy0lmcGmh2UwQn0hkVTHkW3WiEiWcsG_voDE" -X GET http://192.168.4.26:7060/api/search/favorite
## contact
  - swhors@naver.com
