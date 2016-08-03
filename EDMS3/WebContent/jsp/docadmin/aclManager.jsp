<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--
	권한관리
	권한등록/권한복사/ACL사용자추가화면 포함 :: 기존 사용자 공통화면이 있는 경우 공통화면을 사용하세요 
 -->
<div class="cnt_wrapper">
	<div class="depth_navigation">
		문서관리 &gt; 권한(ACL) 관리
	</div>
	
	<div class="acl_sub_wrapper">
		<div class="sub_left">
			<div class="srch_area">
				<div class="srch_form">
					<form>
						<label class="srch_auth_lbl">공개범위
						</label>
							<select id="strIndex">
									<option value="">전체</option>
									<option value="ALL">전사</option>
									<option value="TEAM">부서</option>
									<option value="DEPT">하위부서포함</option>
									<option value="PRIVATE">사용자</option>
							</select>
						<label class="srch_auth_lbl">공개대상
							<input type="text" class="srch_auth_keyword" id="strKeyword1">
						</label>
						<label class="srch_owner_lbl">권한명
							<input type="text" class="srch_auth_keyword" id="strKeyword2">
						</label>
						<button type="button" class="srch_auth_btn bold" onclick="aclManager.event.searchAclList();">검색</button>
					</form>
				</div>
				<div class="srch_result_wrapper">
					<div class="left">
						<button type="button" class="register_auth_btn">등록</button>
						<button type="button" class="delete_auth_btn">삭제</button>
						<button type="button" class="copy_auth_btn">복사</button>
						<div class="menu_imgBtnGrp1">
							<a href="#" class="menu_refresh"><img src="${contextRoot }/img/icon/menu_refresh.png" alt="" title=""></a>
						</div>
					</div>
					<div class="srch_result" id="targetAclGridList">
						<table id="aclGridList">
						</table>
					</div>
				</div>
			</div>
			<div class="pg_subWrapper">
				<div class="pg_navi_wrapper">
					<ul id="aclGridPager" class="pg_navi">
						<!-- 문서목록 Pager -->
					</ul>
	           	</div>	
			</div>
		</div>
		
		<div class="sub_right">
			<div class="acl_config_info">
				<input type="hidden" name="type" id="type" value='update'> 
				<input type="hidden" id="creator_id">
				<input type="hidden" name='acl_id' id="acl_id" /> 
				<input type="hidden" name="aclItemArrayList" id='aclItemArrayList' />
				<input type='hidden' name='open_id' id='open_id' /> 
				<input type='hidden' name='open_isgroup' id='open_isgroup' /> 
				<input type='hidden' name='sort_index' id='acl_sort_index'/>
				<input type='hidden' name='src_acl_name' id='src_acl_name'/>
				
				<table>
					<tr>
						<th><span class="bold" id="acl_name_title"></span></th>
						<td colspan="3">
							<button type="button" id="" name="" class="delete_auth_btn">삭제</button>
							<button type="button" id="" name="" class="copy_auth_btn">복사</button>
						</td>
					</tr>
					<tr>
						<th>권한명</th>
						<td colspan="3">
							<input type="text" id="acl_name" name="acl_name" class="doc_authName">
						</td>
					</tr>
					<tr>
						<th>공개범위</th>
						<td colspan="3">
							<select id="acl_type">
								<option value="ALL">전사</option>
								<option value="TEAM">부서</option>
								<option value="DEPT">하위부서포함</option>
								<option value="PRIVATE">공유안함</option>
							</select>
							<input type="text" id="open_name" name="open_name" class="doc_openRange readonly" readonly="readonly">
							<button type="button" id="" class="">선택</button>
						</td>
					</tr>
					<tr>
						<th>등록자</th>
						<td><label id="creator_name"></label></td>
						<th>등록일자</th>
						<td><label id="create_date"></label></td>
					</tr>
				</table>
				
			</div>
			
			<div class="acl_config_list">
				<div>
					<button type="button" id="" name="" class="add_aclUser btn">추가</button>
					<button type="button" id="" name="" class="delete_aclUser btn">삭제</button>
				</div>
				
				<div class="acl_config_tbl">
					<table id="aclItemGridList"></table>
				</div>
			</div>
		</div>
	</div>
	<div class="btnGrp">
		<button type="button" class="btn1 bold">저장</button>
		<button type="button" class="btn1">취소</button>
	</div>
</div>
<script type="text/javascript" src="${contextRoot}/js/docadmin/aclManager.js"></script>
<script type="text/javascript">
jQuery(function() {		
		
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	aclManager.init("${pageSize}");
});
</script>
<!-- ACL 등록 시작 -->
<div class="register_acl_wrapper hide"></div>
<div class="register_acl hide">
	<div class="window_title">
		권한 등록
        <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="register_acl_cnts">
    	<table>
    		<tr>
    			<th>권한명<span class="required">*</span></th>
    			<td colspan="3"><input type="text" id="" name="" class=""></td>
    		</tr>
    		<tr>
    			<th>공유범위<span class="required">*</span></th>
    			<td colspan="3">
    				<p>
     				<label><input type="radio" name="" class="" value="">공유안함</label>
     				<label><input type="radio" name="" class="" value="">부서공유</label>
     				<label><input type="radio" name="" class="" value="">하위부서공유</label>
     				<label><input type="radio" name="" class="" value="">전사공유</label>
    				</p>
    				<input type="text" id="" name="" class="acl_folder_choose readonly" readonly="readonly">
    				<button type="button" id="" class="acl_folderChoose_btn">선택</button>
    			</td>
    		</tr>
    		<tr>
    			<th>등록자</th>
    			<td>홍길동</td>
    			<th>등록일자</th>
    			<td>2014.08.01</td>
    		</tr>
    	</table>
    	<div class="register_acl_btnGrp">
    		<button type="button" id="" class="add_aclUser_popup">추가</button>
    		<button type="button" id="" class="remove_aclUser_popup">제외</button>
    	</div>
    	<div class="register_acl_field">
    		** jqGrid 영역 **
    		<table id=""></table>
    	</div>
    </div>
   	<div class="btnGrp">
   		<button type="button" id="" class="register_acl_btn bold">저장</button>
   		<button type="button" id="" class="cancel_acl_btn cancel">취소</button>
   	</div>
</div>
<!-- ACL 등록 끝 -->
		
<!-- ACL 복사 시작 -->
<div class="copy_acl_wrapper hide"></div>
<div class="copy_acl hide">
	<div class="window_title">
		권한 복사
        <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="copy_acl_cnts">
    	<table>
    		<tr>
    			<th>권한명<span class="required">*</span></th>
    			<td colspan="3"><input type="text" id="" name="" class=""></td>
    		</tr>
    		<tr>
    			<th>공유범위<span class="required">*</span></th>
    			<td colspan="3">
    				<p>
     				<label><input type="radio" name="" class="" value="">공유안함</label>
     				<label><input type="radio" name="" class="" value="">부서공유</label>
     				<label><input type="radio" name="" class="" value="">하위부서공유</label>
     				<label><input type="radio" name="" class="" value="">전사공유</label>
    				</p>
    				<input type="text" id="" name="" class="acl_folder_choose readonly" readonly="readonly">
    				<button type="button" id="" class="acl_folderChoose_btn">선택</button>
    			</td>
    		</tr>
    		<tr>
    			<th>등록자</th>
    			<td>홍길동</td>
    			<th>복사일자</th>
    			<td>2014.08.01</td>
    		</tr>
    	</table>
    	<div class="copy_acl_btnGrp">
    		<button type="button" id="" class="add_aclUser_popup">추가</button>
    		<button type="button" id="" class="remove_aclUser_popup">제외</button>
    	</div>
    	<div class="copy_acl_field">
    		** jqGrid 영역 **
    		<table id=""></table>
    	</div>
    </div>
   	<div class="btnGrp">
   		<button type="button" id="" class="copy_acl_btn bold">저장</button>
   		<button type="button" id="" class="cancel_acl_btn cancel">취소</button>
   	</div>
</div>
<!-- ACL 복사 끝 -->

<!-- ACL 사용자 추가 시작 -->
<div class="aclUser_add_wrapper hide"></div>
<div class="aclUser_add hide">
	<div class="window_subtitle">
		권한 설정
        <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close1.png" alt="" title=""></a>
    </div>
    <div class="aclUser_add_cnts">
    	<div class="aclUser_left">
    		<div class="aclUser_user_wrapper">
     		<div class="aclUser_group">
     			<div class="title center">그룹</div>
     		</div>
     		<div class="aclUser_users">
     			<div class="title center">사용자</div>
     		</div>
    		</div>
    		<div>
    			<div class="aclUser_srch_form">
    				<div>
    					<form id="">
    						사용자/부서
    						<input type="text" id="" name="" class="">
     					<button type="button" id="" class="">검색</button>
    					</form>
    				</div>
    				<table id="">
    					<tr>
    						<th>1</th>
    						<th>2</th>
    						<th>3</th>
    					</tr>
    					<tr>
    						<td>1</td>
    						<td>2</td>
    						<td>3</td>
    					</tr>
    				</table>
    			</div>
    		</div>
    	</div>
    	<div class="aclUser_center">
    		<div>
    			<a href="#">
    				<img src="${contextRoot}/img/icon/add_user.png" alt="" title="">
    			</a>
    			<a href="#">
    				<img src="${contextRoot}/img/icon/sub_user.png" alt="" title="">
    			</a>
    		</div>
    	</div>
    	<div class="aclUser_right">
    		<div class="aclUser_apply">
	적용대상
	<table id="">
		<tr>
			<th></th>
			<th></th>
		</tr>
		<tr>
			<td></td>
			<td></td>
		</tr>
	</table>	        		
    		</div>
    		<div class="aclUser_authority">
    			권한설정
	<table id="">
		<tr>
			<th></th>
			<th></th>
		</tr>
		<tr>
			<td></td>
			<td></td>
		</tr>
	</table>
    		</div>
    	</div>
    </div>
   	<div class="btnGrp">
   		<button type="button" id="" class="aclUser_add_btn bold">저장</button>
   		<button type="button" id="" class="aclUser_add_btn cancel">취소</button>
   	</div>
</div>
<!-- ACL 사용자 추가 끝 -->