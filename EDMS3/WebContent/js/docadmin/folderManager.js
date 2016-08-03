var folderManager = {
		
		gWorkType : "WORK_MYDEPT",			// 현재 선택된 탭 workType
		groupTree : null,							// 그룹 트리
		projectTree : null,							// 프로젝트 트리
		gFolderTree : null, 						// 나의 부서 JStree object
		
		binder : new DataBinder("#form_details"),
		
		// 0. 초기화
		init : function(workType) {
			var treeOption = null;
			// 트리 초기화
			if (workType == Constant.WORK_MYDEPT) {
				treeOption = {
					divId : "#myDeptFolderTree",
					context : exsoft.contextRoot,
					url : "/folder/folderList.do",
					mapId : Constant.MAP_MYDEPT,
					workType : Constant.WORK_MYDEPT
				};
				
				if (folderManager.groupTree == undefined) {
					folderManager.groupTree = new XFTree(treeOption);
					folderManager.groupTree.callbackSelectNode = folderManager.callback.selectFolder;
					folderManager.groupTree.init(); //부서 rootId는 서버에서 처리		
				} else {
					folderManager.groupTree.refresh();
				}
				folderManager.gFolderTree = folderManager.groupTree;
			} else {
				treeOption = {
					divId : "#projectFolderTree",
					context : exsoft.contextRoot,
					url : "/folder/folderList.do",
					mapId : Constant.MAP_PROJECT,
					workType : Constant.WORK_PROJECT
				};
				
				if (folderManager.projectTree == undefined) {
					folderManager.projectTree = new XFTree(treeOption);
					folderManager.projectTree.callbackSelectNode = folderManager.callback.selectFolder;
					folderManager.projectTree.init(); //부서 rootId는 서버에서 처리
				} else {
					folderManager.projectTree.refresh();
				}
				folderManager.gFolderTree = folderManager.projectTree;
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
			// 구분 셀렉트박스 변경시
			changeWorkType : function(work_type) {
				
				if(work_type == "WORK_MYDEPT") {
					$("#myDeptFolderTree").removeClass("hide");
					$("#projectFolderTree").addClass("hide");
				} else {
					$("#projectFolderTree").removeClass("hide");
					$("#myDeptFolderTree").addClass("hide");
				}
				
				folderManager.init(work_type);
			},
			
			// 스토리지 할당량 체크박스 클릭시
			folderQuotaCheckBox : function(){
				if($("input:checkbox[id='folder_storage_quota_chk']").is(":checked") == false) {
					$('#storage_quota').prop("readonly", false);
					$('#storage_quota').prop("disabled", false);
					$('#storage_quota').removeClass("readonly");
					$('#storage_quota').val("");
				} else {
					$('#storage_quota').prop("readonly", true);
					$('#storage_quota').prop("disabled", true);
					$('#storage_quota').addClass("readonly");
					$('#storage_quota').val("무제한");		
				}
			},
			
			//폴더 상세조회 저장 버튼
			submitUpdate : function() {	
				if(folderManager.event.validate('form_details')) {
					folderManager.binder.set("type","UPDATE");
					folderManager.binder.set("is_save",exsoft.util.layout.getSelectBox('is_save','option'));
					folderManager.binder.set("folder_status",exsoft.util.layout.getSelectBox('folder_status','option'));
					folderManager.binder.set("is_type",exsoft.util.layout.getSelectBox('is_type','option'));
					
					var jsonObject = folderManager.binder.getDataToJson();
					
					exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject ,exsoft.contextRoot + '/folder/folderControl.do', "folderUpdate",
					function(data, param) {
						if(data.result == 'true') {
							folderManager.gFolderTree.refreshNodeForAddChildren(data.refresh_id);
							folderManager.gFolderTree.refreshNodeForAddChildren(data.target_refresh_id);
							jAlert(data.message, "폴더관리", 0);
						}else {
							jAlert(data.message, "폴더관리", 0);
						}
					});
				}	
			},

			//폴더 상세조회 취소 버튼
			cancelUpdate : function() {
				// 폴더 기본정보 조회
				var jsonData = folderManager.binder.getDataToJson();
				exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonData, exsoft.contextRoot + "/folder/folderDetail.do", "folderDetail", folderManager.callback.showDetail);
				jsonData = null;
			},
			
			// 등록 및 수정 정합성 체크
			validate : function(formId) {
				var obj = $('#'+formId);
				//폴더명 체크
				if($.trim(obj.find('input[name=folder_name_ko]').val()) === ''){
					jAlert('폴더명을 입력 하세요.', "폴더관리", 0);
					return false;
				}
				
				//정렬순서
				if($.trim(obj.find('input[name=sort_index]').val()) === ''){
					jAlert('정렬값을 입력 하세요.', "폴더관리", 0);
					return false;
				}
				
				//스토리지 할당량
				if($.trim(obj.find('input[name=storage_quota]').val()) === ''){
					jAlert('스토리지 할당량을 입력 하세요.', "폴더관리", 0);
					return false;
				} 
				
				//상속여부		
				if(obj.find('input[name=is_inherit_acl_chk]').is(':checked')){
					$('#create_is_inherit_acl').val('T');
				}else{
					$('#create_is_inherit_acl').val('F');
				}

				return true;

			},
			
			// 등록버튼 클릭시
			registFolder : function() {
				var node = folderManager.gFolderTree.getCurrentNode();
				folderWindow.callback = folderManager.callback.refreshTree;
				folderWindow.initForm(node);
				folderWindow.binder.set("type", Constant.ACTION_CREATE);
				folderWindow.open();
			},
			
			// 이동버튼 클릭시
			moveFolder : function() {
				var folder_type = $('#folder_type').val();
				if(folder_type == 'DOCUMENT') {
					selectSingleFolderWindow.init(folderManager.callback.moveFolderCallback, "MYDEPT", "WORK_MYDEPT", true, "ALL_TYPE");
				} else {
					jAlert('부서 및 프로젝트 Type 폴더는 이동 할 수 없습니다.', "폴더관리", 0);
				}
				folder_type = null;
			},
			
			 // 삭제버튼 클릭시
			 deleteFolder : function() {
				var folder_type = $('#folder_type').val();
				if(folder_type != 'DOCUMENT') {
					jAlert('부서 및 프로젝트 Type 폴더는 삭제 할 수 없습니다.', "폴더관리", 0);
					return;
				}

				var jsonData = {
					type : Constant.ACTION_DELETE,
					folder_id : $("#folder_id").val(),
					folder_name_ko : $("#folder_name_ko").val()			
				};
				
				// 폴더 기본정보 조회
				exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonData, exsoft.contextRoot + "/folder/folderControl.do", "folderDelete", 
				function(data, param) {
					if(data.result == 'true'){
						folderManager.callback.refreshTree();
						jAlert('폴더 삭제에 성공하였습니다.', "폴더관리", 0);
					}else {
						jAlert(data.message, "폴더관리", 0);
					}
				});
			},
			
			// 권한변경버튼 클릭시
			changeAcl : function() {
				selectAclWindow.init($("#acl_id").val(), Constant.ACL.TYPE_FOLDER, folderManager.callback.selectAclSubmit);
				var obj = {
					current_acl_id : $('#acl_id').val(),
					current_acl_name : $('#acl_name_title').text(),
					parent_folder_id : $('#parent_id').val(),
					folder_id : "",
					type : "folder"
				}
				selectAclWindow.initInherit(obj);
			}
		},

		//5. 화면 UI 변경 처리
		ui : {
			// 기본권한, 추가권한 출력
			aclItemData :  function(data,divIds) {
				exsoft.util.table.tableFolderAclItemPrintList('aclDocTable',data.aclItemList);
			},
			
			// 저장문서유형 세팅.
			docTypeData : function(data, param) {
				$("#is_type").remove();
				
				$('#docType_template').append('<select id="is_type" data-bind="is_type" data-select="true">');
				$.each(data.typeList, function(){
					$("#is_type").append("<option value='{0}'>{1}</option>".format(this.type_id, this.type_name));
				});
				$('#docType_template').append('</select>');
			}
		},

		//6. callback 처리
		callback : {
			// 폴더 선택시
			selectFolder : function(e, data) {
				//1. 문서목록에 폴더명 title 변경
				$("#folder_name_title").html(data.node.text);
				exsoft.util.ajax.ajaxDataFunctionWithCallback({folder_id : data.node.id}, exsoft.contextRoot + "/folder/folderDetail.do", "folderDetail", folderManager.callback.showDetail);
			},
			
			// 폴더상세
			showDetail : function(folderInfo, param) {
				if(folderInfo.result == 'true'){
					var folder = folderInfo.folderDetail;
					
					// storage Quota/Usage 사이즈 변환
					var getQuota = folder.storage_quota;
					var getUsage = folder.storage_usage;
					if(getQuota != -1){
						getQuota = getQuota/1024/1024/1024;
					}
					if(getUsage != -1){
						getUsage = exsoft.util.common.bytesToSize(getUsage, 1);
					}
					
					$("#folder_name_title").html(folder.folder_name_ko);
					$("#acl_name_title").html(folderInfo.aclDetail.acl_name);
					
					folderManager.binder.set("folder_name_ko", folder.folder_name_ko);
					folderManager.binder.set("folder_name_en", folder.folder_name_en);
					folderManager.binder.set("is_save", folder.is_save);
					folderManager.binder.set("folder_status", folder.folder_status);
					exsoft.util.ajax.ajaxDataFunctionWithCallback('', exsoft.contextRoot+'/folder/makeTypeSelectbox.do', '', function(data, param){
						$.when(folderManager.ui.docTypeData(data, param)).then(exsoft.util.common.ddslick('#is_type', 'srch_type1', 'is_type', 98, function(divId, selectedData){
							folderManager.binder.set("is_type", selectedData.selectedData.value);
						})).done(folderManager.binder.set("is_type", folder.is_type));
					});
					folderManager.binder.set("sort_index", folder.sort_index);
					$('#is_inherit_acl_chk').prop('checked',folder.is_inherit_acl == 'T');
					folderManager.binder.set("is_inherit_acl", folder.is_inherit_acl);
					$("#folder_full_path").text(folderManager.gFolderTree.getCurrentNodeFullPath().join(" > "));
					$("#create_date").text(folder.create_date);
					$("#create_name").text(folder.creator_name);
					$("#storage_usage").text(getUsage);
					
					var node = folderManager.gFolderTree.getCurrentNode();		
					
					if((folder.map_id == folder.folder_type) 
							|| (folder.folder_type == 'DOCUMENT' && folder.map_id == "PROJECT" && node.parents.length < 3 )) {
						
						$('#folder_storage_quota_chk').prop("disabled", false);
						
						// storage_quota 셋팅
						if(folder.storage_quota != -1) {
							$('#folder_storage_quota_chk').prop("checked", false);
							$('#storage_quota').val(getQuota);
							$('#storage_quota').prop("readonly", false);
							$('#storage_quota').prop("disabled", false);
							$('#storage_quota').removeClass("readonly");
						} else {
							$('#folder_storage_quota_chk').prop("checked", true);
							$('#storage_quota').val("무제한");
							$('#storage_quota').prop("readonly", true);
							$('#storage_quota').prop("disabled", true);
							$('#storage_quota').addClass("readonly");
						}
					} else {
						$('#folder_storage_quota_chk').prop("checked", true);
						$('#folder_storage_quota_chk').prop("disabled", true);
						$('#storage_quota').val("무제한");
						$('#storage_quota').prop("readonly", true);
						$('#storage_quota').prop("disabled", true);
						$('#storage_quota').addClass("readonly");
					}
					
					//hidden값 set
					folderManager.binder.set("folder_id", folder.folder_id);
					folderManager.binder.set("parent_id", folder.parent_id);
					folderManager.binder.set("map_id", folder.map_id);
					folderManager.binder.set("acl_id", folder.acl_id);
					folderManager.binder.set("folder_type", folder.folder_type);
					
					// 접근자 List 셋팅
					$("#acl_name_title").html(folderInfo.aclDetail.acl_name);

					// 기본권한 :: default - 기본권한 사용안함 권한
					folderManager.ui.aclItemData(folderInfo,"aclDocTable");

				} else {
					jAlert(data.message, "폴더관리", 0);
				}
			},
			
			// 트리 새로고침
			refreshTree : function (e, data) {
				folderManager.gFolderTree.refresh();
			},
			
			// 권한변경이후 콜백
			selectAclSubmit : function(aclItemList) {
				folderManager.binder.set("acl_id", aclItemList.aclId);
				$("#acl_name_title").html(aclItemList.aclDetail.acl_name);
				exsoft.util.table.tableFolderAclItemPrintList('aclDocTable',aclItemList.aclItems);
			},
			
			// 폴더트리에서 이동 버튼 callback 함수
			moveFolderCallback : function(returnFolder) {
				if (returnFolder != null) {
					var isLoop = false;
					
					// 이동하려는 대상 폴더가 자신 및 자신의 하위 폴더인지 체크함
					var current_folder_id = $("#folder_id").val();
					$(returnFolder.parentIdList).each(function(index) {
						if (this == current_folder_id ) {
							alert("현재 폴더 및 현재 폴더 하위로 이동할 수 없습니다.", "폴더관리", 0);	
							isLoop = true;
							return false;
						}
					});
					
					var current_folder_object = folderManager.gFolderTree;	
					var originalRootFolder = current_folder_object.getFolderGroupId(current_folder_object.selectedNode[0]); 
					var targetRootFolder;
					var changeRootFolder = 'F';		
					if(returnFolder.mapId == "PROJECT" && returnFolder.parentGroup == null){
						targetRootFolder = returnFolder.id;
					} else {
						targetRootFolder = returnFolder.parentGroup.id;
					}
					if(originalRootFolder.id != targetRootFolder) {
						changeRootFolder = 'T';
					} 
					if (!isLoop) {
						$("#move_folder_id").val(current_folder_id);  // 변경할 현재 ID 저장
						$("#move_parent_id").val(returnFolder.id); // 선택된 부모 ID 저장
						$("#move_map_id").val(returnFolder.mapId); // 선택된 맵 ID 저장
						$("#move_folder_name_ko").val($("#folder_name_ko").val());			  // 변경할 폴더 이름
						$("#parentGroup_id").val(targetRootFolder);
						$("#root_folder_change").val(changeRootFolder);
						exsoft.util.ajax.ajaxFunctionWithCallback('form_move', exsoft.contextRoot + '/folder/folderControl.do','folderUpdate', 
						function(data, param) {
							if(data.result == 'true'){
								folderManager.gFolderTree.refreshNodeForAddChildren(data.refresh_id);
								folderManager.gFolderTree.refreshNodeForAddChildren(data.target_refresh_id);
								jAlert(data.message, "폴더관리", 0);
							}else {
								jAlert(data.message, "폴더관리", 0);
							}
						});
					}
				}
			}
		},
}