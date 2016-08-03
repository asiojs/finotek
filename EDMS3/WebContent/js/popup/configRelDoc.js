var configRelDoc = {
		
		// 관련문서 설정 Table tr count
		wRefDoc : 0,
		uRefDoc : 0,
		
		// 0. 초기화
        init : function(refDocList) {
    	    configRelDoc.event.tempRefDocData(refDocList);	
        },

        // 1. 팝업
        open : function() {
        	exsoft.util.layout.divLayerOpen("relativeDocs_choose_wrapper", "relativeDocs_choose");
        },

        //2. layer + show
        layer : {
        },

        //3. 닫기 + hide
        close : function() {
        	exsoft.util.layout.divLayerClose("relativeDocs_choose_wrapper", "relativeDocs_choose");
        },

        //4. 화면 이벤트 처리
        event : {
        	// 관련문서 설정 - 선택 Table List Print
			tempRefDocData : function(refDocList) {
				$('#tempRefDocTable tr:gt(0)').remove();
				
				var buffer = "";
				
				for(var i = 0; i < refDocList.length; i++){
					configRelDoc.uRefDoc++;
					
					buffer += "<tr id='REF_{0}'>".format(refDocList[i].doc_id);
					buffer += " <td><input type='radio' name='tempRef' id='tempRef' value='"+refDocList[i].doc_id+"'/> </td>";
					buffer += " <td class='left'>" + exsoft.util.common.stripHtml(refDocList[i].doc_name) + "</td>";
					buffer += " <td>" + refDocList[i].creator_name + "<input type='hidden' id='rootId_"+refDocList[i].doc_id+"' value='{0}'/> </td>".format(refDocList[i].root_id);
					buffer += " <td> <img src='"+ exsoft.contextRoot +"/img/icon/window_close3.png' onclick=javascript:configRelDoc.event.refDocDelete('"+refDocList[i].doc_id+"')></td> "; 
					buffer += "</tr>"; 
				}
				
				$("#tempRefDocTable").append(buffer);
				configRelDoc.open();
			},
			
			// 관련문서 설정 - Table TR delete
			refDocDelete : function(delId){	
				if(configRelDoc.uRefDoc > 2){
					$("#REF_"+delId).remove();
					configRelDoc.uRefDoc -= 1;
				} else {
					jAlert("삭제할 수 없습니다.\n 관련문서 설정시 2개 이상의 문서가 필요합니다.", "관련문서 설정", 0);
					return false;
				}
			},
			
			// 확인버튼 클릭시
        	okButtonClick : function() {
        		var mainCheck = $('input:radio[name=tempRef]').is(':checked');
        		if(!mainCheck){
        			jAlert("메인문서를 선택하세요!", "관련문서 설정", 0);
        			return false;
        		} else {
        			var refMainDocId = $('input[type=radio][name=tempRef]:checked').val();
        			var refMainRootId = $("#rootId_"+refMainDocId).val();
        			
        			var jsonArr = [];
        			var jsonArrIndex = 0;
        			
        			$("input:radio[name='tempRef']").each(function(){
        				var jsonData = {doc_id:"", root_id:""};
        				var getDocId = $(this).val();
        				
        				jsonData['doc_id'] = getDocId;
        				jsonData['root_id'] = $("#rootId_"+getDocId).val();
        				
        				if(jsonData.doc_id != refMainDocId){
        					if(jsonData.doc_id){
        						jsonArr[jsonArrIndex] = jsonData;
        						jsonArrIndex++;
        					}
        				}			
        			});
        			var refInsert = false;
        			if(mainCheck){
        				var jsonObject = {"type":"CHECK", "ref_doc_id":refMainDocId, "ref_root_id":refMainRootId};
        				exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/mypage/tempRefDocument.do', 'check', 
        				function(data, e){
        					if(data.isUsing == 'true'){
        						jConfirm('선택한 메인문서에 이미 관련문서가 있습니다.\n 나머지 문서를 추가하시겠습니까?', "관련문서 설정", 0, function(ret){
        							if(ret){
        								configRelDoc.event.refDocSubmit(refMainDocId, refMainRootId, jsonArr);
        							} else {
        								configRelDoc.close();
        							}
        						});
        					} else {
        						configRelDoc.event.refDocSubmit(refMainDocId, refMainRootId, jsonArr);
        					}
        				});
        			}
        		}
        	},
        	
        	// server send
        	refDocSubmit : function(mainDocId, mainRootId, jsonArray){	
        		var jsonObject = {"type":"INSERT", "ref_doc_id":mainDocId, "ref_root_id":mainRootId, "subDocList":JSON.stringify(jsonArray)};
        		exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/mypage/tempRefDocument.do', 'insert', 
        		function(data, e){
        			if(data.result == 'true'){
        				jAlert("관련문서 등록이 완료되었습니다.", "관련문서 설정", 0);
        				configRelDoc.close();
        				exsoft.util.grid.gridRefresh('mypageDocList', exsoft.contextRoot + '/mypage/authDocumentList.do');
        			} else {
        				jAlert(data.message, "관련문서 설정", 0);
        			} 
        		});
        	},
        	
        	// 취소버튼 클릭시
        	cancelButtonClick : function() {
        		configRelDoc.close();
        	},
        },

        //5. 화면 UI 변경 처리
        ui : {
        },

        //6. callback 처리
        callback : {
        },
	
}