package kr.co.exsoft.eframework.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 문자열 처리 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class StringUtil { 
	
	protected static final Log logger = LogFactory.getLog(StringUtil.class);
    public static final int RIGHT = 1;
    public static final int LEFT = 2;

    //  문자열을 받아서 null이면 true, null이 아니면 false를 return한다.
    public static boolean isNull(String str) {
        if ((str == null) || (str.trim().equals(""))
                || (str.trim().equals("null")))
            return true;
        else
            return false;
    }

    /**
     * 주어진 문자열을 이용하여 지정한 위치로부터 원하는 길이만큼의 문자열을 구함
     *
     * @param str
     *            원하는 문자열 가지고 있는 문자열
     * @param offset
     *            원하는 문자열 시작위치 (1부터 시작)
     * @param leng
     *            원하는 문자열 길이
     * @return 원하는 문자열 객체
     */
    public static String subString(String str, int offset, int leng) {
        return new String(str.getBytes(), (offset - 1), leng);
    }

    /**
     * 주어진 문자열을 이용하여 지정한 위치로부터 끝까지의 문자열을 구함
     *
     * @param str
     *            원하는 문자열 가지고 있는 문자열
     * @param offset
     *            원하는 문자열 시작위치 (1부터 시작)
     * @return 원하는 문자열 객체
     */
    public static String subString(String str, int offset) {
        byte[] bytes = str.getBytes();
        int size = bytes.length - (offset - 1);
        return new String(bytes, (offset - 1), size);
    }

    /**
     * 주어진 문자열을 대상으로하여 주어진 길이만큼의 문자열을 생성하여 리턴함.
     * <p>
     *
     * <pre>
     *
     *
     *          (예)
     *         	String str = &quot;abcd&quot;;
     *         	출력 = &quot;abcd  &quot;
     *
     *         	String str = &quot;abcd&quot;;
     *         	출력 = &quot;abc&quot;
     *
     *         	String str = &quot;가나다라&quot;;
     *         	출력 = &quot;가나다&quot;
     *
     *         	String str = &quot;가나다라&quot;;
     *         	출력 = &quot;???&quot;
     *
     *
     * </pre>
     *
     * @param str
     *            대상 문자열
     * @param size
     *            만들고자 하는 문자열의 길이
     * @return 주어진 길이만큼의 문자
     */
    public static String fitString(String str, int size) {
        return fitString(str, size, StringUtil.LEFT);
    }

    /**
     * 주어진 문자열을 대상으로하여 주어진 길이만큼의 문자열을 생성하여 리턴함.
     * <p>
     *
     * <pre>
     *
     *
     *          (예)
     *         	String str = &quot;abcd&quot;;
     *         	출력 = &quot;  abcd&quot;
     *
     *
     * </pre>
     *
     * @param str
     *            대상 문자열
     * @param size
     *            만들고자 하는 문자열의 길이
     * @param align
     *            정열기준의 방향(RIGHT, LEFT)
     * @return 주어진 길이만큼의 문자
     */
    public static String fitString(String str, int size, int align) {
        byte[] bts = str.getBytes();
        int len = bts.length;
        if (len == size) {
            return str;
        }
        if (len > size) {
            String s = new String(bts, 0, size);
            if (s.length() == 0) {
                StringBuffer sb = new StringBuffer(size);
                for (int idx = 0; idx < size; idx++) {
                    sb.append("?");
                }
                s = sb.toString();
            }
            return s;
        }
        if (len < size) {
            int cnt = size - len;
            char[] values = new char[cnt];
            for (int idx = 0; idx < cnt; idx++) {
                values[idx] = ' ';
            }
            if (align == StringUtil.RIGHT) {
                return String.copyValueOf(values) + str;
            } else {
                return str + String.copyValueOf(values);
            }
        }
        return str;
    }

    /**
     * 문자열을 기본분리자(white space)로 분리하여 문자열배열을 생성함
     *
     * @param str
     * @return 문자열 배열
     */
    public static String[] toStringArray(String str) {
        Vector<String> vt = new Vector<String>();
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            vt.add(st.nextToken());
        }
        return toStringArray(vt);
    }

    /**
     * Vector에 저장된 객체들을 이용하여 문자열 배열을 생성함
     *
     * @param vt
     * @return 문자열 배열
     */
    public static String[] toStringArray(Vector<String> vt) {
        String[] strings = new String[vt.size()];
        for (int idx = 0; idx < vt.size(); idx++) {
            strings[idx] = vt.elementAt(idx).toString();
        }
        return strings;
    }

    /**
     * 주어진 문자열에서 지정한 문자열값을 지정한 문자열로 치환후 그결과 문자열을 리턴함.
     *
     * @param src
     * @param from
     * @param to
     * @return 문자열
     */
    public static String replace(String src, String from, String to) {
        if (src == null)
            return null;
        if (from == null)
            return src;
        if (to == null)
            to = "";
        StringBuffer buf = new StringBuffer();
        for (int pos; (pos = src.indexOf(from)) >= 0;) {
            buf.append(src.substring(0, pos));
            buf.append(to);
            src = src.substring(pos + from.length());
        }
        buf.append(src);
        return buf.toString();
    }

    /**
     * 주어진문자열이 지정한 길이를 초과하는 경우 짤라내고 '...'을 붙여 리턴함.
     *
     * @param str
     * @param limit
     * @return 문자열
     */
    public static String cutString(String str, int limit) {

        if (str == null || limit < 4)
            return str;

        int len = str.length();
        int cnt = 0, index = 0;

        while (index < len && cnt <= limit) {
            if (str.charAt(index++) < 256) // 1바이트 문자라면...
                cnt++; // 길이 1 증가
            else
                // 2바이트 문자라면...
                cnt += 2; // 길이 2 증가
        }

        if (index < len)
            str = str.substring(0, index - 1) + "...";

        return str;
    }

    /**
     * 주어진 문자로 원하는 갯수만큼의 char[] 를 생성함.
     *
     * @param c
     *            생성할 문자
     * @param cnt
     *            생성할 갯수
     * @return char array
     */
    public static char[] makeCharArray(char c, int cnt) {
        char a[] = new char[cnt];
        Arrays.fill(a, c);
        return a;
    }

    /**
     * 주어진 문자로 원하는 갯수만큼의 String 을 생성함.
     *
     * @param c
     *            생성할 문자
     * @param cnt
     *            생성할 갯수
     * @return 원하는 갯수 많큼 생성된 문자열
     */
    public static String getString(char c, int cnt) {
        return new String(makeCharArray(c, cnt));
    }

    // 2002-02-07 추가

    /**
     * String의 좌측 공백을 없앤다.
     *
     * @param lstr
     *            대상 String
     * @return String 결과 String
     */
    public static String getLeftTrim(String lstr) {

        if (!lstr.equals("")) {
            int strlen = 0;
            int cptr = 0;
            boolean lpflag = true;
            char chk;

            strlen = lstr.length();
            cptr = 0;
            lpflag = true;

            do {
                chk = lstr.charAt(cptr);
                if (chk != ' ') {
                    lpflag = false;
                } else {
                    if (cptr == strlen) {
                        lpflag = false;
                    } else {
                        cptr++;
                    }
                }
            } while (lpflag);

            if (cptr > 0) {
                lstr = lstr.substring(cptr, strlen);
            }
        }
        return lstr;
    }

    /**
     * String의 우측 공백을 없앤다.
     *
     * @param lstr
     *            대상 String
     * @return String 결과 String
     */
    public static String getRightTrim(String lstr) {
        if (!lstr.equals("")) {
            int strlen = 0;
            int cptr = 0;
            boolean lpflag = true;
            char chk;

            strlen = lstr.length();
            cptr = strlen;
            lpflag = true;

            do {
                chk = lstr.charAt(cptr - 1);
                if (chk != ' ') {
                    lpflag = false;
                } else {
                    if (cptr == 0) {
                        lpflag = false;
                    } else {
                        cptr--;
                    }
                }
            } while (lpflag);

            if (cptr < strlen) {
                lstr = lstr.substring(0, cptr);
            }
        }
        return lstr;
    }

    /**
     * 좌측에서 특정 길이 만큼 취한다.
     *
     * @param str
     *            대상 String
     * @param Len
     *            길이
     * @return 결과 String
     */
    public static String getLeft(String str, int Len) {
        if (str.equals(null))
            return "";

        return str.substring(0, Len);
    }

    /**
     * 우측에서 특정 길이 만큼 취한다.
     *
     * @param str
     *            대상 String
     * @param Len
     *            길이
     * @return 결과 String
     */
    public static String getRight(String str, int Len) {
        if (str.equals(null))
            return "";

        String dest = "";
        for (int i = (str.length() - 1); i >= 0; i--)
            dest = dest + str.charAt(i);

        str = dest;
        str = str.substring(0, Len);
        dest = "";

        for (int i = (str.length() - 1); i >= 0; i--)
            dest = dest + str.charAt(i);

        return dest;
    }

    /**
     * 입력된 값이 널이면, replace 값으로 대체한다.
     *
     * @param str
     *            입력
     * @param replace
     *            대체 값
     * @return 문자
     */
    public static String nvl(String str, String replace) {
        if (str == null || str.equals("")) {
            return replace;
        } else {
            return str;
        }
    }

    /**
     * Null 또는 공백이면 다른 값으로 대체한다.
     *
     * @param str
     *            입력
     * @param replace
     *            대체 값
     * @return 문
     */
    public static String checkEmpty(String str, String replace) {
        if (str == null || str.equals("")) {
            return replace;
        } else {
            return str;
        }
    }

    /**
     * 프로퍼티 내의 컬럼 값을 변경한다.
     *
     * @param source
     *            컬럼 값
     * @return 변경된 컬럼 값
     */
    public static String convertColumnIntoProp(String source) {
        if (source == null || source.indexOf("_") == -1) {
            if (source.equals(source.toUpperCase())) {
                // DESCRIPTION --> description
                return source.toLowerCase();
            } else {
                // subMeta01 --> subMeta01
                return source;
            }
        }

        StringBuffer buffer = new StringBuffer();

        source = source.toLowerCase();
        String[] tempArr = source.split("_", -1);

        for (int i = 0; i < tempArr.length; i++) {
            if (i == 0) {
                buffer.append(tempArr[i]);
            } else {
                buffer.append(capitalize(tempArr[i]));
            }
        }

        return buffer.toString();
    }

    /**
     * 컬럼내의 프로퍼티 값을 변경한다.
     *
     * @param source
     *            프로퍼티 값
     * @return 변경된 프로퍼티 값
     */
    public static String convertPropIntoColumn(String source) {
        if (source == null || source.length() == 0) {
            return source;
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < source.length(); i++) {
            if (Character.isUpperCase(source.charAt(i))) {
                buffer.append("_");
            }

            buffer.append(Character.toUpperCase(source.charAt(i)));
        }

        return buffer.toString();
    }

    /**
     * 문자를 합친다.
     *
     * @param str
     *            문자
     * @return 합쳐진 문자
     */
    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen).append(
                Character.toTitleCase(str.charAt(0))).append(str.substring(1))
                .toString();
    }

    /**
     * Exception 정보를 String으로 변환한다.
     *
     * @param e
     *            Exception
     * @return String 변환된 Exception
     */
    public static String getErrorTrace(Exception e) {
        if (e == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        String errTrace = sw.toString();

        return errTrace;
    }

    /**
     * String 을 delimeter 기준으로 배열로 생성한다.
     *
     * @param str
     *            String 값
     * @param delimeter
     *            기준
     * @return String[] 생성된 배열
     */
    public static synchronized String[] tokenWork(String str, String delimeter) {
        String[] returnedArray = null;
        //int countX = 0;
        // StringTokenizer tempStr=new StringTokenizer(str,";");
        StringTokenizer tempStr = new StringTokenizer(str, delimeter);
        int FirstTokenCount = tempStr.countTokens();
        returnedArray = new String[FirstTokenCount];
        for (int n = 0; n < FirstTokenCount; n++) {
            returnedArray[n] = tempStr.nextToken();
            // ConsoleLog.print("returnedArray["+n+"]="+returnedArray[n]);
        }
        return returnedArray;
    }

    /**
     * CH001 페이지 리스트 처리를 위해 Total Count SQL Query 문을 생성한다.
     *
     * @param query
     * @return String
     */
    public static String countQuery(String query) {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("\n    SELECT COUNT(*) AS TOTAL_COUNT FROM( ");
        sbuf.append("\n  " + query + " )");
        return sbuf.toString();
    }

    /**
     * CH001 페이지 리스트 처리를 위해 SQL Query 문을 생성한다.
     *
     * @param query
     * @return String
     */
    public static String listQuery(String query) {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("\n    SELECT * FROM( ");
        sbuf.append("\n       SELECT ROWNUM LINENUM, TEMP.* FROM ( ");
        sbuf.append("\n  " + query);
        sbuf.append("\n       ) TEMP WHERE ROWNUM <= ? ) ");
        sbuf.append("\n     WHERE LINENUM >= ? ");
        return sbuf.toString();
    }

    /**
     * prepareStatement의 ?를 변수로 치환한 완성된 query를 생성한다. 특정문자 치환후 return 문자열 치환 sql
     * 작성 날짜: 2006. 11. 03
     *
     * @param sql
     *            쿼리
     * @param al
     *            ? 에 맵핑될 변수
     * @return String
     */
    public static String bindParamToSql(String sql, ArrayList<?> al) {
        if (sql == null || al == null) {
            return sql;
        } else {
            StringBuffer sb = new StringBuffer();
            StringTokenizer st = new StringTokenizer(sql, "?");

            if (st.hasMoreTokens())
                sb.append(st.nextToken());

            int i = 0;

            while (st.hasMoreTokens()) {
                if (i < al.size()) {
                    sb.append("'" + al.get(i) + "'");
                    i++;
                } else {
                    sb.append('?');
                }
                sb.append(st.nextToken());
            }

            while (i < al.size()) {
                sb.append("'" + al.get(i) + "'");
                i++;
            }
            return sb.toString();
        }
    }

    /**
     * html로 헤더의 sort를 해준다. 작성 날짜: 2006. 11. 02
     *
     * @param title
     * @param item
     * @param sortItem
     * @param sortType
     * @param classId
     * @return String
     */
    public static String getHtmlSorting(String title, String item,
            String sortItem, String sortType, String classId) {
        String typeStr = "";
        String typeImage = "";
        StringBuffer html = new StringBuffer();

        if (item.equals(sortItem)) {
            typeStr = (item.equals(sortItem)) ? sortType : "ASC";
            typeImage = (typeStr.equals("ASC")) ? "▲" : "▼";
            html.append("<a href=\"javaScript:fncSorting('" + item + "','"
                    + typeStr + "')\">");
            html.append("<font class=" + classId + ">");
            html.append(title);
            html.append(typeImage);
            html.append("</font>");
            html.append("</a>");
        } else {
            html.append("<a href=\"javaScript:fncSorting('" + item + "','"
                    + typeStr + "')\">" + title + "&nbsp;</a>");
        }

        return html.toString();
    }

    /**
     *
     * 문자열을 int형으로 변환하는 METHOD <br>
     * 변환실패시 -1을 반환함
     *
     * @param sSource
     * @return int
     */
    public static int parseInt(String sSource) {
        return parseInt(sSource, -1);
    }

    /**
     *
     * 문자열을 int형으로 변환하는 METHOD
     *
     * @param sSource
     *            변환할 문자열
     * @param nDefault
     *            변환 실패시 기본값
     * @return int
     */
    public static int parseInt(String sSource, int nDefault) {
        int nResult = 0;

        try {
            nResult = Integer.parseInt(sSource);
        } catch (Exception e) {
            logger.error("failed to convert String to int type [" + sSource + ", " + nDefault + "]", e);
            nResult = nDefault;
        }

        return nResult;
    }

    /**
     *
     * 문자열을 LONG형으로 변환하는 METHOD <br>
     * 변환 실패시 -1로 반환함
     *
     * @param sSource
     * @return long
     */
    public static long parseLong(String sSource) {
        return parseLong(sSource, -1l);
    }

    /**
     *
     * 문자열을 LONG형으로 변환하는 METHOD
     *
     * @param sSource
     *            변환할 문자열
     * @param nDefault
     *            실패시 기본값
     * @return long
     */
    public static long parseLong(String sSource, long nDefault) {
        long nResult = 0l;

        try {
            nResult = Long.parseLong(sSource);
        } catch (Exception e) {
            logger.error("failed to convert String to long type [" + sSource + ", " + nDefault + "]", e);
            nResult = nDefault;
        }

        return nResult;
    }

    
    /**
     * 문자열을 boolean형으로 변환하는 METHOD
     * @param sSource
     * @return boolean
     */
   public static boolean parseBoolean(String sSource) {
       boolean nResult = false;

       try {
           nResult = Boolean.valueOf(sSource).booleanValue();
       } catch (Exception e) {
           logger.error("failed to convert String to boolean type [" + sSource  + "]", e);
       }

       return nResult;
   }

   /**
     *
     * 스트링 치환 함수 주어진 문자열(buffer)에서 특정문자열('src')를 찾아 특정문자열('dst')로 치환
     *
     * @param buffer
     * @param src
     * @param dst
     * @return String
     */
    public static String ReplaceAll(String buffer, String src, String dst) {
        if (buffer == null)
            return null;
        if (buffer.indexOf(src) < 0)
            return buffer;

        int bufLen = buffer.length();
        int srcLen = src.length();
        StringBuffer result = new StringBuffer();

        int i = 0;
        int j = 0;
        for (; i < bufLen;) {
            j = buffer.indexOf(src, j);
            if (j >= 0) {
                result.append(buffer.substring(i, j));
                result.append(dst);

                j += srcLen;
                i = j;
            } else
                break;
        }
        result.append(buffer.substring(i));
        return result.toString();
    }

    /**
     *
     * 전달받은 문자열이 유효한가를 판단하여 반환함 <br>
     * null값이거나, 빈문자열("")이거나, 정수로 변환하여 0이면 false를 반환함
     *
     * @param value
     * @return boolean
     */
    public final static boolean isValid(String value) {
        try {
            if (value == null || value.trim().equals("")
                    || Integer.parseInt(value.trim()) == 0)
                return false;
        } catch (NumberFormatException e) {
            logger.error("failed to convert to number : " + value, e);
        }

        return true;
    }

    /**
     *
     * 전달받은 문자열이 숫자인지를 판단하여 반환함 <br>
     *
     * @param value
     * @return boolean
     */
    public final static boolean isNumeric(String value) {
        try {
            if (value != null)
                Integer.parseInt(value.trim());

            return true;
        } catch (NumberFormatException e) {
            logger.error("failed to convert to number : " + value, e);
        }

        return false;
    }

    /**
     *
     * 금액데이타 123,345,567 형식으로 보여주기
     *
     * @param n
     * @return String
     *
     */
    public static String moneyFormValue(String n) {
        boolean nFlag = true;
        String tMoney = "";

        String o = "";
        String p = "";
        String minus = "";

        if (!n.equals("") && n != null) {
	        if (n.substring(0, 1).equals("-")) {
	            minus = n.substring(0, 1);
	            n = n.substring(1);
	        }

	        if (n.indexOf(".") > 0) {
	            o = n.substring(0, n.indexOf("."));
	            p = n.substring(n.indexOf(".") + 1, n.length());
	        } else {
	            o = n;
	        }

	        o = replace(o, " ", "");
	        o = replace(o, ",", "");
	        o = replace(o, "+", "");

	        int tLen = o.length();

	        for (int i = 0; i < tLen; i++) {
	            if (i != 0 && (i % 3 == tLen % 3))
	                tMoney += ",";
	            if (i < tLen)
	                tMoney += o.charAt(i);
	        }

	        if (p.length() > 0)
	            tMoney += "." + p;
	        if (nFlag == false)
	            tMoney = "-" + tMoney;

	        if (minus.equals("-")) {
	            tMoney = minus + tMoney;
	        }
        }

        return tMoney;
    }

	/**
	 *
	 * 입력한 문자열이 String이면 true를 아니면 false를 반환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * isString("abc")	===> true
	 * isString(new Integer(0))	===> true
	 *
	 * </pre>
	 *
	 * @param element
	 * @return boolean
	 */
	public static boolean isString(Object element)
	{
		if (element instanceof String)
			return true;
		else
			return false;
	}

	/**
	 *
	 * 입력한 값이 null 또는 null String 일 경우 true를 return 한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * isEmpty("")		===> true
	 * isEmpty(null)	===> true
	 * isEmpty("1")		===> false
	 *
	 * </pre>
	 *
	 * @param value
	 * @return boolean
	 */
	public static boolean isEmpty(String value)
	{
		if (value == null || "".equals(value.trim()))
			return true;
		return false;
	}

	/**
	 *
	 * 입력한 값이 null일 경우 null String을 return 한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * NVL(null)		===> ""
	 * NVL("abc  ")		===> "abc"
	 *
	 * </pre>
	 *
	 * @param value
	 * @return String
	 */
	public static String NVL(String value) {
		return (value == null ? "" : value.trim());
	}

	/**
	 *
	 *  입력된 스트링(strTarget)에서 모든 space를 제거하여 Return한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * </pre>
	 *
	* @param strTarget
	* @return java.lang.String
	 */
	public static String compressString(String strTarget)
	{
		String resultStr = "";
		int index = 0;

		for ( int strLen = strTarget.length(); index < strLen ; index++)
		{
			 if ( strTarget.charAt(index) != ' ' && strTarget.charAt(index) != '　') resultStr += strTarget.charAt(index);
		}

		return resultStr;
	}

	/**
	 *
	* 전달받은 Object Array를 String으로 변환하여 return한다.
	*
	* @param objArr Object []
	* @return java.lang.String
	*/
	public static String convertArrayToString(Object[] objArr) {
		if (objArr == null)
			return "null";
		String ret = "";
		for (int i = 0; i < objArr.length; i++) {
			if (i != 0)
				ret += ",";
			ret += objArr[i];
		}
		return ret;
	}

	/**
	 *
	 *  입력된 스트링(strTarget)에서 임의의 위치(index)에 지정문자열(strInsert)을 추가한 문자열을 반환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * insert("abcdefg",0,"11")		===> 11abcdefg
	 * insert("abcdefg",3,"11")		===> abc11defg
	 * insert("abcdefg",-1,"11")	===> abcde11fg
	 * </pre>
	 *
	 * @param strTarget
	 * @param index 지정문자열을 추가할 위치, 대상문자열의 첫문자 위치는 0 부터 시작
	 *                          if ( index < 0 ) then 대상문자열의 끝자리를 0으로 시작한 상대적 위치
	 * @param strInsert
	 * @return java.lang.String
	 */
	public static String insert(String strTarget, int index, String strInsert) {
		String result = null;
		try {
			StringBuffer strBuf = new StringBuffer();
			String tempString  = null;
			int lengthSize = strTarget.length();
			if (index >= 0) {
				if (lengthSize < index) {
					index = lengthSize;
				}
				strBuf.append(strTarget.substring(0, index));
				strBuf.append(strInsert);
				strBuf.append(strTarget.substring(index));

			} else {
				if (lengthSize < Math.abs(index)) {
					index = lengthSize * (-1);
				}
				tempString = strTarget.substring((lengthSize - 1) + index );
				strBuf.append(strTarget.substring(0, (lengthSize - 1) + index));
				strBuf.append(strInsert);
				strBuf.append(tempString);
			}
			result = strBuf.toString();
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 *
	 * 입력된 스트링(strTarget)에서 임의의 위치(index)에 지정문자열(strReplace)로 대체한 문자열을 반환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * replace("abcdefg",0,"11")		===> 11cdefg
	 * replace("abcdefg",-1,"11") 	===> abcde11
	 *
	 * </pre>
	 *
	 * @param strTarget
	 * @param index 지정문자열을 추가할 위치, 대상문자열의 첫문자 위치는 0 부터 시작
	 *                          if ( index < 0 ) then 대상문자열의 끝자리를 0으로 시작한 상대적 위치
	 * @param strReplace
	 * @return java.lang.String
	 */
	public static String replace(String strTarget, int index, String strReplace) {

		if (strTarget == null ) {
			return strTarget;
		}

		String result = null;
		try {
			StringBuffer strBuf = new StringBuffer();
			int lengthSize = strTarget.length();
			if (index >= 0) {
				if (lengthSize < index) {
					index = lengthSize;
				}
				strBuf.append(strTarget.substring(0, index));
				strBuf.append(strReplace);
				strBuf.append(strTarget.substring(index + strReplace.length()));
			} else {
				if (lengthSize < Math.abs(index)) {
					index = lengthSize * (-1);
				}
				strBuf.append(strTarget.substring(0, (lengthSize - 1) + index));
				strBuf.append(strReplace);
				strBuf.append(
				strTarget.substring((lengthSize - 1) + index + strReplace.length()));
			}
			result = strBuf.toString();
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 *
	 * 입력된 스트링(strTarget)에서 지정문자열(strSearch)이 검색된 횟수를 반환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * search("abcdefd","d")		===> 2
	 * search("abc1def2d","12")	===> 0
	 * </pre>
	 *
	 * @param strTarget
	 * @param strSearch
	 * @return 지정문자열이 검색되었으면 검색된 횟수를, 검색되지 않았으면 0 을 반환한다.
	 */
	public static int search(String strTarget, String strSearch) {
		int result = 0;
		try {
			String strCheck = new String(strTarget);
			for (int i = 0; i < strTarget.length();) {
				int loc = strCheck.indexOf(strSearch);
				if (loc == -1) {
					break;
				} else {
					result++;
					i = loc + strSearch.length();
					strCheck = strCheck.substring(i);
				}
			}
		} catch (Exception e) {
			return 0;
		}
		return result;
	}

	/**
	 *
	 * 입력된 스트링에서 cutLength 만큼 글자를 잘라준다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * shortCutString("하나둘셋넷",6)			===> 하나둘
	 * shortCutString("abcdefghijklmn",6)	===> abcdef
	 *
	 * </pre>
	 *
	 * @param strTarget
	 * @param cutLength
	 * @return java.lang.String
	 */
	public static String shortCutString(String strTarget, int cutLength) {
		try {
			//if (strTarget == null || limit < 4)
			if (strTarget == null )
				return strTarget;
			int len = strTarget.length();
			int cnt = 0, index = 0;
			while (index < len && cnt < cutLength) {
				if (strTarget.charAt(index++) < 256) // 1바이트 문자라면...
					cnt++; // 길이 1 증가
				else // 2바이트 문자라면...
					cnt += 2; // 길이 2 증가
					//	<gnoopy> 왜냐하면 자바의 String에서 모든 문자는 2byte처리하기 때문에.
			}
			if (index < len)
			strTarget = strTarget.substring(0, index);
		} catch (Exception e) {
			return null;
		}
		return strTarget;
	}

	/**
	 *
	 * 입력된 스트링에서 제거할 문자(elimination)에 나열한 모든 문자를 제거한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * split("02)2344-5555", "-# /)(:;")	===> 0223445555
	 * split("ABCDEABCDE", "BE")		===> ACDACD
	 *
	 * </pre>
	 * @param strTarget
	 * @param elimination
	 * @return java.lang.String
	 */
	public static String split(String strTarget, String elimination) {
		if (strTarget == null || strTarget.length() == 0 || elimination == null)
			return strTarget;
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(strTarget, elimination);
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		}
		return sb.toString();
	}

	/**
	 *
	 * 입력된 스트링에서 제거할 문자(elimination)에 나열한 모든 문자를 제거한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * split("02)2344-5555", "-# /)(:;")	===> 0223445555
	 * split("ABCDEABCDE", "BE")		===> ACDACD
	 *
	 * </pre>
	 * @param strTarget
	 * @param elimination
	 * @return null
	 */
	public static String split2(String strTarget, String elimination) {
		if (strTarget == null || strTarget.length() == 0 || elimination == null)
			return "";
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(strTarget, elimination);
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		}
		return sb.toString();
	}

	/**
	 *
	 * 입력된 스트링에서 구분자(delimiter)에 나열된 모든 문자를 기준으로 문자열을 분리하고 분리된 문자열을 배열에 할당하여 반환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * split2Array("ABCDEABCDE", "BE")
	 * ===> A
	 *         CD
	 *         A
	 *         CD
	 *
	 * </pre>
	 *
	 * @param strTarget
	 * @param delimiter
	 * @return java.lang.String[]
	 */
	public static String[] split2Array(String strTarget, String delimiter) {
		if (strTarget == null) return null;

		StringTokenizer st = new StringTokenizer(strTarget, delimiter);
		String[] names = new String[st.countTokens()];
		for (int i = 0; i < names.length; i++) {
			names[i] = st.nextToken().trim();
		}

		return names;
	}

	/**
	 *
	 * 입력된 스트링에서 구분자(delimiter)를 하나의 단어로 인식하고 이 단어를 기준으로 문자열을 분리, 분리된 문자열을 배열에 할당하여 반환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * split2Array("AA-BBB--DDDD", "-",true)
	 * ===> AA
	 *         BBB
	 *
	 *         DDDD
	 *
	 * split2Array("AA-BBB--DDDD", "-", false);
	 * ===> AA
	 *         BBB
 	 *         DDDD
	 *
	 * split2Array("ABCDEABCDE", "BE", true)
	 * ===> ABCDEABCDE
	 *
	 * </pre>
	 *
	 * @param strTarget
	 * @param delimiter 구분자(delimiter)로 인식할 단어로서 결과 문자열에는 포함되지 않는다.
	 * @param isIncludedNull 구분자로 구분된 문자열이 Null일 경우 결과값에 포함여부 ( true : 포함, false : 포함하지 않음. )
	 * @return java.lang.String[]
	 */
	public static String[] split2Array(String strTarget, String delimiter, boolean isIncludedNull) {

		//int index = 0;
		String[] resultStrArray = null;

		try {
			Vector<String> v =  new Vector<String>();

			String strCheck = new String(strTarget);
			while (strCheck.length() != 0) {
				int begin = strCheck.indexOf(delimiter);
				if (begin == -1) {
					v.add(strCheck);
					break;
				} else {
					int end = begin + delimiter.length();
					//	StringTokenizer는 구분자가 연속으로 중첩되어 있을 경우 공백 문자열을 반환하지 않음.
					// 따라서 아래와 같이 작성함.
					if (isIncludedNull) {
						v.add(strCheck.substring(0, begin));
						strCheck = strCheck.substring(end);
						if (strCheck.length() == 0 ) {
							v.add(strCheck);
							break;
						}
					} else{
						if (! StringUtil.isEmpty(strCheck.substring(0, begin))) {
							v.add(strCheck.substring(0, begin));
						}
						strCheck = strCheck.substring(end);
					}

				}
			}

			String[] tempString = new String[0];
			resultStrArray = (String[]) v.toArray(tempString);

		} catch (Exception e) {
			return resultStrArray;
		}

		return resultStrArray;
	}

	/**
	 *
	 * 입력한 문자열 앞뒤에  특정문자를 Left Padding한 문자열을 반환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * padLeft("AAAAAA", 'Z', 10) )	===> ZZZZAAAAAA
	 *
	 * </pre>
	 *
	 * @param value
	 * @param padValue
	 * @param length
	 * @return java.lang.String
	 */
	public static String padLeft(String value, char padValue, int length)
	{

		if (value == null) value = "";

		byte[] orgByte = value.getBytes();
		int orglength = orgByte.length;

		if (orglength < length) //add Padding character
		{
			byte[] paddedBytes = new byte[length];

			int padlength = length - orglength;

			for (int i=0; i< padlength; i++) {
				paddedBytes[i] = (byte)padValue;
			}

			System.arraycopy(orgByte, 0, paddedBytes, padlength, orglength);

			return new String(paddedBytes);
		}
		else if ( orglength > length) //주어진 길이보다 남는다면, 주어진 길이만큼만 잘른다.
		{
			byte[] paddedBytes = new byte[length];
			System.arraycopy(orgByte, 0, paddedBytes, 0, length);
			return new String(paddedBytes);
		}

		return new String( orgByte );
	}

	/**
	 *
	 * 입력한 문자열 앞뒤에  특정문자를 Right Pading한 문자열을 반환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * padRight("AAAAAA", 'Z', 10) )	===> AAAAAAZZZZ
	 *
	 * </pre>
	 *
	 * @param value
	 * @param padValue
	 * @param length
	 * @return java.lang.String
	 */
	public static String padRight(String value, char padValue, int length)
	{

		if (value == null) value = "";

		byte[] orgByte = value.getBytes();
		int orglength = orgByte.length;

		if (orglength < length) //add Padding character
		{
			byte[] paddedBytes = new byte[length];

			System.arraycopy(orgByte, 0, paddedBytes, 0, orglength);
			while (orglength < length) {
				paddedBytes[orglength++] = (byte)padValue;
			}
			return new String(paddedBytes);
		}
		else if ( orglength > length) //주어진 길이보다 남는다면, 주어진 길이만큼만 잘른다
		{
			byte[] paddedBytes = new byte[length];
			System.arraycopy(orgByte, 0, paddedBytes, 0, length);
			return new String(paddedBytes);
		}

		return new String( orgByte );
	}

	/**
	 *
	 * 입력된 스트링에서 space, carriage return, new line을 제거한 스트링을 반환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * removeSpaceCRTab("ab\nc\td\r  ")		===> abcd
	 * removeSpaceCRTab("")             	===> ""
	 * removeSpaceCRTab(null)           	===> null
	 *
	 * </pre>
	 *
	 * @param value
	 * @return boolean
	 */
	public static String splitAll(String value)
	{
		String resultStr = "";
		if (value == null) return value;
		else if ("".equals(value.trim())) return "";
		else {
			resultStr = value.trim();
			resultStr = resultStr.replaceAll(" ", "");
			resultStr = resultStr.replaceAll("\n", "");
			resultStr = resultStr.replaceAll("\t", "");
			resultStr = resultStr.replaceAll("\r", "");
		}

		return resultStr;
	}

	/**
	 *
	 * Object 객체 Element 중 key와 일치하는 항목의 Index를 return한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * simpleSearch(new String[] {"a", "b", "c"}, "b") ===> 1 (Index는 0부터 시작)
	 * simpleSearch(new String[] {"a", "b", "c"}, "c") ===> 2
	 * simpleSearch(new String[] {"a", "b", "c"}, "d") ===> -1 (미포함)
	 *
	 * </pre>
	 *
	 * @param a
	 * @param key
	 * @return boolean
	 */
	public static int simpleSearch(Object [] a, Object key) {
		if (key==null || a == null)
			return -1;
		for (int i = 0; i < a.length; i++) {
			if (key.equals(a[i]))
				return i;
		}
		return -1;

	}

	public static String fixStrLength(String msg, int length, boolean isPadRight, String padValue) {

		if (msg == null) return null;

		byte[] len = msg.getBytes();

		String result = null;

		if (len.length <= length) {
			if (isPadRight)
				result = StringUtil.padRight(msg, padValue.charAt(0), length);
			else
				result = StringUtil.padLeft(msg, padValue.charAt(0), length);
		} else {
			// Length 만큼 자르기
			result = StringUtil.shortCutString(msg, length);
		}
		return result;
	}

	public static String cutStrLength(String input, int cutSize) {

		byte[] temp = input.getBytes();
		String str = "";

		int count = 0;

		for (int i=0; i<cutSize; i++) {
			if (temp[i]<0) count++;
		}

		// 한글이 포함되면 글자 깨지지 않도록 1을 더함
		if (count%2!=0) {
			str = new String(temp, 0, cutSize+1);
		} else {
			str = new String(temp, 0, cutSize);
		}

		return str;
	}

	/**
	 *
	 * 입력된 스트링(strTarget)에서 지정문자열(strSearch)을 포함하고 있는지를 체크한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * search("abcdefd","d")		===> true
	 * search("abc1def2d","12")	    ===> false
	 * </pre>
	 *
	 * @param strTarget
	 * @param strSearch
	 * @return 지정문자열이 검색되었으면 true를, 검색되지 않았으면 false를 반환한다.
	 */
	public static boolean includeStr(String strTarget, String strSearch) {

		if (search(strTarget, strSearch) > 0) return true;
		else return false;
	}

	/**
	 *
	 * 입력된 스트링(strType)에서 지정문자열(delim)을 삭제하고 값을 리턴한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * stringFormat("1000000-A00","-")		===> 1000000A00
	 * stringFormat("2005-12-10","-")		===> 20051210
	 * stringFormat("2005/12/10","/")		===> 20051210
	 * </pre>
	 *
	 * @param strType
	 * @param delim
	 * @return 스트링 문자열을 반환한다.
	 */

	public static String stringFormat( String strType ,String delim ) {
		String strResult = null;

		if ( StringUtil.isEmpty(strType ) ) {
			return  strResult;
		}
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(strType,delim);

		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		}
		return strResult = sb.toString();
	}

	/**
	 * 일정길이로 문자열을 잘라서 Array를 반환한다.
	 */
	public static String[] cutStringToArray(String input, int size, int arrCount) {
	    if (input == null) return null;

		byte[] temp = input.getBytes();
		String[] str = new String[arrCount];

		for (int i=0, start=0; i<arrCount; i++) {
		    int count=0, j=0;
			for (j=0; j<size && j+start < temp.length; j++) {
				if (temp[j+start]<0) count++;
			}

			// 한글이 포함되면 글자 깨지지 않도록 1을 더함
			if (j+start < temp.length) {
				if (count%2 != 0) {
					str[i] = new String(temp, start, ++j);
				} else {
					str[i] = new String(temp, start, j);
				}
			}
			start += j;
		}
		return str;
	}


    /**
     *
     * 특수문자를 변환한다.
     *
     * @param p_str
     * @return String
     *
     */
    public static String replaceYesHTML(String p_str) {
        if (p_str == null) {
            p_str = "";
        } else {
            p_str = p_str.trim();
            p_str = replace(p_str, "&", "&amp");
            p_str = replace(p_str, "'", "&#039");
            p_str = replace(p_str, "\"", "");
            p_str = replace(p_str, "##", "-");
        }

        return p_str;
    }

	/**
	 *
	 * 입력된 스트링(s)을 HTML 형태로 변환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * java2Html("\r\n \r\n")
	 * ===> <br>
	 *			<br>
	 *
	 * </pre>
	 *
	 * @param s
	 * @return java.lang.String
	 */
	public static String java2Html(String s) {
		if (s == null)
			return "";

		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == '&')
				buf.append("&amp;");
			else if (c[i] == '<')
				buf.append("&lt;");
			else if (c[i] == '>')
				buf.append("&gt;");
			else if (c[i] == '"')
				buf.append("&quot;");
			else if (c[i] == '\'')
				buf.append("&#039;");
			else if (c[i] == '\n')
				buf.append("<br>");
			else
				buf.append(c[i]);
		}
		return buf.toString();
	}

	/**
	 *
	 * 입력된 스트링(s)에서 carriage return 과 new line을 제거한 스트링을 반환한다.
	 *
	 * @param str
	 * @return java.lang.String
	 */
	public static String removeCRLF(String str) {
		return StringUtil.split(str, "\r\n\"");
	}

	/**
	 *
	 * 조회 > 계약상세조회 - History 기본탭 정보조회 에서 팝업 링크 반환
	 *
	 * @param policy
	 * @param tr_code
	 * @return String
	 *
	 */
	public static String getContractHistoryTransactionDetailPopupScript(String policy, String tr_code) {
	    String resultScript = "";
	    if ("BJ07".equals(tr_code) || "T862".equals(tr_code)
                || "BJ05".equals(tr_code) || "BG10".equals(tr_code)
                || "T032".equals(tr_code) || "T948".equals(tr_code)
                || "T934".equals(tr_code) || "TH09".equals(tr_code)
                || "B780".equals(tr_code) || "T919".equals(tr_code)
                || "T938".equals(tr_code) || "B834".equals(tr_code)
	    ) {
	        resultScript = "onClick=\"JavaScript:fncInquiryTransaction('"+policy+"','"+tr_code+"')\" style=\"cursor:hand\"";
	    }
	    return resultScript;
    }


	/**
	 * 주민번호에서 생일년월일을 생성한다.
	 * @param regstr_id
	 * @return String
	 */
    public static String getBirthday(String regstr_id) {
		if (regstr_id == null)
			return "";
        String temp1 = StringUtil.shortCutString(regstr_id, 6);//주민번호의 앞여섯자리를
                                                               // 리턴한다.
        if (StringUtil.subString(regstr_id, 8, 1).equals("1")
                || StringUtil.subString(regstr_id, 8, 1).equals("2")) {
            String birthday = "19" + temp1;
            return birthday;
        } else if (StringUtil.subString(regstr_id, 8, 1).equals("3")
                || StringUtil.subString(regstr_id, 8, 1).equals("4")) {
            String birthday1 = "20" + temp1;
            return birthday1;
        }
         return "";
	}


    /**
    *
    * BO를 통해 받은 2바이트 형태의 space를 제거
    *
    * @param srcStr
    *
    * @return birthday
    */
    public static String removeHostSpace(String srcStr) {
        return StringUtil.split(srcStr, String.valueOf((char) 12288)).trim();
    }
    
	/**
	 *
	 * 입력된 스트링(s)을 Script 형태로 변환한다.
	 *
	 * <pre>
	 *
	 * [사용 예제]
	 *
	 * java2Script("\r\n \r\n")
	 * ===> <br>
	 *			<br>
	 *
	 * </pre>
	 *
	 * @param s
	 * @return String
	 *
	 */
	public static String java2Script(String s) {
		if (s == null)
			return "";

		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == '"')
				buf.append("\"");
			else if (c[i] == '\'')
				buf.append("\\'");
			else if (c[i] == '\n')
				buf.append("\\n");
			else
				buf.append(c[i]);
		}
		return buf.toString();
	}
	

	/**
	 * 파일명의 확장자를 제외한 파일명을 구한다.
	 * 
	 * @param file_name
	 * @return String
	 */
	public static String getFileNameWithoutExtension(String file_name) {
		String name = file_name;
		
		int ext_index = file_name.lastIndexOf(".");
		
		if (ext_index > 0) {
			name = file_name.substring(0, ext_index);
		}
		
		return name;
	}
	
	/**
	 * 파일명의 확장자를 구한다.
	 * 
	 * @param file_name
	 * @return String
	 */
	public static String getFileExtension(String file_name) {
		
		String ext = "";
		
		int ext_index = file_name.lastIndexOf(".");
		
		if (ext_index > 0) {
			ext = file_name.substring(ext_index + 1, (file_name.length()));
			
			if(ext.length() > 6){
				ext = "";
			}
		}
		
		return ext;
	}
	
	/**
	 * 파일 전체 경로에서 폴더 경로를 구한다.
	 * 
	 * @param savePath
	 * @return String
	 */
	public static String getFolderPath(String savePath) {
		String folderPath = "";
		
		int file_index = savePath.lastIndexOf("/");
		
		if (file_index > 0) {
			folderPath = savePath.substring(0, file_index + 1);
		}

		return folderPath;
	}
	
	/**
	 * 문자열을 Html 포맷으로 변환한다. ('&', '<', '>' 처리)
	 * 
	 * @param s
	 * @return String
	 */
	public static String escapeHtml(String s) {
		if (s == null)
			return "";

		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == '&')
				buf.append("&amp;");
			else if (c[i] == '<')
				buf.append("&lt;");
			else if (c[i] == '>')
				buf.append("&gt;");
			else
				buf.append(c[i]);
		}
		return buf.toString();
	}
	
	/**
	 * 문자열을 CDATA 처리한다.
	 * 
	 * @param string
	 * @return String
	 */
	public static String setCDATA(String string) {
		String ret = "";
		
		if (string != null) {
			ret = "<![CDATA[";
			ret += string;
			ret += "]]>";
		}

		return ret;
	}
	
	/**
	 * 문자열에서 기호를 인코딩 처리한다.(" --> &quot; / ' --> &#039;)
	 * @param s
	 * @return String
	 */
	public static String translate(String s) {
		  
		  if (s == null)
		      return null;

		  StringBuffer buf = new StringBuffer();
		  char[] c = s.toCharArray();
		  int len = c.length;
		  for (int i = 0; i < len; i++) {
			  if (c[i] == '"')
		        buf.append("&quot;");
		      else if (c[i] == '\'')
		        buf.append("&#039;");
		      else
		        buf.append(c[i]);
		  }
		  return buf.toString();
	  }
	
	/***
	 * 이메일 주소 유효성 체크
	 * @param _email
	 * @return String
	 */
	public static boolean isValidEmail(String _email) { 
		if (_email==null) return false; 
		boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", _email.trim()); 
		return b; 
	}  

	
	/**
	 * 특수문자 치환처리
	 * @param str
	 * @return String
	 */
	public static String removeSpecialChar(String str) {
	        StringBuffer buf = new StringBuffer();
	        for (int i = 0; i < str.length(); i++) {
	            if (str.charAt(i) < 32) {
	                if (str.charAt(i) == 10 || str.charAt(i) == 13) {
	                    buf.append(str.charAt(i));
	                } else {
	                    buf.append(' ');
	                }
	            } else {
	                buf.append(str.charAt(i));
	            }
	        }
	        return buf.toString();
	 }
	
	
	/**
	 * 자바스크립트 패턴 문자열 처리 
	 * @param str
	 * @return String
	 */
    public static String getPatternReplace(String str){
    	
		String[] pattern = {"&lt;[sS][cC][rR][iI][pP][tT]","&lt;[jJ][aA][vV][aA][sS][cC][rR][iI][pP][tT]","&lt;[dD][iI][vV]"};
		StringBuffer sb = new StringBuffer();
		
		for(int ii=0; ii<pattern.length; ii++){ 
			Pattern pa = Pattern.compile(pattern[ii]);
			Matcher ma = pa.matcher(str);
			sb = new StringBuffer();
			
			while(ma.find()){
				if(ii == 0)
					ma.appendReplacement(sb, ma.group().substring(0,ma.group().length()-10)+"&lt;x-script");
				else if(ii == 1)
					ma.appendReplacement(sb, ma.group().substring(0,ma.group().length()-14)+"&lt;x-javascipt");
				else if(ii == 2)
					ma.appendReplacement(sb, ma.group().substring(0,ma.group().length()-7)+"&lt;x-div");
			}
			
			ma.appendTail(sb);
			
			str = sb.toString();
		}
		return str;
    }
    
    /**
     * 키워드 검색
     * <pre>Usage : StringUtil.getCharIndex(strKeyword)</pre>
     * @param strChar
     * @return String
     */
	public static String getCharIndex(String strChar) {
		String strKey = "";
		if (strChar.equals("ㄱ"))
			strKey = "'ㄱ','가','각','간','갇','갈','감','갑','값','갓','강','갖','갗','같','갚','갛','개','객','갠','갬','갭','갱','갸','걀','거','걱','건','걷','걸','검','겁','것','겄','겆','겉','겊','게','겐','겔','겜','겠','겨','격','견','겯','결','겸','겹','경','겼','곁','계','고','곡','곤','곧','골','공','곰','곱','곳','곶','과','곽','관','괌','광','괴','굉','교','구','국','군','굳','굴','굵','굶','굼','굽','궁','귀','규','균','귤','그','극','근','금','글','긁','급','긋','긍','기','긴','길','김','깃','깊','까','깍','깎','깐','깔','깜','깡','깨','꺼','꺽','껀','껄','껌','껍','껑','께','꼈','꼬','꼭','꼰','꼴','꼼','꼽','꼿','꽁','꽂','꽃','꽝','꽤','꾀','꾕','꾸','꾹','꾼','꿀','꿇','꿈','꿋','꿔','꿩','꿰','뀌','끄','끅','끈','끊','끌','끔','끗','끙','끝','끼','끽','낀','낄','낌','낑'";
		else if (strChar.equals("ㄴ"))
			strKey = "'ㄴ','나','낙','낚','난','날','남','납','낫','났','낭','낮','낯','낱','낳','내','낸','냄','냉','냇','냈','냐','냠','냥','너','넉','넋','넌','널','넘','넓','넒','넙','넛','넜','넝','넣','네','넵','넷','넸','녀','녁','년','녈','념','녑','녔','녕','녘','노','녹','논','놀','놈','놉','놋','농','높','놓','놔','났','뇌','뇨','누','눅','눈','눌','눕','눙','눠','눴','뉘','뉴','늉','느','늑','는','늘','늙','늚','늠','늡','늣','능','늦','늪','니','닉','닐','님','닙','닛','닝','닢'";
		else if (strChar.equals("ㄷ"))
			strKey = "'ㄷ','다','닥','닦','단','닫','달','닭','닮','닯','닳','담','답','닷','닸','당','닺','닻','닿','대','댁','댈','댐','댑','댓','댔','댕','댜','더','덕','덖','던','덛','덜','덞','덟','덤','덥','덧','덩','덫','덮','데','덴','델','뎀','뎁','뎅','뎌','도','독','돈','돋','돌','돎','돐','돔','돕','돗','동','돛','돝','돼','됐','되','된','될','됨','됩','두','둑','둔','둘','둠','둡','둥','둬','뒀','뒤','뒷','듀','듈','듐','드','득','든','듣','들','듦','듬','듭','듯','등','디','딕','딘','딛','딜','딤','딥','딧','딨','딩','딪','따','딱','딴','딸','땀','땁','땄','땅','땋','때','땐','땔','땜','땝','땟','땠','땡','떠','떡','떤','떨','떪','떫','떰','떱','떳','떴','떵','떻','떼','뗀','뗄','뗐','또','똑','똔','똘','똥','뙤','뚜','뚝','뚠','뚤','뚫','뚬','뚱','뛰','뛴','뛸','뜀','뜁','뜅','뜨','뜩','뜬','뜯','뜰','뜸','뜹','뜻','띄','띌','띔','띕','띠','띤','띨','띰','띱','띳','띵'";
		else if (strChar.equals("ㄹ"))
			strKey = "'ㄹ','라','락','란','랄','람','랍','랏','랐','랑','랒','랖','랗','래','랙','랜','랠','램','랩','랫','랬','랭','랴','략','럇','량','러','럭','런','럴','럼','럽','럿','렀','렁','렇','레','렉','렌','렐','렘','렙','렛','렝','려','력','련','렬','렴','렵','렷','렸','령','례','로','록','론','롤','롬','롭','롯','롱','뢰','뢸','룀','룁','룃','룅','료','룝','룟','룡','루','룩','룬','룰','룸','룹','룻','룽','뤄','뤘','뤼','뤽','륄','륑','류','륙','륜','률','륨','륩','륫','륭','르','륵','른','를','름','릅','릇','릉','릊','릎','리','릭','린','릴','림','립','릿','링'";
		else if (strChar.equals("ㅁ"))
			strKey = "'ㅁ','마','막','만','많','맏','말','맑','맘','맙','맛','망','맞','맡','맣','매','맥','맨','맬','맴','맵','맷','맸','맹','맺','먀','먈','먕','머','먹','먼','멀','멂','멈','멉','멋','멍','멎','멓','메','멕','멘','멜','멤','멥','멧','멨','멩','며','멱','면','멸','몃','몄','명','몇','모','목','몫','몬','몰','몲','몸','몹','못','몽','뫄','뫈','뫘','뫼','묀','묄','묍','묏','묑','묘','묜','묩','묫','무','묵','묶','문','묻','물','묽','묾','뭄','뭅','뭇','뭉','뭍','뭏','뭐','뭔','뭘','뭡','뭣','뮈','뮌','뮐','뮤','뮬','뮴','뮷','므','믄','믈','믐','믓','미','믹','민','믿','밀','밈','밉','밋','밌','밍','및','밑'";
		else if (strChar.equals("ㅂ"))
			strKey = "'ㅂ','바','박','밖','반','받','발','밝','밟','밤','밥','밧','방','밭','배','백','밴','밸','뱀','뱁','뱃','뱄','뱅','뱉','뱌','버','벅','번','벋','벌','범','법','벗','벙','벚','베','벡','벤','벧','벨','벰','벱','벳','벴','벵','벼','벽','변','별','볍','볏','볐','병','볕','보','복','볶','본','볼','봄','봅','봇','봉','봐','봔','봤','봬','뵀','뵈','뵉','뵌','뵐','뵘','뵙','뵤','부','북','분','붇','불','붉','붊','붐','붑','붓','붕','붙','붜','붤','붰','뷔','뷕','뷘','뷜','뷩','뷰','뷴','뷸','븀','븃','브','븍','븐','블','븜','븝','븟','비','빅','빈','빌','빔','빕','빗','빙','빚','빛','빠','빡','빤','빨','빰','빱','빳','빴','빵','빻','빼','빽','뺀','뺄','뺌','뺍','뺏','뺐','뺑','뺘','뺙','뺨','뻐','뻑','뻔','뻗','뻘','뻠','뻣','뻤','뻥','뻬','뼁','뼈','뼉','뼘','뼙','뼛','뼜','뼝','뽀','뽁','뽄','뽈','뽐','뽑','뽕','뾰','뿅','뿌','뿍','뿐','뿔','뿜','뿟','뿡','쀼','쁘','쁜','쁠','쁨','쁩','삐','삑','삔','삘','삠','삡','삣','삥'";
		else if (strChar.equals("ㅅ"))
			strKey = "'ㅅ','사','삭','삯','산','삳','살','삵','삶','삼','삽','삿','샀','상','샅','새','색','샌','샐','샘','샙','샛','샜','생','샤','샥','샨','샬','샴','샵','샷','샹','섀','서','석','섞','섟','선','섣','설','섦','섧','섬','섭','섯','섰','성','섶','세','섹','센','셀','셈','셉','셋','셌','셍','셔','셕','션','셜','셤','셥','셧','셨','셩','셰','소','속','솎','손','솔','솜','솝','솟','송','솥','솨','솩','솬','솰','솽','쇄','쇈','쇌','쇔','쇗','쇘','쇠','쇤','쇨','쇰','쇱','쇳','쇼','쇽','숀','숄','숌','숍','숏','숑','수','숙','순','숟','술','숨','숩','숫','숭','숯','숱','숲','숴','쉈','쉐','쉑','쉔','쉘','쉠','쉥','쉬','쉭','쉰','쉴','쉼','쉽','쉿','슁','슈','슉','슐','슘','슛','슝','스','슥','슨','슬','슭','슴','습','슷','승','시','식','신','싣','실','싫','심','십','싯','싱','싶','싸','싹','싼','쌀','쌈','쌉','쌌','쌍','쌓','쌔','쌕','쌘','쌜','쌤','쌥','쌨','쌩','썅','써','썩','썬','썰','썲','썸','썹','썼','썽','쎄','쎈','쎌','쎌','쏘','쏙','쏜','쏟','쏠','쏢','쏨','쏩','쏭','쏴','쏵','쏸','쐈','쐐','쐤','쐬','쐰','쐴','쐼','쐽','쑈','쑤','쑥','쑨','쑬','쑴','쑵','쑹','쒀','쒔','쒜','쒸','쒼','쓩','쓰','쓱','쓴','쓸','쓺','쓿','씀','씁','씌','씐','씔','씜','씨','씩','씬','씰','씸','씹','씻','씽'";
		else if (strChar.equals("ㅇ"))
			strKey = "'ㅇ','아','악','안','앉','않','알','앍','앎','앓','암','압','앗','았','앙','앝','앞','애','액','앤','앨','앰','앱','앳','앴','앵','야','약','얀','얄','얇','얌','얍','얏','양','얕','얗','얘','얜','얠','얩','어','억','언','얹','얻','얼','얽','얾','엄','업','없','엇','었','엉','엊','엌','엎','에','엑','엔','엘','엠','엡','엣','엥','여','역','엮','연','열','엶','엷','염','엽','엾','엿','였','영','옅','옆','옇','예','옌','옐','옘','옙','옛','옜','오','옥','온','올','옭','옮','옰','옳','옴','옵','옷','옹','옻','와','왁','완','왈','왐','왑','왓','왔','왕','왜','왝','왠','왬','왯','왱','외','왹','왼','욀','욈','욉','욋','욍','요','욕','욘','욜','욤','욥','욧','용','우','욱','운','울','욹','욺','움','웁','웃','웅','워','욱','원','월','웜','웝','웠','웡','웨','웩','웬','웰','웸','웹','웽','위','윅','윈','윌','윔','윕','윗','윙','유','육','윤','율','윰','윱','윳','융','윷','으','윽','은','을','읊','음','읍','읏','응','읒','읓','읔','읕','읖','읗','의','읜','읠','읨','읫','이','익','인','일','읽','읾','잃','임','입','잇','있','잉','잊','잎'";
		else if (strChar.equals("ㅈ"))
			strKey = "'ㅈ','자','작','잔','잖','잗','잘','잚','잠','잡','잣','잤','장','잦','재','잭','잰','잴','잼','잽','잿','쟀','쟁','쟈','쟉','쟌','쟎','쟐','쟘','쟝','쟤','쟨','쟬','저','적','전','절','젊','점','접','젓','정','젖','제','젝','젠','젤','젬','젭','젯','젱','져','젼','졀','졈','졉','졌','졍','조','족','존','졸','졺','좀','좁','좃','종','좆','좇','좋','좌','좍','좔','좝','좟','좡','좨','좼','좽','죄','죈','죌','죔','죕','죗','죙','죠','죡','죤','죵','주','죽','준','줄','줅','줆','줌','줍','줏','중','줘','줬','줴','쥐','쥑','쥔','쥘','쥠','쥡','쥣','쥬','쥰','쥴','쥼','즈','즉','즌','즐','즘','즙','즛','증','지','직','진','짇','질','짊','짐','집','짓','징','짖','짙','짚','짜','짝','짠','짢','짤','짧','짬','짭','짯','짰','짱','째','짹','짼','쨀','쨈','쨉','쨋','쨌','쨍','쨔','쨘','쨩','쩌','쩍','쩐','쩔','쩜','쩝','쩟','쩠','쩡','쩨','쩽','쪄','쪘','쪼','쪽','쫀','쫄','쫌','쫍','쫏','쫑','쫓','쫘','쫙','쫠','쫬','쫴','쬈','쬐','쬔','쬘','쬠','쬡','쭁','쭈','쭉','쭌','쭐','쭘','쭙','쭝','쭤','쭸','쭹','쮜','쮸','쯔','쯤','쯧','쯩','찌','찍','찐','찔','찜','찝','찡','찢','찧'";
		else if (strChar.equals("ㅊ"))
			strKey = "'ㅊ','차','착','찬','찮','찰','참','찹','찻','찼','창','찾','채','책','챈','챌','챔','챕','챗','챘','챙','챠','챤','챦','챨','챰','챵','처','척','천','철','첨','첩','첫','첬','청','체','첵','첸','첼','쳄','쳅','쳇','쳉','쳐','쳔','쳤','쳬','쳰','촁','초','촉','촌','촐','촘','촙','촛','총','촤','촨','촬','촹','최','쵠','쵤','쵬','쵭','쵯','쵱','쵸','춈','추','축','춘','출','춤','춥','춧','충','춰','췄','췌','췐','취','췬','췰','췸','췹','췻','췽','츄','츈','츌','츔','츙','츠','측','츤','츨','츰','츱','츳','층','치','칙','친','칟','칠','칡','침','칩','칫','칭'";
		else if (strChar.equals("ㅋ"))
			strKey = "'ㅋ','카','칵','칸','칼','캄','캅','캇','캉','캐','캑','캔','캘','캠','캡','캣','캤','캥','캬','캭','컁','커','컥','컨','컫','컬','컴','컵','컷','컸','컹','케','켁','켄','켈','켈','켑','켓','켕','켜','켠','켤','켬','켭','켯','켰','켱','코','콕','콘','콜','콤','콥','콧','콩','콰','콱','콴','콸','쾀','쾅','쾌','쾡','쾨','쾰','쿄','쿠','쿡','쿤','쿨','쿰','쿱','쿳','쿵','쿼','퀀','퀄','퀑','퀘','퀭','퀴','퀵','퀸','퀼','큄','큇','큉','큐','큔','큘','큠','크','큭','큰','클','큼','큽','킁','키','킥','킨','킬','킴','킵','킷','킹'";
		else if (strChar.equals("ㅌ"))
			strKey = "'ㅌ','타','탁','탄','탈','탉','탐','탑','탓','탔','탕','태','택','탠','탤','탬','탭','탯','탰','탱','탸','턍','터','턱','턴','털','턺','텀','텁','텃','텄','텅','테','텍','텐','텔','템','텝','텟','텡','텨','텬','텼','톄','톈','토','톡','톤','톨','톰','톱','톳','통','톺','톼','퇀','퇘','퇴','퇸','툇','툉','툐','투','툭','툰','툴','툼','툽','툿','퉁','퉈','퉜','퉤','튀','튁','튄','튈','튐','튑','튕','튜','튠','튤','튬','튱','트','특','튼','튿','틀','틂','틈','틉','틋','틔','틘','틜','틤','틥','티','틱','틴','틸','팀','팁','팃','팅'";
		else if (strChar.equals("ㅍ"))
			strKey = "'ㅍ','파','팍','팎','판','팔','팖','팜','팝','팟','팠','팡','팥','패','팽','팬','팰','펨','팹','팻','팼','팽','퍄','퍅','퍼','퍽','펀','펄','펌','펍','펏','펐','펑','페','펙','펜','펠','펨','펩','펫','펭','펴','편','펼','폄','폅','폈','평','폐','펠','펩','펫','포','폭','폰','폴','폼','폽','폿','퐁','퐈','퐝','푀','푄','표','푠','푤','푭','푯','푸','푹','푼','푿','풀','풂','품','풉','풋','풍','풔','풩','퓌','퓐','퓔','퓜','퓟','퓨','퓬','퓰','퓸','퓻','퓽','프','픈','플','픔','픕','픗','피','픽','핀','필','핌','핍','핏','핑'";
		else if (strChar.equals("ㅎ"))
			strKey = "'ㅎ','하','학','한','할','핥','함','합','핫','항','해','핵','핸','핼','햄','햅','햇','했','행','햐','향','허','헉','헌','헐','헒','험','헙','헛','헝','헤','헥','헨','헬','헴','헵','헷','헹','혀','혁','현','혈','혐','협','혓','혔','형','혜','혠','호','혹','혼','홀','홅','홈','홉','홋','홍','홑','화','확','환','활','홧','황','홰','획','횐','횃','횡','회','획','횐','횔','힙','횟','횡','효','횬','횰','횹','횻','후','훅','훈','훌','훑','훔','훗','훙','훠','훤','훨','훰','훵','훼','훽','휀','휄','휑','휘','휙','휜','휠','휨','휩','휫','휭','휴','휵','휸','휼','흄','흇','흉','흐','흑','흔','흖','흗','흘','흙','흠','흡','흣','흥','흩','희','흰','흴','흼','흽','힁','히','힉','힌','힐','힘','힙','힛','힝'";
		else if (strChar.equals("a"))
			strKey = "'a','b','c','d','e','A','B','C','D','E'";
		else if (strChar.equals("f"))
			strKey = "'f','g','h','i','j','F','G','H','I','J'";
		else if (strChar.equals("k"))
			strKey = "'k','l','m','n','o','K','L','M','N','O'";
		else if (strChar.equals("p"))
			strKey = "'p','q','r','s','t','P','Q','R','S','T'";
		else if (strChar.equals("u"))
			strKey = "'u','v','w','x','y','z','U','V','W','X','Y','X'";
		else if (strChar.equals("0"))
			strKey = "'0','1','2','3','4','5','6','7','8','9'";
		
		return strKey;
	}
	
	
	/**
	 * Today 문자열 가져오기
	 * @return String
	 */
	public static String getToday() {
		
		Calendar cal = Calendar.getInstance();	
		cal.setTime(new java.util.Date(System.currentTimeMillis()));
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());				
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : Today 문자열 가져오기
	 * 2. 처리내용 : yyyy-MM-dd HH:mm:ss
	 * </pre>
	 * @Method Name : getCurrentTime
	 * @return String
	 */
	public static String getCurrentTime() {
		
		 Date today = new Date();		
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 return sdf.format(today);
		 
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 하루전 날짜 문자열 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getYesterday
	 * @return String
	 */
	public static String getYesterday() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");			 
		Calendar cal = Calendar.getInstance();			 
		cal.add(Calendar.DATE,-1);		
		return sdf.format(cal.getTime());
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 숫자 입력받아 날짜 문자열 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getDay
	 * @param day
	 * @return String
	 */
	public static String getDay(int day) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");			 
		Calendar cal = Calendar.getInstance();			 
		cal.add(Calendar.DATE,-day);		
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Map에서 Key의 Value를 String으로 가져오기
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getMapString(Map<String,Object> map, String key) {
		String ret = "";
		if (map.get(key) != null) ret = map.get(key).toString();
		return ret;
	}
	
	public static String getMapString(Map<String, Object> map, String key, String replaceStr) {
		String ret = getMapString(map, key);
		if (ret == "") ret = replaceStr;
		return ret;
	}
	
	/**
	 * Map에서 Key의 Value를 Split하여 List<String>으로 가져오기
	 * @param map
	 * @param key
	 * @param regex
	 * @return
	 */
	public static List<String> getMapStringArray(Map<String,Object> map, String key, String regex) {
		List<String> returnList = new ArrayList<String>();
		
		if (map.get(key) != null) {
			String[] valueArray = map.get(key).toString().split(regex);
			Collections.addAll(returnList, valueArray);
		}
		
		return returnList;
	}
	
	/**
	 * Map에서 Key의 Value를 Int로 가져오기
	 * @param map
	 * @param key
	 * @return
	 */
	public static int getMapInteger(Map<String,Object> map, String key) {
		int ret = -1;
		if (map.get(key) != null) ret = Integer.valueOf(map.get(key).toString());
		return ret;
	}
	
	/**
	 * Map에서 Key의 Value를 Long으로 가져오기
	 * @param map
	 * @param key
	 * @return
	 */
	public static long getMapLong(Map<String, Object> map, String key) {
		long ret = (long) -1;
		if (map.get(key) != null) ret = Long.valueOf(map.get(key).toString());
		return ret;
	}
	
	/**
	 * Map에서 Key의 Value를 
	 * @param map
	 * @param key
	 * @return
	 */
	public static boolean getMapBoolean(Map<String, Object> map, String key) {
		boolean ret = false;
		if (map.get(key) != null) ret = Boolean.valueOf(map.get(key).toString());
		return ret;
	}
	/**
	 * 
	 * <pre>
	 * 1. 개용 : Calendar to String 변환
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : print
	 * @param cal
	 * @return String
	 */
	public static String print(Calendar cal)  {
		  
		  int year = cal.get(Calendar.YEAR);
		  int month = cal.get(Calendar.MONTH)+1;
		  int day = cal.get(Calendar.DATE);
		  
		  String str = String.format("%04d-%02d-%02d",year,month,day);
		  
		  return str;
	 }
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 파일사이즈 변경하기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : fileSize
	 * @param nUsed
	 * @return String
	 */
	public static String fileSize(long nUsed) {
		
		DecimalFormat df = new DecimalFormat(".#");		
		String ret = "";		
		double fileSize = 0;
		
		if (nUsed > 1024) {			
			
			fileSize = (double)nUsed / 1024;
			
			 if (fileSize > 1024) {
				 fileSize = (double)fileSize / 1024;
				 
				 if(fileSize > 1024) {
					 fileSize = (double)fileSize / 1024;
					 
					 if(fileSize > 1024) {
						 ret = df.format((double)fileSize/1024) + " TB";
					 }else {
						 ret = df.format((double)fileSize) + " GB";
					 }
				 }else {
					 ret = df.format((double)fileSize) + " MB";
				 }
			 } else {				 
				 ret = df.format((double)fileSize) + " KB";				 
			 }
			
		}else {
			if(nUsed == -1) {
				ret = "-1";
			}else {
				ret = (nUsed) + " B";
			}
		}

		return ret;
	}
	
	public String getText(String content) {  
		
		Pattern SCRIPTS = Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>",Pattern.DOTALL);  
		Pattern STYLE = Pattern.compile("<style[^>]*>.*</style>",Pattern.DOTALL);  
		Pattern TAGS = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>");  		
		Pattern ENTITY_REFS = Pattern.compile("&[^;]+;");  
		Pattern WHITESPACE = Pattern.compile("\\s\\s+");  
		          
		Matcher m;  
		          
		m = SCRIPTS.matcher(content);  
		content = m.replaceAll("");  
		
		m = STYLE.matcher(content);  
		content = m.replaceAll("");  
		
		m = TAGS.matcher(content);  
		content = m.replaceAll("");  
		
		m = ENTITY_REFS.matcher(content);  
		content = m.replaceAll("");  
		
		m = WHITESPACE.matcher(content);  
		content = m.replaceAll(" ");          
		  
		return content;  
	}  

}

