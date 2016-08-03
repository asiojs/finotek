<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/common/databinder.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/validator.js"></script>
<!-- 
	Usage : 문서수정 Main
 -->
  <script type="text/javascript">
jQuery(function() {	
	
	 //문서수정 - 창 닫기
    $('.doc_modify_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.doc_modify').addClass('hide');
    	$('.doc_modify_wrapper').addClass('hide');
    });

    //문서수정 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.doc_modify_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.doc_modify').addClass('hide');
    });
	
});
</script>
 <!-- 문서수정 시작 -->
   	<div class="doc_modify_wrapper hide"></div>
		<div class="doc_modify hide">
     		<div class="doc_modify_title">
              문서 수정
              <a href="#" class="doc_modify_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
         	</div>	
        	<div class="doc_modify_cnts">
        	<form id='documentUpdate' name='documentUpdate'>
        		<div class="divide_line">
	        		<table>
	       			<tr>
	   				<th>제목</th>
	   				<td colspan="3"><input type="text" class="doc_title" name="" class="" placeholder="문서관리 지침"></td>
	       			</tr>
	       			<tr>
	   				<th>버전설정</th>
	   				<td colspan="3">
	   					<span class="current_doc_ver">Ver 1.0</span>
	   					<label><input type="radio">현재버전 유지</label>
	   					<label><input type="radio">부버전 증가(<span class="next_minor_ver">1.1</span>)</label>
	   					<label><input type="radio">주버전 증가(<span class="next_major_ver">2.0</span>)</label>
	   				</td>
	       			</tr>
	       			</table>
	       		</div>
	      		<div class="divide_bottom">
	      			<table>
	       			<tr>
	   				<th>기본폴더
	   					<span class="required">*</span>
	   				</th>
	   				<td colspan="3">
	   					<input type="text" class="doc_folder" name="" class="" value="부서함/경영지원팀/경영일반/규정관리" readonly="readonly">
	   					<button type="button" class="doc_folder_srch">선택</button>
	   				</td>
	       			</tr>
	    				<tr>
	   				<th>문서유형
	   					<span class="required">*</span>
	   				</th>
	   				<td>
	   					<select id="modify_doc_type">
	   						<option>일반문서</option>
	   					</select>
	   				</td>
	   				<th>보존연한
	   					<span class="required">*</span>
	   				</th>
	   				<td>
	   					<select id="modify_doc_preserve">
	   						<option value="5">5년</option>
	   					</select>
	   				</td>
	       			</tr>
	       			<tr>
	   				<th>보안등급
	   					<span class="required">*</span>
	   				</th>
	   				<td>
	   					<label><input type="radio" name="" class="" value="">일반</label>
	   					<label><input type="radio" name="" class="" value="">대외비</label>
	   					<label><input type="radio" name="" class="" value="">극비</label>
	   				</td>
	   				<th>조회등급
	   					<span class="required">*</span>
	   				</th>
	   				<td>
	   					<select id="modify_doc_inquiry">
	   						<option>	사원이상</option>
	   					</select>
	   				</td>
	       			</tr>
	       			<tr>
	   				<th>권한변경여부</th>
	   				<td colspan="3">
	   					<label><input type="checkbox" class="" name="" class="">상위폴더 권한변경시에도 현재 권한 유지</label>
	   				</td>
	       			</tr>
	       			<tr>
	   				<th>등록자[소유자]</th>
	   				<td>홍길동[홍길동]</td>
	   				<th>등록일</th>
	   				<td>2014.09.15 15:12:33	</td>
	       			</tr>
	       			<tr>
	   				<th>다차원 분류</th>
	   				<td colspan="3">
	   					<div class="doc_classification_list hide">
	   						<ul>
	   							<li>
	   								<input type="hidden" class="" name="" class="" value="">
	   								프로젝트/EDMS구축/설계
	   								<a href="#" class=""></a>
	   							</li>
	   							<li>
	   								<input type="hidden" class="" name="" class="" value="">
	   								전산기획팀/경영일반/공통관리
	   								<a href="#" class=""></a>
	   							</li>
	   						</ul>
	   					</div>
	   					<button type="button" class="doc_classification_btn">선택</button>
	   				</td>
	       			</tr>
    				<tr>
	   				<th>키워드</th>
	   				<td colspan="3">
	   					<input type="text" class="doc_keyword_filled" name="" class="" value="문서관리"><br>
	   					<span class="keyword_instruction">여러개의 키워드 등록 시 ','로 구분해주세요</span>
	   				</td>
	       			</tr>
	       			<tr>
	  				<th>설명</th>
	  				<td colspan="3">
	  					<textarea class="doc_cnts"></textarea>
	  				</td>
	       			</tr>
	        		</table>
	        	</div>
        		<div class="doc_auth">
        			<a class="dropDown_txt">
	                  	<label>권한 : <span class="normal">연구소 부서권한</span></label>
	                  	<span class="dropDown_arrow down"></span>
             		</a>
                 	<button type="button">권한변경</button>
        			<div class="doc_auth_cnts hide">
        				<ul>
        					<li>
	        					<label>
		         					<input type="checkbox" name="" class="" value="">
		         					권한부여받은 사용자에게 '공유받은 문서'제공
		         				</label>
	         				</li>
        				</ul>
        			</div>
        		</div>
        		<div class="doc_relative">
        			<a class="dropDown_txt">
	                  	<label>관련문서 <span class="normal">(<span class="">3</span>)</span></label>
	                  	<span class="dropDown_arrow down"></span>
	               </a>
        			<div class="doc_relative_cnts hide">
        				<table>
        				<colgroup>
        					<col width="40"/>
        					<col width="410"/>
        					<col width="88"/>
        					<col width="88"/>
        					<col width="112"/>
        				</colgroup>
        				<thead>
        				<tr>
        				<th><input type="checkbox" name="" class="" value=""></th>
        				<th class="left">제목</th>
        				<th>용량</th>
        				<th>업로드상태</th>
        				<th>
        					<button type="button" class="relative_docs_add">추가</button>
        					<button type="button" class="relative_docs_remove">삭제</button>
        				</th>
        				</tr>
        				</thead>
        				<tbody>
        				<tr>
        				<td><input type="checkbox" name="" class="" value=""></td>
        				<td class="left">CDB 방법론.pptx</td>
        				<td>1.3MB</td>
        				<td>100%</td>
        				<td class="attach_file_menu">
        					<a href="#" class="download"><img src="${contextRoot}/img/icon/attach_download1.png" alt="" title=""></a>
        					<a href="#" class="delete"><img src="${contextRoot}/img/icon/window_close3.png" alt="" title=""></a>
        				</td>
        				</tr>
        				</tbody>
        				</table>
        			</div>
        		</div>
        		<%-- <div class="doc_modify_attach">
        			<a class="dropDown_txt">
						<label>파일추가</label>
	                  	<span class="dropDown_arrow down"></span>
                 	</a>
                 	<button type="button">내 PC</button>
                 	<a href="#" class="doc_attach_delete">
                 		<img src="${contextRoot}/img/icon/attach_delete.png" alt="" title="">
                 		삭제
                 	</a>
        			<div class="doc_attach_cnts hide">
        				<span class="caption">
        					<img src="${contextRoot}/img/icon/upload_icon.png" alt="" title="">
        					해당창에 마우스로 파일을 끌어와 문서를 등록하세요
        				</span>
        			</div>
	         	</div> --%>
	         	
	         	<!-- 파일첨부 -->
	       		<div class="coop_detail">
		        <div class="coop_detail_cnts">
		        <div id="documentupdatefileuploader">파일추가</div>
		        <div id="totalSize"></div> 
		        </div>
		        </div>
         		
         		</form>
     	</div>
     	<div class="doc_modify_btnGrp">
       		<button type="submit" class="modify_docs">수정</button>
       		<button type="button" class="cancel_modify">취소</button>
       	</div>	
     </div>
   	<!-- 문서수정 끝 -->