package kr.hhplus.be.server.domain.coupon.models;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.BaseEntity;
import kr.hhplus.be.server.common.CouponTypeCd;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "coupon")
@Builder
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_no")
    private long coupon_no;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type_cd")
    private CouponTypeCd couponTypeCd;
    @Column(name = "percent_discount")
    private Long percentDiscount;
    @Column(name = "immediate_discount")
    private Long immediateDiscount;
    @Column(name = "tot_quantity")
    private long totQuantity;
    @Column(name = "stock_quantity")
    private long stockQuantity;

    public void decreaseStock() {
        if (this.stockQuantity > 0) {
            this.stockQuantity -= 1;
        }
    }

}
