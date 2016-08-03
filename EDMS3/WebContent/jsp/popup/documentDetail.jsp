<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<script type="text/javascript" src="${contextRoot}/js/popup/documentVersionDetail.js"></script>
<script type="text/javascript" src="${contextRoot}/js/popup/documentListLayerWindow.js"></script>
<script type="text/javascript" src="${contextRoot}/js/popup/selectFavoriteFolderWindow.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/initbind.js"></script>


<!-- 
	Usage : 
	- 문서기본조회
	- URL복사 & 메일전송
 -->
 
<form name="docVersionDetailFrm">
	<input type="hidden" name="docId">
</form>

<form name="mailReciverUserFrm"></form>

<div class="doc_detail_wrapper hide"></div>

<div class="doc_detail hide">
		<div class="doc_detail_title">
			문서 상세조회
			<a href="#" class="doc_detail_close">
				<img src="${contextRoot}/img/icon/window_close2.png" alt="" title="">
			</a>
		</div>
		
		<div class="doc_detail_cnts">
			<div class="cnts_title"> <span class="title">문서관리 지침</span>
				<div class="cnts_abbr_info"> <span class="cnts_version" id="detail_doc_version">ver 1.1</span> <span class="cnts_locked"> <img id="detail_docLockIcon" src="${contextRoot}/img/icon/lock1.png" alt="" title="">
					<div class="locked_info hide" >
						<p id="lockerInfo">반출자 : 홍길동</p>
						<p id="lockerDate">반출일시 : 2014.08.28</p>
					</div>
					</span>
				</div>
			</div>
			<div class="tab_menu">
				<div class="tab_elem_wrapper">
					<span class="tab_element selected" onclick="javascript:exsoft.document.ui.docDetailSelectAction(0);">기본</span>
					<span class="tab_element" onclick="javascript:exsoft.document.ui.docDetailSelectAction(1);">버전</span> 
					<span class="tab_element" onclick="javascript:exsoft.document.ui.docDetailSelectAction(2);">이력</span> 
					<span class="tab_element" onclick="javascript:exsoft.document.ui.docDetailSelectAction(3);">의견(<span class="opinion_cnt">3</span>)</span> 
				</div>
				<div class="tab_btn_wrapper">
					<button type="button" onclick='javascript:exsoft.document.layer.docUpdateCommonFrm("doc_modify_wrapper","doc_modify");'>수정</button>
					<button type="button" class="delete" onclick="javascript:exsoft.document.event.versionDelete(exsoft.document.prototype.gDocId);">삭제</button>
					<button type="button" class="cancel_checkout" id="btn_detail_checkout" onclick="javascript:exsoft.document.event.Detail_DocumentUnLock();">체크아웃취소</button>
					<div class="tbl_extFunction"> <a class="dropDown_txt">
						<label>추가기능</label>
						<span class="dropDown_arrow down"></span> </a>
						<div class="extFunction_dropDown_wrapper hide">
							<ul class="extFunction_dropDown_menu">
								<li><a href="#" class="move" onclick="javascript:exsoft.document.event.detail_moveDocument();">이동</a></li>
								<li><a href="#" class="copy" onclick="javascript:exsoft.document.event.detail_copyDocument();">복사</a></li>
								<li><a href="#" class="favorite" onclick="javascript:exsoft.document.event.documentAddFavorite();">즐겨찾기 추가</a></li>
								<li><a href="#" class="tempbox" onclick="javascript:exsoft.document.event.documentTempwork();">작업카트 추가</a></li> 
								<li><a href="#" class="archive_excel" onclick="javascript:exsoft.util.grid.excelDown('detaildocHistoryList','${contextRoot}/document/docHistoryListEX.do');">엑셀 이력관리</a></li>
								
							</ul>
						</div>
					</div>
					<button type="button" class="btn_urlCopy" onclick="javascript:exsoft.document.event.sendUrlCopy();">URL복사</button>
					<button type="button" class="url_email_send" id="url_email_send" class="url_email_send hide" onclick="javascript:exsoft.document.event.docDetailsendUrlEmail();">URL 메일송부</button>
				</div>
			</div>
			<div class="tab_form">
				<div class="doc_detail_attach">
					<a class="dropDown_txt">
						<label>
							<strong>첨부파일</strong>
							<span class="attach_cnt"></span>
							<span class="attach_size"></span>
						</label>
					</a>
					<!-- onclick="javascript:exsoft.util.common.zipFileDown('detail_pageList', 'downChk','exsoft.contextRoot')" -->
					<button class="download_all" >모두 저장</button>
					<span class="dropDown_arrow down"></span>
					<div class="attach_docs_wrapper" id="detail_pageList">
						<%-- <ul>
							<li class="attach_docs_list">
								<a href="#">
									<img src="/img/icon/ppt.png" alt="" title="">회의록.docx
								</a>
								<div class="download_detail">
									<span class="download_filesize">1.3MB</span>
									<a href="#" class="download">
										<img src="/${contextRoot}${contextRoot}/img/icon/attach_download1.png" alt="" title="">
									</a>
								</div>
							</li>
							 <li class="attach_docs_list">
								<a href="#">
									<img src="/${contextRoot}${contextRoot}/img/icon/ppt.png" alt="" title="">회의록.docx
								</a>
								<div class="download_detail">
									<span class="download_filesize">1.3MB</span>
									<a href="#" class="download">
										<img src="/${contextRoot}${contextRoot}/img/icon/attach_download1.png" alt="" title="">
									</a>
								</div>
							</li>  
						</ul>--%>
					</div>
				</div>
				<div class="doc_detail_info">
					<a class="dropDown_txt">
						<label><strong>문서 정보</strong></label>
					</a>
					<span class="dropDown_arrow down"></span>
					<div class="detail_info_wrapper">
						<table id="docDetailBasicInfo" class="">
							
						</table>
					</div>
				</div>
				<div class="doc_detail_auth">
					<a class="dropDown_txt">
						<strong>권한 : </strong><label id="docDetailAclName"></label>
						<span class="dropDown_arrow down"></span>
					</a>
					<div class="doc_auth_cnts hide" >
						<table id="detail_docAclItemList">
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
						<table id="detail_docExAclItemList">
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
				<div class="doc_detail_relative">
					<a class="dropDown_txt">
						<strong>관련문서</strong><label id="docRelativeTotal">(3)</label>
						<span class="dropDown_arrow down"></span> 
					</a>
					<div class="relative_docs_wrapper"  id="docDetailRefInfo">
						
							<%-- 
							<ul>
							<li class="relative_docs_list">
								<input type="checkbox" name="" class="relative_docs_checkbox" value="">
								<a href="#">
									<img src="${contextRoot}/img/icon/ppt.png" alt="" title="">
									회의록.docx
								</a>
								<div class="download_detail">
									<span class="download_filesize">1.3MB</span>
									<a href="#" class="download">
										<img src="${contextRoot}/img/icon/attach_download1.png" alt="" title="">
									</a>
									<a href="#" class="contextMenu">
										<img src="${contextRoot}/img/icon/attach_context.png" alt="" title="">
									</a>
									<div class="relative_download_context hide">
										<ul>
											<li><a href="#" class="modify_file"><span class="left"></span><span class="right">파일수정</span></a></li>
											<li><a href="#" class="save_modified"><span class="left"></span><span class="right">수정파일저장</span></a></li>
											<li><a href="#" class="open_modified"><span class="left"></span><span class="right">수정파일열기</span></a></li>
											<li><a href="#" class="cancel_modify"><span class="left"></span><span class="right">수정취소</span></a></li>
										</ul>
									</div>
								</div>
							</li>
							</ul> --%>
						
					</div>
				</div>
			</div>
			<div class="tab_form hide">
				<div class="doc_version" >
				
					<table>
						<colgroup>
						<col width="65"/>
						<col width="292"/>
						<col width="95"/>
						<col width="144"/>
						</colgroup>
						<thead>
							<tr>
								<th>버전</th>
								<th>문서명</th>
								<th>변경자</th>
								<th>변경일자</th>
							</tr>
						</thead>
						<tbody id="detaildocVersionList">
							<!-- <tr>
								<td>ver 2.0</td>
								<td>문서관리 지침</td>
								<td>홍길동</td>
								<td>2014.09.15 15:12:23</td>
							</tr>
							<tr>
								<td>ver 2.0</td>
								<td>문서관리 지침</td>
								<td>홍길동</td>
								<td>2014.09.15 15:12:23</td>
							</tr>
							<tr class="current">
								<td>ver 2.0</td>
								<td>문서관리 지침</td>
								<td>홍길동</td>
								<td>2014.09.15 15:12:23</td>
							</tr> -->
						</tbody>
					</table>
				</div>
				<%-- <div class="doc_ver_attach">
					<div class="doc_ver_title" >
						<span>
							<strong>버전파일</strong> : <span class="ver_file_cnt">1</span>
						</span>
						<div class="ver_btn_wrapper">
							<button class="">버전삭제</button>
							<button class="">상세조회</button>
						</div>
					</div>
					<div class="doc_ver_wrapper" id="docVerAttach">
						<ul>
							<li class="doc_ver_list">
								<a href="#">
									<img src="${contextRoot}/img/icon/ppt.png" alt="" title="">
									회의록.docx
								</a>
								<div class="download_detail">
									<span class="download_filesize">1.3MB</span>
									<a href="#" class="download">
										<img src="${contextRoot}/img/icon/attach_download1.png" alt="" title="">
									</a>
								</div>
							</li>
						</ul>
					</div>
				</div> --%>
			</div>
			<div class="tab_form hide">
				<div class="archive_wrapper"  id="targetDocHistoryList">
					<table id="detaildocHistoryList">
						<%-- <thead>
							<tr>
								<th>no.</th>
								<th>일시
									<a href="#"><img src="${contextRoot}/img/icon/head_dropdown.png" alt="" title=""></a>
								</th>
								<th>수행작업</th>
								<th>작업자</th>
								<th>버전</th>
								<th>비고</th>
							</tr>
						</thead>
						<tbody >
							<!-- <tr>
								<td>1</td>
								<td>2015.02.25 17:12:23</td>
								<td>등록</td>
								<td>홍길동</td>
								<td>1.1</td>
								<td></td>
							</tr>	 -->						
							
						</tbody> --%>
					</table>
				</div>
			</div>
			<div class="tab_form hide">
				<div class="opinion_wrapper">
					<table>
						<!-- <colgroup>
						<col width="85"/>
						<col width="84"/>
						<col width="316"/>
						<col width="134"/>
						</colgroup> -->
						<thead>
							<tr>
								<%-- <th name='creator_name'>의견 등록자</th>
								<th name='parent_creator_name'>답글 대상자</th>
								<th name='content'>내용</th>
								<th name='create_date'>등록일시
									<a href="#"><img src="${contextRoot}/img/icon/head_dropdown.png" alt="" title=""></a>
								</th> --%>
								<th>의견 등록자</th>
								<th>답글 대상자</th>
								<th>내용</th>
								<th>등록일시
									<a href="#"><img src="${contextRoot}/img/icon/head_dropdown.png" alt="" title=""></a>
								</th>
							</tr>
						</thead>
						<tbody  id="detaildocCommentList"></tbody>
					</table>
					<div class="opinion_contextMenu hide">
						<ul>
							<li><a href="javascript:exsoft.document.event.commentAction('REPLY');" class="opinion_reply"><span class="left"></span><span class="right">답글</span></a></li>
							<li><a href="javascript:exsoft.document.event.commentAction('UPDATE');" class="opinion_modify"><span class="left"></span><span class="right">수정</span></a></li>
							<li><a href="javascript:exsoft.document.event.commentAction('DELETE');" class="opinion_delete"><span class="left"></span><span class="right">삭제</span></a></li>
						</ul>
					</div>
				</div>
				<div class="opinion_writeform">
					<form>
						<div class="opinion_account">
							<img src="${contextRoot}/img/icon/user_info.png" alt="">
							<span class="account_nm">홍길동</span>
							<div class="opinion_btnGrp">
								<button type="button" class="refresh">새로고침</button>
								<button type="button" class="register" onclick="javascript:exsoft.document.event.docCommentUpdate();">등록</button>
								<!-- <button type="button" class="delete">삭제</button> -->
							</div>
						</div>
						<div class="opinion_cnt_wrapper">
							<textarea class="" name="">의견을 입력해주세요.</textarea>
						</div>
					</form>
				</div>
			</div>
			<!-- URL 붙여넣기, 메일 내용 -->
			<div class="url_paste hide">
				<div class="url_paste_title">
					URL 붙여넣기 &amp; 메일 내용
					<a href="#" class="url_paste_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
				</div>
				<div class="url_paste_cnts"  id="copyToUrl">
					<table style="border:1px solid #ccc; background:#fff; border-collapse:collapse;font-family:verdana,arial; font-size: 12px; ">
						<colgroup>
						<col width="101"/>
						<col width="126"/>
						<col width="97"/>
						<col width="115"/>
						</colgroup>
						<tr>
							<th>문서제목</th>
							<td colspan="3" id="copy_doc_name">Anydocu_gluesys11103번째 문서 1.1</td>
						</tr>
						<tr>
							<th>폴더</th>
							<td colspan="3" id="copy_folderPath">/엑스소프트/영업팀/영업팀/하위_11</td>
						</tr>
						<tr>
							<th>등록자[소유자]</th>
							<td id ="copy_creator_name">김종민 [김주혁]</td>
							<th>등록일</th>
							<td id="copy_create_date">2015-02-10</td>
						</tr>
						<tr>
							<th>파일</th>
							<td colspan="3" id="copy_file_list">
								<a href="#" class="doc_download">워드문서.docx</a>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<!-- URL 메일 전송  -->
			<div class="url_email hide" id="docDetailUrlEmail">
				<div class="url_email_title">
					URL 메일 전송
					<a href="#" class="url_email_close">
						<img src="${contextRoot}/img/icon/window_close.png" alt="" title="">
					</a>
				</div>
				<div class="url_email_cnts">
					<form>
						<table>
							<colgroup>
							<col width="70"/>
							<col width="310"/>
							</colgroup>
							<tr>
								<th>조회기간</th>
								<td>
									<label>
										<input type="radio" name="urlDate" class="" id="urlDate1" value="">
										제한없음
									</label>
									<label>
										<input type="radio" name="urlDate" class="" id="urlDate2"  value="limit" checked>
										<input type="text" name="" id="urlExpired" class="duration_limit" value="">
										일로 제한
									</label>
								</td>
							</tr>
							<tr id="urlCopyOption" class="">
								<th>메일수신자</th>
								<td>
									<input type="text" name="" class="email_receiver" placeholder="메일수신자를 입력해주세요.">
									<button type="button" class="" class="email_find"  onclick="javascript:exsoft.document.open.reciverDetail();">찾기</button>
								</td>
							</tr>
						</table>
						<div class="url_email_confirm">문서의 URL을 메일로 발송하시겠습니까?</div>
						<div class="url_email_btnGrp">
							<button type="button"  onclick="javascript:exsoft.document.event.sendOperation();">확인</button>
							<button type="button" onclick="javascript:exsoft.util.layout.popDivLayerClose('url_email');">취소</button>
						</div>
					</form>
				</div>
			</div>
	</div>
</div>
<script type="text/javascript">
jQuery(function() {
	
	// Layer 관련 이벤트 정의
	exsoft.util.layout.divLayerOn('doc_detail_wrapper','doc_detail','doc_detail_close');

});
</script>
