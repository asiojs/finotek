/**
 * 공통으로 호출 되는 JS 및 CSS include 처리
 */

//windowType은 호출하는 페이지에서 넘겨줘야 함
if(windowType == 'userLayout'){

	// 사용자 공통화면
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jquery/jquery-ui.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/bxslider/jquery.bxslider.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/ddslick/jquery.ddslick.custom.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/cookie/jquery.cookie.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jalert/jquery.alerts.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/scrollbar/jquery.tinyscrollbar.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/json/json2.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jstree/dist/jstree.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/upload/jquery.uploadfile.3.1.10.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/util.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/tree.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/constant.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/layout/layout.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/databinder.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/validator.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/ecm.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/initbind.js"></script>');

	// jQgrid
	/*
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/js/i18n/grid.locale-kr.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/plugins/ui.multiselect.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/js/jquery.jqGrid.src.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/plugins/jquery.tablednd.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/plugins/jquery.contextmenu.js"></script>');
    $('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/plugins/jquery.searchFilter.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/src/grid.celledit.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/src/grid.subgrid.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/src/grid.treegrid.js"></script>');
	*/
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/js/i18n/grid.locale-kr.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/plugins/ui.multiselect.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/js/jquery.jqGrid.min.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/plugins/jquery.tablednd.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/plugins/jquery.contextmenu.js"></script>');
    $('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/plugins/jquery.searchFilter.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/src/grid.celledit.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/src/grid.subgrid.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/src/grid.treegrid.js"></script>');

	// CSS inculde
	$('head').append('<link href="'+exsoft.contextRoot+'/css/common/ecm.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/common/reset.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/themes/jquery-ui.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/jalert/jquery.alerts.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/ddslick/jquery.ddslick.custom.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/bxslider/jquery.bxslider.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/js/plugins/jstree/dist/themes/default/style.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/upload/uploadfile.3.1.10.css" rel="stylesheet" type="text/css">');

	// jQgrid CSS include
	//$('head').append('<link href="'+exsoft.contextRoot+'/js/plugins/jqgrid/css/ui.jqgrid.css" rel="stylesheet" type="text/css">');
	//$('head').append('<link href="'+exsoft.contextRoot+'/js/plugins/jqgrid/plugins/ui.multiselect.css" rel="stylesheet" type="text/css">');

	$('head').append('<link href="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/css/ui.jqgrid.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/plugins/ui.multiselect.css" rel="stylesheet" type="text/css">');

} else if(windowType == 'userConfig'){

	// 사용자 환경설정
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/ddslick/jquery.ddslick.custom.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jalert/jquery.alerts.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/common.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/util.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/databinder.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/validator.js"></script>');

	// CSS inculde
	$('head').append('<link href="'+exsoft.contextRoot+'/css/common/ecm_pop.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/common/reset.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/ddslick/jquery.ddslick.custom.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/jalert/jquery.alerts.css" rel="stylesheet" type="text/css">');

} else if(windowType == 'noteMain'){

	// 쪽지 메인 화면
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/ddslick/jquery.ddslick.custom.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/util.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/cookie/jquery.cookie.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jalert/jquery.alerts.js"></script>');

	// CSS inculde
	$('head').append('<link href="'+exsoft.contextRoot+'/css/common/ecm_note.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/common/reset.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/themes/jquery-ui.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/jalert/jquery.alerts.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/ddslick/jquery.ddslick.custom.css" rel="stylesheet" type="text/css">');

} else if(windowType == 'noteUserSelect'){
	// 쪽지 사용자 선택 화면
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/ddslick/jquery.ddslick.custom.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/cookie/jquery.cookie.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jalert/jquery.alerts.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/util.js"><\/script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'"><\/script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'"><\/script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'"><\/script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'"><\/script>');

	// CSS inculde
	$('head').append('<link href="'+exsoft.contextRoot+'/css/common/ecm_note.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/common/reset.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/themes/jquery-ui.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/jalert/jquery.alerts.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/ddslick/jquery.ddslick.custom.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'" rel="stylesheet" type="text/css">');

} else if(windowType == 'adminLayout'){

	// 관리자 고통화면
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jquery/jquery-ui.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/bxslider/jquery.bxslider.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/ddslick/jquery.ddslick.custom.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/cookie/jquery.cookie.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jalert/jquery.alerts.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/upload/jquery.fileupload.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/scrollbar/jquery.tinyscrollbar.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/json/json2.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jstree/dist/jstree.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/util.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/admin/ecm.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/tree.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/constant.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/layout/adminLayout.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/databinder.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/common/validator.js"></script>');

	// jQgrid
	/*
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/js/i18n/grid.locale-kr.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/plugins/ui.multiselect.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/js/jquery.jqGrid.src.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/plugins/jquery.tablednd.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/plugins/jquery.contextmenu.js"></script>');
    $('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/plugins/jquery.searchFilter.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/src/grid.celledit.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/src/grid.subgrid.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid/src/grid.treegrid.js"></script>');
*/
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/js/i18n/grid.locale-kr.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/plugins/ui.multiselect.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/js/jquery.jqGrid.min.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/plugins/jquery.tablednd.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/plugins/jquery.contextmenu.js"></script>');
    $('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/plugins/jquery.searchFilter.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/src/grid.celledit.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/src/grid.subgrid.js"></script>');
	$('head').append('<script type="text/javascript" src="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/src/grid.treegrid.js"></script>');

	// CSS inculde
	$('head').append('<link href="'+exsoft.contextRoot+'" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/common/admin/ecm.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/common/reset.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/themes/jquery-ui.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/jalert/jquery.alerts.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/ddslick/jquery.ddslick.custom.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/css/plugins/bxslider/jquery.bxslider.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/js/plugins/jstree/dist/themes/default/style.css" rel="stylesheet" type="text/css">');

	// jQgrid CSS include
	//$('head').append('<link href="'+exsoft.contextRoot+'/js/plugins/jqgrid/css/ui.jqgrid.css" rel="stylesheet" type="text/css">');
	//$('head').append('<link href="'+exsoft.contextRoot+'/js/plugins/jqgrid/plugins/ui.multiselect.css" rel="stylesheet" type="text/css">');

	$('head').append('<link href="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/css/ui.jqgrid.css" rel="stylesheet" type="text/css">');
	$('head').append('<link href="'+exsoft.contextRoot+'/js/plugins/jqgrid_old/plugins/ui.multiselect.css" rel="stylesheet" type="text/css">');

}

