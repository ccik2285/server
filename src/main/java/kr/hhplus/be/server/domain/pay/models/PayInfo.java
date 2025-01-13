package kr.hhplus.be.server.domain.pay.models;


import jakarta.persistence.*;
import kr.hhplus.be.server.common.PayStateCd;
import kr.hhplus.be.server.common.PayTypeCd;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "pay_info")
public class PayInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_no")
    private long payNo;

    @Column(name = "order_dtl_no")
    private long orderDtlNo;

    @Column(name = "pay_type_cd")
    private PayTypeCd payTypeCd;

    @Column(name = "pay_amount")
    private long payAmount;

    @Column(name = "pay_state_cd")
    @Enumerated(EnumType.STRING)
    private PayStateCd payStateCd;

    @Builder
    public PayInfo(Long orderDtlNo, PayTypeCd payTypeCd, Long payAmount, PayStateCd payStateCd) {
        this.orderDtlNo = orderDtlNo;
        this.payTypeCd = payTypeCd;
        this.payAmount = payAmount;
        this.payStateCd = payStateCd;
    }

}
