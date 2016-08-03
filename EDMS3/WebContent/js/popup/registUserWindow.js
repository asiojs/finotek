var registUserWindow = {
		
		type : null, // 등록(userWrite) / 수정(userUpdate) 구분
		callbackFunction : null, // 콜백
		binder : new DataBinder("#pop_user_form"),
		
		// 0. 초기화
		init : {
			// 사용자 등록 팝업창 팝업
			initRegistUserWindow : function(groupName, groupId) {
				
				// 팝업창 폼 초기화
				exsoft.util.common.formClear("pop_user_form");
				registUserWindow.binder.set("jobtitle", "P001"); // 사원
				registUserWindow.binder.set("position", "D001"); // 팀원
				registUserWindow.binder.set("role_id", "CREATOR"); // 작성자
				registUserWindow.binder.set("user_status", "C"); // 사용
				
				// 사용자 ID 입력 가능하도록 처리
				$("#pop_user_id").prop("readonly", false);
				
				// 전달값 셋팅
				registUserWindow.binder.set("group_id", groupId);
				registUserWindow.binder.set("manage_group", groupId);
				registUserWindow.binder.set("group_nm", groupName);
				registUserWindow.binder.set("manage_group_text", groupName);
				
				// UI 셋팅
				$("#popupTitle").html("사용자 추가");
				$("#pop_btn_deleteUser").hide();
				$('#pop_storage_quota_chk').prop("checked", true);
				$('#pop_storage_quota').val("무제한");
				registUserWindow.binder.set("storage_quota", -1);
				$('#pop_storage_quota').prop("readonly", true);
				$('#pop_storage_quota').prop("disabled", true);
				$("#pop_storage_usage_info").addClass("hide");
				$("#pop_storage_quota").addClass("readonly");
				$("#pop_group_nm").addClass("readonly");
				$("#pop_user_id").removeClass("readonly");

				// 타입 설정
				registUserWindow.type = "userWrite";
				registUserWindow.binder.set("type", "insert");
				
				exsoft.util.filter.maxNumber();
				
				registUserWindow.open();
			},
			
			// 사용자 조회 / 수정 팝업
			initUpdateUserWindow : function(userId, callback) {
				// callback 
				registUserWindow.callbackFunction = callback;
				
				// 유저 정보 조회
				exsoft.util.ajax.ajaxDataFunctionWithCallback({user_id:userId, type:'select'}, exsoft.contextRoot + "/admin/userInfoManager.do", "userDetail", registUserWindow.callback)
				
				// 팝업창 폼 클리어
				exsoft.util.common.formClear("pop_user_form");
				
				// 사용자 ID 입력 불가능하도록 처리
				$("#pop_user_id").prop("readonly", true);
				
				// UI 셋팅
				$("#popupTitle").html("사용자 수정");
				$("#pop_btn_deleteUser").show();
				$("#pop_storage_usage_info").show();
				$("#pop_user_id").addClass("readonly");
				$("#pop_group_nm").addClass("readonly");
				// 타입 설정
				registUserWindow.type = "userUpdate";
				registUserWindow.binder.set("type", "update");
				
				exsoft.util.filter.maxNumber(); 
				
				registUserWindow.open();
			}
		},

		// 1. 팝업
		open : function() {
			exsoft.util.layout.divLayerOpen("user_regist_wrapper", "user_regist");
		},

		//2. layer + show
		layer : {
		},

		//3. 닫기 + hide
		close : function() {
			exsoft.util.layout.divLayerClose('user_regist_wrapper', 'user_regist');
		},

		//4. 화면 이벤트 처리
		event : {
			
			// 확인버튼 클릭시
			registUser : function() {
				// 필수항목 유효성 체크
				if (!registUserWindow.event.validationForm()) {
					return;
				}		
				// 스토리지 할당량 변환
				var quota = 0;
				if($("#pop_storage_quota").val() > -1) {
					quota = $("#pop_storage_quota").val() *1024*1024*1024; // 숫자값을 입력했을때
				} else {
					quota = $("#storage_quota").val() *1024*1024*1024; //무제한일때
				}
				registUserWindow.binder.set("storage_quota", quota);
				
				registUserWindow.binder.set("user_id", $("#pop_user_id").val());
				registUserWindow.binder.set("user_name_ko", $("#pop_user_name_ko").val());
				registUserWindow.binder.set("user_name_en", $("#pop_user_name_en").val());
				registUserWindow.binder.set("user_name_zh", $("#pop_user_name_zh").val());
				registUserWindow.binder.set("jobtitle", exsoft.util.layout.getSelectBox('pop_jobtitle','option'));
				registUserWindow.binder.set("position", exsoft.util.layout.getSelectBox('pop_position','option'));
				registUserWindow.binder.set("role_id", exsoft.util.layout.getSelectBox('pop_role_id','option'));
				registUserWindow.binder.set("telephone", $("#pop_telephone").val());
				registUserWindow.binder.set("user_status", exsoft.util.layout.getSelectBox('pop_user_status','option'));
				registUserWindow.binder.set("email", $("#pop_email").val());
				
				var jsonObject = registUserWindow.binder.getDataToJson();
				
				// 서버로 전송
				exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject ,exsoft.contextRoot + '/admin/userInfoManager.do',registUserWindow.type,
				function(data,param){
					if (data.result == "success") {
						// 목록 갱신
						userManager.event.searchUserList();
						
						if (param == "userUpdate") {
							jAlert("사용자 수정이 완료 됐습니다.", "사용자 관리", 0);
							registUserWindow.callbackFunction(data, param);
						} else if(param == "userWrite") {
							jAlert("사용자 등록이 완료 됐습니다.", "사용자 관리", 0);
						}
						
					} else {
						jAlert(data.message, "사용자 관리", 0);
					}
				});
				// 팝업 숨김
				registUserWindow.close();
			},
			
			// 관리부서 선택
			selectManageGroup : function() {
				// Role이 본부 문서 관리자인지 체크
				if (exsoft.util.layout.getSelectBox('pop_role_id','option') != "HEAD_DOC_OPERATOR") {
					jAlert("선택한 역할에 폴더 관리 권한이 없어서 관리부서를 선택할 수 없습니다.", "사용자 관리", 0);
					return;
				}
				selectGroupWindow.init.initPage(registUserWindow.selectManageGroupCallback, "GROUP");
			},
			
			// Form Validation start
			validationForm : function() {
				
				// 사용자 ID 체크
				if ($("#pop_user_id").val().length == 0) {
					jAlert("사용자 ID를 입력해주세요", "사용자 관리", 0);
					$("#pop_user_id").focus();
					return;
				} else if (!exsoft.util.check.userIdCheck($("#pop_user_id").val())) {
					jAlert("사용자 아이디는 영문이나 숫자로만 입력해주세요", "사용자 관리", 0);
					return;
				}
				
				// 사용자 성명 체크
				if ($("#pop_user_name_ko").val().length == 0) {
					jAlert("사용자 성명을 입력해주세요", "사용자 관리", 0);
					$("#pop_user_name_ko").focus();
					return false
				}
				
				// 이메일 유효성 체크
				if ($("#pop_email").val().length > 0 && !exsoft.util.check.emailCheck($("#pop_email").val())) {
					jAlert("이메일주소가 정확하지 않습니다. 다시입력해주세요.", "사용자 관리", 0);
					return;
				}
				
				// 전화번호 유효성 체크
				if ($("#pop_telephone").val().length > 0 && !exsoft.util.check.phoneCheck($("#pop_telephone").val())) {
					jAlert("전화번호가 정확하지 않습니다. 다시 입력해주세요.", "사용자 관리", 0)
					return;
				}
				
				// 기본 값 외 서버로 전송 할 값 설정
				$("#pop_position_nm").val($("#pop_position option:selected").text());
				
				return true;
			},
			
			userQuotaCheckBox : function() {
				if($("input:checkbox[id='pop_storage_quota_chk']").is(":checked") == false){
					$('#pop_storage_quota').prop("readonly", false);
					$('#pop_storage_quota').prop("disabled", false);
					$("#pop_storage_quota").removeClass("readonly");
					$("#pop_storage_quota").val("");
				} else {
					$('#pop_storage_quota').prop("readonly", true);
					$('#pop_storage_quota').prop("disabled", true);
					$("#pop_storage_quota").addClass("readonly");
					registUserWindow.binder.set("storage_quota", "-1");
					$("#pop_storage_quota").val("무제한");
				}
			},
			// Form Validation end
			
			// 삭제
			deleteUser : function() {
				jConfirm($("#pop_user_name_ko").val() + "를 삭제 하시겠습니까 ?", "사용자 관리", 0,
					function(ret) {
						if (ret) {
							exsoft.util.ajax.ajaxDataFunctionWithCallback({userIdList:$("#pop_user_id").val(), type:"delete"}, exsoft.contextRoot + "/admin/userInfoManager.do", "userDelete", registUserWindow.callback);
						}
				});
			}
		},

		//5. 화면 UI 변경 처리
		ui : {
		},

		//6. callback 처리
		callback : function(data, param) {
			if (data.result == "success") {
				if (param == "userDetail") {
					// storage Quota/Usage 사이즈 변환
					var quota = data.userInfo.storage_quota;
					var usage = data.userInfo.storage_usage;
					if (quota != -1) {
						quota = quota/1024/1024/1024;
					}			
					if (usage > 0){
						usage = exsoft.util.common.bytesToSize(usage, 1);
					}
					
					registUserWindow.binder.set("type", "update");
					registUserWindow.binder.set("group_id", data.userInfo.group_id);
					registUserWindow.binder.set("group_nm", data.userInfo.group_nm);
					registUserWindow.binder.set("user_id", data.userInfo.user_id);
					registUserWindow.binder.set("user_name_ko", data.userInfo.user_name_ko);
					registUserWindow.binder.set("user_name_en", data.userInfo.user_name_en);
					registUserWindow.binder.set("user_name_zh", data.userInfo.user_name_zh);
					registUserWindow.binder.set("jobtitle", data.userInfo.jobtitle);
					registUserWindow.binder.set("position", data.userInfo.position);
					registUserWindow.binder.set("role_id", data.userInfo.role_id);
					registUserWindow.binder.set("telephone", data.userInfo.telephone);
					registUserWindow.binder.set("user_status", data.userInfo.user_status);
					registUserWindow.binder.set("email", data.userInfo.email);
					registUserWindow.binder.set("manage_group", data.userInfo.manage_group);
					registUserWindow.binder.set("manage_group_text", data.userInfo.manage_group_nm);
					$("#pop_storage_usage").text(usage);
					if(quota > -1){
						$('#pop_storage_quota_chk').prop("checked", false);
						registUserWindow.binder.set("storage_quota", quota);
						$("#pop_storage_quota").val(quota);
						$('#pop_storage_quota').prop("readonly", false);
						$('#pop_storage_quota').prop("disabled", false);
						$("#pop_storage_quota").removeClass("readonly");
					} else {
						$('#pop_storage_quota_chk').prop("checked", true);
						registUserWindow.binder.set("storage_quota", "-1");
						$("#pop_storage_quota").val("무제한");
						$('#pop_storage_quota').prop("readonly", true);
						$('#pop_storage_quota').prop("disabled", true);
						$("#pop_storage_quota").addClass("readonly");
					}
					
				} else if (param == "userDelete") {
					
					// 팝업 숨김
					registUserWindow.close();
					
					// 목록 갱신
					userManager.event.searchUserList();
					
					jAlert("사용자를 삭제했습니다.", "사용자 관리", 0);
				}
			} else {
				jAlert(data.message, "사용자 관리", 0);
			}
		},
		
		// 관리부서 선택 콜백
		selectManageGroupCallback : function(returnGroup) {
			registUserWindow.binder.set("manage_group", returnGroup[0].original.id);
			registUserWindow.binder.set("manage_group_text", returnGroup[0].original.text);
		}
}