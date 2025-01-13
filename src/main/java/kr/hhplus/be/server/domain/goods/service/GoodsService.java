package kr.hhplus.be.server.domain.goods.service;

import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.repository.GoodsRepositoryCustom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    private final GoodsRepositoryCustom goodsRepositoryCustom;

    public GoodsService(GoodsRepositoryCustom goodsRepositoryCustom) {
        this.goodsRepositoryCustom = goodsRepositoryCustom;
    }
    public List<Goods> findTopSellingGoods() {
        return goodsRepositoryCustom.findTopSellingGoods();
    }
}
