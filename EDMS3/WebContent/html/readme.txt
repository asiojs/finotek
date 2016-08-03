2015-04-24
  /html/user_add1.html
  /html/user_add2.html
  /html/user_add3.html
  -> 부서/사용자/구성원 추가 각각 레이아웃으로 정리
  
  
2015-04-16
  /js/plugins/upload/jquery.uploadfile.3.1.10.js :: line 637
    - 스크립트 width 지정 값 수정(원본은 그 뒤에 주석으로 남겨놓음)



2015-04-13
  /html/admin/rgate/*.html
    - 레이어 팝업 중 사용자 선택부분 위치 수정 
    
  /jsp/selectAccessorWindow.jsp
    - 레이아웃 틀어진 부분 수정

2015-04-10
  /html/admin
    - /doc_config.html
    - /layout.html
    - /rgate_config.html
    - /sys_config.html			: 위 4개 파일은 변동사항 없음
    
    - /document					: 문서관리
      - /bin.html				: 휴지통
      - /config.html				: 문서유형 관리
      - /dup_file.html			: 중복파일 관리
      - /expired.html			: 만기 문서 관리
      - /folder.html				: 폴더 관리
      - /mass_audit.html		: 대량문서 열람 감사 관리
      - /transfer_owner.html	: 소유권 이전 관리
      
    - /rgate					: rGate
      - /app_folder.html		: 프로그램별 작업폴더 관리
      - /application.html		: 저장 허용 프로그램 관리
      - /client_passwd.html		: 클라이언트 삭제 비밀번호 관리
      - /extension.html			: 저장금지 확장자 관리
      - /local.html				: 로컬 저장 허용 관리
      - /network.html			: 네트워크 접속 허용 관리
      - /usb.html				: USB 저장 허용 관리
      
    - /system					: 시스템 관리
      - /acl.html				: 권한(ACL) 관리
      - /config.html				: 환경설정 관리
      - /group.html				: 그룹 관리
      - /menu.html				: 메뉴 접근권한 관리
      - /user.html				: 사용자 관리

  /css/admin/ecm.css
    - 페이지 추가에 따른 css 추가
    
  /js/admin/ecm.js
    - 페이지 추가에 따른 코드 추가



2015-04-07
  /css/ecm.css
    - datepicker 테두리 추가
  /img/icon
    - 신규 아이콘 추가
    
  엑셀파일 psd / 퍼블리싱 작업 된 페이지 비교대조표 작성
   
2015-04-03
  /css/pluagin/bxslider/jquery.bxslider.css
    - 추가
  /html/main.html
    - 추가
  
  /js/plugins/jqgrid/js/jquery.jqGrid.src.js
    - 4009 라인 수정(원본은 주석처리)
  /js/plugins/jqgrid/css/jquery.jqGrid.css
    - 85라인 (loading) 수정
  아이콘파일 추가

2015-04-02
  /css/plugin/ddslick/jquery.ddslick.custom.css
    - 점화 시 생기는 css 테두리 수정
  /css/common/ecm.css
    - 컨텐츠 내 탑 검색창 폰트 색상 수정
  /css/plugins/themes/jquery-ui.css
    - grid 관련 수정
    
  /js/plugins/jqgrid/css/ui.jqgrid.css
    - grid 관련 수정
    - jquery-ui 그루핑 관련 css는 여기에 포함 되어있습니다.
    
  /js/common/include.js
    - jqgrid.min.js -> jqgrid.src.js로 수정
      - jquery.jqGrid.src.js 수정 내용
      line 2883 : comment /* class 부분이 추가되었습니다 */
      line 2884 : class 부분이 추가되었습니다
      before :: .append($('<div style="position:relative;'+(isMSIE && $.jgrid.msiever() < 8 ? "height:0.01%;" : "")+'"></div>').append('<div></div>').append(this)) 
      after  :: .append($('<div class="ui-jqgrid-bdiv-subwrapper" style="position:relative;'+(isMSIE && $.jgrid.msiever() < 8 ? "height:0.01%;" : "")+'"></div>').append('<div></div>').append(this)) 
    
2015-04-01
  /css/common/ecm.css
    - .loading_wrapper background color 변경
    - <div class="depth_navi"> 밑의 태그 변경에 따른 스타일 변경
    - 협업관리 트리메뉴 css 추가
    - 퀵메뉴 화살표 위치 조정
    
  /jsp/processMenu.jsp
    - 협업관리 트리메뉴 css 추가
  /js/common/ecm.js 
    - 협업관리 fold/expand 추가
    
    
2015-03-31
  /html/admin/*
    - footer 수정
  /css/common/admin/ecm.css
    - footer css 수정

  /jsp/layout/userTopMenu.jsp
    - <div class="top_menu_close"> ~ scriptlet 주석 처리(X 버튼 이미지입니다. 삭제대신에 주석처리 하였습니다.)
    
    
2015-03-30
  /html/table_test.html
  /html/table_test1.html 
    - 테이블 구조 변경 샘플파일 입니다.
  /html/doc_integratedsearch.html
    - 새로운 테이블 레이아웃으로 변경
  /html/doc_mydocs7_tempbox.html
    - 작업카트제외, 다운로드, 즐겨찾기 추가 이벤트 추가
  /html/login.html
    - 관리자 로그인 버튼 클릭 시 관리자 로그인 레이어 팝업 출현
        
  /css/common/ecm.css
    - 신규 테이블 레이아웃 추가
    - ** 테이블 레이아웃에서 width값을 지정하는 class같은 것이 없으면 width가 정해진 요소들을 제외하고 1/n으로 width를 나눔 **
  /js/common/ecm.js
    - 테이블 레이아웃 변경에 따른 jquery 이벤트 추가 
    
  /html/admin/*
    - 신규추가
  /css/common/admin/*
    - 신규추가
  /js/common/admin/*
    - 신규추가
  
2015-03-27
  /jsp/myPage/myPageDocList.jsp - 수평, 수직스크롤 안의 내용 추가
  /html/layout.html - 수평,수직 스크롤 안의 내용 추가
  /html/doc_detail2.html 
    - 문서 상세조회 창 너비 조정
    - 협업 탭 메뉴 추가
  /css/common/ecm.css
    - 상기 변동사항 반영
    - 즐겨찾기 선택 / 즐겨찾기 선택 설정 z-index 변경
    
    
2015-03-26
  /css/common/ecm.css 
    - 문서등록 > 관련문서 부분 수정
    - 문서수정 > css 추가
    - 내 소유문서 컨텍스트 메뉴 
       - 소유권 이전, 소유권 전체 이전 삭제에 따른 주석처리 
    - 내 소유문서 > 문서 등록
       - css 추가 
  /html/doc_modify.html
    - 클래스명 변경
  /html/doc_mydocs2.html
    - 나의 문서 > 문서 등록창 추가
    - 나의 문서 > 문서 수정창 추가
  
  /html/doc_integeraedsearch.html
    - 좌측메뉴, 토글버튼 주석처리(삭제)
    
      
  /js/common/ecm.js
    - 문서수정 클래스로 창 닫기, 음영부분으로 창 닫기 추가
    - 내 소유문서 > 문서 등록창 추가로 인한 창 닫기, 음영부분으로 창 닫기 추가
    
    
    
2015-03-25
  /html/pop_myNote.html - 쪽지관리 팝업창
    - 윈도우 사이즈 : 730 x 690px(padding 포함)
    - 사용자 선택 시 : 920 x 630px(padding 포함)
  /css/common/ecm_note.css - ecm.css 부분에서 쪽지 쓰기 관련 부분만 추출
  /js/common/ecm_note.js - ecm.js 부분에서 쪽지 쓰기 관련 부분만 추출
  /js/common/ecm.js - 상기 이유로 인하여 쪽지 쓰기 부분이 주석 처리(제외) 되었습니다.
  /jsp/layout/userHeader.jsp - 환경설정 옆에 드롭다운 화살표 제거

2015-03-24
  /jsp/layout/myPageMenu.jsp 수정  
  /jsp/layout/treeMenu.jsp 수정  
  /jsp/layout/statisticsMenu.jsp 수정(통계 세부 리스트 펼침/접힘 기능 추가)
    이로 인하여
    - /img/icon/tree_fold.png 변경
    - /img/icon/tree_expand.png 추가
    - /js/common/ecm.js 변경
    - /css/common/ecm.css 변경
    
  /js/common/ecm.js 수정
    - layer popup 표시되는 위치 조절  

2015-03-23 
html 태그 내 id => class 변경
(* datepicker, dropdown 제외)

- 파일
  /jsp/layout/staticsMenu.jsp 수정
  /jsp/statistics/loginStatistics.jsp 수정

2015-03-19
- 파일 
  - doc_coop.html : 				업무 상세조회		- 작성단계
  - doc_coop_approval.html : 	업무 상세조회 		- 승인단계
  - doc_coop_supplement.html : 	업무 상세조회 		- 보완단계
  - doc_coop_complete.html : 	업무 상세조회		- 완료단계
  - doc_detailsearch.html :			탑메뉴 상세검색
  - doc_integratedsearch.html :	통합검색			
  - doc_mydocs2.html :			나의문서			- 나의 문서 트리메뉴가 html코드와 css로 정의되어 있습니다.
  														- div id="tbody" 태그 부분 확인 부탁 드립니다.
  														(기존 : 웹 페이지상 thead tbody 합쳐서 scrolling -> 이후 : tbody 부분만 scrolling)
  - doc_statics.html : 				통계
  - error.html : 					에러페이지
  - myNote.html : 					나의 쪽지			- 쪽지관리

