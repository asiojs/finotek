/**
 * 협업자 선택 JavaScript
 */
var exsoftProcessCoworkWindow = {
		
		callbackFunction : null,
		treeObject : null,

		// 0. 초기화
        init : {
    		initProcessCoworkWindow : function(coworkList, callback){
    			// 콜백 함수 저장
    			exsoftProcessCoworkWindow.callbackFunction = callback;
        		
        		// 팝업창 오픈
    			exsoftProcessCoworkWindow.open.layerOpen();
    			
    			// 1. 트리 초기화
        		if (exsoftProcessCoworkWindow.treeObject == undefined) {
        			var treeOption = {
        					divId : "#coopUser_tree",
        					context : exsoft.contextRoot,
        					url : "/group/groupList.do"
        			};
        			exsoftProcessCoworkWindow.treeObject = new XFTree(treeOption);
        			exsoftProcessCoworkWindow.treeObject.callbackSelectNode = function(e, data) {
        				// 검색 옵션 초기화
        				$("#coworkUserName").val("");
        				$("#coworkGroupName").val("");
        				
        				var param = {
        						groupName : "",
        						userName : "",					
        						groupId : data.node.id
        				}
        				
        				// 부서 사용자 목록 조회
        				exsoft.util.grid.gridPostDataRefresh('#coopUser_searchUserList',exsoft.contextRoot + '/user/searchUserList.do', param);
        			}
        			exsoftProcessCoworkWindow.treeObject.init();
        		} else {
        			exsoftProcessCoworkWindow.treeObject.refresh();
        		}
        		
        		// 2. 그룹의 기존 멤버를 설정함
        		if ($("#coopUser_searchUserList")[0].grid == undefined) {
        			$('#coopUser_searchUserList').jqGrid({		
        				url:'${contextRoot}/user/searchUserList.do',
        				mtype:"post",
        				datatype:'json',		
        				jsonReader:{
        					page:'page',total:'total',root:'list'
        				},		
        				colNames:['group_nm','user_nm','user_id'],
        				colModel:[
        					{name:'group_nm',index:'group_nm',width:30, editable:false,sortable:true,resizable:true,hidden:false,align:'center'},
        					{name:'user_nm',index:'user_nm',width:50, editable:false,sortable:true,resizable:true,hidden:false,align:'center'},
        					{name:'user_id',index:'user_id',width:50, editable:false,sortable:false,resizable:true,hidden:false,align:'center'},
        				], 
        				autowidth:true,
        				viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
        				sortname : "user_nm",			
        				sortorder:"asc",
        				scroll: true,
        				scrollOffset: 0,
        				rowNum : 10,						
        				rowList:exsoft.util.grid.listArraySize(),
        				emptyDataText: "데이터가 없습니다.",				
        				caption:'사용자 목록',	
        				pagerpos: 'center',  
        			    pginput: true,
        				loadError:function(xhr, status, error) {       
        					exsoft.util.error.isErrorChk(xhr);
        				 }
        				,loadBeforeSend: function() {					
        					exsoft.util.grid.gridTitleBarHide('coopUser_searchUserList');	
        				}
        				,loadComplete: function() {
        					// 기존 데이터를 삭제함
        					//$("#coopUser_searchUserList").jqGrid("clearGridData");
        					exsoft.util.grid.gridInputInit(false);
        				}
        			});
        			
        			// Grid 컬럼정렬 처리
        			var headerData = '{"group_nm":"부서명","user_nm":"사용자명", "user_id":"사용자 ID"}';
        			exsoft.util.grid.gridColumHeader('coopUser_searchUserList',headerData,'center');

        			//headerData = null;
        		} else {
        			// 기존 데이터를 삭제함
        			$("#coopUser_searchUserList").jqGrid("clearGridData");
        			
        		}
        		
        		exsoft.util.table.tablePrintList('tableAuthor', coworkList.authorList, true, true);
        		exsoft.util.table.tablePrintList('tableCoauthor', coworkList.coauthorList, true, true);
        		exsoft.util.table.tablePrintList('tableApprver', coworkList.approverList, true, true);
        		exsoft.util.table.tablePrintList('tableReceiver', coworkList.receiverList, true, true);
        		
        		//3. 작성자, 공동작성자, 승인자, 수신자 없을 경우 nodata msg 표시
        		exsoftProcessCoworkWindow.ui.addTableNoData('tableAuthor');
        		exsoftProcessCoworkWindow.ui.addTableNoData('tableCoauthor');
        		exsoftProcessCoworkWindow.ui.addTableNoData('tableApprver');
        		exsoftProcessCoworkWindow.ui.addTableNoData('tableReceiver');
        		
    		},
        },
        
        // 1. 팝업
        open : {
        	layerOpen : function() {
        		exsoft.util.layout.divLayerOpen("coopUser_choose_wrapper", "coopUser_choose");
        	},   
        },
        
        //2. layer + show
        layer : {
        	
        },
        
        //3. 닫기 + hide
        close : {
        	layerClose : function() {
        		exsoft.util.layout.divLayerClose("coopUser_choose_wrapper", "coopUser_choose");
        	},  
        },
        
        //4. 화면 이벤트 처리
        event : {
        	/**
        	 * table에 row를 추가 한다.
        	 */
        	addTableRow : function(tableId){
        		var gridUserList = $("#coopUser_searchUserList").getGridParam('selarrrow');
        		var data = new Array();
        		
        		//1. 작성자일 경우 validtion
        		if(gridUserList.length == 0){
        			jAlert('사용자를 선택하세요.');
        			return;
        		}else if(tableId == 'tableAuthor' && gridUserList.length > 1){
        			jAlert('작성자는 1명만 선택 가능합니다.');
        			return;
        		}
        		
        		$(gridUserList).each(function(index){
        			var tempData = $("#coopUser_searchUserList").getRowData(gridUserList[index]);
        			
        			//기존 data가 추가되어 있는지 점검
        			if($(exsoft.util.common.getIdFormat(tableId)+'_'+tempData.user_id).length == 0){
//        				console.log('is rowData [000] : ' + $(exsoft.util.common.getIdFormat(tableId)+'_'+tempData.user_id).length);
        				data.push(tempData);
        			}
        			
        		});
        		
        		if(data.length > 0){
        			$(exsoft.util.common.getIdFormat(tableId)+'_noData').remove();
        			if(tableId == 'tableAuthor'){
        				exsoft.util.table.tablePrintList(tableId, data, true, true);
        			}else{
        				exsoft.util.table.tablePrintList(tableId, data, true, false);
        			}
        		}
        		
        		// grid all uncheck
        		$("#coopUser_searchUserList").resetSelection();
        		
        	},
        	
        	/**
        	 * 선택된 tbale의 row를 삭제 한다.
        	 */
        	delTableRow : function(tableId, allChkBoxId){
        		$(exsoft.util.common.getIdFormat(allChkBoxId)).prop("checked",false);
        		var delChecked = $(exsoft.util.common.getIdFormat(tableId)+' tbody input:checked');
        		
        		if(delChecked.length == 0){
        			jAlert('목록에서 제외할 사용자를 선택하세요.');
        		}else{
        			$(delChecked).each(function(index){
        				$(this).parent().parent().remove();
        			});
        			
        			exsoftProcessCoworkWindow.ui.addTableNoData(tableId);
        		}
        	},
        	
        	/**
        	 * table all check and all uncheck
        	 */
        	selectChkBoxAll : function(allChkBoxId, chkBoxName) {
        		exsoft.util.common.allCheckBox(allChkBoxId, chkBoxName);
        	},
        	
        	// 사용자 검색
        	searchGroupUser : function() {
        		var param = {
        				groupName : $("#coworkGroupName").val(),
        				userName : $("#coworkUserName").val(),
        	 			groupId : ''
        		}
        		
        		console.log('stephan : ' + param);
        		exsoft.util.grid.gridPostDataRefresh('#coopUser_searchUserList',exsoft.contextRoot + '/user/searchUserList.do', param);
        	},
        	
        	submit : function(){

        		//callbackFunction
        		var callbackObj = {};
        		var authorList = exsoftProcessCoworkWindow.dofunction.getTableList('tableAuthor');
        		var coauthorList = exsoftProcessCoworkWindow.dofunction.getTableList('tableCoauthor');
        		var approverList = exsoftProcessCoworkWindow.dofunction.getTableList('tableApprver');
        		var receiverList = exsoftProcessCoworkWindow.dofunction.getTableList('tableReceiver');
        		
        		//validation 체크        		
        		if(authorList.length == 0){
        			jAlert('작성자를 지정하세요.');
        		}else if(approverList.length == 0){
            		jAlert('승인자는 1명 이상 지정하세요.');
        		}else{
        			callbackObj.authorList = authorList;
        			callbackObj.coauthorList = coauthorList;
        			callbackObj.approverList = approverList;
        			callbackObj.receiverList = receiverList;
        			exsoftProcessCoworkWindow.callbackFunction(callbackObj);
        			exsoftProcessCoworkWindow.close.layerClose();
        		}
        		
        	}
        },

        //5. 화면 UI 변경 처리
        ui : {
        	/**
        	 * table에 row가 없을 경우 보여주는 화면
        	 */
        	addTableNoData : function(tableId){
        		if($(exsoft.util.common.getIdFormat(tableId)+' tbody').children('tr').length == 0){
        			$(exsoft.util.common.getIdFormat(tableId)+' tbody').append('<tr id="'+tableId+'_noData"><td colspan=4>추가된 사용자가 없습니다.</td></tr>');
        		}
        	},
        		
        },

        //6. callback 처리
        callback : {
                
        },
        
        //7. 내부처리 함수
        dofunction : {
        	getTableList : function(tableId){
       			$(exsoft.util.common.getIdFormat(tableId)+'_noData').remove();  
        		
        		var arrayList = new Array();
        		$(exsoft.util.common.getIdFormat(tableId)+' tbody').children('tr').each(function(trIndex){
        			var userObject = {};

        			userObject.sort_index = trIndex;
        			// 사용자 정보 set
        			$(this).children('td').each(function(tdIndex){
        				switch (tdIndex) {
						case 1:userObject.group_nm = $(this).html();break;
						case 2:userObject.user_nm = $(this).html();break;
						case 3:userObject.user_id = $(this).html();break;
						default:break;
						}        				
        			});  // td each end...
        			
        			arrayList.push(userObject);
        		});	// tr each end...
        		
        		exsoftProcessCoworkWindow.ui.addTableNoData(tableId);
        		return arrayList;
        	},
        }

}

