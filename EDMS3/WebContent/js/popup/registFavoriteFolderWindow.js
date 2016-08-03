var favoriteFolderWindow = {
	wrapperClass : "favorite_register_wrapper",
	layerClass : "favorite_register",
	callback : null,
	target_folder_id : "",
	folder_nm : "",
	folder_id : "",
	type : "",
	is_virtual : "",
	
	// 폼 초기화(생성 / 수정)
	initForm : function(node, folderId) {
		if (folderId === undefined) {
			// 등록시
			this.target_folder_id = node.id;
			this.is_virtual = "Y";
			
		} else if (folderId !== undefined) {
			// 이름변경시
			$("#fav_fol_name").val(node.text);
			this.folder_id = node.id;
		}
	},
	
	// 화면 Event 처리
	event : {
		// 폴더 등록/수정
		submit : function() {
				
			var postdata = {
					folder_id : favoriteFolderWindow.folder_id,
					target_folder_id : favoriteFolderWindow.target_folder_id,
					folder_nm : $("#fav_fol_name").val(),
					is_virtual : favoriteFolderWindow.is_virtual,
					type : favoriteFolderWindow.type
			}
			exsoft.util.ajax.ajaxDataFunctionWithCallback(postdata, exsoft.contextRoot+"/folder/favoriteControl.do", "", function(data) {
				
				exsoft.util.layout.divLayerClose(favoriteFolderWindow.wrapperClass, favoriteFolderWindow.layerClass);
				$("#fav_fol_name").val("");
				favoriteFolderWindow.callback();
			});
			
		}
	},
	
	//취소버튼 클릭시
	cancelButtonClick : function() {
		exsoft.util.layout.divLayerClose(favoriteFolderWindow.wrapperClass, favoriteFolderWindow.layerClass);
	}
}