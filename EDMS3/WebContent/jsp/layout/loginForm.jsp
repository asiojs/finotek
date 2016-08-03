<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%
	// contextPath가 있는 경우 대비
	String contextRoot = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width; initial-scale=1.0">
<title>index</title>
<link href="<%=contextRoot%>/css/common/ecm.css" rel="stylesheet" type="text/css">
<link href="<%=contextRoot%>/css/common/reset.css" rel="stylesheet" type="text/css">
<link href="<%=contextRoot%>/css/plugins/jalert/jquery.alerts.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=contextRoot%>/js/plugins/jquery/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="<%=contextRoot%>/js/plugins/jalert/jquery.alerts.js"></script>  
<script type="text/javascript">

  jQuery(function() {

	  loginLayerPopupWindowResize($(".loading"));
		$(window).resize(function(){		 
			loginLayerPopupWindowResize($(".loading"));
		});
	
		// 관리자 로그인 창닫기	
		$('.admLogin_close').bind("click", function(e){
			$(this).parents('.admLogin').addClass('hide');
			$('.admLogin_wrapper').addClass('hide');
		});
			
		//관리자 로그인 - 창 닫기 : 음영진 부분 클릭 시 닫기
		$('.admLogin_wrapper').bind("click", function(){
			$(this).addClass('hide');
			$('.admLogin').addClass('hide');
		});
		
		// 관리자 로그인 FORM
		$('.adm_login_btn').bind("click", function(){
			var div = $('.admLogin');
			div.removeClass('hide');
			div.prev().removeClass('hide');
			loginLayerPopupWindowResize(div);		
		});
		
		// 관리자 로그인 처리
		$('.admLogin_btn').bind("click", function(){
			
			var objForm = document.admFrm;
			// 로그인처리
			if (objForm.emp_no.value.length == 0) {
	    		jAlert('<spring:message code="login.error.userid" />','확인',0);				
	        	return false;
			  } else if(objForm.user_pass.value.length == 0) {
	    		jAlert('<spring:message code="login.error.password" />','확인',0);				
	        	return false;
	    	} else {
		    	loginProcess('admFrm',contextRoot+'/loginProcess.do');
		    	return false;
	    	}		
		});
		
		
		setCookieValue("${emp_no}");

	});
	  
  var contextRoot = "<%=contextRoot%>";
  
  // 쿠키값 적용하기
  var setCookieValue = function(cookieVal)	{
	  
	  if(cookieVal != '')	{
		$("#frm input[name=emp_no]").val(cookieVal);			
		$("input[name=keepid]:checkbox").prop("checked",true);
	  }
	  
  }
  
  // 유효성 체크
  var checkBlank =  function() {

		var objForm = document.frm;
	    
	    if (objForm.emp_no.value.length == 0) {
	    	jAlert('<spring:message code="login.error.userid" />','확인',0);				
	        return false;
	    } else if(objForm.user_pass.value.length == 0) {
	    	jAlert('<spring:message code="login.error.password" />','확인',0);				
	        return false;
	    } else {
	    	loginProcess('frm',contextRoot+'/loginProcess.do');
	    	return false;
	    }
	};
	

	// 로그인 처리
	var loginProcess = function(formId,urls) {
		
		jQuery.ajax({			
			url: urls,
			global: false,
			type: "POST",
			data: $("#"+formId).serialize(),			
			dataType: "json",
			async:true,
			cache:false,
			clearForm:true,
			resetForm:true,
			success: function(data){
				$(".loading_wrapper").addClass("hide");
				$(".loading").addClass("hide");
				if(data.result == "true") {					
					location.href = contextRoot+"/loginResponse.do";	
				}else {
					alert(data.message);			
				}
				
			},	
			beforeSend:function() {							
				$(".loading_wrapper").removeClass("hide");
				$(".loading").removeClass("hide");
			},
			error: function(response, textStatus, errorThrown){	
				//exsoft.util.error.isErrorChk(response);		
			}
		
		});
		
	}
	
	//로그인 데이터 로딩중 수직가운데 정렬
	function loginLayerPopupWindowResize(obj) {
		var wrap = $('.login_wrapper');
		obj.prev().css({
			height:$(document).height(),
			width:$(document).width()
		});
		obj.css({
			top:(wrap.height()-obj.height())/2,
			left:(wrap.width()-obj.width())/2
		});
	}

	function getCookieValLogin (offset){
	    var endstr = document.cookie.indexOf (';', offset);
	    if (endstr == -1)
	    endstr = document.cookie.length;
	    return unescape(document.cookie.substring(offset, endstr));
	}
	
	function GetCookieLogin(name){
		  var arg = name + '=';
		    var alen = arg.length;
		    var clen = document.cookie.length;
		    var i = 0;
		    while (i < clen) {
			  var j = i + alen;
			  if (document.cookie.substring(i, j) == arg)
				  return getCookieValLogin(j);
			  i = document.cookie.indexOf(' ', i) + 1;
		      if (i == 0) break;
		    }
		    return null;
	}
	
	var historyYN = GetCookieLogin('loginHistory');
	if(historyYN != null && historyYN =="Y") history.go(1);
	
	
  </script>
</head>

<body>
	<div class="login_wrapper">
		<div class="company_bg">
			<span class="company_title"></span>
		</div>
		<div class="login_sub_wrapper">
			<div class="login_form">
				<span class="login_title">Login</span>
				<form name="frm" id="frm">					
					<input type="hidden" name="login_type" value="user">
					<input type="text" class="emp_no" name="emp_no" placeholder="아이디">
					<input type="password" class="user_pass" name="user_pass" placeholder="패스워드" onkeyup="if(event.keyCode==13) { checkBlank();return false; }"  style="ime-mode:disabled;">
					<label class="idSave_lbl"><input type="checkbox" name="keepid" value="1">ID 저장</label>
					<button type="button" class="login_btn" name="" onClick="checkBlank();">로그인</button>
				</form>
				<button type="button" class="adm_login_btn" name="">관리자 로그인</button>
			</div>
		</div>
		<!-- ajax loading 시작 -->
		<div class="loading_wrapper hide"></div>
	   	<div class="loading hide">
	        <div class="loading_cnts">
	        	<img src="<%=contextRoot%>/img/icon/ajax-loader.gif" alt="" title="">
	        	<p>
	        		데이터 로딩 중입니다.<br>
	        		잠시만 기다려 주세요
	        	</p>
	        </div>
	    </div>
	    <!-- ajax loading 끝 -->
	    
	    <!-- 관리자 로그인 시작 -->
	   	<div class="admLogin_wrapper hide"></div>
	   	<div class="admLogin hide">
	   		<div class="admLogin_title">
	        	<span>관리자 로그인</span>
	        	<a href="#" class="admLogin_close">
	        		<img src="<%=contextRoot%>/img/icon/window_close1.png" alt="" title="">
	        	</a>
	        </div>
	        <div class="admLogin_cnts">
	        	<form name="admFrm" id="admFrm">
	        		<input type="hidden" name="login_type" value="admin">
					<input type="text" name="emp_no" class="adm_empNo" placeholder="아이디" >
					<input type="password" name="user_pass" class="adm_userPass" placeholder="패스워드" value="">
					<button type="button" class="admLogin_btn">관리자 로그인</button>
				</form>
	        </div>
	    </div>
	   	<!-- 관리자 로그인 끝 -->
	</div>
</body>
</html>
