var selectFavoriteFolderWindow = {
	callback : null,
	isValidation : false,
	srcFolderInfo : null,
	
	open : function() {
		exsoft.util.layout.divLayerOpen("favorite_choose_wrapper", "favorite_choose");
	},
	
	close : function() {
		exsoft.util.layout.divLayerClose("favorite_choose_wrapper", "favorite_choose");
	},
	
	tree : {
		favoriteTree : null
	},
	
	treeFunctions : {
		
		initTree : function() {
			var treeOption = {
					context : exsoft.contextRoot,
					url : "/folder/favoriteFolderList.do",
					onlyVirtual : selectFavoriteFolderWindow.srcFolderInfo.only_virtual,
					divId : "#selectFavoriteTreeDiv"
			};
			
			if (selectFavoriteFolderWindow.tree.favoriteTree == undefined) {
				selectFavoriteFolderWindow.tree.favoriteTree = new XFTree(treeOption);
				selectFavoriteFolderWindow.tree.favoriteTree.onlyVirtual = selectFavoriteFolderWindow.srcFolderInfo.only_virtual;
				selectFavoriteFolderWindow.tree.favoriteTree.init();
			} else {
				selectFavoriteFolderWindow.tree.favoriteTree.refresh();
			}
		}
	},
	
	/********************************************************************************** 
	 *  팝업 호출 메서드
	 **********************************************************************************
	 * isValidation 사용 시 필수값 표시 == (#)
	 * srcFolder : {
	 *	 folder_id(#) : 이동시킬 폴더 ID 
	 *	 folder_nm : 이동시킬 폴더 이름
	 *	 parent_id(#) : 이동시킬 폴더의 부모 ID
	 *	 mode :  
	 *	 	"SELECT" : 단순 조회 '하위폴더 포함 체크박스 없음', 
	 *	 	"ADD_FAVORITE" :  폴더 즐겨찾기 추가시 사용 '하위폴더 포함 체크박스 있음'
	 *	 only_virtual :
	 *	 	"Y" : 가상 폴더만 조회
	 *	 	"N" : 모든(가상, 즐겨찾기) 폴더를 조회
	 *	 }
	 * isValidation : 
	 *		 true : 폴더 이동 시 사용.
	 **********************************************************************************/
	init : function(srcFolder, isValidation, callback) {
		selectFavoriteFolderWindow.srcFolderInfo = srcFolder;
		selectFavoriteFolderWindow.isValidation = isValidation;
		selectFavoriteFolderWindow.callback = callback;
		
		selectFavoriteFolderWindow.ui.checkMode();
		selectFavoriteFolderWindow.treeFunctions.initTree();
		selectFavoriteFolderWindow.open();
	},
	
	ui : {
		checkMode : function() {
			if (selectFavoriteFolderWindow.srcFolderInfo != "ADD_FAVORITE") {
				$("#includeSubFolderDiv").hide();
			} else {
				$("#includeSubFolderDiv").show();
			}
		}
	},
	
	event : {
		submit : function() {
			var _src = selectFavoriteFolderWindow.srcFolderInfo;
			var _tar = selectFavoriteFolderWindow.tree.favoriteTree;
			
			var targetNodeId = _tar.getCurrentNodeId();
			var parentIdList = _tar.getCurrentNodeFullPathIds();
			var checkResult = false;
			
			// "즐겨찾기 폴더 이동"일 경우에만 Validation 검증
			if (selectFavoriteFolderWindow.isValidation) {
				
				// 1. 이동 대상 폴더가 현재 폴더와 동일한 위치인지
				if (targetNodeId == _src.parent_id) {
					jAlert("동일한 위치로 이동할 수 없습니다.");
					return;
				}
				
				// 2. 이동 대상 폴더가 자기 자신이거나 , 자신의 하위 폴더인지
				$(parentIdList).each(function(index) {
					if (this == _src.folder_id ) {
						jAlert("현재 폴더 및 현재 폴더 하위로 이동할 수 없습니다.");	
						checkResult = true;
						return false;
					}
				});
				
				if (checkResult) {
					return;
				}
				
			}
			
			_src.target_folder_id = _tar.getCurrentNodeId();
			_src.targetNode = _tar.getCurrentNode();
			_src.includeSubFolder = $("#selectFavoriteIncludeSubFolderChk").is(":checked");
			
			selectFavoriteFolderWindow.callback(_src);
			selectFavoriteFolderWindow.close();
		}
	}
}