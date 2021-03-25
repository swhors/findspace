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
  - 환경 설정 파일을 수정
    - 환경 설정 파일 위치 : src/main/resources/application.properties
    - 수정 할 환경 설정 : 아래의 값은 본인이 Daum이나 Naver의 개발자 계정으로 발급 받은 값을 넣는다.
      - apps.naver.client-id
      - apps.naver.client-secret
      - apps.daum.rest-api-key
  - mvn clean;mvn package
  - java -jar target/findspace-0.0.1-SNAPSHOT.jar

## 상세 기능
- User-Join
  - 사용자를 추가 합니다.
  - 이 명령을 실행 시키고 성공 시에는 DB에 생성 된 사용자 ID를 반환하게 됩니다.

- User-Login
  - 사용자를 인증하고, JWT Tag를 발급하여 검색 서비스 이용시에 사용자 인증을 하도록 합니다.
  - 이 API를 호출 후에, 반환되는 값은 다음과 같이 JWT-Token을 포함하는 JSON 형태의 스트링을 반환 합니다.
    - {"id": 사용자 ID,
    -  "token" : JWT_Token}
  - Search API를 호출할 경우에는 헤더의 "X-AUTH-TOKEN"을 추가하고 위의 값을 입력하여야 합니다.

- Search-Place
  - 사용자의 검색어를 입력 받아서, 다음과 네이버의 검색 엔진으로 조회를 한 후에 결과를 합하여 결과를 전송 합니다.
  - 결과는 다음과 같이 JSON 형태로 반환합니다.
    - {"keyword":"키워드",
    -  "length": int 타입의 검색 결과의 수,
    -  "places":[
    -           {"place":상호명1},
    -           {"place":상호명2},
    -           ...
    -           ]}

- Search-History
  - 사용자 본인이 입력한 검색어 히스토리를 전송 합니다.
  - 결과는 다음과 같이 JSON 형태로 반환합니다.
    - {"length": int 타입의 히스토리 수,
    -  "histories":[
    -               {"keyword":키워드1,"created":조회시간1},
    -               {"keyword":키워드2,"created":조회시간2},
    -               ...
    -              ]}

- Search-Favorite
  - 가장 많이 검색되는 키워드 전송 합니다.
  - 결과는 다음과 같이 JSON 형태로 반환합니다.
    - {"length": int 타입의 favorite의 결과 수,
    -  "favorities":[
    -                {"keyword":키워드1,"hitCount":조회수1},
    -                {"keyword":키워드2,"hitCount":조회수2}
    -                ...
    -               ]}

## 테스트
- CURL을 통한 테스트 방법
  - User-Join
    - curl -H "Content-Type:application/json" -d "{\"userName\":\"사용자이름\",\"password\":\"암호\"}" -X POST http://서버주소:서버포트/join

  - User-Login
    - curl -H "Content-Type:application/json" -d "{\"userName\":\"사용자이름\",\"password\":\"암호\"}" -X POST http://서버주소:서버포트/login

  - Search-Place
    - curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: JWT_Token" -G -X GET http://서버주소:서버포트/api/search/place --data-urlencode "keyword=키워드"

  - Search-History
    - curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: JWT_Token" -X GET http://서버주소:서버포트/api/search/history

  - Search-Favorite
    - curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: JWT_Token" -X GET http://서버주소:서버포트/api/search/favorite

- 예제
  - User-Join
    - curl -H "Content-Type:application/json" -d "{\"userName\":\"a-user@test.com\",\"password\":\"password\"}" -X POST http://127.0.0.1:7060/join

  - User-Login
    - curl -H "Content-Type:application/json" -d "{\"userName\":\"a-user@test.com\",\"password\":\"password\"}" -X POST http://127.0.0.1:7060/login

  - Search-Place
    - curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEAzMzI2MTViYyIsImlhdCI6MTYxNjY1MDEyMywiZXhwIjoxNjE2NjUxOTIzfQ.o4e5M4kWy0lmcGmh2UwQn0hkVTHkW3WiEiWcsG_voDE" -G -X GET http://127.0.0.1:7060/api/search/place --data-urlencode "keyword=seoul"

  - Search-History
    - curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEAzMzI2MTViYyIsImlhdCI6MTYxNjY1MDEyMywiZXhwIjoxNjE2NjUxOTIzfQ.o4e5M4kWy0lmcGmh2UwQn0hkVTHkW3WiEiWcsG_voDE" -X GET http://127.0.0.1:7060/api/search/history

  - Search-Favorite
    - curl -H "Accept: */*" -H "Connection: keep-alive" -H "X-AUTH-TOKEN: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEAzMzI2MTViYyIsImlhdCI6MTYxNjY1MDEyMywiZXhwIjoxNjE2NjUxOTIzfQ.o4e5M4kWy0lmcGmh2UwQn0hkVTHkW3WiEiWcsG_voDE" -X GET http://127.0.0.1:7060/api/search/favorite

- 결과 샘플
  - User-Join
    - {"id":2}
  - User-Login
    - {"id":2, "token":" eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXVzZXJAdGVzdC5jb20iLCJyb2xlcyI6ImphdmEudXRpbC5zdHJlYW0uUmVmZXJlbmNlUGlwZWxpbmUkSGVhZEA2OTFjZGRlYiIsImlhdCI6MTYxNjY1MTMxOSwiZXhwIjoxNjE2NjUzMTE5fQ.GbbpXhqN37MQwQ5qFE-r3RtsMKWaCCAbnrdN9C62l2E"
  - Search-Place
    - {"keyword":"seoul","length":15,"places":[{"place":"포시즌스호텔서울"},{"place":"롯데호텔서울 무궁화"},{"place":"롯데호텔서울 모모야마"},{"place":"롯데호텔서울 도림"},{"place":"아라리오갤러리 서울"},{"place":"NEO SEOUL GUESTHOUSE"},{"place":"Hoods Seoul"},{"place":"dna SEOUL"},{"place":"I Art Seoul Space"},{"place":"uh suite the seoul"},{"place":"CHERTOI SEOUL"},{"place":"YDP seoul"},{"place":"CGS SEOUL"},{"place":"SVA SEOUL OFFICE"},{"place":"라마다서울호텔"}]}
  - Search-History
    - {"length":7,"histories":[{"keyword":"seoul","created":"2021-03-25T14:52:03.288295"},{"keyword":"seoul","created":"2021-03-25T14:35:23.705477"},{"keyword":"seoul","created":"2021-03-25T14:34:23.291119"},{"keyword":"seoul","created":"2021-03-25T14:34:22.136783"},{"keyword":"seoul","created":"2021-03-25T14:34:21.279446"},{"keyword":"seoul","created":"2021-03-25T14:34:16.018"},{"keyword":"seoul","created":"2021-03-25T14:34:12.805273"}]}
  - Search-Favorite
    - {"length":2,"favorities":[{"keyword":"seoul","hitCount":7},{"keyword":"인천횟집","hitCount":2}]}

## contact
  - swhors@naver.com
