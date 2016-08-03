<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/selectMultiUserWindow.js"></script>
<!-- 그룹관리 > 구성원 추가 -->
<div class="grpDeptUser_add_wrapper hide"></div>
   	<div class="grpDeptUser_add hide">
   		<div class="grpDeptUser_add_title">
   			구성원 추가
            <a href="#" class="grpDeptUser_add_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
        </div>
        <div class="grpDeptUser_add_cnts">
	        <div class="grpDeptUser_left">
	        	<div class="grpDeptUser_leftTop">
	        		<div class="grpDeptUser_deptTree1">
<!-- 		        		<div class="grpDeptUser_subtitle"> -->
<!-- 		        			구분  -->
<!-- 		        			<select id="" name="" class=""> -->
<!-- 		        				<option value="">부서</option> -->
<!-- 		        				<option value="">프로젝트</option> -->
<!-- 		        			</select> -->
<!-- 		        		</div> -->
		        		
		        		<!-- 트리 영역 -->
		        		<div id="pop_groupTree" class="tree_cnts"></div>
		        	</div>
<!-- 		        	
		        	<div class="grpDeptUser_deptUserList">
		        		<div class="grpDeptUser_subtitle">
		        			사용자 목록
		        			
		        			<label>
		        				<input type="checkbox" id="" name="" class="" value="">전사 포함
		        			</label>
		        			  
		        		</div>
		        		
		        		그리드 영역
		        		<div>
		        			<table id=""></table>
		        		</div>
		        	</div>
		        	-->
	        	</div>
	        	
	        	<div class="grpDeptUser_leftBottom">
	        		<div class="srch_form right">
	        			<form>
	        				<label>부서원명
	        					<input type="text" id="strKeyword" name="" class="" placeholder="검색어를 입력하세요"
	        					onkeypress="javascript:return selectMultiUserWindow.event.enterKeyPress(event);">
	        				</label>
	        				<button type="button" id="" class="btn2">검색</button>
	        			</form>
	        		</div>
	        		
	        		<!-- 결과 그리드 -->
	        		<div class="srch_result_list">
	        			<table id="pop_userList"></table>
<!-- 		        			<tr> -->
<!-- 		        				<th><input type="checkbox" id="" name="" class=""></th> -->
<!-- 		        				<th>성명/부서명</th> -->
<!-- 		        				<th>사용자/부서ID</th> -->
<!-- 		        			</tr> -->
	        		</div>
	        	</div>
	        	
	        </div>
	        
	        <div class="grpDeptUser_center">
	        	<div class="grpDeptUser_addSubBtnGrp">
	        		<a href="javascript:void(0);" onclick="javascript:selectMultiUserWindow.event.appendUser();"><img src="${contextRoot}/img/icon/add_user.png" alt="" title=""></a>	
	        		<a href="javascript:void(0);" onclick="javascript:selectMultiUserWindow.event.removeUser();"><img src="${contextRoot}/img/icon/sub_user.png" alt="" title=""></a>	
	        	</div>
	        </div>
	        
	        <div class="grpDeptUser_right">
	        	<!-- 그리드 영역 -->
	        	<div class="grpDeptUser_list1">
	        		<table id="memberList"></table>
<!-- 	        			<tr> -->
<!-- 	        				<th><input type="checkbox" id="" name="" class=""></th> -->
<!-- 	        				<th>사용자/부서</th> -->
<!-- 	        			</tr> -->
	        	</div>
	        	
	        	<!-- 권한 영역 -->
<!-- 	        	<div class="grpDeptUser_acl"> -->
<!-- 	        		<table id=""> -->
<!-- 	        			<tr> -->
<!-- 	        				<th colspan="5">권한설정</th> -->
<!-- 	        			</tr> -->
<!-- 	        			<tr> -->
<!-- 	        				<th>구분</th> -->
<!-- 	        				<th>기본권한</th> -->
<!-- 	        				<th>등록</th> -->
<!-- 	        				<th>반출취소</th> -->
<!-- 	        				<th>권한변경</th> -->
<!-- 	        			</tr> -->
<!-- 	        		</table> -->
<!-- 	        	</div> -->
	        </div>
        </div>
        <div class="grpDeptUser_add_btnGrp">
        	<button type="button" id="" class="" onclick="javascript:selectMultiUserWindow.event.submit();">저장</button>
        	<button type="button" id="" class="" onclick="javascript:selectMultiUserWindow.close();">취소</button>
        </div>
    </div>
   	<!-- 구성원 추가 끝 -->