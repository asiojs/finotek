var configFavoriteFolder = {
	favoriteFolderTreeForPopup : null,
	initFavoriteTreeForPopup : false,
	
	// TODO 정리 필요 start...

    // 0. 초기화
    init : {
    	initWindow : function() {
    		//즐겨찾기 구성 - 창 닫기
    	    $('.favorite_configChoose_close').bind("click", function(e){
    	    	e.preventDefault();
    	    	$(this).parents('.favorite_configChoose').addClass('hide');
    	    	$('.favorite_configChoose_wrapper').addClass('hide');
    	    });

    	    //즐겨찾기 구성 -  창 닫기 : 음영진 부분 클릭 시 닫기
    	    $('.favorite_configChoose_wrapper').bind("click", function(){
    	    	$(this).addClass('hide');
    	    	$('.favorite_configChoose').addClass('hide');
    	    });
    	    
    		if (configFavoriteFolder.favoriteFolderTreeForPopup == undefined) {
    			// 즐겨찾기 폴더 편집
    			treeOption = {
    					divId : "#favorite_folder_tree",
    					context : exsoft.contextRoot,
    					url : "/folder/favoriteFolderList.do",
    					contextAction : exsoftMypageFunc.event.treeFunctions.treeContextAction
    			};
    			configFavoriteFolder.favoriteFolderTreeForPopup = new XFTree(treeOption);
    			configFavoriteFolder.favoriteFolderTreeForPopup.template_context();
    			configFavoriteFolder.favoriteFolderTreeForPopup.isFavoriteFolder = true;
    			configFavoriteFolder.favoriteFolderTreeForPopup.init();
    			
    		}
    		configFavoriteFolder.open.layerOpen();
    	}
    },

    // 1. 팝업
    open : {
    	layerOpen : function() {
    		exsoft.util.layout.divLayerOpen("favorite_configChoose_wrapper", "favorite_configChoose");
    	}
    },

    //2. layer + show
    layer : {
    },

    //3. 닫기 + hide
    close : {
    },

    //4. 화면 이벤트 처리
    event : {
    	// 즐겨찾기 구성 - 저장버튼 클릭시
    	okButtonClick : function() {
    		exsoft.util.layout.divLayerClose("favorite_configChoose_wrapper", "favorite_configChoose");
    		exsoftMypageFunc.event.treeFunctions.getCurrentFavoriteTree().refresh();
    	},
    	
    	// 즐겨찾기 폴더 위로 이동 (형제 노드간 이동만 허가함)
    	fGoPrevNode : function() {
    		configFavoriteFolder.favoriteFolderTreeForPopup.moveToPrev();
    	},
    	
    	// 즐겨찾기 폴더 아래로 이동 (형제 노드간 이동만 허가함)
    	fGoNextNode : function() {
    		configFavoriteFolder.favoriteFolderTreeForPopup.moveToNext();
    	},
    	
    	getCurrentFavoriteTreeForPopup : function() {
    		return configFavoriteFolder.favoriteFolderTreeForPopup;
    	},
    	
    	// 즐겨찾기 폴더 새로고침
    	refreshTree : function (e, data) {
    		configFavoriteFolder.event.getCurrentFavoriteTreeForPopup().refresh();
    	}
    },

    //5. 화면 UI 변경 처리
    ui : {
    },

    //6. callback 처리
    callback : {
    }
	
}