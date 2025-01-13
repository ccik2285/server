package kr.hhplus.be.server.domain.goods.usecase;

import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.repository.GoodsRepositoryCustom;
import org.springframework.stereotype.Component;

@Component
public class GetGoodsDetailUseCase {

    private final GoodsRepositoryCustom goodsRepositoryCustom;

    public GetGoodsDetailUseCase(GoodsRepositoryCustom goodsRepositoryCustom) {
        this.goodsRepositoryCustom = goodsRepositoryCustom;
    }

    public Goods execute(Long goodsNo) {
        return goodsRepositoryCustom.findGoodsDetail(goodsNo);
    }
}
