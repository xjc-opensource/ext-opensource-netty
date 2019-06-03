package ext.opensource.netty.server.httpsocket;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MapUrlParamsUtil {
	/**
	 * 将url参数转换成map
	 *
	 * @param param
	 *            aa=11&bb=22&cc=33
	 * @return
	 */
	public static Map<String, Object> getUrlParams(String param) {
		Map<String, Object> map = new HashMap<String, Object>(0);
		if ((param == null || param.trim().length() == 0)) {
			return map;
		}
		String[] params = param.split("&");
		for (int i = 0; i < params.length; i++) {
			String[] p = params[i].split("=");
			if (p.length == 2) {
				map.put(p[0], p[1]);
			}
		}
		return map;
	}

	/**
	 * 将map转换成url
	 *
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, Object> map) {
		if (map == null) {
			return "";
		}
		String connectSymbol = "&";
		
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append(connectSymbol);
		}
		String s = sb.toString();
		if (s.endsWith(connectSymbol)) {
			s = s.substring(0, s.length() -1);
		}
		return s;
	}
}