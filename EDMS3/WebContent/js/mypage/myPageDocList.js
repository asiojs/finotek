/**
 * 나의문서 JavaScript
 */
var exsoftMypageFunc = {

		pageTitle : "",
		pageTitleId : "",
		pageSize : "",
		docTypeSelect : "myPageDoc_folder",
		addFunctionDiv : "docListLayer_add",
		addFunctionDropClass : "extFunction_dropDown_menu",
		listType : "",
		isInitialized : false,
		folder_id : "",
		is_virtual : "",
		actionStatus : "", // 복원시 status값 세팅
		actionInfo : "", // 복원시 docInfo값 세팅
		tempDocDownloadMax : 0, // 작업카트 - 다운로드 (최대 다운로드개수)

		favoriteFolderTree : null, // 즐겨찾기 폴더 Object
		initFavoriteTree : false,

		sharedFolderTree : null, // 공유 폴더 Object
		initSharedTree : false,

		isTreeClicked : false, // click event 발생 지점 체크용도

		// 나의문서 좌측메뉴명 출력 함수
		pageNaviTitle : function() {
			$("#"+exsoftMypageFunc.pageTitleId).html(exsoftMypageFunc.pageTitle);
		},

		// 나의문서 문서목록명
		ConstPageName : {
			'EXPIRED' : '내 만기 문서',
			'TRASHCAN' : '휴지통',
			'CHECKOUT' :  '내 수정중인 문서',
			'FAVORITE' : '즐겨찾기',
			'SHARE' : '공유받은 문서',
			'SHARE_FOLDER' : '공유받은 폴더',
			'TEMPDOC' : '작업카트',
			'RECENTLYDOC' : '최신문서',
			'OWNER' : '내소유문서'
		},

		// 좌측메뉴 ID 정의
		MenuBtnids : {
			'OWNER' : 'mydoc_hold',
			'CHECKOUT' : 'mydoc_edit',
			'EXPIRED' : 'mydoc_expired',
			'TRASHCAN' : 'mydoc_bin',
			'FAVORITE' : 'mydoc_favorite',
			'SHARE' : 'mydoc_shared_doc',
			'SHARE_FOLDER' : 'mydoc_shared_folder',
			'TEMPDOC' : 'mydoc_tempbox',
			'RECENTLYDOC' : 'mydoc_recentbox'
		},

		// 나의문서 기능버튼 리스트
		ConstButtonList : {
			'tbl_reg':'hide',
			'tbl_del':'hide',
			'grant_transfer_btn' : 'hide',
			'grant_transferAll_btn' : 'hide',
			'checkout_cancel' : 'hide',
			'extend_preserve_btn' : 'hide',
			'tbl_restore' : 'hide',
			'tbl_empty' : 'hide',
			'exclude_favorite' : 'hide',
			'exclude_temp' : 'hide',
			'set_relative_docs' : 'hide',
			'url_email_send' : 'hide',
			'exclude_temp' : 'hide',
			'down_load_file' : 'hide',
			'add_favorite' : 'hide',
		},

		// Context메뉴
		ContextMenu : ['documentListLayer_update','documentListLayer_delete','documentListLayer_move',
		               'documentListLayer_copy','documentListLayer_favorite_add','documentListLayer_work_add',
		               'documentListLayer_checkout_cancel','documentListLayer_authWaste_delete','documentListLayer_restore'],

		// 메뉴벌 기능버튼정의
		OWNER : ['tbl_del','grant_transfer_btn','grant_transferAll_btn'],
		EXPIRED : ['tbl_del','extend_preserve_btn'],
		CHECKOUT : ['checkout_cancel'],
		TRASHCAN : ['tbl_del','tbl_restore','tbl_empty'],
		SHARE : ['tbl_del'],
		SHARE_FOLDER : ['tbl_reg','tbl_del'],
		FAVORITE : ['tbl_reg','tbl_del','exclude_favorite'],
		RECENTLYDOC : ['tbl_del'],
		TEMPDOC : ['exclude_temp','set_relative_docs','url_email_send','down_load_file','add_favorite'],

		// 메뉴 추가기능
		addFuncMenuClass : [
            "<li onclick='javascript:exsoftMypageFunc.event.addFuncMenuFunctions.move();'><a href='#' class='move'>이동</a></li>",
            "<li onclick='javascript:exsoftMypageFunc.event.addFuncMenuFunctions.copy();'><a href='#' class='copy'>복사</a></li>",
            "<li onclick='javascript:exsoftMypageFunc.event.addFuncMenuFunctions.favorite();'><a href='#' class='favorite'>즐겨찾기 추가</a></li>",
            "<li onclick='javascript:exsoftMypageFunc.event.addFuncMenuFunctions.tempbox();'><a href='#' class='tempbox'>작업카트 추가</a></li>"],

		 // 0. 초기화
		 init : {
			// 버튼 비활성화처리
			initBtn : function() {
				$.each(exsoftMypageFunc.ConstButtonList, function(key, val) {
					$("#"+key).addClass(val);
				});
			},

			// 컨텍스트 메뉴 초기화
			contextMenuInit : function() {
				for (var n in exsoftMypageFunc.ContextMenu) {
					$("#"+exsoftMypageFunc.ContextMenu[n]).addClass("hide");
				}

			},

			// 메뉴에 따른 버튼 활성화처리
			initMenuBtn : function(btnList,myMenuType,isVirtual) {

				for(var i in btnList)	{

					if(myMenuType == 'SHARE_FOLDER' || myMenuType == 'FAVORITE') {

						if(isVirtual != "") {

							// 가상폴더일때
							if(isVirtual == "Y" && btnList[i] == 'exclude_favorite') {
								$("#"+btnList[i]).removeClass('hide'); // 즐겨찾기 제외버튼 활성화
							} else if(isVirtual == "N" && !(btnList[i] == 'exclude_favorite')) {
								$("#"+btnList[i]).removeClass('hide'); // 등록, 삭제버튼 활성화
							}

						// 공유받은폴더 일때
						} else {
							$("#"+btnList[i]).removeClass('hide');
						}
					// SHARE_FOLDER, FAVORITE을 제외한 나머지 일때
					} else {
						$("#"+btnList[i]).removeClass('hide');
					}
				}
			},

			// 메뉴 상태 표시 초기화
			initMenuSelect : function(MenuBtnids,myMenuType) {

				$.each(exsoftMypageFunc.MenuBtnids, function(key, val) {

					if(myMenuType == key){
						$("#"+val).addClass("selected");
					}else {
						$("#"+val).removeClass("selected");
					}
				});

			},

			// 문서함 구분 SelectBox 활성화처리
			initDocTypeSelect : function(myMenuType) {

				if(myMenuType == 'TRASHCAN' || myMenuType == 'OWNER' || myMenuType == 'RECENTLYDOC') {
					$("#"+exsoftMypageFunc.docTypeSelect).removeClass('hide');
				}else {
					$("#"+exsoftMypageFunc.docTypeSelect).addClass('hide');
				}
			},

			// 추가기능 활성화처리
			// 1.내소유문서 - 이동/복사/즐겨찾기추가/작업카트추가
			// 2.내수정중문서 - 즐겨찾기추가
			// 3.공유받은문서 - 이동/복사/즐겨찾기추가/작업카트추가
			// 4.공유받은폴더 - 이동/복사/즐겨찾기추가/작업카트추가
			// 5. 휴지통/내만기문서/작업카트/즐겨찾기/최신문서 :: 추가기능 제외
			initAddFunc : function(myMenuType) {

				if(myMenuType == 'TRASHCAN' || myMenuType == 'EXPIRED'
					|| myMenuType == 'FAVORITE' || myMenuType == 'TEMPDOC' || myMenuType == 'RECENTLYDOC' ) {

					$("#"+exsoftMypageFunc.addFunctionDiv).addClass('hide');

				}else {

					$("#"+exsoftMypageFunc.addFunctionDiv).removeClass('hide');

					// 추가기능 비활성 및 메뉴에 따른 활성화처리

					$("."+exsoftMypageFunc.addFunctionDropClass).empty();

					if(myMenuType == 'OWNER' || myMenuType == 'SHARE'
						 || myMenuType == 'SHARE_FOLDER') {

						for(var n in exsoftMypageFunc.addFuncMenuClass)	{
							$("."+exsoftMypageFunc.addFunctionDropClass).append(exsoftMypageFunc.addFuncMenuClass[n]);
						}

					}else if(myMenuType == 'CHECKOUT') {

						for(var n in exsoftMypageFunc.addFuncMenuClass)	{

							if(exsoftMypageFunc.addFuncMenuClass[n].indexOf('favorite') != -1) {
								$("."+exsoftMypageFunc.addFunctionDropClass).append(exsoftMypageFunc.addFuncMenuClass[n]);
							}

						}

					}

				}

			},

			// 나의문서 초기화함수 :: 헤더메뉴
			initPage : function(menuType,myMenuType,pageTitleId,tempDocDownloadMax, pageSize) {

				//문서함 select box
				exsoft.util.common.ddslick('#myPageDoc_folder', 'srch_type4', '', 100, exsoftMypageFunc.callback.changeMap);
				exsoft.util.common.ddslick('#mypageRows', 'tbl_rowCount', '', 68, exsoftMypageFunc.callback.mypageRows);
				exsoft.util.common.ddslick('#myPageDoc_srchType', 'srch_type1', '', 70, function(){});
				exsoft.util.layout.topMenuSelect(menuType);

				// 컨텍스트메뉴 초기화
				exsoftMypageFunc.init.contextMenuInit();

				// 우측페이지명 값 설정
				exsoftMypageFunc.pageTitle = exsoftMypageFunc.ConstPageName[myMenuType];
				exsoftMypageFunc.pageTitleId = pageTitleId;
				exsoftMypageFunc.pageSize = pageSize;
				exsoftMypageFunc.pageNaviTitle(exsoftMypageFunc.pageTitle);

				// 기능버튼 초기화
				exsoftMypageFunc.init.initBtn();
				exsoftMypageFunc.init.initMenuBtn(exsoftMypageFunc[myMenuType],myMenuType);
				exsoftMypageFunc.init.initDocTypeSelect(myMenuType);
				exsoftMypageFunc.init.initAddFunc(myMenuType);
				exsoftMypageFunc.init.initMenuSelect(exsoftMypageFunc.MenuBtnids,myMenuType);
				exsoftMypageFunc.listType = myMenuType;		// 현재문서 메뉴명
				exsoftMypageFunc.tempDocDownloadMax = tempDocDownloadMax;  // 작업카트 - 다운로드 (최대 다운로드개수)

				// 페이지목록 값 설정
    			exsoft.util.layout.setSelectBox('mypageRows',exsoftMypageFunc.pageSize);
    			
    			// 상세검색 postdata세팅
				docDetailSearch.listType = exsoftMypageFunc.listType;
				docDetailSearch.map_id = exsoft.util.layout.getSelectBox('myPageDoc_folder','option'),
				docDetailSearch.folder_id = exsoftMypageFunc.folder_id;
				docDetailSearch.is_virtual = exsoftMypageFunc.is_virtual;
				docDetailSearch.url = "/mypage/authDocumentList.do";

    			/****************************************************************************
    			 * 퀵메뉴 : 즐겨찾기 & 공유받은폴더 바로가기 처리조건 추가
    			 ***************************************************************************/
   				$("#strKeyword1").val("");
   				exsoftMypageFunc.isInitialized = true;
   				exsoftMypageFunc.init.initTree();

   				if(myMenuType == 'SHARE_FOLDER') {
   					$("#mydoc_shared_folderTree").slideToggle();
   					exsoftMypageFunc.initSharedTree = true;
   					exsoftMypageFunc.sharedFolderTree.callbackSelectNode = exsoftMypageFunc.callback.selectShareFolderNode;
   				}else if(myMenuType == 'FAVORITE') {
   					$("#mydoc_favorite_tree").slideToggle();
   					exsoftMypageFunc.initFavoriteTree = true;
   					exsoftMypageFunc.favoriteFolderTree.callbackSelectNode = exsoftMypageFunc.callback.selectFavoriteFolderNode;
   				}else {
   					if($('#mypageDocList')[0].grid != undefined)	{
   						var postData = {
   								LIST_TYPE : exsoftMypageFunc.listType,
   								map_id : exsoft.util.layout.getSelectBox('myPageDoc_folder','option'),
   								folder_id : exsoftMypageFunc.folder_id,
   								is_virtual : exsoftMypageFunc.is_virtual,
   								strIndex : exsoft.util.layout.getSelectBox('myPageDoc_srchType','option'),
   								strKeyword1 : $("#strKeyword1").val(),
   						}
   						exsoft.util.grid.gridPostDataRefresh('mypageDocList',exsoft.contextRoot +'/mypage/authDocumentList.do',postData);
   					}else {
   						exsoftMypageFunc.init.initDocumentList("", "", 1);
   					}
   				}

				// 쿠키값에 설정된 화면 상하좌우 분활 자동으로 보이기
				exsoft.common.bind.doFunction.layoutViewCookie();
			},

			// 나의문서 좌측메뉴 :: 즐겨찾기폴더/공유폴더
			menuInitPage : function(myMenuType,folderId,folder_name,isVirtual) {

				// 컨텍스트메뉴 초기화
				exsoftMypageFunc.init.contextMenuInit();

				// 우측페이지명 값 설정
				exsoftMypageFunc.pageTitle = exsoftMypageFunc.ConstPageName[myMenuType];
				exsoftMypageFunc.pageNaviTitle(exsoftMypageFunc.pageTitle);
				exsoftMypageFunc.init.initBtn();
				exsoftMypageFunc.init.initMenuBtn(exsoftMypageFunc[myMenuType],myMenuType,isVirtual);
				exsoftMypageFunc.init.initDocTypeSelect(myMenuType);
				exsoftMypageFunc.init.initAddFunc(myMenuType);
				exsoftMypageFunc.init.initMenuSelect(exsoftMypageFunc.MenuBtnids,myMenuType);
				exsoftMypageFunc.listType = myMenuType;		// 현재문서 메뉴명

				$("#strKeyword1").val("");
				exsoftMypageFunc.isInitialized = true;

				if($('#mypageDocList')[0].grid != undefined)	{

					exsoftMypageFunc.folder_id = folderId;
					exsoftMypageFunc.is_virtual = isVirtual;
					
					// 상세검색 postdata세팅
					docDetailSearch.listType = exsoftMypageFunc.listType;
					docDetailSearch.map_id = exsoft.util.layout.getSelectBox('myPageDoc_folder','option'),
					docDetailSearch.folder_id = exsoftMypageFunc.folder_id;
					docDetailSearch.is_virtual = exsoftMypageFunc.is_virtual;
					docDetailSearch.url = "/mypage/authDocumentList.do";

					var postData = {
								LIST_TYPE : exsoftMypageFunc.listType,
								map_id : exsoft.util.layout.getSelectBox('myPageDoc_folder','option'),
								folder_id : exsoftMypageFunc.folder_id,
								is_virtual : exsoftMypageFunc.is_virtual,
								page_init : 'true',
								strIndex : exsoft.util.layout.getSelectBox('myPageDoc_srchType','option'),
								strKeyword1 : $("#strKeyword1").val(),
						}
					exsoft.util.grid.gridPostDataRefresh('mypageDocList',exsoft.contextRoot +'/mypage/authDocumentList.do',postData);
				}else {
					exsoftMypageFunc.init.initDocumentList(folderId, isVirtual, 1);
				}
				exsoftMypageFunc.init.initTree();
				// folderId,folder_name,isVirtual :: 츨겨찾기폴더,공유폴더에서만 사용됨
			},

			// 나의문서 리스트 가져오기 :: 좌측메뉴,퀵메뉴,상단메뉴 선택의 경우
			initDocumentList : function(folder_id, is_virtual, nPage) {
				// 초기화 여부
				if (!exsoftMypageFunc.isInitialized) return;

				exsoftMypageFunc.folder_id = folder_id;
				exsoftMypageFunc.is_virtual = is_virtual;

				var buffer = "";
				var _postData = {
						LIST_TYPE : exsoftMypageFunc.listType,
						map_id : exsoft.util.layout.getSelectBox('myPageDoc_folder','option'),
						folder_id : exsoftMypageFunc.folder_id,
						is_virtual : exsoftMypageFunc.is_virtual,
						strIndex : exsoft.util.layout.getSelectBox('myPageDoc_srchType','option'),
						strKeyword1 : $("#strKeyword1").val(),
				}

				// Grid 초기화
				$('#mypageDocList').jqGrid('GridUnload');
				// Grid 세팅
				$('#mypageDocList').jqGrid({
					url: exsoft.contextRoot + '/mypage/authDocumentList.do',
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					cmTemplate: { title: false }, // tooltip 제거
					colNames : ['doc_id','page_cnt','relation_doc','is_locked','doc_name','type_name','creator_name','show_date',
					            'acl_create','acl_changePermission','acl_checkoutCancel','root_id','doc_type','lock_owner','is_inherit_acl','lock_status', 'folder_id','acl_level'],
					colModel : [
						{name:'doc_id', index:'doc_id', width:1, editable:false, sortable:false, key:true, align:'center', hidden:true},
						{name:'page_cnt', index:'page_cnt', width:10, editable:false, sortable:false, resizable:true, align:'center',
							formatter : function(cellValue, option, rowObject) {
								return cellValue > 0 ? "<li class='icon' id='file_"+rowObject.doc_id+"'><img src='"+ exsoft.contextRoot +"/img/icon/attach.png' class='attach_file'></li>" : "";
							}
						},
						{name:'relation_doc', index:'relation_doc', width:10, editable:false, sortable:false, resizable:true, align:'center',
							formatter : function(cellValue, option, rowObject) {
								return cellValue > 0 ? "<li class='icon' id='relation _"+rowObject.doc_id+"'><img src='"+ exsoft.contextRoot +"/img/icon/link.png' class='relative_docs'></li>" : "";
							}
						},
						{name:'is_locked', index:'is_locked', width:10, editable:false, sortable:false, resizable:true, align:'center',
							formatter : function(cellValue, option) {
								return cellValue == 'T' ? "<li class='icon'><img src='"+ exsoft.contextRoot +"/img/icon/lock1.png' alt='' class='doc_lock'></li>" : "";
							},
							cellattr : function(rowId, cellValue, rowObject) {
								var tooltip = '<p>반출자 : '+rowObject.lock_owner+'</p>';
								tooltip += '<p>반출일시 : '+rowObject.lock_date+'</p>';
								var mouseMove = 'onmousemove="javascript:exsoft.util.common.tooltip(\'mypageDocList\',\''+tooltip+'\',\'true\',event)"';
							    var mouseOut = 'onmouseout="javascript:$(\'.tooltip\').addClass(\'hide\')"';

							    return rowObject.is_locked == 'T' ? mouseMove+' '+mouseOut : "";
							}
						},
						{name:'doc_name', index:'doc_name', width:150, editable:false, sortable:true, resizable:true,
							formatter : function(cellValue, option, rowObject){
								return "<img src='"+ exsoft.contextRoot +""+ rowObject.page_extension_img +"' class='extension'>" +
										"<a href='#' onclick='exsoft.preview.event.getPreview(\"{0}\")'>&nbsp;&nbsp;{1}</a>&nbsp;".format(rowObject.doc_id, cellValue) +
										"<a href='#' onclick='exsoftMypageFunc.event.docFunctions.popDocDetail(\"{0}\")'><img src='{1}/img/icon/new_window.png'></a>".format(rowObject.doc_id, exsoft.contextRoot);
							},
							cellattr : function(rowId, cellValue, rowObject) {
								var tooltip = '<p>'+rowObject.doc_name+'</p>';
								var mouseMove = 'onmousemove="javascript:exsoft.util.common.tooltip(\'mypageDocList\',\''+tooltip+'\',\'false\',event)"';
							    var mouseOut = 'onmouseout="javascript:$(\'.tooltip\').addClass(\'hide\')"';

							    return mouseMove+' '+mouseOut;
							}
						},
						{name:'type_name', index:'type_name', width:20, editable:false, sortable:true, resizable:true, align:'center'},
						{name:'creator_name', index:'creator_name', width:30, editable:false, sortable:true, resizable:true, align:'center'},
						{name:'show_date', index:'show_date', width:30, editable:false, sortable:true, resizable:true, align:'center'},
						{name:'acl_level', index:'acl_level', width:20, editable:false, sortable:false, resizable:true, align:'center',
							formatter : function(cellValue, option) {
								return "<li class='previlege'><img src='"+ exsoft.contextRoot +"/img/icon/prev_"+ (cellValue.toLowerCase()).substring(0,1) +".png' class='previlege_grade'><label class='hide'>" + exsoft.util.grid.getAclItemTitle(cellValue) + "</label</li>";
							},
							cellattr: function (rowId, cellValue, rowObject) {
								var tooltip = '<p>소유자 : '+rowObject.owner_name+'</p>';
									tooltip += '<p>기본권한 : '+ exsoft.util.grid.getAclItemTitle(rowObject.acl_level) + '</p>';
									tooltip += '<p>반출취소 : '+(rowObject.acl_checkoutCancel == 'T' ? "가능" : "없음")+'</p>';
									tooltip += '<p>권한변경 : '+(rowObject.acl_changePermission == 'T' ? "가능" : "없음")+'</p>';
								var mouseMove = 'onmousemove="javascript:exsoft.util.common.tooltip(\'mypageDocList\',\''+tooltip+'\',\'true\',event)"';
							    var mouseOut = 'onmouseout="javascript:$(\'.tooltip\').addClass(\'hide\')"';

							    return mouseMove+' '+mouseOut;
				            }
						},

						{name:'acl_create', index:'acl_create', width:1, editable:false, sortable:false, align:'center', hidden:true},
						{name:'acl_changePermission', index:'acl_changePermission', width:1, editable:false, sortable:false, align:'center', hidden:true},
						{name:'acl_checkoutCancel', index:'acl_checkoutCancel', width:1, editable:false, sortable:false, align:'center', hidden:true},
						{name:'root_id', index:'root_id', width:1, editable:false, sortable:false, align:'center', hidden:true},
						{name:'doc_type', index:'doc_type', width:1, editable:false, sortable:false, align:'center', hidden:true},
						{name:'lock_owner', index:'lock_owner', width:1, editable:false, sortable:false, align:'center', hidden:true},
						{name:'is_inherit_acl', index:'is_inherit_acl', width:1, editable:false, sortable:false, align:'center', hidden:true},
						{name:'lock_status',index:'lock_status',width:1,editable:false,sortable:false,align:'center',hidden:true},
						{name:'folder_id',index:'folder_id',width:1, editable:false,sortable:false,align:'center',hidden:true},
					],
					autowidth:true,viewrecords: true,multikey: "ctrlKey",multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "show_date",
					sortorder:"desc",
					scrollOffset: 0,
					viewsortcols:'vertical',
					rowNum : exsoftMypageFunc.pageSize,
					emptyDataText: "데이터가 없습니다.",
					caption:'나의문서 문서목록',
					postData : _postData,
					onCellSelect : function(rowid, iCol, cellcontent, e) {

						var setCol = "";
						var preview = 'doc_preview';
						var file = 'attach_file';
						var relation = 'relative_docs';
						var lock = 'doc_lock';

						if(~cellcontent.indexOf(preview)){
							setCol = preview;
						} else if(~cellcontent.indexOf(file)) {
							setCol = file;
						} else if (~cellcontent.indexOf(relation)) {
							setCol = relation;
						} else if (~cellcontent.indexOf(lock)) {
							setCol = lock;
						}

						if(iCol == 0){
							// 체크시 row값을 set한다.(선택시 : rowid셋팅, 해제시 : rowid제거)
							$("#mypageDocList").jqGrid('setSelection',rowid);
						} else if(setCol == preview){
						} else if(setCol == file && cellcontent != ''){
							var row = $("#mypageDocList").getRowData(rowid);
							documentListLayerWindow.open.openAttachWindow(row);
						} else if(setCol == relation && cellcontent != ''){
							var row = $("#mypageDocList").getRowData(rowid);
							documentListLayerWindow.open.openRelationWindow(row);
						} else if(setCol == lock && cellcontent != ''){
						}
//						if(iCol != 0){
//							// 선택된 row '>' 표시
//				            $("#select_list").remove();
//							$("#"+rowid).find('#doc_preview').prepend("<span id='select_list' class='select_list_icon'></span>");
//						}
					},
					loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('mypageDocList');
						exsoft.util.grid.gridNoDataMsgInit('mypageDocList');
					}
					,loadComplete: function(data) {
						if ($("#mypageDocList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('mypageDocList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('mypageDocList');
						}

						if(exsoftMypageFunc.listType == 'CHECKOUT') {
							$("#mypageDocList").jqGrid("setLabel", "show_date", "반출일");
						} else if(exsoftMypageFunc.listType == 'EXPIRED') {
							$("#mypageDocList").jqGrid("setLabel", "show_date", "만기일");
						} else if(exsoftMypageFunc.listType == 'TRASHCAN') {
							$("#mypageDocList").jqGrid("setLabel", "show_date", "삭제일");
						} else {
							$("#mypageDocList").jqGrid("setLabel", "show_date", "등록일");
						}

						exsoft.util.grid.gridInputInit(false);

						exsoft.util.grid.gridPager("#mypagePager",data);

						$('#mypageDocList').jqGrid("destroyGroupHeader");

						$("tr.jqgrow", this).contextMenu('documentListLayer_context_menu', {
							bindings: {
					            // 수정
					            'documentListLayer_update' : function(trigger) {
					            	var row = $("#mypageDocList").getRowData(trigger.id);
					            	var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(row.acl_level));

					            	if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
					            		jAlert("문서 수정 권한이 없습니다.", "수정", 0);
					            		return false;
					            	}
					            	documentUpdate(trigger.id, fRefreshDocumentList);
								},
								// 삭제
								'documentListLayer_delete': function(trigger) {
									var row = $("#mypageDocList").getRowData(trigger.id);
									var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(row.acl_level));
									var jsonArr = [{
										doc_id : row.doc_id
										, root_id : row.root_id
										, is_locked : row.lock_status
										, doc_type : row.doc_type
									}];

									if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("DELETE")) {
		        						jAlert("문서 삭제 권한이 없습니다.", "삭제", 0);
		        						return false;
		        					}

									documentListLayerWindow.gObjectID = "mypageDocList";
									documentListLayerWindow.event.documentDeleteSend(jsonArr, "ONLY");
					            },
								// 이동
					            'documentListLayer_move': function(trigger) {
					            	var row = $("#mypageDocList").getRowData(trigger.id);
					            	var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(row.acl_level));
					            	var jsonArr = [{
					            		doc_id : row.doc_id
					            		, doc_name : exsoft.util.common.stripHtml(row.doc_name)
					            		, is_locked : row.lock_status
					            		, root_id : row.root_id
					            		, doc_type : row.doc_type
					            		, is_inherit_acl : row.is_inherit_acl
					            		, folder_id : row.folder_id
					            	}];

					            	if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
			        					jAlert("문서 이동 권한이 없습니다.", "이동", 0);
			        					return false;
			        				}

					            	documentListLayerWindow.gObjectID = "mypageDocList";
					            	documentListLayerWindow.gWorkType = null;
									documentListLayerWindow.event.documentMove("ONLY", jsonArr);
					            },
					            // 복사
					            'documentListLayer_copy': function(trigger) {
					            	var row = $("#mypageDocList").getRowData(trigger.id);
					            	var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(row.acl_level));
					            	var jsonArr = [{
					            		doc_id : row.doc_id
					            		, doc_name : exsoft.util.common.stripHtml(row.doc_name)
					            		, is_locked : row.lock_status
					            		, root_id : row.root_id
					            		, doc_type : row.doc_type
					            		, is_inherit_acl : row.is_inherit_acl
					            		, folder_id : row.folder_id
					            	}];

					            	if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
			        					jAlert("문서 복사 권한이 없습니다.", "복사", 0);
			        					return false;
			        				}

					            	documentListLayerWindow.gObjectID = "mypageDocList";
					            	documentListLayerWindow.gWorkType = null;
									documentListLayerWindow.event.documentCopy("ONLY", jsonArr);
					            } ,
					            // 즐겨찾기 추가
					            'documentListLayer_favorite_add' : function(trigger) {
									var row = $('#mypageDocList').getRowData(trigger.id);
									var jsonArr = [{
										doc_id : row.doc_id
										,root_id : row.root_id
									}];

									documentListLayerWindow.event.documentAddFavoriteSend(jsonArr);
								},
					            // 작업카트 추가
								'documentListLayer_work_add': function(trigger) {
					            	var row = $("#mypageDocList").getRowData(trigger.id);
					            	var jsonArr = [{
					            		doc_id : row.doc_id
					            		, root_id : row.root_id
					            		, is_locked : row.lock_status
					            	}];

					            	documentListLayerWindow.event.documentTempworkSend(jsonArr);
					            } ,
								// 체크아웃 취소
					            'documentListLayer_checkout_cancel':function(trigger) {
									var row = $('#mypageDocList').getRowData(trigger.id);
									var jsonArr = [{
										doc_id : row.doc_id
										, root_id : row.root_id
										, is_locked : row.lock_status
										, doc_type : row.doc_type
									}];

									documentListLayerWindow.gObjectID = "mypageDocList";
									documentListLayerWindow.event.documentCancelCheckoutSend(jsonArr, "ONLY");
								},
								// 나의문서-휴지통-삭제
								'documentListLayer_authWaste_delete': function(trigger) {
									exsoftMypageFunc.event.docFunctions.documentDelete(exsoftMypageFunc.listType, "ONLY", trigger.id);
					            },
					            // 나의문서-휴지통-복원
					            'documentListLayer_restore': function(trigger) {
					            	exsoftMypageFunc.event.docFunctions.documentRestore("ONLY", trigger.id);
					            },

					        },
					        onContextMenu: function(event) {
					        	if(exsoftMypageFunc.listType == 'OWNER'
					        		|| exsoftMypageFunc.listType == 'SHARE'
					        		|| exsoftMypageFunc.listType == 'FAVORITE'
					        		||	exsoftMypageFunc.listType == 'RECENTLYDOC') {
					        		if(exsoftMypageFunc.is_virtual == "Y") {
					        			$("#documentListLayer_update").removeClass('hide');
						        		$("#documentListLayer_delete").removeClass('hide');
						        		$("#documentListLayer_move").removeClass('hide');
						        		$("#documentListLayer_copy").removeClass('hide');
						        		$("#documentListLayer_work_add").removeClass('hide');
					        		} else {
					        			$("#documentListLayer_update").removeClass('hide');
						        		$("#documentListLayer_delete").removeClass('hide');
						        		$("#documentListLayer_move").removeClass('hide');
						        		$("#documentListLayer_copy").removeClass('hide');
						        		$("#documentListLayer_favorite_add").removeClass('hide');
						        		$("#documentListLayer_work_add").removeClass('hide');
					        		}
								} else if(exsoftMypageFunc.listType == 'CHECKOUT') {
									$("#documentListLayer_update").removeClass('hide');
									$("#documentListLayer_favorite_add").removeClass('hide');
									$("#documentListLayer_checkout_cancel").removeClass('hide');
								} else if(exsoftMypageFunc.listType == 'EXPIRED') {
									$("#documentListLayer_delete").removeClass('hide');
								} else if(exsoftMypageFunc.listType == 'TRASHCAN') {
									$("#documentListLayer_authWaste_delete").removeClass('hide');
									$("#documentListLayer_restore").removeClass('hide');
								} else if(exsoftMypageFunc.listType == 'SHARE_FOLDER') {
									$("#documentListLayer_update").removeClass('hide');
					        		$("#documentListLayer_delete").removeClass('hide');
					        		$("#documentListLayer_move").removeClass('hide');
					        		$("#documentListLayer_copy").removeClass('hide');
					        		$("#documentListLayer_favorite_add").removeClass('hide');
					        		$("#documentListLayer_work_add").removeClass('hide');
					        		$("#documentListLayer_checkout_cancel").removeClass('hide');
								}
					            return true;
					        }
						});

					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }

				});

				// 컬럼 헤더 정렬 및 다국어 변경 처리
				var headerData = '{"doc_id":"doc_id","page_cnt":"<img src=\'{0}/img/icon/attach.png\' class=\'attach_file\'>","relation_doc":"<img src=\'{0}/img/icon/link.png\' class=\'relative_docs\'>","is_locked":"<img src=\'{0}/img/icon/lock.png\' class=\'doc_lock\'>","doc_name":"제목","type_name":"문서유형","creator_name":"등록자","show_date":"등록일","acl_level":"권한"}'.format(exsoft.contextRoot);
				exsoft.util.grid.gridColumHeader('mypageDocList',headerData,'center');
				headerData = null;
			},

			initTree : function() {
				var treeOption = null;

				// 즐겨찾기 폴더 선택
				if (exsoftMypageFunc.favoriteFolderTree == undefined) {
					treeOption = {
						divId : "#mydoc_favorite_tree",
						context : exsoft.contextRoot,
						url : "/folder/favoriteFolderList.do"
					};

					exsoftMypageFunc.favoriteFolderTree = new XFTree(treeOption);
					exsoftMypageFunc.favoriteFolderTree.callbackSelectNode = exsoftMypageFunc.callback.selectFavoriteFolderNode;
					exsoftMypageFunc.favoriteFolderTree.isFavoriteFolder = true;
					exsoftMypageFunc.favoriteFolderTree.init();
				}

				//공유받은 폴더 선택
				if (exsoftMypageFunc.sharedFolderTree == undefined) {
					treeOption = {
							divId : "#mydoc_shared_folderTree",
							context : exsoft.contextRoot,
							url : "/folder/shareFolderList.do",
							isSelectHiddenRoot : true
					};
					exsoftMypageFunc.sharedFolderTree = new XFTree(treeOption);
					exsoftMypageFunc.sharedFolderTree.callbackSelectNode = exsoftMypageFunc.callback.selectShareFolderNode;
					exsoftMypageFunc.sharedFolderTree.init();
				}
			},
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

			// 문서관련
			docFunctions : {

				// 소유권 변경(선택문서 / 전체문서)
				changeOwner : function(changeType) {

					// 1. 선택문서 소유권 변경일 경우 유효성처리
					if (changeType == "SELECT_DOC") {
						if(!exsoft.util.grid.gridSelectCheck("mypageDocList")) {
							jAlert("소유권을 이전할 문서를 선택하세요.", "소유권이전", 0);
							return;
						}
					}

					// 2. 대상 사용자 선택팝업
					selectSingleUserWindow.init.initSingleUserWindow(function(userInfo) {

						// 소유권 변경 수행
						var postData = {
								changeType : changeType,
								targetUserId : userInfo.user_id,
								selectDocList : exsoft.util.grid.gridSelectData("mypageDocList", "doc_id"),
								search_type : exsoft.util.layout.getSelectBox('myPageDoc_folder','option') == "MYDEPT" ? "WORKSPACE" : "MYWORK"
						}

						exsoft.util.ajax.ajaxDataFunctionWithCallback(postData, exsoft.contextRoot+'/document/changeOwner.do', "changeOwner", function(data, param) {
							if (data.result == "false") {
								jAlert(data.message, "소유권이전", 0);
								return;
							} else {
								exsoftMypageFunc.event.docFunctions.refreshDocumentList();
								jAlert("소유권 이전 완료", "소유권이전", 0);
							}
						});

					})
				},

				//체크아웃 취소
				documentCancelCheckout : function() {
					documentListLayerWindow.gObjectID = "mypageDocList";
					documentListLayerWindow.event.documentCancelCheckout();
				},

				// 내만기문서 - 보존기간연장
				extendPreserve : function() {

					if(!exsoft.util.grid.gridSelectCheck("mypageDocList")) {
						jAlert("보존기간을 연장할 문서를 선택하세요.", "보존기간 연장", 0);
						return false;
					} else {
						var docIdList = null;
						try {
							docIdList = exsoft.util.grid.gridSelectData('mypageDocList','doc_id');
							extendDecade.init.initWindow(docIdList);
						}
						finally {
							docIdList = null;
						}
					}
				},

				// 휴지통 - 복원
				documentRestore : function(status, getInfo) {

					exsoftMypageFunc.actionStatus = status;
					exsoftMypageFunc.actionInfo = getInfo;

					if(status != "ONLY" && !exsoft.util.grid.gridSelectCheck('mypageDocList')) {
						jAlert('복원할 문서를 선택하세요.', "복원", 0);
						return false;
					} else {
						var getMapID = $('#map_id option:selected').val();
						if(getMapID == "MYPAGE"){
							selectSingleFolderWindow.init(exsoftMypageFunc.event.docFunctions.restoreFolderCallback, "MYPAGE", null, true, "ALL_TYPE");
						} else {
							selectSingleFolderWindow.init(exsoftMypageFunc.event.docFunctions.restoreFolderCallback, "MYDEPT", "WORK_MYDEPT", true, "ALL_TYPE");
						}
					}
				},

				// 복원 server send
				restoreFolderCallback : function(returnFolder){
					var targetFolderId = returnFolder.id;
					var jsonArr = [];
					var jsonArrIndex = 0;

					if(exsoftMypageFunc.actionStatus != "ONLY"){
						var id = $("#mypageDocList").getGridParam('selarrrow');

						for(var i = 0; i < id.length; i++){
							var rowData = {doc_id:"", doc_name:"", root_id:"", doc_type:""};
							var rowId = $("#mypageDocList").getRowData(id[i]);

							//jsonObject
							rowData['doc_id'] = rowId.doc_id;
							rowData['doc_name'] = rowId.doc_name;
							rowData['root_id'] = rowId.root_id
							rowData['doc_type']= rowId.doc_type;

							if(rowData.doc_id){
								jsonArr[jsonArrIndex] = rowData;
								jsonArrIndex ++;
							}

						}
					} else {
						var rowData = {doc_id:"", doc_name:"", root_id:"", doc_type:""};
						var rowId = $("#mypageDocList").getRowData(exsoftMypageFunc.actionInfo);

						//jsonObject
						rowData['doc_id'] = rowId.doc_id;
						rowData['doc_name'] = rowId.doc_name;
						rowData['root_id'] = rowId.root_id
						rowData['doc_type']= rowId.doc_type;

						if(rowData.doc_id){
							jsonArr[jsonArrIndex] = rowData;
							jsonArrIndex ++;
						}
					}

					if(jsonArr.length > 0){
						jConfirm('문서를 복원하시겠습니까?', '복원', 0, function(ret) {
							var jsonObject = {"type":"RESTORE", "targetFolderId":targetFolderId, "docList":JSON.stringify(jsonArr)};
							if(ret) {
								exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/mypage/authWasteDocDelete.do', 'delete',
									function(data, e){
										if(data.result == 'true'){
											exsoft.util.grid.gridRefresh('mypageDocList', exsoft.contextRoot + '/mypage/authDocumentList.do');
										} else {
											jAlert(data.message, '복원', 0);
										}
									}
								);
							}
						});
					}
				},

				// 휴지통비우기
				allDocumentDelete : function(){
					jConfirm("휴지통의 모든 문서를 영구삭제하시겠습니까?", "휴지통 비우기", 0, function(ret) {
						var jsonObject = {"type":"ALL_DELETE"};
						if(ret){
							exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/mypage/authWasteDocDelete.do', 'delete',
								function(data, e){
									if(data.result == 'true'){
										exsoft.util.grid.gridRefresh('mypageDocList', exsoft.contextRoot + '/mypage/authDocumentList.do');
									} else {
										jAlert(data.message, "휴지통 비우기", 0);
									}
								}
							);
						}
					});
				},

				// 삭제 버튼 클릭시 (function내에서 list_type으로 구별해줌)
				documentDelete : function(list_type, status, getInfo) {

					// 휴지통 - 삭제
					if(list_type == "TRASHCAN") {
						if(status != "ONLY" && !exsoft.util.grid.gridSelectCheck('mypageDocList')) {
							jAlert("삭제하고자 하는 문서를 선택하세요", "삭제", 0);
							return false;
						} else {
							var jsonArr = [];
							var jsonArrIndex = 0;

							if(status != "ONLY") {
								var id = $("#mypageDocList").getGridParam('selarrrow');

								for(var i = 0; i < id.length; i++){
									var rowData = {doc_id : "",  is_locked : "",root_id : "", doc_type : ""};
									var rowId = $("#mypageDocList").getRowData(id[i]);

									var lock = exsoft.util.common.stripHtml(rowId.is_locked);
									if(lock != 'F'){
										lock = 'T';
									}

									//jsonObject
									rowData['doc_id'] = rowId.doc_id;
									rowData['is_locked'] = lock;
									rowData['root_id'] = rowId.root_id;
									rowData['doc_type'] = rowId.doc_type;

									if(rowData.doc_id){
										jsonArr[jsonArrIndex] = rowData;
										jsonArrIndex ++;
									}
								}
							} else {
								var rowData = {doc_id:"", root_id:"", is_locked:"", doc_type:""};
								var rowId = $("#mypageDocList").getRowData(getInfo);

								if(lock != 'F') {
									lock = 'T';
								}
								//jsonObject
								rowData['doc_id'] = rowId.doc_id;
								rowData['root_id'] = rowId.root_id;
								rowData['is_locked'] = lock;
								rowData['doc_type'] = rowId.doc_type;

								if(rowData.doc_id){
									jsonArr[jsonArrIndex] = rowData;
									jsonArrIndex++;
								}
							}

							if(jsonArr.length > 0){
								jConfirm('선택한 문서를 영구삭제하시겠습니까?', '삭제', 0, function(ret) {
									var jsonObject = {"type":"DELETE", "delDocList":JSON.stringify(jsonArr)};
									if(ret){
										exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/mypage/authWasteDocDelete.do', 'delete',
											function(data, e){
												if(data.result == 'true'){
													exsoft.util.grid.gridRefresh('mypageDocList', exsoft.contextRoot + '/mypage/authDocumentList.do');
												} else {
													jAlert(data.message, '삭제', 0);
												}
											}
										);
									}
								});
							}
						}
					} else {
						if(!exsoft.util.grid.gridSelectCheck("mypageDocList")) {
							jAlert("삭제할 문서를 선택하세요", "삭제", 0);
							return false;
						} else {
							documentListLayerWindow.gObjectID = "mypageDocList";
							documentListLayerWindow.event.documentDelete();
						}
					}
				},

				// 즐겨찾기 제외
				deleteFavoriteDocument : function() {
					var jsonArr = [];
					var jsonArrIndex = 0;
					var actionObject = "mypageDocList";

					if(!exsoft.util.grid.gridSelectCheck("mypageDocList")) {
						jAlert('즐겨찾기 제외할 문서를 선택하세요.', "즐겨찾기 제외", 0);
						return false;
					} else {
						jConfirm("즐겨찾기를 제외하시겠습니까?", "즐겨찾기 제외", 0, function(ret){
							var id = $("#" + actionObject).getGridParam('selarrrow');
							for(var i = 0; i < id.length; i++){
								var rowData = {doc_id:"", root_id:""};
								var rowId = $("#" + actionObject).getRowData(id[i]);

								//jsonObject
								rowData['doc_id'] = rowId.doc_id;
								rowData['root_id'] = rowId.root_id;
									if(rowData.doc_id){
									jsonArr[jsonArrIndex] = rowData;
									jsonArrIndex++;
								}
							}
							if(jsonArr.length > 0) {

								var jsonObject = {
									type : "DELETE_FAVORITES",
									docList : JSON.stringify(jsonArr)
								}

								// 4. 즐겨찾기 정보 서버 처리
								exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/document/documentControl.do','DELETE_FAVORITES', function(data, param) {
									exsoft.util.grid.gridRefresh('mypageDocList', exsoft.contextRoot + '/mypage/authDocumentList.do');
									jAlert("즐겨찾기 제외 완료", "즐겨찾기 제외", 0);
								});

							} else {
								jAlert("즐겨찾기 문서를 구성하는 중 오류가 발생했습니다.", "즐겨찾기 제외", 0);
							}
						});
					}
				},

				// 작업카트 제외
				tempDocumentDelete : function(){

					var jsonArr = [];
					var jsonArrIndex = 0;

					if(!exsoft.util.grid.gridSelectCheck("mypageDocList")) {
						jAlert('작업카트에서 제외할 문서를 선택하세요.', "작업카트 제외", 0);
						return false;
					} else {
						var id = $("#mypageDocList").getGridParam('selarrrow');

						for(var i = 0; i < id.length; i++){
							var rowData = {doc_id:"", root_id:""};
							var rowId = $("#mypageDocList").getRowData(id[i]);

							//jsonObject
							rowData['doc_id'] = rowId.doc_id;
							rowData['root_id'] = rowId.root_id;
							rowData['is_locked'] = rowId.lock_status;

							if(rowData.doc_id) {
								jsonArr[jsonArrIndex] = rowData;
								jsonArrIndex++;
							}
						}
						jConfirm("선택한 문서를 작업카트에서 제외하시겠습니까?", "작업카트 제외", 0, function(ret){
							var jsonObject = {"docList":JSON.stringify(jsonArr)};
							if(ret) {
								exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/mypage/tempDocumentDelete.do', 'delete',
								function(data, e){
									if(data.result == 'true'){
										exsoft.util.grid.gridRefresh('mypageDocList', exsoft.contextRoot + '/mypage/authDocumentList.do');
									}
								});
							}
						});
					}
				},

				// 작업카트 - 관련문서 설정
				tempRefDocList : function() {
					configRelDoc.wRefDoc = 0;
					configRelDoc.uRefDoc = 0;

					var id = $("#mypageDocList").getGridParam('selarrrow');

					if(!exsoft.util.grid.gridSelectCheck('mypageDocList') || (id.length < 2)){
						jAlert("관련문서 설정시 2개 이상의 문서를 선택해주세요.", "관련문서 설정", 0);
						return false;
					} else {
						var docIdList = exsoft.util.grid.gridSelectData('mypageDocList', 'doc_id');
						var rowDataList = new Array();
						$(docIdList.split(",")).each(function(i) {
							if(this != ""){
								var row = $("#mypageDocList").getRowData(this);
								rowDataList.push(row);
								configRelDoc.wRefDoc += rowDataList.length;
							}
						});

						configRelDoc.event.tempRefDocData(rowDataList);
					}
				},

				// 작업카트 - 다운로드
				documentDownLoad : function() {
					var downMax = exsoftMypageFunc.tempDocDownloadMax;
					var allPageCnt = 0;

					if(!exsoft.util.grid.gridSelectCheck('mypageDocList')){
						jAlert("다운로드할 문서를 선택하세요.", "다운로드", 0);
						return false;
					} else {
						var id = $("#mypageDocList").getGridParam('selarrrow');

						if(id.length > downMax){
							jAlert("최대 {0}개까지 다운로드가 가능합니다. \n 확인바랍니다".format(downMax), "다운로드", 0);
							return false;
						} else {
							var jsonArr = [];
							var jsonArrIndex = 0;

							for(var i = 0; i < id.length; i++){
								var rowData = {doc_id:""};
								var rowId = $('#mypageDocList').getRowData(id[i]);
								var gAclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(rowId.acl_level));

								if(exsoft.util.common.getAclLevel(gAclLevel) < exsoft.util.common.getAclLevel("READ")) {
									jAlert("다운로드 권한이 없습니다.", "다운로드", 0);
									return false;
								}
								rowData['doc_id'] = rowId.doc_id;
								if(rowId.page_cnt != ''){
									allPageCnt++;
								}
								if(rowData.doc_id){
									jsonArr[jsonArrIndex] = rowData;
									jsonArrIndex++;
								}
							}
						}
						if(allPageCnt > 0) {
							jConfirm('선택한 문서를 일괄 다운로드 하시겠습니까?', "다운로드", 0, function(r){
								if(r){
									$(location).attr("href", exsoft.contextRoot + '/mypage/tempDownload.do?docList='+JSON.stringify(jsonArr)+'&isZip=T' );
								}
							});
						} else {
							jAlert("선택한 문서의 첨부파일 목록이 없습니다.\n 첨부파일이 없으면 다운로드 할 수 없습니다.", "다운로드", 0);
							return false;
						}
					}
				},

				// 작업카트 - URL메일 송부
				sendEmail : function() {
					registMail.wMailDoc = 0;
					registMail.uMailDoc = 0;

					if(!exsoft.util.grid.gridSelectCheck('mypageDocList')){
						jAlert("메일 송부할 문서를 선택하세요.", "URL 메일송부", 0);
						return false;
					} else {
						var docIdList = exsoft.util.grid.gridSelectData('mypageDocList', 'doc_id');
						var rowDataList = new Array();
						$(docIdList.split(",")).each(function(i) {
							if(this != ""){
								var row = $("#mypageDocList").getRowData(this);
								rowDataList.push(row);
								registMail.wMailDoc += rowDataList.length;
							}
						});

						registMail.initTree(rowDataList);
					}

				},

				// 작업카트 - 즐겨찾기 추가
				addFavorite : function() {

					if(!exsoft.util.grid.gridSelectCheck("mypageDocList")) {
						jAlert("즐겨찾기 추가할 문서를 선택하세요.", "즐겨찾기 추가", 0);
						return false;
					} else {
						documentListLayerWindow.gObjectID = "mypageDocList";
						documentListLayerWindow.event.documentAddFavorite();
					}
				},

				// 검색 돋보기버튼 클릭시
				searchDocument : function() {
					exsoftMypageFunc.init.initDocumentList("", "", 1);
				},

				// 엔터키 입력시
				enterKeyPress : function(e) {
					if (e.keyCode == 13) {
						exsoftMypageFunc.event.docFunctions.searchDocument();
						return false;
					}
				},

				// 문서목록 새로고침
				refreshDocumentList : function() {
					exsoft.util.grid.gridRefresh('mypageDocList', exsoft.contextRoot + '/mypage/authDocumentList.do');
				},

				// 페이지이동 처리(공통)
				gridPage : function(nPage)	{
					$("#mypageDocList").setGridParam({page:nPage,postData:{is_search:'false',page_init:'false'}}).trigger("reloadGrid");
				},

				// 페이지목록 선택시(공통)
				rowsPage : function(rowNum) {
					$("#mypageDocList").setGridParam({rowNum:rowNum});		// 페이지목록 설정값 변경처리
					$("#mypageDocList").setGridParam({page:1,postData:{is_search:'false',page_init:'true'}}).trigger("reloadGrid");
				},

				// 문서명 우측 문서상세버튼 클릭시
				popDocDetail : function(docId) {
					exsoft.document.layer.docCommonFrm('doc_detail_wrapper', 'doc_detail', docId);
				}

			},

			// 트리관련
			treeFunctions : {

				// 즐겨찾기 편집창 컨텍스트메뉴
				treeContextAction : {
					// 즐겨찾기 등록
					createFavoriteFolder : function(node) {
						exsoft.util.layout.divLayerOpen(favoriteFolderWindow.wrapperClass, favoriteFolderWindow.layerClass);
						favoriteFolderWindow.callback = configFavoriteFolder.event.refreshTree;
						favoriteFolderWindow.initForm(node);
						favoriteFolderWindow.type = "CREATE";
					},

					// 즐겨찾기 이름 변경
					modifyFavoriteFolder : function(node) {
						exsoft.util.layout.divLayerOpen(favoriteFolderWindow.wrapperClass, favoriteFolderWindow.layerClass);
						favoriteFolderWindow.callback = configFavoriteFolder.event.refreshTree;
						favoriteFolderWindow.initForm(node, node.id);
						favoriteFolderWindow.type = "UPDATE";
					},

					// 즐겨찾기 이동
					moveFavoriteFolder : function(node) {
						var jsonObject = {
								folder_id : node.id,
								folder_nm : node.text,
								parent_id : node.parent,
								mode : "SELECT",
								only_virtual : "Y"
						};

						// 즐겨찾기 부모 선택창 팝업 (selectFavoriteFolderWindow.jsp 필요함)
						selectFavoriteFolderWindow.init(jsonObject, false, function(returnObject) {

							var postdata = {
									type : "MOVE",
									source_folder_id : returnObject.folder_id,
									source_parent_id : returnObject.parent_id,
									source_sorts : returnObject.targetNode.original.sorts,
									target_folder_id : returnObject.target_folder_id
							};

							exsoft.util.ajax.ajaxDataFunctionWithCallback(postdata, exsoft.contextRoot+"/folder/favoriteControl.do", "", function(data, param) {
								if (data.result == "true") {
									jAlert("즐겨찾기 이동 완료.", "즐겨찾기 이동", 0);
									configFavoriteFolder.event.getCurrentFavoriteTreeForPopup().refresh();
								} else {
									jAlert(data.message, "즐겨찾기 이동", 0);
								}
							});
						});
					},

					// 즐겨찾기 삭제
					deleteFavoriteFolder : function(node) {
						var postdata = {
								folder_id : node.id,
								folder_name_ko : node.text,
								type : "DELETE"
						}

						jConfirm("폴더를 삭제 하시겠습니까?", "폴더 삭제", 0, function(ret) {

							if (ret) {

								exsoft.util.ajax.ajaxDataFunctionWithCallback(postdata, exsoft.contextRoot+"/folder/favoriteControl.do", "", function(data) {

									if (data.result == "true") {
										jAlert("즐겨찾기 삭제 완료.", "폴더 삭제", 0);
										configFavoriteFolder.event.getCurrentFavoriteTreeForPopup().refresh();
									} else {
										jAlert(data.message, "폴더 삭제", 0);
									}
								})
							}
						});
					}
				},

				getCurrentFavoriteTree : function() {
					return exsoftMypageFunc.favoriteFolderTree;
				},

				getCurrentSharedTree : function() {
					return exsoftMypageFunc.sharedFolderTree;
				},

				// 즐겨찾기폴더 새로고침
				favoriteRefresh : function() {
					exsoftMypageFunc.event.treeFunctions.getCurrentFavoriteTree().refresh();
					$("#mydoc_favorite_tree").slideDown();
				},

				// 공유받은폴더 :: 새로고침 후에는 항상 폴더목록 펼친상태 유지하게 변경처리
				sharedRefresh : function() {
					exsoftMypageFunc.event.treeFunctions.getCurrentSharedTree().refresh();
					$("#mydoc_shared_folderTree").slideDown();
				}

			},

			// 추가기능관련
			addFuncMenuFunctions : {
				// 이동
				move : function() {
					documentListLayerWindow.gObjectID = "mypageDocList";
					documentListLayerWindow.gWorkType = null;
					documentListLayerWindow.event.documentMove();
				},

				// 복사
				copy : function() {
					documentListLayerWindow.gObjectID = "mypageDocList";
					documentListLayerWindow.gWorkType = null;
					documentListLayerWindow.event.documentCopy();
				},

				// 즐겨찾기 추가
				favorite : function() {
					documentListLayerWindow.gObjectID = "mypageDocList";
					documentListLayerWindow.event.documentAddFavorite();
				},

				// 작업카트 추가
				tempbox : function() {
					documentListLayerWindow.gObjectID = "mypageDocList";
					documentListLayerWindow.event.documentTempwork();
				},
			},

		 },

		 //5. 화면 UI 변경 처리
		 ui : {

		 },

		 //6. callback 처리
		 callback : {

			// Callback :: 즐겨찾기 폴더 선택 콜백
			selectFavoriteFolderNode : function(e, data) {

				// 초기화중일 경우는 제외한다

				if (data.node.parent == "#" && !exsoftMypageFunc.initFavoriteTree) {
					exsoftMypageFunc.initFavoriteTree = true;
					return;
				}

				// 트리 폴더 선택임을 체크함
				isTreeClicked = true;

				exsoftMypageFunc.init.menuInitPage("FAVORITE", data.node.id, data.node.text, data.node.original.is_virtual, 1);
			},

			// Callback :: 공유받은 폴더 선택 콜백
			selectShareFolderNode : function(e, data) {
				// 초기화중일 경우는 제외한다
				if (data.node.parent == "#" && !exsoftMypageFunc.initSharedTree) {
					exsoftMypageFunc.initSharedTree = true;
					return;
				}

				// 트리 폴더 선택임을 체크함
				isTreeClicked = true;
				exsoftMypageFunc.init.menuInitPage("SHARE_FOLDER", data.node.id, data.node.text, "", 1);
			},

			// Callback : selectbox 콜백
			changeMap : function(divId, selectedData){
				exsoftMypageFunc.init.initDocumentList("", "", 1);
			},

			// 페이지목록 변경선택시
			mypageRows : function(divId, selectedData){
				exsoftMypageFunc.event.docFunctions.rowsPage(selectedData.selectedData.value);
			},
		 }

}