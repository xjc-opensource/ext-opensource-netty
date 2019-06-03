package ext.opensource.netty.server.mqtt.api;

import java.util.List;

import ext.opensource.netty.common.core.CacheList;
import ext.opensource.netty.server.mqtt.common.ProcedureMessage;
import ext.opensource.netty.server.mqtt.protocol.data.ProcedureClientData;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface ProcedureDataService {
	/**
	 * custom set procedure cache
	 * @param cacheList
	 */
	public void setProcedureCacheList(CacheList<ProcedureClientData> cacheList);
	
	/**
	 * removeByClient
	 * @param clientId
	 */
	public void removeByClient(String clientId);
	
	/**
	 * getPubRelMessageForClient
	 * @param clientId
	 * @return
	 */
	public List<ProcedureMessage> getPubRelMessageForClient(String clientId);
	
	/**
	 * getPubRelMessage
	 * @param clientId
	 * @param packId
	 * @return
	 */
	public ProcedureMessage getPubRelMessage(String clientId, int packId);
	
	/**
	 * removePubRelMessage
	 * @param clientId
	 * @param packId
	 * @return
	 */
	public ProcedureMessage removePubRelMessage(String clientId, int packId);
	
	/**
	 * putPubRelMessage
	 * @param clientId
	 * @param info
	 */
	public void putPubRelMessage(String clientId, ProcedureMessage info);
}
