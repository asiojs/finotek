var urlCopyPeriod = {
		
		gUrlExpired : null,
		gServerUrl : null,
		copy_type : null,
		setUrlValue : 0,
		
		 // 0. 초기화
        init : function(copy_type) {
        	// 작업카트 >  URL 메일 송부 > 보내기 > URL조회기간 설정 - 창 닫기
    	    $('.url_copy_close').bind("click", function(e){
    	    	e.preventDefault();
    	    	$(this).parents('.url_copy').addClass('hide');
    	    	$('.url_copy_wrapper').addClass('hide');
    	    });

    	    // 작업카트 >  URL 메일 송부 > 보내기 > URL조회기간 설정 창 닫기 : 음영진 부분 클릭 시 닫기
    	    $('.url_copy_wrapper').bind("click", function(){
    	    	$(this).addClass('hide');
    	    	$('.url_copy').addClass('hide');
    	    });
    	    
        	var jsonObject = { "type":"INFO"}; 	
        	exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/document/copyUrlLink.do', 'urlInfo', 
    			function(data, e){
    				if(data.result == 'true'){
    					
    					if(data.expired == 0)	{						
    						$("#urlDate1").prop("disabled",false);
    					}else {						
    						$("#urlDate1").prop("disabled",true);
    					}
    					
    					$("#urlExpired").val(data.expired);
    					
    					// URL 복사 전역변수 
    					urlCopyPeriod.gServerUrl =data.urlInfo;
    					urlCopyPeriod.gUrlExpired = data.expired;
    					
    				} else {
    					jAlert(data.message, "URL유효기간 설정", 0);
    				}
    			}	
    		);
        	
        	urlCopyPeriod.copy_type = copy_type;
        	if(copy_type == "URL_SEND") {
        		$("#url_copy_confirmText").html("URL 메일 송부를 하시겠습니까?");
        	} else {
        		$("#url_copy_confirmText").html("문서의 URL을 복사하시겠습니까?");
        	}
        	
        	urlCopyPeriod.open();
        },

        // 1. 팝업
        open :  function() {
        	exsoft.util.layout.divLayerOpen("url_copy_wrapper", "url_copy");
        },

        //2. layer + show
        layer : {
        },

        //3. 닫기 + hide
        close : function() {
        	exsoft.util.layout.divLayerClose("url_copy_wrapper", "url_copy");
        },

        //4. 화면 이벤트 처리
        event : {
        	getInfo : function() {
        		// URL 복사 유효기간 유효성 체크처리
        		var checkOption = $('input:radio[name="url_date"]:checked').val();

        		if(checkOption == "limit") {
        			
        			if($("#urlExpired").val().length == 0 ||  $("#urlExpired").val() == 0  )	{
        				jAlert("조회기간을 입력하세요.(0이상)");
        				return false;
        			}else if($("#urlExpired").val() > urlCopyPeriod.gUrlExpired) {
        				jAlert("조회기간은 시스템 유효기간 이내에서 입력가능합니다.("+urlCopyPeriod.gUrlExpired+"일이내)")
        				return false;
        			}
        			
        			urlCopyPeriod.setUrlValue = $("#urlExpired").val();
        		}
        		
        		if(urlCopyPeriod.copy_type == "URL_SEND") {
            		urlCopyPeriod.close();
            		registMail.event.sendOperation();
        		} else {
        			
        		}
        	}
        },

        //5. 화면 UI 변경 처리
        ui : {
        },

        //6. callback 처리
        callback : {
        },

}