package kr.hhplus.be.server.domain.goods.repository;

import kr.hhplus.be.server.domain.goods.models.Goods;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface GoodsRepositoryCustom {
    Page<Goods> findGoodsList(Pageable pageable);
    Goods findGoodsDetail(Long goodsNo);
    Page<Goods> searchGoodsByName(String goodsNm, Pageable pageable);

}
