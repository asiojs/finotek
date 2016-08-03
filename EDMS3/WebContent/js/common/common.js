/**
 * EDMS HTML5 Global Common
 */
if( typeof exsoft == 'undefined') {
	window.exsoft = {}; 
}
	
// context path 
exsoft.contextRoot = contextRoot;
exsoft.notyDivId = null;

$.extend(exsoft, {
	/**
	 * Global 객체 정의(namespace pattern)
	 * @param name : namespace 명
	 */
	namespace : function ( name ) {
		var names = name.split(".");
		var topClass = exsoft;
		var i=0;
		
		for ( i=( names[0]=='exsoft'? 1 : 0); i<names.length; i++ ) {
			topClass[ names[i]] = topClass[names[i]] || {}; // typeof short-hand 방식
			topClass = topClass[names[i]];
		}
	},
	
	/**
	 * prototype만 상속 받을 시 사용함
	 * @param parent : 상속 받을 namespace명
	 * @param child : 상속 받은 객체에 추가 정의
	 * 
	 */
	prototypeExtend : function( parent, child) {
		var fn = function() {}
		fn.prototype = $.extend( true, {}, parent.prototype, child);

		var sub = function(){};
		sub.prototype = new fn();
		//sub.prototype.constructor = sub;
		//sub.superClass = parent.prototype;

		//prototype으로 정의되지 않은 상속은 DOM에 매달리지 않으므로 직접 붙여준다.
		var instance =  new sub();
		instance.prototype = sub.prototype;
		instance.prototype.superClass = parent.prototype;
		return instance;
	}
});

exsoft.namespace('user');
exsoft.namespace("document");
exsoft.namespace("process");
exsoft.namespace("common.bind");
exsoft.namespace("common.file");
/***********************************************
 * loginUser
 **********************************************/
/**
 * user 관련 util
 *
 * @namespace : exsoft.user
 *
 */
exsoft.user = {
		user_id : null,
		user_name : null,
		acl_menu_part : null,
		manage_group_id : null,
		manage_group_nm : null,
		user_email : null,
		
}; // exsoft.util.error end...

/***********************************************
 * document
 **********************************************/
/**
 * 문서 common
 * namespace로 관리
 */

exsoft.document = {
		//binder : new DataBinder("#documentWrite"),

    	commDocBinder : null,
		wFileUploadJsonArr : new Array(),
		actionType : null,
		init : {
			//1 문서 기본
			docDetailInit : function(docId){

				exsoft.document.prototype.gDocId = docId;
        		//exsoft.util.common.formClear("frm_docListLayer_detail_search");
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({doc_id:docId}, exsoft.contextRoot+"/document/documentDetail.do", "select", 
        				function(data,e) {

        					exsoft.document.event.printPageList(data);//첨부파일
        					exsoft.document.event.printDocumentVO(data);//기본정보
        					
        					// 권한 Setting
        					$("#docDetailAclName").html(data.aclDetail.acl_name); //권한
        					
        					exsoft.util.table.tableDocumentAclItemPrintList('detail_docAclItemList', data.aclItemList);
        					exsoft.util.table.tableDocumentAclItemPrintList('detail_docExAclItemList', data.aclExItemList);
        					
        					exsoft.document.event.printRefDoc(data);//관련문서
        					//의견 총수
        					$(".opinion_cnt").text(data.commentCnt);
        					
        			});	
        		

        	},
        	//1 문서 기본
			docWriteInit : function(){

				
				var folder_id = $("#documentWrite input[name=folder_id]").val(); 
				var folder_path = $("#documentWrite input[name=folder_path]").val();
				
				//메인에서 들어올 경우
				if(folder_id == null || folder_id == ""){
					exsoft.document.commDocBinder = new DataBinder("#documentWrite");
					exsoft.document.commDocBinder.set("actionType", Constant.ACTION_CREATE);
					exsoft.document.actionType = 'C';

					exsoft.document.init.initDdslick();      
	        		$("#documentWrite").validation.clearShadowBox();
	        		
					// 1.폴더에 문서등록 권한이 있는지 체크한다.

					//exsoft.user.acl_menu_part = "${acl_menu_part}";
					//alert(exsoft.user.acl_menu_part);
					// 파일 관련
	        		exsoft.common.file.init.initSettings('documentfileuploader', exsoft.document.callback.fileupload);
	        		
	        	
					exsoft.document.commDocBinder.set("map_id","MYDEPT");
				}else{
					
					var jsonObject = { "folder_id":folder_id,"folder_path":folder_path};
				
					exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject,exsoft.contextRoot+"/document/docCommonRegist.do", "docRegistForm", 
					
						function(data,e) {
						
							if(data.result == "true")	{
								// 2.문서등록 폼 초기화 처리
								//docRegistFrm();

								// 3. 공통처리 - 기본문서유형선택 및 map_id
								// 기본폴더ID/MAP_ID :: 폴더선택 화면에서 변경시 폴더ID는 변경됨
								exsoft.document.commDocBinder.set("folder_id",folder_id);
								exsoft.document.commDocBinder.set("folder_path",folder_path);
								exsoft.document.commDocBinder.set("map_id",data.folderVO.map_id);
								
								// 4. 문서유형 선택유무 제어
								wDocType = data.defaultType;		// 폴더선택화면으로 넘겨줄 기본문서유형
								exsoft.document.commDocBinder.set("doc_type",data.defaultType);
																
								if(data.isChangeType == "FALSE") {
									$("#documentWrite select[name=doc_type]").prop("disabled",true);
									exsoft.document.commDocBinder.set("isChangeType","FALSE");
									
								}else {
									$("#documentWrite select[name=doc_type]").prop("disabled",false);
									exsoft.document.commDocBinder.set("isChangeType","TRUE");
								}
								exsoft.document.commDocBinder.set("folderIsType",data.is_type);
								
								// 개인함의 경우 HIDDEN처리 
								// sercurityView - 보안등급/조회등급 || aclLockView - 권한변경유무 || 
								// keywordView - 키워드 || multiFolderView - 다차원분류 || workSpaceView - 권한/관련문서
								//var mypageHide = new Array('#sercurityView', '#aclLockView', '#isShareView', '#keywordView', '#multiFolderView','#workSpaceView','#authorView');
								if(data.folderVO.map_id == "MYPAGE")	{
									// 보존년한 영구 기본선택					
									$("#documentWrit select[name=preservation_year]").val(0);
									
									// 확장문서유형		
									exsoft.documenet.event.setExtendTypeAttrItem(data.defaultType);
								}else {	
									// 권한 :: 권한속성 아이템 
									$("#wAclName").html(data.aclDetail.acl_name);

									//alert(exsoft.document.commDocBinder.set("acl_id",data.aclDetail.acl_name));
									exsoft.util.table.tableDocumentAclItemPrintList('docmentWrite_acl',data.aclItemList);
									exsoft.util.table.tableDocumentAclItemPrintList('docmentWrite_extAcl',data.aclExItemList);

									// 확장문서유형
									exsoft.documenet.event.setExtendTypeAttrItem(data.defaultType);
									
									// 보존년한 5년 기본선택
									$("#documentWrite select[name=preservation_year]").val(5);

								}
								
								// upload 상태값 초기화
								exsoft.common.file.prototype.wUploadObj.fCounter = 0;
								exsoft.common.file.prototype.wUploadObj.sCounter = 0;
								exsoft.common.file.prototype.wUploadObj.tCounter = 0;
								exsoft.common.file.prototype.wUploadObj.upCounter = 0;
								
								
							}else {
								jAlert(data.message);
							}
					});
					
				}
				
        	},
        	
        	docUpdateInit : function(){

				exsoft.document.commDocBinder = new DataBinder("#documentUpdate");
				exsoft.document.commDocBinder.set("actionType", Constant.ACTION_CREATE);
				exsoft.document.actionType = 'C';

        		// 문서유형 select-box
				//exsoft.document.init.initDdslick();       		
        		$("#documentUpdate").validation.clearShadowBox();
        		// 파일 관련
        		exsoft.common.file.init.initSettings('documentupdatefileuploader', exsoft.document.callback.fileupload);
        		

        	},
        	initDdslick : function(type){
    			//검색 selectbox		
    			exsoft.util.common.ddslick('#register_docType', 'srch_type1', 'doc_type', 85, function(divId, selectedData){
    				exsoft.document.commDocBinder.set("doc_type", selectedData.selectedData.value);
    				//문서유형에 맞는 확장 속성을 표시 한다.
    				// 1.폴더에 문서등록 권한이 있는지 체크한다.
    				exsoft.document.event.setExtendTypeAttrItem(selectedData.selectedData.value);    				
    			});
    			// 보존년한 selectbox
    			exsoft.util.common.ddslick('#register_preservationyear', 'srch_type1', 'preservation_year', 58, function(divId, selectedData){
    				
    				exsoft.document.commDocBinder.set("preservation_year", selectedData.selectedData.value);	
    			});	
    			// 조회등급 selectbox
    			exsoft.util.common.ddslick('#register_accessgrade', 'srch_type1', 'access_grade', 58, function(divId, selectedData){
    				
    				exsoft.document.commDocBinder.set("access_grade", selectedData.selectedData.value);
    			});	
    			
    		},
    		
    	
        	
		},
		
		open : {
			// 메일 수신자 선택
			userSelectUrl : "/document/reciverUserSelect.do",
			userSelectTarget : "mailReciverUserFrm",
			
			// 쪽지보내기 사용자선택
			docVersionDetailUrl : "/document/docVersionDetail.do",
			docVersionDetailTarget : "docVersionDetailFrm",

			// 메일수신자 선택 - 창열기
			reciverDetail : function() {
				this.contextRoot = exsoft.contextRoot;
				this.openWindow(this.userSelectTarget,700,630);
				this.formUserInit(document.mailReciverUserFrm,this.contextRoot+this.userSelectUrl,this.userSelectTarget);
			},
			// 사용자 환경설정 새창 CALL
			versionDetail : function(docId) {
				this.contextRoot = exsoft.contextRoot;
				this.openWindow(this.docVersionDetailTarget,680,630);
				this.formInit(document.docVersionDetailFrm,this.contextRoot+this.docVersionDetailUrl,docId,this.docVersionDetailTarget);
			},
			// 새창 띄우기
			openWindow : function(targetName,width,height) {
				var win= "";
				win = window.open("",targetName,"width="+width+", height="+height+", toolbar=no, menubar=no, scrollbars=no, resizable=no" );
				win.focus();			// 새창의 경우 항상 맨위로				
			},
		
			formInit : function(formName,url,docId,targetName)	{	

				var frm = formName;
				frm.action = url;
				frm.method = "post";
				frm.target = targetName;
				frm.docId.value = docId;
				frm.submit();
			},

			formUserInit : function(formName,url,targetName)	{	

				var frm = formName;
				frm.action = url;
				frm.method = "post";
				frm.target = targetName;
				frm.submit();
			},
			
		},
		
		layer : {
			// 문서상세조회 Layer OPEN
			docCommonFrm : function(wrapperClass,layerClass,docId) {
				exsoft.util.layout.divLayerOpen(wrapperClass,layerClass);
				
				exsoft.document.init.docDetailInit(docId);
			},
			// 문서등록 Layer OPEN
			docWriteCommonFrm : function(wrapperClass,layerClass) {
				exsoft.util.layout.divLayerOpen(wrapperClass,layerClass);
				exsoft.document.init.docWriteInit();
			},
			// 문서수정 Layer OPEN
			docUpdateCommonFrm : function(wrapperClass,layerClass) {
				exsoft.util.layout.divLayerOpen(wrapperClass,layerClass);
				exsoft.document.init.docUpdateInit();
			},
		},
		
		event : {
			
			// 폴더찾기 호출
			selectFolderFind : function() {
				selectSingleFolderWindow.init(exsoft.document.callback.folderFind);
				
			},

			// 관련문서 창 호출
			selectRelDocWindow : function() {
				selectrelativeDocWindow.init(exsoft.document.callback.relDocWindow);
				
			},
			
			// 문서 권한 변경
        	changeDocumentAcl : function() {
        		selectAclWindow.initDocument(""/*AclId*/, Constant.ACL.TYPE_DOC, ""/*docId*/,exsoft.document.callback.selectAcl);
        	},
        	
			// 문서 유형에 따른 확장속성 표시  click.ddslick
        	setExtendTypeAttrItem : function(selectValue){
        		var jsonObject = {"type_id":selectValue,"is_extended":"T"}; 	
				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject,exsoft.contextRoot+'/type/attrList.do', '#documentWrite_docAttrView', function(data, param){
					exsoft.util.table.tableExtendTypeItemPrintList('documentWrite_docAttrView', data.list, exsoft.document.actionType);
					 if(data.records != 0){
						 $(param).removeClass('hide');
						 exsoft.document.commDocBinder.set("is_extended", 'T');
						 
						// table에 select box가 존재하면 ddslick을 해준다.
	    				var $extendType = $('#processWrite_docAttrView tbody').find('input, select');
	    				$($extendType).each(function(idx){
	    					var name = $(this).attr('name');
	    					
	    					if($(this).is('select')){
	    						$(this).attr('id', name);
	    						$(this).attr('data-select', 'true');
	    						exsoft.util.common.ddslick(name,'srch_type1',name,80, function(divId, selectValue){
	    							exsoft.document.commDocBinder.set(name, selectValue);
	    						});
	    					}else{
	    						$(this).attr('data-bind', name);
	    					}		    					
	    				});
	    				exsoft.document.commDocBinder.bindingElement(); // data-bind 전체 bind
						 
					 }else{
						 $(param).addClass('hide');
						 exsoft.document.commDocBinder.set("is_extended", 'F');
					 }
					 
				}); // 확장속성 set
        	},
			
			// 다차원분류 선택 
			registDocSelectMultiFolderFind : function() {
				var doc_type = exsoft.document.commDocBinder.get("folderIsType");				
				var map_id = exsoft.document.commDocBinder.get("map_id");
				//var doc_type = document.documentWrite.folderIsType.value;
				//var map_id = document.documentWrite.map_id.value;
				selectMultiFolderWindow.init(exsoft.document.event.registDocMultiFolderFind, map_id, "WORK_MYDEPT", true, doc_type);
			},

			// 다차원 분류 선택 
			registDocMultiFolderFind :  function(obj)	{
				//$("#multiFolder").empty(); // 다차원분류 선택 폴더 초기화
				exsoft.document.event.multiFolderAdd(obj,'documentWrite',document.documentWrite.folder_id.value,'multiFolder');		
			},
			
			/**
			 * 문서등록/수정 다차원 분류 추가 
			 * @param folderArr - 다차원분류 선택 폴더 리스트 Array
			 * @param divIds - 다차원분류 ID
			 * @param defaultFolderValue - 기본폴더ID
			 * @param formId - 폼ID
			 */
			multiFolderAdd : function(obj,formId,defaultFolderValue,divIds) {
				var buffer = "";
				buffer += "<ul>";
				$.each(obj, function(index, result) {
					
					// 기본폴더가 아니고 이미 추가된 다차원 분류가 아닌 경우에만 입력처리한다.
					if(!exsoft.document.event.chkMultiFolderList(formId,'multi_folder',this.id) && 
							this.id != defaultFolderValue )	{
										
						var divNames = exsoft.util.common.uniqueId();
						
						buffer += "<li>";
						buffer += "<input type='hidden' class='' name='multi_folder' value='"+this.id+"'>";
						buffer += "<input type='text' name='multi_folder_path' readonly value='"+this.fullPath.join("/")+"'>";
						//buffer += "<span class='x_button' onclick=\"javascript:base.removeDivIds('"+divNames+"');\"></span><br />";
						buffer += "</li>";
					}
					
				});

				buffer += "</ul>";
				$(".doc_classification_list").removeClass("hide");
				$('#'+divIds).append(buffer);
			},
			/**
			 * 문서 등록/수정 다차원 분류 중복 체크
			 * @param formId
			 * @param inputName
			 * @param value
			 * @returns {Boolean}
			 */
			chkMultiFolderList : function(formId,inputName,value){

				var ret = false;
				
				$("#"+formId + " "+ " input[name='"+inputName+"']").each(function() {			
					if(this.value == value)	{
						ret = true;
						return false;		// break
					}			
				});
				
				return ret;				
			},
			
			//1-1. 첨부파일 출력
        	printPageList : function(data) {
        		//데이터 초기화
        		$( "#detail_pageList").empty();
        		
        		var pageList = data.pageList;	
        		//var tableId = "#detail_pageList";
        		var buffer = "";
        		exsoft.document.prototype.gPageList = data.pageList;					// URL 복사
        		//base.tableRemoveAll(tableId);											// 기존 목록을 삭제
        		//base.showHideInit('vAttachShowHide','vAttachView');			// 첨부파일 기본 숨김	
        		$(".attach_cnt").html(pageList.length+"개");				// 첨부 파일 갯수를 표시함	
        		
        		//$("#attach_cnt").html("({0})".format(pageList.length));
        		buffer += "<ul>";
        		
        		$(pageList).each(function(index) {			// 첨부 파일 갯수만큼 루프
        			
        			//화장자별 아이콘 변경        			
        			var imgext = exsoft.document.ui.imgExtension(pageList[index].page_name);
        			
        			buffer += "<li class='attach_docs_list'>";
        			//buffer += "<input type='checkbox' name='downChk' value='{0}'></input>'.format("+pageList[index].page_id+")";		// 번호		
        			buffer += "<a href='#'><img src='"+exsoft.contextRoot+"/img/icon/"+imgext+"' alt='' title=''>"+pageList[index].page_name+"</a>";
        			buffer += "<div class='download_detail'>";
        			buffer += "<span class='download_filesize'>"+pageList[index].fsize+"</span>";
        			buffer += "<a href='#' class='download'>";
        			buffer += "<img src='"+exsoft.contextRoot+"/img/icon/attach_download1.png' alt='' title=''>";
        			buffer += "</a></div>";
	        		buffer += "</li>";

        		});

        		buffer += "</ul>";
        		$( "#detail_pageList").append(buffer);
        	},
        	
			//조회수(xr_document.read_cnt)update
        	updateReadCount : function(readcnt) {
        		var cnt = parseInt(readcnt) + 1;
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({doc_id:exsoft.document.prototype.gDocId,readcnt:cnt}, exsoft.contextRoot+"/document/doReadCountUpdate.do", "comment",
	            		function(data, e) {
		        			if(data.result == "true")	{
								
							}else {
								jAlert(data.message, "확인", 0);
							}	
	        		});
        	},
        		
        		
        	//1-2 문서 기본정보 출력
        	printDocumentVO : function(data) {
        		//데이터 초기화
        		$('#docDetailBasicInfo').empty();
        		var docVO = data.documentVO;
        		var buffer = "";
        		
        		// 다차원분류 초기화
        		$("#multiFolder").empty();
        		// 개인문서함 > 다차원분류 tr hide
        		/*var mypageHide = new Array('#detail_multiLink_view');	
        		if(docVO.map_id == "MYPAGE"){
        			base.mypageHideShow(mypageHide,false);
        		} else {
        			base.mypageHideShow(mypageHide,true);
        		}	
        		*/
        		//조회수 갱신
        		exsoft.document.event.updateReadCount(docVO.read_cnt);
        		
        		//첨부파일 총량 
        		$(".attach_size").html("("+exsoft.util.common.bytesToSize(docVO.page_total,1)+")");
        		
        		// 전역변수 설정
        		exsoft.document.prototype.getDocVO = docVO;
        		exsoft.document.prototype.gRootId = docVO.root_id == "" ? docVO.doc_id : docVO.root_id;
        		exsoft.document.prototype.gAclLevel = docVO.acl_level == "" ? "NONE" : docVO.acl_level;
        		exsoft.document.prototype.gAcl_checkoutCancel = docVO.acl_checkoutCancel;
        		exsoft.document.prototype.gFolderPath = data.folderPath;			// URL 복사

        		
        		buffer += "<tr><th>문서명</th><td colspan='3'>"+docVO.doc_name+"</td></tr>";
        		buffer += "<tr><th>기본폴더</th><td colspan='3'>"+data.folderPath+"</td></tr>";
        		buffer += "<tr><th>문서유형</th><td>"+docVO.type_name+"</td>";
        		buffer += "<th>보존연한</th><td>"+docVO.preservation_year == "0" ? "영구" : docVO.preservation_year + "년</td></tr>";        		

        		buffer += "<tr><th>보안등급</th><td>"+exsoft.util.common.findCodeName(data.securityList,docVO.security_level)+"</td>";
        		buffer += "<th>조회등급</th><td>"+exsoft.util.common.findCodeName(data.positionList,docVO.access_grade)+ "</td></tr>";
        		//buffer += "<tr><th>보안등급</th><td>"+docVO.security_level+"</td>";
        		//buffer += "<th>조회등급</th><td>"+docVO.access_grade+ "</td></tr>";

        		buffer += "<tr><th>등록자(소유자)</th><td>"+docVO.creator_name + " [" + docVO.owner_name + "]</td>";
        		buffer += "<th>등록일</th><td>"+docVO.create_date+ "</td></tr>";
        		buffer += "<tr><th>수정자</th><td>"+docVO.updater_name + "</td>";
        		buffer += "<th>수정일</th><td>"+docVO.update_date+ "</td></tr>";
			
        	
        		if(docVO.map_id != "MYPAGE") {
        			if(data.multiFolderList.length > 0){
        				buffer += "<tr><th>다차원 분류</th><td colspan='3'>"+docVO.doc_name+"</td></tr>";
        				for(var m=0; m < data.multiFolderList.length; m++) {
        					//var divNames = exsoft.util.common.uniqueId();
        					buffer += data.multiFolderList[m].folder_path;
        				}
        				
        				buffer += "</td></tr>";
        				
        				//$('#detail_multiLink').append(buffer);
        			}	
        		} 

        		buffer += "<tr><th>키워드</th><td colspan='3'>"+docVO.keyword+"</td></tr>";
        		//$("#detail_keyword").html(docVO.keyword);
        		
        		// DaumEditor View mode
        		buffer += "<tr><th>설명</th><td colspan='3'>"+docVO.doc_description.replace('&lt;','<').replace('& lt;','<').replace('&gt;', '>').replace('& gt;', '>')+"</td></tr>";
        		$('#docDetailBasicInfo').append(buffer);
        		
        		//$("#vtemp_content").html(docVO.doc_description.replace('&lt;','<').replace('& lt;','<').replace('&gt;', '>').replace('& gt;', '>'));
        		//$('#vIframe_editor').attr("src","${contextRoot}/editor_7.4.5/doc_view.jsp");				

        		$("#detail_doc_version").html("Ver " + docVO.version_no);
        		if (docVO.is_locked != "T") {
        			$("#btn_detail_checkout, #detail_docLockIcon").hide();
        		} else {
        			$("#btn_detail_checkout, #detail_docLockIcon").show();
        			$("#lockerInfo").text("반출자 :" + docVO.lock_owner_name);
        			$("#lockerDate").text("반출일시 :" + docVO.lock_date );
        		}
        	},
        	
        	//1-3. 관련 문서 출력
        	printRefDoc : function(data) {
        		//데이터 초기화
        		$('#docDetailRefInfo').empty();
        		var refDocList = data.refDocumentList;	

        		//alert(refDocList.size());
        		var buffer = "";
        		// 데이터가 없을 경우 관련문서를 표시하지 않는다
        		if(refDocList.length == 0 ){
        			$(".doc_detail_relative").addClass("hide");
        		}else{
        		
	        		buffer += "<ul>";
	        		//테스트 해보기
	        		$(refDocList).each(function(index) {		// 관련 문서 갯수만큼 루프
	        			//$(tableId + " tr:last td:eq(0)").html("<a href=\"javascript:initDocumentViewWindow('{0}');\">{1}</a>".format(this.doc_id, this.doc_name));		// 제목	
	        			//$(tableId + " tr:last td:eq(1)").html(this.creator_name);				// 등록자		
	        			//$(tableId + " tr:last td:eq(2)").html(this.create_date);				// 등록일
		        		buffer += "<li class='relative_docs_list'>";
		        		buffer += "<input type='checkbox' name='' class='relative_docs_checkbox' value=''>";
		        		//화장자별 아이콘 변경
	        			var imgext = exsoft.document.ui.imgExtension(this.doc_name);
		        		buffer += "<a href='#'><img src=" + exsoft.contextRoot +"/img/icon/"+imgext+"' alt='' title=''></a>";
		        		buffer += "<div class='download_detail'>";
		        		buffer += "<span class='download_filesize'></span>";
		        		buffer += "<a href='#' class='download'>";
		        		buffer += "<img src=" + exsoft.contextRoot +"/img/icon/attach_download1.png' alt='' title=''></a>";
		        		buffer += "<a href='#' class='contextMenu'><img src=" + exsoft.contextRoot +"/img/icon/attach_context.png' alt='' title=''></a>";
		        		buffer += "<div class='relative_download_context'>";
		        		buffer += "<ul>";
		        		buffer += "<li><a href='#' class='modify_file'><span class='left'></span><span class='right'>파일수정</span></a></li>";
		        		buffer += "<li><a href='#' class='save_modified'><span class='left'></span><span class='right'>수정파일저장</span></a></li>";
		        		buffer += "<li><a href='#' class='open_modified'><span class='left'></span><span class='right'>수정파일열기</span></a></li>";
		        		buffer += "<li><a href='#' class='cancel_modify'><span class='left'></span><span class='right'>수정취소</span></a></li>";
		        		buffer += "</ul>";
		        		buffer += "</div>";
		        		buffer += "</div>";
		        		buffer += "</li>";
		        		
	        		});
	
	        		buffer += "</ul>";
	        		$('#docRelativeTotal').html(refDocList.length);
	        		$('#docDetailRefInfo').append(buffer);
        		
        		}
        	},
        	
        	// 2. 문서의 모든 버전을 가져오고 화면에 표시한다
        	getDocumentVersionList : function() {
        		// 파라미터 추가
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({doc_id:exsoft.document.prototype.gDocId,table_nm:"XR_DOCUMENT"}, exsoft.contextRoot+"/document/documentVersionList.do", "version",
        			function(data, e) {
        			
        			exsoft.document.event.printVersionList(data);
        		});
        	},
        	
        	//2-1. 버전 목록 출력
        	printVersionList : function(data) {
        		//데이터 초기화
        		$("#detaildocVersionList").empty();
				$("#docVerAttach").empty();
				
        		var versionList = data.docAllVersionList;
        		var buffer = "";
        		
        		$(versionList).each(function(index) {

        			if (versionList[index].is_current == "T") {
        				buffer += "<tr class='current'>";
        			}else{
        				buffer += "<tr>";
        			}
					//buffer += "<input type='hidden' id='docVersionid' value='"+versionList[index].doc_id+"'>";
        			buffer += "<td>"+versionList[index].version_no+"버전</td>";// 버전
        			buffer += "<td>"+versionList[index].doc_name+"</td>";// 문서명
        			buffer += "<td>"+versionList[index].creator_name+"</td>";// 등록자
        			buffer += "<td>"+versionList[index].create_date+"</td>";// 등록일자
        			buffer += "</tr>";

					buffer += "<tr><td colspan='4'>";
					buffer += "<div class='doc_ver_attach'>";
					buffer += "<div class='doc_ver_title'>";
					buffer += "<span>";
					buffer += "<strong>버전파일</strong> : <span class='ver_file_cnt'>"+versionList[index].pageList.length+"</span>";
					buffer += "</span>";
					buffer += "<div class='ver_btn_wrapper'>";
					buffer += "<button class='' onClick=\"javascript:exsoft.document.event.versionDelete('" +versionList[index].doc_id + "')\">버전삭제</button>";
					buffer += "<button class='' onClick=\"javascript:exsoft.document.open.versionDetail('" +versionList[index].doc_id + "')\" >상세조회</button>";
					buffer += "</div></div>";
					buffer += "<div class='doc_ver_wrapper' id='docVerAttach'>";
					buffer += "<ul>";

        			// 첨부파일이 있을경우        			
    				if (versionList[index].pageList != undefined && versionList[index].pageList.length > 0) {
    					
    					$(versionList[index].pageList).each(function(i){
						
    						buffer += "<li class='doc_ver_list'>";
    						buffer += "<a href='#'>";
    	        			var imgext = exsoft.document.ui.imgExtension(versionList[index].pageList[i].page_name);
    	        			buffer += "<img src='"+exsoft.contextRoot+"/img/icon/"+imgext+"' alt='' title=''>"+versionList[index].pageList[i].page_name+"</a>";
    	        			buffer += "<div class='download_detail'>";
    	        			buffer += "<span class='download_filesize'>"+exsoft.util.common.bytesToSize(versionList[index].pageList[i].page_size,1)+"</span>";
    	        			buffer += "<a href='#' class='download'>";
    	        			buffer += "<img src='"+exsoft.contextRoot+"/img/icon/attach_download1.png' alt='' title=''></a>";
    	        			buffer += "</div></li>";
    						
    					});
    					buffer +="</ul></div></div></td></tr>";
    					//$("#docVerAttach").append(str);
    				}
        		});
        		$("#detaildocVersionList").append(buffer);
        		//var imgext = exsoftLayoutFunc.ui.imgExtension(pageList[index].page_name);
    			
        	},
        	// 2-2. 특정 버전을 삭제한다
        	versionDelete : function(docId) {
        		if (docId == exsoft.document.prototype.gRootId) {
        			jAlert("기본 문서 버전은 삭제할 수 없습니다.");
        			return;
        		}

        		jConfirm('선택한 버전을 삭제하시겠습니까?', 'Confirm', function(r) {
        	        if (r)
        	        	exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({doc_id:docId}, exsoft.contextRoot+"/document/deleteVersion.do", "deleteVersion",
        	       			function(data, e) {
        	        			exsoft.document.init.docDetailInit(docId);
        	       		});
        	    });
        	},
        	//3. 문서의 이력정보를 가져오고 화면에 표시한다
        	getDocumentHistoryList : function() {
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({doc_id:exsoft.document.prototype.gDocId}, exsoft.contextRoot+"/document/docHistoryList.do", "version",
        	 		function(data, e) {
        			
        			exsoft.document.event.historyGridList(data);
        	 	});
        	},
        	
        	//3-1 문서 이력정보 표시
        	historyGridList : function() {
        		//docHistoryList
        		$('#detaildocHistoryList').jqGrid({		
        			url: exsoft.contextRoot+'/document/docHistoryList.do',
        			mtype:"post",
        			datatype:'json',		
        			jsonReader:{
        				root:'list'
        			},		
        			colNames:['action_date','action_name','actor_nm','version_no','etc'],
        			colModel:[
        			          {name:'action_date',index:'action_date',width:110, editable:false,sortable:true,resizable:true,align:'center'},
        			          {name:'action_name',index:'action_id',width:80, editable:false,sortable:true,resizable:true,align:'center'},
        			          {name:'actor_nm',index:'actor_nm',width:70, editable:false,sortable:true,resizable:true,align:'center'},
        			          {name:'version_no',index:'version_no',width:50, editable:false,sortable:true,resizable:true,align:'center'},
        			          {name:'etc',index:'etc',width:150, editable:false,sortable:false,resizable:true,align:'center',
        			        	  formatter : function (cellValue, option, rowObject) {
        			        		  var noteStr = "";
        			        		  if (rowObject.action_id == "MOVE") {
        			        			  noteStr = "[{0}]폴더에서 [{1}]폴더로 이동".format(rowObject.before_nm, rowObject.after_nm);

        			        		  } else if (rowObject.action_id == "CHANGE_CREATOR") {
        			        			  noteStr = "[{0}]에서 [{1}]로 소유권 이전".format(rowObject.before_nm, rowObject.after_nm);
        			        		  }
        			        		  return noteStr;
        			        	  }
        			          },
        			          ], 
        				autowidth:true,
        				height:"auto",			
        				viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
        				sortname : "action_date",			
        				sortorder:"desc",
        				//scroll: true,
        				rowNum : 20,				
        				rowList : exsoft.util.grid.listArraySize(),		
        				emptyDataText: "조회된 결과가 없습니다.",			
        		// 		pager:'#historyGridPager',
        				caption:'문서이력',
        				rownumbers:true,
        				rownumWidth:40,
        				scroll : true,	// Virtual Scoll 활성화
        				postData : {doc_id:exsoft.document.prototype.gDocId}
        				,loadBeforeSend: function() {						
        					exsoft.util.grid.gridNoDataMsgInit('detaildocHistoryList');
        					exsoft.util.grid.gridTitleBarHide('detaildocHistoryList');
        				}
        				,loadComplete: function() {
        					
        					if ($("#detaildocHistoryList").getGridParam("records")==0) {
        						
        						exsoft.util.grid.gridPagerViewHide('detaildocHistoryList');
        						exsoft.util.grid.gridNoDataMsg('detaildocHistoryList','no_data');				
        						exsoft.util.grid.gridPagerHide('detaildocHistoryList');			
        						
        					}else {
        											
        						exsoft.util.grid.gridPagerViewHide('detaildocHistoryList');
        						exsoft.util.grid.gridPagerShow('detaildocHistoryList');
        					}
        					
        					exsoft.util.grid.gridInputInit(false);
        					//exsoft.util.grid.gridResize('detaildocHistoryList','targetDocHistoryList',55);
        					
        				}		
        				,loadError:function(xhr, status, error) {       
        					exsoft.util.error.isErrorChk(xhr);
        				 }		
        				
        		});

        		// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"action_date":"일시","action_name":"수행작업","actor_nm":"작업자","version_no":"버전","etc":"비고"}';
				exsoft.util.grid.gridColumHeader('detaildocHistoryList',headerData,'center');
        		
        	},
        	
        	
        	// 4. 문서의 댓글을 가져오고 화면에 표시한다
        	getDocumentCommentList : function() {
        		// 파라미터 추가
        		//$("#detaildocCommentList").empty();
        		$(".account_nm").html(exsoft.user.user_name);
        		
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({root_id:exsoft.document.prototype.gRootId,table_nm:"XR_COMMENT"}, exsoft.contextRoot+"/document/documentCommentList.do", "comment",
        			function(data, e) {
        			//exsoft.util.table.tablePrintList('detaildocCommentList', data.list, false, true);
        			exsoft.document.event.printCommentList(data);
        		});
        	},
        	
        	//4-1. 문서 댓글 목록 출력
        	printCommentList : function(data) {
        		$("#detaildocCommentList").empty();
        		//var historyList = data.docHistoryList;
        		var buffer="";
        		// 댓글 갯수만큼 루프
        		$(data.list).each(function(index) {
        			//수정
        			buffer += "<tr id='comTR'>";

        			buffer += "<input type='hidden' id='com_id' value='"+this.com_id+"'>";
        			buffer += "<input type='hidden' id='content' value='"+this.content+"'>";
        			buffer += "<input type='hidden' id='com_step' value='"+this.com_step+"'>";
        			if(this.com_order != 0) {
        				buffer += "<td>└ "+this.creator_name+"</td>"; // 이름       + 댓글표시
        			}else{
        				buffer += "<td>"+this.creator_name+"</td>"; // 이름  
        			}
        			buffer += "<td>"+this.parent_creator_name+"</td>"; //  수행작업
        			buffer += "<td >"+this.content+"</td>"; //  내용
        			buffer += "<td>"+this.create_date+"</td>"; //  등록일
        			buffer += "</tr>";
        			
        		});
        		$("#detaildocCommentList").append(buffer);
        	},
        	//
        	
        	commentAction  : function(kbn){
    			exsoft.document.prototype.commentKbn = kbn;
    			
    			var obj = $("#detaildocCommentList").find(".current");
    			var com_id= obj.find("input[id='com_id']").val();
    			var content= obj.find("input[id='content']").val();
    			var comstep= obj.find("input[id='com_step']").val();
    			
    			if(kbn==Constant.ACTION_UPDATE){//갱신일때
    				$(".opinion_writeform").removeClass("hide");
    				$(".opinion_cnt_wrapper").find("textarea").val(content);
        			//this.docCommentUpdate(com_id,content);
    			}else if(kbn==Constant.ACTION_REPLY){//댓글일때
    				exsoft.document.prototype.commentKbn = kbn;
    				$(".opinion_writeform").removeClass("hide");
    				$(".opinion_cnt_wrapper").find("textarea").val("");
    			}else if(kbn==Constant.ACTION_DELETE){//삭제일때
    				
        			var creator_id = obj.find("input[id='content']").val();
        			
    				jConfirm("의견을 삭제하시겠습니까?", "확인", 6, function(r){
    					if(r){
    		        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({root_id:exsoft.document.prototype.gRootId,com_id:com_id,kbn:kbn,com_step:comstep}, exsoft.contextRoot+"/document/documentCommentUpdate.do", "comment",
    		            		function(data, e) {
    			        			if(data.result == "true")	{
    									jAlert('의견을 삭제 했습니다.', "확인", 0);
    									exsoft.document.event.getDocumentCommentList();
    								}else {
    									jAlert(data.message, "확인", 0);
    								}	
    		        		});
    		         	
    					};
    				});
    			}
        		
        	},

			//4-2. 문서 댓글 
        	docCommentUpdate : function() {
        		var obj = $("#detaildocCommentList").find(".current");
    			var com_id= obj.find("input[id='com_id']").val();
    			var content= $(".opinion_cnt_wrapper").find("textarea").val();
    			
    			if(exsoft.document.prototype.commentKbn==null){
    				//신규 의견 등록
    				jConfirm("의견을 등록하시겠습니까?", "확인", 6, function(r){
    					if(r){
    		        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({root_id:exsoft.document.prototype.gRootId,content:content,kbn:exsoft.document.prototype.commentKbn}, exsoft.contextRoot+"/document/documentCommentUpdate.do", "comment",
    		            		function(data, e) {
    			        			if(data.result == "true")	{
    									jAlert('의견을 등록 했습니다.', "확인", 0);
    									exsoft.document.event.getDocumentCommentList();
    								}else {
    									jAlert(data.message, "확인", 0);
    								}	
    		        		});
    					};
    				});  
    				
    			}else if(exsoft.document.prototype.commentKbn== Constant.ACTION_UPDATE || exsoft.document.prototype.commentKbn== Constant.ACTION_REPLY){
    				//4-2. 문서 댓글 수정/댓글 추가
        			jConfirm("의견을 등록하시겠습니까?", "확인", 6, function(r){
        				if(!r){
        					return false;	
        				}
        			}); 
    					//if(r){
    		        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({root_id:exsoft.document.prototype.gRootId,com_id:com_id,content:content,kbn:exsoft.document.prototype.commentKbn}, exsoft.contextRoot+"/document/documentCommentUpdate.do", "comment",
    		            		function(data, e) {
    			        			if(data.result == "true")	{
    			        				jAlert('의견을 등록 했습니다.', "확인", 0);   									
    			        				exsoft.document.event.getDocumentCommentList();
    								}else {
    									jAlert(data.message, "확인", 0);
    								}	
    		        		});
    					//};
    				   			
    			}
    			exsoft.document.prototype.commentKbn = null;
			},
        
        	//문서 잠금 해제
        	Detail_DocumentUnLock : function() {
        		if(exsoft.document.prototype.gAcl_checkoutCancel == 'T') {
        			var jsonArr = [{
        				doc_id : exsoft.document.prototype.getDocVO.doc_id
        				, root_id : exsoft.document.prototype.getDocVO.root_id
        				, is_locked : exsoft.document.prototype.getDocVO.lock_status
        				, doc_type : exsoft.document.prototype.getDocVO.doc_type			
        			}];
        			documentListLayerWindow.event.documentCancelCheckoutSend(jsonArr, 'null');
        		} else {
        			jAlert('반출(잠금) 해제 권한이 없습니다');
        		}  
        		
        		
        	},
        	//문서 복사
        	detail_copyDocument : function(){	
        		if (exsoft.util.common.getAclLevel(exsoft.document.prototype.gAclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
        			jAlert("문서 복사 권한이 없습니다.");
        			return false;
        		} 
        		if(exsoft.document.prototype.getDocVO.lock_status == 'T') {
        			jAlert("체크아웃한 문서가 존재합니다.\n 체크아웃취소 후 다시 작업하시기 바랍니다.");
        			return false;
        		}
        		
        		var jsonArr = [{
        			doc_id : exsoft.document.prototype.getDocVO.doc_id
        			, doc_name : exsoft.document.prototype.getDocVO.doc_name
        			, is_locked : exsoft.document.prototype.getDocVO.lock_status
        			, root_id : exsoft.document.prototype.getDocVO.root_id
        			, doc_type : exsoft.document.prototype.getDocVO.doc_type
        			, is_inherit_acl : exsoft.document.prototype.getDocVO.is_inherit_acl
        			, folder_id : exsoft.document.prototype.getDocVO.folder_id
        		}];
        		documentListLayerWindow.event.documentDetailCopy("DETAIL", jsonArr);
        		//selectSingleFolderWindow.Detailinit(documentListLayerWindow.popupFolderCallback);
        	},
        	
        	// 문서 이동
        	detail_moveDocument : function(){	
        		if (exsoft.util.common.getAclLevel(exsoft.document.prototype.gAclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
        			jAlert("문서 이동 권한이 없습니다.");
        			return false;
        		} 
        		
        		if(exsoft.document.prototype.getDocVO.lock_status == 'T') {
        			jAlert("체크아웃한 문서가 존재합니다.\n 체크아웃취소 후 다시 작업하시기 바랍니다.");
        			return false;
        		}
        		
        		var jsonArr = [{
        			doc_id : exsoft.document.prototype.getDocVO.doc_id
        			, doc_name : exsoft.document.prototype.getDocVO.doc_name
        			, is_locked : exsoft.document.prototype.getDocVO.lock_status
        			, root_id : exsoft.document.prototype.getDocVO.root_id
        			, doc_type : exsoft.document.prototype.getDocVO.doc_type
        			, is_inherit_acl : exsoft.document.prototype.getDocVO.is_inherit_acl
        			, folder_id : exsoft.document.prototype.getDocVO.folder_id
        		}];
        		documentListLayerWindow.event.documentDetailMove("DETAIL", jsonArr);
        		//selectSingleFolderWindow.init(documentListLayerWindow.popupFolderCallback, getMapId, getWorkType, true, getDocType);
        	},
        	
        	// 문서 즐겨찾기 추가 
        	documentAddFavorite : function(){
        		var jsonArr = [];
        		var jsonArrIndex = 0;
        		
        		var rowData = {doc_id:"", root_id:""};
        								
        		//jsonObject
        		rowData['doc_id'] = exsoft.document.prototype.gDocId;
        		rowData['root_id'] =  exsoft.document.prototype.gRootId;
        		if(rowData.doc_id){
        			jsonArr[jsonArrIndex] = rowData;
        		//	jsonArrIndex++;
        		}
        				
        		if(jsonArr.length > 0) {	
        			documentListLayerWindow.event.documentAddFavoriteSend(rowData);
        			return;
        		} else {
        			jAlert("즐겨찾기 문서를 구성하는 중 오류가 발생했습니다.", "즐겨찾기 추가", 0);
        		}
        		
        	},
        	
        	// 작업카트 추가 - 추가기능/버튼 일때 사용 (multi Selected)
        	documentTempwork : function(){
        		var jsonArr = [];
        		var jsonArrIndex = 0;

        		
    			var rowData = {doc_id:"", root_id:""};

    			if(exsoft.document.prototype.getDocVO.lock_status == 'T') {
    				jAlert("체크아웃한 문서가 존재합니다.\n 체크아웃취소 후 다시 작업하시기 바랍니다.", "작업카트 추가", 0);
    				return false;
    			}
    					
    			//jsonObject
    			rowData['doc_id'] = exsoft.document.prototype.gDocId;
    			rowData['root_id'] = exsoft.document.prototype.gRootId;
    			rowData['is_locked'] = exsoft.document.prototype.getDocVO.lock_status;				
    					
    			if(rowData.doc_id){
    				jsonArr[jsonArrIndex] = rowData;
    				jsonArrIndex++;
    			}
    				//}
    				
    			if(jsonArr.length > 0){
    				documentListLayerWindow.event.documentTempworkSend(jsonArr);
    			} else {
    				jAlert("작업카트에 문서를 추가하는 중 오류가 발생했습니다.", "작업카트 추가", 0);
    			}		
						
			},
			
			sendUrlCopy : function() {
				//$(".url_email").removeClass("hide");
				
				// 문서첨부파일 처리
        		var buffer = "";
        		var params = "";

				$("#copy_doc_name").html(exsoft.document.prototype.getDocVO.doc_name + " " + exsoft.document.prototype.getDocVO.version_no) ;
        		$("#copy_folderPath").html(exsoft.document.prototype.gFolderPath);
        		$("#copy_creator_name").html(exsoft.document.prototype.getDocVO.creator_name + " [" + exsoft.document.prototype.getDocVO.owner_name + "]");
        		$("#copy_create_date").html(exsoft.document.prototype.getDocVO.create_date);

        		// 첨부파일 처리 : copy_file_list 	
        		if(exsoft.document.prototype.gPageList.length == 0) {
        			
        			$("#copy_file_list").html("첨부된 파일이 없습니다.");
        			
        		}else {
        			$(exsoft.document.prototype.gPageList).each(function(index) {

        				if(urlCopyPeriod.setUrlValue == 0)	{
        					params = this.page_id + "#" + "9999-12-31";	
        				}else {
        					params = this.page_id + "#" + exsoft.util.date.addDate("d",urlCopyPeriod.setUrlValue, exsoft.util.date.todayStr(),"-");
        				}
        				 			
        				buffer += "<a href='" + urlCopyPeriod.gServerUrl + base64Encode(params) + "'>" + this.page_name + "</a><br>";
        				
        				params = "";
        				
        			});
        			$("#copy_file_list").html(buffer);
        		}
        			
        		exsoft.util.common.copyToClipboard('copyToUrl');
        		exsoft.util.layout.divLayerClose('url_copy_wrapper','url_copy');
        		
        		buffer = null;
        		params = null;
				
			},
			
			
			//URL 메일송부
			getUrlInfo : function() {
				//exsoft.document.prototype.gUrlExpired = "99";
        		//alert(exsoft.document.prototype.getDocVO.doc_name);
        		// URL 복사 유효기간 유효성 체크처리
        		var checkOption = $('input:radio[name="urlDate"]:checked').val();

        		if(checkOption == "limit") {
        			
        			if($("#urlExpired").val().length == 0 ||  $("#urlExpired").val() == 0  )	{
        				jAlert("조회기간을 입력하세요.(0이상)");
        				return false;
        			}else if($("#urlExpired").val() > exsoft.document.prototype.gUrlExpired) {
        				jAlert("조회기간은 시스템 유효기간 이내에서 입력가능합니다.("+exsoft.document.prototype.gUrlExpired+"일이내)");
        				return false;
        			}
        			
        			exsoft.document.prototype.setUrlValue = $("#urlExpired").val();
        		}
        		//alert(exsoft.document.prototype.copy_type);
        		if(exsoft.document.prototype.copy_type == "URL_SEND") {
            		urlCopyPeriod.close();
            		registMail.event.sendOperation();
        		} else {
        			
        		
        		}
        		
        	},
        	
        	
        	sendOperation : function() {
        		// 문서첨부파일 처리
        		var buffer = "";
        		var params = "";
        		
        		$("#copy_doc_name").html(exsoft.document.prototype.getDocVO.doc_name + " " + exsoft.document.prototype.getDocVO.version_no) ;
        		$("#copy_folderPath").html(exsoft.document.prototype.gFolderPath);
        		$("#copy_creator_name").html(exsoft.document.prototype.getDocVO.creator_name + " [" + exsoft.document.prototype.getDocVO.owner_name + "]");
        		$("#copy_create_date").html(exsoft.document.prototype.getDocVO.create_date);
      		
        		
        		$(exsoft.document.prototype.gPageList).each(function(index) {

    				if(exsoft.document.prototype.setUrlValue == 0)	{
    					params = this.page_id + "#" + "9999-12-31";	
    				}else {
    					params = this.page_id + "#" + exsoft.util.date.addDate("d",exsoft.document.prototype.setUrlValue, exsoft.util.date.todayStr(),"-");
    				}
    				 			
    				buffer += "<br><a href='" + exsoft.document.prototype.gServerUrl + base64Encode(params) + "'>" + this.page_name + "</a><br>";
    				
    				params = "";
    				
    			});
        			
        		$("#copy_file_list").html(buffer);
        		
    			var postData = {
    					subject : "문서 발송",//$("#email_subject").val(),
    					receiver_address : $(".email_receiver").val(),
    					//cc_address : $("#cc_email").val(),
    					//hcc_address : $("#hcc_email").val(),
    					messageText : $(".url_paste_cnts").html()
    			};
    			
    			exsoft.util.ajax.ajaxDataFunctionWithCallback(postData, exsoft.contextRoot+'/common/sendURLMail.do', "sendURLMail", function(data, param) {
    				if (data.result == "success") {
    					registMail.close.close();
    					jAlert("메일 발송 완료", "URL메일송부", 0);
    				} else {
    					jAlert(data.message, "URL메일송부", 0);
    					return;
    				}
    			});
        	},
			// URL메일 송부
			docDetailsendUrlEmail : function() {
				var jsonObject = { "type":"INFO"}; 	
				
				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject, exsoft.contextRoot+'/document/copyUrlLink.do', 'urlInfo', 
						function(data, e){
							if(data.result == 'true'){
								
								if(data.expired == 0)	{						
									$("#urlDate1").prop("disabled",false);
								}else {						
									$("#urlDate1").prop("disabled",true);
								}
								
								$("#urlExpired").val(data.expired);
								
								// URL 복사 전역변수 
								exsoft.document.prototype.gUrlExpired = data.expired;
								exsoft.document.prototype.gServerUrl =data.urlInfo;
								
							} else {
								jAlert(data.message);
							}
						}	
					);
				
				$(".url_email").removeClass("hide");
								
			},

        	// 폴더 기본 권한 set
        	setAclItem : function(acl_id){
        		exsoft.util.ajax.ajaxDataFunctionWithCallback({"acl_id" : acl_id}, exsoft.contextRoot+"/permission/aclItemList.do", "", function(data, acl_id) {
        			// 기본 접근자세팅
        			exsoft.util.table.tableDocumentAclItemPrintList('#docmentWrite_acl', data.list);
				});
        		
        	},
        	// exRep ECM에 파일 등록을 성공하였을 경우 후 처리
        	setUploadFile : function(data){
        		// 파일 업로드 성공 처리 :: 성공한 파일개수 증가 및 성공 값 array 담기
        		exsoft.document.wFileUploadJsonArr.push({orgFile:data.orgFile,contentPath:data.contentPath,fileSize:data.fileSize,volumeId:data.volumeId});
        		exsoft.common.file.prototype.wUploadObj.upCounter += 1;
        	},
        	
        	// 등록 취소 시 기존에 등록한 파일을 삭제 한다.
        	deleteUploadFile : function(){
        		//console.log('[stephan][exsoft.document.wFileUploadJsonArr.length] : '+exsoftProcessWrite.wFileUploadJsonArr.length);
        		if(exsoft.document.wFileUploadJsonArr.length >0){
        			var jsonObject = {"fileList":JSON.stringify(exsoft.document.wFileUploadJsonArr)};
        			exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject, exsoft.contextRoot+"/common/fileDelete.do", null, function(){});
        		}
        	},
        	
        	//문서등록 처리
			documentSubmit : function(){
				
				if ($("#documentWrite").validation()) {
					 // jsonMultiFolders :: 다차원 분류 리스트 : multi_folder 
				    //var jsonMultiFolderArr = exsoft.document.event.getMultiFolderList('documentWrite','multi_folder');
					//objForm.jsonMultiFolders.value = JSON.stringify(jsonMultiFolderArr);
        			/**********************************************************************
            		// fileCounter :: 업로드 영역에 있는 파일 수
            		// 1 : 첨부파일 없는 문서등록 처리
            		// 2이상 : 첨부파일 업로드 후 문서등록 처리
            		// upCounter :: 대상 파일 중 업로드 성공한 파일 수
            		**********************************************************************/
					// page_cnt :: 첨부파일수 
					var objForm = document.documentWrite;
					objForm.page_cnt.value = exsoft.common.file.prototype.wUploadObj.fileCounter - 1;

            		
        			if(exsoft.common.file.prototype.wUploadObj.fileCounter == 1 ||
        					(exsoft.common.file.prototype.wUploadObj.fileCounter -1) == exsoft.common.file.prototype.wUploadObj.upCounter)	{
        				
        				exsoft.document.commDocBinder.set("isType","insert"); //업무구분
        				exsoft.document.commDocBinder.set("version_type","NEW"); //버전타입
        				exsoft.document.commDocBinder.set("doc_name",document.documentWrite.doc_name.value); //버전타입
                		var jsonObject = exsoft.document.commDocBinder.getDataToJson(); 
                		jsonObject.fileList = JSON.stringify(exsoft.document.wFileUploadJsonArr);
                		jsonObject.page_cnt = exsoft.common.file.prototype.wUploadObj.fileCounter - 1;
                		jsonObject.security_level = $("#documentWrite input[name=security_level]:radio:checked").val();
                		jsonObject.acl_id = $("#documentWrite input[name=acl_id]").val();
                		
                		$("#documentWrite select[name=doc_type]").prop("disabled",false);
                		
        				// 등록처리
                		exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject ,exsoft.contextRoot + '/document/docSubmit.do',null,function(data, param){          						
                			if(data.result == "true")	{				 
                				exsoft.document.close.layerClose(false);
                				// 등록창 닫기
                			}else {
                				jAlert(data.message);				  				  
                			}
        				});
        			}else {
        				// 대상 파일을 업로드 한다.
        				$("#loading_message").show();
    					exsoft.common.file.prototype.wUploadObj.startUpload();	
        			}
        			
        		} else {
					jAlert("validation 실패");
				}
				
				
				/*
				// page_cnt :: 첨부파일수 
				var objForm = document.documentWrite;
				objForm.page_cnt.value = wUploadObj.fileCounter - 1;
				if(wUploadObj.fileCounter > 1)	{
					objForm.fileList.value =  JSON.stringify(wJsonArr);
				}
	
				$("#documentWrite select[name=doc_type]").prop("disabled",false);
	
				// 등록처리
				exsoft.util.ajax.ajaxFunctionWithCallback("documentWrite",exsoft.contextRoot+"/document/docSubmit.do", "docCommonRegist", 
						
					function(data,e) {
						
						if(data.result == "true")	{				 
							exsoft.document.close.layerClose(false);
							// 등록창 닫기
						}else {
							 jAlert(data.message);				  				  
						}
				});*/
			}
			
		},//event END
		 //3. 닫기 + hide
        close : {
        	layerClose : function(isFileDelete){
        		if(true){
        			exsoft.document.event.deleteUploadFile();
        		}
        		exsoft.common.file.prototype.wUploadObj.cancelAll();
        		exsoft.util.layout.divLayerClose('doc_register_wrapper','doc_register');
        	},
        },
        
		ui : {
			
			//문서 상세보기 선택탭 영역에 따른 액션 분기
			docDetailSelectAction : function(index) {
				if(index==0){
					exsoft.document.init.docDetailInit(exsoft.document.prototype.gDocId);
					//document.docDetailInit("DOC000000033762");
				}else if(index==1){
					exsoft.document.event.getDocumentVersionList();
				}else if(index==2){
					exsoft.document.event.getDocumentHistoryList();
				}else if(index==3){
					exsoft.document.event.getDocumentCommentList();
				}
			},
			imgExtension : function(page_name) {
	        	var ext = page_name.lastIndexOf(".");
				var extnm = page_name.substring(ext+1);
				if(extnm=="xls" || extnm=="xlsx"){
					imgext = "xls.png";
				}else if(extnm=="ppt"|| extnm=="pptx" ){
					imgext = "ppt.png";        			
				}else if(extnm=="hwp"|| extnm=="hwp" ){
					imgext = "hwp.png";       			
				}else if(extnm=="doc"|| extnm=="docx" ){
					imgext = "doc.png";
				}
				return imgext;
        	},
		},
		callback : {
			// 폴더 선택 콜백
			selectAcl : function(aclInfo) {
				console.log("selectAclCallback");
				console.log(aclInfo);
				
				/*
				 * aclInfo 상세
				 * .aclDetail [object]
				 * .aclId [var]
				 * .aclItems [list]
				 * .exAclItems [list]
				 * 
				 * 콘솔로그 확인 후 필요한거 쓰시면 됩니다.
				 */
			},
			relDocWindow : function(returnObjects){
				var documentList = new Array();
				var selectCnt = returnObjects.length;
				
				/*// 1. 선택 문서 중복제거
				$(returnObjects).each(function(index) {
					var returnDoc = this;
					var isExists = false;
					
					// 기존의 목록에 있는지 체크함
					$("#uRefDocTable input[type=checkbox]").each(function(index) {
						if (this.value == returnDoc.doc_id) {
							isExists = true;
						}
					});
					
					// 중복이 아닐경우 추가할 목록에 구성함
					if (!isExists) {
						documentList.push(returnDoc);
					}
				});
				
				// 2. 한도 초과 확인
				if (wRefDoc + documentList.length > wDefaultRefDocCnt) {
					jAlert('관련문서는 최대 {0}개까지 가능합니다.'.format(wDefaultRefDocCnt));
					return false;
				}
				
				// 3. 문서등록 초기상태인 경우나 문서등록 취소 후 다시 창을 띄운 경우 
				if(wRefDoc == 0)	{
					$('#uRefDocTable tr:gt(0)').remove();
				}
				
				wRefDoc += documentList.length;
				
				// 4. 목록에 선택한 문서를 추가한다
				var buffer = "";
				
				// D.DOC_ID, D.DOC_NAME, D.CREATOR_NAME, D.CREATE_DATE
				$(documentList).each(function(i) {
					buffer += "<tr id='{0}'><td><input type='checkbox'  name='uRefDocIdx' id='uRefDocIdx' value='{0}'/></td>".format(this.root_id == "" ? this.doc_id : this.root_id);
					buffer += "<td><a href=\"javascript:initDocumentViewWindow('{0}');\">{1}</td>".format(this.doc_id, this.doc_name);
					buffer += "<td>{0}</td>".format(this.creator_name);
					buffer += "<td>{0}</td>".format(this.create_date);
					buffer += "</tr>";
				});
					
				$('#uRefDocTable').append(buffer);	*/
			},
			
			// 기본폴더 set
        	folderFind : function(nodeInfo) {
        		//console.info(nodeInfo);
        		exsoft.document.commDocBinder.set("folder_path", nodeInfo.full_path.join("/"));
        		exsoft.document.commDocBinder.set("folder_id", nodeInfo.id);
        		exsoft.document.commDocBinder.set("map_id", nodeInfo.map_id);
        		exsoft.document.commDocBinder.set("acl_id", nodeInfo.original.acl_id);
        		// 문서유형 set
        		if(nodeInfo.original.is_type == 'ALL_TYPE'){
        			$('#register_docType').ddslick('enable');	
        			//document.documentWrite.folderIsType.value = nodeInfo.original.is_type;
            		exsoft.document.commDocBinder.set("folderIsType",nodeInfo.original.is_type);
        		}else{
        			$('#register_docType').ddslick('disable');
        			exsoft.document.commDocBinder.set("doc_type", nodeInfo.original.is_type);
    				
    				//문서유형에 맞는 확장 속성을 표시 한다.
        			exsoft.document.event.setExtendTypeAttrItem(nodeInfo.original.is_type);
            		exsoft.document.commDocBinder.set("folderIsType",nodeInfo.original.is_type);
        			
        		}
        		// 권한 셋팅
        		exsoft.document.event.setAclItem(nodeInfo.original.acl_id);
        		
        		exsoft.document.init.docWriteInit();
			},

			// 파일 처리 callback
			fileupload : function(files,data,xhr){
				exsoft.document.event.setUploadFile(data);
				// 전체 파일이 올라 갔을 경우
				if((exsoft.common.file.prototype.wUploadObj.fileCounter -1) == exsoft.common.file.prototype.wUploadObj.upCounter)	{	
					exsoft.document.event.documentSubmit();
				}
			},
			
			

			
		}//callback END
		
		
		
		
		
		
		
		
		
		
		
		
}; // exsoft.document end...

exsoft.document.prototype = {
		gDocId : null,
		gRootId : null,
		gAclLevel : null,
		gAcl_checkoutCancel : null,
		getDocVO : null,
		gPageList : null,				// URL 복사
		gFolderPath : null,				// URL 복사
		gUpdateCallback : null,			// 수정 화면 전환 후 사용되는 콜백
		commentKbn : null,
		
		gUrlExpired : null,
		gServerUrl : null,
		copy_type : null,
		setUrlValue : 0,
		
		
}; // exsoft.document.prototype end...
/***********************************************
 * Preview
 **********************************************/
exsoft.namespace("preview");
exsoft.preview = {
		binder : null,
		event : {
			getPreview : function(docId) {
				if (exsoft.preview.binder == null) {
					exsoft.preview.binder = new DataBinder("#workDocListBody");
				}
				
				exsoft.preview.ajax.documentDetail(docId);
			}
		},
		ajax : {
			documentDetail : function(docId) {
				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({doc_id:docId}, exsoft.contextRoot+"/document/documentDetail.do", "select", function(data,e) {
					
					
					console.info(data);
					exsoft.preview.binder.binding(data.documentVO);
					exsoft.preview.binder.set("folderPath", data.folderPath);
					exsoft.preview.binder.set("multiFolderList", exsoft.preview.functions.getMultiFolderList(data.multiFolderList));
					
					
					exsoft.preview.ui.printPageList("#previewPageListHorizon", data.pageList);//첨부파일
					exsoft.preview.ui.printPageList("#previewPageListVertical", data.pageList);//첨부파일
					
					// 권한 Setting
					exsoft.preview.binder.set("aclName", data.aclDetail.acl_name);
					
					exsoft.util.table.tableDocumentAclItemPrintList('detail_docAclItemListHorizon', data.aclItemList);
					exsoft.util.table.tableDocumentAclItemPrintList('detail_docAclItemListVertical', data.aclItemList);
					exsoft.util.table.tableDocumentAclItemPrintList('detail_docExAclItemListHorizon', data.aclExItemList);
					exsoft.util.table.tableDocumentAclItemPrintList('detail_docExAclItemListVertical', data.aclExItemList);
					
					
					// 문서 설명 (웹 에디터 적용 후 다시 해야함)
					
					// 확장 속성 정보
					
					exsoft.preview.ui.hidePreviewSample();
    			});	
			}
		},
		
		functions : {
			getMultiFolderList : function(multiFolderList) {
				var _ret = "";
				
				$(multiFolderList).each(function() {
					_ret += "{0}<br/>".format(this.folder_path);
				});
				
				return _ret;
			}
		},
		ui : {
			// 화면 분할 기본 화면 숨기기
			hidePreviewSample : function() {
				$(".aside_title").addClass("hide");
				$(".aside_cnts").addClass("hide");
				$(".bottom_title").addClass("hide");
				$(".bottom_cnts").addClass("hide");
				
				$(".doc_detail_attach, .doc_detail_info, .doc_detail_auth").removeClass("hide");
			},
			
			//1-1. 첨부파일 출력
        	printPageList : function(divId, pageList) {
        		//데이터 초기화
        		$(divId).empty();
        		
        		exsoft.preview.binder.set("previewAttachFileCount", pageList.length);
        		
        		var buffer = "<ul>";
        		$(pageList).each(function(index) {			// 첨부 파일 갯수만큼 루프
        			
        			//화장자별 아이콘 변경        			
        			var imgext = exsoft.document.ui.imgExtension(pageList[index].page_name);
        			
        			buffer += "<li class='attach_docs_list'>";
        			buffer += "<a href='#'><img src='"+exsoft.contextRoot+"/img/icon/"+imgext+"' alt='' title=''>"+pageList[index].page_name+"</a>";
        			buffer += "<div class='download_detail'>";
        			buffer += "<span class='download_filesize'>"+pageList[index].fsize+"</span>";
        			buffer += "<a href='#' class='download'>";
        			buffer += "<img src='"+exsoft.contextRoot+"/img/icon/attach_download1.png' alt='' title=''>";
        			buffer += "</a></div>";
	        		buffer += "</li>";

        		});

        		buffer += "</ul>";
        		$(divId).append(buffer);
        	},
		}
}

/***********************************************
 * process
 **********************************************/
/**
 * 협업프로세스 common
 * namespace로 관리
 */
exsoft.process = {
		testMsg : function(){
			alert('11exsoft.process.common')
		},
		
		write : function(wrapClass, divClass){
			exsoft.util.common.formClear('processWrite');
			$('.'+wrapClass).removeClass('hide');
			$('.'+divClass).removeClass('hide');
			
			exsoftProcessWrite.init.initProcessWrite();
			exsoft.util.layout.lyrPopupWindowResize($('.'+divClass));
		},
		
}; // exsoft.process end...

exsoft.process.prototype = {
		
}; // exsoft.process.prototype end...

/***********************************************
 * 공통 bind 관련
 **********************************************/
/**
 * 공통 bind 관련 common
 * namespace로 관리
 */
exsoft.common.bind = {
		// 좌측 메뉴 펼치기/숨기기
		leftMenuToggle : function(){
			$("body").off("click", '.toggle_tree_menu');
			$("body").on("click", '.toggle_tree_menu', function(e){
				// 운영중 오류 날경우 $('.cnts_tbl_wrapper div:nth-child(1)).attr('id') 에서 gbox_ 만 제외하면 됨
				var tableId = $('.cnts_list div:nth-child(1) div:nth-child(3) div:nth-child(3) div:nth-child(1) table').attr('id');
				var targetId = $('.cnts_tbl_wrapper').attr('id');
				var width = $('.lnb_menu').width();

				if(!$(this).hasClass('toggle_hide')) {
					$('.contents').animate({
						'left':0,
						'width':'100%'
					}, 300, function(){
						$('.lnb_menu').addClass('hide');
						$('.toggle_tree_menu').addClass('toggle_hide');
					});
					if (typeof tableId != 'undefined' && typeof targetId != 'undefined')
						exsoft.util.grid.gridIsLeftMenuResize(tableId,targetId,20,-width);
				} else {
					$('.lnb_menu').removeClass('hide');
					$('.contents').removeAttr('style');
					$('.toggle_tree_menu').removeClass('toggle_hide');
					if (typeof tableId != 'undefined' && typeof targetId != 'undefined')
						exsoft.util.grid.gridIsLeftMenuResize(tableId,targetId,20,0);
				}
			});
		},
		
		//퀵메뉴 펼치기/숨기기
		quickMenuToggle : function(){
			$('a.quick_menu').unbind("click");
			$('a.quick_menu').bind("click", function(e){
				e.preventDefault();
				var target = $(this).find('span[class^="quick_menu"]');
				var div = $('.quick_sub_wrapper');

				if(!target.hasClass('toggle_hide')) {
					div.animate({width:0}, 500, function(){
						target.addClass('toggle_hide');
					});
				} else {
					div.animate({width:750}, 500, function(){
						target.removeClass('toggle_hide');
					});
				}
			});
		},
		
		//화면 상하좌우 분활 icon 선택(레이아웃 선택)
		layoutViewToggle : function(){
			$('body').off('click', '.layout_view a');
			$('body').on('click', '.layout_view a', function(e){
				e.preventDefault();
				var dropDown_menu = $(this).parent().find('.layout_view_dropDown');
				if(dropDown_menu.hasClass('hide')){
					dropDown_menu.removeClass('hide');
					$(this).addClass('clicked');
					
					var listViewType = $.cookie('listViewType');
					if(listViewType == 'undefined') {
						listViewType = "list_only";
					}
					$('.layout_view_dropDown > ul').find('li#'+listViewType).addClass('checked');
				} else {
					dropDown_menu.addClass('hide');
					$(this).removeClass('clicked');
				}
			});
		},
		
		//화면 상하좌우 분화에 따라 화면 보이기
		layoutViewDivide : function(){
			//테이블 메뉴 - 레이아웃 선택 드롭다운 체크 선택변경
			$('body').off('click', '[class^="layout_view_dropDown"] > ul li > a');
			$('body').on('click', '[class^="layout_view_dropDown"] > ul li > a', function(e){
				e.preventDefault();
				var li = $('.layout_view_dropDown > ul').find('li');
				var parent = $(this).parents('.layout_view_dropDown');
				li.removeClass('checked');

				$(this).parent().addClass('checked');
				exsoft.common.bind.doFunction.setListView($(this).parent().attr('id'));
				parent.addClass('hide');
			});
		},
		
		/**
		 * Event 관련 함수
		 */		
		event : {
			// 화면 좌우 분활 시 마우스 드래그 시 화면 분활 비율 설정
			layoutDragHorizontal : function() {		
				$('body').off('mousedown', '[class^="horizontal_draggable"]');
				$('body').on('mousedown', '[class^="horizontal_draggable"]', function(e){
					var cntsList = $('.cnts_list');
					var parent = $(this).parent();
					var cntsListStartWidth = cntsList.width();
					var startWidth = parent.width();
					var pX = e.pageX;

					$(document).on('mouseup', function(e){
						$(document).off('mouseup').off('mousemove');
					});

					$(document).on('mousemove', function(me){
						var mx = (me.pageX - pX);
						parent.css({width: startWidth - mx});
						cntsList.css({width: cntsListStartWidth + mx});
					});
				});
			},
			
			// 화면 상하 분활 시 마우스 드래그 시 화면 분활 비율 설정
			layoutDragVertical : function() {	
				$('body').off('mousedown', '[class^="vertical_draggable"]');
				$('body').on('mousedown', '[class^="vertical_draggable"]', function(e){
					var cntsList = $('.cnts_list');
					var parent = $(this).parent();
					var cntsListStartHeight = cntsList.height();
					var startHeight = parent.height();
					var pY = e.pageY;

					$(document).on('mouseup', function(e){
						$(document).off('mouseup').off('mousemove');
					});

					$(document).on('mousemove', function(me){
						var my = (me.pageY-pY);
						parent.css({height:startHeight-my});
						cntsList.css({height:cntsListStartHeight+my});
					});
				});
			},
			
			// 문서 상세조회 의견 contextMenu
			commentCentextMenu : function(){
				$('body .opinion_wrapper > table').off('mousedown', 'tbody > tr > td');
				$('body .opinion_wrapper > table').on('mousedown', 'tbody > tr > td',function(e){
        			var context_menu = $('.opinion_contextMenu');
        			if(e.which == 3) {
        				var offsetX = e.pageX - $('.opinion_wrapper').offset().left;
        				var offsetY = e.pageY - $('.opinion_wrapper').offset().top;
        				context_menu.css({
        					left:offsetX,
        					top:offsetY
        				});

        				context_menu.removeClass('hide');
        				$(this).parents('table').find('tr').removeClass('current');
        				$(this).parent().addClass('current');
        			} else if(e.which == 1) {
        				context_menu.addClass('hide');
        			}
        		});
			},
			
			// div 드롭다운(동적 사용을 위해 on 사용) :: 메뉴 펼치기/숨기기
			divDropDown : function(){
				$("body").off("click", 'a[class="dropDown_txt"]');
				$("body").on("click", 'a[class="dropDown_txt"]', function(e){
					e.preventDefault();
					var span = $(this).parent().find("span");
					var divLength = $(this).parent().children('div').children('div').length;

					if(span.hasClass("down")) {
						span.removeClass("down");
						span.addClass("up");

						$(this).parent().children('div').removeClass('hide');

						if(divLength > 0) {
							$(this).next().removeClass('hide');
						} 

					} else {
						span.removeClass("up");
						span.addClass("down");

						$(this).parent().children('div').addClass('hide');

						if(divLength > 0) {
							$(this).next().addClass('hide');
						} 

					}
				});
			},
			
			// 텝이동
			/*commentTabMenu : function(){
				 //탭 요소 클릭 시 폼 변경
			    $('.tab_element').bind("click", function(){
			        var idx = $(this).index();
			        var targetFrm = $(this).parent().parent().parent().find('div[class^="tab_form"]');
			        targetFrm.addClass('hide');
			        targetFrm.eq(idx).removeClass('hide');

			        $('.tab_element').removeClass('selected');
			        $(this).addClass('selected');
			    });

			},*/
			
			
			// 텝이동
			urlEmailClose : function(){
			    //URL메일발송 - 창 닫기
			    $('.url_email_close').bind("click", function(e){
			    	e.preventDefault();
			    	$(this).parents('.url_email').addClass('hide');
			    }); 
			},
			// 문서 상세조회 - 창 닫기
			docDetailWindowClose : function(){
			  $('.doc_detail_close').bind("click", function(e){
			      e.preventDefault();
			      $(this).parents('.doc_detail').addClass('hide');
			      $('.doc_detail_wrapper').addClass('hide');
			  });
			}
		},
		
		/**
		 * 일반함수
		 */
		doFunction : {
			// 목록 화면 분활 종류 설정
			setListView : function(listViewType){
				var tableId = $('.cnts_list div:nth-child(1) div:nth-child(3) div:nth-child(3) div:nth-child(1) table').attr('id');
				var targetId = $('.cnts_tbl_wrapper').attr('id');
				
				if(listViewType == "list_only") {
					$('[class*="cnts_aside"]').addClass('hide');
					$('[class*="cnts_bottom"]').addClass('hide');

					$('[class*="cnts_list"]').css({
						width:'100%',
						height:'100%',
						'max-width':'100%',
						'max-height':'100%'
					});

				} else if(listViewType == "horizontal_divide") { // 좌우 분활
					$('[class*="cnts_bottom"]').addClass('hide');
					$('[class*="cnts_aside"]').removeClass('hide');
					$('[class*="cnts_list"]').css({
						'height':'100%',
						'max-height':'100%'
					});

					var cntsListWidth = $('[class*="cnts_list"]').width();
					var cntsListMinWidth = parseInt($('[class*="cnts_list"]').css('min-width').replace('px', ''), 10);
					var asideWidth = $('[class*="cnts_aside"]').width();
					var tblWrapWidth = $('cnts_tbl_wrapper').width();

					$('[class*="cnts_list"]').width(tblWrapWidth - asideWidth);
					$('[class*="cnts_list"]').css({'max-width' : tblWrapWidth - 200});
					$('[class*="cnts_aside"]').css({'max-width' : tblWrapWidth - 700});
					
				} else if(listViewType == "vertical_divide") { // 상하 분활
					$('[class*="cnts_aside"]').addClass('hide');
					$('[class*="cnts_bottom"]').removeClass('hide');
					$('[class*="cnts_list"]').css({
						'width':'100%',
						'max-width':'100%'
					});

					var cntsListHeight = $('[class*="cnts_list"]').height();
					var cntsListMinHeight = parseInt($('[class*="cnts_list"]').css('min-height').replace('px', ''), 10);
					var bottomHeight = $('[class*="cnts_bottom"]').height();
					var tblWrapHeight = $('.cnts_tbl_wrapper').height();

					$('[class*="cnts_list"]').css({
						height:tblWrapHeight - 66,
						'max-height':tblWrapHeight - 66
					});
					$('[class*="cnts_bottom"]').css({'max-height' : tblWrapHeight - cntsListMinHeight});
				}

				$.cookie('listViewType', listViewType);
			},
			
			// 화면분활 쿠키값 보이기
			layoutViewCookie : function(){
				var listViewType = $.cookie('listViewType');
				if(listViewType == 'undefined') {
					listViewType = "list_only";
				}
				
				exsoft.common.bind.doFunction.setListView(listViewType);
			},
			
		},
		
}; // exsoft.common.bind end...

exsoft.common.bind.prototype = {
		
}; // exsoft.common.bind.prototype end...

/***********************************************
 * file upload 관련
 **********************************************/
/**
 *  file upload 관련 common
 * namespace로 관리
 */
exsoft.common.file = {
		
		wSettings : null,
		wMaxFileSize : 2048,
		wTotalFileSize : 4096, 
		wMaxFileCount : 10,
		wExtList : 'exe;bat;dll;ocx;',
		
		init : {
			initSettings : function(id, callback){
				exsoft.common.file.prototype.fileuploadDivId = id;
				
				// 환경설정 적용값:: 파일등록개수/파일사이즈/전체사이즈/확장자목록
				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(null,exsoft.contextRoot + '/common/configFileInfo.do',null,function(data, param){
					exsoft.common.file.wMaxFileSize =  exsoft.util.common.fileConfig(data.FILESIZE.skey, data.FILESIZE.is_use, data.FILESIZE.sval, exsoft.common.file.prototype.wDefaultFileSize);
					exsoft.common.file.wTotalFileSize = exsoft.util.common.fileConfig(data.FILETOTAL.skey, data.FILETOTAL.is_use, data.FILETOTAL.sval, exsoft.common.file.prototype.wDefaultFileTotal);
					exsoft.common.file.wMaxFileCount = exsoft.util.common.fileConfig(data.FILECNT.skey, data.FILECNT.is_use, data.FILECNT.sval, exsoft.common.file.prototype.wDefaultFileCnt);
					exsoft.common.file.wExtList =  exsoft.util.common.fileConfig(data.EXT.skey, data.EXT.is_use, data.EXT.sval,"*");
					
					exsoft.common.file.wSettings = null;
					exsoft.common.file.prototype.wUploadObj = null;
					
					exsoft.common.file.wSettings = {
							url: exsoft.contextRoot+"/common/fileUpload.do",						
							multiple:true,													// multiple file selection is allowed.		    
							autoSubmit:false,
							dragDrop:true,
							fileName: "wFiles",
							formData: {"uniqStr":exsoft.util.common.uniqueId()},		   
							maxFileSize:exsoft.common.file.wMaxFileSize,					// 파일최대크기(1GB=1024*1000*1000) :: -1 제한없음
							totalFileSize:exsoft.common.file.wTotalFileSize,				// 총파일사이즈제한 :: -1 제한없음
							maxFileCount:exsoft.common.file.wMaxFileCount,					// Allowed Maximum number of files to be uploaded :: -1 제한없음
							allowedTypes:exsoft.common.file.wExtList,						// 허용하지 않는 확장자 리스트 :: * 제한없음		   
							returnType:"json",
							onSuccess:function(files,data,xhr){		
								callback(files,data,xhr);
							},		 
							onError: function(files,status,errMsg)
							{		    	
								$("#loading_message").hide();
							},
							showProgress:true,
							showDone: false,
							showError: true,												// 에러메세지 출력(파일사이즈,파일개수)
							multiDragErrorStr: "드래그 & 드롭 파일은 허용되지 않습니다.",
							extErrorStr:"허용되지 않는 확장자입니다.",
							sizeErrorStr:"파일당 최대크기를 초과하였습니다.",
							totalSizeErrorStr:"파일최대크기를 초과하였습니다.",
							uploadErrorStr:"업로드를 실패하였습니다.",		
							serverErrorStr : "서버URL 주소가 잘못되었거나 서버가 응답하지 않습니다.",	
							duplicateErrorStr : "동일한 파일이 존재합니다.",
							dragdropWidth: 150 // file box 넓이
					};
					
					//파일추가 영역이 호출 시 지속적으로 생김. dragDrop:true일 경우 기존 영역 삭제. false일 경우 그때 가서 고민
					$('.ajax-upload-dragdrop').remove();
					exsoft.common.file.prototype.wUploadObj = $(exsoft.util.common.getIdFormat(id)).uploadFile(exsoft.common.file.wSettings);
					
				}); // ajax 호출 끝
			}
		},
}; // exsoft.common.file end...

exsoft.common.file.prototype = {
		fileuploadDivId : null,  // document, process 구분해야 함
		wUploadObj : null,
		
		//config.properties 값
		wDefaultFileCnt : null,
		wDefaultFileSize : null,
		wDefaultFileTotal : null,
};  // exsoft.common.file.prototype end...