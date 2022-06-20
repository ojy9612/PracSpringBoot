# hanghae99Homework02


### 프레임워크와 라이브러리의 차이점
- 프레임워크는 IOC가 일어나 프레임워크에 맞춰서 개발자가 개발을 해야한다.
- 라이브러리는 IOC가 일어나지 않는다.

### 코드를 구현할때 예외처리를 위해 무엇을 했나요?
- 예외를 직접 만들고 에러코드도 직접만들어서 프론트가 작업하기 쉽게 만들었다.


### Restful이란?
- 올바르게 REST API를 설계하는것.
REST API는 http통신에 CRUD요청을 Method로 표현해 특정 방식으로 전달하는 방식.

### 왜 Restful하게 짜야하나요?
- 하나의 서버로 다양한 client를 수용하기 위해서 Restful하게 코드를 짜야합니다.

### Restful의 장/단점
1. **장점**
- 알아보기 쉬워진다. API의 이름이 굉장히 직관적이여서 한눈에 무슨 동작을 하는지 알 수 있다.
2. **단점**
- 누군가 공식적으로 발표한 방식이 아니기 때문에 사람마다 다르게 해석할 수 있다.
- 서비스와 복잡해질수록 Over-Fetching 과 Under-Fetching 문제가 발생할 수 있다.
- Over-Fetching : user_id만 필요하지만 user전체를 전송하게됨.
- Under-Fetching : 원하는 데이터를 위해 여러API를 호출함.
- HTTP에 의존적이다. Rest는 point-to-point 통신모델(Web)을 기본으로하므로 서버-클라이언트가 연결을 유지한채 상호작용하는 어플리케이션 개발에는 적당하지 않다. 
- CRUD를 제외한 처리는 힘들다.

### Restful의 대안은?
- GraphQL은 Rest API의 단점을 보안하기 위해 나왔다.
- Over-Fetching 과 Under-Fetching 문제를 큰 자원소모 없이 해결할 수 있다.
### Restful하게 짜기 위해 무엇을 고려했나요?
- API Url을 통일시키고 Delete,Put 같은 Method를 적극적으로 사용했다.
- 대문자를 사용 X, 동사 포함 X, 마지막에 '/'사용 X, 확장자 사용 X 등 API명에 신경썼다.
- Content-Type : application/json 을 우선으로 사용했고 통일시켰다.

### Entity 설계를 위해 무엇을 하였나요? 연관관계에 근거하여 설명해주세요.
- Like기능은 Board와 User의 N:N관계라는 것에 기반해 Like Table을 만든 뒤 1:N 으로 Like와 Board,Uesr를 연결시켜주었다.
N:N으로 설계시 Talbe의 책임분리가 안된다. 그리고 JPA를 사용하는데에 어려움이 있어 실무에서는 사용하지 않는다고 들었다.


### 번외. **CORS 해결하기**
- CORS는 한 도메인이 다른 도메인 (도메인 간 요청)을 가진 리소스에 액세스 할 수 있게하는 보안 메커니즘이다. CORS는 동일한 출처의 리소스에만 접근하도록 제한한다. 여기서 출처는 프로토콜, 호스트명, 포트가 같다는 것을 의미한다.
- CORS를 가장 쉽게 적용시키는 방법은 JWT토큰을 사용하는 것이라고 생각해 Session대신 Jwt를 적용했다.
- jwt만 적용하면되는지 알았는데 CorsConfigurationSource 함수를 Bean에 등록한뒤 프론트의 서버를 등록해줘야 한다.("\*"로 모든 접속이 가능하게 했다.)


# 트러블슈팅!
### 1. 스파르타 강의에서는 UserDetailsImpl에 User 객체를 넣어서 관리 했지만 객체를 넣을 경우 토큰인증을 할 때 마다 db쿼리문이 2번씩 발생하는 문제가 있다.(강의에서 정정하는것을 퀴즈로 내주고 답은 공개하지 않았다.)
- 해결 - UserDetailsImpl의 User객체(id,password,roles)를  String타입으로 바꿔서 저장했다.

### 2. (should be mapped with insert="false" update="false") 에러 board테이블에서 자꾸 해당 에러가 뜬다.
- 해결 - JoinColumn의 name이 중복되서 나타나는 문제였다. name은 내 테이블에 설정한 문자로 컬럼을 생성하라는 뜻으로 상대 테이블과 매핑시켜주는 역할을 하지 않는다.

### 3. (More than one row with the given identifier was found) 에러 게시글을 3개 작성했을 때 나온다.
- 해결 - 게시글과 유저는 OneToMany 관계인데 OneToOne을 사용했을 때 나오는 에러였다..

### 4. 게시글 작성시 userDetailsImpl 객체에서 유저 정보를 받아오지만 Board객체를 생성할때엔 User객체가 필요해 결국 User관련 DB쿼리문이 2번 발생한다. 1번문제 다시 발생..  객체대신 아이디만 저장해야할까?(그럼 외래키는 어떻게 해야하지?)
- 해결 - 현재 프로젝트는 ID만 저장하는 것이 효율적이나 큰 프로젝트를 할 때엔 객체를 저장하는 것이 낫다는 얘기를 들었다. DB가 복잡해질수록 객체를 타고가는 것이 쿼리문을 여러번 날리는 것보다 빠를것이다. (객체로 저장함)

### 5. (No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer) 에러
- 해결 - LAZY를 적용시켜 자식 객체를 불러오기전에 부모 객체를 JSON으로 직렬화시킬때 나는 에러다.문제가 되는 컬럼을 EAGER로 바꾸고 필요 없는 컬럼에 @JsonIgnore 어노테이션을 달아 직렬화되지 않게 바꿔주었다.

### 6. 게시글(Board) 삭제시 좋아요(Likes)객체가 참조중이여서 삭제가 안되는 문제
- 해결 - (orphanRemoval = true) 옵션을 넣어줘서 게시글삭제시 Likes DB의 데이터까지 한번에 삭제되도록 함

### 7. 프로젝트 진행중 이미지업로드 기능을 구현한 후에 게시글 관련해서 에러를 발견했다. 이미지 업로드기능을 가지고 에러를 찾기엔 너무 귀찮은것 같다.
- 해결 - 프로젝트를 복사한뒤 커밋 되돌리기 기능으로 에러를 찾았다.

### 8. 이미지파일과 JSON을 한번에 받을 수 없다. (가능해도 일단 포스트맨에서 안된다.)
- 해결 - @ResponseBody 대신 @RequestParam으로 통일해서 데이터를 받아온다.

### 9. 예외처리시 적절한 이름을 고르기 어려웠다.
- 해결 - 예외를 직접 만들고 예외코드도 만들어서 적용시켰다. 

### 10. OneToMany의 관계에 있는 객체들중 서로를 참조시키는 addBoard같은 함수가있다. 이 함수들을 생성자에 넣어서 바로 실행되게 코드를 짰는데 올바른 방법인가? 편하고 신경안써도 되서 좋긴하다.

### 11. 좋아요 기능을 하나의 API로 만들어서 호출시 ON OFF를 번갈아가면서 하게 해주었다. 책임분리 해야할까? 


