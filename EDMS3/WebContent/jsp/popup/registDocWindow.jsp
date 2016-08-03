<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/common/databinder.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/validator.js"></script>
<!-- 
	Usage : 문서등록 메인
 -->
 <script type="text/javascript">
jQuery(function() {	
	
		//문서등록 - 창 닫기
		$('.doc_register_close').bind("click", function(e){
		    e.preventDefault();
		    $(this).parents('.doc_register').addClass('hide');
		    $('.doc_register_wrapper').addClass('hide');
		});

		//문서등록 - 창 닫기 : 음영진 부분 클릭 시 닫기
		$('.doc_register_wrapper').bind("click", function(){
			$(this).addClass('hide');
			$('.doc_register').addClass('hide');
		});
	
});
</script>

<div class="doc_register_wrapper hide"></div>
<div class="doc_register hide">
	<div class="doc_register_title">
		문서 등록
		<a href="#" class="doc_register_close">
			<img src="${contextRoot}/img/icon/window_close.png" alt="" title="">
		</a>
	</div>
	<div class="doc_register_wrap">
		<div class="doc_register_cnts">
		
		<form id='documentWrite' name='documentWrite'>
			<input type="hidden" name='folder_id' data-bind="folder_id">
	        <input type="hidden" name='map_id' data-bind="map_id">
			<input type="hidden" name="acl_id" data-bind="acl_id"><!-- 기본ACL -->
	        <input type="hidden" name='is_extended' data-bind="is_extended">
			<input type="hidden" name="isChangeType" data-bind="isChangeType">				<!-- 문서유형 변경유무 -->
		
    		 	<!--<input type="hidden" name="isType" value="insert" data-bind="isType">  업무구분 -->
			<!-- <input type="hidden" name="version_type" value="NEW" data-bind="version_type">	버전타입 -->
			<input type="hidden" name="page_cnt">					<!-- 첨부파일수 -->	
			<input type="hidden" name="jsonMultiFolders">			<!-- 다차원 분류 -->
		
		
			
			<table id="registDoc">
				<tr>
					<th>제목 <span class="required">*</span></th>
					<td colspan="3">
						<input type="text" id="" name="doc_name" class="doc_title" value="" data-bind="doc_name" ex-valid="require">
					</td>
				</tr>
				<tr>
					<th>기본폴더 <span class="required">*</span></th>
					<td colspan="3">
						<input type="text" class="doc_folder  readonly" name="folder_path" data-bind='folder_path' ex-valid="require" disabled>
						<!-- <input type="text" id="" name="folder_path" class="doc_folder" value="부서함/경영지원팀/경영일반/규정관리" readonly="readonly"> -->
						<button type="button" class="doc_folder_srch" onclick="javascript:exsoft.document.event.selectFolderFind();">선택</button>
					</td>
				</tr>
				<tr>
					<th>문서유형 <span class="required">*</span></th>
					<td>
						<!-- <select id="register_doc_type">
							<option>일반문서</option>
						</select> -->
						
						
						
						<select id="register_docType"  name="doc_type" data-select='true' >
								<c:choose>
									<c:when test="${fn:length(typeList) > 0}">
										<c:set var="count" value="${fn:length(typeList) }"></c:set>
										<c:forEach items="${typeList}" var="m" varStatus="type_id">
											<c:choose>
												<c:when test="${m.is_base == 'T'}">
													<option value="${m.type_id}" selected="selected">${m.type_name}</option>
												</c:when>
												<c:otherwise>
													<option value="${m.type_id}">${m.type_name}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:when>
								</c:choose>
							</select>
					</td>
					<th>보존연한 <span class="required">*</span></th>
					<td>
					<!-- 	<select id="register_doc_preserve">
							<option value="5">5년</option>
						</select> -->
						
						<select id="register_preservationyear"  name="preservation_year" data-select='true'>
						<c:choose>
						<c:when test="${fn:length(preservation_year) > 0}">
						<c:set var="count" value="${fn:length(preservation_year) }"></c:set>
							<c:forEach items="${preservation_year}" var="m" varStatus="code_id">	   							
								<c:choose>
						 		<c:when test="${m.code_id == '0'}"><option value="${m.code_id}" selected>${m.code_nm}</option></c:when>
								<c:otherwise><option value="${m.code_id}">${m.code_nm}</option></c:otherwise>
								</c:choose>						
							</c:forEach>
		            	</c:when>
				 		</c:choose>
		                </select>
					</td>
				</tr>
				<tr id="sercurityView">
					<th>보안등급 <span class="required">*</span></th>
					<td>	
					<!-- 
						<label><input type="radio" name="" class="" value="">일반</label>
						<label><input type="radio" name="" class="" value="">대외비</label> 
						<label><input type="radio" name="" class="" value="">극비</label> -->
						<c:choose>               
						<c:when test="${fn:length(sercurity) > 0}">
						<c:set var="count" value="${fn:length(sercurity) }"></c:set>
							<c:forEach items="${sercurity}" var="m" varStatus="code_id">	   	
								<c:choose>
						 		<c:when test="${m.code_id == 'COMMON'}"><input type="radio" name="security_level" value="${m.code_id}" checked/>${m.code_nm}</c:when>
								<c:otherwise><input type="radio" name="security_level" value="${m.code_id}"/>${m.code_nm}</c:otherwise>
								</c:choose>						
							</c:forEach>	       
		            	</c:when>
			 		</c:choose>
				
					</td>
					<th>조회등급 <span class="required">*</span></th>
					<td>
						<!-- <select id="register_doc_inquiry">
							<option>사원이상</option>
						</select> -->
						
						<select id="register_accessgrade"  name="access_grade"  data-select='true'>
		              	<c:choose>               
						<c:when test="${fn:length(position) > 0}">
						<c:set var="count" value="${fn:length(position) }"></c:set>
							<c:forEach items="${position}" var="m" varStatus="code_id">	   	
								<option value="${m.code_id}">${m.code_nm}</option>
							</c:forEach>	       
		            	</c:when>
				 		</c:choose>
		                </select>
                
					</td>
				</tr>
				<tr>
					<th>권한유지</th>
					<td colspan="3">
						<label><input type="checkbox" id="" name="is_inherit_acl_chk"  class="">상위폴더 권한변경시에도 현재 권한 유지</label>
					</td>
				</tr>
				<tr>
					<th>다차원 분류</th>
					<td colspan="3">
						<div class="doc_classification_list hide" id="multiFolder" id="docMultiFolderSelected">
							<!-- <ul>
								<li>
									<input type="hidden" id="" name="" class="" value="">
									프로젝트/EDMS구축/설계 <a href="#" class=""></a>
								</li>
								<li>
									<input type="hidden" id="" name="" class="" value="">
									전산기획팀/경영일반/공통관리 <a href="#" class=""></a>
								</li>
							</ul> -->
						</div>
						<button type="button" class="doc_classification_btn" onclick="javascript:exsoft.document.event.registDocSelectMultiFolderFind();">선택</button>
					</td>
				</tr>
				<tr>
					<th>키워드</th>
					<td colspan="3">
						<input type="text" class="doc_keyword" name="keyword" class="" placeholder="여러개의 키워드 등록시 ','로 구분해주세요">
					</td>
				</tr>
				<tr>
					<th>설명</th>
					<td colspan="3"><textarea name="doc_description" class="doc_cnts"></textarea></td>
				</tr>
			</table>
			
			<!-- 확장문서유형 START -->
			<table class="hide" id="documentWrite_docAttrView">
				<thead></thead>
				<tbody></tbody>
			</table>
			<!-- 확장문서유형 END  -->
			
			

			<div class="doc_auth">
				<a class="dropDown_txt"> <label id="wAclName">경영지원팀 부서 기본 권한</label> <span	class="dropDown_arrow down"></span>
				</a>
				<button type="button" onclick="exsoft.document.event.changeDocumentAcl();">권한변경</button>
				<div class="doc_auth_cnts hide">
					<table id="docmentWrite_acl">						
						<thead>
								<tr>
									<!-- <th>기본 접근자</th> -->
									<th>기본 접근자</th>
									<th>기본권한</th>
									<th>문서등록</th>
									<th>반출취소</th>
									<th>권한변경</th>
								</tr>
							</thead>
		         			<tbody></tbody>
			         		</table>
			         		<table id="docmentWrite_extAcl">
		         				<thead>
			         				<tr>
				         				<!-- <th>추가 접근자</th> -->
				         				<th>추가 접근자</th>
				         				<th>기본권한</th>
				         				<th>문서등록</th>
				         				<th>반출취소</th>
				         				<th>권한변경</th>
			         				</tr>
			         			</thead>
			         			<tbody></tbody>
			         			</table>
						<%-- <tr>
							<th rowspan="4">기본권한</th>
							<th>접근자</th>
							<th>기본권한</th>
							<th>문서등록</th>
							<th>반출취소</th>
							<th>기본권한</th>
						</tr>
						<tr>
							<td>소유자</td>
							<td><img src="${contextRoot}/img/icon/prev_d.png" class="auth_grade" alt="" title="">삭제</td>
							<td><img src="${contextRoot}/img/icon/auth_pass.png" alt="" title=""></td>
							<td><img src="${contextRoot}/img/icon/auth_pass.png" alt="" title=""></td>
							<td><img src="${contextRoot}/img/icon/auth_pass.png" alt="" title=""></td>
						</tr>
						<tr>
							<td>전체</td>
							<td><img src="${contextRoot}/img/icon/prev_r.png" class="auth_grade" alt="" title="">조회</td>
							<td><img src="${contextRoot}/img/icon/auth_nopass.png" alt="" title=""></td>
							<td><img src="${contextRoot}/img/icon/auth_nopass.png" alt="" title=""></td>
							<td><img src="${contextRoot}/img/icon/auth_nopass.png" alt="" title=""></td>
						</tr>
						<tr>
							<td>영업팀</td>
							<td><img src="${contextRoot}/img/icon/prev_u.png" class="auth_grade" alt="" title="">수정</td>
							<td><img src="${contextRoot}/img/icon/auth_pass.png" alt="" title=""></td>
							<td><img src="${contextRoot}/img/icon/auth_nopass.png" alt="" title=""></td>
							<td><img src="${contextRoot}/img/icon/auth_nopass.png" alt="" title=""></td>
						</tr>
						<tr>
							<th rowspan="4">추가접근자</th>
							<th>접근자</th>
							<th>기본권한</th>
							<th>문서등록</th>
							<th>반출취소</th>
							<th>기본권한</th>
						</tr>
						<tr>
							<td colspan="5">데이터가 없습니다.</td>
						</tr> --%>
					
				</div>
			</div>
			
			
			<div class="doc_relative">
   				<a class="dropDown_txt">
                	<label>관련문서 (<span class="relative_docs_cnt">3</span>)</label>
   					<button type="button" onclick="javascript:exsoft.document.event.selectRelDocWindow();">추가</button>
					<span class="dropDown_arrow up"></span>
				</a>
				<div class="doc_relative_cnts">
					<ul>
						<li class="relative_docs_list">
							<a href="#">
								<img src="/img/icon/ppt.png" alt="" title="">
								회의록.docx
							</a>
							<div class="download_detail">
								<span class="download_filesize">1.3MB</span>
								<a href="#" class="download">
									<img src="/img/icon/attach_download1.png" alt="" title="">
								</a>
								<a href="#" class="contextMenu">
									<img src="/img/icon/attach_context.png" alt="" title="">
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
						<li class="relative_docs_list">
							<a href="#">
								<img src="/img/icon/ppt.png" alt="" title="">
								회의록.docx
							</a>
							<div class="download_detail">
								<span class="download_filesize">1.3MB</span>
								<a href="#" class="download">
									<img src="/img/icon/attach_download1.png" alt="" title="">
								</a>
								<a href="#" class="contextMenu">
									<img src="/img/icon/attach_context.png" alt="" title="">
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
						<li class="relative_no_result">
							데이터가 없습니다.
						</li>
					</ul>
       			</div>
       		</div>
       		<!-- 파일첨부 -->
       		<div class="coop_detail">
	        <div class="coop_detail_cnts">
	        <div id="documentfileuploader">파일추가</div>
	        <div id="totalSize"></div> 
	        </div>
	        </div>
         		
       		
       		
       		</form>
       		
       		
			<%-- <div class="doc_register_attach">
				<a class="dropDown_txt"> 
					<label>첨부파일</label> 
					<span class="dropDown_arrow down"></span>
				</a>
				<button type="button">내 PC</button>
				<a href="#" id="doc_attach_delete"> 
					<img src="${contextRoot}/img/icon/attach_delete.png" alt="" title="">
					삭제
				</a>
				<div class="attach_cnts_list hide">
					<!-- 해당창에 마우스로 파일을 끌어와 문서를 등록하세요 -->
					<ul>
						<li>
							<input type="checkbox" name="" class=""> 
							<img src="${contextRoot}/img/icon/ppt.png" alt="" title=""> 
							<a href="#" class="filename">파워포인트문서.pptx</a> <span class="filesize">1.3MB</span>
							<a href="#" class="remove_attach">
								<img src="${contextRoot}/img/icon/window_close3.png" alt="" title="">
							</a>
						</li>
					</ul>
				</div>
			</div> --%>
		          	
			
		</div>
		
		<div class="doc_register_btnGrp">
			<button type="submit" onclick="javascript:exsoft.document.event.documentSubmit();">등록</button>
			<button type="reset" onclick="javascript:exsoft.document.close.layerClose(true);">취소</button>
		</div>
	</div>
	<div class="doc_register_recent">
		<div class="recent_docs_title">최근문서</div>
		<div class="recent_list_wrap">
			<ul class="recent_list">
				<li>
					<a href="#">자재 업무 매뉴얼</a> 
					<a href="#" class="recent_del">
						<img src="${contextRoot}/img/icon/recent_doc_del.png" alt="" title="">
					</a>
				</li>
			</ul>
		</div>
	</div>
</div>