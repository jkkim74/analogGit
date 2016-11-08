# 파일설명
- .bowerrc : Bower 의존성 설치 위치
- .eslintrc : Javascript Linting(정적분석) 설정파일
- .gitignore : 생략
- bower.json : Bower 설정파일
- gulpfile.js : Gulp 설정파일
- package.json : NPM 설정파일
- pom.xml : Maven 설정파일
- node_modules : (자동생성) NPM 의존성
- src/main/java : Java 소스파일
- src/main/resources/config : Back-end 환경설정파일
- src/main/resources/sql : Back-end SQL 파일 (.xml)
- src/main/resources/templates : 이메일 등 템플릿파일
- src/main/webapp : HTML, JS, CSS 소스파일 및 이미지와 폰트
- src/main/webapp/bower_components : (자동생성) Bower 의존성

# 빌드환경설명
Front-end에서는 Bower, Gulp를 사용하여 의존성 및 빌드를 관리하며 (NPM은 Bower, Gulp 의존성 설치를 위함) Back-end에서는 Maven을 사용하여 관리한다.

빌드 순서는 Front-end ---> Back-end 이며 다음과 같다.
1. > npm install (Bower, Gulp 다운로드 및 설치)
2. > bower install (Bower 의존성 설치. 생략가능. offline 고려하여 미리 넣어둠)
3. > gulp (index.html에 .js, .css 파일과 Bower 의존성을 injection. 부가로 Linting도 실행)
4. > mvn clean package (src/main/webapp의 Front-end와 Back-end를 .war로 묶음)

개발상황에서는 주로 2, 3번 사항이 요구된다. 4번은 CI 서버에서 수행하며 1번은 최초 빌드환경 구성 시만 필요하다.