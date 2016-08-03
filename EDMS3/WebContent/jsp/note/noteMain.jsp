<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width; initial-scale=1.0">
<title>Note</title>
<script type="text/javascript">
var contextRoot = "${contextRoot}";
var windowType = 'noteMain';
</script>

<script type="text/javascript" src="${contextRoot}/js/plugins/jquery/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/common.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/include.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/ecm_note.js"></script> <!-- TODO noteMain.js 합친 후 삭제 -->   
<script type="text/javascript" src="${contextRoot}/js/note/noteMain.js"></script>
<%-- 
<script type="text/javascript" src="${contextRoot}/js/note/jquery.tokeninput.js"></script>
<link href="${contextRoot}/js/note/styles/token-input-facebook.css" rel="stylesheet" type="text/css">
<link href="${contextRoot}/js/note/styles/token-input-mac.css" rel="stylesheet" type="text/css">
<link href="${contextRoot}/js/note/styles/token-input.css" rel="stylesheet" type="text/css">
--%>

<script type="text/javascript">

var url = exsoft.contextRoot;
exsoft.user.user_id = "${user_id}";
$(document).ready(function() {	

	exsoftNoteFunc.event.reciveInit();
	
});


<!--
	// 페이지 최초 진입 후 TAB메뉴에 따라서 데이터를 비동기적으로 가져오게 처리한다.
	// TAB 전환시 해당 내용을 비동기적으로 가져온다.
-->

</script>

</head>


<body>
  <div class="wrap">
   	<!-- 쪽지관리 시작 -->
   	<div class="myNote">
   		<div class="myNote_title">쪽지 관리</div>
        <div class="myNote_cnts">
        	<div class="">
        		<div class="myNote_cnts_srch">
					<!-- <form class="myNote_cnts_srchForm" method="post"> -->
					<select id="myNote_srch_type">
						<option value='0'>내용</option>
						<option value='1'>수/발신자</option>
					</select>
					<input type="text" class="myNote_srch_keyword" id="searchKeyword" name="myNote_srch_keyword" placeholder="검색어를 입력하세요">
					<button type="submit" class="myNote_srch_btn" onclick="javascript:exsoftNoteFunc.event.searchKeyword()"></button>
					<!-- </form> -->
				</div>
        		<div class="tab_menu">
	                 <div class="tab_elem_wrapper">
	                     <span class="tab_element" onclick="javascript:exsoftNoteFunc.initAction.noteSelectAction(0);">대화함</span>
	                     <span class="tab_element selected" onclick="javascript:exsoftNoteFunc.initAction.noteSelectAction(1);">받은 쪽지함</span>
	                     <span class="tab_element" onclick="javascript:exsoftNoteFunc.initAction.noteSelectAction(2);">보낸 쪽지함</span>
	                     <span class="tab_element" onclick="javascript:exsoftNoteFunc.initAction.noteSelectAction(3);">쪽지 보관함</span>
	                 </div>
	                 <div class="tab_btn_wrapper1">
	                 	<button type="button" class="myNote_compose" onclick="javascript:exsoftNoteFunc.newOpen.noteCommonFrm('myNoteWrite','noteWriteFrm');">쪽지쓰기</button>
	                 	<button type="button" class="myNote_refresh" onclick="javascript:exsoftNoteFunc.initAction.noteRefresh();" >새로고침</button>
	                 </div>                
	             </div>
	             <div class="tab_form hide" ><!--  대화함 -->
	             	<div class="myNote_sub_cnts" id="TalkTAB">
	             		<table>
	             		<thead>
	             		<tr>
	             		<th class="tooltip">&nbsp;</th>
	             		<th class="subject">보낸사람 / 받는사람 / 내용</th>
	             		<th class="noteDate">날짜</th>
	             		<th class="listMenu">&nbsp;</th>
	             		</tr>
	             		</thead>
	             		<!-- 대화함 List Start -->
	             		<tbody id="noteTalk"></tbody>
	             		<!-- 대화함 List End -->
	             		</table>
	             		
	             		<div class="pg_navi_wrapper" id="talkPageing">
			            	<%-- <ul class="pg_navi">
			                	<li class="first"><a href="#"><img src="${contextRoot}/img/icon/pg_first.png" alt="" title=""></a></li>
			                    <li class="prev"><a href="#"><img src="${contextRoot}/img/icon/pg_prev.png" alt="" title=""></a></li>
			                	<li class="curr"><a href="#">1</a></li>
			                    <li><a href="#">2</a></li>
			                    <li><a href="#">3</a></li>
			                    <li><a href="#">4</a></li>
			                    <li><a href="#">5</a></li>
			                    <li><a href="#">6</a></li>
			                    <li><a href="#">7</a></li>
			                    <li><a href="#">8</a></li>
			                    <li><a href="#">9</a></li>
			                    <li><a href="#">10</a></li>
			                    <li class="next"><a href="#"><img src="${contextRoot}/img/icon/pg_next.png" alt="" title=""></a></li>
								<li class="last"><a href="#"><img src="${contextRoot}/img/icon/pg_last.png" alt="" title=""></a></li>
			                </ul>     --%>
			            </div>
	             	</div>
	             </div>
	             <div class="tab_form"><!--  받은쪽지함 -->
	             	<div class="myNote_sub_cnts" id="ReciveTAB">
	             		<table>
	             		<thead>
	             		<tr>
	             		<th class="tooltip">&nbsp;</th>
	             		<th class="subject">발신자 / 내용</th>
	             		<th class="noteDate">날짜</th>
	             		<th class="listMenu">&nbsp;</th>
	             		</tr>
	             		</thead>
	             		<!-- 받은 쪽지함 List Start -->
	             		<tbody id="noteRecive"></tbody>
	             		<!-- 받은 쪽지함 List End -->	             		
	             		</table>
	             		
	             		<div class="pg_navi_wrapper" id="recivePageing"></div>
	             		
	             	</div>
	             </div>
	             <div class="tab_form hide"><!--  보낸쪽지함 -->
	             	<div class="myNote_sub_cnts" id="SendTAB">
	             		<table>
	             		<thead>
	             		<tr>
	             		<th class="tooltip">&nbsp;</th>
	             		<th class="subject">수신자 / 내용</th>
	             		<th class="noteDate">날짜</th>
	             		<th class="listMenu">&nbsp;</th>
	             		</tr>
	             		</thead>
	             		<!-- 보낸쪽지함 Start -->
	             		<tbody id="noteSend"></tbody>
	             		<!-- 보낸쪽지함 End -->
	             		
	             		</table>
	             		
	             		<div class="pg_navi_wrapper" id="sendPageing">
			            	<%-- <ul class="pg_navi">
			                	<li class="first"><a href="#"><img src="${contextRoot}/img/icon/pg_first.png" alt="" title=""></a></li>
			                    <li class="prev"><a href="#"><img src="${contextRoot}/img/icon/pg_prev.png" alt="" title=""></a></li>
			                	<li class="curr"><a href="#">1</a></li>
			                    <li><a href="#">2</a></li>
			                    <li><a href="#">3</a></li>
			                    <li><a href="#">4</a></li>
			                    <li><a href="#">5</a></li>
			                    <li><a href="#">6</a></li>
			                    <li><a href="#">7</a></li>
			                    <li><a href="#">8</a></li>
			                    <li><a href="#">9</a></li>
			                    <li><a href="#">10</a></li>
			                    <li class="next"><a href="#"><img src="${contextRoot}/img/icon/pg_next.png" alt="" title=""></a></li>
								<li class="last"><a href="#"><img src="${contextRoot}/img/icon/pg_last.png" alt="" title=""></a></li>
			                </ul>     --%>
			            </div>
	             	</div>
	             </div>
	             <div class="tab_form hide"><!--  쪽지보관함 -->
	             	<div class="myNote_sub_cnts" id="SaveTAB">
	             		<table>
	             		<thead>
	             		<tr>
	             		<th class="tooltip">&nbsp;</th>
	             		<th class="subject">수발신자 / 내용</th>
	             		<th class="noteDate">날짜</th>
	             		<th class="listMenu">&nbsp;</th>
	             		</tr>
	             		</thead>	             		
	             		<!-- 쪽지보관함 Start -->
	             		<tbody id="noteSave"></tbody>	             		
	             		<!-- 쪽지보관함 End -->
	             		</table>
	             		
	             		<div class="pg_navi_wrapper" id="savePageing">
			            	<%-- <ul class="pg_navi">
			                	<li class="first"><a href="#"><img src="${contextRoot}/img/icon/pg_first.png" alt="" title=""></a></li>
			                    <li class="prev"><a href="#"><img src="${contextRoot}/img/icon/pg_prev.png" alt="" title=""></a></li>
			                	<li class="curr"><a href="#">1</a></li>
			                    <li><a href="#">2</a></li>
			                    <li><a href="#">3</a></li>
			                    <li><a href="#">4</a></li>
			                    <li><a href="#">5</a></li>
			                    <li><a href="#">6</a></li>
			                    <li><a href="#">7</a></li>
			                    <li><a href="#">8</a></li>
			                    <li><a href="#">9</a></li>
			                    <li><a href="#">10</a></li>
			                    <li class="next"><a href="#"><img src="${contextRoot}/img/icon/pg_next.png" alt="" title=""></a></li>
								<li class="last"><a href="#"><img src="${contextRoot}/img/icon/pg_last.png" alt="" title=""></a></li>
			                </ul>     --%>
			            </div>
	             	</div>
	             </div>
        	</div>
        	<!-- 
	        <div class="myNote_btnGrp">
	        	<button type="button" class="myNote_ok" class="">확인</button>
	        	<button type="button" class="myNote_cancel" class="">취소</button>
	        </div>
	         -->
        </div>        
    </div>
   	<!-- 쪽지관리 끝 -->
   	
	   	<!-- 쪽지 쓰기 시작 -->
	   	<div class="myNoteWrite hide">
	   		<form name="noteWriteFrm" id="noteWriteFrm">
	   		<div class="myNoteWrite_title">
	   			쪽지 쓰기
	            <a href="javascript:void(0);" class="myNoteWrite_close">
	            <img src="${contextRoot}/img/icon/window_close.png" onclick="javascript:exsoft.util.layout.popDivLayerClose('myNoteWrite');"></a>
	        </div>
	        <div class="myNoteWrite_cnts">
	        	<div>
		        	<table>
		        	<colgroup>
		        		<col width="87"/>
		        		<col width="481"/>
		        	</colgroup>
		        	<tr>
		        		<th>받는사람<span class="required">*</span></th>
	        		<td>
	        			<input type="hidden" id="reciverArrayList" class="" value="" name="" >
	        			<input type="text" id="noteReciver" class="" value="" name="noteReciver" onclick="javascript:exsoftNoteFunc.newOpen.userSelect();" >
	        			<button type="button" onclick="javascript:exsoftNoteFunc.newOpen.userSelect();">선택</button>
					</td>
	        	</tr>
	        	<tr>
	        	<th>내용</th>
	        	<td>
	        		<textarea class=""  id="noteContent"  name="vc_message" class="" onKeyUp='javascript:exsoft.util.common.limitStringCheck(this,"1000")'></textarea>
	        		<p class="note_count"><span class="current_count" id="current_textcnt"></span>/1000자</p>
	        	</td>
	        	</tr>
	        	</table>
        	</div>
	        <div class="myNoteWrite_btnGrp">
	        	<button type="button" class="myNoteWrite_confirm" class="" onclick="javascript:exsoftNoteFunc.event.insertNoteWrite('myNoteWrite','noteWriteFrm');">확인</button>
	        	<button type="button" class="myNoteWrite_cancel" onclick="javascript:exsoft.util.layout.popDivLayerClose('myNoteWrite');">취소</button>
	        </div>
        </div>
        </form>
    </div>
   	<!-- 쪽지 쓰기 끝 -->		
   	
   	<!-- 쪽지 전달 시작 -->
   	<div class="myNoteForward hide">
   	<form name="noteForwardFrm" id="noteForwardFrm">
   		<div class="myNoteForward_title">
   			쪽지 전달
   			<a href="javascript:void(0);" class="myNoteForward_close">
            <img src="${contextRoot}/img/icon/window_close.png" onclick="javascript:exsoft.util.layout.popDivLayerClose('myNoteForward');"></a>
        </div>
        <div class="myNoteForward_cnts">
        	<div>
	        	<table>
	        	<colgroup>
	        		<col width="87"/>
	        		<col width="481"/>
	        	</colgroup>
	        	<tr>
	        	<th>
	        		받는사람
	        		<span class="required">*</span>
	        	</th>
	        	<td>
	        		<input type="hidden" id="reciverArrayList" value="" name="" class="">
	        		<input type="text" id="noteReciver" name="" class="" onclick="javascript:exsoftNoteFunc.newOpen.userSelect();">
	        		<button type="button" onclick="javascript:exsoftNoteFunc.newOpen.userSelect();">선택</button>
	        	</td>
	        	</tr>
	        	<tr>
	        	<th>내용</th>
	        	<td>	
	        		<textarea class="" name="" id="noteContent" onKeyUp='javascript:exsoft.util.common.limitStringCheck(this,"1000")'></textarea>
	        		<p class="note_count"><span class="current_count" id="current_textcnt"></span>/1000자</p>
	        	</td>
	        	</tr>
	        	</table>
        	</div>
	        <div class="myNoteForward_btnGrp">
	        	<button type="button" class="myNoteForward_confirm" onclick="javascript:exsoftNoteFunc.event.insertNoteWrite('myNoteForward','noteForwardFrm');" >확인</button>
	        	<button type="button" class="myNoteForward_cancel"  onclick="javascript:exsoft.util.layout.popDivLayerClose('myNoteForward');">취소</button>
	        </div>
        </div>
        </form>
    </div>
   	<!-- 쪽지 전달 끝 -->
   	
   	<!-- 쪽지 답장 시작 -->
   	<div class="myNoteReply hide">
   		<form name="noteReplyFrm" id="noteReplyFrm">
   		<div class="myNoteReply_title">
   			쪽지 답장
            <a href="#" class="myNoteReply_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
        </div>
        <div class="myNoteReply_cnts">
        	<div>
	        	<table>
	        	<colgroup>
	        		<col width="87"/>
	        		<col width="481"/>
	        	</colgroup>
	        	<tr>
	        	<th>
	        		받는사람
	        		<span class="required">*</span>
	        	</th>
	        	<td>
	        		<input type='hidden' id='noteReplyroot_id'  value=''>
	        		<input type='hidden' id='noteReplycreator_id'  value=''>
	        		<input type="text" id="noteReReciver" class="" name="noteReReciver" class="">
	        	</td>
	        	</tr>
	        	<tr>
	        	<th>내용</th>
	        	<td>	
	        		<textarea class="" name="noteReContent" onKeyUp='javascript:exsoft.util.common.limitStringCheck(this,"1000")'></textarea>
	        		<p class="note_count"><span class="current_count" id="current_textcnt"></span>/1000자</p>
	        	</td>
	        	</tr>
	        	</table>
        	</div>
	        <div class="myNoteReply_btnGrp">
	        	<button type="button" class="myNoteReply_confirm" onclick="javascript:exsoftNoteFunc.event.insertNoteReWrite();">확인</button>
	        	<button type="button" class="myNoteReply_cancel" onclick="javascript:exsoft.util.layout.popDivLayerClose('myNoteReply');">취소</button>
	        </div>
        </div>
        </form>
    </div>
   	<!-- 쪽지 답장 끝 -->
  </div>
 <form name="noteUserFrm"></form>
</body>
</html>