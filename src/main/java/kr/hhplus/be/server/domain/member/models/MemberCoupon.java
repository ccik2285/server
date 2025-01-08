package kr.hhplus.be.server.domain.member.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.hhplus.be.server.common.BaseEntity;
import kr.hhplus.be.server.common.CouponStateCd;
import kr.hhplus.be.server.common.CouponTypeCd;
import lombok.Getter;

@Entity
@Getter
@Table(name = "member_coupon")
public class MemberCoupon extends BaseEntity {

    @Id
    @Column(name = "mbr_no")
    private long mbrNo;
    @Column(name = "coupon_type_cd")
    private CouponTypeCd couponTypeCd;
    @Column(name = "coupon_state_cd")
    private CouponStateCd couponStateCd;
    @Column(name = "coupon_no")
    private long couponNo;



}
