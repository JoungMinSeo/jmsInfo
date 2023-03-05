<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>회원가입</title>
  <script type="text/javascript">
    var SBUxConfig = {
      Path : "../../../resource/sbux",
      SBGrid : {	
        Theme : 'default',	
        Version2_5 : true,
      },
      SBChart : {	
        Version2_0 : true	
      },
    };
    </script>
  
  <script src="../../../resource/sbux/SBUx.js" type="text/javascript"></script>
  <script src="../../../resource/js/common/common.js"></script>
  <script src="../../../resource/js/user/user.js"></script>
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
</head>
<body>
  <div class="inputUser" style="width: 600px;">
    <br>
    <sbux-label id="idxLabel_userId" name="userId" uitype="normal" text="ID" style="font-size: large;">
    </sbux-label>
		<sbux-input id="userId" name="userId" uitype="text"></sbux-input>

    <sbux-label id="idxLabel_userPassword" name="userPassword" uitype="normal" text="비밀번호" style="font-size: large;">
    </sbux-label>
		<sbux-input id="userPassword" name="userPassword" uitype="password"></sbux-input>

    <sbux-label id="idxLabel_userName" name="userName" uitype="normal" text="이름" style="font-size: large;">
    </sbux-label>
		<sbux-input id="userName" name="userName" uitype="text"></sbux-input>

    <sbux-button id="btn_signIn" name="btn_signUp" uitype="normal" text="회원가입" onclick="signUp()">
		</sbux-button>
  </div>
</body>
</html>

