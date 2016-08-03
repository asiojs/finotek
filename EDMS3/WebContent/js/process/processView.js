/**
 * 협업 진행단계 관련
 */
var exsoftProcessViewFunc = {
		
		process_id : '',
		doc_root_id : '',		
		isModify : false,				// 속성수정
		isDelete : false,				// 삭제
		isApproveRequest : false,		// 승인요청
		isApprove : false,				// 승인
		isApproveReject : false,		// 반려
		isFileModify : false,			// 파일수정
		isContent : false,				// 내용 사용 여부
		
		 // 0. 초기화
        init : {
        	initProcessView : function(processId, doc_root_id){
        		exsoftProcessViewFunc.process_id = processId;
        		exsoftProcessViewFunc.doc_root_id = doc_root_id;
        		
        		 $('.coopUser_detail_wrapper').removeClass('hide');
				 $('.coopUser_detail').removeClass('hide');
				 
        		exsoft.util.layout.lyrPopupWindowResize($(".coopUser_detail"));
        		
        		exsoftProcessViewFunc.doFunction.setProcessRole(false,false,false,false,false,false,false);
        		
        		// 1. 협업정보 가져오기
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({actionType:Constant.ACTION_VIEW, process_id:processId},exsoft.contextRoot + '/process/processControl.do',null,function(data, param){
        			if(data.result == 'true'){
        				var processVo = data.processVo;
            			var processExecutorList = data.processExecutorList;
            			
            			// 현 단계 이미지 색상 칠하기
            			$('#processView_step li').removeClass('current');
            			$('#processView_step li:nth-child('+processVo.status_number+')').addClass('current');
            			
            			// 협업 기본 정보 set
            			$('#processView_info tr:nth-child(1)').find('span').text(processVo.name);					// 업무명
            			$('#processView_info tr:nth-child(3)').find('span').text(processVo.creator_name);			// 업무요청자
            			$('#processView_info tr:nth-child(4)').find('span').text(processVo.expect_date);			// 업무완료 예정일
            			$('#processView_info tr:nth-child(5)').find('span').text(processVo.content);				// 업무 요청 내용
            			
            			// 협업자 정보 set
            			var isModifyNdelete = false;
            			$(processExecutorList).each(function(idx){
            				var executor_name = this.executor_name;
            				var executor_id = this.executor_id;
            				switch (this.type) {
	    						case Constant.PROCESS.TYPE_AUTHOR:exsoftProcessViewFunc.doFunction.setExecutorName(6, executor_name);break;
	    						case Constant.PROCESS.TYPE_COAUTHOR:exsoftProcessViewFunc.doFunction.setExecutorName(7, executor_name);break;
	    						case Constant.PROCESS.TYPE_APPROVER:exsoftProcessViewFunc.doFunction.setExecutorName(8, executor_name);break;
	    						case Constant.PROCESS.TYPE_RECEIVER:exsoftProcessViewFunc.doFunction.setExecutorName(9, executor_name);break;
	    						default:break;
    						};
    						
    						// 현 단계 및 사용자에 맞는 추가기능 set
                			// 1:작성, 2:승인, 3:보완, 4:완료
    						var user_id = exsoft.user.user_id;
                			if(processVo.status_number == 1 || processVo.status_number == 3){
                				//processVo.creator_id 요청자 exsoft.user.user_id
                				if(user_id == processVo.creator_id){ // 요청자
                					// isModify,isDelete,isApproveRequest,isApprove,isApproveReject,isFileModify,isContent
                					exsoftProcessViewFunc.doFunction.setProcessRole(true,true,false,false,false,true,false);
                				}else if(user_id == executor_id && this.type == Constant.PROCESS.TYPE_AUTHOR){ // 작성자
                					// isModify,isDelete,isApproveRequest,isApprove,isApproveReject,isFileModify,isContent
                					exsoftProcessViewFunc.doFunction.setProcessRole(true,true,true,false,false,true,true);
                				}else if(user_id == executor_id && this.type == Constant.PROCESS.TYPE_COAUTHOR){ // 공동 작성자
                					// isModify,isDelete,isApproveRequest,isApprove,isApproveReject,isFileModify,isContent
                					exsoftProcessViewFunc.doFunction.setProcessRole(false,false,false,false,false,true,false);
                				}
                			}else if(processVo.status_number == 2){
                				// TODO : 승인 목록 보이기...
                				if(this.type == Constant.PROCESS.TYPE_APPROVER){
                					console.log('user_id:excutor_id:status ==> ' +user_id+' : '+executor_id+' : '+this.status);
                					if(user_id == executor_id && this.status == Constant.PROCESS.EXECUTOR_START){ 
                						// isModify,isDelete,isApproveRequest,isApprove,isApproveReject,isFileModify,isContent
                						exsoftProcessViewFunc.doFunction.setProcessRole(true,false,false,true,true,true,true);
                					}else if(user_id == executor_id && this.status != Constant.PROCESS.EXECUTOR_START){
                						exsoftProcessViewFunc.doFunction.setProcessRole(true,false,false,false,false,false,false);
                					}
                					
                				}
                			}
    						
                			// 단계에 상관없이 요청자, 대표작성자는 수정, 삭제 권한을 부여
                			if(user_id == processVo.creator_id || this.type == Constant.PROCESS.TYPE_AUTHOR){
                				exsoftProcessViewFunc.isModify = true;
                				exsoftProcessViewFunc.isDelete = true;
                			}
                			
            			});
            			
            			
            			// 승인자, 수신자 tooltip 설정
            			exsoftProcessViewFunc.doFunction.setTooltip('processView_approver', 'processView_approverTooltip', processVo, Constant.PROCESS.TYPE_APPROVER);
            			exsoftProcessViewFunc.doFunction.setTooltip('processView_receiver', 'processView_receiverTooltip', processVo, Constant.PROCESS.TYPE_RECEIVER);
            			
            			// 기능 버튼 hide 제거
            			//, , , , , (requestApproval_wordcnts)
            			exsoftProcessViewFunc.isModify ? $('#processView_modify').removeClass('hide') : $('#processView_modify').addClass('hide');
        				exsoftProcessViewFunc.isDelete ? $('#processView_delete').removeClass('hide') : $('#processView_delete').addClass('hide');
        				exsoftProcessViewFunc.isApproveRequest ? $('#processView_approveRequest').removeClass('hide') : $('#processView_approveRequest').addClass('hide');
        				exsoftProcessViewFunc.isApprove ? $('#processView_approve').removeClass('hide') : $('#processView_approve').addClass('hide');
        				exsoftProcessViewFunc.isApproveReject ? $('#processView_approveReject').removeClass('hide') : $('#processView_approveReject').addClass('hide');
        				exsoftProcessViewFunc.isContent ? $('#processView_content').removeClass('hide') : $('#processView_content').addClass('hide');
        				exsoftProcessViewFunc.isContent ? $('.requestApproval_wordcnts').removeClass('hide') : $('.requestApproval_wordcnts').addClass('hide');
            			
            			// TODO : 파일 관련 처리
        				
        				// 승인요청 목록 처리
        				exsoftProcessViewFunc.ui.printProcessSituation(processId);
            			
        			}else{
        				jAlert(data.message);
        			}
        			
        			
        		});
        		// 2. 문서정보 가져오기
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({doc_id:doc_root_id}, exsoft.contextRoot+"/document/documentDetail.do", null, function(data,param) {

					//exsoft.document.event.printPageList(data);//첨부파일
					//exsoft.document.event.printDocumentVO(data);//기본정보
        			var docVO = data.documentVO;
    					
        			$('#processView_info tr:nth-child(2)').find('span').text(data.folderPath); 		// 기본폴더
        			$('#processView_info tr:nth-child(10)').find('span').text(docVO.type_name);		// 문서유형
					
        			// 권한 Setting
					$("#processView_aclName").html(data.aclDetail.acl_name); //권한
					
					exsoft.util.table.tableDocumentAclItemPrintList('processView_acl', data.aclItemList);
					exsoft.util.table.tableDocumentAclItemPrintList('processView_extAcl', data.aclExItemList);
    			});	
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
        	approveAction : function(action_type) {
        		//exsoftProcessViewFunc.process_id APPROVEREQUEST|APPREVE|APPROVEREJECT
        		if(exsoftProcessViewFunc.isModify == true && $("#processWriteFrm").validation()){
        			var jsonObject = {actionType : action_type, 
        							  process_id : exsoftProcessViewFunc.process_id, 
        							  doc_root_id : exsoftProcessViewFunc.doc_root_id,
        							  content : $('#processView_content').text()};
	        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject, exsoft.contextRoot+'/process/processControl.do', null, function(data, param){
	        			if(data.result == 'true'){
	        				exsoft.util.layout.divLayerClose('coopUser_detail_wrapper','coopUser_detail');

	        				// 리스트 새로 고침, processList.jsp에 ID 정의 되어 있음
	        				console.log('++++++++++++++++++++++++: ' + $('#processDocGridList').length);
	        				if($('#processDocGridList').length > 0){
	        					exsoft.util.grid.gridRefresh('processDocGridList',exsoft.contextRoot+'/process/processList.do');
	        				}
	        					
	        			}
	        			
	        			jAlert(data.message);
	        		});
        		}        		
        	}
        },
        
        //5. 화면 UI 변경 처리
        ui : {
        	// 처리현황(xr_comment) 내용
        	printProcessSituation : function(process_id){
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({root_id:process_id}, exsoft.contextRoot+'/document/documentCommentList.do', null, function(data, param){
        			$('.approvalResult_cnt').text(data.records);
        			$('#processView_situation').empty();
        			
        			if(data.result == 'true'){
        				$(data.list).each(function(idx){
        					var liContent = '<li class="approvalResult_list">';
        					    liContent += '<p><span class="bold">'+this.creator_name +'</span>';
        					    liContent += '<span>' + this.create_date + '</span></p>'; 
        					    liContent += '<p>' + this.content+'</p></li>'
        					$('#processView_situation').append(liContent);
        				});
        			}else{
        				$('#processView_situation').append('<li class="approvalResult_list"><p>등록된 데이터가 없습니다.</p></li>');
        			}
        		});
        	}

        },

        //6. callback 처리
        callback : {
                
        },
        
        //7. doFunction
        doFunction : {
        	// 협업자 정보 set
        	setExecutorName : function(idx, name){
        		var currentText = $('#processView_info tr:nth-child('+idx+')').find('span').text();
        		$('#processView_info tr:nth-child('+idx+')').find('span').text(function(idx){
        			var tempStr = currentText + ',' + name;
        			var firstComma = tempStr.substring(0,1);
        			if(firstComma == ','){
        				tempStr = tempStr.substring(1);
        			}
					return tempStr;
				});
        	},
        	
        	// 승인자, 수신자 현황 툴팁 set
        	setTooltip : function(id, tooltipId, processVo, type){
				
				var tooltip = '';
				var count = 0;
				var status = '';
				var executorArray = new Array();
			
				if(Constant.PROCESS.TYPE_APPROVER == type){
					count = processVo.approval_count;
					status = '승인완료';
					executorArray = processVo.approval_list;
				}else{
					count = processVo.receiver_count;
					status = '열람완료';
					executorArray = processVo.receiver_list;
				}
				
				if(executorArray.length > 0 > 0){
        			$(exsoft.util.common.getIdFormat(id)).text('['+count+']');
        			var tempList = executorArray;
					    $.each(tempList, function(){
					    	var excutors = this.split('|');
						    if(excutors[1] == status){
							    tooltip += '<p>● '+excutors[0]+' : '+excutors[1]+'</p>';
						    }else{
							    tooltip += '<p>○ '+excutors[0]+' : '+excutors[1]+'</p>';										   
						    }
					    });
					
					    $(exsoft.util.common.getIdFormat(tooltipId)).html(tooltip);
				}
			},
			
			// 현 단계 및 사용자에 맞는 추가기능 set
			// isModify,isDelete,isApproveRequest,isApprove,isApproveReject,isFileModify,isContent
        	setProcessRole : function(isModify,isDelete,isApproveRequest,isApprove,isApproveReject,isFileModify,isContent){
        		exsoftProcessViewFunc.isModify = isModify != undefined ? isModify : false;							// 속성수정
				exsoftProcessViewFunc.isDelete = isDelete != undefined ? isDelete : false;							// 삭제
				exsoftProcessViewFunc.isApproveRequest = isApproveRequest != undefined ? isApproveRequest : false;	// 승인요청
				exsoftProcessViewFunc.isApprove = isApprove != undefined ? isApprove : false;						// 승인
				exsoftProcessViewFunc.isApproveReject = isApproveReject != undefined ? isApproveReject : false;		// 반려
				exsoftProcessViewFunc.isFileModify = isFileModify != undefined ? isFileModify : false;				// 파일수정
				exsoftProcessViewFunc.isContent = isContent != undefined ? isContent : false;						// 내용 사용 여부
        	},
        	
        	
        }
		
}