package ext.opensource.netty.server.mqtt.protocol.data;

import ext.opensource.netty.server.mqtt.common.ProcedureMessage;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ProcedureClientData
		extends
			BaseDataInMap<Integer, ProcedureMessage> {
	private static final long serialVersionUID = 1L;

	public ProcedureClientData(String name) {
		super(name);
	}
	public ProcedureClientData() {

	}
}
