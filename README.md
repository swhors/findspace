# findspace

## 소개
- 이것은 사용자의 입력을 받아서 다음과 네이버의 Open-Api를 이용하여 검색을 하여 사용자에게 전달하는 서버입니다.
- 이것은 사용자 관리와 검색을 위하여 다음과 같은 API를 제공합니다.
  - User-Join
  - User-Login
  - Serach-Place
  - Search-History
  - Search-Favorite

## 상세 기능
- User-Join
  - 사용자를 추가 합니다.
  - 테스트 방법은 다음과 같습니다.
    - curl -H "Content-Type:application/json" -d "{\"userName\":\"a-user@test.com\",\"password\":\"password\"}" -X POST http://192.168.4.26:7060/join
  - 이 명령을 실행 시키고 성공 시에는 DB에 생성 된 사용자 ID를 반환하게 됩니다.
- User-Login
  - 사용자를 인증하고, JWT Tag를 발급하여 검색 서비스 이용시에 사용자 인증을 하도록 합니다.
- Search-Place
  - 사용자의 검색어를 입력 받아서, 다음과 네이버의 검색 엔진으로 조회를 한 후에 결과를 합하여 결과를 전송 합니다.
- Search-History
  - 사용자 본인이 입력한 검색어 히스토리를 전송 합니다.
- Search-Favorite
  - 가장 많이 검색되는 키워드 전송 합니다.

## contact
  - swhors@naver.com
