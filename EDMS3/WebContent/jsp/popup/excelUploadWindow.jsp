<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	엑셀 일괄 업로드
 -->
<div class="batch_upload_wrapper hide"></div>
<div class="batch_upload hide">
	<div id="groupUploadView" class="window_title">
		부서 일괄 업로드
        <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="batch_upload_cnts">
    	 <div class="inputfileimage">
    		<input type="file" name="Filedata" id="upFile" class="file_input_hidden" onchange="javascript:$('#groupFilePath').val(this.value);">
    		<div id="progress">
	        	<div class="bar" style="width: 0%;"></div>
	    	</div>
    	</div>
    </div>
   	<div class="btnGrp">
   		<button type="button" class="batch_upload_btn" onclick="javascript:exsoft.util.layout.divLayerClose('batch_upload_wrapper', 'batch_upload');">닫기</button>
   	</div>
</div>

<div class="user_batchUpload_wrapper hide"></div>
<div class="user_batchUpload hide">
	<div class="window_title">
		사용자 일괄 업로드
        <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="batch_upload_cnts">
    	 <div class="inputfileimage">
    		<input type="file" name="Filedata" id="userUpFile" class="file_input_hidden" onchange="javascript:$('#userFilePath').val(this.value);">
    		<div id="progressUser">
	        	<div class="bar" style="width: 0%;"></div>
	    	</div>
    	</div>
    </div>
   	<div class="btnGrp">
   		<button type="button" class="user_batchUpload_btn" onclick="javascript:exsoft.util.layout.divLayerClose('user_batchUpload_wrapper', 'user_batchUpload');">닫기</button>
   	</div>
</div>


<script type="text/javascript" src="${contextRoot}/js/popup/excelUploadWindow.js"></script>
<script type="text/javascript">
jQuery(function(){	

	// 그룹일괄등록처리
	$('#upFile').fileupload({        
        url: "${contextRoot}/admin/groupUpload.do",
        dataType: 'json',
        replaceFileInput: false,
        add: function(e, data){
        	
            var uploadFile = data.files[0];

          	if (!(/xls|XLS/i).test(uploadFile.name)) {
          		jAlert('xls 만 가능합니다', "부서 일괄 업로드", 0);
                return false;
            }            
            else if (uploadFile.size > 5000000) { // 5mb
            	jAlert('파일 용량은 5메가를 초과할 수 없습니다.', "부서 일괄 업로드", 0);
            	return false;
            }
                        
            data.submit();
        },
        progressall: function(e,data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .bar').css('width',progress + '%');
        },
        done: function (e, data) {
        	if(data.result.retype == "fail") {
        		$("#upFile").val("");
        		jAlert(data.result.message, "부서 일괄 업로드", 0);      
        	}else {
        		jAlert(data.result.retype, "부서 일괄 업로드", 0);        
        		exsoft.util.layout.divLayerClose('batch_upload_wrapper', 'batch_upload');
        		groupManager.groupTree.refresh();
        	}
        },
        fail: function(){
            alert("서버와 통신 중 문제가 발생했습니다", "부서 일괄 업로드", 0);
        }
    });
	
	
	// 사용자일괄등록처리
	$('#userUpFile').fileupload({        
        url: "${contextRoot}/admin/userUpload.do",
        dataType: 'json',
        replaceFileInput: false,
        add: function(e, data){
        	
            var uploadFile = data.files[0];

          	if (!(/xls|XLS/i).test(uploadFile.name)) {
          		jAlert('xls 만 가능합니다', "사용자 일괄 업로드", 0);
                return false;
            }            
            else if (uploadFile.size > 5000000) { // 5mb
            	jAlert('파일 용량은 5메가를 초과할 수 없습니다.', "사용자 일괄 업로드", 0);
            	return false;
            }
                        
            data.submit();
        },
        progressall: function(e,data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progressUser .bar').css('width',progress + '%');
        },
        done: function (e, data) {
        	if(data.result.retype == "fail") {
        		$("#userUpFile").val("");
        		jAlert(data.result.message, "사용자 일괄 업로드", 0);      
        	}else {
        		jAlert(data.result.retype, "사용자 일괄 업로드", 0);        
        		exsoft.util.layout.divLayerClose('user_batchUpload_wrapper', 'user_batchUpload');
        		userManager.groupTree.refresh();
        	}
        },
        fail: function(){
            alert("서버와 통신 중 문제가 발생했습니다", "사용자 일괄 업로드", 0);
        }
    });
	
});
</script>