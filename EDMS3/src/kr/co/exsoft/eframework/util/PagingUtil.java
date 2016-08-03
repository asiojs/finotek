package kr.co.exsoft.eframework.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * Paging 처리 클래스 일반 (URL 방식인 경우)
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
public class PagingUtil {

	public int nPage; 								// 현재 페이지
	public int nTotLineNum; 						// 총 라인수
	public int nMaxListLine; 						// 한페이지에 보여지는 최대 라인수
	public int nMaxListPage; 						// 한페이지에 보여지는 페이지수
	public String strLink; 							// 페이지 이동시에 get방식으로 넘기는 데이터값
	public String strPageName; 					// 현재페이지 이름
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
	
	public PagingUtil() {} 
	
	
	/**
	 * 페이지 초기값 설정
	 * @param req
	 * @param nPage - 현재 페이지
	 * @param nTotLineNum - 총 라인수
	 * @param nMaxListLine - 한페이지에 보여지는 최대 라인수
	 * @param nMaxListPage - 한페이지에 보여지는 페이지수
	 * @param strLink
	 */
	public PagingUtil(HttpServletRequest req, int nPage, int nTotLineNum,int nMaxListLine, int nMaxListPage, String strLink) {
		
		this.nPage = nPage == 0 ? 1 : nPage;
		this.nTotLineNum = nTotLineNum;
		this.nMaxListLine = nMaxListLine;
		this.nMaxListPage = nMaxListPage;
		this.strLink = strLink;
		this.currentPageSetUp = getPageSetUp();
		this.strPageName = req.getRequestURI();
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
	 * 페이지 초기값 설정
	 * @param req
	 * @param nPage
	 * @param nTotLineNum
	 * @param nMaxListLine
	 * @param nMaxListPage
	 * @param strLink
	 * @param Link
	 */
	public PagingUtil(HttpServletRequest req, int nPage, int nTotLineNum,int nMaxListLine, int nMaxListPage, String strLink, String Link) {
		
		this.nPage = nPage == 0 ? 1 : nPage;
		this.nTotLineNum = nTotLineNum;
		this.nMaxListLine = nMaxListLine;
		this.nMaxListPage = nMaxListPage;
		this.strLink = strLink;
		this.currentPageSetUp = getPageSetUp();
		this.strPageName = req.getRequestURI();
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
		this.strLink = Link;
		
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
			if (strLink.toLowerCase().indexOf("javascript:") == -1) {
				return new StringBuffer("<a href=")
						.append(strPageName)
						.append("?")
						.append("nPage=")
						.append(currentPageSetUp - nMaxListPage + 1)
						.append(strLink)
						.append("><img src='/img/page/page_prev.gif'></a>&nbsp;&nbsp;")
						.toString();
			} else {
				return new StringBuffer("<a href=\"")
						.append(strLink)
						.append("(")
						.append(currentPageSetUp - nMaxListPage + 1)
						.append(");\"")
						.append(
								"><img src='/img/page/page_prev.gif'></a>&nbsp;&nbsp;")
						.toString();
			}

		else
			return "";
	}

	
	/***
	 * 첫페이지로 이동시 post로 넘겨지는 값
	 * @return String
	 */
	protected String getFirstPage() {

		if (nPage > 1)
			if (strLink.toLowerCase().indexOf("javascript:") == -1) {
				return new StringBuffer("<a href=")
						.append(strPageName)
						.append("?nPage=1")
						.append(strLink)
						.append("><img src='/img/page/page_first.gif'></a>")
						.toString();
			} else {
				return new StringBuffer("<a href=\"")
						.append(strLink)
						.append("(1);\"><img src='/img/page/page_first.gif'></a>")
						.toString();
			}
		else
			return "";

	}

	/**
	 * 이전페이지 이동시 post로 넘겨지는 값
	 * @return String
	 */
	protected String getPrevPage() {
		int nPrevPage = nPage - 1;
		if (strLink.toLowerCase().indexOf("javascript:") == -1) {
			if (nPage > 1)
				return new StringBuffer("<a href=")
						.append(strPageName)
						.append("?")
						.append("nPage=")
						.append(nPrevPage)
						.append(strLink)
						.append("><img src='/img/page/page_prev.gif'></a>&nbsp;&nbsp;")
						.toString();
			else
				return "<img src='/img/page/page_prev.gif'>&nbsp;&nbsp;";
		} else {
			if (nPage > 1)
				return new StringBuffer("<A HREF=")
						.append("\"" + strLink + "(" + nPrevPage + ");\"")
						.append("><img src='/img/page/page_prev.gif'></a>&nbsp;&nbsp;")
						.toString();
			else
				return "<img src='/img/page/page_prev.gif'>&nbsp;&nbsp;";
		}
	}

	/**
	 * 페이지 리스트 보여주기 (링크될 페이지와 값을 같이 리턴)
	 * @return String
	 */
	protected String getLinkPageList() {
		StringBuffer buf = new StringBuffer("|");
		if (strLink.toLowerCase().indexOf("javascript:") == -1) {
			for (int i = (currentPageSetUp + 1); i <= (nTotPageSize)
					&& i <= (currentPageSetUp + nMaxListPage); i++) {
				if (i != nPage)
					buf.append("&nbsp;<a href =").append(strPageName).append(
							"?nPage=").append(i).append(strLink).append(">")
							.append(i).append("</a>&nbsp;|");
				else
					buf.append("&nbsp;<b>").append(i).append("</b>&nbsp;|");
			}
		} else {
			for (int i = (currentPageSetUp + 1); i <= (nTotPageSize)
					&& i <= (currentPageSetUp + nMaxListPage); i++) {
				if (i != nPage)
					buf.append("&nbsp;<A HREF =\"").append(strLink).append("(")
							.append(i).append(");  \">").append(i).append(" </a>&nbsp;|");
				else
					buf.append("&nbsp;<b>").append(i).append("</b>&nbsp;|");
			}
		}

		return buf.toString();
	}

	/**
	 * 다음페이지 이동시 post로 넘겨지는 값
	 * @return String
	 */
	protected String getNextPage() {
		int nNextPage = nPage + 1;
		if (strLink.toLowerCase().indexOf("javascript:") == -1) {
			if (nPage < nTotPageSize)
				return new StringBuffer("&nbsp;&nbsp;<a href=")
						.append(strPageName)
						.append("?")
						.append("nPage=")
						.append(nNextPage)
						.append(strLink)
						.append("><img src='/img/page/page_next.gif'></a>")
						.toString();

			else
				return "&nbsp;&nbsp;<img src='/img/page/page_next.gif'>";
		} else {
			if (nPage < nTotPageSize)
				return new StringBuffer("&nbsp;&nbsp;<a href=\"")
						.append(strLink)
						.append("(")
						.append(nNextPage)
						.append(");\"><img src='/img/page/page_next.gif'></a>")
						.toString();

			else
				return "&nbsp;&nbsp;<img src='/img/page/page_next.gif'>";

		}
	}

	/**
	 * 다음 몇개페이지 이동버튼의 post로 넘겨지는 값
	 * @return String
	 */
	protected String getLinkPageNext() {
		if ((nTotPageSize - currentPageSetUp) > nMaxListPage)
			if (strLink.toLowerCase().indexOf("javascript:") == -1) {
				return new StringBuffer("&nbsp;&nbsp;<a href=")
						.append(strPageName)
						.append("?")
						.append("nPage=")
						.append(currentPageSetUp + nMaxListPage + 1)
						.append(strLink)
						.append("><img src='/img/page/page_next.gif'></a>")
						.toString();
			} else {
				return new StringBuffer("&nbsp;&nbsp;<a href=\"")
						.append(strLink)
						.append("(")
						.append(currentPageSetUp + nMaxListPage + 1)
						.append(");\"><img src='/img/page/page_next.gif'></a>")
						.toString();
			}
		else
			return "";
	}

	/**
	 * 마지막페이지 이동버튼의 post로 넘겨지는 값
	 * @return String
	 */
	protected String getLastPage() {
		if (nPage < nTotPageSize)
			if (strLink.toLowerCase().indexOf("javascript:") == -1) {
				return new StringBuffer("<a href=")
						.append(strPageName)
						.append("?nPage=")
						.append(nTotPageSize)
						.append(strLink)
						.append("><img src='/img/page/page_last.gif'></a>")
						.toString();
			} else {
				return new StringBuffer("<a href=\"")
						.append(strLink)
						.append("(")
						.append(nTotPageSize)
						.append(");\"><img src='/img/page/page_last.gif'></a>")
						.toString();
			}

		else
			return "";
	}
	
	/**
	 * 페이지의 첫글번호 구하기
	 * @return String
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
