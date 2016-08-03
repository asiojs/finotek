var registMail = {
	treeObjectForRegistMail : null,
	
	// URL메일송부  Table tr count
	wMailDoc : 0,
	uMailDoc : 0,
	
	// 사용자선택 받는사람/참조/숨은참조  Table tr count
	wReceiver : 0,
	
	// 첨부파일
	gPageList : null,
	
	// 0. 초기화
    initTree : function(rowDataList) {
    	// 작업카트 >  URL 메일 송부 - 창 닫기
	    $('.url_emailSend_close').bind("click", function(e){
	    	e.preventDefault();
	    	$(this).parents('.url_emailSend').addClass('hide');
	    	$('.url_emailSend_wrapper').addClass('hide');
	    });

	    // 작업카트 >  URL 메일 송부 창 닫기 : 음영진 부분 클릭 시 닫기
	    $('.url_emailSend_wrapper').bind("click", function(){
	    	$(this).addClass('hide');
	    	$('.url_emailSend').addClass('hide');
	    });
	    
	    // 작업카트 >  URL 메일 송부 > 찾기 - 창 닫기
	    $('.user_choose_close').bind("click", function(e){
	    	e.preventDefault();
	    	$(this).parents('.user_choose').addClass('hide');
	    	$('.user_choose_wrapper').addClass('hide');
	    });

	    // 작업카트 >  URL 메일 송부 > 찾기 창 닫기 : 음영진 부분 클릭 시 닫기
	    $('.user_choose_wrapper').bind("click", function(){
	    	$(this).addClass('hide');
	    	$('.user_choose').addClass('hide');
	    });
	    
    	// 1. 트리 초기화
		if (registMail.treeObjectForRegistMail == undefined) {
			var treeOption = {
					divId : "#user_tree",
					context : exsoft.contextRoot,
					url : "/group/groupList.do"
			};
			registMail.treeObjectForRegistMail = new XFTree(treeOption);
			registMail.treeObjectForRegistMail.callbackSelectNode = function(e, data) {
				// 검색 옵션 초기화
				$("#mail_userName").val("");
				$("#mail_groupName").val("");
				
				var param = {
						groupName : "",
						userName : "",					
						groupId : data.node.id
				}
				
				// 부서 사용자 목록 조회
				exsoft.util.grid.gridPostDataRefresh('#mail_user_table', exsoft.contextRoot + '/user/searchUserList.do', param);
			}
			registMail.treeObjectForRegistMail.init();
			registMail.event.mailDocData(rowDataList);
			
		} else {
			registMail.treeObjectForRegistMail.refresh();
			registMail.event.mailDocData(rowDataList);
		}
		
		// 2. 사용자 목록 그리드 초기화
		if ($("#mail_user_table")[0].grid == undefined) {
			$('#mail_user_table').jqGrid({
				url:exsoft.contextRoot + '/user/searchUserList.do',
				mtype:"post",
				datatype:'json',		
				jsonReader:{
					page:'page',total:'total',root:'list'
				},		
				colNames:['','group_nm','user_name_ko','position_nm','role_nm','email','user_status_nm'],
				colModel:[
					{name:'user_id',index:'user_id',width:80, editable:false,sortable:false,resizable:true,align:'center',key:true},
					{name:'group_nm',index:'group_nm',width:90, editable:false,sortable:true,resizable:true,hidden:false,align:'center'},
					{name:'user_name_ko',index:'user_name_ko',width:100, editable:false,sortable:false,resizable:true,hidden:false,align:'center'},
					{name:'position_nm',index:'position_nm',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
					{name:'role_nm',index:'role_nm',width:10, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
					{name:'email',index:'email',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
					{name:'user_status_nm',index:'user_status_nm',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'}
				], 
				autowidth:true,
				height:200,
				viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
				sortname : "user_name_ko",			
				sortorder:"asc",
				scroll:true, // virtual Scrolling
				scrollOffset : 0,  
				rowNum : 10,						
				rowList : exsoft.util.grid.listArraySize(),
				emptyDataText: "데이터가 없습니다.",				
				caption:'사용자 목록',	
				loadError:function(xhr, status, error) {       
					exsoft.util.error.isErrorChk(xhr);
				 }
				,loadBeforeSend: function() {					
					exsoft.util.grid.gridTitleBarHide('mail_user_table');	
				}
				,loadComplete: function() {							
					exsoft.util.grid.gridInputInit(false);
				}
				
			});
			
			// Grid 컬럼정렬 처리
			var headerData = '{"user_name_ko":"사용자명","user_id":"ID","group_nm":"그룹명"}';
			exsoft.util.grid.gridColumHeader('mail_user_table',headerData,'center');
			
			headerData = null;
		}
    },

    // 1. 팝업
    open : {
    	// URL메일송부 - 창열기
    	open : function() {
    		exsoft.util.layout.divLayerOpen("url_emailSend_wrapper", "url_emailSend");
    	},
    	
    	// 사용자선택 - 창열기
    	addrOpen : function() {
    		// 받는사람 / 참조 / 숨은참조 테이블 초기화
    		$('#receiver_table tr:gt(0)').remove();
    		$('#cc_table tr:gt(0)').remove();
    		$('#hcc_table tr:gt(0)').remove();
    		
    		// 받는사람 / 참조 / 숨은참조 체크박스 초기화
    		exsoft.util.common.checkboxInit("checkAll_receiver_table");
    		exsoft.util.common.checkboxInit("checkAll_cc_table");
    		exsoft.util.common.checkboxInit("checkAll_hcc_table");
    		
    		exsoft.util.layout.divLayerOpen("user_choose_wrapper", "user_choose");
    	},
    },

    //2. layer + show
    layer : {
    },

    //3. 닫기 + hide
    close : {
    	// URL메일송부 - 창열기
    	close : function() {
    		exsoft.util.layout.divLayerClose("url_emailSend_wrapper", "url_emailSend");
    	},
    	
    	// 사용자선택 - 창닫기
    	addrClose : function() {
    		exsoft.util.layout.divLayerClose("user_choose_wrapper", "user_choose");
    	},
    },

    //4. 화면 이벤트 처리
    event : {
    	// URL메일송부 - 보내기버튼 클릭시
    	sendButtonClick : function() {
    		if($("#receiver_email").val() == "") {
    			jAlert("받는사람을 입력해주세요", "URL메일송부", 0);
    			return;
			} else if($("#email_subject").val() == "") {
				jAlert("메일 제목을 입력해주세요", "URL메일송부", 0);
    			return;
			} else {
				urlCopyPeriod.init("URL_SEND");
			}
    	},
    	
    	// 사용자선택 - 확인버튼 클릭시
    	okButtonClick : function() {
    		var receiver_mail_add = "";
    		var cc_mail_add = "";
    		var hcc_mail_add = "";
    		
    		var receiver_user_id = $("input[name=chb_receiver_table]");
    		var cc_user_id = $("input[name=chb_cc_table]");
    		var hcc_user_id = $("input[name=chb_hcc_table]");
			
    		// 받는사람
			if(receiver_user_id.length > 0) {
				$(receiver_user_id).each(function(index) {
			    	if(receiver_user_id.length > 1) {
			    		receiver_mail_add += $("#mail_"+$(this).val()).val() + ";";
					} else {
						receiver_mail_add = $("#mail_"+$(this).val()).val();
					}
			    });
				
				if(receiver_user_id.length > 1) {
					receiver_mail_add = receiver_mail_add.substring(0, receiver_mail_add.length-1);
				}
				
				$("#receiver_email").val(receiver_mail_add);
				
			} else {
				jAlert("받는사람을 선택하세요", "사용자선택", 0);
				return false;
			}
			
			// 참조
			$(cc_user_id).each(function(index) {
		    	if(cc_user_id.length > 1) {
		    		cc_mail_add += $("#mail_"+$(this).val()).val() + ";";
				} else {
					cc_mail_add = $("#mail_"+$(this).val()).val();
				}
		    });
			
			if(cc_user_id.length > 1) {
				cc_mail_add = cc_mail_add.substring(0, cc_mail_add.length-1);
			}
			
			$("#cc_email").val(cc_mail_add);
			
			// 숨은참조
			$(hcc_user_id).each(function(index) {
		    	if(hcc_user_id.length > 1) {
		    		hcc_mail_add += $("#mail_"+$(this).val()).val() + ";";
				} else {
					hcc_mail_add = $("#mail_"+$(this).val()).val();
				}
		    });
			
			if(hcc_user_id.length > 1) {
				hcc_mail_add = hcc_mail_add.substring(0, hcc_mail_add.length-1);
			}
			
			$("#hcc_email").val(hcc_mail_add);
			
			// 사용자 선택 창 닫기
			registMail.close.addrClose();
    	},
    	
    	// URL메일송부 - 취소버튼 클릭시
    	sendCancelButtonClick : function() {
    		registMail.close.close();
    	},
    	
    	// 사용자선택 - 취소버튼 클릭시
    	selectUserCancelButtonClick : function() {
    		// 받는사람 / 참조 / 숨은참조 테이블 초기화
    		$('#receiver_table tr:gt(0)').remove();
    		$('#cc_table tr:gt(0)').remove();
    		$('#hcc_table tr:gt(0)').remove();
    		
    		// 받는사람 / 참조 / 숨은참조 체크박스 초기화
    		exsoft.util.common.checkboxInit("checkAll_receiver_table");
    		exsoft.util.common.checkboxInit("checkAll_cc_table");
    		exsoft.util.common.checkboxInit("checkAll_hcc_table");
    		
    		registMail.close.addrClose();
    	},
    	
    	// URL메일송부 - Table List Print
    	mailDocData : function(mailDocList) {
			// 화면 초기화
    		$('#mailDocTable tr:gt(0)').remove();
			$("#receiver_email").val("");
			$("#cc_email").val("");
			$("#hcc_email").val("");
			$("#email_subject").val("");
			$("#email_msgTxt").val("");
			
			var buffer = "";
			var doc_id_list = "";
			
			$(mailDocList).each(function(index) {
		    	if(mailDocList.length > 1) {
		    		doc_id_list += "'"+mailDocList[index].doc_id+"'" + ",";
				} else {
					doc_id_list = "'"+mailDocList[index].doc_id+"'";
				}
		    });
			
			if(mailDocList.length > 1) {
				doc_id_list = doc_id_list.substring(0, doc_id_list.length-1);
			}
			
			exsoft.util.ajax.ajaxDataFunctionWithCallback({doc_id_list:doc_id_list}, exsoft.contextRoot+'/document/documentAttachFileByIDList.do', "attachFile", function(data, param) {
				if (data.result == "false") {
					jAlert(data.message, "URL메일송부", 0);
					return;
				} else {
					registMail.gPageList = data.pageList;
					
					for(var j = 0; j < data.pageList.length; j++){
						buffer += "<tr id='MAIL_{0}'>".format(data.pageList[j].page_id);
						buffer += " <td class='left'>" + exsoft.util.common.stripHtml(data.pageList[j].page_name) + "</td>";
						buffer += " <td>" + data.pageList[j].fsize + "</td>";
						buffer += " <td> <img src='"+ exsoft.contextRoot +"/img/icon/window_close3.png' onclick=javascript:registMail.event.mailDocDelete('"+data.pageList[j].page_id+"')></td> "; 
						buffer += "</tr>"; 
					}
				}
				
				$("#mailDocTable").append(buffer);
				
				// 보내는사람 / Email 세팅
				$("#sender_name").val(exsoft.user.user_name);
				$("#sender_email").val(exsoft.user.user_email);
				
				registMail.open.open();
			});
		},
		
		// URL메일송부 - Table TR delete
		mailDocDelete : function(delId){	
			$("#MAIL_"+delId).remove();
		},
		
		// 받는사람 / 참조 / 숨은참조 사용자 추가
		mailReceiverPlus : function(target_name, table_name) {
			registMail.wReceiver = 0;
			
			if(!exsoft.util.grid.gridSelectCheck('mail_user_table')){
				jAlert("사용자를 선택하세요", target_name, 0);
				return false;
			} else {
				var docIdList = exsoft.util.grid.gridSelectData('mail_user_table', 'user_id');
				var rowDataList = new Array();
				$(docIdList.split(",")).each(function(i) {
					if(this != ""){
						var row = $("#mail_user_table").getRowData(this);
						
						rowDataList.push(row);
						registMail.wReceiver += rowDataList.length;
					}
				});
				
				registMail.event.setMailReceiverTable(rowDataList, table_name);	
			}
		},
		
		// 받는사람 / 참조 / 숨은참조 사용자리스트 테이블 세팅
		setMailReceiverTable : function(receiverList, table_name) {
			var buffer = "";
			
			for(var i = 0; i < receiverList.length; i++){
				registMail.uMailDoc++;
				
				buffer += "<tr id='RCV_{0}'>".format(receiverList[i].user_id);
				buffer += " <td class='left'><input type='checkbox' name='chb_"+table_name+"' value='{0}'></td>".format(receiverList[i].user_id);
				buffer += " <td>" + receiverList[i].user_name_ko + "<input type='hidden' id='mail_"+receiverList[i].user_id+"' value='{0}'/> </td>".format(receiverList[i].email);
				buffer += " <td>" + receiverList[i].email + "</td>";
				buffer += "</tr>"; 
			}
			
			$('#'+table_name).append(buffer);
		},
		
		// 받는사람 / 참조 / 숨은참조 사용자 제외
		mailReceiverMinus : function(target_name, table_name) {
			var checkedVal = "";
			var selected_user = $("input[name=chb_"+table_name+"]:checked");
			
			if(selected_user.length > 0) {
				$(selected_user).each(function(index) {
			    	if(selected_user.length > 1) {
						checkedVal += $(this).val() + ",";
					} else {
						checkedVal = $(this).val();
					}
			    });
				
				if(selected_user.length > 1) {
					checkedVal = checkedVal.substring(0, checkedVal.length-1);
				}
				
				var a = checkedVal.split(',');
				for(i=0; i< a.length; i++) {
					$("#"+table_name+" #RCV_"+a[i]).remove();
			    }
				
			} else {
				jAlert("사용자를 선택하세요", target_name, 0);
				return false;
			}
		},
		
		// 사용자 검색
    	searchGroupUser : function() {
    		
    		var param = {
    				groupName : $("#mail_groupName").val(),
    				userName : $("#mail_userName").val(),
    	 			groupId : ''
    		}
    		// 부서 사용자 목록 조회
			exsoft.util.grid.gridPostDataRefresh('#mail_user_table', exsoft.contextRoot + '/user/searchUserList.do', param);
    	},
    	
    	// 엔터키 입력시
		enterKeyPress : function(e) {
			if (e.keyCode == 13) {
				registMail.event.searchGroupUser();
				return false;
			}
		},
    	
		// 메일발송
    	sendOperation : function() {
    		// 문서첨부파일 처리
    		var buffer = "";
    		var params = "";
    		
    		if(registMail.gPageList.length == 0) {
    			
    			$("#copy_file_list").html("첨부된 파일이 없습니다.");
    			
    		} else {
    			
    			$(registMail.gPageList).each(function(index) {

    				if(urlCopyPeriod.setUrlValue == 0)	{
    					params = this.page_id + "#" + "9999-12-31";	
    				}else {
    					params = this.page_id + "#" + exsoft.util.date.addDate("d",urlCopyPeriod.setUrlValue, exsoft.util.date.todayStr(),"-");
    				}
    				 			
    				buffer += "<br><a href='" + urlCopyPeriod.gServerUrl + base64Encode(params) + "'>" + this.page_name + "</a><br>";
    				
    				params = "";
    				
    			});
    			$("#copy_file_list").html(buffer);
    		}
    		
    		if($("#email_msgTxt").val() == "") {
    			$("#copy_msg_txt").html("입력한 내용이 없습니다.");
    		} else {
    			$("#copy_msg_txt").html($("#email_msgTxt").val());
    		}
    		
    		
			var postData = {
					subject : $("#email_subject").val(),
					receiver_address : $("#receiver_email").val(),
					cc_address : $("#cc_email").val(),
					hcc_address : $("#hcc_email").val(),
					messageText : $("#copyToMail").html()
			}
			
			exsoft.util.ajax.ajaxDataFunctionWithCallback(postData, exsoft.contextRoot+'/common/sendURLMail.do', "sendURLMail", function(data, param) {
				if (data.result == "success") {
					registMail.close.close();
					jAlert("메일 발송 완료", "URL메일송부", 0);
				} else {
					jAlert(data.message, "URL메일송부", 0);
					return;
				}
			});
    	}
    },

    //5. 화면 UI 변경 처리
    ui : {
    },

    //6. callback 처리
    callback : {
    }
	
}
