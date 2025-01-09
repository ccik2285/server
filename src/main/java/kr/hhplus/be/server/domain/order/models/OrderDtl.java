package kr.hhplus.be.server.domain.order.models;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.BaseEntity;
import kr.hhplus.be.server.common.OrderStateCd;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "order_dtl")
public class OrderDtl extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_dtl_no")
    private long orderDtlNo;
    @Column(name = "order_no")
    private long orderNo;
    @Column(name = "goods_no")
    private long goodsNo;
    @Column(name = "order_quantity")
    private long orderQuantity;
    @Column(name = "price_amt")
    private long priceAmt;
    @Column(name = "order_state_cd")
    private OrderStateCd orderStateCd;

    @Builder
    public OrderDtl(long orderNo,long goodsNo,long orderQuantity,long priceAmt) {
        this.orderNo = orderNo;
        this.goodsNo = goodsNo;
        this.orderQuantity = orderQuantity;
        this.priceAmt = priceAmt;
    }
}
