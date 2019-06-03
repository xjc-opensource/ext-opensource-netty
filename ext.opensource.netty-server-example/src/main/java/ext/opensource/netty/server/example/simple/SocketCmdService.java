package ext.opensource.netty.server.example.simple;

import org.springframework.stereotype.Component;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.common.TranDataProto;
import ext.opensource.netty.common.TranDataProtoUtil;
import ext.opensource.netty.server.simple.SocketCmdEvent;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Component
public class SocketCmdService implements SocketCmdEvent {
    @Override
    public TranDataProto doCmd(String cmd, String message) {
        NettyLog.info("业务层收到的数据:" + message);  
        return TranDataProtoUtil.getMsgInstance(10003, "中国人" + "\r\n");
    }
}