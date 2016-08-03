<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	jQuery(function() {
		 $('.scrollbar1').tinyscrollbar();
		 exsoftLayoutFunc.close.searchClose();
	});
</script>
		<div class="contents_full">
			<div class="cnts_integratedSrch">
				<div class="integratedSrch_lbl">SEARCH</div>
				<div class="integratedSrch_form_wrapper">
					<form class="cnts_srch_form" method="post">
						<button type="button" id="" name="" class="doc_cabinet_btn">문서함구분</button>
						<input type="text" name="" class="choose_cabinet readonly" readonly="readonly">
						<input type="text" name="srch_keyword" class="integratedSrch_keyword" placeholder="검색어를 입력하세요">
						<button type="submit" class="integratedSrch_btn">검 색</button>
					</form>
					
					<button type="button" id="" name="" class="integratedSrch_detail_btn">상세검색</button>
					<label class="research_in_result"><input type="checkbox" id="" name="" value="">결과내 재 검색</label>
                    <div class="integratedCnts_dropDown_menu hide">
						<form>
							<table class="srch_integrated_tbl">
								<tr>
								<th>기본검색</th>
								<td>
									<input type="text" class="srch_integrated_txt" placeholder="검색어를 입력하세요">
									<label><input type="checkbox" id="" name="" class="" value="">결과내 재 검색</label>
								</td>
								</tr>
								<tr>
								<th>등록기간</th>
								<td>
									<a href="#" id="0" class="srch_integrated_regDate">전체</a>
									<a href="#" id="1" class="srch_integrated_regDate">1개월</a>
									<a href="#" id="2" class="srch_integrated_regDate">3개월</a>
									<a href="#" id="3" class="srch_integrated_regDate">6개월</a>
									<a href="#" id="4" class="srch_integrated_regDate">1년</a>			
									<input type="text" id="datepicker3" name="" class="srch_integrated_startDate input-text calend" name="start_date" readonly="readonly">
									<span class="dateto">-</span>
									<input type="text" id="datepicker4" name="" class="srch_integrated_endDate input-text calend" name="end_date" readonly="readonly">
								</td>
								</tr>
								<tr>
								<th></th>
								<td>
									<label><input type="radio" name="" class="" value="">전체</label>
									<label><input type="radio" name="" class="" value="">제목</label>
									<label><input type="radio" name="" class="" value="">본문</label>
									<label><input type="radio" name="" class="" value="">등록자</label>
									<label><input type="radio" name="" class="" value="">첨부파일명</label>
									<label><input type="radio" name="" class="" value="">첨부내용</label>
								</td>
								</tr>
								<tr>
								<th>문서유형</th>
								<td>
									<select id="srch_type6" name="">
										<option value="" selected="selected">전체</option>
										<option value="">일반문서</option>
										<option value="">파일문서</option>
									</select>							
								</td>
								</tr>
								<tr>
								<th>폴더위치</th>
								<td>
									<input type="text" id="" name="" class="srch_integrated_folder">
									<button type="button" id="" name="" class="srch_integrated_folderChoose">선택</button>
									<label><input type="checkbox" id="" name="" class="" value="">하위폴더 포함</label>
								</td>
								</tr>
								<tr>
								<td colspan="2" class="srch_integrated_btnGrp">
									<button type="submit" id="srch_integrated_submit" class="srch_integrated_btn">검색</button>
									<button type="reset" id="" class="srch_integrated_btn">초기화</button>
								</td>
								</tr>
							</table>
						</form>
					</div>
					<div class="integratedSrch_duration">
						<label class="duration_lbl">작성기간</label>
						<a href="#" id="0" class="srch_integrated_regDate">전체</a>
						<a href="#" id="1" class="srch_integrated_regDate">1년</a>
						<a href="#" id="2" class="srch_integrated_regDate">3년</a>
						<a href="#" id="3" class="srch_integrated_regDate">5년</a>
						<a href="#" id="self" class="srch_integrated_regDate">직접입력</a>			
						<input type="text" id="datepicker8" name="" class="srch_integrated_startDate input-text calend" name="start_date" readonly="readonly">
						<span class="dateto">-</span>
						<input type="text" id="datepicker9" name="" class="srch_integrated_endDate input-text calend" name="end_date" readonly="readonly">
					</div>
				</div>	
			</div>
			<div class="cnts_tbl_menu">
				<div class="tbl_menu_left">
					<div class="integrated_srch_result">
						검색결과 : <span class="integrated_srch_cnts">10</span>건
						<span class="integrated_srch_department">부서함</span> | 
						본문 미리보기 :
					</div>
					<div class="tbl_extFunction">
						<a class="dropDown_txt">
                        	<label>사용</label>
                        	<span class="dropDown_arrow down"></span>
                        </a>
                        <div class="extFunction_dropDown_wrapper hide">
                        	<ul class="extFunction_dropDown_menu">
                            	<li><a href="#">사용</a></li>
                                <li><a href="#">미사용</a></li>
                            </ul>
                        </div>
					</div>
				</div>
				<div class="tbl_menu_right">
					<ul class="tbl_thumbMenu">
						<li class="excel_download"><a href="#" class="menu"></a></li>
						<li class="layout_view"><a href="#" class="menu"></a>
                        	<div class="layout_view_dropDown hide">
                            	<ul>
                                	<li id="list_only"><a href="#">메일 목록만 보기</a></li>
                                	<li id="horizontal_divide"><a href="#">좌우 분할로 보기</a></li>
                                	<li id="vertical_divide"><a href="#">상하 분할로 보기</a></li>
                                </ul>
                            </div>
                        </li>
						<li class="menu_refresh"><a href="#" class="menu"></a></li>
					</ul>
					<select id="tbl_rowCount" name="">
						<option value="10">10개</option>
                        <option value="15">15개</option>
                        <option value="20">20개</option>
                        <option value="25">25개</option>
					</select>
				</div>
			</div>	
			<div class="cnts_tbl_wrapper">
            	<div class="cnts_list">            	
                    <div class="table">
            			<div class="thead_wrapper">
            				<div class="thead">
		                		<span class="docOpen">문서열기</span>
		                		<span>제목</span>
		                		<span class="folderPath">폴더경로</span>
		                		<span class="docAccu">정확도</span>
		                		<span class="regid">
									<a href="#" class="sort_up">등록자</a>
								</span>
		                		<span class="regDate_cellEnd">
		                			<a href="#" class="sort_down">등록일</a>
		                		</span>
	                		</div>
            			</div>
            			<div class="scrollbar1">
	                		<div class="scrollbar"><div class="track"><div class="thumb"><div class="end"></div></div></div></div>
	                		<div class="viewport">
	                			<div class="overview">
	                				<div class="tblRow">
				                		<span class="docOpen">
				                			<a href="#"><img src="${contextRoot}/img/icon/hwp.png" class="extension" alt="" title=""></a>
				                		</span>
				                		<span>
				                			<a href="#">자재업무 매뉴얼...자재업무 매뉴얼...자재업무 매뉴얼...</a>
				                			
				                		</span>
				                		<span class="folderPath">
				                			<span class="integrated_srch_department">부서함</span>
				                			/경영지원팀/경영일반/규정관리
				                		</span>
				                		<span class="docAccu">100</span>
				                		<span class="regid_noborder">홍길동</span>
				                		<span class="regDate_cellEnd">2015-03-19</span>
	                				</div>
	                				<div class="tblRow">
				                		<span class="docOpen"></span>
				                		<span class="preview">
				                			첨부파일 : 자재업무 매뉴얼.ppt<br>
				                			첨부파일 : 자재업무 매뉴얼.ppt<br>
				                			첨부파일 : 자재업무 매뉴얼.ppt
				                		</span>
	                				</div>
	                				<div class="tblRow">
				                		<span class="docOpen">
				                			<a href="#"><img src="${contextRoot}/img/icon/hwp.png" class="extension" alt="" title=""></a>
				                		</span>
				                		<span>
				                			<a href="#">자재업무 매뉴얼...자재업무 매뉴얼...자재업무 매뉴얼...</a>
				                			
				                		</span>
				                		<span class="folderPath">
				                			<span class="integrated_srch_department">부서함</span>
				                			/경영지원팀/경영일반/규정관리
				                		</span>
				                		<span class="docAccu">100</span>
				                		<span class="regid_noborder">홍길동</span>
				                		<span class="regDate_cellEnd">2015-03-19</span>
	                				</div>
	                				<div class="tblRow">
				                		<span class="docOpen"></span>
				                		<span class="preview">
				                			첨부파일 : 자재업무 매뉴얼.ppt<br>
				                			첨부파일 : 자재업무 매뉴얼.ppt<br>
				                			첨부파일 : 자재업무 매뉴얼.ppt
				                		</span>
	                				</div>
	                				<div class="tblRow">
				                		<span class="docOpen">
				                			<a href="#"><img src="${contextRoot}/img/icon/hwp.png" class="extension" alt="" title=""></a>
				                		</span>
				                		<span>
				                			<a href="#">자재업무 매뉴얼...자재업무 매뉴얼...자재업무 매뉴얼...</a>
				                			
				                		</span>
				                		<span class="folderPath">
				                			<span class="integrated_srch_department">부서함</span>
				                			/경영지원팀/경영일반/규정관리
				                		</span>
				                		<span class="docAccu">100</span>
				                		<span class="regid_noborder">홍길동</span>
				                		<span class="regDate_cellEnd">2015-03-19</span>
	                				</div>
	                				<div class="tblRow">
				                		<span class="docOpen"></span>
				                		<span class="preview">
				                			첨부파일 : 자재업무 매뉴얼.ppt<br>
				                			첨부파일 : 자재업무 매뉴얼.ppt<br>
				                			첨부파일 : 자재업무 매뉴얼.ppt
				                		</span>
	                				</div>
	                				
		                		</div>
	                		</div>
	                	</div>
            		</div>
                </div>
                
                <div class="cnts_aside hide">
                	<div class="horizontal_draggable"></div>
                    <div class="aside_cnts_wrapper">
                    	<div class="cnts_sub_wrapper">
                    		<div class="aside_title">수평드래그 타이틀
	                    		<span class="regDate">2010-05-31</span>
	                    	</div>
	                    	<div class="aside_cnts">
	                    		수평드래그 내용
	                    	</div>
                    	</div>
                    </div>
                </div>
                <div class="cnts_bottom hide">
                	<div class="vertical_draggable"></div>
                    <div class="bottom_cnts_wrapper">
                   		<div class="bottom_title">
                   			수직드래그 타이틀
                   			<span class="regDate">2010-05-31</span>
                   		</div>
                    	<div class="bottom_cnts">
                    		수직드래그 내용
                    	</div>
                    </div>
                </div>
			</div>
			<div class="pg_navi_wrapper">
            	<ul class="pg_navi">
                	<li class="first"><a href="#"><img src="${contextRoot}/img/icon/pg_first.png" alt="" title=""></a></li>
                    <li class="prev"><a href="#"><img src="${contextRoot}/img/icon/pg_prev.png" alt="" title=""></a></li>
                	<li class="curr"><a href="#">1</a></li>
                    <li><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li><a href="#">4</a></li>
                    <li><a href="#">5</a></li>
                    <li><a href="#">6</a></li>
                    <li><a href="#">7</a></li>
                    <li><a href="#">8</a></li>
                    <li><a href="#">9</a></li>
                    <li><a href="#">10</a></li>
                    <li class="next"><a href="#"><img src="${contextRoot}/img/icon/pg_next.png" alt="" title=""></a></li>
					<li class="last"><a href="#"><img src="${contextRoot}/img/icon/pg_last.png" alt="" title=""></a></li>
                </ul>    
            </div>
        </div>