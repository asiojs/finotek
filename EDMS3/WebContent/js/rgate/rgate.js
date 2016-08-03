/**
 * RGATE 관련 스크립트
 */
var exsoftAdminRgateFunc = {

		contentType : "",

		init : {

			// 환경설정 초기 페이지
			initPage : function(contentType) {

				exsoftAdminRgateFunc.contentType = contentType;											// 현 페이지 정보

				exsoft.util.common.ddslick('#strIndex', 'rGate_policy', '', 98, function(divId){});		// SELECT BOX 플러그인 적용

			},


		},

		open : {

			// 부서,사용자 추가 :: 페이지별로 분기개발한다.
			addGroupWrite : function() {

				exsoft.util.layout.divLayerOpen('rGate_deptUserPolicy_wrapper','rGate_deptUserPolicy');

				// 각 페이지에 맞게 분기개발처리한다. TODO - 문서중앙화 고도화시 구현
			},

			// 확장자 추가 :: 구현시 삭제처리하며 전사추가 OR 정책수정시 바로 호출한다.
			addExtManager : function() {
				exsoft.util.layout.divLayerOpen('rGate_extension_wrapper','rGate_extension');
			},

			// 저장허용프로그램 목록 & 선택
			selectProcManager : function() {
				exsoft.util.layout.divLayerOpen('rGate_appList_wrapper','rGate_appList');
			},

			// 프로그램 추가
			addProcManager : function() {
				exsoft.util.layout.divLayerOpen('rGate_registApplication_wrapper','rGate_registApplication');
			},

			// 프로그램 추가
			selectExceptProcManager : function() {
				exsoft.util.layout.divLayerOpen('rGate_application_wrapper','rGate_application');
			},

			// IP목록관리
			selectIPList : function() {
				exsoft.util.layout.divLayerOpen('rGate_netList_wrapper','rGate_netList');
			},

			// IP추가
			addIP : function() {
				exsoft.util.layout.divLayerOpen('rGate_registIPAddr_wrapper','rGate_registIPAddr');
			},

			configPasswd : function() {
				exsoft.util.layout.divLayerOpen('rGate_setPwd_wrapper','rGate_setPwd');
			}


		},

		layer : {

		},

		close : {

		},

		event : {

		},

		ui : {


		},

		callback : {

		}

}