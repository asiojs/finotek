/**
 * 상세검색 JavaScript
 */
var gViewDocId;
var gRootId ="";
var exsoftDocVersionDetailFunc = {
		
        // 0. 초기화
        init : {
        	initDocumentViewWindow : function(docid) {
				
        		gViewDocId = docid;
        		//this.gRootId = gRootId;
        		// 문서 기본정보 조회
        		exsoftDocVersionDetailFunc.event.getDocumentView();
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
        	// 1-1. 문서 상세정보를 가져오고 화면에 표시한다
        	getDocumentView : function() {

        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({doc_id:gViewDocId}, exsoft.contextRoot+"/document/documentDetail.do", "select", 
        			function(data,e) {

        				exsoftDocVersionDetailFunc.event.printDocumentViewVO(data);
        				
        				//base.tableAclItemPrintList("view_docAclItemList", data.aclItemList,'document');
        				//base.tableAclItemPrintList("view_docExAclItemList", data.aclExItemList,'document');
        				
        				//printRefDocView(data);        				
        				//printPageListView(data);
        		});
        	},
        	//2-1. 문서 기본정보 출력
        	printDocumentViewVO : function(data) {
        		var docVO = data.documentVO;

        		//gRootId = docVO.root_id == "" ? docVO.doc_id : docVO.root_id;
        		$("#view_doc_name").html(docVO.doc_name);
        		$("#view_doc_version").html("Ver " + docVO.version_no);
        		$("#view_folderPath").html(data.folderPath);
        		$("#view_type_name").html(docVO.type_name);
        		$("#view_preservation_year").html(docVO.preservation_year + "년");        		
        		$("#view_security_level_name").html(exsoft.util.common.findCodeName(data.securityList,docVO.security_level));
        		$("#view_access_grade_name").html(exsoft.util.common.findCodeName(data.positionList,docVO.access_grade));
        		$("#view_creator_name").html(docVO.creator_name + " [" + docVO.owner_name + "]");
        		$("#view_create_date").html(docVO.create_date);
        		$("#view_multiLink").html();
        		$("#view_keyword").html(docVO.keyword);
        		$("#vtemp_content").html(docVO.doc_description.replace('&lt;','<').replace('& lt;','<').replace('&gt;', '>').replace('& gt;', '>'));
        		$('#vIframe_editor').attr("src","${contextRoot}/editor_7.4.5/doc_view.jsp");				

        	}
        	
        },

        //5. 화면 UI 변경 처리
        ui : {        		
        },

        //6. callback 처리
        callback : {                
        },


}
