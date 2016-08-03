/**
 * 통계 공통 스크립트
 */
var exsoftStatisticsFunc = {

		pageTitle : "",
		pageTitleId : "",
		pageSize : "",
		part : "",
		gridId : "",
		gridUrl : "",
		subGrid : "",
		workType : "",
		statisticsMenuType : "",
		chartInit : "true",
		searchOptions :  ['statisticsStrYear','statisticsDecade','statisticsSdate','statisticsEdate'],

		// 통계메뉴
		ConstPageName : {
			//'myStatistics' : '내 문서 현황',
			'myStatistics' : exsoft.message.statistic.myStatisticsTitle,
			'loginStatistics' : '로그인 이력',
			'userDocStatistics' :  '사용자별 등록/활용 현황',
			'groupDocStatistics' : '부서별 등록/활용 현황',
			'decadeDocStatistics' : '기간별 등록/활용 현황',
			'userFolderStatistics' : '사용자/문서함별 보유현항',
			'folderDocStatistics' : '문서함/폴더별  보유현황',
			'typeStatistics' : '문서유형별 보유 현황',
			'securityLevelStatistics' : '보안등급별 보유현황',
			'quotaStatistics' : '문서 Quota 현황'
		},

		SubMenu : ['loginStatistics','userDocStatistics','groupDocStatistics','decadeDocStatistics','userFolderStatistics','folderDocStatistics','typeStatistics','securityLevelStatistics','quotaStatistics'],

		init : {

			// 로그인 이력 초기 함수
			initPage : function(menuType,statisticsMenuType,pageTitleId,pageSize,part,gridId,gridUrl,subGrid) {

				// 로그인이력,사용자별등록/활용현황

				// 상단 메뉴 선택 표시
				exsoft.util.layout.topMenuSelect(menuType);

				// 메뉴PATH
				exsoftStatisticsFunc.pageTitle = exsoftStatisticsFunc.ConstPageName[statisticsMenuType];
				exsoftStatisticsFunc.pageTitleId = pageTitleId;
				exsoftStatisticsFunc.ui.pageNaviTitle(exsoftStatisticsFunc.pageTitle);
				exsoftStatisticsFunc.pageSize = pageSize;
				exsoftStatisticsFunc.part = part;

				// 통계현황인 경우 선택된 메뉴 표시
				exsoftStatisticsFunc.init.subMenuInit(statisticsMenuType);


				// 사용자/문서함별 보유현황 & 문서함/폴더별 보유 현황 구분자(USER/FOLDER);
				if(statisticsMenuType == "userFolderStatistics")	{
					exsoftStatisticsFunc.workType = "USER";
				}else if(statisticsMenuType == "folderDocStatistics")	{
					exsoftStatisticsFunc.workType = "FOLDER";
				}

				// 메인 GRID
				exsoftStatisticsFunc.gridId = gridId;
				exsoftStatisticsFunc.gridUrl = gridUrl;

				// 서브 GRID
				exsoftStatisticsFunc.subGrid = subGrid;

				// 검색조건 공통
				exsoft.util.common.ddslick('#statisticsStrIndex', 'srch_type1', '', 79, function(divId){});

				// 페이지목록 :: 사용안하는 경우 처리
				if(statisticsMenuType == "decadeDocStatistics" || statisticsMenuType == "typeStatistics" || statisticsMenuType == "myStatistics" ) {
					$("#statisticsRows").addClass("hide");
				}else {
					$("#statisticsRows").removeClass("hide");
					exsoft.util.common.ddslick('#statisticsRows', 'tbl_rowCount', '', 68, exsoftStatisticsFunc.callback.statisticsRows);
				}
				exsoft.util.common.ddslick('#statisticsDecade', 'srch_type1', '', 83, exsoftStatisticsFunc.callback.statisticsDecade);

    			// 날짜 DatePicker
    			$("#statisticsSdate").datepicker({dateFormat:'yy-mm-dd'});
    			$("#statisticsEdate").datepicker({dateFormat:'yy-mm-dd'});

    			// 페이지목록 값 설정
    			exsoft.util.layout.setSelectBox('statisticsRows',exsoftStatisticsFunc.pageSize);

    			// 기간 1개월 기본 적용처리
    			exsoft.util.date.changeDate("one_month", "statisticsSdate", "statisticsEdate");

    			// 기간별 등록활용현황 예외처리
    			if(statisticsMenuType == "decadeDocStatistics") {

    				exsoft.util.date.selectYearBox(2010,2021,'statisticsStrYear');

    				// 년도 선택 비활성 및 초기값 설정
					exsoft.util.common.ddslick('#statisticsStrYear', 'srch_type1', '', 79, function(divId){});
    				$('#statisticsStrYear').addClass("hide");
    			}
			},


			// 차트 SelectBox 동적 생성 처리
			chatPageInit : function(statisticsMenuType) {

				var chartType = "";
				var colType = "";

				// ChartType 초기데이터
				exsoft.util.common.chartTypeInit();
				exsoft.util.common.ddslick('#chartType', 'chart_type', '', 88, exsoftStatisticsFunc.callback.chatTypeChange);


				// 파라미터정의
				// 부서별 등록활용현황 - bar
				// 기간별 등록활용현황,문서함/폴더별  보유현황 - line
				// 문서유형별 보유현황,보안등급별 보유현황 - pie
				if(statisticsMenuType == "decadeDocStatistics"|| statisticsMenuType == "folderDocStatistics") {
					chartType = "line";
				}else if(statisticsMenuType == "groupDocStatistics" ) {
					chartType = "bar";
				}else if(statisticsMenuType == "typeStatistics" || statisticsMenuType == "securityLevelStatistics") {
					chartType = "pie";
				}

				exsoft.util.layout.setSelectBox("chartType",chartType);

				// ColType 초기데이터 :
				// 파라미터정의 => 1 - 부서별 등록활용현황/기간별 등록활용현황
				// 파라미터정의 => 2 - 문서함/폴더별  보유현황,문서유형별 보유현황,보안등급별 보유현황
				if(statisticsMenuType == "folderDocStatistics") {
					exsoft.util.common.colTypeInit(2);
					colType = "page_cnt";
				}else if(statisticsMenuType == "typeStatistics" || statisticsMenuType == "securityLevelStatistics") {
					exsoft.util.common.colTypeInit(2);
					colType = "doc_cnt";
				}else {
					exsoft.util.common.colTypeInit(1);
					colType = "create_cnt";
				}

				exsoft.util.common.ddslick('#colType', 'chart_count', '', 88,exsoftStatisticsFunc.callback.colTypeChange);
				exsoft.util.layout.setSelectBox("colType",colType);

				exsoftStatisticsFunc.statisticsMenuType = statisticsMenuType;

			},

			// 좌측메뉴초기화
			subMenuInit : function(statisticsMenuType) {
				for (var n in exsoftStatisticsFunc.SubMenu) {
					$("#"+exsoftStatisticsFunc.SubMenu[n]).removeClass("selected");
				}

				$("#"+statisticsMenuType).addClass("selected");
			},

		},

		open : {

			// 차트 OPEN
			chatViewer : function() {

				exsoftStatisticsFunc.chartInit = "false";

				if(exsoftStatisticsFunc.statisticsMenuType == "decadeDocStatistics") {		// 기간별 등록활용 현황의 경우 조건 다름
					if($("input[name='term']:checked").val() == "daily")	{
						if(exsoft.util.check.searchValid($("#statisticsSdate").val(),$("#statisticsEdate").val()) ) 	 {
							exsoftStatisticsFunc.event.chartViewerProc();
						}
					}else {
						exsoftStatisticsFunc.event.chartViewerProc();
					}

				}else {

					if(exsoft.util.check.searchValid($("#statisticsSdate").val(),$("#statisticsEdate").val()) ) 	 {
						exsoftStatisticsFunc.event.chartViewerProc();
					}

				}
			}

		},

		layer : {

		},

		close : {
		},

		event : {

			// 로그인 이력 GRID
			loginLogGridList : function() {

				$('#loginLogGridList').jqGrid({
					url: exsoft.contextRoot+exsoftStatisticsFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['connect_time','user_nm','user_id','group_nm','connect_ip','cert_nm'],
					colModel:[
						{name:'connect_time',index:'connect_time',width:50, align:'center',editable:false,sortable:true,key:true,align:'center'},
						{name:'user_nm',index:'user_nm',width:50, align:'center',editable:false,sortable:true,key:true,align:'center'},
						{name:'user_id',index:'user_id',width:50, align:'center',editable:false,sortable:true,key:true,align:'center'},
						{name:'group_nm',index:'group_nm',width:50, align:'center',editable:false,sortable:true,key:true,align:'center'},
						{name:'connect_ip',index:'connect_ip',width:50, align:'center',editable:false,sortable:true,key:true,align:'center'},
						{name:'cert_nm',index:'code_nm',width:50, align:'center',editable:false,sortable:true,key:true,align:'center'}
					],
					autowidth:true,viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "connect_time",
					sortorder:"desc",
					scrollOffset: 0,
					viewsortcols:'vertical',
					rowNum : exsoftStatisticsFunc.pageSize,
					emptyDataText: "데이터가 없습니다.",
					caption:'로그인 이력',
					postData : {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),strKeyword:$("#statisticsKeyword").val(),sdate:$("#statisticsSdate").val(),edate:$("#statisticsEdate").val(),part : exsoftStatisticsFunc.part},
					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('loginLogGridList');
						exsoft.util.grid.gridNoDataMsgInit('loginLogGridList');
					}
					,loadComplete: function(data) {

						if ($("#loginLogGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('loginLogGridList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('loginLogGridList');
						}

						exsoft.util.grid.gridInputInit(false);

						exsoft.util.grid.gridPager("#loginLogGridPager",data);

					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }
				});

			 	// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"connect_time":"접속일자","user_nm":"사용자명","user_id":"사용자ID","group_nm":"부서명","connect_ip":"IP주소","cert_nm":"인증여부"}';
				exsoft.util.grid.gridColumHeader('loginLogGridList',headerData,'center');

			},			/// END OF GRID

			// 사용자별 등록/활용 현황 GRID
			userDocGridList : function() {

				$('#userDocGridList').jqGrid({
					url:exsoft.contextRoot+exsoftStatisticsFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['user_nm','user_id','group_nm','type_name','create_cnt','read_cnt','update_cnt','delete_cnt'],
					colModel:[
						{name:'user_nm',index:'user_nm',width:40, align:'center',editable:false,sortable:false,key:true,align:'center'},
						{name:'user_id',index:'user_id',width:40, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'group_nm',index:'group_nm',width:40, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'type_name',index:'type_name',width:30, editable:false,sortable:true,resizable:true,align:'center',summaryType:'count',summaryTpl : '소계'},
						{name:'create_cnt',index:'create_cnt',width:20, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'read_cnt',index:'read_cnt',width:20, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'update_cnt',index:'update_cnt',width:20, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'delete_cnt',index:'delete_cnt',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}}
					],
					autowidth:true,viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "user_nm",
					sortorder:"asc",
					scrollOffset: 0,
					viewsortcols:'vertical',
					rowNum : exsoftStatisticsFunc.pageSize,
					emptyDataText: "데이터가 없습니다.",
					caption:'사용자별 등록/활용 현황',
					postData : {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),strKeyword:$("#statisticsKeyword").val(),sdate:$("#statisticsSdate").val(),edate:$("#statisticsEdate").val(),part : exsoftStatisticsFunc.part},
					grouping: true,
					groupingView : {
						groupField : ['user_nm'],
						groupColumnShow : [false],
						groupText:['<b>{0}({1})</b>'],
				   		groupCollapse : false,
						groupOrder: ['asc'],
						groupSummary : [true],
						groupDataSorted : true
					},
					userDataOnFooter: true,
					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('userDocGridList');
						exsoft.util.grid.gridNoDataMsgInit('userDocGridList');
					}
					,loadComplete: function(data) {
						if ($("#userDocGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('userDocGridList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('userDocGridList');
						}
						exsoft.util.grid.gridInputInit(false);

						exsoft.util.grid.gridPager("#userDocGridPager",data);
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }
				});

			 	// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"user_nm":"사용자명","user_id":"사용자ID","group_nm":"부서명","type_name":"문서유형","create_cnt":"등록","read_cnt":"조회","update_cnt":"수정","delete_cnt":"삭제"}';
				exsoft.util.grid.gridColumHeader('userDocGridList',headerData,'center');
			},

			// 부서별 등록/활용 현황 GRID
			groupDocGridList : function() {
				$('#groupDocGridList').jqGrid({
					url:exsoft.contextRoot+exsoftStatisticsFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['group_nm','group_id','type_name','create_cnt','read_cnt','update_cnt','delete_cnt'],
					colModel:[
						{name:'group_nm',index:'group_nm',width:50, align:'center',editable:false,sortable:false,key:true,align:'center'},
						{name:'group_id',index:'group_id',width:50, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'type_name',index:'type_name',width:50, editable:false,sortable:true,resizable:true,align:'center',summaryType:'count',summaryTpl : '소계'},
						{name:'create_cnt',index:'create_cnt',width:20, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'read_cnt',index:'read_cnt',width:20, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'update_cnt',index:'update_cnt',width:20, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'delete_cnt',index:'delete_cnt',width:20, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}}
					],
					autowidth:true,viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "group_nm",
					sortorder:"asc",
					scrollOffset: 0,
					viewsortcols:'vertical',
					rowNum : exsoftStatisticsFunc.pageSize,
					emptyDataText: "데이터가 없습니다.",
					caption:'부서별 등록/활용 현황',
					postData : {strKeyword:$("#statisticsKeyword").val(),sdate:$("#statisticsSdate").val(),edate:$("#statisticsEdate").val(),part : exsoftStatisticsFunc.part},
					grouping: true,
					groupingView : {
						groupField : ['group_nm'],
						groupColumnShow : [false],
						groupText:['<b>{0}({1})</b>'],
				   		groupCollapse : false,
						groupOrder: ['asc'],
						groupSummary : [true],
						groupDataSorted : true
					},
					userDataOnFooter: true,
					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('groupDocGridList');
						exsoft.util.grid.gridNoDataMsgInit('groupDocGridList');
					}
					,loadComplete: function(data) {

						if ($("#groupDocGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('groupDocGridList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('groupDocGridList');
						}

						exsoft.util.grid.gridInputInit(false);
						exsoft.util.grid.gridPager("#groupDocGridPager",data);

					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }
				});

				// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"group_nm":"부서명","group_id":"부서ID","type_name":"문서유형","create_cnt":"등록","read_cnt":"조회","update_cnt":"수정","delete_cnt":"삭제"}';
				exsoft.util.grid.gridColumHeader('groupDocGridList',headerData,'center');
			},


			//기간별 등록/활용 현황 GRID
			decadeDocGridList : function() {
				$('#decadeDocGridList').jqGrid({
					url:exsoft.contextRoot+exsoftStatisticsFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['dateStr','create_cnt','read_cnt','update_cnt','delete_cnt'],
					colModel:[
						{name:'dateStr',index:'order_str',width:50, align:'center',editable:false,sortable:true,key:true,align:'center'},
						{name:'create_cnt',index:'create_cnt',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'read_cnt',index:'read_cnt',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'update_cnt',index:'update_cnt',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'delete_cnt',index:'delete_cnt',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}}
					],
					autowidth:true,viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "order_str",
					sortorder:"desc",
					scrollOffset: 0,
					emptyDataText: "데이터가 없습니다.",
					caption:'기간별 등록/활용 현황',
					postData : {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),strKeyword:$("#statisticsKeyword").val(),sdate:$("#statisticsSdate").val(),
						edate:$("#statisticsEdate").val(),term:$("input[name='term']:checked").val(),strYear:exsoft.util.layout.getSelectBox('statisticsStrYear','option'),part : exsoftStatisticsFunc.part},
					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('decadeDocGridList');
						exsoft.util.grid.gridNoDataMsgInit('decadeDocGridList');
					}
					,loadComplete: function(data) {

						if ($("#decadeDocGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('decadeDocGridList','nolayer_data');	// no_data => nolayer_data 변경처리
						}else {
							exsoft.util.grid.gridViewRecords('decadeDocGridList');
						}

						exsoft.util.grid.gridInputInit(false);
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }
				});

				// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = null;
				if($("input[name='term']:checked").val() == "daily")	{
					headerData = '{"dateStr":"일자","create_cnt":"등록","read_cnt":"조회","update_cnt":"수정","delete_cnt":"삭제"}';
				}else {
					 headerData = '{"dateStr":"년월","create_cnt":"등록","read_cnt":"조회","update_cnt":"수정","delete_cnt":"삭제"}';
				}

				exsoft.util.grid.gridColumHeader('decadeDocGridList',headerData,'center');

			},

			// 사용자/문서함별 보유 현황 GRID
			userFolderGridList : function() {
				$("#userFolderGridList").jqGrid({
					url:exsoft.contextRoot+exsoftStatisticsFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['user_nm','owner_id','group_nm','map_nm','doc_cnt','page_cnt','page_total'],
					colModel:[
						{name:'user_nm',index:'user_nm',width:50, align:'center',editable:false,sortable:false,key:true,align:'center'},
						{name:'owner_id',index:'owner_id',width:50, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'group_nm',index:'group_nm',width:50, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'map_nm',index:'map_nm',width:30, editable:false,sortable:false,resizable:true,align:'center',summaryType:'count',summaryTpl : '소계'},
						{name:'doc_cnt',index:'doc_cnt',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'page_cnt',index:'page_cnt',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'page_total',index:'page_total',width:40, editable:false,sortable:false,resizable:true,align:'center',
								sorttype:'number',formatter:exsoft.util.grid.bytes2Size,summaryType:'sum'},
					],
					autowidth:true,viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					scrollOffset: 0,
					viewsortcols:'vertical',
					rowNum : exsoftStatisticsFunc.pageSize,
					emptyDataText: "데이터가 없습니다.",
					caption:'사용자/문서함별 보유 현황',
					postData : {
							strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),strKeyword:$("#statisticsKeyword").val(),
							sdate:$("#statisticsSdate").val(),edate:$("#statisticsEdate").val(),
							part : exsoftStatisticsFunc.part,workType:exsoftStatisticsFunc.workType},
					grouping: true,
					groupingView : {
						groupField : ['user_nm'],
						groupColumnShow : [false],
						groupText:['<b>{0}({1})</b>'],
				   		groupCollapse : false,
						groupOrder: ['ASC'],
						groupSummary : [true],
						groupDataSorted : true
					},
					userDataOnFooter: true,
					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('userFolderGridList');
						exsoft.util.grid.gridNoDataMsgInit('userFolderGridList');
					}
					,loadComplete: function(data) {

						if ($("#userFolderGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('userFolderGridList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('userFolderGridList');
						}

						exsoft.util.grid.gridInputInit(false);
						exsoft.util.grid.gridPager("#userFolderGridPager",data);

					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }
				});

			 	// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"user_nm":"사용자명","owner_id":"사용자ID","group_nm":"부서명","map_nm":"문서함","doc_cnt":"문서수","page_cnt":"파일수","page_total":"용량"}';
				exsoft.util.grid.gridColumHeader('userFolderGridList',headerData,'center');

			},


			// 문서함/폴더별 보유 현황 GRID
			folderDocGridList : function() {
				$('#folderDocGridList').jqGrid({
					url:exsoft.contextRoot+exsoftStatisticsFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['group_nm','doc_cnt','page_cnt','fsize'],
					colModel:[
						{name:'group_nm',index:'group_nm',width:50, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'doc_cnt',index:'doc_cnt',width:40, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'page_cnt',index:'page_cnt',width:40, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'fsize',index:'page_total',width:40, editable:false,sortable:false,resizable:true,align:'center'}
					],
					autowidth:true,viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "FOLDER_NAME_KO",
					sortorder:"asc",
					scrollOffset: 0,
					viewsortcols:'vertical',
					rowNum : exsoftStatisticsFunc.pageSize,
					caption:'문서함/폴더별 보유 현황',
					postData : {
						strFolderIdx:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
						sdate:$("#statisticsSdate").val(),edate:$("#statisticsEdate").val(),
						part : exsoftStatisticsFunc.part,workType:exsoftStatisticsFunc.workType},

					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('folderDocGridList');
						exsoft.util.grid.gridNoDataMsgInit('folderDocGridList');
					}
					,loadComplete: function(data) {

						if ($("#folderDocGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('folderDocGridList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('folderDocGridList');
						}

						exsoft.util.grid.gridInputInit(false);
						exsoft.util.grid.gridPager("#folderDocGridPager",data);
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }
				});

			 	// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"group_nm":"구분","doc_cnt":"문서수","page_cnt":"파일수","fsize":"용량"}';
				exsoft.util.grid.gridColumHeader('folderDocGridList',headerData,'center');

			},

			// 문서유형별 보유 현황 GRID
			typeStatGridList : function() {
				$('#typeStatGridList').jqGrid({
					url:exsoft.contextRoot+exsoftStatisticsFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['type_name','map_nm','doc_cnt','page_cnt','page_total'],
					colModel:[
						{name:'type_name',index:'type_name',width:50, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'map_nm',index:'map_nm',width:50, editable:false,sortable:false,resizable:true,align:'center',summaryType:'count',summaryTpl : '소계',
							cellattr: function (rowId, cellValue, rowObject) {
								return 'style="cursor: pointer;"';
				          	}
						},
						{name:'doc_cnt',index:'doc_cnt',width:40, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'page_cnt',index:'page_cnt',width:40, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'page_total',index:'page_total',width:40, editable:false,sortable:false,resizable:true,align:'center',
								sorttype:'number',formatter:exsoft.util.grid.bytes2Size,summaryType:'sum'}
					],
					autowidth:true,viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "type_name",
					sortorder:"asc",
					scrollOffset: 0,
					emptyDataText: "데이터가 없습니다.",
					caption:'문서유형별 보유 현황',
					postData : {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),sdate:$("#statisticsSdate").val(),edate:$("#statisticsEdate").val(),part : exsoftStatisticsFunc.part },
					grouping: true,
					groupingView : {
						groupField : ['type_name'],
						groupColumnShow : [false],
						groupText:['<b>{0}({1})</b>'],
				   		groupCollapse : false,
						groupOrder: ['ASC'],
						groupSummary : [true],
						groupDataSorted : true
					},
					userDataOnFooter: true,
					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('typeStatGridList');
						exsoft.util.grid.gridNoDataMsgInit('typeStatGridList');
					}
					,loadComplete: function() {
						if ($("#typeStatGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('typeStatGridList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('typeStatGridList');
						}

						exsoft.util.grid.gridInputInit(false);
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }

				});

			 	// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"type_name":"문서유형","map_nm":"문서함","doc_cnt":"문서수","page_cnt":"파일수","page_total":"용량"}';
				exsoft.util.grid.gridColumHeader('typeStatGridList',headerData,'center');

			},

			// 보안등급별 보유현황 GRID
			securityLevelGridList : function() {
				$('#securityLevelGridList').jqGrid({
					url:exsoft.contextRoot+exsoftStatisticsFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['code_nm','user_nm','doc_cnt','page_cnt','page_total'],
					colModel:[
						{name:'code_nm',index:'code_nm',width:50, align:'center',editable:false,sortable:false,key:true,align:'center'},
						{name:'user_nm',index:'user_nm',width:50, editable:false,sortable:false,resizable:true,align:'center',summaryType:'count',summaryTpl : '소계'},
						{name:'doc_cnt',index:'doc_cnt',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'page_cnt',index:'page_cnt',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'page_total',index:'page_total',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:exsoft.util.grid.bytes2Size,summaryType:'sum'}
					],
					autowidth:true,viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "security_level",
					sortorder:"asc",
					scrollOffset: 0,
					viewsortcols:'vertical',
					rowNum : exsoftStatisticsFunc.pageSize,
					emptyDataText: "데이터가 없습니다.",
					caption:'보안등급별보유 현황',
					postData : {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),strKeyword:$("#statisticsKeyword").val(),sdate:$("#statisticsSdate").val(),edate:$("#statisticsEdate").val(),part : exsoftStatisticsFunc.part},
					grouping: true,
					groupingView : {
						groupField : ['code_nm'],
						groupColumnShow : [false],
						groupText:['<b>{0}({1})</b>'],
				   		groupCollapse : false,
						groupOrder: ['asc'],
						groupSummary : [true],
						groupDataSorted : true
					},
					userDataOnFooter: true,
					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('securityLevelGridList');
						exsoft.util.grid.gridNoDataMsgInit('securityLevelGridList');
					}
					,loadComplete: function(data) {

						if ($("#securityLevelGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('securityLevelGridList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('securityLevelGridList');
						}

						exsoft.util.grid.gridInputInit(false);
						exsoft.util.grid.gridPager("#securityLevelGridPager",data);

					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }

				});

				// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"code_nm":"보안등급","user_nm":"사용자","doc_cnt":"문서수","page_cnt":"파일수","page_total":"용량"}';
				exsoft.util.grid.gridColumHeader('securityLevelGridList',headerData,'center');

			},

			// 문서 Quota 현황 GRID
			quotaGridList : function() {
				$('#quotaGridList').jqGrid({
					url:exsoft.contextRoot+exsoftStatisticsFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['part_nm','storage_quota','page_total'],
					colModel:[
						{name:'part_nm',index:'part_nm',width:50, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'storage_quota',index:'storage_quota',width:40, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',
							formatter:function(cellValue, option, rowObject){
								if(cellValue > -1) {
									return exsoft.util.grid.bytes2Size(rowObject.storage_quota);
								} else {
									return "<span>무제한</span>";
								}
							}
						},
						{name:'page_total',index:'page_total',width:40, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',
							formatter: function (cellValue, option,rowObject) {
								//할당랼 초과시 현사용량 색 변경처리
								if(rowObject.page_total > rowObject.storage_quota && rowObject.storage_quota != -1)	{
									return "<font color='red'>"+exsoft.util.grid.bytes2Size(rowObject.page_total)+"</font>";
								}else {
									return exsoft.util.grid.bytes2Size(rowObject.page_total);
								}
							}
						}
					],
					autowidth:true,viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "part_nm",
					sortorder:"asc",
					scrollOffset: 0,  // 스크롤 위치 조정. 0으로 지정 안할 경우 테이블 우측에 여백 생김
					viewsortcols:'vertical',
					rowNum : exsoftStatisticsFunc.pageSize,
					emptyDataText: "데이터가 없습니다.",
					caption:'문서 Quota 현황',
					postData : {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),part : exsoftStatisticsFunc.part},
					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('quotaGridList');
						exsoft.util.grid.gridNoDataMsgInit('quotaGridList');
					}
					,loadComplete: function(data) {

						if ($("#quotaGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('quotaGridList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('quotaGridList');
						}

						exsoft.util.grid.gridInputInit(false);
						exsoft.util.grid.gridPager("#quotaGridPager",data);
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }
				});

				// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"part_nm":"구분","storage_quota":"할당량","page_total":"현사용량"}';
				exsoft.util.grid.gridColumHeader('quotaGridList',headerData,'center');
			},


			// 내문서현황 GRID
			myGridList : function() {
				$('#myStatisticsGridList').jqGrid({
					url:exsoft.contextRoot+exsoftStatisticsFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['type_name','create_cnt','read_cnt','update_cnt','delete_cnt'],
					colModel:[
						{name:'type_name',index:'type_name',width:50, align:'center',editable:false,sortable:false,key:true,align:'center'},
						{name:'create_cnt',index:'create_cnt',width:20, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'read_cnt',index:'read_cnt',width:20, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'update_cnt',index:'update_cnt',width:20, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}},
						{name:'delete_cnt',index:'delete_cnt',width:30, editable:false,sortable:false,resizable:true,align:'center',
							sorttype:'number',formatter:'number',summaryType:'sum', formatoptions:{thousandsSeparator:",", decimalPlaces: 0}}
					],
					autowidth:true,viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "type_name",
					sortorder:"asc",
					scrollOffset: 0,
					viewsortcols:'vertical',
					rowNum : exsoftStatisticsFunc.pageSize,
					emptyDataText: "데이터가 없습니다.",
					caption:'내문서현황',
					postData : {sdate:$("#statisticsSdate").val(),edate:$("#statisticsEdate").val()},
					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('myStatisticsGridList');
						exsoft.util.grid.gridNoDataMsgInit('myStatisticsGridList');
					}
					,loadComplete: function(data) {

						if ($("#myStatisticsGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('myStatisticsGridList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('myStatisticsGridList');
						}

						exsoft.util.grid.gridInputInit(false);
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }
				});

				// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"type_name":"문서유형","create_cnt":"등록","read_cnt":"조회","update_cnt":"수정","delete_cnt":"삭제"}';
				exsoft.util.grid.gridColumHeader('myStatisticsGridList',headerData,'center');

			},

			//검색처리(공통)
			searchFunc : function() {

				// 검색기간 유효성 체크 추가 처리(전체옵션 제거)
				if(exsoft.util.check.searchValid($("#statisticsSdate").val(),$("#statisticsEdate").val()) ) 	 {

					var postData = {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
							strKeyword:exsoft.util.common.sqlInjectionReplace($("#statisticsKeyword").val()),
							sdate:$("#statisticsSdate").val(),
							edate:$("#statisticsEdate").val(),
							part : exsoftStatisticsFunc.part,
							workType:exsoftStatisticsFunc.workType,
							is_search:'true'};

					exsoft.util.grid.gridPostDataRefresh(exsoftStatisticsFunc.gridId,exsoft.contextRoot+exsoftStatisticsFunc.gridUrl, postData);

				}
			},


			//검색처리(내문서현황)
			searchMyFunc : function() {

				// 검색기간 유효성 체크 추가 처리(전체옵션 제거)
				if(exsoft.util.check.searchValid($("#statisticsSdate").val(),$("#statisticsEdate").val()) ) 	 {

					var postData = {
							sdate:$("#statisticsSdate").val(),
							edate:$("#statisticsEdate").val(),
							};

					exsoft.util.grid.gridPostDataRefresh(exsoftStatisticsFunc.gridId,exsoft.contextRoot+exsoftStatisticsFunc.gridUrl, postData);

				}
			},


			//검색처리(문서Quota현황)
			searchQuotaFunc : function() {

				var postData = {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
						part :exsoftStatisticsFunc.part,
						is_search:'true'};
				exsoft.util.grid.gridPostDataRefresh(exsoftStatisticsFunc.gridId,gridId,exsoft.contextRoot+exsoftStatisticsFunc.gridUrl, postData);
			},


			//검색처리(문서함/폴더별 보유현황)
			searchFolderFunc : function() {

				// 검색기간 유효성 체크 추가 처리(전체옵션 제거)
				if(exsoft.util.check.searchValid($("#statisticsSdate").val(),$("#statisticsEdate").val()) ) 	 {

					var postData = {strFolderIdx:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
							sdate:$("#statisticsSdate").val(),
							edate:$("#statisticsEdate").val(),
							part : exsoftStatisticsFunc.part,
							workType:exsoftStatisticsFunc.workType,
							is_search:'true'};

					exsoft.util.grid.gridPostDataRefresh(exsoftStatisticsFunc.gridId,exsoft.contextRoot+exsoftStatisticsFunc.gridUrl, postData);

				}
			},

			// 검색처리(기간별 등록/활용 현황)
			searchDecadeFunc : function() {

				var term = $("input[name='term']:checked").val();
				if(term == "monthly")	{
					var postData = {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
							term:$("input[name='term']:checked").val(),
							strKeyword:exsoft.util.common.sqlInjectionReplace($("#statisticsKeyword").val()),
							strYea:exsoft.util.layout.getSelectBox('statisticsStrYear','option'),
							sdate:$("#statisticsSdate").val(),
							edate:$("#statisticsEdate").val(),
							part :exsoftStatisticsFunc.part,
							is_search:'true'};
					exsoft.util.grid.gridPostDataRefresh(exsoftStatisticsFunc.gridId,exsoft.contextRoot+exsoftStatisticsFunc.gridUrl, postData);
				}else {

					// 검색기간 유효성 체크 추가 처리(전체옵션 제거)
					if(exsoft.util.check.searchValid($("#statisticsSdate").val(),$("#statisticsEdate").val()) ) 	 {
						var postData = {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
								term:$("input[name='term']:checked").val(),
								strKeyword:exsoft.util.common.sqlInjectionReplace($("#statisticsKeyword").val()),
								strYear:exsoft.util.layout.getSelectBox('statisticsStrYear','option'),
								sdate:$("#statisticsSdate").val(),
								edate:$("#statisticsEdate").val(),
								part :exsoftStatisticsFunc.part,
								is_search:'true'};
						exsoft.util.grid.gridPostDataRefresh(exsoftStatisticsFunc.gridId,exsoft.contextRoot+exsoftStatisticsFunc.gridUrl, postData);
					}
				}


			},

			// 페이지이동 처리(공통)
			gridPage : function(nPage)	{
				$(exsoftStatisticsFunc.gridId).setGridParam({page:nPage,postData:{is_search:'false',page_init:'false'}}).trigger("reloadGrid");
			},

			// 페이지목록 선택시(공통)
			rowsPage : function(rowNum) {
				$(exsoftStatisticsFunc.gridId).setGridParam({rowNum:rowNum});		// 페이지목록 설정값 변경처리
				$(exsoftStatisticsFunc.gridId).setGridParam({page:1,postData:{is_search:'false',page_init:'true'}}).trigger("reloadGrid");
			},

			// 차트 그리기
			chartViewerProc : function() {

				exsoft.util.layout.divLayerOpen('statics_view_wrapper', 'statics_view');

				exsoftStatisticsFunc.ui.clearCanvas();

				if(exsoftStatisticsFunc.statisticsMenuType == "decadeDocStatistics")	{           // 기간별 등록활용 현황
					exsoftStatisticsFunc.chart.decadeDocStatisticsChart();
				}else if(exsoftStatisticsFunc.statisticsMenuType == "typeStatistics")	{			// 문서유형별 보유현황
					exsoftStatisticsFunc.chart.typeStatisticsChart();
				}else if(exsoftStatisticsFunc.statisticsMenuType == "groupDocStatistics")	{			// 부서별 등록/활용 현황
					exsoftStatisticsFunc.chart.groupDocStatisticsChart();
				}else if(exsoftStatisticsFunc.statisticsMenuType == "folderDocStatistics")	{			// 문서함/폴더별 보유현황
					exsoftStatisticsFunc.chart.folderDocStatisticsChart();
				}else if(exsoftStatisticsFunc.statisticsMenuType == "securityLevelStatistics")	{			// 보안 등급별 보유현황
					exsoftStatisticsFunc.chart.securityLevelStatisticsChart();
				}


			},


		},

		chart : {

			// 문서유형별 그래프 그리기
			typeStatisticsChart : function() {

				var jsonObject = {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
						strKeyword : $("#statisticsKeyword").val(),
						sdate:$("#statisticsSdate").val(),
						edate:$("#statisticsEdate").val(),
						isChart:'chart',
						part : exsoftStatisticsFunc.part,
						page : $('#typeStatGridList').getGridParam('page'),
						rows : $('#typeStatGridList').getGridParam('rowNum'),
						chartType : exsoft.util.layout.getSelectBox('chartType','option'),
			 			colType : exsoft.util.layout.getSelectBox('colType','option'),
			 			yTitle :  exsoft.util.layout.getSelectBox('colType','text'),
						is_search:'false'};

					exsoftStatisticsFunc.callback.charServerCall(jsonObject,'/statistics/typeStatisticsList.do');
			},

			// 기간별 등록/활용 현황 그래프 그리기
			decadeDocStatisticsChart : function() {

				var jsonObject = {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
			 			strKeyword : $("#statisticsKeyword").val(),term:$("input[name='term']:checked").val(),
			 			sdate:$("#statisticsSdate").val(),strYear:exsoft.util.layout.getSelectBox('statisticsStrYear','option'),
			 			edate:$("#statisticsEdate").val(),
			 			isChart:'chart',
			 			part : exsoftStatisticsFunc.part,
			 			page : $('#decadeDocGridList').getGridParam('page'),
			 			rows : $('#decadeDocGridList').getGridParam('rowNum'),
			 			chartType : exsoft.util.layout.getSelectBox('chartType','option'),
			 			colType : exsoft.util.layout.getSelectBox('colType','option'),
			 			yTitle :  exsoft.util.layout.getSelectBox('colType','text'),
			 			is_search:'false'};

				exsoftStatisticsFunc.callback.charServerCall(jsonObject,'/statistics/decadeDocGridList.do');

			},

			// 부서별 등록/활용 현황
			groupDocStatisticsChart : function() {

				var jsonObject = {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
			 			strKeyword : $("#statisticsKeyword").val(),
			 			sdate:$("#statisticsSdate").val(),strYear:exsoft.util.layout.getSelectBox('statisticsStrYear','option'),
			 			edate:$("#statisticsEdate").val(),
			 			isChart:'chart',
			 			part : exsoftStatisticsFunc.part,
			 			page : $('#groupDocGridList').getGridParam('page'),
			 			rows : $('#groupDocGridList').getGridParam('rowNum'),
			 			chartType : exsoft.util.layout.getSelectBox('chartType','option'),
			 			colType : exsoft.util.layout.getSelectBox('colType','option'),
			 			yTitle :  exsoft.util.layout.getSelectBox('colType','text'),
			 			is_search:'false'};

				exsoftStatisticsFunc.callback.charServerCall(jsonObject,'/statistics/groupDocGridList.do');

			},

			// 문서함/폴더별 보유현황
			folderDocStatisticsChart : function() {

				var jsonObject = {strFolderIdx:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
						sdate:$("#statisticsSdate").val(),strYear:exsoft.util.layout.getSelectBox('statisticsStrYear','option'),
						edate:$("#statisticsEdate").val(),
						isChart:'chart',
						workType:exsoftStatisticsFunc.workType,
						part : exsoftStatisticsFunc.part,
						page : $('#folderDocGridList').getGridParam('page'),
						rows : $('#folderDocGridList').getGridParam('rowNum'),
						chartType : exsoft.util.layout.getSelectBox('chartType','option'),
						colType : exsoft.util.layout.getSelectBox('colType','option'),
						yTitle :  exsoft.util.layout.getSelectBox('colType','text'),
						is_search:'false'};

				exsoftStatisticsFunc.callback.charServerCall(jsonObject,'/statistics/userFolderStatisticsList.do');

			},

			// 보안 등급별 보유현황
			securityLevelStatisticsChart : function() {

				var jsonObject = {strIndex:exsoft.util.layout.getSelectBox('statisticsStrIndex','option'),
						strKeyword : $("#statisticsKeyword").val(),
						sdate:$("#statisticsSdate").val(),
						edate:$("#statisticsEdate").val(),
						isChart:'chart',
						part : exsoftStatisticsFunc.part,
						page : $('#securityLevelGridList').getGridParam('page'),
						rows : $('#securityLevelGridList').getGridParam('rowNum'),
						chartType : exsoft.util.layout.getSelectBox('chartType','option'),
			 			colType : exsoft.util.layout.getSelectBox('colType','option'),
			 			yTitle :  exsoft.util.layout.getSelectBox('colType','text'),
						is_search:'false'};

					exsoftStatisticsFunc.callback.charServerCall(jsonObject,'/statistics/securityStatisticsList.do');
			}


		},

		ui : {

			// 통계 메뉴PATH
			pageNaviTitle : function() {
				$("#"+exsoftStatisticsFunc.pageTitleId).html(exsoftStatisticsFunc.pageTitle);
			},

			// 기간별 등록/활용 현황 기간표시 Show/Hide
			termChange : function() {

				// TO DO 년도/기간 비활성화 처리
				if($("input[name='term']:checked").val() == "daily")	{
					for (var n in exsoftStatisticsFunc.searchOptions) {
						if(exsoftStatisticsFunc.searchOptions[n] == "statisticsStrYear")	{
							$("#statisticsStrYear").addClass("hide");
							$("#statisticsDecade").removeClass("hide");
							$("#statisticsSdate").removeClass("readonly");
							$("#statisticsEdate").removeClass("readonly");
						}else {
							$("#"+exsoftStatisticsFunc.searchOptions[n]).prop("disabled",false);
						}
					}
				}else {
					for (var n in exsoftStatisticsFunc.searchOptions) {
						if(exsoftStatisticsFunc.searchOptions[n] == "statisticsStrYear")	{
							$("#statisticsStrYear").removeClass("hide");
							$("#statisticsDecade").addClass("hide");
							$("#statisticsSdate").addClass("readonly");
							$("#statisticsEdate").addClass("readonly");
						}else {
							$("#"+exsoftStatisticsFunc.searchOptions[n]).prop("disabled",true);
						}
					}
				}

			},
			// 캔버스 초기화 처리
			clearCanvas : function() {
				$('#compradores').remove();
				$('.statics_chart_area').append('<canvas id="compradores" width="650" height="400"></canvas>');
			}

		},

		callback : {

			// 페이지목록 변경선택시
			statisticsRows : function(divId, selectedData){
				exsoftStatisticsFunc.event.rowsPage(selectedData.selectedData.value);
			},

			// 날짜목록 변경선택시
			statisticsDecade : function(divId, selectedData){
				exsoft.util.date.changeDate(selectedData.selectedData.value, "statisticsSdate", "statisticsEdate");
			},

			// 챠트 속성 변경에 따른 이벤트 처리
			colTypeChange : function(divId, selectedData,arrParam) {
				if(exsoftStatisticsFunc.chartInit != "true")	{
					exsoftStatisticsFunc.open.chatViewer();
				}
			},

			chatTypeChange : function(divId, selectedData,arrParam) {
				if(exsoftStatisticsFunc.chartInit != "true")	{
					exsoftStatisticsFunc.open.chatViewer();
				}
			},


			charServerCall : function(jsonObject,urls) {

				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject, exsoft.contextRoot+urls, 'chartView',

						function(data, e){

							if(data.result == 'true'){

								var chartData = null;
								var ctx = null;
								var labels = [],dataset=[];

								if(data.chartType == "bar" || data.chartType == "line") {

									for(var item in data.chartData){
				 						labels.push(data.chartData[item].label);
				 						dataset.push(data.chartData[item].value);
				 					}
								}

								if(data.chartType == "bar") {

									chartData = {
				 							labels : labels,
				 				            datasets : [{
				 				                fillColor: "rgba(220,220,220,0.5)",
				 				                strokeColor: "rgba(220,220,220,0.8)",
				 				                highlightFill: "rgba(220,220,220,0.75)",
				 				                highlightStroke: "rgba(220,220,220,1)",
				 				                data : dataset
				 				            }]
				 				        }

									ctx = document.getElementById('compradores').getContext('2d');
									new Chart(ctx).Bar(chartData);

								}else if(data.chartType == "line") {
									chartData = {
				 							labels : labels,
				 				            datasets : [{
				 				            	fillColor: "rgba(220,220,220,0.2)",
				 				            	strokeColor: "rgba(220,220,220,1)",
				 				            	pointColor: "rgba(220,220,220,1)",
				 				            	pointStrokeColor: "#fff",
				 				            	pointHighlightFill: "#fff",
				 				            	pointHighlightStroke: "rgba(220,220,220,1)",
				 				                data : dataset
				 				            }]
				 				        }

									ctx = document.getElementById("compradores").getContext("2d");
									new Chart(ctx).Line(chartData, {
										scaleShowGridLines : true,
										scaleGridLineColor : "rgba(0,0,0,0.05)",
										scaleGridLineWidth : 1,
										bezierCurve : true,
										bezierCurveTension : 0.4,
										pointDot : true,
										pointDotRadius : 4,
										pointDotStrokeWidth : 1,
										pointHitDetectionRadius : 20,
										datasetStroke : true,
										datasetStrokeWidth : 2,
										datasetFill : true,
										onAnimationProgress: function() {
				 							//console.log("onAnimationProgress");
				 						},
				 						onAnimationComplete: function() {
				 							//console.log("onAnimationComplete");
				 						}
									});

								}else if(data.chartType == "pie") {
									 ctx = document.getElementById("compradores").getContext("2d");
									 new Chart(ctx).Doughnut(data.chartData);
								}

							}else {
								jAlert('그래프를 생성하는데 실패하였습니다.','확인',0);
							}

					});

			}

		},








}