<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<script type="text/javascript">
	var SBUxConfig = {
		Path : "../../resource/sbux",
		SBGrid : {	
			Theme : 'default',	
			Version2_5 : true,
		},
		SBChart : {	
			Version2_0 : true	
		},
	};
	</script>

<script src="../../resource/sbux/SBUx.js" type="text/javascript"></script>
<script src="../../resource/js/common/common.js"></script>
<script src="../../resource/js/user/user.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
	<div class="inputUser" style="width: 600px;">
		<!-- 로그인중이 아닐 때에만 Login 버튼이 보임  -> taglib ( security/tags ) 때문에 가능 -->
		<sec:authorize access="isAnonymous()">
			<sbux-input id="userId" name="input_text" uitype="text"></sbux-input>
			<sbux-input id="userPassword" name="input_pw" uitype="password"></sbux-input>
			<sbux-button id="btn_login" name="btn_norm" uitype="normal" text="로그인" onclick="login()"></sbux-button>
		</sec:authorize>
		
		<!-- 로그인 중일 경우에만 Logout 버튼이보임 -->
		<sec:authorize access="isAnonymous()">
			<sbux-button id="btn_logout" name="btn_logout" uitype="normal" text="로그아웃" onclick="logout()"></sbux-button>
		</sec:authorize>
		
		<sbux-button id="btn_agree" name="btn_agree" uitype="normal" text="전자동의서" onclick="agreePage()">
		</sbux-button>
		<sbux-button id="btn_signIn" name="btn_signUp" uitype="normal" text="회원가입" onclick="signUpPage()">
		</sbux-button>
	</div>
</body>
</html>