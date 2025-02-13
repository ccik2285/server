package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.models.OrderDtl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<OrderDtl, Long> {
}
