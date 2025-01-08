package kr.hhplus.be.server.domain.order.models;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.OrderStateCd;
import lombok.Getter;

@Entity
@Getter
@Table(name = "order_dtl")
public class OrderDtl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ord_dtl_no")
    private long OrdDtlNo;
    @Column(name = "goods_no")
    private long goods_no;
    private long quauntity;
    @Column(name = "order_state_cd")
    private OrderStateCd orderStateCd;
}
