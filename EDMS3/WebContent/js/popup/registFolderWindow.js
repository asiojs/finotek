var folderWindow = {
		binder : new DataBinder("#registFolder"),
		wrapperClass : "subFolder_add_wrapper",
		layerClass : "subFolder_add",
		aclTableId : "#registFolderAclList",
		folderTypeId : "#registFolderType",
		aclDetail : null,
		callback : null,
		
		// 폼 초기화(생성 / 수정)
		initForm : function(node, folderId) {
			if (folderId === undefined) {
				// 생성 모드일 경우
				folderWindow.clearForm();
				folderWindow.binder.bindingElement();
				folderWindow.binder.set("parent_id", node.id);
				folderWindow.binder.set("map_id", node.original.map_id);
				folderWindow.binder.set("type", "CREATE");
				folderWindow.initFormCommon(node);
			} else if (folderId !== undefined) {
				// 수정 모드일 경우 폴더정보를 조회해서 화면에 출력
				folderWindow.ajax.getFolderInfo(folderId);
				folderWindow.initFormCommon(node);
			}
		},

		clearForm : function() {
			folderWindow.aclDetail = null;
			$("#registFolder input[data-bind=folder_id]").val("");
			
			$("#registFolder input[data-bind=storage_quota]").val("0");
			$("#registFolder input[data-bind=folder_name_ko]").val("");
			$("#registFolder input[data-bind=folder_name_en]").val("");
			$("#registFolder input[data-bind=sort_index]").val("");
			
			if ($("#storage_quotaCheckBox").is(":checked"))
				$("#storage_quotaCheckBox").click();
		},
		
		open : function() {
			exsoft.util.layout.divLayerOpen("subFolder_add_wrapper", "subFolder_add");
		},
		
		close : function() {
			exsoft.util.layout.divLayerClose("subFolder_add_wrapper", "subFolder_add");
		},
		
		// 폼 초기화 공통 메서드
		initFormCommon : function(node) {
			folderWindow.binder.set("full_path", node.full_path.join("/"));
			folderWindow.ajax.getTypes(node);
			folderWindow.ajax.getAclDetail(node.original.acl_id);
			
			//selectbox 처리
			folderWindow.initDdslick();
			
			// 최근 사용 폴더 목록
			folderWindow.ajax.getRecentlyFolderList();
		},
		
		initDdslick : function(){
			// 저장 여부
			exsoft.util.common.ddslick('#register_doc_save', 'register_doc_save', 'is_save', 98, function(divId, selectedData){
				//$("[data-bind=is_save]").val(selectedData.selectedData.value);
				folderWindow.binder.set("is_save", selectedData.selectedData.value);
				
			});
			
			// 사용여부
			exsoft.util.common.ddslick('#register_doc_use', 'register_doc_use', 'folder_status', 98, function(divId, selectedData){
				//$("[data-bind=folder_status]").val(selectedData.selectedData.value);
				folderWindow.binder.set("folder_status", selectedData.selectedData.value);
			});
			
		},
		
		initGetTypes : function(data, param){
			$(folderWindow.folderTypeId).remove();
			
			//registFolderType_template
			$('#registFolderType_template').append('<select id="registFolderType" data-bind="is_type" data-select="true">');
			$.each(data.typeList, function(){
				
				$(folderWindow.folderTypeId).append("<option value='{0}'>{1}</option>".format(this.type_id, this.type_name));
			});
			$('#registFolderType_template').append('</select>');
		},
		
		// 서버 Ajax 처리
		ajax : {
			getAclDetail : function(aclId) {
				exsoft.util.ajax.ajaxDataFunctionWithCallback({"acl_id" : aclId}, exsoft.contextRoot+"/permission/aclItemList.do", "", function(data, acl_id) {
					folderWindow.aclDetail = data.aclDetail;
					folderWindow.binder.set("acl_id", data.aclDetail.acl_id);
			   		
					//ui.drawAclTable(data, folderWindow.aclTableId);
					exsoft.util.table.tableFolderAclItemPrintList(folderWindow.aclTableId, data.list);
			   		folderWindow.ui.changeAclName(data.aclDetail.acl_name);
				})
			},
			getTypes : function(node) {
				exsoft.util.ajax.ajaxDataFunctionWithCallback('', exsoft.contextRoot+'/folder/makeTypeSelectbox.do', '', function(data, param){
					$.when(folderWindow.initGetTypes(data, param)).then(exsoft.util.common.ddslick('#registFolderType', 'register_saveDoc_type', 'is_type', 98, function(divId, selectedData){
						folderWindow.binder.set("is_type", selectedData.selectedData.value);
					})).done(folderWindow.binder.set("is_type", node.original.is_type));
				});
			},
			
			getFolderInfo : function(folderId) {
				exsoft.util.ajax.ajaxDataFunctionWithCallback({folder_id : folderId}, exsoft.contextRoot+"/folder/folderDetail.do", '', function(data, param){
					if (data.result == "true") {
						
						folderWindow.binder.binding(data.folderDetail);
						folderWindow.binder.set("type", "UPDATE");
						
						var _quota = folderWindow.binder.getDataToJson().storage_quota;
						
						if (_quota == "-1") {
							var cbStorageQuota = $("#storage_quotaCheckBox");
							if (!cbStorageQuota.is(":checked")) {
								cbStorageQuota.click();
							} else {
								var quotaObj = $("[data-bind=storage_quota]");
								quotaObj.prop("readonly", true);
								quotaObj.prop("disabled", true);
								quotaObj.addClass("readonly");
								quotaObj.removeAttr("ex-valid");
								quotaObj.val("무제한");
							}
						} else {
							var cbStorageQuota = $("#storage_quotaCheckBox");
							if (cbStorageQuota.is(":checked")) {
								cbStorageQuota.prop("checked", false);
								var quotaObj = $("[data-bind=storage_quota]");
								quotaObj.removeClass("readonly");
								quotaObj.prop("readonly", false);
								quotaObj.prop("disabled", false);
								quotaObj.attr("ex-valid", "digit");
							}
							folderWindow.binder.set("storage_quota", parseInt(_quota/1024/1024/1024)); // BTYE -> GB 로 단위 변환
						}
					} else {
						jAlert(data.message);
					}
				});
			},
			
			getRecentlyFolderList : function() {
				exsoft.util.ajax.ajaxDataFunctionWithCallback("", exsoft.contextRoot+"/folder/recentlyFolderList.do", '', function(data, param){
					if (data.result == "true") {
						folderWindow.ui.recentFolderList(data.recentlyFolderList);
					} else {
						jAlert(data.message);
					}
				});
			},
			
			deleteRecentlyFolder : function(idx) {
				exsoft.util.ajax.ajaxDataFunctionWithCallback({idx : idx}, exsoft.contextRoot+"/folder/recentlyFolderDelete.do", idx, function(data, deleteIdx){
					if (data.result == "true") {
						folderWindow.ui.removeRecentFolder(deleteIdx);
					} else {
						jAlert(data.message);
					}
				});
			}
		},
		
		// 화면 Event 처리
		event : {
			// 폴더 등록/수정
			submit : function() {
				if ($("#registFolder").validation()) {
					var _quota = folderWindow.binder.getDataToJson().storage_quota;
					
					if (_quota != "-1") {
						folderWindow.binder.set("storage_quota", _quota*1024*1024*1024); // GB -> BTYE 로 단위 변환
					}
					
					exsoft.util.ajax.ajaxDataFunctionWithCallback(folderWindow.binder.getDataToJson(), exsoft.contextRoot+"/folder/folderControl.do", "", function(data) {
						
						// 콜백 호출
						folderWindow.callback();
						exsoft.util.layout.divLayerClose(folderWindow.wrapperClass, folderWindow.layerClass);
					})
				} else {
					jAlert("validation 실패");
				}
				
			},
			// 스토리지 용량제한 체크/체크해제
			changeQuotaCheck : function(obj) {
				var checked = $(obj).is(":checked");
				var quotaObj = $("[data-bind=storage_quota]");
				
				quotaObj.prop("readonly", checked);
				quotaObj.prop("disabled", checked);

				if (checked) {
					quotaObj.addClass("readonly");
					quotaObj.removeAttr("ex-valid");
					folderWindow.binder.set("storage_quota", "-1");
				} else {
					quotaObj.removeClass("readonly");
					quotaObj.attr("ex-valid", "digit");
					folderWindow.binder.set("storage_quota", "");
				}
				
				quotaObj.val(checked ? "무제한" : "0");
			},
			
			// 상위 폴더 선택
			selectParentFolder : function() {
				if (folderWindow.binder.get("map_id") == "MYPAGE") {
					selectSingleFolderWindow.init(folderWindow.callbackFunctions.selectParentFolder, Constant.MAP_MYPAGE, Constant.WORK_MYPAGE, true, "ALL_TYPE");
				} else {
					selectSingleFolderWindow.init(folderWindow.callbackFunctions.selectParentFolder);
					
				}
			},
			
			// 권한 변경 선택
			changeFolderAcl : function() {
				
				var _folderInfo = folderWindow.binder.getDataToJson();
				var _aclId = folderWindow.binder.get("acl_id");
				selectAclWindow.init(_aclId, Constant.ACL.TYPE_FOLDER, folderWindow.callbackFunctions.selectAcl);
				
				var _obj = {
					current_acl_id : folderWindow.aclDetail.acl_id,
					current_acl_name : folderWindow.aclDetail.acl_name,
					parent_folder_id : folderWindow.binder.get("parent_id"),
					folder_id : "",
					type : "folder"
				};
				
				selectAclWindow.initInherit(_obj);
			},
			
			// 최근 폴더 삭제
			deleteRecentlyFolder : function(idx) {
				folderWindow.ajax.deleteRecentlyFolder(idx);
			}
			
		},
		callbackFunctions : {
			selectParentFolder : function(nodeInfo) {
				folderWindow.binder.set("full_path", nodeInfo.full_path.join("/"));
				folderWindow.binder.set("parent_id", nodeInfo.id);
			},
			selectAcl : function(aclInfo) {
				folderWindow.ajax.getAclDetail(aclInfo.aclId);
			}
		},
		ui : {
			changeAclName : function(aclName) {
				$("#aclName").html(aclName);
			},
			
			recentFolderList : function(list) {
				$("#recentFolderList").empty();
				var _str = "";
				
				$(list).each(function() {
					_str += "<li id='{0}'>".format(this.idx);
					_str += "<a href='#' onclick='folderWindow.ajax.getFolderInfo(\"{0}\");'>{1}</a>".format(this.target_id, this.target_name);
					_str += "<a href='#' class='recent_del' onclick='folderWindow.event.deleteRecentlyFolder(\"{0}\")'><img src='{1}/img/icon/recent_doc_del.png'></a>".format(this.idx, exsoft.contextRoot);
					_str += "</li>";
				});
				
				$("#recentFolderList").append(_str);
			},
			
			removeRecentFolder : function(idx) {
				$("#"+idx).remove();
			}
		},
}