/**
 * EDMS HTML5 Global JS Function
 * util관련 js
 */

/**
 * Javascript 기본 String 자료형의 메서드를 확장합니다.
 *
 * 예 ) 'The {0} is dead. Don\'t code {0}. Code {1} that is open source!'.format('ASP', 'PHP');
 * 반환 : 'The ASP is dead. Don\'t code ASP. Code PHP that is open source!'
 * @returns {String}
 */
String.prototype.format = function() {
    var formatted = this;
    var lengthCnt = arguments.length; // 속도를 위해  for문에 선언 안함
    for (var i = 0; i < lengthCnt; i++) {
        var regexp = new RegExp('\\{'+i+'\\}', 'gi');
        formatted = formatted.replace(regexp, arguments[i]);
    }
    return formatted;
};


exsoft.namespace('util.common');
exsoft.namespace('util.layout');
exsoft.namespace('util.ajax');
exsoft.namespace('util.check');
exsoft.namespace('util.date');
exsoft.namespace('util.filter');
exsoft.namespace('util.error');
exsoft.namespace('util.grid');
exsoft.namespace('util.table');
exsoft.namespace('util.websocket');


/***********************************************
 * common
 **********************************************/
/**
 * 공통으로 사용되는 util
 *
 * @namespace : exsoft.util.common
 *
 */
exsoft.util.common = {

		/**
		 * checkbox 전부 체크
		 * @param name
		 */
		allCheck : function(name) {
			var check = $('#checkAll').is(':checked');

			$('input[name='+ name +']').each(function() {
				if($(this).attr("disabled") != "disabled") {
					this.checked = check;
				}
			});
		},

		/**
		 * checkbox 전부 체크
		 * @param checkId
		 * @param name
		 */
		allCheckBox : function(checkId,name) {
			var check = $('#'+checkId).is(':checked');

			$('input[name='+ name +']').each(function() {
				if($(this).attr("disabled") != "disabled") {
					this.checked = check;
				}
			});
		},

		/**
		 * 전체선택 체크박스  초기화
		 * @param id
		 */
		checkboxInit : function(id) {
			$("#"+id).prop("checked",false);
		},

		/**
		 * 체크박스 선택된 개수 가져오기
		 */
		checkBoxCheckedLength : function(chkName) {
			return $("input:checkbox[name="+chkName+"]:checked").length;
		},

		//////////// 파일다운로드 관련 ////////////
		/**
		 * 파일 다운로드
		 * @param page_id
		 */
		fileDown : function(page_id) {

			var JsonArr = [];
			var JsonArrIndex = 0;
			var jsonData = {page_id:""};

			jsonData['page_id'] = page_id;
			if(jsonData.page_id){
				JsonArr[JsonArrIndex] = jsonData;
				JsonArrIndex++;
			}

			$(location).attr("href", exsoft.contextRoot+"/common/downLoad.do?pageList="+JSON.stringify(JsonArr));
		},

		/**
		 * 모두 저장 ZIP
		 * @param formId - 폼명
		 * @param checkBox - 체크박스명
		 */
		zipFileDown : function(formId,checkBox) {

			var JsonArr = [];
			var JsonArrIndex = 0;

			$('#'+formId+' input[name='+ checkBox +']').each(function() {
				var jsonData = {page_id:""};
				jsonData['page_id'] =  this.value;

				if(jsonData.page_id){
					JsonArr[JsonArrIndex] = jsonData;
					JsonArrIndex++;
				}

			});

			if(JsonArrIndex == 0) {
				jAlert('No Files.');
				return false;
			}else {
				$(location).attr("href", exsoft.contextRoot+"/common/downLoad.do?pageList="+JSON.stringify(JsonArr)+"&isZip=T");
			}

		},

		/**
		 * 파일사이즈 구하기
		 * @param bytes Number of bytes to convert
		 * @param precsion Number of digits after the decimal separator
		 * @returns {String}
		 */
		bytesToSize : function(bytes,precision) {
			var kilobyte = 1024;
			var megabyte = kilobyte * 1024;
			var gigabyte = megabyte * 1024;
			var terabyte = gigabyte * 1024;

			if ((bytes >= 0) && (bytes < kilobyte)) {
				return bytes + ' B';
			} else if ((bytes >= kilobyte) && (bytes < megabyte)) {
				return (bytes / kilobyte).toFixed(precision) + ' KB';
			} else if ((bytes >= megabyte) && (bytes < gigabyte)) {
				return (bytes / megabyte).toFixed(precision) + ' MB';
			} else if ((bytes >= gigabyte) && (bytes < terabyte)) {
				return (bytes / gigabyte).toFixed(precision) + ' GB';
			} else if (bytes >= terabyte) {
				return (bytes / terabyte).toFixed(precision) + ' TB';
			}  else {
				return bytes + ' B';
			}
		},

		/**
		 * JQgrid 에서 File용량 계산하기(함수명 변경처리)
		 * @param bytes
		 * @returns {String}
		 */
		gridBytesToSize : function(bytes) {
		        var sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
		        if (bytes == 0) return '0B';
		        else if(bytes == -1) return '-1';

		        var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
		        var size = (i<2) ? Math.round((bytes / Math.pow(1024, i))) : Math.round((bytes / Math.pow(1024, i)) * 100)/100;
		        return  size + ' ' + sizes[i];
		},

		////////// 쿠키관련 //////////
		/**
		 * 쿠키 생성
		 * @param key
		 * @param value
		 * @param expire
		 */
		setCookie : function(key, value, expire) {
			var cookieDate = new Date();

			if(expire < 1) {
				expire = expire * 10;

				cookieDate.setHours(cookieDate.getHours() + expire);
			} else {
				cookieDate.setDate(cookieDate.getDate() + expire);
			}

			document.cookie = key + "=" + escape(value) + "; expires=" + cookieDate.toGMTString() + "; path=/";
		},

		/**
		 * 쿠키 정보 가져오기
		 * @param key
		 * @returns
		 */
		getCookie : function(key) {
			var cookie = document.cookie;
			var first = cookie.indexOf(key+"=");

			if(first >= 0) {
				var str = cookie.substring(first, cookie.length);
				var last = str.indexOf(";");

				if(last < 0) {
					last = str.length;
				}
				str = str.substring(0,last).split("=");

				return unescape(str[1]);
			} else {
				return null;
			}
		},

		/**
		 *cookie 지우기
		 * @param key
		 */
		delCookie : function(key) {
			today = new Date();
			today.setDate(today.getDate() - 1);
			document.cookie = key + "=; path=/; expires=" + today.toGMTString() + ";";
		},

		//////////XSS & Sql Injection 관련 //////////
		/**
		 * Sql Injection 키워드 제거 처리
		 * @param str
		 */
		sqlInjectionReplace : function(str) {

			str = str.replace("'" , "");
			str = str.replace( "--" , "");
			str = str.replace("--, #" , " " );
			str = str.replace("/* */" , " " );
			str = str.replace("' or 1=1--" , " " );
			str = str.replace("union" , " " );
			str = str.replace("select" , " " );
			str = str.replace("delete" , " " );
			str = str.replace("insert" , " " );
			str = str.replace("update" , " " );
			str = str.replace("drop" , " " );
			str = str.replace("on error resume" , " " );
			str = str.replace("execute" , " " );
			str = str.replace("windows" , " " );
			str = str.replace("boot" , " " );
			str = str.replace("-1 or" , " " );
			str = str.replace("-1' or" , " " );
			str = str.replace("../" , " " )
			str = str.replace("-1' or" , " " );
			str = str.replace("unexisting" , " " );
			str = str.replace("win.ini" , " " );

			return str;
		},

		/**
		 * HTML Tag 제거처리
		 * @param str
		 * @returns
		 */
		stripHTMLtag : function(str) {
			var objStrip = new RegExp();
			objStrip = /[<][^>]*[>]/gi;
			return str.replace(objStrip, "");
		},

		/**
		 * String에서 html태그를 제거하여 반환한다.
		 * @param v
		 * @returns
		 */
		stripHtml : function(v) {
			v = String(v);
			var regexp = /<("[^"]*"|'[^']*'|[^'">])*>/gi;
			if (v) {
				v = v.replace(regexp,"");
				return (v && v !== '&nbsp;' && v !== '&#160;') ? v.replace(/\"/g,"'") : "";
			}
				return v;
		},

		/**
		 * Div 객체나 Tr 객체 삭제처리
		 * @param divIds
		 */
		removeDivIds : function(divIds)	{
			$("#"+divIds).remove();
		},

		////////// Etc //////////
		/**
		 * 에디터 내용 가져오기
		 */
		editorContent  : function() {

			return iframe_editor.Editor.getContent();
		},

		/**
		 * 유니크값 생성 - UUID
		 */
		uniqueId : function() {

			var d = new Date().getTime();
			var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
				var r = (d + Math.random()*16)%16 | 0;
			    d = Math.floor(d/16);
			    return (c=='x' ? r : (r&0x7|0x8)).toString(16);
			});

			return uuid;
		},

		/**
		 * 문서 등록/수정을 위한 파일첨부 관련 옵션 정의
		 * @param type : 환경설정 옵션
		 * @param is_use : 사용유무
		 * @param fval : 키값
		 * @param defaultValue : 기본값으로 사용할 값
		 */
		fileConfig : function(type, is_use, fval, defaultValue) {

			var ret = null;

			switch(type)	{

				case "EXT":
					if(is_use == "Y")	{ret = fval.replace(";",",");}
					else {	ret = defaultValue;	}
					break;
				case "FILECNT":
					if(is_use == "Y")	{ret = fval;}
					else {	ret = defaultValue;	}
					break;
				case "FILESIZE":
					if(is_use == "Y")	{ret = fval*1024*1024;}
					else {	ret = defaultValue*1024*1024;	}
					break;
				case "FILETOTAL":
					if(is_use == "Y")	{ret = fval*1024*1024;}
					else {	ret =  defaultValue*1024*1024;	}
					break;
			}

			return ret;
		},

		/**
		 * 클립보드 복사처리 divId 내용
		 * @param divId
		 */
		copyToClipboard : function(divId) {

			 var text = $('#'+divId).html();
			 if(window.clipboardData){
				 // IE
				 window.clipboardData.setData('text', text);
			 }else {
				 // IE 외 Browser
				 window.prompt ("Copy to clipboard: Ctrl+C, Enter", text);
			 }
		},

		/**
		 * 해당 입력칸의 숫자N이면 다음값으로 포커스 이동처리
		 * @param N
		 * @param Obj
		 * @param nextID
		 * @usage onKeyUp="exsoft.util.common.nextBlank(3,this.id,'ip2')"
		 */
		nextBlank : function(N,Obj,nextID) {

			if(document.getElementById(Obj).value.length == N) {
				document.getElementById(nextID).focus();
			}
		},

		/**
		 * Textarea max length 구하기
		 * @param obj
		 * @usage <textarea name="reason" id="reason" rows="3" cols="30" class="w2" maxlength="50" onKeyUp="return exsoft.util.common.isMaxlength(this);"></textarea>
		 */
		isMaxlength : function(obj) {
			var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
			if (obj.getAttribute && obj.value.length > mlength) {
				obj.value = obj.value.substring(0,mlength)
			}
		},

		/**
		 * element의 ID 포맷이 맞는지 확인
		 */
		isIdFormat : function(eId) {
			if (eId.indexOf("#") === 0) {
				return true;
			} else {
				return false;
			}
		},

		/**
		 * element의 ID 포맷이 맞는지 확인하고, 맞지 않으면 ID포맷으로 만들어서 리턴
		 */
		getIdFormat : function(eId) {
			if (!exsoft.util.common.isIdFormat(eId)) {
				eId = "#" + eId;
			}
			return eId;
		},

		/**
		 * Form 초기화
		 * @param formId
		 */
		formClear : function(formId)	{
			$(exsoft.util.common.getIdFormat(formId))[0].reset();
		},

		/**
		 * selectbox 디자인 변경
		 */
		ddslick : function(divId, className, dataBindName, width, callback, arrParam){
			divId = exsoft.util.common.getIdFormat(divId);
			$(divId).ddslick({
				width:width,
				background:"rgba(255, 255, 255, 0)",
				onSelected: function(selectedData){
					$(divId).addClass(className); //css변경

					//databinder 사용 여부  dd-selected-value input name
					if(dataBindName){ // if(dataBindName !== '') 동일한 조건문, boolean 아님 혼돈하지 말 것
						$(divId + ' input[name=dd-selected-value]').attr('data-bind',dataBindName);
						$(divId + ' input[name=dd-selected-value]').attr('data-select','true');
						//$(divId + ' input[name=dd-selected-value]').trigger('change'); // hidden type은 강제로 change event 부여

					}

					callback(divId, selectedData, arrParam);
				}
			});
		},

		/**
		 * Chart Type 초기화
		 */
		chartTypeInit : function() {
			$("#chartType").empty();
			$("#chartType").append("<option value='bar'>막대차트</option>");
			$("#chartType").append("<option value='line'>라인차트</option>");
			$("#chartType").append("<option value='pie'>파이차트</option>");
		},

		/**
		 * Chart 구분 컬럼
		 * @param type - 구분자
		 */
		colTypeInit : function(type) {

			$("#colType").empty();

			if(type == 1){
				$("#colType").append("<option value='create_cnt'>등록건수</option>");
				$("#colType").append("<option value='read_cnt'>조회건수</option>");
				$("#colType").append("<option value='update_cnt'>수정건수</option>");
				$("#colType").append("<option value='delete_cnt'>삭제건수</option>")
			}else {
				$("#colType").append("<option value='doc_cnt'>문서수</option>");
				$("#colType").append("<option value='page_cnt'>파일수</option>");
				$("#colType").append("<option value='page_total'>용량</option>");
			}
		},

		/**
		 * ACL의 가중치를 구한다 (수행 권한이 충분한지 비교하기 위함)
		 */
		getAclLevel : function(en_title) {
			var Levels = {
					NONE : 0,
					BROWSE : 1,
					READ : 2,
					UPDATE : 3,
					DELETE : 4
			}

			return Levels[en_title];

		},

		/**
		 * AclItem 기본권한 한글 -> 영문
		 */
		getAclItemTitleEn :function(ko_title) {
			switch(ko_title) {
				case "삭제" : return "DELETE"; break;
				case "수정" : return "UPDATE"; break;
				case "조회" : return "READ"; break;
				case "목록" : return "BROWSE"; break;
				case "없음" : return "NONE"; break;
			}
		},

		/**
		 *
		 * @param id : jquery id
		 * @param obj : jqgrid row Object
		 * @param isLeft : 마우스 포인트 기준 show 위치 왼쪽, 오른쪽 선택
		 */
		tooltip : function(id, tooltip, isLeft, event){
			$('.tooltip').empty().append(tooltip);
			$('.tooltip').removeClass('hide');

			$('.tooltip').css('top',event.clientY+'px');

			if(isLeft == 'true'){
				$('.tooltip').css('left',(event.clientX-$('.tooltip').width()-25)+'px');
			}else{
				$('.tooltip').css('left',(event.clientX+15)+'px');
			}
		},

		/**
		 * 탭 브라우저 전환시 특정 div show&hide
		 * @param tabIdList, divId
		 */
		tabControldivId : function(tabIdList, divId) {
			$(tabIdList).each(function(index) {
				tabIdList[index] = exsoft.util.common.getIdFormat(this);
			});

			$(function(){
				$(tabIdList.join(", ")).click(function() {
					var selectTab = $(this); //현재 탭

					$(tabIdList).each(function(index) {

						if ('#'+selectTab.attr("id") == tabIdList[index]) {
							$(tabIdList[index] + divId).removeClass("hide");
						} else {
							$(tabIdList[index] + divId).addClass("hide");
						}
					});

				});
			})
		},

		// List에서 해당 키값에 해당하는 값을 찾는다.
		findCodeName : function(list,compareValue) {
			var ret = null;
			$(list).each(function(i) {
				if (list[i].code_id == compareValue) {
					ret = list[i].code_nm;
					return false;
				}
			})
			return ret;
		},

		/**
		 * 관리자 문서권한 속성부분 Show/Hide
		 * @param imgId - 이미지아이콘 id
		 * @param viewId - view영역 id
		 */
		showHide : function(imgId,viewId) {

			if($(imgId).hasClass("down")) {
				$(imgId).removeClass("down");
				$(imgId).addClass("up");
				$(viewId).removeClass("hide")
			}else {
				$(imgId).removeClass("up");
				$(imgId).addClass("down");
				$(viewId).addClass("hide")
			}
		},

		/**
		 * ddslick 값을 가져온다
		 * @param targetId - ddslick ID
		 */
		getDdslick : function(targetId) {
			return $(targetId + ' input[name=dd-selected-value]').val()
		},

		/**
		 * form객체의 엘리먼트 값을 json형식으로 반환
		 * @param form_id
		 * @returns json string
		 */
		getFormToJsonObject : function(form_id)
		{
		    var obj = {};
		    var arr = $(exsoft.util.common.getIdFormat(form_id)).serializeArray();
		    $.each(arr, function() {
		        if (obj[this.name] !== undefined) {
		            if (!obj[this.name].push) {
		            	obj[this.name] = [obj[this.name]];
		            }
		            obj[this.name].push(this.value || '');
		        } else {
		        	obj[this.name] = this.value || '';
		        }
		    });
		    return obj;
		},

		/**
		 * XFTree 객체에서 선택된 Node 정보를 구성하여 반환한다
		 *
		 * @param treeObject - XFTrees 객체
		 * @param treeDivId - Tree Div ID
		 */
		getReturnTreeObject : function(treeObject, treeDivId, mapId) {
			var objectArray = new Array();
			var returnObj;

			$(treeObject.getCurrentNodeIds()).each(function(index){

				returnObj = new Object();
				returnObj.id = this;	// 선택한 노드의 ID
				returnObj.parentIdList = $(treeDivId).jstree("get_path", this, "", true);	// 선택한 노드부터 최상위 노드까지의 폴더 ID 목록
				returnObj.fullPath = $(treeDivId).jstree("get_path", this);		// 선택한 노드부터 최상위 노드까지의 폴더 이름 목록
				returnObj.mapId = mapId;
				returnObj.text = $(treeDivId).jstree("get_text", this);	// 선택된 노드 이름
				returnObj.acl_id = $(treeDivId).jstree("get_node", this).original.acl_id; // acl_id(custom data는 original 객체 안에 있다.)
				returnObj.original = $(treeDivId).jstree("get_node", this).original;
				returnObj.is_type = $(treeDivId).jstree("get_node", this).original.is_type; 	// 문서유형ID
				returnObj.parentGroup = treeObject.getFolderGroupId(this);
				objectArray.push(returnObj);
			});

			return objectArray;
		},

		/**
		 * fadin fadout Noty
		 *
		 * @param arg[0] msg - noty message
		 * @param arg[1] divId - noty target div id
		 * @param arg[2] duration - noty fade in out duration
		 */
		noty : function() {

			if (arguments[1] === undefined && exsoft.notyDivId === undefined)
				return;

			var _opt = {
					msg : arguments[0],
					divId : arguments[1] === undefined ? exsoft.notyDivId : arguments[1],
					duration : arguments[2] === undefined ? 2000 : arguments[2],
			}

			$(_opt.divId).html(_opt.msg).fadeIn(_opt.duration).fadeOut(_opt.duration);
		},

		// textarea 최대 글자 수 체크
		limitStringCheck : function(obj, maxLength){
			var koreaTextLength = 0;
			var byteCount = 0;
			var currentText = $(obj).val();

			// 한글은 2byte 계산한다.
			for (var k=0;k < currentText.length; k++)
			{
				onechar = currentText.charAt(k);
				if (escape(onechar) =='%0D') {} else if (escape(onechar).length > 4) { byteCount += 2; } else { byteCount++; }

				if(byteCount <= maxLength){
					koreaTextLength ++;
				}
			}

			if(byteCount > maxLength){
				$(obj).val(currentText.substr(0, koreaTextLength));
				$(obj).next().children('span').text(maxLength);
			}else{
				$(obj).next().children('span').text(byteCount);
			}
		}
}; // exsoft.util.common end...

exsoft.util.common.prototype = {

}; // exsoft.util.common.prototype end...


/***********************************************
 * layout
 **********************************************/
/**
 * layout 관련 util
 *
 * @namespace : exsoft.util.layout
 *
 */
exsoft.util.layout = {
		////////// 레이아웃관련 //////////
		/**
		 * 로그아웃
		 */
		logout : function(urls) {
			location.href = urls
		},

		/**
		 * 레이어 empty
		 * @param divIds - 레이어ID
		 */
		layerEmpty : function(divIds)  {
			$(exsoft.util.common.getIdFormat(divIds)).empty();
		},

		browser : function() {
			var s = navigator.userAgent.toLowerCase();
			var match = /(webkit)[ \/](\w.]+)/.exec(s) ||
			              /(opera)(?:.*version)?[ \/](\w.]+)/.exec(s) ||
			              /(msie) ([\w.]+)/.exec(s) ||
			              /(mozilla)(?:.*? rv:([\w.]+))?/.exec(s) ||
			             [];
			return { name: match[1] || "", version: match[2] || "0" };
		},

		/**
		 * 새창닫기 : IE/FireFox/Chrome
		 */
		windowClose : function() {
			window.open('','_self').close();
		},

		/**
		 * 상단메뉴 표시처리
		 * @param menuType
		 */
		topMenuSelect : function(menuType) {
			var menuArray = ['myDocMenu','myWorkMenu','workSpaceMenu','workProcessMenu','statisticsMenu'];
			  for (var i = 0; i < menuArray.length ; i++) {
				  $("#"+menuArray[i]).removeClass('selected');
				  $("#"+menuArray[i]+"Selected").removeClass('selected');
				  if(menuType == menuArray[i]) {
					  $("#"+menuArray[i]).addClass('selected');
					  $("#"+menuArray[i]+"Selected").addClass('selected');
				  }
			  }
		},


		/**
		 * 관리자 메뉴 선택시 contents 영역 변경처리
		 * @param url
		 */
		goAdminContent : function(url){
			jQuery.ajax({
				url: exsoft.contextRoot+"/common/sessionCheck.do",
				global: false,
				type: "POST",
				data: {session_id:'check'},
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					$('#admContents').load(url);
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorChk(response);
				}
			});
		},

		/**
		 * 사용자 메뉴 선택시 contents 영역 변경처리
		 * @param url
		 */
		goUserContent : function(url){
			jQuery.ajax({
				url: exsoft.contextRoot+"/common/sessionCheck.do",
				global: false,
				type: "POST",
				data: {session_id:'check'},
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					$('#userContents').load(url);
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorChk(response);
				}
			});

		},

		/**
		 * 레이어팝업 수직가운데 정렬
		 * @param obj
		 */
		lyrPopupWindowResize : function(obj) {
			var wrap = $('.wrap');
		    obj.prev().css({
		    	height:$(document).height(),
		    	width:$(document).width()
		    });
		    obj.css({
		        top:(wrap.height()-obj.height())/2,
		        left:(wrap.width()-obj.width())/2
		    });
		},

		//레이어팝업 수직가운데 정렬
		lyrPopupWindowResizeArr : function(arr) {
			var wrap = $('.wrap');

			for(var i = 0; i < arr.length; i++) {
				arr[i].prev().css({
					height:$(document).height(),
					width:$(document).width()
				});
				arr[i].css({
					top:(wrap.height()-arr[i].height())/2,
					left:(wrap.width()-arr[i].width())/2
				});
			}
		},

		/**
		 * Layer Open
		 * @param wrapperClass
		 * @param layerClass
		 */
		divLayerOpen : function(wrapperClass,layerClass) {
			$("."+wrapperClass).removeClass('hide');
			$("."+layerClass).removeClass('hide');
			exsoft.util.layout.lyrPopupWindowResize($("."+layerClass));
		},

		/**
		 * Layer Close
		 * @param wrapperClass
		 * @param layerClass
		 */
		divLayerClose : function(wrapperClass,layerClass) {
			$("."+wrapperClass).addClass('hide');
			$("."+layerClass).addClass('hide');
		},

		popDivLayerClose : function(layerClass) {
			$("."+layerClass).addClass('hide');
		},

		/***
		 * DropMenu Proc
		 * @param dropMenuClass
		 */
		dropMenu : function(dropMenuClass) {
			if($("."+dropMenuClass).hasClass('hide')) {
				$("."+dropMenuClass).removeClass('hide');
			}else {
				$("."+dropMenuClass).addClass('hide');
			}
		},

		/**
		 * SelectBox 값 설정하기 :: ddslick Version
		 * @param selectId
		 * @param setVal
		 */
		setSelectBox : function(selectId,setVal)	{

			var currVal = "";
			var findDivs = "#"+selectId + " li";

			$(findDivs).each(function( index ) {
				currVal = $(this).find('.dd-option-value').val();

				if(currVal == setVal)	{
					$("#"+selectId).ddslick('select', {index: $(this).index()});
				}
			});

		},

		/**
		 * 선택된 SelectBox 값 가져오기 :: :: ddslick Version
		 * @param selectId
		 * @param type - option,text
		 */
		getSelectBox : function(selectId,type)	{

			var retVal = "";

			if(type == "option")	{
				retVal = $("#"+selectId+" .dd-selected-value").val();
			}else {
				retVal = $("#"+selectId+" .dd-selected-text").html();
			}

		    return retVal;

		},

		/**
		 * Radio 버튼 Check
		 * @param radioNm
		 * @param setVal
		 */
		setRadioVal : function(radioNm,setVal)	{
			$("input:radio[name='"+radioNm+"']:radio[value='"+setVal+"']").prop("checked", true);
		},

		/**
		 * 선택된 Radio 값 가져오기
		 * @param radioNm
		 */
		getRadioVal : function(radioNm)	{
			return $(":input:radio[name="+radioNm+"]:checked").val();
		},

		// Layer 닫기 관련 이벤트 함수
		divLayerOn : function(wrapperClass,layerClass,closeClass) {

			// Layer close Event func
			$('.'+closeClass).on("click", function(e){
				 e.preventDefault();
				  $(this).parents('.'+layerClass).addClass('hide');
				  $('.'+wrapperClass).addClass('hide');
			});

			// Layer 음영 Click Event Func
			$('.'+wrapperClass).on("click", function(){
			    	$(this).addClass('hide');
			    	$('.'+layerClass).addClass('hide');
			});

			exsoft.util.layout.lyrPopupWindowResize($("."+layerClass));
			$(window).resize(function(){
				exsoft.util.layout.lyrPopupWindowResize($("."+layerClass));
			});

		}

}; // exsoft.util.layout end...

exsoft.util.layout.prototype = {

}; // exsoft.util.layout.prototype end...


/***********************************************
 * ajax
 **********************************************/
/**
 * ajax 관련 util
 *
 * @namespace : exsoft.util.ajax
 *
 */
exsoft.util.ajax = {
		/**
		 * ajax form 공통처리 함수
		 * @param formId - 서버에 넘겨질 form id
		 * @param urls - 서버 호출 urls
		 * @param param - return 처리시 분기할 파라미터값
		 * @param divId - buttonId
		 */
		ajaxFunction : function(formId,urls,param)  {

			jQuery.ajax({
				url: urls,
				global: false,
				type: "POST",
				data: $("#"+formId).serialize(),
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					exsoft.util.ajax.loadingHide();
					// 결과값을 받아서 처리할 함수
					returnAjaxFunction(data,param);
				},
				beforeSend:function() {
					exsoft.util.ajax.loadingShow();
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorChk(response);
				}

			});

		},

		/**
		 * ajax multi form 공통처리 함수
		 * @param formId1 - 서버에 넘겨질 form id1
		 * @param formId2 - 서버에 넘겨질 form id2
		 * @param urls  - 서버 호출 urls
		 * @param param - return 처리시 분기할 파라미터값
		 */
		ajaxMuitiFrmFunction : function(formId1,formId2,urls,param)  {
			jQuery.ajax({
				url: urls,
				global: false,
				type: "POST",
				data: $("#"+formId1+",#"+formId2).serialize(),
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					exsoft.util.ajax.loadingHide();

					// 결과값을 받아서 처리할 함수
					returnAjaxMuitiFrmFunction(data,param);
				},
				beforeSend:function() {
					exsoft.util.ajax.loadingShow();
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorChk(response);
				}

			});

		},

		/**
		 * ajax json 공통처리함수
		 * @param jsonObject - 서버에 넘겨질 data 값 json 형식으로 key:value
		 * @param urls - 서버 호출 urls
		 * @param param - return 처리시 분기할 파라미터값
		 * @usage var jsonObject = { "id":"outsider", "sex":"male" }; 형태로 파라미터를 생성해서 처리해준다.
		 */
		ajaxDataFunction : function(jsonObject,urls,param)  {

			jQuery.ajax({
				url: urls,
				global: false,
				type: "POST",
				data: jsonObject,
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					exsoft.util.ajax.loadingHide();
					// 결과값을 받아서 처리할 함수
					returnAjaxDataFunction(data,param);
				},
				beforeSend:function() {
					exsoft.util.ajax.loadingShow();
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorChk(response);
				}

			});
		},

		/**
		 *  ajax json 공통처리함수
		 */
		ajaxFunctionNoLodingWithCallback : function(formId,urls,param,callbackFunction)  {

			jQuery.ajax({
				url: urls,
				global: false,
				type: "POST",
				data: $("#"+formId).serialize(),
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					// 결과값을 받아서 처리할 함수
					callbackFunction(data,param);
				},
				beforeSend:function() {
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorChk(response);
				}

			});

		},

		/**
		 * ajax json 공통처리함수
		 * @param jsonObject - 서버에 넘겨질 data 값 json 형식으로 key:value
		 * @param urls
		 * @param param
		 * @param callbackFunction
		 */
		ajaxDataFunctionWithCallback : function(jsonObject, urls, param, callbackFunction) {
			jQuery.ajax({
				url: urls,
				global: false,
				type: "POST",
				data: jsonObject,
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					exsoft.util.ajax.loadingHide();
					// 결과값을 받아서 처리할 함수
					callbackFunction(data,param);
				},
				beforeSend:function() {
					exsoft.util.ajax.loadingShow();
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorChk(response);
				}

			});
		},

		/**
		 * ajax json 공통처리함수 - 로딩메세지 없음
		 * @param jsonObject
		 * @param urls
		 * @param param
		 * @param callbackFunction
		 */
		ajaxDataFunctionNoLodingWithCallback : function(jsonObject, urls, param, callbackFunction) {
			jQuery.ajax({
				url: urls,
				global: false,
				type: "POST",
				data: jsonObject,
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					callbackFunction(data,param);
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorChk(response);
				}

			});
		},

		/**
		 *
		 * ajax form callback 공통처리 함수
		 * @param formId - 서버에 넘겨질 form id
		 * @param urls - 서버 호출 urls
		 * @param param - return 처리시 분기할 파라미터값(사용하지 않음)
		 * @callbackFunction - return 처리함수
		 */
		ajaxFunctionWithCallback : function(formId,urls,param,callbackFunction)  {

			jQuery.ajax({
				url: urls,
				global: false,
				type: "POST",
				data: $("#"+formId).serialize(),
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					exsoft.util.ajax.loadingHide();
					// 결과값을 받아서 처리할 함수
					callbackFunction(data,param);
				},
				beforeSend:function() {
					exsoft.util.ajax.loadingShow();
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorChk(response);
				}

			});

		},

		/**
		 * 새창에 Ajax Call Function
		 * @param formId
		 * @param urls
		 * @param callbackFunction
		 */
		ajaxPopFunctionWithCallback : function(formId,urls,callbackFunction)  {

			jQuery.ajax({
				url: urls,
				global: false,
				type: "POST",
				data: $("#"+formId).serialize(),
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					exsoft.util.ajax.loadingHide();
					// 결과값을 받아서 처리할 함수
					callbackFunction(data,param);
				},
				beforeSend:function() {
					exsoft.util.ajax.loadingShow();
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorPopChk(response);
				}

			});

		},

		/**
		 * ajax json 공통처리함수
		 * @param jsonObject - 서버에 넘겨질 data 값 json 형식으로 key:value
		 * @param urls
		 * @param callbackFunction
		 */
		ajaxPopDataFunctionWithCallback : function(jsonObject, urls, callbackFunction) {
			jQuery.ajax({
				url: urls,
				global: false,
				type: "POST",
				data: jsonObject,
				dataType: "json",
				async:true,
				cache:false,
				clearForm:true,
				resetForm:true,
				success: function(data){
					exsoft.util.ajax.loadingHide();
					// 결과값을 받아서 처리할 함수
					callbackFunction(data);
				},
				beforeSend:function() {
					exsoft.util.ajax.loadingShow();
				},
				error: function(response, textStatus, errorThrown){
					exsoft.util.error.isErrorPopChk(response);
				}

			});
		},
		/**
		 * Ajax Loading 메세지 보이지
		 */
		loadingShow : function() {
			$(".loading_wrapper").removeClass("hide");
			$(".loading").removeClass("hide");
		},

		/**
		 * Ajax Loading 메세지 숨기기
		 */
		loadingHide : function() {
			$(".loading_wrapper").addClass("hide");
			$(".loading").addClass("hide");
		},
}; // exsoft.util.ajax end...

exsoft.util.ajax.prototype = {

}; // exsoft.util.ajax.prototype end...


/***********************************************
 * check
 **********************************************/
/**
 * check 관련 util
 *
 * @namespace : exsoft.util.error
 *
 */
exsoft.util.check = {
		////////// 유효성 체크 ////////
		/**
		 *  webfolder 파일/폴더명 체크
		 * @param str
		 * @returns {Boolean}
		 */
		webfolderCheck : function(str) {
			var word = /[\\/:*?\"<>|]/g;

			if(word.test(str)) {
				return false;
			} else {
				return true;
			}
		},

		/**
		 *  폴더경로 체크
		 * @param str
		 * @returns {Boolean}
		 */
		folderPathCheck : function(str) {
			var word = /[\*?\"<>|]/g;

			if(word.test(str)) {
				return false;
			} else {
				return true;
			}
		},

		/**
		 * 이메일 유효성 체크
		 * @param str
		 * @returns {Boolean}
		 */
		emailCheck : function(str) {
			var email = /^[-_.0-9A-Za-z]+@[0-9A-Za-z]+[-0-9A-Za-z]*[0-9A-Za-z]+\.[A-Za-z]+(\.[A-Za-z]+)*$/i;

			if(email.test(str)) {
				return true;
			} else {
				return false;
			}
		},


		/**
		 * 핸드폰 번호 유효성 체크
		 * @param str
		 * @returns {Boolean}
		 */
		phoneCheck : function(str) {
			var phone = /^\d{2,3}-\d{3,4}-\d{4}$/;

			if (phone.test(str)) {
				return true;
			} else {
				return false;
			}
		},

		/**
		 * 사용자 아이디 유효성 체크
		 * @param str
		 * @returns {Boolean}
		 */
		userIdCheck : function(str)  {

			var word = /^[a-z]{1}[a-z0-9._%-]*$/;

			if(word.test(str)) {
				return true;
			} else {
				return false;
			}
		},

		/**
		 * 검색어 필터 유효성 체크
		 * @param str
		 */
		searchWordCheck : function(str) {

			var filter = "!@#$%^&*._-\\\,'`~/=+;:<>/?(){}[]|";
			var temp_reg = "";
			var temp_qry = str;

			var lengthCnt = filter.length; // 속도를 위해  for문에 선언 안함
			for (var i = 0; i < lengthCnt; i++) {
				temp_reg = filter.charAt(i);
				temp_qry = temp_qry.split(temp_reg).join(" ");
			}

			if (str.length > 0 && temp_qry.trim().length <= 0) {
				return false;
			}else {
				return true;
			}
		},

		/**
		 * 통계 - 검색날짜 유효성 체크
		 * @param startDt - 검색시작일
		 * @param endDt - 검색종료일
		 */
		searchValid : function(startDt,endDt) {

			if(startDt.length != 10 || endDt.length != 10) {
				jAlert('검색기간을 정확히 입력하세요',"확인",0);
				return false;
			}

			var startDate = startDt.split("-");
			var endDate =  endDt.split("-");
			var sDate = new Date(startDate[0], startDate[1], startDate[2]).valueOf();
			var eDate = new Date(endDate[0], endDate[1], endDate[2]).valueOf();

			if( sDate > eDate )	{
				jAlert("시작일이 종료일보다 클 수 없습니다.","확인",0);
				return false;
			}

			if(exsoft.util.date.diffDays(sDate,eDate) > 365) {
				jAlert("최대 검색기간은 1년을 초과할 수 없습니다.","확인",0);
				return false;
			}

			return true;
		},

		/**
		 * url 이 유효한지 체크한다.
		 * @param urls
		 * @returns {Boolean}
		 */
		isValidUrl : function(urls) {

			var chkExp = /http:\/\/([\w\-]+\.)+/g;

		 	if (chkExp.test(urls)) {
		  		return true;
		 	} else {
		  		return false;
		 	}
		},

		/**
		 * 한글체크
		 * @param obj
		 * @returns {Boolean}
		 */
		hanWordCheck : function(obj) {
			var pattern = /[^(ㄱ-힣)]/; 		// 한글인 경우
			if (pattern.test(obj.value)) {
				return true;
			}else {
				return false;
			}
		},

		/**
		 * 문서유형 속성리스트에서 중복된 속성값이 입력되었는지 체크한다.
		 * @param jsonArr
		 * @param param
		 * @returns {Boolean} :: true - 중복값 존재 false - OK
		 */
		inputArrayValid : function(jsonArr,param) {

			var results = new Array();

			for (var j=0; j<jsonArr.length; j++) {

				var key = jsonArr[j][param];

				if (!results[key]) results[key] = 1;
				else results[key] = results[key] + 1;

			}

			for (var k in results) {
				if(results[k] > 1) {
					return true;
				}
			}

			return false;
		},

}; // exsoft.util.check end...


exsoft.util.check.prototype = {

}; // exsoft.util.check.prototype end...


/***********************************************
 * date
 **********************************************/
/**
 * date 관련 util
 *
 * @namespace : exsoft.util.date
 *
 */
exsoft.util.date = {
		////////// Date 관련 //////////
		/**
		 * 날짜 계산하기
		 * @param type - d:일 / m:월
		 * @param addVal - 변경한 일/월
		 * @param dateType - 날짜
		 * @param delimiter - 구분자
		 * @usage 2008-01-01 3일 => exsoft.util.date.addDate("d",3,"2008-01-01","-")
		 * @usage 2008-01-01 3개월 => exsoft.util.date.addDate("m",,3,"2008-01-01","-")
		 */
		addDate : function(type,addVal,dateType,delimiter)	{

			var yyyy,mm,dd;
			var cDate,oDate;
			var cYear,cMonth,cDay;

			if(delimiter != "") {
				dateType = dateType.replace(eval("/\\" + delimiter + "/g"),"");
			}

			yyyy = dateType.substr(0,4);
			mm = dateType.substr(4,2);
			dd = dateType.substr(6,2);

			if(type == "yyyy") {
				yyyy = (yyyy*1) + (addVal*1);
			}else if(type == "m") {
				mm = (mm*1) + (addVal*1);
			}else if(type == "d") {
				dd = (dd*1) + (addVal*1);
			}

			cDate = new Date(yyyy,mm-1,dd);
			cYear = cDate.getFullYear();
			cMonth = cDate.getMonth()+1;
			cDay = cDate.getDate();

			cMonth = cMonth < 10 ? "0" + cMonth : cMonth;
			cDay = cDay < 10 ? "0" + cDay : cDay;

			if(delimiter != "") {
				return cYear + delimiter + cMonth + delimiter + cDay;
			}else {
				return cYear + cMonth + cDay;
			}
		},

		/**
		 * 두 입력날짜의 차이 :: Javascript Date형
		 * @param start
		 * @param end
		 * @returns {Number}
		 */
		diffDays : function(start,end) {
			return (end-start)/(1000*60*60*24)
		},

		/**
		 * DatePicker Today 구하기
		 */
		todayStr : function() {

			var toDate = "";

			var today = new Date();
			var year = today.getFullYear();		// FF Bug Patch
			var month = today.getMonth()+1;

			if(month < 10) {
				month = "0"+month;
			}

			var day = today.getDate();
			if(day < 10) {
				day = "0"+day;
			}

			toDate = year+"-"+month+"-"+day;

			return toDate;
		},

		/**
		 * 통계 및 검색화면 날짜 선택 처리
		 * @param obj
		 */
		changeDate : function(value, sdateId, edateId) {
			var obj = {};
			obj.value = value;
			obj.startId = sdateId;
			obj.endId = edateId;

			exsoft.util.date.setDate(obj);
		},

		/**
		 * 날짜 변경 처리
		 * @param obj
		 */
		setDate : function(obj) {

			var toDate = exsoft.util.date.todayStr();

			switch(obj.value) {
				case "one_month":			// 1month
					$(exsoft.util.common.getIdFormat(obj.startId)).val(exsoft.util.date.addDate("m",-1,toDate,"-"));
					$(exsoft.util.common.getIdFormat(obj.endId)).val(toDate);
					break;
				case "three_month":			// 3month
					$(exsoft.util.common.getIdFormat(obj.startId)).val(exsoft.util.date.addDate("m",-3,toDate,"-"));
					$(exsoft.util.common.getIdFormat(obj.endId)).val(toDate);
					break;
				case "half_year":			// 6month
					$(exsoft.util.common.getIdFormat(obj.startId)).val(exsoft.util.date.addDate("m",-6,toDate,"-"));
					$(exsoft.util.common.getIdFormat(obj.endId)).val(toDate);
					break;
				case "one_year":			// 1year
					$(exsoft.util.common.getIdFormat(obj.startId)).val(exsoft.util.date.addDate("m",-12,toDate,"-"));
					$(exsoft.util.common.getIdFormat(obj.endId)).val(toDate);
					break;
				default :			// 직접입력 OR 전체
					$(exsoft.util.common.getIdFormat(obj.startId)).val('');
					$(exsoft.util.common.getIdFormat(obj.endId)).val('');
					break;
			}
		},

		/**
		 * 년도 선택박스 설정하기
		 */
		selectYearBox : function(minYear,maxYear,divIds) {

			var buffer = "";

			for(var i=minYear;i<maxYear;i++)	{
				$("#"+divIds).append("<option value='"+i+"'>"+i+"년</option>");
			}

			var today = new Date();
			$("#"+divIds).val(today.getFullYear());
		},

}; // exsoft.util.date end...

exsoft.util.date.prototype = {

}; // exsoft.util.date.prototype end...

exsoft.util.grid = {

		/**
		 * 페이지번호 입력창 숫자 입력 및 캡션 보이기여부
		 * @param isCaption - 캡션 사용여부
		 * @param skinId - 스킨클래스명
		 */
		gridInputInit : function(isCaption,skinId) {

			//$('.ui-pg-input').numeric();
			//$('.ui-pg-input').css("ime-mode","disabled");

			if(isCaption) {
				$(".ui-jqgrid-titlebar").hide();
			}


			// 레이아웃 공통 스킨처리 :: 기본테마=Blue
			//if (skinId == undefined) {	skinId = "grid_liteGray";		}

			// Jqgrid 공통 CSS 변경처리
			/*
			$(".ui-jqgrid-htable" ).removeClass( "ui-jqgrid-htable" ).addClass( skinId );
			$(".ui-jqgrid-btable" ).addClass( skinId );
			$('.ui-jqgrid .ui-jqgrid-bdiv').css({ 'overflow-y': 'auto','overflow-x':'hidden' });
			$('.ui-pg-input').css({'height':'19px','margin-bottom':'0px','margin-left':'0px','width':'35px'});		// 페이징 숫자 위치 조정

			*/
			//$('.ui-jqgrid .ui-jqgrid-bdiv').css({ 'overflow-y': 'auto','overflow-x':'hidden' });
			//$('.ui-jqgrid-sortable').css({'height':'19px'});														// Table 헤더 정렬 변경
		},

		/**
		 * 그리드 Title 바 숨기기
		 * @param gridIds
		 */
		gridTitleBarHide : function(gridIds) {
			$("#gview_"+gridIds+" > .ui-jqgrid-titlebar").hide();
		},

		/**
		 * 그리드 No Msg 초기화
		 * @param gridIds
		 */
		gridNoDataMsgInit : function(gridIds) {
			$("#"+gridIds+"_nodata").remove();
		},

		/**
		 * 그리드 Pager 숨기기
		 * @param gridIds
		 */
		gridPagerHide : function(gridIds) {
			$(".ui-pg-table",$('#'+gridIds).getGridParam('pager')).hide();
		},

		/**
		 * 그리드 레코드 없음 표시 공통처리
		 * @param divIds
		 * @param layerId
		 */
		gridNoRecords : function(divIds,layerId)	{

			$("#emptyPage").show();
			$("#dataPage").hide();

			exsoft.util.grid.gridPagerViewHide(divIds);
			exsoft.util.grid.gridNoDataMsg(divIds,layerId);
			exsoft.util.grid.gridPagerHide(divIds);
		},

		/**
		 * 그리드 레코드 수 보기 숨김
		 * @param gridIds
		 */
		gridPagerViewHide : function(gridIds) {
			$(".ui-paging-info",$('#'+gridIds).getGridParam('pager')).html('');
		},

		/**
		 * 그리드 Pager 보이기
		 * @param gridIds
		 */
		gridPagerShow : function(gridIds) {
			$(".ui-pg-table",$('#'+gridIds).getGridParam('pager')).show();
		},

		/**
		 * 그리드 레코드 있음 표시 공통처리
		 * @param divIds
		 */
		gridViewRecords : function(divIds)	{

			$("#emptyPage").hide();
			$("#dataPage").show();

			exsoft.util.grid.gridPagerViewHide(divIds);
			exsoft.util.grid.gridPagerShow(divIds);
		},

		/**
		 * 그리드 내 페이징 처리
		 *
		 * @param gridId - 그리드 id
		 * @param newValue - 사용자가 입력한 페이지값
		 * @param pgButton - 페이지 버튼 객체
		 */
		onPager : function(gridId,newValue,pgButton)		{

			var requestedPage = $("#"+gridId).getGridParam("page");
			var lastPage = $("#"+gridId).getGridParam("lastpage");
			var rows = $('#'+gridId).closest('.ui-jqgrid').find('.ui-pg-selbox').val();
			var nPage = 0;

			if (parseInt(requestedPage) > parseInt(lastPage) || parseInt(newValue) > parseInt(lastPage) ) {

				$("#"+gridId).setGridParam({page:lastPage,postData:{is_search:'false'}}).trigger("reloadGrid");
			}

			if (pgButton.indexOf("next") >= 0)
				nPage = ++requestedPage;
		    else if (pgButton.indexOf("prev") >= 0)
		    	nPage = --requestedPage;
		    else if (pgButton.indexOf("last") >= 0)
		    	nPage = $("#"+gridId).getGridParam('lastpage');
		    else if (pgButton.indexOf("first") >= 0)
		    	nPage = 1;
		    else if (pgButton.indexOf("user") >= 0)
		    	nPage = newValue;

			// 페이지 개수 재설정 및 reload
			$("#"+gridId).setGridParam({rowNum:rows});
			$("#"+gridId).setGridParam({page:nPage,postData:{is_search:'false',page_init:'false'}}).trigger("reloadGrid");
		},

		/**
		 * Grid 컬럼정렬 처리
		 * @param gridIds 그리드ID
		 * @param headerData 컬럼 JsonData
		 * @param align 정렬기준
		 */
		gridColumHeader : function(gridIds,headerData,align) {
			var result =  $.parseJSON(headerData);
			$.each(result, function(key, value) {
				 $("#"+gridIds).jqGrid('setLabel',key,value,{'text-align':align});
			});
		},

		/**
		 * 그리드 Edit 모드에서 rowid 개수 체크
		 * @param gridIds
		 */
		gridEditRowCnt : function(gridIds) {

			var rowIDs = $("#"+gridIds).jqGrid('getDataIDs');
			return rowIDs.length;
		},

		/**
		 * 목록 개수/크기 공통 Array
		 */
		listArraySize : function() {
			var listSize = [5,10,15,20,30,50];
			return listSize;
		},

		/**
		 * 그리드 자동 리사이즈 처리
		 * @param gridId - 그리드ID
		 * @param targetId - 레이어ID
		 * @param resizeHeight - 조정높이값 = 72
		 * @param resizeWidth - 조정넓이값 = 72
		 */
		gridResize : function(gridId,targetId,resizeHeight,resizeWidth) {

			$(window).bind('resize', function() {

				var width = jQuery("#"+targetId).width();
				var height = jQuery("#"+targetId).height();
				// CSS 변경에 따른 사이즈 조정처리
				if (resizeWidth == undefined) {
					$("#"+gridId).setGridWidth(width-2);
				}else {
					$("#"+gridId).setGridWidth(width-resizeWidth);
				}
				$("#"+gridId).setGridHeight(height-resizeHeight);
			}).trigger('resize');
		},

		/**
		 * 자측메뉴 펼치기/숨기기 이벤트에 대한 그리드 자동 리사이즈 처리
		 * @param gridId - 그리드ID
		 * @param targetId - 레이어ID
		 * @param resizeHeight - 조정높이값 = 72
		 * @param resizeWidth - 조정넓이값 = 72
		 */
		gridIsLeftMenuResize : function(gridId,targetId,resizeHeight,resizeWidth){

			var width = jQuery("#"+targetId).width();
			var height = jQuery("#"+targetId).height();
			// CSS 변경에 따른 사이즈 조정처리
			$("#"+gridId).setGridWidth(width-resizeWidth);
			$("#"+gridId).setGridHeight(height-resizeHeight);
		},

		/**
		 * 그리드 전체 체크box 해제
		 * @param gridIds
		 */
		gridCheckBoxInit : function(gridIds) {
			$('#'+gridIds).jqGrid('resetSelection');
		},

		/**
		 * 그리드 체크된 row 지우기
		 * @param gridIds
		 * @param noDeleteRowId : 해당 grid rowId(값)는 지우지 않는다
		 * @param msg : 삭제 제외 메세지
		 * @param isDeleteRow : noDeleteRowId 값 row 삭제 여부
		 * @returns {Boolean}
		 */
		gridDeleteRow : function(gridIds, noDeleteRowId, msg, isDeleteRow) {
			if(!this.gridSelectCheck(gridIds))	{
				jAlert("삭제할 항목을 선택하세요.");
				return false;
			}

			// 선택한 ROW 삭제 버그가 있어서 역순으로 처리
			try {
				var selectedRowIds  = $("#"+gridIds).getGridParam('selarrrow');
				for(var i=selectedRowIds.length-1; i>=0; i--){
					if( !isDeleteRow && selectedRowIds[i] == noDeleteRowId){
						$('#'+gridIds).jqGrid('setSelection',selectedRowIds[i],false); //checkbox 해제
					}
					else {
						$("#"+gridIds).delRowData(selectedRowIds[i]);
					}
				}
			}finally{
				selectedRowIds = null;
			}
		},

		/**
		 * Grid Refresh
		 * @param divId - 그리드ID
		 * @param urls - url 주소
		 */
		gridRefresh : function(divId,urls) {

			$("#"+divId).jqGrid('setGridParam', {
				url: urls,
				datatype: "json"
			 }).trigger("reloadGrid");
		},

		/**
		 * Jqgrid excel download
		 * @param divIds
		 * @param urls
		 */
		excelDown : function(divIds,urls) {
			$("#"+divIds).jqGrid('excelExport',{tag:'excel',url:urls});
		},

		/**
		 * Grid Refresh
		 * @param divId - 그리드ID
		 * @param urls - url 주소
		 * @param postData - 넘길data json 형식
		 */
		gridPostDataRefresh : function(divId,urls,postData) {
			// postData값과 중복되지 않는 기존 postData 값은 변경되지 않는다.
			$(exsoft.util.common.getIdFormat(divId)).jqGrid('setGridParam', {
				url: urls,
				datatype: "json",
				postData: postData
			 }).trigger("reloadGrid");
		},

		/**
		 * Grid Refresh(postData 전체 초기화)
		 * @param divId - 그리드ID
		 * @param urls - url 주소
		 * @param postData - 넘길data json 형식
		 */
		gridPostDataInitRefresh : function(divId,urls,postData) {
			// 기존 postData를 전체 초기화 시킨다
			$(exsoft.util.common.getIdFormat(divId)).setGridParam ({postData: null});

			$(exsoft.util.common.getIdFormat(divId)).jqGrid('setGridParam', {
				url: urls,
				datatype: "json",
				postData: postData
			 }).trigger("reloadGrid");
		},

		/**
		 * 그리드 No Msg 보여주기
		 * @param gridIds
		 * @param classId - no_data : 메인 nolayer_data : 레이어Pop
		 */
		gridNoDataMsg : function(gridIds,classId) {
			var noMessage = "<div id='"+gridIds+"_nodata' class='"+classId+"'>"+$('#'+gridIds).getGridParam('emptyDataText')+"</div>";
			$(exsoft.util.common.getIdFormat(gridIds)).after(noMessage);
		},

		/**
		 * JQgrid 에서 File용량 계산하기
		 * @param bytes
		 * @returns {String}
		 */
		bytes2Size : function(bytes) {
		        var sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
		        if (bytes == 0) return '0B';
		        else if(bytes == -1) return '-1';

		        var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
		        var size = (i<2) ? Math.round((bytes / Math.pow(1024, i))) : Math.round((bytes / Math.pow(1024, i)) * 100)/100;
		        return  size + ' ' + sizes[i];
		},

		/**
		 * JqGrid 페이징처리 : 1차고도화 디자인 적용
		 * @param gridId
		 * @param data
		 * @returns {String}
		 */
		gridPager : function(gridPagerId,data){

			$(gridPagerId).empty();
			return $(gridPagerId).append(data.pagingInfo.strLinkPagePrev)
			.append(data.pagingInfo.strLinkPageList)
			.append(data.pagingInfo.strLinkPageNext);
		},

		/**
		 * AclItem 기본권한 영문 -> 한글
		 */
		getAclItemTitle : function(en_title) {
			switch(en_title) {
				case "DELETE" : return "삭제"; break;
				case "UPDATE" : return "수정"; break;
				case "READ" : return "조회"; break;
				case "BROWSE" : return "목록"; break;
				case "NONE" : return "권한없음"; break;
				default : return "권한없음"; break;
			}
		},

		/**
		 * 그리드 체크박스 선택여부
		 * @param gridIds 그리드 ID
		 */
		gridSelectCheck : function(gridIds) {

			var list =  $("#"+gridIds).getGridParam('selarrrow');

			if(list.length == 0)	{
				return false;
			}
			return true;
		},

		/**
		 * grid row_id에 해당하는 특정 cell 값을 비교
		 * @param gridIds : grid divID
		 * @param row_id : 비교 대상 row의 id
		 * @param row_name : 비교 대상 row의 특정 cell name
		 * @param compare_value : 비교 대상 row의 특정 cell값과 비교할 값
		 * @returns {Boolean}
		 */
		gridIsRowDataExist : function(gridIds, row_id, row_name, compare_value){
			// 주의 : row_name이 변수인 관계로 chaining 방식이 아닌 배열 호출 방식으로 값을 불러 온다
			if( $(exsoft.util.common.getIdFormat(gridIds)).getRowData(row_id)[row_name] == compare_value)
				return true;
			else
				return false;
		},

		/**
		 * 그리드 체크된 컬럼ID값을 가져온다.
		 * @param gridIds 그리드 ID
		 * @param param 그리드 컬럼ID
		 * @returns {String}
		 */
		gridSelectData : function(gridIds,param) {

			var result = "";
			var list = $("#"+gridIds).getGridParam('selarrrow');
			for (var j = 0; j < list.length; j++) {
				var rowdata = $("#"+gridIds).getRowData(list[j]);
				result += rowdata[param];

				// 마지막 원소일 경우 ','를 추가하지 않는다
				if ((j + 1) != list.length) {
					result += ",";
				}
			}

			return result;
		},

		/**
		 *
		 * @param gridIds
		 * @param rowId
		 * @returns {String} 1:edited , 0:not edited
		 */
		gridEditMode : function(gridIds,rowId)	{
			var edited = "0";
			var ind = $("#"+gridIds).getInd(rowId,true);
			if(ind != false){
				edited = $(ind).attr("editable");
			}

			return edited;
		},

		/**
		 * Tree그리드 헤더내 체크박스 선택
		 * @param e
		 * @param id
		 * @param name
		 */
		gridAllCheck : function(e,id,name) {

			// grid colum header exception
			e = e || event;
			e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;

			var check = $('#'+id).is(':checked');

			$('input[name='+ name +']').each(function() {
				if($(this).attr("disabled") != "disabled") {
					this.checked = check;
				}
			});
		},

		gridAllCheckFF : function(id,name) {

			var check = $('#'+id).is(':checked');

			$('input[name='+ name +']').each(function() {
				if($(this).attr("disabled") != "disabled") {
					this.checked = check;
				}
			});
		},

		/**
		 * 접근권한 라디오 선택 처리.
		 * @param level - 메뉴레벨
		 * @param rowId - 메뉴아이디
		 * @param cellValue - 접근권한(전사/하위부서포함/소속부서)
		 */
		radioFormatter : function(level,rowId,cellValue,type) {

			var disableOpt = "";
			var returnVal = "";

			if(level == 0){		disableOpt = "disabled";		}

			switch(type)	{

				case  'ALL' :
					if(cellValue == "ALL")	{
						returnVal =  '<input type="radio"  ' + disableOpt + ' onclick="javascript:exsoft.util.grid.checkBoxChanged(\''+ rowId +'\',event);" value="ALL" name="radio_' + rowId + '" checked />';
		    		}else {
		    			returnVal =  '<input type="radio"  ' + disableOpt + ' onclick="javascript:exsoft.util.grid.checkBoxChanged(\''+ rowId +'\',event);" value="ALL" name="radio_' + rowId + '"/>';
		    		}
					break;
				case 'GROUP' :
					if(cellValue == "GROUP")	{
						returnVal = '<input type="radio"  ' + disableOpt + ' onclick="javascript:exsoft.util.grid.checkBoxChanged(\''+ rowId +'\',event);" value="GROUP" name="radio_' + rowId + '" checked />';
		    		}else {
		    			returnVal = '<input type="radio"  ' + disableOpt + ' onclick="javascript:exsoft.util.grid.checkBoxChanged(\''+ rowId +'\',event);" value="GROUP" name="radio_' + rowId + '"/>';
		    		}
					break;
				case 'TEAM' :
					if(cellValue == "TEAM")	{
		    			return '<input type="radio"  ' + disableOpt + ' onclick="javascript:exsoft.util.grid.checkBoxChanged(\''+ rowId +'\',event);" value="TEAM" name="radio_' + rowId + '" checked />';
		    		}else {
		    			return '<input type="radio"  ' + disableOpt + ' onclick="javascript:exsoft.util.grid.checkBoxChanged(\''+ rowId +'\',event);" value="TEAM" name="radio_' + rowId + '"/>';
		    		}
					break;
			}

			return returnVal;
		},

		// 라디오버튼 선택처리
		checkBoxChanged : function(menu_cd,e) {

			e = e || event;
			e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;

			$("input:checkbox[name='idx']").each(function() {

				var str = $(this).val();

				if(str.indexOf(menu_cd) != -1) {
					$(this).prop("checked",true);
				}

		    });
		},

		/**
		 * treeGrid 체크박스 선택헤제 처리
		 * @param e
		 * @param checkBox
		 */
		treeGridChckAllRelease : function(e,checkBox) {

			e = e || event;
			e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;

			$("#"+checkBox).attr('checked',false);
		},

		/**
		 *  treeGrid 체크박스 선택헤제 처리
		 */
		treeGridChckAllReleaseFF : function(checkBox) {
			$("#"+checkBox).attr('checked',false);
		},

		// CheckBox Browser Bug Fix
		checkBox : function(e)	{
			e = e||event;/* get IE event ( not passed ) */
			e.stopPropagation? e.stopPropagation() : e.cancelBubble = true;
		},

		/**
		 * 그리드 전체 Row의 컬럼ID값을 가져온다.
		 * @param gridIds 그리드 ID
		 * @param param 그리드 컬럼ID
		 * @returns {String}
		 */
		gridSelectDataAllRow : function(gridIds,param) {

			var result = "";
			var list = $("#"+gridIds).jqGrid('getDataIDs');
			for (var j = 0; j < list.length; j++) {
				var rowdata = $("#"+gridIds).getRowData(list[j]);
				result += rowdata[param];

				// 마지막 원소일 경우 ','를 추가하지 않는다
				if ((j + 1) != list.length) {
					result += ",";
				}
			}

			return result;
		},

		/**
		 * selectAccessorWindow 공통 callback 합수
		 * grid에 선택한 접근자를 추가 한다.
		 * @param aclItemRowList
		 */
		gridSetAclItemAddCallback : function(gridIds, aclItemRowList) {
			var aclItemIdList = exsoft.util.grid.gridSelectArrayDataAllRow(gridIds, "accessor_id", "accessor_id");
			// 선택된 접근자 목록을 추가함
			$(aclItemRowList).each(function() {
				// 중복데이터 필터링
				var isDuplicate = false;
				var row = this;

				$(aclItemIdList).each(function(i) {
					if (this.accessor_id == row.accessor_id)
						isDuplicate = true;
				})

				if (!isDuplicate) {
					$("#"+gridIds).jqGrid("addRowData", this.accessor_id, this);
					$('#'+gridIds).editRow(this.accessor_id,true);
				}
			});
		},

		/**
		 * 그리드 전체 컬럼ID값을 가져온다.
	     * @param gridIds 그리드 ID
		 * @param param 그리드 컬럼ID
		 * @returns {Array}
		 */
		gridSelectArrayDataAllRow : function(gridIds,param,key) {

			var jsonArr = [];
			var jsonArrIndex = 0;
			var list = $("#"+gridIds).jqGrid('getDataIDs');

			for (var j = 0; j < list.length; j++) {

				var result = {};
				var rowdata = $("#"+gridIds).getRowData(list[j]);

				result[key] = rowdata[param];

				if(result[key]) {
					jsonArr[jsonArrIndex] = result;
					jsonArrIndex++;
				}
			}
			try {
				return jsonArr;
			}finally{
				result = null;
				rowdata = null;
				jsonArr = null;
			}
		},
}; // exsoft.util.grid end...

exsoft.util.grid.prototype = {

}; // exsoft.util.grid.prototype end...

/***********************************************
 * filter
 **********************************************/
/**
 * filter 관련 util
 *
 * @namespace : exsoft.util.filter
 *
 */
exsoft.util.filter = {
		////////// Filter 관련 //////////
		/**
		 * 입력창 필터함수
		 * @param filter - filterKey함수의 매개변수로 [0-9], [a-z], [A-Z]를 넣어주면 각각 숫자만 영문소문자, 영문대문자만 입력가능하게 한다.
		 */
		inputBoxFilter : function(filter) {

			if(filter) {
				var sKey = String.fromCharCode(event.keyCode);
				var re = new RegExp(filter);
				if(!re.test(sKey)) return false;
			}
			return true;
		},



		/**
		 * 숫자만 입력되게 처리 - IE를 제외한 Browser 처리
		 * @param val
		 * @returns
		 */
		numLine : function() {

			$('.numline').numeric();
			$('.numline').css("ime-mode","disabled");

			// IE Browser 제외
			$('.numline').keyup(function(event) {
				this.value=this.value.replace(/[^0-9]/g,'')
			});

			$('.numline').focusout(function(event) {
				this.value=this.value.replace(/[^0-9]/g,'')
			});
		},

		/**
		 * 입력창 최대값 처리
		 */
		maxNumber : function() {

			$(":input").keyup(function(){

				var inputLength = 0;
				var strTemp = "";
				var input = 0;

				// 한글(2Byte),영어(1Byte) 공통 적용 로직
				var lengthCnt = $(this).val().length; // 속도를 위해  for문에 선언 안함
				for(var i=0;i<lengthCnt;i++)	{

					if (escape($(this).val().charAt(i)).length >= 4) {
						inputLength += 2;
					}else if (escape($(this).val().charAt(i)) != "%0D") {
						inputLength++;
					}

					// maxlength 비교처리
					if (inputLength > $(this).attr('maxlength')) {
						strTemp = $(this).val().substr(0,input);
						break;
					}

					input++;
				}

				if(strTemp.length > 0)	{
					$(this).prop('value',strTemp);
					$(this).focus();
				}

			});

		},

		/**
		 * 숫자만 체크
		 * @param e
		 * @usage onKeyPress="exsoft.util.filter.numInput(event);"
		 */
		numInput : function(e) {
			event = event || window.event;
            var keyID = (event.which) ? event.which : event.keyCode;
            if( ( keyID >=48 && keyID <= 57 ) || ( keyID >=96 && keyID <= 105 )
            		|| keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 ){
                return;
            }else{
                return false;
            }
		},

}; // exsoft.util.filter end...

exsoft.util.filter.prototype = {

}; // exsoft.util.filter.prototype end...


/***********************************************
 * error
 **********************************************/
/**
 * error 관련 util
 *
 * @namespace : exsoft.util.error
 *
 */
exsoft.util.error = {
		////////// Error Handling //////////
		/**
		 * String To Xml Object
		 * @param text
		 */
		stringToXml : function(text)	{
			var doc = "";
			if (window.ActiveXObject){
					doc=new ActiveXObject('Microsoft.XMLDOM');
					doc.async='false';
					doc.loadXML(text);
				} else {
					var parser=new DOMParser();
					doc=parser.parseFromString(text,'text/xml');
				}

			return doc;
		},

		/**
		 * 세션에러 처리
		 * @param response
		 * @returns {Boolean}
		 */
		isErrorChk : function(response){

			var xmlDoc = exsoft.util.error.stringToXml(response.responseText);

			if(xmlDoc != null && xmlDoc.getElementsByTagName("RESPONSES").length != 0) {

				var kk = xmlDoc.getElementsByTagName("RESULT")[0].childNodes[0].nodeValue;
				if(kk == "common.session.error"){
					alert(xmlDoc.getElementsByTagName("MESSAGE")[0].childNodes[0].nodeValue);
					top.location.href = "/";
		    		return false;
				}else {
					alert(xmlDoc.getElementsByTagName("MESSAGE")[0].childNodes[0].nodeValue);
					return false;
				}
			}
		},

		/**
		 * 세션에러 처리
		 * @param response
		 * @returns {Boolean}
		 */
		isErrorPopChk : function(response){

			var xmlDoc = exsoft.util.error.stringToXml(response.responseText);

			if(xmlDoc != null && xmlDoc.getElementsByTagName("RESPONSES").length != 0) {

				var kk = xmlDoc.getElementsByTagName("RESULT")[0].childNodes[0].nodeValue;

				if(kk == "common.session.error"){
					alert(xmlDoc.getElementsByTagName("MESSAGE")[0].childNodes[0].nodeValue);
					exsoft.util.layout.windowClose();
		    		return false;
				}else {
					alert(xmlDoc.getElementsByTagName("MESSAGE")[0].childNodes[0].nodeValue);
					return false;
				}
			}
		},

}; // exsoft.util.error end...

exsoft.util.error.prototype = {

}; // exsoft.util.error.prototype end...

/***********************************************
 * table
 **********************************************/
/**
 * table 공통 함수
 *
 * @namespace : exsoft.util.table
 *
 */
exsoft.util.table = {

		/**
		 * AclItem 기본권한 영문 -> 한글
		 */
		getAclItemTitle : function(en_title) {
			switch(en_title) {
				case "DELETE"	: return "삭제";
				case "UPDATE"	: return "수정";
				case "READ" 	: return "조회";
				case "BROWSE"	: return "목록";
				case "NONE"		: return "없음";
			}
		},

		getAclImg : function(aclItem) {
			switch(aclItem) {
				case "DELETE"	: return "d";
				case "UPDATE"	: return "u";
				case "READ" 	: return "r";
				case "BROWSE"	: return "r";
				case "NONE"		: return "r";
			}
		},
		getAclCheckerImg : function(aclItem) {
			if (aclItem == "T") return "";
			else return "no";
		},

		/**
		 * Table : <thead> 제외한 Row를 모두 삭제한다
		 */
		tableRemoveAll : function(tableId) {
			$(exsoft.util.common.getIdFormat(tableId) + " tbody").empty();
		},

		/**
		 * Table : Row를 추가한다
		 */
		tableAddRow : function(tableId) {
			var trModel = $(exsoft.util.common.getIdFormat(tableId) + " thead tr:eq(0)").clone();

			// 첫행이 숨김처리 되있을 경우 추가한 Row를 보이도록 처리함

			if (!trModel.is(":visible")) {
				trModel.css("display","");
			}

			// th일 경우 td로 변환
			trModel.children("th").replaceWith(function(i, html) {
				return "<td>" + html + "</td>";
			});

			// tr, td css remove
			trModel.removeClass();
			trModel.find("td").removeClass();

			$(exsoft.util.common.getIdFormat(tableId)+' tbody').append(trModel);
		},

		/**
		 * Table : 데이터를 테이블 목록에 출력한다
		 * @param tableId : data를 표시 할 table의 ID
		 * @param data - 테이블 목록
		 * @param isCheckBox - 체크박스 표시 여부
		 * @param isRemoveAll - body의 전체 항목을 지우는 옵션
		 *
		 */
		tablePrintList : function(tableId, data, isCheckBox, isRemoveAll) {
			// 모든 Row를 삭제함
			if(isRemoveAll)
				exsoft.util.table.tableRemoveAll(tableId);

			// 출력할 데이터의 Row를 추가하고 값을 설정한다.
			$(data).each(function(index){
				exsoft.util.table.tableAddRow(tableId);

				var tdCols = $(exsoft.util.common.getIdFormat(tableId) + " thead tr:eq(0) th");
				$(tdCols).each(function(tdIndex) {
					var colName = $(this).attr("name");
					if(colName != null) {
						if(isCheckBox && tdIndex == 0){
							//<input type="checkbox" id="\'{0}\'" name="\'{0}\'">
							$(exsoft.util.common.getIdFormat(tableId) + " tr:last td:eq(" + tdIndex + ")").html('<input type="checkbox" id="{0}" name="{1}">'.format(tableId+'_'+data[index][colName], tableId+'_'+colName));
						} else {
							$(exsoft.util.common.getIdFormat(tableId) + " tr:last td:eq(" + tdIndex + ")").html(data[index][colName]);
						}

					}
				});
			});
		},

		/**
		 * Table : 데이터를 테이블 목록에 출력한다
		 * @param tableId : data를 표시 할 table의 ID
		 * @param data - 테이블 목록
		 * @param isCheckBox - 체크박스 표시 여부
		 * @param isRemoveAll - body의 전체 항목을 지우는 옵션
		 *
		 */
		tablePrintMainList : function(tableId, data, isCheckBox, isRemoveAll,isExtension,contextRoot) {
			// 모든 Row를 삭제함
			if(isRemoveAll)
				exsoft.util.table.tableRemoveAll(tableId);

			// 출력할 데이터의 Row를 추가하고 값을 설정한다.
			$(data).each(function(index){
				exsoft.util.table.tableAddRow(tableId);

				var tdCols = $(exsoft.util.common.getIdFormat(tableId) + " thead tr:eq(0) th");
				$(tdCols).each(function(tdIndex) {
					var colName = $(this).attr("name");
					if(colName != null) {
						if(isCheckBox && tdIndex == 0){
							//<input type="checkbox" id="\'{0}\'" name="\'{0}\'">
							$(exsoft.util.common.getIdFormat(tableId) + " tr:last td:eq(" + tdIndex + ")").html('<input type="checkbox" id="{0}" name="{1}">'.format(tableId+'_'+data[index][colName], tableId+'_'+colName));
						} else {
							if(colName == "doc_name_limit" &&  isExtension)	{
								var imgsrc = "<img src='"+contextRoot+data[index]['page_extension_img']+"'>";
								$(exsoft.util.common.getIdFormat(tableId) + " tr:last td:eq(" + tdIndex + ")").html(imgsrc + data[index][colName]);
							}else {
								$(exsoft.util.common.getIdFormat(tableId) + " tr:last td:eq(" + tdIndex + ")").html(data[index][colName]);
							}
						}

					}
				});
			});
		},

		/**
		 * Table : 데이터를 테이블 목록에 출력한다
		 * @param tableId : data를 표시 할 table의 ID
		 * @param data - 테이블 목록
		 * @param isCheckBox - 체크박스 표시 여부
		 * @param isRemoveAll - body의 전체 항목을 지우는 옵션
		 *
		 */
		tablePrintNoteList : function(tableId, data, isCheckBox, isRemoveAll) {
			// 모든 Row를 삭제함
			if(isRemoveAll)
				exsoft.util.table.tableRemoveAll(tableId);

			// 출력할 데이터의 Row를 추가하고 값을 설정한다.
			$(data).each(function(index){

				if(index == 11) return;

				exsoft.util.table.tableAddRow(tableId);

				var tdCols = $(exsoft.util.common.getIdFormat(tableId) + " thead tr:eq(0) th");
				$(tdCols).each(function(tdIndex) {
					var colName = $(this).attr("name");
					if(colName != null) {
						if(isCheckBox && tdIndex == 0){
							$(exsoft.util.common.getIdFormat(tableId) + " tr:last td:eq(" + tdIndex + ")").html('<input type="checkbox" id="{0}" name="{1}">'.format(tableId+'_'+data[index][colName], tableId+'_'+colName));
						} else {
							$(exsoft.util.common.getIdFormat(tableId) + " tr:last td:eq(" + tdIndex + ")").html(data[index][colName]);
						}

					}
				});
			});
		},

		/**
		 * Table : 문서에 대한 AclItem table list
		 * @param tableId : data를 표시 할 table의 ID
		 * @param aclItemList - 권한 접근자 목록
		 *
		 */
		tableDocumentAclItemPrintList : function(tableId, aclItemList) {
			var tableId = exsoft.util.common.getIdFormat(tableId);

			// 헤더를 제외한 모든 Row를 삭제함
			exsoft.util.table.tableRemoveAll(tableId);
			var rowSpanVal = aclItemList == null || aclItemList.length == 0 ? 2 : aclItemList.length+1;

			// thead와 tbody의 tr끼리는 rowspan을 할 수 없음

			/* 레이아웃 변경에 의한 주석처리
			$(tableId + " thead tr:eq(0)").addClass('hide');
			var trHead = $(tableId + " thead tr:eq(0)").clone();
			trHead.removeClass('hide');
			trHead.children('th:eq(0)').attr("rowspan", rowSpanVal);
			$(tableId+' tbody').append(trHead);
			 */

			$(aclItemList).each(function(index){
				var trModel = '<tr>';
				trModel += "<td>{0}</td>".format(this.accessor_name);
				trModel += "<td><img src='{0}/img/icon/prev_{1}.png' class='auth_grade'>{2}</td>".format(exsoft.contextRoot, exsoft.util.table.getAclImg(this.doc_default_acl), exsoft.util.table.getAclItemTitle(this.doc_default_acl));
				trModel += "<td><img src='{0}/img/icon/auth_{1}pass.png'></td>".format(exsoft.contextRoot, exsoft.util.table.getAclCheckerImg(this.doc_act_create));
				trModel += "<td><img src='{0}/img/icon/auth_{1}pass.png'></td>".format(exsoft.contextRoot, exsoft.util.table.getAclCheckerImg(this.doc_act_cancel_checkout));
				trModel += "<td><img src='{0}/img/icon/auth_{1}pass.png'></td>".format(exsoft.contextRoot, exsoft.util.table.getAclCheckerImg(this.doc_act_change_permission));
				trModel += '</tr>';
				$(tableId+' tbody').append(trModel);
			});

			//추가 접근자만 해당
			if(aclItemList == null || aclItemList.length == 0){
				//레이아웃 변경에 따른 주석처리
				//trHead.children('th:eq(0)').attr("rowspan", rowSpanVal);
				$(tableId+' tbody').append("<tr><td colspan='5'>데이타가 없습니다.</td></tr>");
			}else{

			}
		},

		/**
		 * Table : 폴더에 대한 AclItem table list
		 * @param tableId : data를 표시 할 table의 ID
		 * @param aclItemList - 권한 접근자 목록
		 *
		 */
		tableFolderAclItemPrintList : function(tableId, aclItemList) {
			var tableId = exsoft.util.common.getIdFormat(tableId);

			// 헤더를 제외한 모든 Row를 삭제함
			exsoft.util.table.tableRemoveAll(tableId);

			var tableStr = "";

			$(aclItemList).each(function(idx) {
				tableStr += "<tr>";
				tableStr += "<td>{0}</td>".format(this.accessor_name);
				tableStr += "<td><img src='{0}/img/icon/prev_{1}.png' class='auth_grade'>{2}</td>".format(exsoft.contextRoot, exsoft.util.table.getAclImg(this.fol_default_acl), exsoft.util.table.getAclItemTitle(this.fol_default_acl));
				tableStr += "<td><img src='{0}/img/icon/auth_{1}pass.png'></td>".format(exsoft.contextRoot, exsoft.util.table.getAclCheckerImg(this.fol_act_create));
				tableStr += "<td><img src='{0}/img/icon/auth_{1}pass.png'></td>".format(exsoft.contextRoot, exsoft.util.table.getAclCheckerImg(this.fol_act_change_permission));
				tableStr += "<td><img src='{0}/img/icon/prev_{1}.png' class='auth_grade'>{2}</td>".format(exsoft.contextRoot, exsoft.util.table.getAclImg(this.doc_default_acl), exsoft.util.table.getAclItemTitle(this.doc_default_acl));
				tableStr += "<td><img src='{0}/img/icon/auth_{1}pass.png'></td>".format(exsoft.contextRoot, exsoft.util.table.getAclCheckerImg(this.doc_act_create));
				tableStr += "<td><img src='{0}/img/icon/auth_{1}pass.png'></td>".format(exsoft.contextRoot, exsoft.util.table.getAclCheckerImg(this.doc_act_cancel_checkout));
				tableStr += "<td><img src='{0}/img/icon/auth_{1}pass.png'></td>".format(exsoft.contextRoot, exsoft.util.table.getAclCheckerImg(this.doc_act_change_permission));
				tableStr += "</tr>";
			});
			$(tableId + ' tbody').append(tableStr);
		},

		/**
		 * 확장문서 속성 표시 :: 기존 base.extendTypePrint
		 * @param tableId - 확장문서유형 속성을 표시할 table의 ID
		 * @param attrItemList - 확장문서유형 속성 목록
		 * @param type - 등록:C 수정:U 보기:V
		 */
		tableExtendTypeItemPrintList : function(tableId, attrItemList, type){
			var tableId = exsoft.util.common.getIdFormat(tableId);
			var buffer = "";
			var defaultVal = "";
			var splitVal = new Array();

			// 헤더를 제외한 모든 Row를 삭제함
			exsoft.util.table.tableRemoveAll(tableId);

			$(attrItemList).each(function(index){

				buffer += "<tr>";
				buffer += "<th>"+this.attr_name+"</th>";
				buffer += "<td colspan='3'>";

				// XR_ATTRITEM 이 존재하는 경우
				if(this.has_item == "T")	{
					// SELECT/RADIO/CHECK/INPUT
					if(this.display_type == "SELECT" && type !='V')	{
						buffer += "<select name='"+this.attr_id+"'>";
					}

					// XR_ATTRITEM 파싱처리
					$(this.item_list).each(function(idx){

						// 문서등록/수정/보기에 따른 기본값 변경처리
						if(type == "C")	{
							defaultVal = attrItemList[index].default_item_index;
						}else {
							defaultVal = attrItemList[index].attr_value;
						}

						if(type != 'V')	{
							if(attrItemList[index].display_type == "RADIO")	{
								if(this.item_index == defaultVal)	{
									buffer += "<input type='radio' name='"+this.attr_id+"' value='"+this.item_index+"' checked /><span class='radio'>" + this.item_name+"&nbsp</span>";
								}else {
									buffer += "<input type='radio' name='"+this.attr_id+"' value='"+this.item_index+"' /><span class='radio'>" + this.item_name+"&nbsp</span>";
								}
							}else if(attrItemList[index].display_type == "SELECT")	{
								if(this.item_index == defaultVal)	{
									buffer += "<option value='"+this.item_index+"' selected>"+this.item_name+"</option>";
								}else {
									buffer += "<option value='"+this.item_index+"'>"+this.item_name+"</option>";
								}
							}else if(attrItemList[index].display_type == "CHECK")	{
								if(type =="C")	{
									if(this.item_index == defaultVal)	{
										buffer += "<input type='checkbox'  name='"+this.attr_id+"' value='"+this.item_index+"' checked/><span class='checkbox'>"+this.item_name+"&nbsp</span>";
									}else {
										buffer += "<input type='checkbox'  name='"+this.attr_id+"' value='"+this.item_index+"'/><span class='checkbox'>"+this.item_name+"&nbsp</span>";
									}
								}
								else {
									if(defaultVal != -1 && defaultVal.indexOf(this.item_index) != -1) 	{
										buffer += "<input type='checkbox'  name='"+this.attr_id+"' value='"+this.item_index+"' checked/><span class='checkbox'>"+this.item_name+"&nbsp</span>";
									}else {
										buffer += "<input type='checkbox'  name='"+this.attr_id+"' value='"+this.item_index+"'/><span class='checkbox'>"+this.item_name+"&nbsp</span>";
									}
								}
							}
						}else {
							if(this.item_index == defaultVal)	{
								buffer += "<input type='text' name='"+this.item_index+"' value='"+this.item_name+"'  size='60' maxlength='100' readonly/>";
							}
						}

						defaultVal = "";
						splitVal = new Array();
					});

					if(attrItemList[index].display_type == "SELECT" && type !='V')	{
						buffer += "</select>";
					}

				}else {
					// INPUT
					if(type != 'V')	{
						buffer += "<input type='text' name='"+this.attr_id+"' value='"+this.attr_value+"'  size='60' maxlength='100'/>";
					}else {
						buffer += "<input type='text' name='"+this.attr_id+"' value='"+this.attr_value+"'  size='60' maxlength='100' readonly/>";
					}

				}

				buffer += "</td>";
				buffer += "</tr>";

			});

			$(tableId+' tbody').append(buffer);
		},

};  // exsoft.util.table end...

exsoft.util.table.prototype = {

}; // exsoft.util.table.prototype end...

/***********************************************
 * websocket
 **********************************************/
/**
 * agent를 통한 통신
 *
 * @namespace : exsoft.util.websocket
 *
 */
exsoft.util.websocket = {

		connect : function(userId, callback){
			try{
				socket = new WebSocket(exsoft.util.websocket.prototype.host);
				socket.onopen = function(){
					socket.send('0'+ '|' + userId);
					setTimeout(exsoft.util.websocket.keepAlive, 5000);
				};

				socket.onmessage = function(msg){

					var str = msg.data;
					var tokens = str.split('|');
					if(tokens[0] == "1"){
						if (callback != null) {
							callback(tokens[1],tokens[2]); // action, result
						}
					};
				};

				socket.onclose = function(){};

			} catch(exception){
				alert('Error'+exception);
			}
		},

		keepAlive : function() {
			if (socket && socket.readyState==1) {
				socket.send('0'+ '|PING');
				setTimeout(keepAlive, 5000);
			}
		},

		addToDownload : function(docId,pageId,fileName) {
			// SERVICEID | CMD | DOCID1 * PAGEID1 * FILENAME1 ^ DOCID2 * PAGEID2 * FILENAME2
			socket.send('1' + '|ADD_TO_DOWNLOAD' + "|" + docId +"*" + pageId + "*" + fileName );
		},

		checkout : function (docId,pageId,fileName,versionNo,isCurrent,ownerId) {
			// SERVICEID | CMD | DOCID ^ PAGEID ^ FILENAME ^ VERSION ^ IS_CURRENT ^ OWNERID
			socket.send('1' + '|CHECKOUT' + "|" + docId + "^" + pageId + "^" + fileName + "^" + versionNo + "^" + isCurrent +"^" + ownerId);
		},

		checkUser : function() {
			// SERVICEID | CMD | DOCID1 * PAGEID1 * FILENAME1 ^ DOCID2 * PAGEID2 * FILENAME2
			 socket.send('1' + '|CHECK_USER' + "|DUMMY");
		},

		clearDownload : function() {
			// SERVICEID | CMD | DOCID1 * PAGEID1 * FILENAME1 ^ DOCID2 * PAGEID2 * FILENAME2
			 socket.send('1' + '|CLEAR_DOWNLOAD' + "|DUMMY" );
		},

		doDownload : function() {
			// SERVICEID | CMD | DOCID1 * PAGEID1 * FILENAME1 ^ DOCID2 * PAGEID2 * FILENAME2
			 socket.send('1' + '|DO_DOWNLOAD' + "|DUMMY" );
		},

		view : function(docId,pageId,fileName) {
			// SERVICEID | CMD | DOCID ^ PAGEID ^ FILENAME ^ VERSION ^ IS_CURRENT ^ OWNERID
			socket.send('1' + '|VIEW' + "|" + docId + "^" + pageId +"^" + fileName) ;
		},

}; // exsoft.util.websocket end...

exsoft.util.websocket.prototype = {
		host : 'ws://localhost:23232/EdmHelper',  // 절대 수정하지 말 것. agent가 설정한 url 임
}; // exsoft.util.websocket.prototype end...
