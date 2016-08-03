<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>		
<!-- 
	//	부서별 등록/활용현황
 -->
<div class="cnt_wrapper">		
	<jsp:include page="/jsp/layout/statisticsMenu.jsp" />	  
		<div class="contents">
			<div class="cnts_srch_wrapper">
				<div class="toggle_tree_menu"></div>
				<div class="cnts_srch_detail">					
					<div class="cnts_srch">	
						<label class="statistics_lbl">부서명</label>					
						<input type="text" class="srch_keyword" name="statisticsKeyword" id="statisticsKeyword" placeholder="검색어를 입력하세요" maxlength="20" onkeydown="javascript:if(event.keyCode == 13) { exsoftStatisticsFunc.event.searchFunc(); return false;}" >
						<button type="button" class="srch_btn" onclick="javascript:exsoftStatisticsFunc.event.searchFunc();"></button>
					</div>
					<div class="cnts_datePicker">
						<label class="statistics_lbl">기간</label>
						<select id="statisticsDecade">									
							<option value="one_month">1개월</option>
							<option value="three_month">3개월</option>
							<option value="half_year">6개월</option>
							<option value="one_year">1년</option>
							<option value="input">직접입력</option>
						</select>
						<input type="text" id="statisticsSdate" name="statisticsSdate" class="input-text calend">
						<span class="dateto">-</span>
						<input type="text" id="statisticsEdate" name="statisticsEdate" class="input-text calend">
						<!-- * 도움말 영역입니다. -->
					</div>
				</div>
				<div class="depth_navi">
                	<span id="statisticsPageTitle"></span>
                </div>
			</div>
			<div class="cnts_tbl_menu">
				<div class="tbl_menu_left">
					<span class="help_text">* 조회 데이터는 익일 기준 통계 자료입니다.</span> 
				</div>
				<div class="tbl_menu_right">
					<ul class="tbl_thumbMenu">
						<li class="chart_view"><a href="javascript:void(0);" class="menu" onclick="javascript:exsoftStatisticsFunc.open.chatViewer('groupDocStatistics');"></a></li>
						<li class="excel_download"><a href="javascript:void(0);" class="menu" onClick="javascript:exsoft.util.grid.excelDown('groupDocGridList','${contextRoot}/statistics/groupDocGridList.do');"></a></li>
						<li class="menu_refresh"><a href="javascript:void(0);" class="menu" onclick="javascript:exsoft.util.grid.gridRefresh('groupDocGridList','${contextRoot}/statistics/groupDocGridList.do')"></a></li>
					</ul>
					<select id="statisticsRows">
						<option value="5">5개</option>
						<option value="10">10개</option>
                        <option value="15">15개</option>
                        <option value="20">20개</option>
                        <option value="30">30개</option>
                        <option value="50">50개</option>
					</select>
				</div>
			</div>	
			<div class="cnts_tbl_wrapper" id="targetGroupDocGridcontainer">
				<table id="groupDocGridList"></table>		
			</div>
			<div class="pg_navi_wrapper">
				<ul id="groupDocGridPager" class="pg_navi"></ul>
			</div>
	</div>
</div>
<script type="text/javascript" src="${contextRoot}/js/plugins/chart/Chart.min.js"></script>
<script type="text/javascript" src="${contextRoot}/js/statistics/statistics.js"></script>
 <script type="text/javascript">
  jQuery(function() {	  	 
	  
	  exsoft.util.filter.maxNumber();			// maxlength
	  	  
	  exsoftStatisticsFunc.init.initPage("${menuType}","groupDocStatistics",'statisticsPageTitle','${pageSize}','${part}','#groupDocGridList','/statistics/groupDocGridList.do');
	  exsoftStatisticsFunc.event.groupDocGridList();
	  exsoft.util.grid.gridResize('groupDocGridList','targetGroupDocGridcontainer',35,0);

	  // 메뉴숨김 변경처리
	  $(".toggle_tree_menu").change(function(event,status) {		

		  var menuWidth = $('.lnb_menu').width();
		  if(status == 'hide')	{
			  // 숨김
			  exsoft.util.grid.gridResize('groupDocGridList','targetGroupDocGridcontainer',20,-menuWidth);
		  }else {
			  // 펼침			  
			  exsoft.util.grid.gridResize('groupDocGridList','targetGroupDocGridcontainer',20,0);
		  }
	  });
	  
	  // 차트 INIT
	  exsoftStatisticsFunc.init.chatPageInit("groupDocStatistics");
	  	  
  });

</script>
<!-- Div Layer add -->
<jsp:include page="/jsp/statistics/displayChart.jsp" />