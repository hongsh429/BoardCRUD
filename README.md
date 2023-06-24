# BoardCRUD

### 수정, 삭제 API의 request를 어떤 방식으로 사용하셨나요?
수정의 경우, @RequestBody를 통해 json 형식 데이터를 받아오는 방식으로 처리했습니다.<br>
또한 수정하고자 하는 게시글은 @PathValiable 로 데이터를 받아왔습니다.<br>
삭제의 경우, @RequestBody에 비밀번호를 json 형식으로 받아오는 방법과,<br>
단순 String 넘겨주는 방식 모두 사용해봤습니다. json으로 넘겨준 비밀번호는 Map<String, Object>를 통해<br>
key-value 쌍으로 받아왔습니다. 삭제의 경우에도 @PathValiable 로 삭제글 번호를 받아왔습니다.<br>
<br><br>

### 어떤 상황에 어떤 방식의 request를 써야하나요?
### RESTful한 API를 설계했나요? 어떤 부분이 그런가요? 어떤 부분이 그렇지 않나요?
기본적으로 url은 매우 단순하고 필요한 정보만 넘기도록 설계했습니다.<br>
대신 다양한 방식의 method를 사용하여 요청 url를 매핑시켰고,<br>
설계조건에 주어진 방식에 맞게 DTO 객체를 만들어 json으로 넘기는 방식으로 진행했습니다.<br>
다만, 선택한 게시글을 조회하는 부분의 설계조건이 지속적으로 사용하던<br>
응답dto객체와의 폼이 맞지 않아, 특정게시글조회 기능만의 dto객체를 만들었는데,<br>
이 부분의 설계조건은 RESTful하지 않다고 생각합니다.
<br><br>

### 적절한 관심사 분리를 적용했나요?
컨트롤러에서 모든 기능을 수행하는 방식이 아닌, 3 layer architecture로 설계하여<br>
controller는 url mapping의 역할을 수행하도록 하였고,<br>
Service에선 실제 비지니스 로직을 구현하였으며,<br>
Repository에선 디비를 관리하도록 설계를 하였습니다.<br>
