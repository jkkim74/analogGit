# 파일설명
- .bowerrc : Bower 설치 위치
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
- src/main/resources/templates : 템플릿파일
- src/main/webapp : HTML, JS, CSS 소스파일 및 이미지와 폰트
- src/main/webapp/bower_components : (자동생성) Bower 의존성

# 빌드환경
## Front-end
Bower와 Gulp를 사용하여 Front-end에서 사용하는 의존성을 관리

package.json 변경 시 1회만 실행하면 된다
> npm start (Bower, Gulp 다운로드 및 설치)

bower.json 변경 시 1회만 실행하면 된다
> gulp (linting과 index.html 파일에 의존성을 injection)

## Back-end
Maven을 사용하여 Back-end에서 사용하는 의존성을 관리
> mvn clean package
