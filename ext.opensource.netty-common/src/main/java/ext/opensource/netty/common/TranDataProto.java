package ext.opensource.netty.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/


@Getter
@Setter
@Data
@AllArgsConstructor
@Builder
public class TranDataProto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int flag;
	private Integer sequence;
	private Integer code;
    private String content;
    
    public TranDataProto() {	
    } 
        
}