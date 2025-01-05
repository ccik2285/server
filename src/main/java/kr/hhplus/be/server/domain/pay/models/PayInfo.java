package kr.hhplus.be.server.domain.pay.models;


import jakarta.persistence.*;
import kr.hhplus.be.server.common.PayStateCd;
import kr.hhplus.be.server.common.PayTypeCd;
import lombok.Getter;

@Getter
@Entity
@Table(name = "pay_info")
public class PayInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_no")
    private long payNo;

    @Column(name = "ord_no")
    private long ordNo;

    @Column(name = "pay_type_cd")
    private PayTypeCd payTypeCd;

    @Column(name = "pay_amount")
    private long payAmount;

    @Column(name = "pay_state_cd")
    private PayStateCd payStateCd;

}
