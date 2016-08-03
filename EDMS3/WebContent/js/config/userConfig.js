/**
 * 사용자 환경설정 js
 */

$(function(){

	exsoft.util.filter.maxNumber();			// maxlength

	exsoft.util.common.ddslick('#language', 'language', '', 123, function(){});
	exsoft.util.common.ddslick('#pg_size', 'pg_size', '', 70, function(){});
	exsoft.util.common.ddslick('#doc_search', 'doc_search', '', 70, function(){});

	  //스킨설정 클릭
    $('.default_setting').find('a[class^="skin_set"]').bind("click", function(e){
    	e.preventDefault();
	    $('.default_setting').find('a[class^="skin_set"]').removeClass('pushed');
	    $(this).addClass('pushed');
	 });

});

var exsoftUserConfigFunc = {

		currentTab : "",

		tabClass :  ['myinfo','passwdConf','myconfig'],

		tabForms :  ['myinfoFrm','passwdConfFrm','myconfigFrm'],

		tabHeight : {
			'myinfo' : '572',
			'passwdConf' : '592',
			'myconfig' :  '552',
		},

		notyId : "#noty",

		userConfigUrl : "/user/userConfigProc.do",

		userConfigBinder : null,

		init : {

			notyEmpty : function(notyId)	{
				$(notyId).html('');
			},

			// 탭선택 초기화
			tabSelectInit : function() {
				for (var n in exsoftUserConfigFunc.tabClass) {
					$("#"+exsoftUserConfigFunc.tabClass[n]).removeClass("selected");
				}
			},

			tabFormInit : function() {
				for (var n in exsoftUserConfigFunc.tabForms) {
					$("#"+exsoftUserConfigFunc.tabForms[n]).addClass("hide");
				}
			},

			// 새창 CALL시 최초 진입
			pageInit : function(tabType,width,height){

				exsoftUserConfigFunc.currentTab = tabType;
				exsoftUserConfigFunc.init.tabSelectInit();
				exsoftUserConfigFunc.ui.tabActiveStatus(tabType);		// 선택된 탭 및 컨텐츠 활성화
				exsoftUserConfigFunc.event.configDetail(tabType);				// 서버CALL

				window.resizeTo(width,height);		// 페이지 resize
			},

		},

		open : {

		},

		layer : {

		},

		close : {

		},

		event : {

			// 탠선택시
			tabSelectFunc : function(tabType) {

				if(exsoftUserConfigFunc.currentTab != tabType) {

					exsoftUserConfigFunc.currentTab = tabType;
					exsoftUserConfigFunc.init.tabSelectInit();
					exsoftUserConfigFunc.init.tabFormInit();
					exsoftUserConfigFunc.ui.tabActiveStatus(tabType);		// 선택된 탭 및 컨텐츠 활성화
					exsoftUserConfigFunc.event.configDetail(tabType);				// 서버CALL

					window.resizeTo(750,exsoftUserConfigFunc.tabHeight[tabType]);		// 페이지 resize
					window.focus();
				}

				// 현재 활성화된 TAB을 누를 경우 PASS

			},

			// 환경설정값 조회
			configDetail : function(tabType) {

					//var userConfigBinder = new DataBinder("#"+tabType+"Frm");
					exsoftUserConfigFunc.userConfigBinder = new DataBinder("#"+tabType+"Frm");

					exsoft.util.ajax.ajaxPopDataFunctionWithCallback({type:'view',updateType:tabType}, exsoft.contextRoot+exsoftUserConfigFunc.userConfigUrl,function(data, e) {
						if(data.result == "false")	{
							jAlert('데이터를 로드하는데 실패했습니다.','확인',0);
							exsoft.util.layout.windowClose();
						}else {

							if(tabType == "myconfig") {

								// 스킨은 추후 설정한다. :: addClass("pushed");
								exsoft.util.layout.setSelectBox("language",data.userVO.language);				// 언어
								exsoft.util.layout.setSelectBox("pg_size",data.userVO.page_size);				// 페이지당 목록수
								exsoft.util.layout.setSelectBox("doc_search",data.userVO.doc_search);		// 나의문서 표시기간
								exsoft.util.layout.setRadioVal("view_type",data.userVO.view_type);				// 미리보기 설정

							}else {
								exsoftUserConfigFunc.userConfigBinder.binding(data.userVO);
							}
						}
					});

				},

			updateConfigProc : function() {

				//	사용자정보 & 패스워드 관리 Validator

				var jsonObject = null;
				var scopeFrm = "";

				if(exsoftUserConfigFunc.currentTab != 'myconfig')	{

					scopeFrm = exsoftUserConfigFunc.currentTab + "Frm";

					var isValid = $("#"+scopeFrm).validation({
						options : {
							debug : false,alert : false,effect : false,
							guideMessage : true,
							notyId : 'noty',
						}
					});

					if(!isValid) {
						return false;
					}

					if(exsoftUserConfigFunc.currentTab == "passwdConf") {
						exsoftUserConfigFunc.userConfigBinder.set("user_pass",exsoftUserConfigFunc.userConfigBinder.get("passwd1"));
					}

				}else {

					exsoftUserConfigFunc.userConfigBinder.set("language",exsoft.util.layout.getSelectBox('language','option'));				// 언어
					exsoftUserConfigFunc.userConfigBinder.set("theme","themeGray");														// 스킨
					exsoftUserConfigFunc.userConfigBinder.set("page_size",exsoft.util.layout.getSelectBox('pg_size','option'));					// 페이지당 문서목록수
					exsoftUserConfigFunc.userConfigBinder.set("doc_search",exsoft.util.layout.getSelectBox('doc_search','option'));			// 나의문서 표시기간
					exsoftUserConfigFunc.userConfigBinder.set("view_type",exsoft.util.layout.getRadioVal('view_type'));							// 본문보기
					// 첨부아이콘 미리보기
				}

				// TAB별 구분값 정의
				exsoftUserConfigFunc.userConfigBinder.set("type","update");
				exsoftUserConfigFunc.userConfigBinder.set("updateType",exsoftUserConfigFunc.currentTab);

				exsoft.util.ajax.ajaxPopDataFunctionWithCallback(exsoftUserConfigFunc.userConfigBinder.getDataToJson(), exsoft.contextRoot + exsoftUserConfigFunc.userConfigUrl,
					function(data, e){

						if(data.result == "true"){
							$(exsoftUserConfigFunc.notyId).html(data.message);
							setTimeout("exsoftUserConfigFunc.init.notyEmpty('"+exsoftUserConfigFunc.notyId+"')",2000);
						}else {
							$(exsoftUserConfigFunc.notyId).html(data.message);
						}
					}
				);

		},


	},

	ui : {
		// 탭활성화처리
		tabActiveStatus : function(tabType){
			$("#"+tabType).addClass("selected");
			$("#"+tabType+"Frm").removeClass("hide");
		},
	},

	callback : {

	},


}