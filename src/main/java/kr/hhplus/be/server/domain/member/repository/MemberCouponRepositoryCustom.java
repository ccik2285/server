package kr.hhplus.be.server.domain.member.repository;

import kr.hhplus.be.server.domain.member.models.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepositoryCustom extends JpaRepository<MemberCoupon,Long> {
}
