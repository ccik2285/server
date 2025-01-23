package kr.hhplus.be.server.domain.order.models;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "`order`")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 방식 사용
    @Column(name = "order_no")
    private Long orderNo;

    @Column(name = "mbr_no")
    private long mbrNo;
    @Column(name = "total_amt")
    private Long totalAmt;

    @Builder
    public Order(Long orderNo,Long mbrNo,Long totalAmt) {
        this.orderNo = orderNo;
        this.mbrNo = mbrNo;
        this.totalAmt = totalAmt;
    }
    public Order() {
    }


}
