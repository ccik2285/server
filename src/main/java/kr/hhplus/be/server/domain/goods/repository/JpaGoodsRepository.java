package kr.hhplus.be.server.domain.goods.repository;

import kr.hhplus.be.server.domain.goods.models.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGoodsRepository extends JpaRepository<Goods,Long> {
}
