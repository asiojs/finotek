/**
 * 환경설정 관련 스크립트
 */
var exsoftAdminConfFunc = {

		defaultFileSize : "",
		productInfo : "",
		tabMenu : "",
		currentTab : "",

		tabClass :  ['fileConfig','versionConfig','auditConfig','trashConfig','urlConfig'],
		tabForms :  ['fileConfigFrm','versionConfigFrm','auditConfigFrm','trashConfigFrm','urlConfigFrm'],
		tabCodes : {
			'fileConfig' : 'FILE',
			'versionConfig' : 'VERSION',
			'auditConfig' : 'AUDIT',
			'trashConfig' : 'TRASH',
			'urlConfig' : 'URL',
		},

		configUrl : "/admin/confControl.do",
		configBinder : null,

		init : {

			// 환경설정 초기 페이지
			initPage : function(defaultFileSize,productInfo,tabType) {

				exsoftAdminConfFunc.defaultFileSize = defaultFileSize;
				exsoftAdminConfFunc.productInfo = productInfo;
				exsoftAdminConfFunc.currentTab = tabType;
				exsoftAdminConfFunc.ui.tabActiveStatus(exsoftAdminConfFunc.currentTab);
				exsoftAdminConfFunc.event.configDetail();		// 서버CALL
			},

			// 탭선택 초기화
			tabSelectInit : function() {
				for (var n in exsoftAdminConfFunc.tabClass) {
					$("#"+exsoftAdminConfFunc.tabClass[n]).removeClass("selected");
				}
			},

			tabFormInit : function() {
				for (var n in exsoftAdminConfFunc.tabForms) {
					$("#"+exsoftAdminConfFunc.tabForms[n]).addClass("hide");
				}
			},

			// 휴지통관리 체크박스 초기화
			trashCheckBoxInit : function(isVal,checkId,targetId) {
				if(isVal == "Y")	{
					$(checkId).prop("checked",true);
					$(targetId).prop("readonly",false);
					$(targetId).removeClass("readonly");
				}else {
					$(checkId).prop("checked",false);
					$(targetId).prop("readonly",true);
					$(targetId).addClass("readonly");
				}

			},

		},

		open : {

		},

		layer : {

		},

		close : {

		},

		event : {

			// 환경설정 서버 CALL
			configDetail : function() {

				exsoftAdminConfFunc.configBinder = new DataBinder("#"+exsoftAdminConfFunc.currentTab+"Frm");

				console.log("exsoftAdminConfFunc.currentTab="+exsoftAdminConfFunc.currentTab);

				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({type:'select',stype:exsoftAdminConfFunc.tabCodes[exsoftAdminConfFunc.currentTab]},
						exsoft.contextRoot+exsoftAdminConfFunc.configUrl,exsoftAdminConfFunc.currentTab,function(data,param) {

					// Current TAB 분기
					if(data.result == "false"){
						jAlert('데이터를 로드하는데 실패했습니다.','확인',0);
						return false;
					}else {

						if(param == "auditConfig")	{

							exsoftAdminConfFunc.configBinder.binding(data.audit);

						}else if(param == "trashConfig")	{

							exsoftAdminConfFunc.init.trashCheckBoxInit(data.ptrash.is_use,'#p_check','#pdecade');
							$("#pdecade").val(data.ptrash.decade);
							exsoftAdminConfFunc.init.trashCheckBoxInit(data.strash.is_use,'#s_check','#sdecade');
							$("#sdecade").val(data.strash.decade);

						}else if(param == "fileConfig")	{

							$("#ext").val(data.EXT.sval);
							exsoftAdminConfFunc.ui.setFileConfig(data.FILECNT.is_use,'fileCnt',data.FILECNT.sval);
							exsoftAdminConfFunc.ui.setFileConfig(data.FILESIZE.is_use,'fileSize',data.FILESIZE.sval);
							exsoftAdminConfFunc.ui.setFileConfig(data.FILETOTAL.is_use,'fileTotal',data.FILETOTAL.sval);

						}else if(param == "versionConfig")	{
							console.log("data.mypage.is_use="+data.mypage.is_use);
							console.log("data.mypage.val="+data.mypage.val);
							exsoftAdminConfFunc.ui.setVersionConfig(data.mypage.is_use,data.mypage.val,'m');
							console.log("data.workspace.is_use="+data.workspace.is_use);
							console.log("data.workspace.val="+data.workspace.val);
							exsoftAdminConfFunc.ui.setVersionConfig(data.workspace.is_use,data.workspace.val,'w');

						}else if(param == "urlConfig")	{
							$("#expired").val(data.expired);
						}
					}


				});
			},

			// 환경설정 변경처리 : Appliance 고려안함
			configUpdate : function() {

				var objForm = document.frm;

				// 파일설정
				if(exsoftAdminConfFunc.currentTab == "fileConfig") {

					var extList = $("#ext").val();
					if ((extList.length-1) != extList.lastIndexOf(";")) {
						$("#ext").val(extList + ";");
					}

					 // 첨부파일 갯수 제한
					if($("input:checkbox[id='cfileCnt']").is(":checked") ) {
						 objForm.is_fileCnt.value = "Y";
						 if (objForm.fileCnt.value.length == 0) {
							 jAlert('첨부파일 개수 제한를 입력하세요.','확인',0);
							 return false;
						 }
					}else {
						objForm.is_fileCnt.value = "N";
					}

					 // 첨부파일 사이즈 제한
					if($("input:checkbox[id='cfileSize']").is(":checked") ) {
						 objForm.is_fileSize.value = "Y";
						 if (objForm.fileSize.value.length == 0) {
							 jAlert('첨부파일 사이즈 제한 크기를 입력하세요.');
							 return false;
						 }

						 if (objForm.fileSize.value > exsoftAdminConfFunc.defaultFileSize) {
							 jAlert('첨부파일 사이즈는' + exsoftAdminConfFunc.defaultFileSize + 'MB를 초과할 수 없습니다.','확인',0);
							 return false;
						 }

					 }else {
						 objForm.is_fileSize.value = "N";
					 }

					 // 문서당 첨부파일 총 사이즈 제한
					 if($("input:checkbox[id='cfileTotal']").is(":checked") ) {
						 objForm.is_fileTotal.value = "Y";
						 if (objForm.fileTotal.value.length == 0) {
							 jAlert('첨부파일 총 사이즈 제한 크기를 입력하세요.','확인',0);
							 return false;
						 }
					 }else {
						 objForm.is_fileTotal.value = "N";
					 }

				}else if(exsoftAdminConfFunc.currentTab == "urlConfig") {
					if (objForm.expired.value.length == 0) {
						$("#expired").val(0);
					}
				}else if(exsoftAdminConfFunc.currentTab == "versionConfig") {
					 // 버전관리 사용여부
					 if($("input:checkbox[id='mVersionChk']").is(":checked") ) {
						 objForm.is_mVersion.value = "Y";
					 }else {
						 objForm.is_mVersion.value = "N";
					 }

					if($("input:checkbox[id='wVersionChk']").is(":checked") ) {
						objForm.is_wVersion.value = "Y";
					 }else {
						 objForm.is_wVersion.value = "N";
					 }

				}else if(exsoftAdminConfFunc.currentTab == "trashConfig") {

					// 개인휴지통 자동 비우기 체크
					if($("input:checkbox[id='p_check']").is(":checked") ) {
						 if (objForm.pdecade.value.length == 0) {
							 jAlert('개인 휴지통 지난 문서 자동 비우기 날짜를 입력하세요.','확인',0);
							 return false;
						 }
						 objForm.pis_use.value = "Y";
					}else {
						 objForm.pis_use.value = "N";
					}

					// 시스템 휴지통 자동 비우기 체크
					if($("input:checkbox[id='s_check']").is(":checked") ) {
						 if (objForm.sdecade.value.length == 0) {
							 jAlert('시스템 휴지통 지난 문서 자동 비우기 날짜를 입력하세요.','확인',0);
							 return false;
						 }
						 objForm.sis_use.value = "Y";
					}else {
						 objForm.sis_use.value = "N";
					}


				}else if(exsoftAdminConfFunc.currentTab == "auditConfig") {

					 if (objForm.read_count_threshold.value.length == 0) {
						 jAlert('열람 기준건수를 입력하세요.','확인',0);
						 return false;
					 }
					 // 메일 자동 전송 여부
					 if($("input:checkbox[id='is_mail']").is(":checked") ) {
						 objForm.send_report_mail.value = "T";
					 }else {
						 objForm.send_report_mail.value = "F";
					 }

					 // 이메일 유효성 체크
					 var emailList = objForm.report_mail_receiver_address.value;
					 if(emailList.length != 0) {
						 var result =  emailList.split(';');
						 for(var i=0;i<result.length;i++)	{
							 if(result[i].length !=0 && !exsoft.util.check.emailCheck(result[i]))	{
								 jAlert('이메일주소가 정확하지 않습니다. 다시 입력해주세요.','확인',0);
								 return false;
							 }
						 }
					 }
				}

				objForm.type.value = "update";
				objForm.stype.value = exsoftAdminConfFunc.tabCodes[exsoftAdminConfFunc.currentTab];

				// 서버 CALL
				exsoft.util.ajax.ajaxFunctionNoLodingWithCallback('frm',exsoft.contextRoot+exsoftAdminConfFunc.configUrl,
						exsoftAdminConfFunc.currentTab,function(data,param) {

					// 데이터 후 처리
					if(data.result == "true")	{
						jAlert('환결설정이 정상적으로 변경되었습니다.','확인',0);
					}else {
						jAlert(data.message,'확인',0);
					}
				});


			}

		},

		ui : {

			// 첨부파일 설정처리
			setFileConfig : function(is_use,fkey,fval) {

				if(is_use == 'Y') {
					$("#c"+fkey).prop("checked",true)
					$("#"+fkey).prop("readonly",false);
					$("#"+fkey).removeClass("readonly");
				}else {
					$("#c"+fkey).prop("checked",false)
					$("#"+fkey).prop("readonly",true);
					$("#"+fkey).addClass("readonly");
				}
				$("#"+fkey).val(fval);

			},

			// 버전설정 설정처리(isType:m-개인,w-업무)
			setVersionConfig : function(is_use,fval,isType) {

				$("input:radio[name='"+isType+"Version']:radio[value='"+fval+"']").prop("checked",true);
				if(is_use== "Y") {
					$("#"+isType+"VersionChk").prop("checked",true);
				}else {
					$("#"+isType+"VersionChk").prop("checked",false);
					$("input:radio[id='"+isType+"Version']").each(function(i) {
						$(this).attr("disabled", true);
					})
				}
			},


			// TAB 선택시
			tabSelectFunc : function(tabType) {

				if(exsoftAdminConfFunc.currentTab != tabType) {

					exsoftAdminConfFunc.currentTab = tabType;
					exsoftAdminConfFunc.init.tabSelectInit();
					exsoftAdminConfFunc.init.tabFormInit();
					exsoftAdminConfFunc.ui.tabActiveStatus(tabType);			// 선택된 탭 및 컨텐츠 활성화
					exsoftAdminConfFunc.event.configDetail(tabType);			// 서버CALL
				}

				// 현재 활성화된 TAB을 누를 경우 PASS

			},

			// 탭활성화처리
			tabActiveStatus : function(tabType){
				$("#"+tabType).addClass("selected");
				$("#"+tabType+"Frm").removeClass("hide");
			},


			isUseChange : function(checkId,targetId) {
				if($("input:checkbox[id='"+checkId+"']").is(":checked") ) {
					$(targetId).prop("readonly",false);
					$(targetId).removeClass("readonly");
				}else {
					$(targetId).prop("readonly",true);
					$(targetId).addClass("readonly");
				}
			},

			onlyNumber : function(event) {

				var Rtext = /[0-9]/g;
				var key = (window.netscape) ? event.which : event.keyCode
				var t = Rtext.test(String.fromCharCode(key));
				if(t) {
					return true;
			   } else {
				   return false;
			   }
			},

			cancelFunc : function() {
				exsoftAdminConfFunc.event.configDetail(exsoftAdminConfFunc.currentTab);
			}
		},

		callback : {

		}
}
