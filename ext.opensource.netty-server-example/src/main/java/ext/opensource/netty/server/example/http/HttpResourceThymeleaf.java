package ext.opensource.netty.server.example.http;

import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import ext.opensource.netty.server.httpsocket.HttpResourceFile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class HttpResourceThymeleaf extends HttpResourceFile {
	private final TemplateEngine engine = new TemplateEngine();
	private final String HTML_FLAG = "html";
	private final String HTM_FLAG = "htm";

	private boolean isThymeleafFile(String resPath) {
		if (resPath == null) {
			return false;
		}
		String suffixName = resPath.substring(resPath.lastIndexOf(".") + 1);
		if (suffixName.equalsIgnoreCase(HTML_FLAG) || suffixName.equalsIgnoreCase(HTM_FLAG)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected ByteBuf buildRes(String resPath, Map<String, Object> parameters) {
		if (!isThymeleafFile(resPath)) {
			
			return super.buildRes(resPath, parameters);
		} else {
			try {
				Context context = new Context();
				context.setVariables(parameters);

				if (!engine.isInitialized()) {
					ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
					resolver.setPrefix(this.getRootDir());
					//设置不缓存
					resolver.setCacheable(false);
					engine.setTemplateResolver(resolver);
				}

				String htmlContext = engine.process(resPath, context);
				return Unpooled.copiedBuffer(htmlContext, CharsetUtil.UTF_8);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}
}