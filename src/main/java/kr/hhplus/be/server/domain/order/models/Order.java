package kr.hhplus.be.server.domain.order.models;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "order")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_no")
    private long orderNo;

    @Column(name = "mbr_no")
    private long mbrNo;
    @Column(name = "total_amt")
    private long totalAmt;

    @Builder
    public Order(Long mbrNo) {
        this.mbrNo = mbrNo;
    }

}
