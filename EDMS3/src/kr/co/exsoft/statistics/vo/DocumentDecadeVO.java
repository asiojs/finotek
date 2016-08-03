package kr.co.exsoft.statistics.vo;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.vo.VO;

/**
 * 기간별 등록/활용 현황 VO
 *
 * @author 패키지팀
 * @since 2014. 11. 20.
 * @version 1.0
 * 
 */

public class DocumentDecadeVO extends VO {

	// TABLE : XR_DOCUMENT_USER_HT , XR_DOCUMENT_GROUP_HT   :: ALIAS = DecadeDocHt
	private String gdate;							// 부서별 문서현황 기준일						
	private String udate;							// 사용자별 문서현황 기준일
	private int create_cnt;							// 등록
	private int read_cnt;							// 조회
	private int update_cnt;							// 수정		
	private int delete_cnt;							// 삭제	
	
	// 조회항목
	@SuppressWarnings("unused")
	private String dateStr;							// 기준일 문자열 변경처리
	private String isType;							// 구분(일별/월별)
	private String isGroup;							// 구분(사용자/부서)
	private String order_str;						// 정렬기준컬럼
	
	public String getOrder_str() {
		return order_str;
	}
	public void setOrder_str(String order_str) {
		this.order_str = order_str;
	}
	public String getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}
	
	public String getIsType() {
		return isType;
	}
	
	public void setIsType(String isType) {
		this.isType = isType;
	}
	
	public String getDateStr() {
		
		String ret = "";
	
		if(isType.equals(Constant.DAY_TERM) && isGroup.equals(Constant.IS_USER)) {							// 사용자 & 일별
			ret = udate.replaceAll("-",".");
		}else if(isType.equals(Constant.MONTH_TERM) && isGroup.equals(Constant.IS_USER)) {				// 사용자 & 월별
			ret = udate.replaceAll("-",".");
		}else if(isType.equals(Constant.DAY_TERM) && isGroup.equals(Constant.IS_GROUP)) {				// 부서 & 일별
			ret = gdate.replaceAll("-",".");
		}else if(isType.equals(Constant.MONTH_TERM) && isGroup.equals(Constant.IS_GROUP)) {			// 부서 & 월별
			ret = gdate.replaceAll("-",".");
		}
		
		return ret;
	}
	
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getGdate() {
		return gdate;
	}
	public void setGdate(String gdate) {
		this.gdate = gdate;
	}
	public String getUdate() {
		return udate;
	}
	public void setUdate(String udate) {
		this.udate = udate;
	}
	public int getCreate_cnt() {
		return create_cnt;
	}
	public void setCreate_cnt(int create_cnt) {
		this.create_cnt = create_cnt;
	}
	public int getRead_cnt() {
		return read_cnt;
	}
	public void setRead_cnt(int read_cnt) {
		this.read_cnt = read_cnt;
	}
	public int getUpdate_cnt() {
		return update_cnt;
	}
	public void setUpdate_cnt(int update_cnt) {
		this.update_cnt = update_cnt;
	}
	public int getDelete_cnt() {
		return delete_cnt;
	}
	public void setDelete_cnt(int delete_cnt) {
		this.delete_cnt = delete_cnt;
	}
	
	
}
