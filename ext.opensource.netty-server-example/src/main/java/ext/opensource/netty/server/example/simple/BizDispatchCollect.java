package ext.opensource.netty.server.example.simple;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class BizDispatchCollect {
	private static Map<String, Object> coursesTable = new ConcurrentHashMap<>();

	public static void setCourses(Map<String, Object> courseMap) {
		coursesTable.clear();
		if (courseMap != null && courseMap.size() > 0) {
			for (Map.Entry<String, Object> entry : courseMap.entrySet()) {
				coursesTable.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public static Object getBizDispatchObject(String code) {
		return coursesTable.get(code);
	}

	public static void processMesage(String code, String dataContent) {
		//
	}

}
