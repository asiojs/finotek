<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width; initial-scale=1.0">

<title>index</title>
<script type="text/javascript">
var contextRoot = "${contextRoot}";
var windowType = 'userConfig';
</script>

<script type="text/javascript" src="${contextRoot}/js/plugins/jquery/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/common.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/include.js"></script>
<script type="text/javascript" src="${contextRoot}/js/config/userConfig.js"></script>

</head>
<script type="text/javascript">
jQuery(function() {
	exsoftUserConfigFunc.init.pageInit('${tabType}','${width}','${height}');
});
</script>    
<body>
  <div class="wrap">
	<!-- 환경설정 시작 -->
   	<div class="config">
   		<div class="config_title">환경설정</div>
        <div class="config_cnts">
        	<div class="tab_menu">
                 <div class="tab_elem_wrapper">
                     <span class="tab_element selected" id="myinfo" onclick="javascript:exsoftUserConfigFunc.event.tabSelectFunc('myinfo');">내정보</span>
                     <span class="tab_element"  id="passwdConf" onclick="javascript:exsoftUserConfigFunc.event.tabSelectFunc('passwdConf');">비밀번호관리</span>
                     <span class="tab_element" id="myconfig" onclick="javascript:exsoftUserConfigFunc.event.tabSelectFunc('myconfig');">기본환경설정</span>
                     <div id="noty"></div>
                 </div>
			</div>
			
			<!-- 내 정보 -->
            <div class="tab_form hide" id="myinfoFrm">
            	<table>
            	<colgroup>
            		<col width="74"/>
            		<col width="79"/>
            		<col width="495"/>
            	</colgroup>
            	<tr>
            	<th rowspan="2">성명</th>
            	<td>국문</td>
            	<td><div data-bind="user_nm"></div></td>
            	</tr>
            	<tr class="line">
            	<td>영문</td>
            	<td>
            		<input type="text" data-bind="user_name_en" class="input_txt_myInfo" maxlength="30">
            	</td>
            	</tr>
            	<tr>
            	<th rowspan="2">연락처</th>
            	<td>휴대폰</td>
            	<td>
            		<input type="text" data-bind="telephone" class="input_txt_myInfo" ex-valid="phone" ex-display="휴대폰" ex-option maxlength="20">
            	</td>
            	</tr>
            	<tr class="line">
            	<td>이메일</td>
            	<td>
            		<input type="text" data-bind="email" class="input_txt_myInfo" ex-valid="email" ex-display="이메일" ex-option maxlength="32">
            		<!-- <p class="caution">
            			휴대폰, 이메일 주소는 아이디/비밀번호 찾기 시 본인확인 수단으로 사용됩니다.<br>
						본인의 정보가 타인에게 수신되지 않도록 정확한 정보를 입력해주세요. 
            		</p>
            		-->
            	</td>
            	</tr>
            	<tr class="line">
            	<th>부서</th>
            	<td>&nbsp;</td>
            	<td><div data-bind="group_nm"></div></td>
            	</tr>
            	<tr>
            	<th>역할</th>
            	<td>&nbsp;</td>
            	<td><div data-bind="role_nm"></div></td>
            	</tr>
            	</table>
            </div>
            
            <!-- 비밀번호 관리 -->
            <div class="tab_form hide"  id="passwdConfFrm">                      
            	<p class="caution">
            		<span class="caution_title">보안을 위해 아래와 같이 비밀번호를 설정해 주세요.</span><br>
					ㆍ영문+숫자 4자~10자로 사용가능하며, 특수문자 !@#$%^&amp;*()_+|[]{}&quot;;:/?.&gt;,&lt;의 사용이 가능합니다.<br>
					ㆍ영문(대소문자 구분), 특수기호를 혼합하여 비밀번호를 설정하면, 더욱 안전한 비밀번호를 만드실 수 있습니다.
            	</p>
            	<div class="passwd_change">
            		<table>
            		<colgroup>
            			<col width="153"/>
            			<col width="495"/>
            		</colgroup>
            		<tr>
            			<th>아이디</th>
            			<td><input type="text" data-bind="user_id" class="input_txt_passwd readonly" readonly="readonly" ex-valid="require" ex-display="아이디" maxlength="30"></td>
            		</tr>
            		<tr>
            			<th>현재 비밀번호</th>
            			<td><input type="password" class="input_txt_passwd" data-bind="current_pass" ex-valid="require" ex-display="현재 비밀번호" maxlength="10">
					</td>
            		</tr>
            		<tr>
            			<th>새 비밀번호</th>
            			<td><input type="password" class="input_txt_passwd"  name="passwd1" data-bind="passwd1" ex-length="4,10" ex-valid="passwd" ex-display="새 비밀번호" maxlength="10"></td>
            		</tr>
            		<tr>
            			<th>새 비밀번호 확인</th>
            			<td><input type="password" class="input_txt_passwd"  name="passwd2" data-bind="passwd2" ex-length="4,10" ex-valid="passwd" ex-display="새 비밀번호 확인" ex-equalTo="passwd1,passwd2" maxlength="10"></td>
            		</tr>
            		</table>
            	</div>
            </div>
            
            <!-- 기본환경 설정 -->
            <div class="tab_form hide" id="myconfigFrm">
            	<table class="default_setting">
            	<colgroup>
            		<col width="153"/>
            		<col width="495"/>
            	</colgroup>
            	<tr class="line">
            	<th>언어설정</th>
            	<td>
            		<select id="language">
            			<option value="KO">한국어</option>
            			<!-- <option value="EN">영어</option> -->
            		</select>
            	</td>
            	</tr>
            	<tr class="line">
            	<th>스킨설정</th>
            	<td>
            		<a href="javascript:void(0);" class="skin_set" id="themeGray"><img src="${contextRoot}/img/icon/skin_gray.png" alt="" title=""></a>
            		<a href="javascript:void(0);" class="skin_set" id="themeRed"><img src="${contextRoot}/img/icon/skin_red.png" alt="" title=""></a>
            		<a href="javascript:void(0);" class="skin_set" id="themeBlue"><img src="${contextRoot}/img/icon/skin_blue.png" alt="" title=""></a>
            	</td>
            	</tr>
            	<tr class="line">
            	<th>페이지당 문서목록 수</th>
            	<td>
            		<select id=pg_size>
            			<option value="5">5개</option>
						<option value="10" selected>10개</option>
						<option value="15">15개</option>
						<option value="20">20개</option>
						<option value="30">30개</option>
						<option value="50">50개</option>						
            		</select>
            	</td>
            	</tr>
            	<tr class="line">
            	<th>나의문서 표시 기간</th>
            	<td>
            		<select id="doc_search">
            			<option value="1">1년</option>
						<option value="3">3년</option>						
						<option value="5">5년</option>
						<option value="10">10년</option>											
						<option value="0">전체</option>
            		</select>
            	</td>
            	</tr>
            	<tr>
            	<th>본문 보기</th>
            	<td>
            		<label>
            			<input type="radio" name="view_type" value="LIST">
            			<img src="${contextRoot}/img/icon/view_listonly.png" alt="" title="">
            			목록만 보기
            		</label>
            		<label>
            			<input type="radio" name="view_type" value="RIGHT" checked>
            			<img src="${contextRoot}/img/icon/view_horizontal.png" alt="" title="">
            			좌우 분할로 보기
            		</label>
            		<label>
            			<input type="radio" name="view_type" value="BOTTOM">
            			<img src="${contextRoot}/img/icon/view_vertical.png" alt="" title="">
            			상하 분할로 보기
            		</label>            	 
            	</td>
            	</tr>
            	</table>
            </div>
        </div>
        <div class="config_btnGrp">
       		<button type="button" class="config_confirm_btn" onclick="javascript:exsoftUserConfigFunc.event.updateConfigProc();">저장</button>
       		<button type="button" class="config_cancel_btn" onclick="exsoft.util.layout.windowClose();">취소</button>
       	</div>
    </div>
   	<!-- 환경설정 끝 -->
  </div>
</body>
</html>
    