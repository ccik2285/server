package kr.hhplus.be.server.domain.pay.repository;

import kr.hhplus.be.server.common.PayStateCd;
import kr.hhplus.be.server.common.PayTypeCd;

public interface PayRepositoryCustom {
    boolean payOrder(Long orderDtlNo, PayTypeCd payTypeCd, Long payAmount, PayStateCd payStateCd);
}
