package kr.hhplus.be.server.domain.goods.repository;

import kr.hhplus.be.server.domain.goods.models.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaGoodsRepository extends JpaRepository<Goods,Long> {
    List<Goods> findAllByGoodsNo(Long goodsNo);
    List<Goods> findAllByGoodsNm(String goodsNm);
}
