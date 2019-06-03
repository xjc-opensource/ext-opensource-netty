package ext.opensource.netty.client.mqtt.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeMessage {
    private String topic;
    private int qos;
}