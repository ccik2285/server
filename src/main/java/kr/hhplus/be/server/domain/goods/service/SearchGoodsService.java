package kr.hhplus.be.server.domain.goods.service;

import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.repository.GoodsRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SearchGoodsService {

    private final GoodsRepositoryCustom goodsRepositoryCustom;

    public SearchGoodsService(GoodsRepositoryCustom goodsRepositoryCustom) {
        this.goodsRepositoryCustom = goodsRepositoryCustom;
    }

    public Page<Goods> getGoodsList(int page, int size){
        return goodsRepositoryCustom.findGoodsList(PageRequest.of(page, size));
    }

    public Goods getGoodsDetail(Long goodsNo) {
        return goodsRepositoryCustom.findGoodsDetail(goodsNo);
    }
    public Page<Goods> searchGoodsByName(String goodsNm, int page, int size) {
        return goodsRepositoryCustom.searchGoodsByName(goodsNm, PageRequest.of(page, size));
    }

}
