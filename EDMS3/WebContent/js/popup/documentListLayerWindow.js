var documentListLayerWindow = {
		
		gObjectID : "", // 문서목록 tableID
		gWorkType : "", // 문서함구분 ID
		gCurrentFolderId : "", // 현재 선택된 폴더 ID
		
		actionName : null,
		actionStatus : null,
		actionInfo : null,
		actionObject : null,

		/**
		 * 첨부파일 레이어
		 */
		currentGridRow : null,		// 현재 그리드에서 선택된 row
		
		// 0. 초기화
        init : {
        },

        // 1. 팝업
        open : {
        	openAttachWindow : function(row) {
        		
        		// 1. 모든 창을 닫는다
        		documentListLayerWindow.close.closeAllGridSubPopup();
        		
        		// 2. gridRow 저장
        		documentListLayerWindow.currentGridRow = row;
        		
        		// 3. 팝업창 계산
        		var lyr_popup = $('#attachWindowPopup');
        		var _pos = documentListLayerWindow.ui.getLayerPosition(row.doc_id, "attachWindowPopup");
        		
        		// 4. 첨부파일 목록 구성
        		documentListLayerWindow.ajax.getAttachList(function() {
        			// 5. 구성 후 팝업창 Show
        			if(lyr_popup.hasClass('hide')){
        				lyr_popup.removeClass('hide');
        			}
        		});
        		
        	},
        	openRelationWindow : function(row, type) {
        		// 1. 모든 창을 닫는다
        		documentListLayerWindow.close.closeAllGridSubPopup();
        		
        		// 2. gridRow 저장
        		documentListLayerWindow.currentGridRow = row;
        		
        		// 3. 팝업창 계산
        		var lyr_popup = $('#relationWindowPopup');
        		var _pos = documentListLayerWindow.ui.getLayerPosition(row.doc_id, "relationWindowPopup");
        		
        		// 4. 첨부파일 목록 구성
        		documentListLayerWindow.ajax.getRelationList(function() {
        			// 5. 구성 후 팝업창 Show
        			if(lyr_popup.hasClass('hide')){
        				lyr_popup.removeClass('hide');
        			}
        			
        		});
        	}
        },

        //2. layer + show
        layer : {
        },

        //3. 닫기 + hide
        close : {
        	closeAllGridSubPopup : function() {
        		documentListLayerWindow.close.closeAttachWindow();
        		documentListLayerWindow.close.closeRelationWindow();
        	},
        	
        	closeRelationWindow : function() {
        		$("#relationWindowPopup").addClass("hide");
        	},
        	
        	closeAttachWindow : function() {
        		$("#attachWindowPopup").addClass("hide");
        	},
        },

        ajax : {
        	// 첨부파일 목록
        	getAttachList : function(loopback) {
        		exsoft.util.ajax.ajaxDataFunctionWithCallback({doc_id:documentListLayerWindow.currentGridRow.doc_id}, exsoft.contextRoot + '/document/documentAttachFileByDoc_id.do', 'param', function(data, param){
        			var _target = $("#attachWindowPopup .attach_file_list");
        			var _content = "";
        			
        			// 1. 사이즈 변환
        			$(data.pageList).each(function(idx){
        				this.page_size = exsoft.util.common.bytesToSize(this.page_size, 1); 
        			});
        			
        			// 2.카운트 설정
        			$('#attachWindowPopup .file_cnt').html('파일 : ' + data.pageList.length);
        			
        			// 3.대상 초기화
        			_target.empty();
        			
        			// 3.목록 출력
        			$(data.pageList).each(function(idx) {
        				_content += "<li>";
        				_content += "	<span class='attach_name'>";
        				_content += "		<a href='#'>{0}</a>".format(this.page_name);
        				_content += "	</span>";
        				_content += "	<span class='attach_detail'>";
        				_content += "		{0}".format(this.page_size);
        				_content += "		<a href='#'><img src='{0}/img/icon/attach_download.png' onclick='documentListLayerWindow.event.downloadFile(\"{1}\")'></a>".format(exsoft.contextRoot, this.page_id);
        				_content += "	</span>";
        				_content += "</li>";
        			});
        			_target.append(_content);
        			
        			loopback();
        		});
        	},
        	
        	// 관련문서 목록
        	getRelationList : function(loopback) {
        		exsoft.util.ajax.ajaxDataFunctionWithCallback({doc_id:documentListLayerWindow.currentGridRow.root_id != '' ? documentListLayerWindow.currentGridRow.root_id : documentListLayerWindow.currentGridRow.doc_id}, exsoft.contextRoot + '/document/documentRelationDocByDoc_id.do', 'param', function(data, param){
        			$(data.relationDocList).each(function(index){
        				var $obj = data.relationDocList[index] 
        				this.no = index+1;
//        				exsoft.document.layer.docCommonFrm('doc_detail_wrapper', 'doc_detail', docId);
        				this.doc_name = "<a href='#' onclick='exsoft.document.layer.docCommonFrm(\"doc_detail_wrapper\", \"doc_detail\", \"{0}\");'>{1}</a>".format(this.doc_id, this.doc_name);
        			});
        			exsoft.util.table.tablePrintList("relationWindowDocList", data.relationDocList, false, true);
        			
        			loopback();
        		});
        	}
        },
        //4. 화면 이벤트 처리
        event : {
        	
        	// 체크아웃 취소 - (multi Selected)
        	documentCancelCheckout : function(){

        		documentListLayerWindow.actionObject = "{0}".format(documentListLayerWindow.gObjectID);	
        		
        		if(!exsoft.util.grid.gridSelectCheck(documentListLayerWindow.actionObject)) {
        			jAlert("체크아웃을 취소하고자 하는 문서를 선택하세요.", "체크아웃 취소", 0);
        			return false;
        		} else {
        			var jsonArr = [];
        			var jsonArrIndex = 0;
        			var id = $("#" + documentListLayerWindow.actionObject).getGridParam('selarrrow');

        			for(var i = 0; i < id.length; i++){
        				var rowData = {doc_id : "",  is_locked : "",root_id : "", doc_type : ""};
        				var rowId = $("#" + documentListLayerWindow.actionObject).getRowData(id[i]);
        			
        				//jsonObject 
        				rowData['doc_id'] = rowId.doc_id;
        				rowData['is_locked'] = rowId.lock_status;			
        				rowData['root_id'] = rowId.root_id;
        				rowData['doc_type'] = rowId.doc_type;

        				if(rowData.doc_id){
        					jsonArr[jsonArrIndex] = rowData;
        					jsonArrIndex ++;
        				}
        			}
        			
        			if(jsonArr.length > 0){
        				documentListLayerWindow.event.documentCancelCheckoutSend(jsonArr, 'null');
        			}
        		}
        	},
        	
        	//체크아웃 취소 - server send
        	documentCancelCheckoutSend : function(docJsonArr,status) {	

        		var postData;	
        		documentListLayerWindow.actionObject = "{0}".format(documentListLayerWindow.gObjectID);	
        		
        		if(documentListLayerWindow.actionObject == 'mypageDocList') {
        			if(exsoftMypageFunc.listType == 'OWNER') {
						postData = {
								LIST_TYPE:'OWNER', 
								page_init:'true', 
								map_id:exsoft.util.layout.getSelectBox('myPageDoc_folder','option')
								};	
					} else if(exsoftMypageFunc.listType == 'SHARE_FOLDER') {
						postData = {
								LIST_TYPE: "SHARE_FOLDER", 
								folder_id:exsoftMypageFunc.folder_id
								};
					} else if(exsoftMypageFunc.listType == 'SHARE') {
						postData = {
								LIST_TYPE: "SHARE"
								};
					} else if(exsoftMypageFunc.listType == 'FAVORITE') {
						postData = {
								LIST_TYPE: "FAVORITE", 
								folder_id:exsoftMypageFunc.folder_id, 
								is_virtual:exsoftMypageFunc.is_virtual
								};
					} else if(exsoftMypageFunc.listType == 'CHECKOUT') {
						postData = {
								LIST_TYPE:'CHECKOUT', 
								page_init:'true'
								};
					}
        		}
        		
        		jConfirm('체크아웃을 취소하시겠습니까?', '체크아웃 취소', 0, function(ret){
        			var jsonObject = {"type":"CANCEL_CHECKOUT", "docList":JSON.stringify(docJsonArr)};
        			if(ret) {
        				exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/document/documentControl.do', 'cancelCheckout',
        				function(data, e){
        					if(data.result == 'true'){
        						exsoft.util.common.noty("체크아웃 취소 완료");
        						jAlert("체크아웃 취소 완료.", "체크아웃 취소", 0);
        						if(status == "DETAIL")
        							exsoft.util.layout.popDivLayerClose.layerClose('detail_documentWindow'); 
        						if(documentListLayerWindow.actionObject == 'mypageDocList') {
        							exsoft.util.grid.gridPostDataInitRefresh(documentListLayerWindow.actionObject, exsoft.contextRoot + '/mypage/authDocumentList.do', postData);
        						} else {
        							exsoft.util.grid.gridRefresh(documentListLayerWindow.actionObject, exsoft.contextRoot + '/document/workDocumentList.do');
        						}	
        					} else {
        						jAlert(data.message, "체크아웃 취소", 0);
        					}
        				});
        			}
        		});
        	},
        	
        	// 문서 삭제 - (multi Selected)
        	documentDelete : function(){
        		var jsonArr = [];
        		var jsonArrIndex = 0;
        		
        		documentListLayerWindow.actionObject = "{0}".format(documentListLayerWindow.gObjectID);
        		
        		if(!exsoft.util.grid.gridSelectCheck(documentListLayerWindow.actionObject)) {
        			jAlert("삭제할 문서를 선택하세요", "삭제", 0);
        			return false;
        		} else {
        				
        				var id = $("#" + documentListLayerWindow.actionObject).getGridParam('selarrrow');
        				
        				for(var i = 0; i < id.length; i++){
        					var rowData = {doc_id:"", root_id:"", is_locked:"", doc_type:""};
        					var rowId = $("#" + documentListLayerWindow.actionObject).getRowData(id[i]);
        					var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(rowId.acl_level));
        					
        					if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("DELETE")) {
        						jAlert("문서 삭제 권한이 없습니다.", "삭제", 0);
        						return false;
        					}
        					
        					if(rowId.lock_status == 'T') {
        						jAlert("체크아웃한 문서가 존재합니다.\n 체크아웃취소 후 다시 작업하시기 바랍니다.", "삭제", 0);
        						return false;
        					}
        					
        					//jsonObject
        					rowData['doc_id'] = rowId.doc_id;
        					rowData['root_id'] = rowId.root_id;
        					rowData['is_locked'] = rowId.lock_status;
        					rowData['doc_type'] = rowId.doc_type;
        					
        					if(rowData.doc_id){
        						jsonArr[jsonArrIndex] = rowData;
        						jsonArrIndex++;
        					}
        				}		
        			if(jsonArr.length > 0){
        				documentListLayerWindow.event.documentDeleteSend(jsonArr);
        				return;
        			}		
        		}	
        	},

        	// 문서 삭제 server send
        	documentDeleteSend : function(docJsonArr, status) {
        		
        		var postData;	
        		documentListLayerWindow.actionObject = "{0}".format(documentListLayerWindow.gObjectID);	
        		
        		if(documentListLayerWindow.actionObject == 'mypageDocList') {
        			if(exsoftMypageFunc.listType == 'OWNER') {
						postData = {
								LIST_TYPE:'OWNER', 
								page_init:'true', 
								map_id:exsoft.util.layout.getSelectBox('myPageDoc_folder','option')
								};	
					} else if(exsoftMypageFunc.listType == 'SHARE_FOLDER') {
						postData = {
								LIST_TYPE: "SHARE_FOLDER", 
								folder_id:exsoftMypageFunc.folder_id
								};
					} else if(exsoftMypageFunc.listType == 'SHARE') {
						postData = {
								LIST_TYPE: "SHARE"
								};
					} else if(exsoftMypageFunc.listType == 'FAVORITE') {
						postData = {
								LIST_TYPE: "FAVORITE", 
								folder_id:exsoftMypageFunc.folder_id, 
								is_virtual:exsoftMypageFunc.is_virtual
								};
					} else if(exsoftMypageFunc.listType == 'EXPIRED') {
						postData = {
								LIST_TYPE:'EXPIRED', 
								page_init:'true'
								};
					}
        		}
        		jConfirm('선택한 문서를 삭제하시겠습니까?', "삭제", 0, function(ret) {
        			var jsonObject = {"type":"DELETE", "delDocList":JSON.stringify(docJsonArr)}; 
        			if(ret){
        				exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/document/documentControl.do', 'delete',
        					function(data, e){
        						if(data.result == 'true'){
        							exsoft.util.common.noty("문서 삭제 완료");
        							
        							if(status == "DETAIL")
        								exsoft.util.layout.popDivLayerClose.layerClose('detail_documentWindow');
        							if(documentListLayerWindow.actionObject == 'mypageDocList') {
        								exsoft.util.grid.gridRefresh(documentListLayerWindow.actionObject, exsoft.contextRoot + '/mypage/authDocumentList.do', postData);
        							} else {
        								exsoft.util.grid.gridRefresh(documentListLayerWindow.actionObject, exsoft.contextRoot + '/document/workDocumentList.do');
        							}	
        						} else {
        							jAlert(data.message);
        						}
        					}		
        				);
        			}
        		});
        	},
        	
        	// 문서 즐겨찾기 추가 - 추가기능/버튼 일때 사용 (multi Selected)
        	documentAddFavorite : function(){
        		var jsonArr = [];
        		var jsonArrIndex = 0;
        		
        		documentListLayerWindow.actionObject = "{0}".format(documentListLayerWindow.gObjectID);
        		
        		if(!exsoft.util.grid.gridSelectCheck(documentListLayerWindow.actionObject)) {
        			jAlert("즐겨찾기 추가할 문서를 선택하세요.", "즐겨찾기 추가", 0);
        			return false;
        		} else {
        			var id = $("#" + documentListLayerWindow.actionObject).getGridParam('selarrrow');
        			for(var i = 0; i < id.length; i++){
        				var rowData = {doc_id:"", root_id:""};
        				var rowId = $("#" + documentListLayerWindow.actionObject).getRowData(id[i]);
        								
        				//jsonObject
        				rowData['doc_id'] = rowId.doc_id;
        				rowData['root_id'] = rowId.root_id;
        				if(rowData.doc_id){
        					jsonArr[jsonArrIndex] = rowData;
        					jsonArrIndex++;
        				}				
        			}		
        			if(jsonArr.length > 0) {	
        				documentListLayerWindow.event.documentAddFavoriteSend(jsonArr);
        				return;
        			} else {
        				jAlert("즐겨찾기 문서를 구성하는 중 오류가 발생했습니다.", "즐겨찾기 추가", 0);
        			}
        		}
        	},

        	//문서 즐겨찾기 추가 - Server send
        	documentAddFavoriteSend : function(docJsonArr) {	
        		// 1. 옵션 설정
        		var popupOption = {
        				mode : "SELECT"		// 단순 조회 (하위폴더 포함 체크 박스 사용안함)
        				,only_virtual : "Y"		// 가상 폴더만 조회
        		};
        		
        		// 2. 즐겨찾기 선택창 팝업 (selectFavoriteFolderWindow.jsp 필요)
        		selectFavoriteFolderWindow.init(popupOption, /*유효성 검사*/ false, function (returnObject) {
        			
        			// 3. 서버 전송 파라메터 설정
        			var jsonObject = {
        					type : "ADD_TO_FAVORITES"
        					,folder_id : returnObject.target_folder_id
        					,docList : JSON.stringify(docJsonArr)
        			};
        			// 4. 즐겨찾기 정보 서버 처리
        			exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/document/documentControl.do','ADD_TO_FAVORITES', function(data, param) {
        				exsoft.util.common.noty("즐겨찾기 추가 완료");
        				jAlert("즐겨찾기 추가 완료", "즐겨찾기 추가", 0);
        			});
        		});
        	},
        	
        	// 작업카트 추가 - 추가기능/버튼 일때 사용 (multi Selected)
        	documentTempwork : function(){
        		var jsonArr = [];
        		var jsonArrIndex = 0;

        		documentListLayerWindow.actionObject = "{0}".format(documentListLayerWindow.gObjectID);
        		
        		if(!exsoft.util.grid.gridSelectCheck(documentListLayerWindow.actionObject)){
        			jAlert("작업카트에 추가할 문서를 선택하세요", "작업카트 추가", 0);
        			return false;
        		} else {
    				var id = $("#" + documentListLayerWindow.actionObject).getGridParam('selarrrow');
    				
    				for(var i = 0; i < id.length; i++){
    				var rowData = {doc_id:"", root_id:""};
    					var rowId = $("#" + documentListLayerWindow.actionObject).getRowData(id[i]);

    					if(rowId.lock_status == 'T'){
    						jAlert("체크아웃한 문서가 존재합니다.\n 체크아웃취소 후 다시 작업하시기 바랍니다.", "작업카트 추가", 0);
    						return false;
    					}
    					
    					//jsonObject
    					rowData['doc_id'] = rowId.doc_id;
    					rowData['root_id'] = rowId.root_id;
    					rowData['is_locked'] = rowId.lock_status;				
    					
    					if(rowData.doc_id){
    						jsonArr[jsonArrIndex] = rowData;
    						jsonArrIndex++;
    					}
    				}
    				
    				if(jsonArr.length > 0){
    					documentListLayerWindow.event.documentTempworkSend(jsonArr);
    				} else {
    					jAlert("작업카트에 문서를 추가하는 중 오류가 발생했습니다.", "작업카트 추가", 0);
    				}		
				}			
			},
        			
        	// 작업카트 추가 Server send
        	documentTempworkSend : function(docJsonArr) {
        		jConfirm('작업카트에 추가하시겠습니까?', "작업카트 추가", 0, function(r){
        			var jsonObject = {"type":"ADD_TO_TEMPWORK", "docList":JSON.stringify(docJsonArr)};
        			if(r) {

        				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject, exsoft.contextRoot + '/document/documentControl.do', 'tempwork',
        				function(data, e){
        					if(data.result == 'true'){
        						exsoft.util.common.noty("작업카트 추가 완료");
        						jAlert("작업카트 추가 완료.", "작업카트 추가", 0);
        					} else {
        						jAlert(data.message, "작업카트 추가", 0);
        					}
        				});
        			}
        		});
        	},
        	
        	//문서 이동
        	documentMove : function(status, getInfo){

        		documentListLayerWindow.actionName = "MOVE";
        		documentListLayerWindow.actionStatus = status;
        		documentListLayerWindow.actionInfo = getInfo;
        		documentListLayerWindow.actionObject = "{0}".format(documentListLayerWindow.gObjectID);
        		
        		var getWorkType = "{0}".format(documentListLayerWindow.gWorkType);
        		var getMapId = "";
        		var getDocType = "";

        		if(getWorkType == 'WORK_MYDEPT'){
        			getMapId = 'MYDEPT';
        			getDocType = 'ALL_TYPE';
        		} else if (getWorkType == 'WORK_ALLDEPT'){
        			getMapId = 'MYDEPT';
        			getDocType = 'REPORT';
        		} else if (getWorkType == 'WORK_PROJECT'){
        			getMapId = 'PROJECT';
        			getDocType = 'XR_DOCUMENT';
        		} else if (getWorkType == 'null' || getWorkType == "MYPAGE") {
        			getMapId = 'MYPAGE';
        			getDocType = 'ALL_TYPE';
        		}
        		
        		if(status != "ONLY" && status != "DETAIL" && !exsoft.util.grid.gridSelectCheck(documentListLayerWindow.actionObject)) {
        			jAlert("이동할 문서를 선택하세요", "이동", 0);
        			return false;
        		} else {
        			var id = $("#" + documentListLayerWindow.actionObject).getGridParam('selarrrow');
        			for(var i = 0; i < id.length; i ++){	
        				
        				var rowId = $("#" + documentListLayerWindow.actionObject).getRowData(id[i]);
        				var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(rowId.acl_level));
        				
        				if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
        					jAlert("문서 이동 권한이 없습니다.", "이동", 0);
        					return false;
        				}			
        				if(rowId.lock_status == 'T'){
        					jAlert("체크아웃한 문서가 존재합니다.\n 체크아웃취소 후 다시 작업하시기 바랍니다.", "이동", 0);
        					return false;
        				}
        			}
        			
        			selectSingleFolderWindow.init(documentListLayerWindow.popupFolderCallback, getMapId, getWorkType, true, getDocType);
        		}
        	},
        	//문서 이동
        	documentDetailMove : function(status, getInfo){

        		documentListLayerWindow.actionName = "MOVE";
        		documentListLayerWindow.actionStatus = status;
        		documentListLayerWindow.actionInfo = getInfo;
        		documentListLayerWindow.actionObject = "{0}".format(documentListLayerWindow.gObjectID);
        		
        	/*	var getWorkType = "{0}".format(documentListLayerWindow.gWorkType);
        		var getMapId = "";
        		var getDocType = "";

        		if(getWorkType == 'WORK_MYDEPT'){
        			getMapId = 'MYDEPT';
        			getDocType = 'ALL_TYPE';
        		} else if (getWorkType == 'WORK_ALLDEPT'){
        			getMapId = 'MYDEPT';
        			getDocType = 'REPORT';
        		} else if (getWorkType == 'WORK_PROJECT'){
        			getMapId = 'PROJECT';
        			getDocType = 'XR_DOCUMENT';
        		} else if (getWorkType == 'null') {
        			getMapId = 'MYPAGE';
        			getDocType = 'ALL_TYPE';
        		}*/
        		
        		if(status != "ONLY" && status != "DETAIL" && !exsoft.util.grid.gridSelectCheck(documentListLayerWindow.actionObject)) {
        			jAlert("이동할 문서를 선택하세요", "이동", 0);
        			return false;
        		} else {
        			/*var id = $("#" + documentListLayerWindow.actionObject).getGridParam('selarrrow');
        			for(var i = 0; i < id.length; i ++){	
        				
        				var rowId = $("#" + documentListLayerWindow.actionObject).getRowData(id[i]);
        				var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(rowId.acl_level));
        				
        				if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
        					jAlert("문서 이동 권한이 없습니다.", "이동", 0);
        					return false;
        				}			
        				if(rowId.lock_status == 'T'){
        					jAlert("체크아웃한 문서가 존재합니다.\n 체크아웃취소 후 다시 작업하시기 바랍니다.", "이동", 0);
        					return false;
        				}
        			}*/
        			
        			selectSingleFolderWindow.Detailinit(documentListLayerWindow.popupFolderCallback);
        		}
        	},
        	// 문서 복사
        	documentCopy : function(status, getInfo){

        		documentListLayerWindow.actionName = "COPY";
        		documentListLayerWindow.actionStatus = status;
        		documentListLayerWindow.actionInfo = getInfo;
        		documentListLayerWindow.actionObject = "{0}".format(documentListLayerWindow.gObjectID);

        		var getWorkType = "{0}".format(documentListLayerWindow.gWorkType);
        		var getMapId = "";
        		var getDocType = "";

        		if(getWorkType == 'WORK_MYDEPT'){
        			getMapId = 'MYDEPT';
        			getDocType = 'ALL_TYPE';
        		} else if (getWorkType == 'WORK_ALLDEPT'){
        			getMapId = 'MYDEPT';
        			getDocType = 'REPORT';
        		} else if (getWorkType == 'WORK_PROJECT'){
        			getMapId = 'PROJECT';
        			getDocType = 'XR_DOCUMENT';
        		} else if (getWorkType == 'null') {
        			getMapId = 'MYDEPT';
        			getDocType = 'ALL_TYPE';
        			getWorkType = 'WORK_MYDEPT';
        		}
        		
        		if(status != "ONLY" && status != "DETAIL" && !exsoft.util.grid.gridSelectCheck(documentListLayerWindow.actionObject)) {
        			jAlert("복사할 문서를 선택하세요", "복사", 0);
        			return false;
        		} else {
        			var id = $("#" + documentListLayerWindow.actionObject).getGridParam('selarrrow');
        			for(var i = 0; i < id.length; i ++){	
        				var rowId = $("#" + documentListLayerWindow.actionObject).getRowData(id[i]);
        				var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(rowId.acl_level));
        				
        				if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
        					jAlert("문서 복사 권한이 없습니다.", "복사", 0);
        					return false;
        				}
        				if(rowId.lock_status == 'T'){
        					jAlert("체크아웃한 문서가 존재합니다.\n 체크아웃취소 후 다시 작업하시기 바랍니다.", "복사", 0);
        					return false;
        				}
        			}
        			
        			selectSingleFolderWindow.init(documentListLayerWindow.popupFolderCallback, getMapId, getWorkType, true, getDocType);
        		}
        	},
        	
        	// 첨부파일 모두 저장
        	downloadAllFiles : function() {
        		if (documentListLayerWindow.functions.checkAclLevel()) {
        			$(location).attr("href", exsoft.contextRoot + '/mypage/tempDownload.do?docList=[{"doc_id":"'+ documentListLayerWindow.currentGridRow.doc_id + '"}]&isZip=T' );
        		} else {
        			jAlert("첨부파일 다운로드 권한이 없습니다.");
        		}
        	},
        	
        	// 첨부파일 저장
        	downloadFile : function(page_id) {
        		if (documentListLayerWindow.functions.checkAclLevel()) {
        			var jsonArr = [];
        			var jsonArrIndex = 0;
        			var jsonData = {page_id:""};
        			
        			jsonData['page_id'] = page_id;
        			if(jsonData.page_id){
        				jsonArr[jsonArrIndex] = jsonData;
        				jsonArrIndex++;
        			}
        			
        			$(location).attr("href", exsoft.contextRoot + "/common/downLoad.do?pageList="+JSON.stringify(jsonArr));		
        		} else {
        			jAlert("첨부파일 다운로드 권한이 없습니다.");
        		}
        	},
        	
        	// 관련문서 상세보기
        	popupDocumentDetail : function(docId, aclLevel) {
        		// 권한 체크
        		if (aclLevel == Constant.ACL.ACL_NONE || aclLevel == Constant.ACL.ACL_BROWSE) {
        			jAlert("관련 문서에 대한 조회 권한이 없습니다.");
        		} else {
        			jAlert("관련 문서 상세조회가 팝업될 예정");
        			// 문서 상세 조회를 팝업한다
        			// documentDetail.init(docId);
        		}
        	}
        },

        functions : {
        	checkAclLevel : function() {
        		var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(documentListLayerWindow.currentGridRow.acl_level));
        		
        		if (exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("READ")) {
        			return false;
        		} else {
        			return true;
        		}
        	}
        },
        
        //5. 화면 UI 변경 처리
        ui : {
        	getLayerPosition : function (target, id) {
        	    var tInput  = $("#" + target).offset();
        	    var tHeight = $("#" + target).outerHeight();
        	    var tWidth  = $("#" + target).outerWidth();

        	    if(tInput != null){
    	            $("#" + id).css({"top":tInput.top+tHeight , "left":tInput.left});
        	    }
        	},
        	
        	
        
        },

        //6. callback 처리
    	popupFolderCallback : function(returnFolder){

    		var targetFolderId = returnFolder.id;	
    		var jsonArr = [];
    		var jsonArrIndex = 0;
    		
    		var actionType = "";
    		var actionText = "";
    		
    		if(documentListLayerWindow.actionName == "MOVE") {
    			actionType = "MOVE";
    			actionText = '이동';
    		} else {
    			actionType = "COPY";
    			actionText = '복사';
    		}
    		var postData;
    		
    		if(documentListLayerWindow.actionObject == 'mypageDocList') {
    			if(exsoftMypageFunc.listType == 'OWNER') {
					postData = {
							LIST_TYPE:'OWNER', 
							page_init:'true', 
							map_id:exsoft.util.layout.getSelectBox('myPageDoc_folder','option')
							};	
				} else if(exsoftMypageFunc.listType == 'SHARE_FOLDER') {
					postData = {
							LIST_TYPE: "SHARE_FOLDER", 
							folder_id:exsoftMypageFunc.folder_id
							};
				} else if(exsoftMypageFunc.listType == 'SHARE') {
					postData = {
							LIST_TYPE: "SHARE"
							};
				} else if(exsoftMypageFunc.listType == 'FAVORITE') {
					postData = {
							LIST_TYPE: "FAVORITE", 
							folder_id:exsoftMypageFunc.folder_id, 
							is_virtual:exsoftMypageFunc.is_virtual
							};
				}
    		}
    		
    		if(documentListLayerWindow.actionStatus != "ONLY" && documentListLayerWindow.actionStatus != "DETAIL"){
    			var id = $("#" + documentListLayerWindow.actionObject).getGridParam('selarrrow');
    			
    			for(var i = 0; i < id.length; i ++){			
    				var rowData = {doc_id : "", doc_name : "", is_locked : "",	root_id : "", doc_type : "", is_inherit_acl : "", folder_id : ""};
    				var rowId = $("#" + documentListLayerWindow.actionObject).getRowData(id[i]);
    				
    				//jsonObject 
    				rowData['doc_id'] = rowId.doc_id;
    				rowData['doc_name'] = exsoft.util.common.stripHtml(rowId.doc_name);
    				rowData['is_locked'] = rowId.lock_status;
    				rowData['root_id'] = rowId.root_id;
    				rowData['doc_type'] = rowId.doc_type;
    				rowData['is_inherit_acl'] = rowId.is_inherit_acl;
					rowData['folder_id'] = rowId.folder_id;
					
    				if(documentListLayerWindow.actionObject == 'workDocList' || documentListLayerWindow.actionObject == 'myDocument_Gridlist'){
    					rowData['folder_id'] = documentListLayerWindow.gCurrentFolderId;
    				} else {
    					rowData['folder_id'] = rowId.folder_id;	
    				}		
    				if(rowData.doc_id){
    					jsonArr[jsonArrIndex] = rowData;
    					jsonArrIndex ++;
    				}
    			}
    		} else {
    			jsonArr = documentListLayerWindow.actionInfo;
    		}
    		
    		if(jsonArr.length > 0){
    			jConfirm('문서를 '+ actionText +'하시겠습니까?' , actionText, 0,
    				function(r) {
    					var jsonObject = {"type":actionType, "targetFolderId":targetFolderId, "docList":JSON.stringify(jsonArr)};
    						if(r) {
    							exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/document/documentControl.do', actionType,
    								function(data, e){
    									if(data.result == 'true'){
    										if(documentListLayerWindow.actionStatus == "DETAIL")
    											exsoft.util.layout.popDivLayerClose.layerClose('detail_documentWindow');
    										if(documentListLayerWindow.actionObject == 'mypageDocList'){
    											exsoft.util.grid.gridPostDataInitRefresh(documentListLayerWindow.actionObject, exsoft.contextRoot + '/mypage/authDocumentList.do', postData);
    										} else {
    											exsoft.util.grid.gridRefresh(documentListLayerWindow.actionObject, exsoft.contextRoot + '/document/workDocumentList.do');	
    										}	
    										exsoft.util.common.noty("문서 " + actionText + " 완료되었습니다.");
    										jAlert("문서 " + actionText + " 완료되었습니다.", actionText, 0);
    									} else {
    										jAlert(data.message, actionText, 0);
    									}
    								}	
    							);						
    						}
    				}
    			)				
    		}
    	},

}