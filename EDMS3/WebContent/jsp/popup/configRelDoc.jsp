<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/configRelDoc.js"></script>

<div class="relativeDocs_choose_wrapper hide"></div>
	<div class="relativeDocs_choose hide">
   		<div class="relativeDocs_choose_title">
	         관련문서 설정
	         <a href="#" class="relativeDocs_choose_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
		</div>
		<div class="relativeDocs_sub_wrapper">
			<div class="relativeDocs_choose_cnts">
				* 메인문서를 선택하세요!
				<table class="" id="tempRefDocTable">
					<col style="width:10%;"> <col style="width:55%;"> <col style="width:25%;"> <col style="width:10%;">
					<thead>
						<tr>
							<th name="no">No.</th>
							<th name="doc_name">제목</th>
							<th name="creator_name">등록자</th>
							<th name="delete_no"></th>
						</tr>
					</thead>
				</table>
				
				<div class="relativeDocs_choose_btnGrp">
					<button type="button" class="relativeDocs_confirm_btn" class="" 
					onclick="javascript:configRelDoc.event.okButtonClick();" >확인</button>
					<button type="button" class="relativeDocs_cancel_btn" class="" 
					onclick="javascript:configRelDoc.event.cancelButtonClick();" >취소</button>
				</div>
			</div>
		</div>
    </div>
    <script type="text/javascript">
	jQuery(function() {
		// 작업카트 >  관련문서 설정 - 창 닫기
	    $('.relativeDocs_choose_close').bind("click", function(e){
	    	e.preventDefault();
	    	$(this).parents('.relativeDocs_choose').addClass('hide');
	    	$('.relativeDocs_choose_wrapper').addClass('hide');
	    });

	    // 작업카트 >  관련문서 설정 창 닫기 : 음영진 부분 클릭 시 닫기
	    $('.relativeDocs_choose_wrapper').bind("click", function(){
	    	$(this).addClass('hide');
	    	$('.relativeDocs_choose').addClass('hide');
	    });
	});
	</script>