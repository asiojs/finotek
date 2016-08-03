<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="statics_view_wrapper hide"></div>
<div class="statics_view hide">
	<div class="statics_view_title">
		통계 그래프 보기 
		<a href="#" class="statics_view_close" onclick="exsoft.util.layout.divLayerClose('statics_view_wrapper', 'statics_view');">
		<img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
	</div>
	<div class="statics_view_cnts">
		<div class="statics_sub_wrapper">
			<select id="chartType"></select> 
			<select id="colType"></select>
		</div>
		<div class="statics_chart_area">
			<canvas id="compradores" width="650" height="400"></canvas>
		</div>
	</div>
	<div class="statics_view_btnGrp">
		<button type="button" class="statics_close_btn" onclick="exsoft.util.layout.divLayerClose('statics_view_wrapper', 'statics_view');">닫기</button>
	</div>
</div>
 <script type="text/javascript">
  jQuery(function() {
	  
	  // 차트관련
	  $('.statics_view_wrapper').bind("click", function(){
			$(this).addClass('hide');
	    	$('.statics_view').addClass('hide');
		});
	  
	  ///chartViewerProc();
  });
 
</script>