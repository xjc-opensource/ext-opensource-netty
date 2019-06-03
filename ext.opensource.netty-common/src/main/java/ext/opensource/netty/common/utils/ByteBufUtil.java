package ext.opensource.netty.common.utils;

import io.netty.buffer.ByteBuf;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ByteBufUtil {

    public  static byte[]  copyByteBuf(ByteBuf byteBuf){
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
