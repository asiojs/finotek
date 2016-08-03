<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/selectSingleFolderWindow.js"></script>
<!-- 
	Usage : 
	- 나의 문서 폴더선택 화면
	- 업무문서함/개인문서함 폴더선택 화면
	- 관리자 폴더관리 폴더선택 화면
		
 -->
<script type="text/javascript">
	exsoft.util.common.ddslick("doc_folder_list", 'doc_folder_list', '', 260, selectSingleFolderWindow.callback.changedDocFolderList);
</script>
<div class="folder_choose_wrapper hide"></div>
<div class="doc_folder_choose hide">
	<div class="doc_folder_title">
		폴더 선택 
		<a href="#" class="doc_folder_close" onclick="selectSingleFolderWindow.close();"><img src="${contextRoot}/img/icon/window_close1.png"></a>
	</div>
	<div class="doc_folder_cnts">
		<div class="doc_folder_box">
			<span id="lb_singleFolderWorkspace" class="box_lbl">문서함</span> 
			<select id="doc_folder_list">
				<option value="WORK_MYDEPT">부서</option>
				<option value="WORK_ALLDEPT">전사</option>
				<option value="WORK_PROJECT">프로젝트</option>
			</select>
		</div>
		<div id="singleFolderMypageTree"	class="doc_folder_tree2"></div>
		<div id="singleFolderMydeptTree"	class="doc_folder_tree2 hide"></div>
		<div id="singleFolderAlldeptTree"	class="doc_folder_tree2 hide"></div>
		<div id="singleFolderProjectTree"	class="doc_folder_tree2 hide"></div>
		<div id="singleFolderFreeDocTree"	class="doc_folder_tree2 hide"></div>
		<div class="doc_folder_btnGrp2">
			<button type="button" class="confirm" onclick="selectSingleFolderWindow.event.submit();">확인</button>
			<button type="button" class="cancel" onclick="exsoft.util.layout.divLayerClose('folder_choose_wrapper', 'doc_folder_choose')">취소</button>
		</div>
	</div>
</div>
