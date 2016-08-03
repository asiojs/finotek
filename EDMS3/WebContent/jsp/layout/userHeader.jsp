<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header class="header">
	<!-- TODO :: 로고 클릭 안됨 -->
	<div class="logo">
		<h1><a href="javascript:void(0);" onclick="javascript:exsoft.util.layout.goUserContent('${contextRoot}/user/mainContent.do');"><img src="${contextRoot}/img/logo.png"></a></h1>
	</div>
	<div class="header_menu_wrapper">
		<div class="header_icon">
			<ul class="header_icon_menu">
				<li id="myDocMenuSelected">
					<a href="javascript:exsoft.util.layout.goUserContent('${contextRoot}/mypage/myPageDocList.do')" class="page" id="myDocMenu"></a>
					<div class="hide">나의 문서</div>
				</li>
				<li id="myWorkMenuSelected">
					<a href="javascript:exsoft.util.layout.goUserContent('${contextRoot}/document/myDocList.do')" class="user" id="myWorkMenu"></a>
					<div class="hide">개인문서함</div>
				</li>
				<li id="workSpaceMenuSelected">
					<a href="javascript:exsoft.util.layout.goUserContent('${contextRoot}/document/workDocList.do')" class="inbox" id="workSpaceMenu"></a>
					<div class="hide">업무문서함</div>
				</li>
				<li id="workProcessMenuSelected">
					<a href="javascript:exsoft.util.layout.goUserContent('${contextRoot}/process/processLayout.do')" class="usergroup" id="workProcessMenu"></a>
					<div class="hide">협업업무</div>
				</li>
				<li id="statisticsMenuSelected">
					<a href="javascript:exsoft.util.layout.goUserContent('${contextRoot}/statistics/statisticsMenu.do?type=myStatistics')"  class="piechart" id="statisticsMenu"></a> 
					<div class="hide">통계</div>
				</li>
			</ul>
		</div>
		<div class="header_user">
			<ul class="header_user_menu">			
				<li class="user_info">
					<a href="javascript:void(0);"><img src="${contextRoot}/img/icon/user_info.png" alt="">
						${user_name}님(${role_nm})
						<img src="${contextRoot}/img/icon/head_dropdown.png" class="head_dropDown">
					</a>
					<div class ="info_dropDown_menu hide">
						<div class="tooltip_arrow"></div>
						<div>
							<ul>
								<li><a href="javascript:exsoftLayoutFunc.open.userConfig('myinfo');">내정보</a></li>
								<li><a href="javascript:exsoftLayoutFunc.open.userConfig('passwdConf');">보안 설정</a></li>
							</ul>
						</div>
					</div>
				</li>
				<li class="user_note">
					<a href="javascript:void(0);">
						<img src="${contextRoot}/img/icon/user_note.png" alt="">	
						<span class="num_cnt" id="topNoteNewCnt"></span>		
					</a>			
					<div class="note_dropDown_menu hide">
						<div class="tooltip_arrow"></div>
						<ul id="newNoteList"></ul>
					</div>
				</li>
				<li class="user_inbox">
					<a href="javascript:exsoft.util.layout.goUserContent('${contextRoot}/mypage/myPageDocList.do?myMenuType=TEMPDOC')">
						<img src="${contextRoot}/img/icon/user_inbox.png" alt="">
						<span class="num_cnt" id="tempDocNewCnt"></span>
					</a>
				</li>
				<li class="user_setting">
					<a href="javascript:exsoftLayoutFunc.open.userConfig('myconfig');">
						<img src="${contextRoot}/img/icon/user_setting.png" alt="">
					</a>
				</li>
				<li class="user_logout">
					<a href="javascript:void(0);" onclick="exsoft.util.layout.logout('${contextRoot}/logout.do')">
						<img src="${contextRoot}/img/icon/user_logout.png" alt="">
					</a>
				</li>
			</ul>
		</div>
	</div>
</header>
<!--  환경설정 Target -->
<form name="popFrm">
	<input type="hidden" name="tabType">
</form>

<form name="popNoteFrm">
	<input type="hidden" name="tabType">
</form>

<form name="adminFrm">
	<input type="hidden" name="tabType">
</form>

<script type="text/javascript">
jQuery(function() {
	
	// 헤더 쪽지 및 작업카트 목록 표시하기	
	exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({'new_count' : Constant.ALLDATA},'${contextRoot}/note/noteTopNSelect.do', 
			'#topNoteNewCnt', exsoftLayoutFunc.callback.noteInfo);			// 새쪽지개수 및 목록
			
	exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({'LIST_TYPE' : Constant.LIST_TYPE.DOCUMENT_LIST_TYPE_TEMPDOC},'${contextRoot}/mypage/authDocumentList.do', 
		'#tempDocNewCnt', exsoftLayoutFunc.callback.infoCount);				// 작업카트개수
});
</script>