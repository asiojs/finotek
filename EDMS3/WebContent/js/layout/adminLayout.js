/**
 * Admin Layout 관련 스크립트
 */
var exsoftAdminLayoutFunc = {

		adminTopMenu : ['systemMenu','documentMenu','rGateMenu'],
		systemMenu :  ['groupManager','userManager','menuAuthManager','confManager'],
		documentMenu :  ['typeManager','folderManager','aclManager','expiredManager','ownerManager','wasteManager','duplicateManager','auditManager'],
		rgateMenu :  ['extManager','procManager','usbManager','networkManager','exceptionManager','uninstallManager'],
		userConfigBinder : null,
		userConfigUrl : "/user/userConfigProc.do",

		init : {

			menuInit : function() {
				for (var n in exsoftAdminLayoutFunc.adminTopMenu) {
					$("#"+exsoftAdminLayoutFunc.adminTopMenu[n]).removeClass("selected");
				}

				for (var n in exsoftAdminLayoutFunc.systemMenu) {
					$("#"+exsoftAdminLayoutFunc.systemMenu[n]).removeClass("selected");
				}

				for (var n in exsoftAdminLayoutFunc.documentMenu) {
					$("#"+exsoftAdminLayoutFunc.documentMenu[n]).removeClass("selected");
				}

				for (var n in exsoftAdminLayoutFunc.rgateMenu) {
					$("#"+exsoftAdminLayoutFunc.rgateMenu[n]).removeClass("selected");
				}
			},

			// 관리자 상단메뉴 선택 적용 처리
			menuSelected : function(topSelect,subSelect) {
				$("#"+topSelect).addClass("selected");
				$("#"+subSelect).addClass("selected");
			},

		},

		open : {

			passwdConfig : function(wrapperClass,layerClass) {

				exsoft.util.layout.divLayerOpen(wrapperClass,layerClass);
				exsoft.util.common.formClear("adminPasswd");
				exsoftAdminLayoutFunc.event.configDetail();
			}

		},

		layer : {

		},

		close : {

		},

		event : {

			// 패스워드 INFO
			configDetail : function() {

				exsoftAdminLayoutFunc.userConfigBinder = new DataBinder("#adminPasswd");

				exsoft.util.ajax.ajaxPopDataFunctionWithCallback({type:'view',updateType:"passwdConf"}, exsoft.contextRoot+exsoftAdminLayoutFunc.userConfigUrl,function(data, e) {
					if(data.result == "false")	{
						jAlert('데이터를 로드하는데 실패했습니다.','확인',0);
						exsoft.util.layout.windowClose();
					}else {
						exsoftAdminLayoutFunc.userConfigBinder.binding(data.userVO);
					}
				});

			},

			// 패스워드 UPDATE
			updateConfigProc : function() {

				var jsonObject = null;
				var scopeFrm = "";

				var isValid = $("#adminPasswd").validation({
					options : {
						debug : false,alert : true,effect : false,
						guideMessage : true,
						//notyId : 'noty',
					}
				});

				if(!isValid) {	return false;	}

				exsoftAdminLayoutFunc.userConfigBinder.set("user_pass",exsoftAdminLayoutFunc.userConfigBinder.get("passwd1"));
				exsoftAdminLayoutFunc.userConfigBinder.set("type","update");
				exsoftAdminLayoutFunc.userConfigBinder.set("updateType","passwdConf");

				exsoft.util.ajax.ajaxPopDataFunctionWithCallback(exsoftAdminLayoutFunc.userConfigBinder.getDataToJson(), exsoft.contextRoot + exsoftAdminLayoutFunc.userConfigUrl,
						function(data, e){

							if(data.result == "true"){
								jAlert(data.message,"확인",0);
								exsoft.util.layout.divLayerClose('passwd_set_wrapper','passwd_set');
							}else {
								jAlert(data.message,"확인",0);
							}
						}
					);
			}


		},

}

