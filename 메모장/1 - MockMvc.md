# MockMvc

### 프론트 스택 

```text
// SSR -> jsp, thymeleaf
        // -> html rendering
// SPA -> vue
        // -> javascript + <-> API (JSON)
```

### MockMvc?

- `Spring Framework`에서 제공하는 테스트 유틸리티
- Spring MVC 웹 애플리케이션의 컨트롤러 계층을 테스트하기 위해 사용
- 실제 웹 서버를 실행하지 않고도 컨트롤러에 대한 요청과 응답을 시뮬레이션 
- 해당 기능을 통해 빠르고 효율적으로 웹 애플리케이션 기능을 검증 가능 

### MockMvc 주요 기능 

- 애플리케이션 컨텍스트 사용
  - `MockMvc`는 스프링 애플리케이션 컨텍스트 내에서 작동 
  - 스프링 빈 및 의존성 주입, 보안 설정 등 스프링의 다양한 기능 테스트 가능 
- 다양한 HTTP 요청 시뮬레이션 
  - `MockMvc`는  `GET, POST, PUT, DELETE` 등의 다양한 HTTP 요청을 시뮬레이션이 가능 
  - 요청의 헤더, 본문, 파라미터 등을 설정할 수 있다. 
- 응답 검증 
  - 응답의 상태 코드, 헤더 본문 등의 내용을 쉽게 검증할 수 있는 API 제공 

### 간단한 Controller 생성 

```java
@RestController
public class PostController {
    @GetMapping("/posts")
    public String get(){
        return "Hello World";
    }
}
```

### 테스트 환경에서 MockMvc 주입 방법 

```java
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
}    
```
- `@SpringBootTest` + `@AutoConfigureMockMvc`
  - 전체 스프링 애플리케이션 컨텍스트를 로드하고 통합 테스트를 수행
  - 일반적으로 여러 계층(예: 서비스, 리포지토리, 보안 등)을 포함하는 통합 테스트에 사용
  - 애플리케이션의 모든 빈을 로드
  - 전체 애플리케이션의 다양한 계층을 포함한 종단 간 테스트가 가능
  - 실제 애플리케이션과 거의 유사한 환경에서 테스트가 이루어지기 때문에 테스트 환경에 가까운 조건에서 테스트하고자 할 때 유용

```java
@WebMvcTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
}    
```
- `@WebMvcTest`
   - 컨트롤러 계층만을 테스트하기 위한 슬라이스 테스트
   - `@Controller`, `@ContollerAdvice`, `@JsonComponent` 등 웹과 관련된 컴포넌트만 로드 
   - 웹 계층만을 테스트하기 때문에 애플리케이션의 전체 컨텍스트가 아닌 컨트롤러 관련 빈만 로드
   - 이를 통해 컨트롤러 로직에 대한 집중적인 테스트가 가능

### 생성한 컨트롤러 MockMvc 활용 테스트 

```java
@WebMvcTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    @DisplayName("/posts 요청시 Hello World 출력")
    void test() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"));
    }
}
```
- `mockMvc.perform(MockMvcRequestBuilders.get("/posts"))`
  - `MockMvc`를 사용하여 `/posts` 경로에 대한 GET 요청을 시뮬레이션 
- `.andExpect(MockMvcResultMatchers.status().isOk())`
  - 시뮬레이션 요청에 대한 응답 상태 코드가 `200 OK` 인지 확인 
- `.andExpect(MockMvcResultMatchers.content().string("Hello World"))`
  - 시뮬레이션된 요청에 대한 응답 본문(body)가 `Hello World` 문자열을 포함하는지 검증 

### HTTP 요청에 대한 summary 출력하기 

```java
@WebMvcTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    @DisplayName("/posts 요청시 Hello World 출력")
    void test() throws Exception {
        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print()); // summary 출력 
    }
}
```
- `.andDo(MockMvcResultHandlers.print());`

실행 결과 
```text
MockHttpServletRequest:
      HTTP Method = GET
      Request URI = /posts
       Parameters = {}
          Headers = []
             Body = null
    Session Attrs = {}

Handler:
             Type = com.jeulog.controller.PostController
           Method = com.jeulog.controller.PostController#get()

Async:
    Async started = false
     Async result = null

Resolved Exception:
             Type = null

ModelAndView:
        View name = null
             View = null
            Model = null

FlashMap:
       Attributes = null

MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [Content-Type:"text/plain;charset=UTF-8", Content-Length:"11"]
     Content type = text/plain;charset=UTF-8
             Body = Hello World
    Forwarded URL = null
   Redirected URL = null
          Cookies = []
```

### MockMvc static import 

static import 전 
```java
@Test
@DisplayName("/posts 요청시 Hello World 출력")
void test() throws Exception {
    // expected
    mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("Hello World"))
            .andDo(MockMvcResultHandlers.print());
}
```
- `MockMvcRequestBuilders`
- `MockMvcResultMatchers`
- `MockMvcResultHandlers`

static import 후
```java
@Test
@DisplayName("/posts 요청시 Hello World 출력")
void test() throws Exception {
    // expected
    mockMvc.perform(get("/posts"))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello World"))
            .andDo(print());
}
```