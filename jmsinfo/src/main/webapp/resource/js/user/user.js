// 로그인
function login() {

  if($("#userId").val() == "") {
    alert("아이디를 입력해주세요");
    return;
  }

  if($("#userPassword").val() == "") {
    alert("비밀번호를 입력해주세요");
    return;
  }
  
  const param = {
    "userId" : $("#userId").val(),
    "userPassword" : $("#userPassword").val()
  }

  const success = function(result, textStatus, request) {
    
    console.log(request.getResponseHeader('authorization'));
    console.log(request.getResponseHeader('Refresh'));
  	if(result.resultCode == 200) {
  	  localStorage.setItem("accessToken", request.getResponseHeader('authorization'));
  	  localStorage.setItem("refreshToken", request.getResponseHeader('Refresh'));
  	  $(location).attr("href", "/");
  	  alert("로그인 성공");
  	}else {
  	  alert(result.failMsg);
  	}

  }

  const error = function(err) {
    console.log(err);
  }

  doPostNoToken("http://localhost:8080/user/login", param, success, error);
}

// 전자 동의서 페이지 이동
function agreePage() {
  $(location).attr("href", "/noAuth/agreementPage");
}

// 회원가입 페이지 이동
function signUpPage() {
  $(location).attr("href", "/noAuth/signUpPage");
}

// 회원가입
function signUp() {

  if($("#userId").val() == "") {
    alert("아이디를 입력해주세요");
    return;
  }

  if($("#userPassword").val() == "") {
    alert("비밀번호를 입력해주세요");
    return;
  }

  if($("#userName").val() == "") {
    alert("이름을 입력해주세요");
    return;
  }
  
  const param = {
    "userId" : $("#userId").val(),
    "userPassword" : $("#userPassword").val(),
    "userName" : $("#userName").val(),
    "authority" : "ROLE_USER"
  }

  const success = function(result) {
    alert("회원가입 성공\r\n로그인 진행해주세요.");
    $(location).attr("href", "/");
  }

  const error = function(err) {
    console.log(err);
  }

  doPost("http://localhost:8080/user/insertUser", param, success, error);
}

// 로그아웃
function logout() {

  const success = function(result){
  	if(result.resultCode == 200) {
  	  localStorage.removeItem("accessToken");
	  localStorage.removeItem("refreshToken");
	  alert('로그아웃 성공')
  	}
	
  }

  const error = function(error) {
    
  }


  doPost("http://localhost:8080/user/logout", null, success, error);
}