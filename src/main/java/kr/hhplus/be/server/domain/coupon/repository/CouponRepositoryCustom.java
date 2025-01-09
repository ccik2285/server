package kr.hhplus.be.server.domain.coupon.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponRepositoryCustom extends JpaRepository<Coupon,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.couponNo = :couponNo")
    Optional<Coupon> findByIdForUpdate(@Param("couponNo") Long couponNo);
}
