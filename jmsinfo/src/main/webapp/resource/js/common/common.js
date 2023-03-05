function doGet(url, param, successCallback, errorCallback = error(err)) {

  console.log("doGet Start");
  console.log("param >>>>> ", param);
  console.log("url >>>>>", url);
  if(successCallback == null){
    alert('성공함수 넣어주세요')
    return;
  }
  
  $.ajax({
    type: 'get',
    url: url,
	  contentType:'application/json',
    data: JSON.stringify(param),
    success : function(result) { // 결과 성공 콜백함수
      console.log("result >>>>>>>>", result);
      return successCallback(result);
    },
    error: function (err) {
      console.log('에러 발생')
      return errorCallback(err);
    }
  });
}

function doPost(url, param, successCallback, errorCallback = error()) {
  
  const localAT = localStorage.getItem("accessToken");
  
  if(localAT == null) {
    alert("Access 토큰 없음 재 로그인 필요");
    return;
  }
  
  const decodeAT = JSON.parse(decodeToken(localAT));

  let httpHeader = null;

  if(decodeAT.exp < Date.now()/1000 + 60){
    httpHeader = {
      'Authorization': localStorage.getItem('accessToken'),
      'Refresh': localStorage.getItem('refreshToken'),
    }
  }else{
    httpHeader = {
      'Authorization': localStorage.getItem('accessToken'),
    }
  }

  $.ajax({
    type: "post",
    url: url,
    dataType : "json",
    contentType: "application/json; charset=utf-8",
    data : JSON.stringify(param),
    headers : httpHeader,
    success : function (result, textStatus, request){
      console.log(result);
      const reqAT = request.getResponseHeader("authorization");
      
      if(reqAT != localAT && reqAT != null){
        localStorage.setItem("accessToken", reqAT)
      }
      
      return successCallback(result, textStatus, request);
    },
    error : function(err){
      console.log("에러 발생");
      return errorCallback(err);
    }
  });

}

function doPostNoToken(url, param, successCallback, errorCallback = error()) {

  $.ajax({
    type: "post",
    url: url,
    dataType : "json",
    contentType: "application/json; charset=utf-8",
    data : JSON.stringify(param),
    success : function (result, textStatus, request){
      return successCallback(result, textStatus, request);
    },
    error : function(err){
      console.log("에러 발생");
      return errorCallback(err);
    }
  });

}

function error() {
  alert('시스템 오류입니다. 관리자에게 문의 바랍니다.')
}

// JWT 복호화
function decodeToken(enToken) {
  
  var base64Url = enToken.split('.')[1];
  var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');

  var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return jsonPayload;
}