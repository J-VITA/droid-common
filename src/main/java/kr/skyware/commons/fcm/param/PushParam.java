package kr.skyware.commons.fcm.param;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Builder
public class PushParam {
    private String token;

    public PushParam(){}
    public PushParam(String token){
        this.token = token;
    }

}
