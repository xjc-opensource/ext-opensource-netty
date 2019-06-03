package ext.opensource.netty.server.httpsocket;

import java.io.InputStream;
import java.util.Map;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class HttpResourceFile extends BaseHttpResource {
	
	@Override
	protected ByteBuf buildRes(String resPath, Map<String, Object> parameters) {
		try {
			InputStream inputStream = HttpResourceFile.class.getResourceAsStream("/" + this.getRootDir() + resPath);
			if (inputStream != null) {
				int available = inputStream.available();
				if (available != 0) {
					byte[] bytes = new byte[available];
					inputStream.read(bytes);
					return ByteBufAllocator.DEFAULT.buffer(bytes.length).writeBytes(bytes);
				}
			}
		} catch (Exception e) {
		}
		return null;
	}	
}
