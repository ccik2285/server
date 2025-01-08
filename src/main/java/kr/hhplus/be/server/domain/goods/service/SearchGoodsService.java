package kr.hhplus.be.server.domain.goods.service;

import kr.hhplus.be.server.domain.goods.dto.response.GoodsPageResponse;
import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.repository.GoodsRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchGoodsService {

    private final GoodsRepositoryCustom goodsRepositoryCustom;

    public SearchGoodsService(GoodsRepositoryCustom goodsRepositoryCustom) {
        this.goodsRepositoryCustom = goodsRepositoryCustom;
    }

    public Page<GoodsPageResponse> getGoodsList(int page, int size){
        return convertToDtoPage(goodsRepositoryCustom.findGoodsList(PageRequest.of(page, size)));
    }

    public Goods getGoodsDetail(Long goodsNo) {
        return goodsRepositoryCustom.findGoodsDetail(goodsNo);
    }
    public Page<GoodsPageResponse> searchGoodsByName(String goodsNm, int page, int size) {
        return convertToDtoPage(goodsRepositoryCustom.searchGoodsByName(goodsNm, PageRequest.of(page, size)));
    }

    private Page<GoodsPageResponse> convertToDtoPage(Page<Goods> goodsPage) {
        List<GoodsPageResponse> goodsDtoList = goodsPage.getContent().stream()
                .map(goods -> new GoodsPageResponse(
                        goods.getGoodsNo(),
                        goods.getGoodsNm(),
                        goods.getSalePrice()))
                .collect(Collectors.toList());

        return new PageImpl<>(goodsDtoList, goodsPage.getPageable(), goodsPage.getTotalElements());
    }

}
