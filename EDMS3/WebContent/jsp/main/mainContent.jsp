<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/main/mainContent.js"></script>

<!-- 
	// EDMS 초기화면
 -->
<div class="main_cnt_wrapper">
	<nav class="main_lnb_menu">
		<section class="main_lnb_info">
			<ul>
				<li class="reg_doc"><a href="#" onclick='javascript:exsoft.document.layer.docWriteCommonFrm("doc_register_wrapper","doc_register");'>문서등록</a></li>
				<li class="reg_work selected"><a href="javascript:void(0);" onclick='javascript:exsoft.process.write("coop_register_wrapper","coop_register");'>업무등록</a></li>
			</ul>
		</section>
		
		<section class="main_coop_process">
			<div class="main_coop_title">협업 업무 현황</div>
			<div class="coop_proceed_summary">
				<ul>
					<li class="composite">
						<a href="#">
							<span class="cnt">99+</span>
							<span class="process_caption">작성중</span>
						</a>
					</li>
					<li class="next_proceed"></li>
					<li class="approval">
						<a href="#">
							<span class="cnt">99+</span>
							<span class="process_caption">승인중</span>
						</a>
					</li>
					<li class="next_proceed"></li>
					<li class="supplement">
						<a href="#">
							<span class="cnt">99+</span>
							<span class="process_caption">보완중</span>
						</a>
					</li>
					<li class="next_proceed"></li>
					<li class="notopen">
						<a href="#">
							<span class="cnt">99+</span>
							<span class="process_caption">미열람</span>
						</a>
					</li>
				</ul>
			</div>
			<div class="coop_doc_list">
				<table>
					<colgroup>
						<col width="17"/>
						<col width="136"/>
						<col width="53"/>
						<col width="92"/>
					</colgroup>
					<tr>
						<td><img src="${contextRoot}/img/icon/file.png" alt="" title=""></td>
						<td>자재업무 매뉴얼</td>
						<td>김문서</td>
						<td>2015-03-15</td>
					</tr>
					<tr>
						<td><img src="${contextRoot}/img/icon/file.png" alt="" title=""></td>
						<td>자재업무 매뉴얼</td>
						<td>김문서</td>
						<td>2015-03-15</td>
					</tr>
					<tr>
						<td><img src="${contextRoot}/img/icon/file.png" alt="" title=""></td>
						<td>자재업무 매뉴얼</td>
						<td>김문서</td>
						<td>2015-03-15</td>
					</tr>
					<tr>
						<td><img src="${contextRoot}/img/icon/file.png" alt="" title=""></td>
						<td>자재업무 매뉴얼</td>
						<td>김문서</td>
						<td>2015-03-15</td>
					</tr>
					<tr>
						<td><img src="${contextRoot}/img/icon/file.png" alt="" title=""></td>
						<td>자재업무 매뉴얼</td>
						<td>김문서</td>
						<td>2015-03-15</td>
					</tr>
				</table>
			</div>
		</section>
		
		<section class="exsoft_banner">
			<!-- bxslider -->
			
			<ul class="banner_list">
				<li><img src="${contextRoot}/img/slider/banner_img01.png" alt="" title=""></li>
				<li><img src="${contextRoot}/img/slider/banner_img02.png" alt="" title=""></li>
				<li><img src="${contextRoot}/img/slider/banner_img03.png" alt="" title=""></li>
			</ul>
		</section>
    </nav>
	
	<div class="main_contents">
		<div class="main_doc_group1">
			<div class="doc_group_element" id="mainNewDoc">
				<div class="main_doc_title"><a href="javascript:exsoft.document.layer.docCommonFrm('doc_detail_wrapper','doc_detail','DOC000000033762');">새로운 문서</a>
				<a href="javascript:void(0);" class="view_more" onclick="javascript:exsoftLayoutFunc.event.goContent('RECENTLYDOC');">더보기</a></div>
				<div class="main_doc_cnts">
					<table id="mainNewDocTr">
						<colgroup><col width="265"/><col width="96"/><col width="93"/></colgroup>
						<thead>
						<tr>
							<th name='doc_name_limit'>제목</th>
							<th name='creator_name'>등록자</th>
							<th name="create_date">등록일</th>
						</tr>
						</thead>						
						<tbody></tbody>
					</table>
				</div>
			</div>
			<div class="doc_group_element down" id="mainMostViewDoc">
				<div class="main_doc_title">최다조회 문서</div>
				<div class="main_doc_cnts">
					<table id="mainMostViewDocTr">
						<colgroup><col width="265"/><col width="96"/><col width="93"/></colgroup>
						<thead>
						<tr>
							<th name='doc_name_limit'>제목</th>
							<th name='creator_name'>등록자</th>
							<th name="create_date">등록일</th>
						</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<div class="doc_group_element down" id="mainMostMyDoc">
				<div class="main_doc_title">최다조회 내 문서</div>
				<div class="main_doc_cnts">
					<table id="mainMostMyDocTr">
						<colgroup><col width="265"/><col width="96"/><col width="93"/></colgroup>
						<thead>
						<tr>
							<th name='doc_name_limit'>제목</th>
							<th name='creator_name'>등록자</th>
							<th name="create_date">등록일</th>
						</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<div class="group_btnMenu">
				<ul>
					<li class="new_doc selected"><a href="javascript:exsoft.document.layer.docCommonFrm('doc_detail_wrapper','doc_detail','DOC000000033762');">새로운 문서</a><span class="subtitle">(업무 문서 기준)</span></li>
					<li class="rank_doc1"><a href="javascript:void(0);">최다조회 문서<br><span class="subtitle">(전체 문서 기준)</span></a></li>
					<li class="rank_doc2"><a href="javascript:void(0);">최다조회 내 문서<br><span class="subtitle">(내 등록 문서 기준)</span></a></li>
				</ul>
			</div>
		</div>
		<div class="main_doc_group2">
			<div class="doc_group_element"  id="mainRecentlyDoc">
				<div class="main_doc_title">최근조회 문서</div>
				<div class="main_doc_cnts">
					<table  id="mainRecentlyDocTr">
						<colgroup><col width="265"/><col width="96"/><col width="93"/></colgroup>
						<thead>
						<tr>
							<th name='doc_name_limit'>제목</th>
							<th name='creator_name'>등록자</th>
							<th name="read_date">조회일</th>
						</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<div class="doc_group_element down" id="mainCheckOutDoc">
				<div class="main_doc_title">내 수정중인 문서<a href="javascript:void(0);" class="view_more" onclick="javascript:exsoftLayoutFunc.event.goContent('CHECKOUT');">더보기</a>
				</div>
				<div class="main_doc_cnts">
					<table  id="mainCheckOutDocTr">
						<colgroup><col width="265"/><col width="96"/><col width="93"/></colgroup>
						<thead>
						<tr>
							<th name='doc_name_limit'>제목</th>
							<th name='creator_name'>등록자</th>
							<th name="lock_date">잠금일</th>
						</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<div class="doc_group_element down" id="mainReceivedNote">
				<div class="main_doc_title">내 받은 쪽지 현황<a href="javascript:void(0);" class="view_more" onclick="javascript:exsoftLayoutFunc.open.noteMain('RECEIVE');">더보기</a></div>
				<div class="main_doc_cnts">
					<table  id="mainReceivedNoteTr">
						<colgroup><col width="361"/><col width="93"/></colgroup>
						<thead>
						<tr>
							<th name="subject">발신자/내용</th>							
							<th name="main_date">날짜</th>
						</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<div class="group_btnMenu">
				<ul>
					<li class="recent_doc selected"><a href="#">최근조회 문서</a></li>
					<li class="mydoc_modify"><a href="#">내 수정중인 문서</a></li>
					<li class="mynote_receive"><a href="#">내 받은 쪽지 현황</a></li>
				</ul>
			</div>
		</div>
	</div>
   </div>
<script type="text/javascript">
jQuery(function() {
	
	// 퍼블리셔 작업부분 복사
	$('.banner_list').bxSlider({
		  auto: true,
		  controls:false,
		  adaptiveHeight:true,
		  pager:true
	});	
		
	$('.group_btnMenu li a').on("click", function(){
		
		var idx = $(this).parent().index();
		var div = $(this).parents('.group_btnMenu').parent().find('.doc_group_element');
		var currDiv = $(this).parents('.group_btnMenu').parent().find('div[class$="doc_group_element"]');
		var currIdx = currDiv.index();
		var toBeShownDiv = div.eq(idx);
		var parents = $(this).parents('.group_btnMenu');
		
		if(idx == currIdx) return false;
		
		parents.find('li').removeClass('selected');
		
		toBeShownDiv.stop().animate({top:0}, 500, function(r){			});		
		currDiv.stop().animate({top:462}, 500, function(r){currDiv.addClass('down');});
		toBeShownDiv.removeClass('down');

		$(this).parent().addClass('selected');
		
		// 데이터출력처리
		if(toBeShownDiv[0].id == "mainNewDoc") {						// 새로운 문서
			exsoftLayoutFunc.init.mainDocList('mainNewDocTr','NEWDOC')
		}else if(toBeShownDiv[0].id == "mainMostViewDoc") {		// 최다조회 문서
			exsoftLayoutFunc.init.mainDocList('mainMostViewDocTr','MOSTVIEWDOC')
		}else if(toBeShownDiv[0].id == "mainMostMyDoc") {		// 최다조회 내 문서
			exsoftLayoutFunc.init.mainDocList('mainMostMyDocTr','MOSTMYDOC')
		}else if(toBeShownDiv[0].id == "mainRecentlyDoc") {		// 최근조회문서
			exsoftLayoutFunc.init.mainDocList('mainRecentlyDocTr','RECENTLYDOC')
		}else if(toBeShownDiv[0].id == "mainCheckOutDoc") {		// 내수정중인문서
			exsoftLayoutFunc.init.mainDocList('mainCheckOutDocTr','CHECKOUTDOC')
		}else if(toBeShownDiv[0].id == "mainReceivedNote") {		// 내받은쪽지현황
			exsoftLayoutFunc.init.mainNoteList('mainReceivedNoteTr');
		}
		
	});
	
	// 초기값 - 새로운문서/최근조회문서
	exsoftLayoutFunc.init.mainDocList('mainNewDocTr','NEWDOC')
	exsoftLayoutFunc.init.mainDocList('mainRecentlyDocTr','RECENTLYDOC')
	
});
</script>   
<jsp:include page="/jsp/process/processWrite.jsp" />
<jsp:include page="/jsp/popup/selectSingleFolderWindow.jsp"/>
<jsp:include page="/jsp/popup/selectAclWindow.jsp"/>
<jsp:include page="/jsp/popup/selectAccessorWindow.jsp"/>
<jsp:include page="/jsp/popup/registDocWindow.jsp" /><!-- 문서 등록화면 -->
<script type="text/javascript" src="${contextRoot}/js/common/base64.js"></script>
<jsp:include page="/jsp/popup/documentDetail.jsp" /><!-- 문서 상세 조회화면 -->
<jsp:include page="/jsp/popup/updateDocWindow.jsp" /><!-- 문서 수정 화면 -->
<jsp:include page="/jsp/popup/selectMultiFolderWindow.jsp"/>
<jsp:include page="/jsp/popup/relDocWindow.jsp"/>
<script type="text/javascript">
jQuery(function() {	
	
	exsoft.util.layout.topMenuSelect("mainContent");
	
	// 사용되는 레이아웃 POP DEFINE
	exsoft.util.layout.lyrPopupWindowResize($(".doc_detail"));		
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".doc_detail"));
	});
	
	exsoft.util.layout.lyrPopupWindowResize($(".doc_register"));		
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".doc_register"));
	});
	//탭 요소 클릭 시 폼 변경
    $('.tab_element').bind("click", function(){
        var idx = $(this).index();
        var targetFrm = $(this).parent().parent().parent().find('div[class^="tab_form"]');
        targetFrm.addClass('hide');
        targetFrm.eq(idx).removeClass('hide');

        $('.tab_element').removeClass('selected');
        $(this).addClass('selected');
    });
});
</script>