package ext.opensource.netty.server.httpsocket;

import java.util.HashMap;
import java.util.Map;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public abstract class BaseHttpResource {
	private static String STR_SLASH = "/";
	
	@Getter
	@Setter
	@NonNull
	private String rootDir = "";

	@Getter
	@Setter
	@NonNull
	private String defaultIndexName;
	
	@Getter
	@Setter
	private HttpResourceProcess httpResourceProcess;

	/**
	 * buildRes
	 * @param resPath
	 * @param parameters
	 * @return
	 */
	protected abstract ByteBuf buildRes(String resPath,
			Map<String, Object> parameters);

	public ByteBuf buildWebSocketRes(String filename,
			Map<String, Object> parameters) {
		if (STR_SLASH.equals(filename)) {
			return buildRes( defaultIndexName, parameters);
		} else {
			return buildRes(filename, parameters);
		}
	}
	
	public ByteBuf buildWebSocketRes(HttpRequest req, String filename) {
		String reqFileName = filename;
		if (STR_SLASH.equals(filename)) {
			reqFileName = defaultIndexName;
		} 
		
		Map<String, Object> reqParameter = new HashMap<String, Object>(16);
		if (httpResourceProcess != null) {
			httpResourceProcess.porcessResPath(req, reqFileName, reqParameter);
		}
		
		return buildRes(reqFileName, reqParameter);
	}

}
