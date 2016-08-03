<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/registGroupWindow.js"></script>
<!-- 
	등록버튼 클릭, 하위부서 추가
 -->
<div class="subDept_add_wrapper hide"></div>
<div class="subDept_add hide">
	<div class="window_title">
		하위부서 추가
        <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="subDept_add_cnts">
    	<form id="p_group_form">
	    	<table>
	    		<colgroup>
	    			<col width="150"/>
	    			<col width="350"/>
	    		</colgroup>
	    		<tr>
	    			<th>부서명</th>
	    			<td><input type="text" id="p_group_name_ko" data-bind="p_group_name_ko"  class="" placeholder="부서명을 입력하세요" onchange="registGroupWindow.event.groupNameChanged();" maxlength="32"></td>
	    		</tr>
	    		<tr>
	    			<th>부서명(영문)</th>
	    			<td><input type="text" id="p_group_name_en" data-bind="p_group_name_en" placeholder="부서명(영문)을 입력하세요" maxlength="32"/></td>
	    		</tr>
	    		<tr>
	    			<th>정렬순서</th>
	    			<td><input type="text" id="p_sort_index" data-bind="p_sort_index" maxlength="4" placeholder="숫자를 입력해주세요" onkeydown="return exsoft.util.filter.numInput(event);" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')"></td>
	    		</tr>
	    		<tr>
	    			<th>사용여부</th>
	    			<td>
	    				<select id="p_group_status" data-bind="p_group_status">
	    					<option value="C">사용</option>
	    					<option value="D">미사용</option>
	    				</select>
	    			</td>
	    		</tr>
	    		<tr>
	    			<th>부서경로</th>
	    			<td>
	    			<input type="text" id="p_group_full_path" data-bind="p_group_full_path" class="readonly" placeholder="" readonly="readonly" >
	    			<button type="button" onclick="javascript:registGroupWindow.event.selectGroup();" class="btn1" ><span>선택</span></button>
	    			</td>
	    		</tr>
	    	</table>
    	</form>
    </div>
   	<div class="btnGrp">
   		<button type="button" id="" class="subDept_add_btn bold" onclick="javascript:registGroupWindow.event.registGroupSubmit();">확인</button>
   		<button type="button" id="" class="subDept_cancel_btn cancel" onclick="javascript:registGroupWindow.close();">취소</button>
   	</div>
</div>