# sunflower-server

## API 명세

### 수학 문제를 촬영한 pdf 파일 업로드 API

- 클라이언트에서 요청에 pdf 파일을 포함해 보낸다.
- 서버는 아래 과정을 수행한다.
    - mathpix 외부 api(OCR) 호출: 수학문제 사진 pdf -> txt파일
    - 해바라기 점역 API 호출: txt파일 -> brf파일 (점자 파일)
    - brf파일을 데이터베이스에 저장하고, 클라이언트에 접근 가능한 경로(path) 반환

#### Request

##### Header

```http request
POST /translate-pdf

Content-Type: multipart/form-data
```

##### Body

- Multipart 폼 데이터 (구체적인 내용은 추후에 업데이트 예정)

#### Response

##### Header

```http request
HTTP/1.1 201 CREATED

Location: translations/{id}
```

##### Body

None

---

### 점자 파일 요청 API

#### Request

##### Header

```http request
GET /translations/{id}
```

##### Body

None

#### Response

##### Header

```http request
HTTP/1.1 200 OK
```

##### Body

- brf 파일 (구체적인 내용은 추후에 업데이트 예정)
