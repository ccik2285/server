package kr.hhplus.be.server.domain.pay.dto.request;

import kr.hhplus.be.server.common.PayStateCd;
import kr.hhplus.be.server.common.PayTypeCd;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PayDetailRequest {
    private PayTypeCd payTypeCd;
    private Long payAmount;
    private PayStateCd payStateCd;
}
