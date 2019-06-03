package ext.opensource.netty.server.mqtt.protocol.process;

import java.util.ArrayList;
import java.util.List;
import ext.opensource.netty.common.NettyLog;
import ext.opensource.netty.server.mqtt.api.TopicService;
import ext.opensource.netty.server.mqtt.common.BorkerMessage;
import ext.opensource.netty.server.mqtt.common.RetainMessage;
import ext.opensource.netty.server.mqtt.common.SubscribeTopicInfo;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class TopicProcess {
	private TopicService topicService;
	
	public TopicProcess(TopicService topicService) {
		this.topicService = topicService;
	}

	/**
	 * 发布信息如带有保留
	 * @param bMsgInfo
	 */
	public void publicRetainMessage(BorkerMessage bMsgInfo) {
		byte[] msgBytes = bMsgInfo.getMsgBytes();
		String topicName = bMsgInfo.getTopicName();
		
		if (msgBytes.length == 0) {
			NettyLog.debug("save retain remove: {}", topicName);
			topicService.removeRetainMessage(topicName);
		} else {
			NettyLog.debug("save retain: {}", topicName);
			RetainMessage retainMessageStore = RetainMessage.builder().sourceClientId(bMsgInfo.getSourceClientId())
					.sourceMsgId(bMsgInfo.getSourceMsgId()).topicName(topicName).iQosLevel(bMsgInfo.getIQosLevel()).msgBytes(msgBytes)
					//.borkerMsgId(bMsgInfo.getBorkerMsgId())
					.build();
			topicService.putRetainMessage(topicName, retainMessageStore);
		}
	}
	
	/**
	 * 删除用户订阅主题
	 */
	public void removeClientTopic(String clientId, List<String> topicFilters) {
		topicFilters.forEach(topicFilter -> {
			topicService.remove(topicFilter, clientId);
			NettyLog.debug("UNSUBSCRIBE - clientId: {}, topicFilter: {}", clientId, topicFilter);
		});
	}
	
	/**
	 * 用户登录清除
	 */
	public void removeByCleanSession(String clientId) {
		topicService.removeForClient(clientId);
	}
	
	private boolean validTopicFilter(List<MqttTopicSubscription> topicSubscriptions) {
		return true;
	}
	
	public List<Integer> processTopicSubscribe(String clientId, List<MqttTopicSubscription> topicSubscriptions ) {
		if (this.validTopicFilter(topicSubscriptions)) {
			List<Integer> mqttQoSList = new ArrayList<Integer>();
			topicSubscriptions.forEach(topicSubscription -> {
				String topicFilter = topicSubscription.topicName();
				MqttQoS mqttQoS = topicSubscription.qualityOfService();
				SubscribeTopicInfo subscribeStore = new SubscribeTopicInfo(clientId, topicFilter, mqttQoS.value());
				topicService.put(topicFilter, subscribeStore);
				mqttQoSList.add(mqttQoS.value());
				NettyLog.debug("SUBSCRIBE - clientId: {}, topFilter: {}, QoS: {}", clientId, topicFilter,
						mqttQoS.value());
			});
			return mqttQoSList;
			
		} else {
			NettyLog.error("error Subscribe");
			return null;
		}
	}
	
	public List<RetainMessage> searchRetainMessage(String topicFilter) {
		return topicService.searchRetainMessage(topicFilter);
	}
	
	public List<SubscribeTopicInfo> search(String topicFilter) {
		return topicService.search(topicFilter);
	}
	
	public boolean checkVaildTopic(String topicName) {
		boolean flag = false;
		
		String oneLevelFlagStr = "#";
		String allLevelFlagStr = "*";
		if ((topicName != null) && (topicName.trim().length() > 0)) {
			if (topicName.contains(oneLevelFlagStr) || topicName.contains(allLevelFlagStr)) {
				flag = false;
			} else {
				flag = true;
			}
		}
		return flag;
	}
	
}
