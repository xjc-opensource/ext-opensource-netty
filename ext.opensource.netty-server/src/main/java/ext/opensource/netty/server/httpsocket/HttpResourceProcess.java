
package ext.opensource.netty.server.httpsocket;

import java.util.Map;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @author ben
 * @Title: HttpResourceProcess.java
 * @Description:
 **/

public interface HttpResourceProcess {
	/**
	 * porcessResPath
	 * @param reqPath
	 * @param reqParameter
	 */
	public void porcessResPath(HttpRequest req, String reqPath, Map<String, Object> reqParameter);
}
