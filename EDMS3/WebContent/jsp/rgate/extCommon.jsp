<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--  
	RGATE 정책설정  
-->
<!-- 
	TODO : 사용자 정책 설정은 3가지 타입으로 UI구현필요 / 좌측부분은 공통입니다.
	캡쳐화면이나 화면정의서 참조 바랍니다.
	1.사용자/부서 || 저장금지 확장자 
	2.사용자/부서 || 프로세스명
	3 사용자/부서 || 삭제 비밀번호 설정
	4.사용자/부서
-->
<div class="rGate_deptUserPolicy_wrapper hide"></div>
	   	<div class="rGate_deptUserPolicy hide">
	   		<div class="window_title">
	   			부서/사용자 정책 설정
	            <a href="#" class="window_close"><img src="/img/icon/window_close.png" alt="" title=""></a>
	        </div>
	        <div class="rGate_deptUserPolicy_cnts">
	        	<div class="deptUserPolicy_sub_wrapper">
	        		<div class="deptUserPolicy_left">
	        			<div class="deptUserPolicy_top">
	        				<div class="dept">
		        				<div class="dept_title">부서</div>
			        			<div class="dept_tree"><!-- Tree 선택 사용자 리스트 --></div>
		        			</div>
		        			
		        			<div class="user">
			        			<div class="selected_user">
			        				<table id=""><!-- 부서원 검색 화면 -->
			        					<colgroup>
			        						<col width="38"/>
			        						<col width="139"/>
			        					</colgroup>
			        					<tr>
			        						<th><input type="checkbox" id="" name="" class="checkAll" value=""></th>
			        						<th>사용자</th>
			        					</tr>
			        				</table>
			        			</div>
		        			</div>
	        			</div>
	        			
	        			<div class="deptUserPolicy_bottom">
	        				<div class="deptUserPolicy_subWrapper">
		        				<div class="deptUser_srch_form">
			        				<form>
			        					<label>
			        						<select class="user_type">
				        						<option value="">사용자</option>
				        						<option value="">부서</option>
				        					</select>
			        						<input type="text" id="" name="" class="srch_txt">
			        					</label>
			        					<button type="button" id="" name="" class="btn">선택</button>
			        				</form>
			        			</div>
			        			<div class="deptUser_srch_result">
			        				<table class="result"><!--  GRID 영역 -->
			        					<tr>
			        						<th><input type="checkbox" id="" name="" class="checkAll" value=""></th>
			        						<th>구분</th>
			        						<th>성명/부서명</th>
			        						<th>ID</th>
			        					</tr>
			        					<tr>
			        						<td><input type="checkbox" name="" class="" value=""></td>
			        						<td>사용자</td>
			        						<td>경영정보팀</td>
			        						<td>10001281</td>
			        					</tr>			        				
			        				</table>
			        			</div>
		        			</div>
		        			
		        			<div class="pg_subWrapper">
		        				<div class="pg_navi_wrapper">
					            	<ul class="pg_navi" id=""><!--  GridPager 영역 --></ul>    
					           	</div>
		        			</div>
	        			</div>
	        		</div>
	        		<div class="deptUserPolicy_center">
	        			<div class="selected_btnGrp1">
		        			<a href="#" class="">
								<img src="/img/icon/add_user.png" alt="" title="">
							</a>
							<a href="#" class="">
								<img src="/img/icon/sub_user.png" alt="" title="">
							</a>
		        		</div>
	        		</div>	        			  
	        		<!-- 
	        			동적으로 변경되는 부분은 /html/admin/document/*.html 을 참조하여 구성한다. 
					-->      		
	        		<div class="deptUserPolicy_right">
	        			<div class="selected_applyUser1">
							<div class="selected_cnts1">
								<div class="selected_sub_wrapper1">
									<table id="">
										<tr><th><input type="checkbox" id="" name="" class="" value=""></th>
										<th>사용자/부서</th>
										</tr>
										<!--  선택한 사용자/부서 목록 -->
										<tr>
											<td><input type="checkbox" id="" name="" class="" value=""></td>
											<td>홍길동</td>
										</tr>
									</table>
								</div>
							</div>
						</div>					
	        			<div class="selected_prohibitedExt1">
							<div class="selected_cnts1">
								<div class="selected_sub_wrapper1">
									<table id="">
									<!-- GRID 영역 -->
										<tr>
											<th><input type="checkbox" id="" name="" class="" value=""></th>
											<th>저장금지 확장자</th>
										</tr>
										<tr>
											<td><input type="checkbox" id="" name="" class="" value=""></td>
											<td>exe</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
						
	        		</div>
	        	</div>
	        </div>
	        <div class="btnGrp">
        		 <button type="button" id="" name="" class="btn1 bold" value="">저장</button>
        		 <button type="button" id="" name="" class="btn1" value="">취소</button>
        	</div>
	    </div>
