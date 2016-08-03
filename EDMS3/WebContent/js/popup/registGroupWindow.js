var registGroupWindow = {

		groupInfo : new Object(),
		groupWriteBinder : new DataBinder("#p_group_form"),

		// 0. 초기화
		init : function(fullPath, parentId, mapId) {

			// 등록버튼 클릭, 하위부서 추가 - 창 닫기
    	    $('.window_close').bind("click", function(e){
    	    	e.preventDefault();
    	    	$(this).parents('.subDept_add').addClass('hide');
    	    	$('.subDept_add_wrapper').addClass('hide');
    	    });

    	    // 등록버튼 클릭, 하위부서 추가 -  창 닫기 : 음영진 부분 클릭 시 닫기
    	    $('.subDept_add_wrapper').bind("click", function(){
    	    	$(this).addClass('hide');
    	    	$('.subDept_add').addClass('hide');
    	    });

			// 팝업창 form clear
    	    exsoft.util.common.formClear("p_group_form");

			// parameter 저장
    	    registGroupWindow.groupInfo.type = "groupWrite";
    	    registGroupWindow.groupInfo.fullPath = fullPath;
    	    registGroupWindow.groupInfo.parentId = parentId;
    	    registGroupWindow.groupInfo.mapId = mapId;

    	    // DataBinder 처리
			registGroupWindow.groupWriteBinder.set("parent_id",parentId);
			registGroupWindow.groupWriteBinder.set("group_full_path",fullPath);
			registGroupWindow.groupWriteBinder.set("map_id",mapId);
			registGroupWindow.groupWriteBinder.set("p_group_status","C");

			exsoft.util.common.ddslick('#p_group_status','use_yn','p_group_status',79, function(){});

			exsoft.util.layout.divLayerOpen("subDept_add_wrapper", "subDept_add");

			exsoft.util.filter.maxNumber();
		},

		// 1. 팝업
		open : {
		},

		//2. layer + show
		layer : {
		},

		//3. 닫기 + hide
		close : function() {
			exsoft.util.layout.divLayerClose("subDept_add_wrapper", "subDept_add");
		},

		//4. 화면 이벤트 처리
		event : {

			selectGroup : function() {
				alert("그룹선택팝업 미처리");
			},

			// 버튼 : 그룹 저장
			registGroupSubmit : function() {

				// 필수항목 유효성 체크
				if (!registGroupWindow.event.validationForm()) {
					return;
				}

				registGroupWindow.groupWriteBinder.set("type","insert");
				registGroupWindow.groupWriteBinder.set("group_name_ko",$("#p_group_name_ko").val());
				registGroupWindow.groupWriteBinder.set("group_name_en",$("#p_group_name_en").val());
				registGroupWindow.groupWriteBinder.set("sort_index",$("#p_sort_index").val());
				registGroupWindow.groupWriteBinder.set("group_status",exsoft.util.layout.getSelectBox('p_group_status','option'));

				// Server Call
				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(registGroupWindow.groupWriteBinder.getDataToJson() ,
						exsoft.contextRoot +exsoft.contextRoot +"/admin/groupInfoManager.do", "groupWrite",
					function(data, param) {
						if(data.result == "success") {
							groupManager.event.refreshTree();		// 그릅관리 트리를 초기화 한다
						} else {
							jAlert(data.message, "그룹관리", 0);
						}
				});

				// 창닫기
				registGroupWindow.close();
			},

			// Form Validation
			validationForm : function() {

				// 부서명 체크 (group_name_ko컬럼만 NN이므로 해당 컬럼만 체크)
				if ($("#p_group_name_ko").val().length == 0) {
					jAlert("부서명을 입력해주세요.");
					return false;
				}

				// 정렬순서 체크
				if ($("#p_sort_index").val().length == 0) {
					jAlert("정렬순서를 입력해주세요.");
					return false;
				}

				return true;
			},

			// 부서명 변경 시 부서경로 변경 이벤트 핸들링
			groupNameChanged : function() {
				registGroupWindow.groupInfo.groupNameKo = $("#p_group_name_ko").val();
				$("#p_group_full_path").val(registGroupWindow.groupInfo.fullPath + " > " + registGroupWindow.groupInfo.groupNameKo);
			},

		},

		//5. 화면 UI 변경 처리
		ui : {
		},

		//6. callback 처리
		callback : {
		},
}