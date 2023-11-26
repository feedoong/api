## REST API 문서화
이 프로젝트의 REST API 문서는 `Spring REST Docs`와 `asciidoctor`를 활용하여 관리됩니다.

### 1. 테스트 코드 작성

`Spring REST Docs`를 이용한 테스트 코드를 작성하여 API의 세부 사항을 명세합니다.

### 2. snippets adoc 생성

테스트 실행을 통해 API 요청과 응답에 대한 세부 정보가 담긴 `adoc` 파일이 `build/generated-snippets` 디렉토리에 생성됩니다.

### 3. HTML 문서에 사용될 adoc 파일 제작

생성된 생성된 `snippets`를 통합하여 `rest-api.adoc` 문서를 작성합니다.

### 4 HTML 생성

```sh
$ ./gradlew copyDocument
```

`copyDocument`는 `asciidoctor` 태스크를 실행하여 HTML 문서를 생성하고, 이를  
`src/main/resources/static/docs` 디렉토리로 복사하여 빌드 과정에 포함시킵니다.
