package kr.co.exsoft.eframework.util;

/**
 * 
 * Paging 처리 클래스 일반 (AJAX 방식인 경우)
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
public class PagingAjaxUtil {

	public int nPage; 								// 현재 페이지
	public int nTotLineNum; 						// 총 라인수
	public int nMaxListLine; 						// 한페이지에 보여지는 최대 라인수
	public int nMaxListPage; 						// 한페이지에 보여지는 페이지수
	public String strLink; 							// 페이지 이동시에 get방식으로 넘기는 데이터값
	public int nTotPageSize; 						// 총 페이지 수
	public String strFirstPage; 						// 첫페이지 이동버튼
	public String strLastPage; 						// 마지막페이지 이동버튼
	public String strLinkPageList; 				// 페이지 리스트 ([1][2]...)
	public String strLinkNextPage; 				// 다음페이지 이동버튼
	public String strLinkPrevPage; 				// 이전페이지 이동버튼
	public String strLinkPageNext; 				// 다음 몇페이지 이동 버튼
	public String strLinkPagePrev; 				// 이전 몇페이지 이동버튼
	public int nListNum; 							// 페이지에 보여지는 글수
	public int currentPageSetUp; 				// 페이징리스트의 첫페이지 번호
	public int nFirstArticleNum; 					// 페이지의 첫글번호
	public String strLinkQuery;  					// 현재페이지 쿼리스트링
	public String contextRoot;						// ContextRoot Path
	
	public PagingAjaxUtil() { }
	
	/**
	 * 페이징 초기값 설정
	 * @param req
	 * @param nPage
	 * @param nTotLineNum
	 * @param nMaxListLine
	 * @param nMaxListPage
	 * @param strLink
	 */
	public PagingAjaxUtil(int nPage, int nTotLineNum,int nMaxListLine, int nMaxListPage, String strLink,String contextRoot) {
		
		this.contextRoot = contextRoot;
		this.nPage = nPage == 0 ? 1 : nPage;
		this.nTotLineNum = nTotLineNum;
		this.nMaxListLine = nMaxListLine;
		this.nMaxListPage = nMaxListPage;
		this.strLink = strLink;
		this.currentPageSetUp = getPageSetUp();
		this.nTotPageSize = getTotPageSize();
		this.strFirstPage = getFirstPage();
		this.strLastPage = getLastPage();
		this.strLinkPageList = getLinkPageList();
		this.strLinkNextPage = getNextPage();
		this.strLinkPrevPage = getPrevPage();
		this.strLinkPageNext = getLinkPageNext() + "\n" + this.strLastPage;
		this.strLinkPagePrev = this.strFirstPage + "\n" + getLinkPagePrev();
		this.nListNum = getListNum();
		this.nFirstArticleNum = getFirstArticleNum();
		this.strLinkQuery = "nPage=" + nPage + strLink;		
	}
		
	/**
	 * 총페이지 수 구하기
	 * @return int
	 */
	protected int getTotPageSize() {
		if (nTotLineNum == 0)
			nTotPageSize = 1;
		else if ((nTotLineNum % nMaxListLine) != 0)
			nTotPageSize = nTotLineNum / nMaxListLine + 1;
		else
			nTotPageSize = nTotLineNum / nMaxListLine;
		return nTotPageSize;
	}
	
	/**
	 * 페이지이동버튼리스트의 마지막페이지 구하기
	 * @return int
	 */
	protected int getPageSetUp() {
		int currentPageSetUp = (nPage / nMaxListPage) * nMaxListPage;
		if (nPage % nMaxListPage == 0)
			currentPageSetUp -= nMaxListPage;
		return currentPageSetUp;
	}

	/**
	 * 페이지당 보여줄 글수(마지막페이지)
	 * @return int
	 */
	protected int getListNum() {
		if (nTotPageSize == nPage && (nTotLineNum % nMaxListLine) != 0)
			nListNum = nTotLineNum % nMaxListLine;
		else
			nListNum = nMaxListLine;
		return nListNum;
	}

	/**
	 * 이전 몇개페이지 이동버튼의 post로 넘겨지는 값
	 * @return String
	 */
	protected String getLinkPagePrev() {
		if (nPage > nMaxListPage)
			return new StringBuffer("<li class='prev'><a href=\"")
				.append(strLink)
				.append("(")
				.append(currentPageSetUp - nMaxListPage + 1)
				.append(");\"")
				.append("><img src='"+contextRoot+"/img/icon/pg_prev2.png' border='0'></a></li>").toString();			// 이미지교체
		else
			return "<li class='prev'><a href='javascript:void(0);'><img src='"+contextRoot+"/img/icon/pg_prev.png' border='0'></a></li>";
	}

	/**
	 * 첫페이지로 이동시 post로 넘겨지는 값
	 * @return String
	 */
	protected String getFirstPage() {

		if (nPage > 1)
			return new StringBuffer("<li class='first'><a href=\"")
				.append(strLink)
				.append("(1);\"><img src='"+contextRoot+"/img/icon/pg_first2.png' border='0'></a></li>").toString();	
		else
			return "<li class='first'><a href='javascript:void(0);'><img src='"+contextRoot+"/img/icon/pg_first.png' border='0'></a></li>";

	}

	/**
	 * 이전페이지 이동시 post로 넘겨지는 값
	 * @return String
	 */
	protected String getPrevPage() {
		int nPrevPage = nPage - 1;
			if (nPage > 1)
				return new StringBuffer("<li class='prev'><a href=")
					.append("\"" + strLink + "(" + nPrevPage + ");\"")
					.append("><img src='"+contextRoot+"/img/icon/pg_prev2.png' border='0'></a></li>").toString();		
			else
				return "<li class='prev'><a href='javascript:void(0);'><img src='"+contextRoot+"/img/icon/pg_prev.png' border='0'></a></li>";
		
	}

	/**
	 * 페이지 리스트 보여주기 (링크될 페이지와 값을 같이 리턴) 
	 * @return String
	 */
	protected String getLinkPageList() {
		
		StringBuffer buf = new StringBuffer("");
		
		for (int i = (currentPageSetUp + 1); i <= (nTotPageSize)
				&& i <= (currentPageSetUp + nMaxListPage); i++) {
			if (i != nPage)
				buf.append("<li><a href =\"").append(strLink).append("(").append(i).append(");  \">").append(i).append(" </a></li>");
			else
				buf.append("<li class='curr'><a href='javascript:void(0);'>").append(i).append("</a></li>");		// 현재 Page
		}
		
		return buf.toString();
	}

	/**
	 * 다음페이지 이동시 post로 넘겨지는 값
	 * @return String
	 */
	protected String getNextPage() {
		
		int nNextPage = nPage + 1;
		
		if (nPage < nTotPageSize)
			return new StringBuffer("<li class='next'><a href=\"")
				.append(strLink)
				.append("(")
				.append(nNextPage)
				.append(");\"><img src='"+contextRoot+"/img/icon/pg_next2.png' border='0'></a></li>").toString();

		else
			return "<li class='next'><a href='javascript:void(0);'><img src='"+contextRoot+"/img/icon/pg_next.png' border='0'></a></li>";
	}

	/**
	 * 다음 몇개페이지 이동버튼의 post로 넘겨지는 값
	 * @return String
	 */
	protected String getLinkPageNext() {
		if ((nTotPageSize - currentPageSetUp) > nMaxListPage)
			return new StringBuffer("<li class='next'><a href=\"")
				.append(strLink)
				.append("(")
				.append(currentPageSetUp + nMaxListPage + 1)
				.append(");\"><img src='"+contextRoot+"/img/icon/pg_next2.png' border='0'></a></li>").toString();
		else
			return "<li class='next'><a href='javascript:void(0);'><img src='"+contextRoot+"/img/icon/pg_next.png' border='0'></a></li>";
	}

	/**
	 * 마지막페이지 이동버튼의 post로 넘겨지는 값
	 * @return String
	 */
	protected String getLastPage() {
		if (nPage < nTotPageSize)
			return new StringBuffer("<li class='last'><a href=\"")
				.append(strLink)
				.append("(")
				.append(nTotPageSize)
				.append(");\"><img src='"+contextRoot+"/img/icon/pg_last2.png' border='0'></a></li>").toString();
		else
			return "<li class='last'><a href='javascript:void(0);'><img src='"+contextRoot+"/img/icon/pg_last.png' alt='' title=''></a></li>";
	}

	/**
	 * 페이지의 첫글번호 구하기
	 * @return int
	 */
	protected int getFirstArticleNum() {
		int nFirstArticleNum = (nPage - 1) * nMaxListLine + 1;
		if (nFirstArticleNum <= 0)
			nFirstArticleNum = 1;
		return nFirstArticleNum;
	}

	/**
	 * 페이지이동시 post값으로 넘겨지는 값을 URL인코딩
	 * @param str
	 * @return String
	 */
	protected String getUrlLink(String str) {
		try {
			str = java.net.URLEncoder.encode(str, "ISO-8859-1");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		return str;
	}
}
