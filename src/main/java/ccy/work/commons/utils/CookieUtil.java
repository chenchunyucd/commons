package ccy.work.commons.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	
	private final static Log LOG = LogFactory.getLog(CookieUtil.class);

	/**
	 * @param response
	 * @cookieName     cookie名
	 * @cookieValue    cookie的值
	 * @cookieDomain   cookie要存入的域
	 * @time           cookie超时时间
	 */
	public static void setCookie(HttpServletResponse response,String cookieName,String cookieValue,String domain, Integer cookieMaxAge) {
		
		// HttpOnly
		StringBuffer vsp_c = new StringBuffer(200);
		vsp_c.append(cookieName).append("=").append(cookieValue).append(";");
		vsp_c.append(" Domain=").append(domain).append(";");
		vsp_c.append("max-age="+cookieMaxAge);  //过期时间更新为此方法   jixiaodong 
		vsp_c.append("; Path=").append("/");
		vsp_c.append(";");
		vsp_c.append(" HttpOnly");
		 
		response.addHeader("Set-Cookie",vsp_c.toString());
		
		if (LOG.isInfoEnabled()) {
			LOG.info("vsp_c:" + vsp_c.toString());
		}
		
	}
	
	
	public static Cookie setCookie( HttpServletResponse response, String name, String value, int maxAge, String path, String domain) {
        response.setHeader("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        if (path != null) {
            cookie.setPath(path);
        }
        cookie.setDomain(domain);
        response.addCookie(cookie);

        return cookie;
    }
	 
	/**
	 * @param response
	 */
	public static void clearCookies(HttpServletResponse response, Cookie cookie) {

		if (cookie != null) {
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}
	
	public static Cookie getCookie(HttpServletRequest request,String cookieName){

			//获取cookie数组
			Cookie[] cookies = request.getCookies();
			
			if(cookies == null){
				return null;
			}
			
			for (Cookie c : cookies) {
				if (cookieName.equals(c.getName())) {
					return c;
				}
			}
			return null;
	}

	public static String getCookieValue(HttpServletRequest request, String cookieName) {

		//获取cookie数组
		Cookie[] cookies = request.getCookies();
		
		if(cookies == null){
			return "";
		}
		
		for (Cookie c : cookies) {
			if (cookieName.equals(c.getName())) {
				return c.getValue();
			}
		}
		return "";
	}
}