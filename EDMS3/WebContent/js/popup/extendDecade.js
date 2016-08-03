var extendDecade = {

	docIdList : "",
	callType : "",

    // 0. 초기화
    init : {
    	initWindow : function(docIdList) {
    		// 내 만기 문서 >  보존기간연장 - 창 닫기
    	    $('.extend_preserve_close').bind("click", function(e){
    	    	e.preventDefault();
    	    	$(this).parents('.extend_preserve').addClass('hide');
    	    	$('.extend_preserve_wrapper').addClass('hide');
    	    });

    	    // 내 만기 문서 >  보존기간연장 창 닫기 : 음영진 부분 클릭 시 닫기
    	    $('.extend_preserve_wrapper').bind("click", function(){
    	    	$(this).addClass('hide');
    	    	$('.extend_preserve').addClass('hide');
    	    });

    	    exsoft.util.common.ddslick('#preservationYear', 'set_extend_preserve', '', 58, function(){});

    		extendDecade.docIdList = docIdList;
    		extendDecade.open.layerOpen();
    	}
    },

    // 1. 팝업
    open : {
    	layerOpen : function() {
    		exsoft.util.layout.divLayerOpen("extend_preserve_wrapper", "extend_preserve");
    	}
    },

    //2. layer + show
    layer : {
    },

    //3. 닫기 + hide
    close : {
    	layerClose : function() {
    		exsoft.util.layout.divLayerClose("extend_preserve_wrapper", "extend_preserve");
    	}
    },

    //4. 화면 이벤트 처리
    event : {
    	okButtonClick : function() {
    		jConfirm('선택한 문서의 보존기간을 연장하시겠습니까?', '보존기간 연장', 0, function(ret){
    			if(ret) {
					var year = exsoft.util.layout.getSelectBox('preservationYear','option');
					console.log(year);
					var jsonObject = {"docIdList":extendDecade.docIdList, "preservation_year":year};
					exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/mypage/expiredDocument.do','expired',
						function(data, e){
							if(data.result == 'true'){
								extendDecade.close.layerClose();

								if(extendDecade.callType == "admin") {
									exsoft.util.grid.gridRefresh('expiredMgrDocList', exsoft.contextRoot + '/admin/documentList.do');
								} else {
									exsoft.util.grid.gridRefresh('mypageDocList', exsoft.contextRoot + '/mypage/authDocumentList.do');
								}

							} else {
								jAlert(data.message, "보존기간 연장", 0);
							}
						});
    			}
			});
    	},

    	cancelButtonClick : function() {
    		extendDecade.close.layerClose();
    	}
    },

    //5. 화면 UI 변경 처리
    ui : {
    },

    //6. callback 처리
    callback : {
    }

}