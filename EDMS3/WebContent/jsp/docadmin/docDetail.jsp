<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	만기문서관리소유권변경/휴지통관리 상세공통화면 
-->
<div class="sub_right">
	<div class="tab_doc_info">
		<div class="title"><span id="detailTitle" class="title"></span> </div>
		<div>
			<div class="tab_menu">
                 <div class="tab_elem_wrapper">
                     <span class="tab_element selected" id="defaultDoc" onclick="javascript:exsoftAdminDocFunc.ui.tabSelectFunc('defaultDoc');">기본</span>	                     
                     <span class="tab_element" id="historyDoc" onclick="javascript:exsoftAdminDocFunc.ui.tabSelectFunc('historyDoc');">이력</span>
                 </div>
			</div>
			
			<!-- 기본속성탭 시작 -->
            <div class="tab_form" id="defaultDocFrm">
            	<table>
            		<tr>
            			<th>제목</th>
            			<td colspan="3"><label id="doc_title"></label></td>
            		</tr>
            		<tr>
            			<th>문서유형</th>
            			<td><label id="doc_type"></label></td>
            			<th>버전</th>
            			<td><label id="doc_ver"></label></td>
            		</tr>
            		<tr>
            			<th>보존연한</th>
            			<td><label id="doc_preservation_year"></label></td>
            			<th>보안등급</th>
            			<td><label id="doc_security"></label></td>
            		</tr>
            		<tr>
            			<th>등록자</th>
            			<td><label id="doc_creator_name"></label></td>
            			<th>등록일</th>
            			<td><label id="doc_create_date"></label></td>
            		</tr>
            		<tr id="docUserInfo">
            			<th><label id="doc_owner"></label></th>
            			<td><label id="doc_owner_name"></label></td>
            			<th><label id="doc_date"></label></th>
            			<td><label id="doc_waste_date"></label></td>
            		</tr>
            		<tr>
            			<th>설명</th>
            			<td colspan="3">
            				<span style='display: none'><TEXTAREA id="vtemp_content"></TEXTAREA></span> 
							<iframe src="" id="vIframe_editor" name="vIframe_editor" style="border: 0 solid transparent; margin: 0; height: 120px; width: 99%"></iframe>
            			</td>
            		</tr>
            	</table>
            </div>
            <!-- 기본속성탭 종료 -->
            
			<!-- 이력탭 시작 -->
			<div class="tab_form hide"  id="historyDocFrm">
				<div class="archive_wrapper" id="targetDocHistoryList">
					<table id="detail_docHistoryList"></table>							
				</div>
				<!-- TODO - 이력탭 PAGER -->
				<div class="pg_subWrapper">
					<div class="pg_navi_wrapper">
						<ul class="pg_navi" id=""></ul>
					</div>
				</div>
			</div>
			<!-- 이력탭 종료 -->
		</div>
	</div>
		
	<!--  권한속성 시작 -->
	<div class="dropDown_wrapper">
		<a class="dropDown_txt" onclick="javascript:exsoft.util.common.showHide('#aclShowHide','#aclView');"><label><strong><span id="wAclName" class="title"></span></strong></label></a>
		<span class="dropDown_arrow down" id="aclShowHide"></span>
		<div class="dropDown_subWrapper hide" id="aclView">
			<div class="auth_tbl_wrapper">
				<table id="aclDocTable">
       				<thead>
       					<tr><th colspan="5">기본 접근자</th></tr>
         				<tr>
	         				<th>접근자</th>
	         				<th>기본권한</th>
	         				<th>문서등록</th>
	         				<th>반출취소</th>
	         				<th>권한변경</th>
         				</tr>
         			</thead>
         			<tbody></tbody>
         		</table>
				<br/>
				
				<table id="exAclDocTable">
       				<thead>
       					<tr><th colspan="5">추가 접근자</th></tr>
        				<tr>
         				<th>접근자</th>
         				<th>기본권한</th>
         				<th>문서등록</th>
         				<th>반출취소</th>
         				<th>권한변경</th>
        				</tr>
         			</thead>
         			<tbody></tbody>
         		</table>
			</div>
		</div>
	</div>
	<!--  권한속성 종료 -->
		
	<!--  파일속성 시작 -->
	<div class="dropDown_wrapper">
		<a class="dropDown_txt" id="fileShowHide" onclick="javascript:exsoft.util.common.showHide('#fileShowHide','#fileView');"><label><strong>파일</strong></label></a>
		<span class="dropDown_arrow down"></span>
		<div class="dropDown_subWrapper hide" id="fileView">
			<div class="auth_tbl_wrapper">
				<table id="attachFiles">
				<col style="width:5%">
          		<col style="width:55%">
          		<col style="width:25%">
          		<col style="width:15%">
       				<tr>
        				<th>No.</th>
        				<th>파일명</th>
        				<th>크기</th>
        				<th>저장</th>
       				</tr>
       				</table>
			</div>
		</div>
	</div>
	<!--  파일속성 종료 -->
</div>