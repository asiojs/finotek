/**
 * 상세검색 JavaScript
 */
var processDetailSearch = {

	targetGridId : null,	// 대상 그리드 ID
	listType : null, // 나의문서 > 메뉴ID
	map_id : null, //나의문서 > 문서함ID
	folder_id : null, // 나의문서 > 즐겨찾기/공유받은폴더 ID
	is_virtual : null, // 나의문서 > 즐겨찾기(폴더/문서)
	url : null,
	processType : null, // 협업관리 > processType
	
    // 0. 초기화
    init : {

    	// 상세검색 초기화 함수
		initPage : function(menuType,processType,pageTitleId) {
			// 기본검색
			exsoft.util.common.ddslick('docDetailIndex_select','srch_type2','', 80, function(){});
			// 문서유형
			exsoft.util.common.ddslick('docDetailType_select','srch_type3','', 90, function(divId, selectedData){
				var selectValue = selectedData.selectedData.value;
				$('#docDetailSearch_docType').parent().parent().addClass('hide');

				if(selectValue == 'ALL_TYPE'){
					$('#docDetailSearch_docType tbody').empty();
				}else{
					var jsonObject = {"type_id":selectValue,"is_extended":"T"}; 	
					exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject,exsoft.contextRoot+'/type/attrList.do', '#docDetailSearch_docType', function(data, tableId){
						
						exsoft.util.table.tableExtendTypeItemPrintList(tableId, data.list, exsoftProcessWrite.actionType);
						 if(data.records != 0){
							 $(tableId).parent().parent().removeClass('hide');
							 // input type=text는 css 적용
							 $(tableId + ' tbody').find(':text').addClass('srch_detail_keyword');
							 
							// table에 select box가 존재하면 ddslick을 해준다.
		    				var $extendType = $(tableId + ' tbody').find('input, select');
		    				$($extendType).each(function(idx){
		    					var name = $(this).attr('name');
		    					
		    					if($(this).is('select')){
		    						$(this).attr('id', name);
		    						exsoft.util.common.ddslick(name,'srch_type1','',80, function(divId, selectValue){
		    							
		    						});
		    					}		    					
		    				});
							 
						 }
						
					});
				}
			});
			
			processDetailSearch.init.initBind();
		},
		
		initBind : function() {
			$('.detail_dropDown_menu').find('a[class="srch_detail_regDate"]').off("click");
			$('.detail_dropDown_menu').find('a[class="srch_detail_regDate"]').on("click", function(e){
				var obj = {};
				obj.value = $(this).attr('id');
				obj.startId = 'datepicker1';
				obj.endId = 'datepicker2';
				
				exsoft.util.date.setDate(obj);
			});
			
			$("#datepicker1").datepicker({dateFormat:'yy-mm-dd'});
			$("#datepicker2").datepicker({dateFormat:'yy-mm-dd'});    		},
		
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
    
    functions : {
    	changeFolderId : function(folderId) {
    		$("#frm_docListLayer_detail_search input[name=folder_id]").val(folderId);
    	}
    },
    
    //4. 화면 이벤트 처리
    event : {
    	search : function() {
    		if (processDetailSearch.targetGridId == null) {
    			console.error("[processDetailSearch.js] 'processDetailSearch.targetGridId'가 지정되지 않았습니다.");
    			return;
    		}
    		
    		var postData = exsoft.util.common.getFormToJsonObject("frm_docListLayer_detail_search");
    		postData.strIndex = exsoft.util.common.getDdslick("#docDetailIndex_select");
    		//postData.doc_type = exsoft.util.common.getDdslick("#docDetailType_select");
    		//postData.LIST_TYPE = processDetailSearch.listType;
    		//postData.map_id = processDetailSearch.map_id;
    		//postData.folder_id = processDetailSearch.folder_id;
    		//postData.is_virtual = processDetailSearch.is_virtual;
    		postData.type = processDetailSearch.processType;
    		
    		console.info("url?!!!");
    		console.info(processDetailSearch.url);
    		exsoft.util.grid.gridPostDataInitRefresh(processDetailSearch.targetGridId, exsoft.contextRoot + processDetailSearch.url,postData);
    		
    		console.info(postData);
    		console.info("search!!!");
    	},
    	
    	processDetailSearchInit : function(){
    		exsoft.util.common.formClear("frm_docListLayer_detail_search");
    		
    		$('#docDetailSearch_docType').parent().parent().addClass('hide');
    		$('#docDetailSearch_docType tbody').empty();
    		
    		$('#frm_docListLayer_detail_search .dd-container').each(function(){
    			var valText;
    			var val;
    			
    			$(this).children('ul').find('li > a').removeClass('dd-option-selected');
    			
    			$(this).children('ul').find('li > a').each(function(){
    				$(this).addClass('dd-option-selected');
    				valText = $(this).children('label').text();
    				val = $(this).children('input').val();
    				return false; // break;
    			});
    			
    			$(this).children('div').children('input').val(val);
    			$(this).children('div').children('a').children('label').text(valText);
    		});
    		
    	}
    },

    //5. 화면 UI 변경 처리
    ui : {        		
    },

    //6. callback 처리
    callback : {                
    },


}
