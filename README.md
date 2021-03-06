# findspace

## 소개
- 이 프로젝트는 사용자의 키워드를 입력 받아서 위치를 검색하는 것입니다.
- 검색은 다음과 네이버의 Open-Api를 이용하며, 두 개의 API를 통해 검색한 결과를 합하여 사용자에게 전달 합니다.

- 사용자 관리와 검색을 위하여 다음과 같은 API를 제공합니다.
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
  - 환경 설정 파일을 수정
    - 환경 설정 파일 위치 : src/main/resources/application.properties
    - 수정 할 환경 설정 : 아래의 값은 본인이 Daum이나 Naver의 개발자 계정으로 발급 받은 값을 넣는다.
      - apps.naver.client-id
      - apps.naver.client-secret
      - apps.daum.rest-api-key
  - mvn clean;mvn package
  - java -jar target/findspace-0.0.1-SNAPSHOT.jar

- API 사용 주의
  - 이 프로그램의 Security를 위하여 JWT를 사용합니다.
  - User-Login을 제외한 나머지 API를 호출하기 위해서는 사용자 로그인 후에 얻은 JWT-Token을 헤더의 "X-AUTH-TOKEN"에 추가하여야 합니다.

## 상세 설명
- User-Join
  - 설명
    - 사용자를 추가 합니다.
    - 이 API는 "admin@test.com / password"으로 로그인하고 얻은 JWT-Token을 이용하여야 사용자 추가가 가능합니다.
  - API
    ```
    /join
    body : {"userName": 사용자ID, "password": 암호}
    ```
  - 결과
    ```
    { "id" : 사용자 ID }
    ```

- User-Login
  - 설명
    - 사용자를 인증하고, JWT Tag를 발급하여 검색 서비스 이용시에 사용자 인증을 하도록 합니다.
  - API
    ```
    /login
    body : {"userName": 사용자ID, "password": 암호}
    ```
  - 결과
    ```
    {
     "id": 사용자 ID,
     "token" : JWT_Token
    }
    ```
  - 주의
    - Search API를 호출할 경우에는 헤더의 "X-AUTH-TOKEN"을 추가하고 위의 "token"을 입력하여야 합니다.

- Search-Place
  - 설명
    - 사용자의 검색어를 입력 받아서, 다음과 네이버의 검색 엔진으로 조회를 한 후에 결과를 합하여 결과를 전송 합니다.
  - API
    ```
    /api/search/place?"keyword=키워드"
    ```
  - 결과
    ```
    {
     "keyword":"키워드",
     "length": int 타입의 검색 결과의 수,
     "places":[
              {"place":상호명1},
              {"place":상호명2},
              ...
              ]
    }
    ```

- Search-History
  - 설명
    - 사용자 본인이 입력한 검색어 히스토리를 전송 합니다.
  - API
    ```
    /api/search/history
    ```
  - 결과
    ```
    {
     "length": int 타입의 히스토리 수,
     "histories":[
                  {"keyword":키워드1,"created":조회시간1},
                  {"keyword":키워드2,"created":조회시간2},
                  ...
                 ]
    }
    ```

- Search-Favorite
  - 설명
    - 가장 많이 검색되는 키워드 전송 합니다.
  - API
    ```
    /api/search/favorite
    ```
  - 결과
    ```
    {
     "length": int 타입의 favorite의 결과 수,
     "favorities":[
                   {"keyword":키워드1,"hitCount":조회수1},
                   {"keyword":키워드2,"hitCount":조회수2}
                   ...
                  ]
    }
    ```

## 테스트
- CURL을 통한 테스트 방법
  - User-Join
    ```
    curl -H "Content-Type:application/json"  -H "X-AUTH-TOKEN: JWT_Token" -d "{\"userName\":\"사용자이름\",\"password\":\"암호\"}" -X POST http://서버주소:서버포트/join
    ```

  - User-Login
    ```
    curl -H "Content-Type:application/json" -d "{\"userName\":\"사용자이름\",\"password\":\"암호\"}" -X POST http://서버주소:서버포트/login
    ```

  - Search-Place
    ```
    curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: JWT_Token" -G -X GET http://서버주소:서버포트/api/search/place --data-urlencode "keyword=키워드"
    ```

  - Search-History
    ```
    curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: JWT_Token" -X GET http://서버주소:서버포트/api/search/history
    ```

  - Search-Favorite
    ```
    curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: JWT_Token" -X GET http://서버주소:서버포트/api/search/favorite
    ```

- 예제
  - User-Join
    ```
    curl -H "Content-Type:application/json" -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEAzMzI2MTViYyIsImlhdCI6MTYxNjY1MDEyMywiZXhwIjoxNjE2NjUxOTIzfQ.o4e5M4kWy0lmcGmh2UwQn0hkVTHkW3WiEiWcsG_voDE" -d "{\"userName\":\"a-user@test.com\",\"password\":\"password\"}" -X POST http://127.0.0.1:7060/join
    ```

  - User-Login
    ```
    curl -H "Content-Type:application/json" -d "{\"userName\":\"a-user@test.com\",\"password\":\"password\"}" -X POST http://127.0.0.1:7060/login
    ```

  - Search-Place
    ```
    curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEAzMzI2MTViYyIsImlhdCI6MTYxNjY1MDEyMywiZXhwIjoxNjE2NjUxOTIzfQ.o4e5M4kWy0lmcGmh2UwQn0hkVTHkW3WiEiWcsG_voDE" -G -X GET http://127.0.0.1:7060/api/search/place --data-urlencode "keyword=seoul"
    ```

  - Search-History
    ```
    curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEAzMzI2MTViYyIsImlhdCI6MTYxNjY1MDEyMywiZXhwIjoxNjE2NjUxOTIzfQ.o4e5M4kWy0lmcGmh2UwQn0hkVTHkW3WiEiWcsG_voDE" -X GET http://127.0.0.1:7060/api/search/history
    ```

  - Search-Favorite
    ```
    curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEAzMzI2MTViYyIsImlhdCI6MTYxNjY1MDEyMywiZXhwIjoxNjE2NjUxOTIzfQ.o4e5M4kWy0lmcGmh2UwQn0hkVTHkW3WiEiWcsG_voDE" -X GET http://127.0.0.1:7060/api/search/favorite
    ```

- 결과 샘플
  - User-Join
    ```
    {"id":2}
    ```
  - User-Login
    ```
    {"id":2, "token":" eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEA2OTFjZGRlYiIsImlhdCI6MTYxNjY1MTMxOSwiZXhwIjoxNjE2NjUzMTE5fQ.GbbpXhqN37MQwQ5qFE-r3RtsMKWaCCAbnrdN9C62l2E"
    ```
  - Search-Place
    ```
    {"keyword":"seoul","length":15,"places":[{"place":"포시즌스호텔서울"},{"place":"롯데호텔서울 무궁화"},{"place":"롯데호텔서울 모모야마"},{"place":"롯데호텔서울 도림"},{"place":"아라리오갤러리 서울"},{"place":"NEO SEOUL GUESTHOUSE"},{"place":"Hoods Seoul"},{"place":"dna SEOUL"},{"place":"I Art Seoul Space"},{"place":"uh suite the seoul"},{"place":"CHERTOI SEOUL"},{"place":"YDP seoul"},{"place":"CGS SEOUL"},{"place":"SVA SEOUL OFFICE"},{"place":"라마다서울호텔"}]}
    ```
  - Search-History
    ```
    {"length":7,"histories":[{"keyword":"seoul","created":"2021-03-25T14:52:03.288295"},{"keyword":"seoul","created":"2021-03-25T14:35:23.705477"},{"keyword":"seoul","created":"2021-03-25T14:34:23.291119"},{"keyword":"seoul","created":"2021-03-25T14:34:22.136783"},{"keyword":"seoul","created":"2021-03-25T14:34:21.279446"},{"keyword":"seoul","created":"2021-03-25T14:34:16.018"},{"keyword":"seoul","created":"2021-03-25T14:34:12.805273"}]}
    ```
  - Search-Favorite
    ```
    {"length":2,"favorities":[{"keyword":"seoul","hitCount":7},{"keyword":"인천횟집","hitCount":2}]}
    ```

## contact
  - swhors@naver.com

## 기타
  -  JWT 관련 일부 코드는 다음 사이트의 자바 코드를 참조하여 작성하였습니다.
    - https://daddyprogrammer.org/post/636/springboot2-springsecurity-authentication-authorization/
