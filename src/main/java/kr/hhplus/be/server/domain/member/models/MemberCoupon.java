package kr.hhplus.be.server.domain.member.models;


import jakarta.persistence.*;
import kr.hhplus.be.server.common.BaseEntity;
import kr.hhplus.be.server.common.CouponStateCd;
import kr.hhplus.be.server.common.CouponTypeCd;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@Table(name = "member_coupon")
public class MemberCoupon extends BaseEntity {

    @Id
    @Column(name = "mbr_no")
    private long mbrNo;
    @Column(name = "coupon_type_cd")
    private CouponTypeCd couponTypeCd;
    @Column(name = "coupon_state_cd")
    @Enumerated(EnumType.STRING)
    private CouponStateCd couponStateCd;
    @Column(name = "coupon_no")
    private long couponNo;

}
