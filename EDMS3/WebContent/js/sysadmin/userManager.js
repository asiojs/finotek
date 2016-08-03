var userManager = {

		groupTree : null,

		// 0. 초기화
		init : function() {
			if (userManager.groupTree == undefined) {
				treeOption = {
    					divId : "#groupTree",
    					context : exsoft.contextRoot,
    					url : "/group/groupList.do"
    			};
				userManager.groupTree = new XFTree(treeOption);
				userManager.groupTree.callbackSelectNode = userManager.callback.selectNode;
				userManager.groupTree.init();
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
			// 사용자 목록 조회
			searchUserList : function(group_id) {
				$("#search_txt").val("");
				
				var param = {
						search_type : "",
						search_txt : ""
				};
				
				// groupId가 없을 경우 현재 선택된 폴더 ID로 교체함	
				if (group_id == undefined || group_id == '') {
					param.groupId = userManager.groupTree.getCurrentNodeId();
				} else {
					param.groupId = group_id;
				}
				
				exsoft.util.grid.gridPostDataRefresh('#userGridList', exsoft.contextRoot + '/user/searchUserList.do', param);
			},

			// 사용자 목록 GRID
			userGridList : function() {
				$("#userGridList").jqGrid({
					url:exsoft.contextRoot + '/user/searchUserList.do',
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['성명', 'ID', '직위','역할', '메일','상태'],
					colModel:[
					          {name:'user_nm',index:'user_nm', align:'center',width:10, editable:false,sortable:true,resizable:true,hidden:false,
					        	  cellattr: function (rowId, cellValue, rowObject) {
										return 'style="cursor: pointer;"';
						          }
					          },
					          {name:'user_id',index:'user_id',align:'center',width:10, editable:false,sortable:true,resizable:true,hidden:false,
					        	  cellattr: function (rowId, cellValue, rowObject) {
										return 'style="cursor: pointer;"';
						          }
					          },
					          {name:'jobtitle_nm',index:'jobtitle_nm',align:'center',width:10, editable:false,sortable:true,resizable:true,hidden:false,
					        	  cellattr: function (rowId, cellValue, rowObject) {
										return 'style="cursor: pointer;"';
						          }
					          },
					          {name:'role_nm',index:'role_nm',align:'center',width:10, editable:false,sortable:true,resizable:true,hidden:false,
					        	  cellattr: function (rowId, cellValue, rowObject) {
										return 'style="cursor: pointer;"';
						          }
					          },
					          {name:'email',index:'email',align:'center',width:10, editable:false,sortable:true,resizable:true,hidden:false,
					        	  cellattr: function (rowId, cellValue, rowObject) {
										return 'style="cursor: pointer;"';
						          }
					          },
					          {name:'user_status_nm',index:'user_status',align:'center',width:10, editable:false,sortable:true,resizable:true,hidden:false,
					        	  cellattr: function (rowId, cellValue, rowObject) {
										return 'style="cursor: pointer;"';
						          }
					          }
					],
					autowidth:true,
					height:"auto",
					viewrecords: true,multiselect:true,multikey: "ctrlKey",sortable: true,shrinkToFit:true,gridview: true,
					sortname : "user_nm",
					sortorder:"asc",
					viewsortcols:'vertical',
					emptyDataText: "데이터가 없습니다.",
					caption:'사용자 목록',
					pagerpos: 'center',
				    pginput: true
				    ,onCellSelect : function(rowid,iCol,cellcontent,e){
						if(iCol == 0){
							// 체크시 row값을 set한다.(선택시 : rowid셋팅, 해제시 : rowid제거)
							$("#userGridList").jqGrid('setSelection',rowid);
						} else {
							var curUserId = $('#userGridList').getRowData(rowid).user_id;
							registUserWindow.init.initUpdateUserWindow(curUserId, function(data, param) {
				 				userManager.event.searchUserList();
				 			});
						}

					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }
					,loadBeforeSend: function() {
						exsoft.util.grid.gridNoDataMsgInit('userGridList');
						exsoft.util.grid.gridTitleBarHide("userGridList");
					}
					,loadComplete: function() {

						if ($("#userGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('userGridList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('userGridList');
						}

						exsoft.util.grid.gridInputInit(false);
					}
				});
			},

			// 등록버튼 클릭시
			userRegist : function() {
				registUserWindow.init.initRegistUserWindow(userManager.groupTree.getCurrentNodeName(), userManager.groupTree.getCurrentNodeId());
			},

			// 삭제버튼 클릭시
			userDelete : function() {
				if (!exsoft.util.grid.gridSelectCheck("userGridList")) {
					jAlert("삭제 할 사용자를 선택하세요", "사용자 관리", 0);
				} else {
					jConfirm("선택한 사용자를 삭제 하시겠습니까 ?", "사용자 관리", 0,
						function(r) {
							if (r) {
								var userIdList = exsoft.util.grid.gridSelectData("userGridList","user_id");

								exsoft.util.ajax.ajaxDataFunctionWithCallback({userIdList:userIdList, type:"delete"}, exsoft.contextRoot + "/admin/userInfoManager.do", "userDelete", userManager.callback.userActionCallback);
							}
					});
				}
			},

			// 이동버튼 클릭시
			userMoveGroup : function() {
				if (!exsoft.util.grid.gridSelectCheck("userGridList")){
					jAlert("이동 할 사용자를 선택하세요", "사용자 관리", 0);
				} else {
					//initGroupWindow(userGroupMoveCallback, "GROUP");
					selectGroupWindow.init.initPage(userManager.callback.userGroupMoveCallback, "GROUP");
				}
			},

			// 사용 / 중지버튼 클릭시
			userStatusUpdate : function(status) {
				if (!exsoft.util.grid.gridSelectCheck("userGridList")) {
					jAlert("변경 할 사용자를 선택하세요", "사용자 관리", 0);
				} else {
					jConfirm("선택한 사용자의 상태를 변경 하시겠습니까 ?", "사용자 관리", 0,
						function(ret) {
							if (ret) {
								var userIdList = exsoft.util.grid.gridSelectData("userGridList","user_id");

								exsoft.util.ajax.ajaxDataFunctionWithCallback({userIdList:userIdList, type:"update_status", user_status:status}, exsoft.contextRoot + "/admin/userInfoManager.do", "userStatusUpdate", userManager.callback.userActionCallback);
							}
					});
				}
			},

			// 비밀번호 초기화버튼 클릭시
			userPassReset : function() {
				if (!exsoft.util.grid.gridSelectCheck("userGridList")) {
					jAlert("초기화 할 사용자를 선택하세요", "사용자 관리", 0);
				} else {
					jConfirm("선택한 사용자의 비밀번호를 초기화 하시겠습니까 ?", "사용자 관리", 0,
						function(ret) {
							if(ret) {
								var userIdList = exsoft.util.grid.gridSelectData("userGridList","user_id");

								exsoft.util.ajax.ajaxDataFunctionWithCallback({userIdList:userIdList, type:"reset_pass"}, exsoft.contextRoot + "/admin/userInfoManager.do", "userPassReset", userManager.callback.userActionCallback);
							}
					});
				}
			},

			// 검색버튼 클릭시
			searchFunction : function() {
				if($("#search_txt").val() != "" && $("#search_txt").val() != undefined) {
					var param = {
							search_type : exsoft.util.layout.getSelectBox('search_type','option'),
							search_txt : $("#search_txt").val()
					}
					exsoft.util.grid.gridPostDataRefresh('#userGridList', exsoft.contextRoot + '/user/searchUserList.do', param);
				} else {
					jAlert("검색어를 입력하세요", "사용자 관리", 0);
				}
			},
			
			// 엔터키 입력시
			enterKeyPress : function(e) {
				if (e.keyCode == 13) {
					userManager.event.searchFunction();
					return false;
				}
			}
		},

		//5. 화면 UI 변경 처리
		ui : {
		},

		//6. callback 처리
		callback : {
			selectNode : function(e, data) {
				// 부서 선택 시 사용자 목록 출력
				userManager.event.searchUserList(data.node.id);
        	},

        	// Callback : 비밀번호 초기화 / 잠금 / 해제 / 이동
        	userActionCallback : function(data, param) {

        		if (data.result == "success") {

        			userManager.event.searchUserList();

        			if (param == "userPassReset") {

        				jAlert("사용자의 비밀번호가 초기화 됐습니다.", "사용자 관리", 0);

        			} else if (param == "userStatusUpdate") {

        				jAlert("사용자의 상태를 변경했습니다.", "사용자 관리", 0);

        			} else if (param == "userGroupMove") {

        				jAlert("사용자의 그룹을 변경했습니다.", "사용자 관리", 0);

        			} else if (param == "userDelete") {

        				jAlert("사용자를 삭제했습니다.", "사용자 관리", 0)

        			}
        		} else {
        			jAlert(data.message, "사용자 관리", 0);
        		}
        	},

        	//사용자 이동 윈도우 콜백
        	userGroupMoveCallback : function(returnGroup) {
        		var currentGroupId = userManager.groupTree.getCurrentNodeId();

        		// 이동하려는 부서와 현재 부서가 동일한지 체크
        		if (currentGroupId == returnGroup[0].id) {
        			jAlert("동일한 부서로 이동은 할수 없습니다.", "사용자 관리", 0);

        		} else if (!exsoft.util.grid.gridSelectCheck("userGridList")){
        			jAlert("이동 할 사용자를 선택하세요", "사용자 관리", 0);

        		} else {
        			jConfirm("선택한 사용자의 부서를 이동 하시겠습니까 ?", "사용자 관리", 0,
        				function(ret) {
        					if (ret) {
        						var userIdList = exsoft.util.grid.gridSelectData("userGridList","user_id");
        						exsoft.util.ajax.ajaxDataFunctionWithCallback({userIdList:userIdList, type:"move", groupId:returnGroup[0].id}, exsoft.contextRoot + "/admin/userInfoManager.do", "userGroupMove", userManager.callback.userActionCallback);
        					}
        			});
        		}
        	}
        },
}