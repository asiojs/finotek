var auditManager = {

		gAudit_date : null,
		gUser_id : null,
		gUser_name : null,
		part : null,

		 // 0. 초기화
        init : {

        	initPage : function(part,sdate,edate) {

        		auditManager.part = part;
        		auditManager.init.auditListGrid();

        		$("#sdate").val(sdate);
        		$("#edate").val(edate);
        	},

        	auditListGrid : function() {

        		$('#auditGridList').jqGrid({
        			url:exsoft.contextRoot + '/admin/auditPage.do',
        			mtype:"post",
        			datatype:'json',
        			jsonReader:{
        				page:'page',toatl:'toatl',root:'list'
        			},
        			colNames:['audit_date','user_id','user_name','read_count','report_mail_sent_date'],
        			colModel:[
        				{name:'audit_date',index:'audit_date',width:80, editable:false,sortable:true,resizable:true,align:'center'},
        			    {name:'user_name',index:'user_name',width:80, editable:false,sortable:true,resizable:true,align:'center'},
        			    {name:'user_id',index:'user_id',width:80, editable:false,sortable:true,resizable:true,align:'center'},
        			    {name:'read_count',index:'read_count',width:70, editable:false,sortable:true,resizable:true,align:'center'},
        			    {name:'report_mail_sent_date',index:'report_mail_sent_date',width:80, editable:false,sortable:true,resizable:true,align:'center',hidden:true}
        			   ],
        			autowidth:true,
        			height:"auto",
        			viewrecords: true,
        			multiselect:false,
        			sortname : "audit_date",
        			sortorder:"desc",
        			sortable: true,
        			shrinkToFit:true,
        			scrollOffset: 0,
        			gridview: true,
        			rowNum : 15,
        			emptyDataText: "데이터가 없습니다.",
        			caption:'대량문서 열람 목록',
        			postData : {sdate:$("#sdate").val(),edate:$("#edate").val(),is_search:'true',part:auditManager.part}
        			,onCellSelect : function(rowid,iCol,cellcontent,e){
        				auditManager.event.auditViewRefresh('auditGridList',rowid);
        			}
        			,loadBeforeSend: function() {
        				exsoft.util.grid.gridNoDataMsgInit('auditGridList');
        				exsoft.util.grid.gridTitleBarHide('auditGridList');
        			}
        			,loadComplete: function(data) {

        				if ($("#auditGridList").getGridParam("records")==0) {
        					$(".sub_right").addClass("hide");
        					exsoft.util.grid.gridNoRecords('auditGridList','no_data');
        				}else {
        					$(".sub_right").removeClass("hide");
        					exsoft.util.grid.gridViewRecords('auditGridList');

        					// 조회화면 DISPLAY

        					// 조회화면 DISPLAY
        					var rowId = $("#auditGridList").getDataIDs()[0];
        					auditManager.gAudit_date = $("#auditGridList").getRowData(rowId).audit_date;
        					auditManager.gUser_id = $("#auditGridList").getRowData(rowId).user_id;
        					auditManager.gUser_name = $("#auditGridList").getRowData(rowId).user_name;

        					auditManager.event.auditDetailCall(auditManager.gAudit_date,auditManager.gUser_id);
        				}

        				exsoft.util.grid.gridInputInit(true);
        				exsoft.util.grid.gridPager("#auditGridPager",data);
        			}
        			,loadError:function(xhr, status, error) {
        				exsoft.util.error.isErrorChk(xhr);
        	        }

        		});

        		var headerData = '{"audit_date":"감사일","user_name":"성명","user_id":"사용자ID","read_count":"조회수","report_mail_sent_date":"메일 발송일"}';
    		 	exsoft.util.grid.gridColumHeader('auditGridList',headerData,'center');
        	},

        	// 대량문서 열람 상세 목록
        	auditDetailListGrid : function(audit_date,user_id) {

        		$('#auditDetailList').jqGrid({
        			url:exsoft.contextRoot + '/admin/auditDetailPage.do',
        			mtype:"post",
        			datatype:'json',
        			jsonReader:{
        				page:'page',toatl:'toatl',root:'list'
        			},
        			colNames:['page_name','page_size','action_date'],
        			colModel:[
        				{name:'page_name',index:'page_name',width:220, editable:false,sortable:true,resizable:true,align:'left'},
        			    {name:'page_size',index:'page_size',width:80, editable:false,sortable:true,resizable:true,align:'center'},
        			    {name:'action_date',index:'action_date',width:80, editable:false,sortable:true,resizable:true,align:'center'}
        			   ],
        			autowidth:true,
        			height:"auto",
        			viewrecords: true,
        			multiselect:false,
        			sortname : "page_name",
        			sortorder:"desc",
        			sortable: true,
        			shrinkToFit:true,
        			scrollOffset: 0,
        			gridview: true,
        			rowNum : 15,
        			emptyDataText: "데이터가 없습니다.",
        			caption:'대량문서 열람 상세 목록',
        			postData :{audit_date:audit_date,user_id:user_id,is_search:'true'}
        			,loadBeforeSend: function() {
        				exsoft.util.grid.gridTitleBarHide('auditDetailList');
        				exsoft.util.grid.gridNoDataMsgInit('auditDetailList');
        			}
        			,loadComplete: function(data) {

        				if ($("#auditDetailList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('auditDetailList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('auditDetailList');
						}

						exsoft.util.grid.gridInputInit(false);

						exsoft.util.grid.gridPager("#auditDetailPager",data);

        			}
        			,loadError:function(xhr, status, error) {
        				exsoft.util.error.isErrorChk(xhr);
        	        }

        		});

        		var headerData = '{"page_name":"파일명","page_size":"크기","action_date":"조회일"}';
    		 	exsoft.util.grid.gridColumHeader('auditDetailList',headerData,'center');

        	}

        },

        // 1. 팝업
        open : {
        },

        //2. layer + show
        layer : {
        },

        //3. 닫기 + hide
        close : {
        },

        //4. 화면 이벤트 처리
        event : {
        	// 페이지이동 처리(공통)
			gridPage : function(nPage) {
				$("#auditGridList").setGridParam({page:nPage,postData:{is_search:'false',page_init:'false'}}).trigger("reloadGrid");
			},

			gridPageForDetail : function(nPage) {
				$("#auditDetailList").setGridParam({page:nPage,postData:{is_search:'false',page_init:'false'}}).trigger("reloadGrid");
			},

			//상세화면 갱신처리
        	auditViewRefresh : function(gridIds,rowid)	{
        		auditManager.gAudit_date = $("#"+gridIds).getRowData(rowid).audit_date;
        		auditManager.gUser_id = $("#"+gridIds).getRowData(rowid).user_id;
        		auditManager.gUser_name = $("#"+gridIds).getRowData(rowid).user_name;

        		auditManager.event.auditDetailCall(auditManager.gAudit_date,auditManager.gUser_id);
        	},

        	// 상세보기 요청
        	auditDetailCall : function(audit_date,user_id) {

        		if($('#auditDetailList')[0].grid != undefined)	{
        			var postData = { audit_date:audit_date,user_id:user_id,is_search:'true'}; 		// 리스트 변경선택시 상세리스트 페이징 초기화 처리
        			exsoft.util.grid.gridPostDataRefresh('auditDetailList',exsoft.contextRoot + '/admin/auditDetailPage.do',postData);
        		}else {
        			auditManager.init.auditDetailListGrid(audit_date,user_id);
        		}

        		$("#detailTitle").html(auditManager.gUser_name + " :: " + auditManager.gAudit_date);

        		postData = null;
        	},

        	//검색처리
        	searchFunc : function() {

        		if($("#sdate").val().length == 0 || $("#edate").val().length == 0) {
        			jAlert("감사일을 입력하세요", "대량문서 열람 감사 관리", 0);
        			return false;
        		}

        		// 검색기간 유효성 체크 및 1년이내 검색만 가능함
        		if(exsoft.util.check.searchValid($("#sdate").val(),$("#edate").val()) ) {
        			var postData = {
        					sdate:$("#sdate").val(),
        					edate:$("#edate").val(),
        					is_search:'true'
					} ;
        			exsoft.util.grid.gridPostDataRefresh('auditGridList',exsoft.contextRoot + '/admin/auditPage.do',postData);
        		}

        		postData = null;

        	}
        },

        //5. 화면 UI 변경 처리
        ui : {
        },

        //6. callback 처리
        callback : {
        },

}