package ext.opensource.netty.server.httpsocket;

import java.util.Map;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public abstract class BaseHttpResource {
	@Getter
	@Setter
	@NonNull
	private String rootDir = "";

	@Getter
	@Setter
	@NonNull
	private String defaultIndexName;

	private static String STR_SLASH = "/";

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
			return buildRes(defaultIndexName, parameters);
		} else {
			return buildRes(filename, parameters);
		}
	}

}
