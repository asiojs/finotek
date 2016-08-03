/**
 * 협업 등록/수정 JavaScript
 */
var exsoftProcessWrite = {
		binder : new DataBinder("#processWrite"),
		coworkList : {},
		wFileUploadJsonArr : new Array(),
		actionType : "C",
		defaultDocType : null,

		// 0. 초기화
        init : {
        	// 협업 등록
        	initProcessWrite : function(){
        		exsoftProcessWrite.binder.set("actionType", Constant.ACTION_CREATE);
        		exsoftProcessWrite.actionType = 'C'
        		// 문서유형 select-box
        		exsoftProcessWrite.init.initDdslick();
        		
        		$("#processWrite").validation.clearShadowBox();
        		exsoft.util.table.tableDocumentAclItemPrintList('processWrite_acl', null);
        		exsoft.util.table.tableDocumentAclItemPrintList('processWrite_extAcl', null);

        		exsoftProcessWrite.wFileUploadJsonArr = new Array();
        		exsoftProcessWrite.coworkList = {};
        		
        		//최근 업무 목록 표시
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(null,exsoft.contextRoot + '/process/processRecentlyList.do',null,function(data, param){
        			var $ul = $('.coop_recent_wrap ul');
        			$ul.empty();
        			$(data.list).each(function(idx){
        				var name = this.name.length > 15 ? this.name.substr(0,15)+'...' : this.name;
        				var strHtml = '';
        				strHtml += '<li id="'+this.recently_id+'">';
        				strHtml += '<a href="javascript:exsoftProcessWrite.event.setInfoByRecent(\''+this.process_id+'\');">'+name+'</a>'
        				strHtml += '<a href="javascript:exsoftProcessWrite.event.deleteRecentRow(\''+this.recently_id+'\');" class="coop_recent_del"><img src="'+exsoft.contextRoot+'/img/icon/recent_doc_del.png"></a>'
        				strHtml += '</li>';
        				
        				$ul.append(strHtml);
        			});
        		});
        		
        		// 파일 관련
        		exsoft.common.file.init.initSettings('processfileuploader', exsoftProcessWrite.callback.fileupload);
        		
        		// 초기화 작업
        		exsoftProcessWrite.binder.set("requestorName", exsoft.user.user_name);
        		exsoftProcessWrite.binder.set("requestorId", exsoft.user.user_id);
        		exsoftProcessWrite.binder.set("actionType", Constant.ACTION_CREATE);
        		
        		// 문서유형 기본값으로 설정 한다.
        		if(exsoftProcessWrite.defaultDocType != null){
        			exsoftProcessWrite.doFunction.setExtendTypeAttrItem(exsoftProcessWrite.defaultDocType);
        			exsoftProcessWrite.binder.set("doc_type", exsoftProcessWrite.defaultDocType);
        			$('#processWrite_docType').ddslick('enable');  // 문서유형 selectbox 선택 가능하게 변경
        		}
        		
        		exsoftProcessWrite.binder.bindingElement(); // data-bind 전체 bind
        	},
        	
        	// 협업 수정
        	initProcessModify : function(){
        		exsoftProcessWrite.binder.set("actionType", Constant.ACTION_UPDATE);
        		exsoftProcessWrite.actionType = 'U';
        	},
        	
        	/**
    		 * select box :: ddslick 사용
    		 */    		
    		initDdslick : function(type){
    			//검색 selectbox		
    			exsoft.util.common.ddslick('#processWrite_docType', 'srch_type1', 'doc_type', 85, function(divId, selectedData){
    				exsoftProcessWrite.binder.set("doc_type", selectedData.selectedData.value);
    				// 최초 default값 set :: 용도는 등록창 닫고 새로 열었을 경우 문서유형 기본값으로 변경
    				if(exsoftProcessWrite.defaultDocType == null){
    					exsoftProcessWrite.defaultDocType = selectedData.selectedData.value;
    				}
    				//문서유형에 맞는 확장 속성을 표시 한다.
    				exsoftProcessWrite.doFunction.setExtendTypeAttrItem(selectedData.selectedData.value);
    				
    			});	// 문서유형 selectbox
    		},
        },
        
        // 1. 팝업
        open : {
        	// 협업자 팝업
        	processCoworkWindow : function(){
        		exsoftProcessCoworkWindow.init.initProcessCoworkWindow(exsoftProcessWrite.coworkList, exsoftProcessWrite.callback.processCoworkWindow);
        	},
        	
        	// 기본폴더 선택
			selectFolderWindow : function() {
				selectSingleFolderWindow.init(exsoftProcessWrite.callback.selectFolderWindow);
			},
        },
        
        //2. layer + show
        layer : {
        	
        },
        
        //3. 닫기 + hide
        close : {
        	layerClose : function(isFileDelete){
        		if(true){
        			// exRep ECM에 등록된 물리적 파일 삭제 대상으로 등록
        			exsoftProcessWrite.doFunction.deleteUploadFile()
        		}
        		
        		// fileupload plug-in 목록 초기화
        		exsoft.common.file.prototype.wUploadObj.cancelAll();
        		exsoft.util.layout.divLayerClose('coop_register_wrapper','coop_register');
        	},
        },
        
        //4. 화면 이벤트 처리
        event : {
        	// 최근 업무 등록  목록에서 선택 시 기본 정보 set
        	setInfoByRecent : function(process_id){
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({process_id : process_id},exsoft.contextRoot + '/process/selectProcessRecently.do',null,function(data, param){
        			if(data.result == 'true'){
        				// 업무명
        				exsoftProcessWrite.binder.set("name", data.processName);
        				
        				// 기본폴더
        				exsoftProcessWrite.binder.set("full_path", data.full_path);
                		exsoftProcessWrite.binder.set("folder_id", data.folder_id);
                		exsoftProcessWrite.binder.set("map_id", data.map_id);
                		exsoftProcessWrite.binder.set("acl_id", data.acl_id);
                		
                		// 권한 셋팅
                		exsoftProcessWrite.doFunction.setAclItem(data.acl_id);
        				
        				// 협업자 set
        				var coworkerObj = {};        				
        				coworkerObj.authorList = new Array();
        				coworkerObj.coauthorList = new Array();
        				coworkerObj.approverList = new Array();
        				coworkerObj.receiverList = new Array();
        				
        				$(data.list).each(function(idx){
        					switch (this.type) {
							case Constant.PROCESS.TYPE_AUTHOR:coworkerObj.authorList.push(this);break;
							case Constant.PROCESS.TYPE_COAUTHOR:coworkerObj.coauthorList.push(this);break;
							case Constant.PROCESS.TYPE_APPROVER:coworkerObj.approverList.push(this);break;
							case Constant.PROCESS.TYPE_RECEIVER:coworkerObj.receiverList.push(this);break;
							default:break;
							}
        				});
        				
        				exsoftProcessWrite.callback.processCoworkWindow(coworkerObj);
        			}else{
        				jAlert(data.message);
        			}
        		});
        	},
        	
        	// 최근 업무 등록 목록 삭제
        	deleteRecentRow : function(recently_id){
        		exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({recently_id : recently_id},exsoft.contextRoot + '/common/deleteRecently.do',null,function(data, param){
        			if(data.result == 'true'){
        				$('.coop_recent_wrap ul').find(exsoft.util.common.getIdFormat(recently_id)).remove();
        			}else{
        				jAlert(data.message);
        			}
        		});
        	},
        	
        	// 문서 권한 변경
        	chagneDocumentAcl : function() {
        		selectAclWindow.initDocument(""/*AclId*/, Constant.ACL.TYPE_DOC, "DOC000000033756"/*docId*/,exsoftProcessWrite.callback.selectAcl);
        	},
        	
        	submit : function(){
        		console.log("[stephan][fileCounter]"+exsoft.common.file.prototype.wUploadObj.fileCounter);
        		console.info(exsoftProcessWrite.binder.getDataToJson());
        		
        		if ($("#processWrite").validation()) {
        			/**********************************************************************
            		// fileCounter :: 업로드 영역에 있는 파일 수
            		// 1 : 첨부파일 없는 문서등록 처리
            		// 2이상 : 첨부파일 업로드 후 문서등록 처리
            		// upCounter :: 대상 파일 중 업로드 성공한 파일 수
            		**********************************************************************/
        			if(exsoft.common.file.prototype.wUploadObj.fileCounter == 1 ||
        					(exsoft.common.file.prototype.wUploadObj.fileCounter -1) == exsoft.common.file.prototype.wUploadObj.upCounter)	{
        			     				
        				// 1. 협업자 정보를 jsonArray에 담아서 서버 호출 한다.
                		exsoftProcessWrite.binder.setJsonArray("authorList", exsoftProcessWrite.coworkList.authorList);
                		exsoftProcessWrite.binder.setJsonArray("coauthorList", exsoftProcessWrite.coworkList.coauthorList);
                		exsoftProcessWrite.binder.setJsonArray("approverList", exsoftProcessWrite.coworkList.approverList);
                		exsoftProcessWrite.binder.setJsonArray("receiverList", exsoftProcessWrite.coworkList.receiverList);

                		var jsonObject = exsoftProcessWrite.binder.getDataToJson(); 
                		jsonObject.fileList = JSON.stringify(exsoftProcessWrite.wFileUploadJsonArr);
                		jsonObject.page_cnt = exsoft.common.file.prototype.wUploadObj.fileCounter - 1;
                		
                		console.info(exsoftProcessWrite.binder.getDataToJson());
                		
            			exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject ,exsoft.contextRoot + '/process/processControl.do',null,function(data, param){
                			if(data.result == 'true'){
                				 jAlert(data.message);
                				exsoftProcessWrite.close.layerClose(false);
                			}else{
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
        	},
        },

        //5. 화면 UI 변경 처리
        ui : {
        	getWorkerList : function(bindName, arrayObj){
        		var textList = '';
        		$(arrayObj).each(function(index){
        			textList += this.user_nm+';';
        		});
        		exsoftProcessWrite.binder.set(bindName, textList);
        	}
        		
        },

        //6. callback 처리
        callback : {
        	// 협업자 목록 set
        	processCoworkWindow : function(coworkerObj){
        		exsoftProcessWrite.coworkList.authorList = coworkerObj.authorList;
        		exsoftProcessWrite.coworkList.coauthorList = coworkerObj.coauthorList;
        		exsoftProcessWrite.coworkList.approverList = coworkerObj.approverList;
        		exsoftProcessWrite.coworkList.receiverList = coworkerObj.receiverList;
        		
        		//협업자 셋팅 필요
        		exsoftProcessWrite.ui.getWorkerList('coworkAuthor', exsoftProcessWrite.coworkList.authorList);
        		exsoftProcessWrite.ui.getWorkerList('coworkCoauthor', exsoftProcessWrite.coworkList.coauthorList);
        		exsoftProcessWrite.ui.getWorkerList('coworkApprover', exsoftProcessWrite.coworkList.approverList);
        		exsoftProcessWrite.ui.getWorkerList('coworkReceiver', exsoftProcessWrite.coworkList.receiverList);
        		
        	},

        	// 기본폴더 set
        	selectFolderWindow : function(nodeInfo) {
        		console.info(nodeInfo);
        		exsoftProcessWrite.binder.set("full_path", nodeInfo.full_path.join("/"));
        		exsoftProcessWrite.binder.set("folder_id", nodeInfo.id);
        		exsoftProcessWrite.binder.set("map_id", nodeInfo.mapId);
        		exsoftProcessWrite.binder.set("acl_id", nodeInfo.original.acl_id);
        		
        		// 문서유형 set
        		if(nodeInfo.original.is_type == 'ALL_TYPE'){
        			$('#processWrite_docType').ddslick('enable');
        		}else{
        			$('#processWrite_docType').ddslick('disable');
        			exsoftProcessWrite.binder.set("doc_type", nodeInfo.original.is_type);
    				
    				//문서유형에 맞는 확장 속성을 표시 한다.
    				exsoftProcessWrite.doFunction.setExtendTypeAttrItem(nodeInfo.original.is_type);
        		}
        		
        		
        		// 권한 셋팅
        		exsoftProcessWrite.doFunction.setAclItem(nodeInfo.original.acl_id);
			},
			
			// 파일 처리 callback
			fileupload : function(files,data,xhr){
				exsoftProcessWrite.doFunction.setUploadFile(data);
				// 전체 파일이 올라 갔을 경우
				if((exsoft.common.file.prototype.wUploadObj.fileCounter -1) == exsoft.common.file.prototype.wUploadObj.upCounter)	{	
					exsoftProcessWrite.event.submit();
				}
			},
			
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
			}
                
        },
        
        // 7. 내부 함수 처리
        doFunction : {
        	// exRep ECM에 파일 등록을 성공하였을 경우 후 처리
        	setUploadFile : function(data){
        		// 파일 업로드 성공 처리 :: 성공한 파일개수 증가 및 성공 값 array 담기
        		exsoftProcessWrite.wFileUploadJsonArr.push({orgFile:data.orgFile,contentPath:data.contentPath,fileSize:data.fileSize,volumeId:data.volumeId});
        		exsoft.common.file.prototype.wUploadObj.upCounter += 1;
        	},
        	
        	// 등록 취소 시 기존에 등록한 파일을 삭제 한다.
        	deleteUploadFile : function(){
        		console.log('[stephan][exsoftProcessWrite.wFileUploadJsonArr.length] : '+exsoftProcessWrite.wFileUploadJsonArr.length);
        		if(exsoftProcessWrite.wFileUploadJsonArr.length >0){
        			var jsonObject = {"fileList":JSON.stringify(exsoftProcessWrite.wFileUploadJsonArr)};
        			exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject, exsoft.contextRoot+"/common/fileDelete.do", null, function(){})
        		}
        	},
        	
        	// 폴더 기본 권한 set
        	setAclItem : function(acl_id){
        		exsoft.util.ajax.ajaxDataFunctionWithCallback({"acl_id" : acl_id}, exsoft.contextRoot+"/permission/aclItemList.do", "", function(data, acl_id) {
        			// 기본 접근자
        			exsoft.util.table.tableDocumentAclItemPrintList('processWrite_acl', data.list);
				})
        		
        	},
        	
        	// 문서 유형에 따른 확장속성 표시  click.ddslick
        	setExtendTypeAttrItem : function(selectValue){
        		var jsonObject = {"type_id":selectValue,"is_extended":"T"}; 	
				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject,exsoft.contextRoot+'/type/attrList.do', '#processWrite_docAttrView', function(data, tableId){
					// 확장속성 table을 그린다.
					exsoft.util.table.tableExtendTypeItemPrintList(tableId, data.list, exsoftProcessWrite.actionType);
					 if(data.records != 0){
						 $(tableId).removeClass('hide');
						 exsoftProcessWrite.binder.set("is_extended", 'T');
						 
						// table에 select box가 존재하면 ddslick을 해준다.
	    				var $extendType = $(tableId + ' tbody').find('input, select');
	    				$($extendType).each(function(idx){
	    					var name = $(this).attr('name');
	    					
	    					if($(this).is('select')){
	    						$(this).attr('id', name);
	    						$(this).attr('data-select', 'true');
	    						exsoft.util.common.ddslick(name,'srch_type1',name,80, function(divId, selectValue){
	    							exsoftProcessWrite.binder.set(name, selectValue);
	    						});
	    					}else{
	    						$(this).attr('data-bind', name);
	    					}		    					
	    				});
	    				exsoftProcessWrite.binder.bindingElement(); // data-bind 전체 bind
						 
					 }else{
						 $(tableId).addClass('hide');
						 exsoftProcessWrite.binder.set("is_extended", 'F');
					 }
					 
				}); // 확장속성 set
        	}
        	
        	
        },

}

