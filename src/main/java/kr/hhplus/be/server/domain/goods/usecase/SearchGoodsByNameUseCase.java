package kr.hhplus.be.server.domain.goods.usecase;

import kr.hhplus.be.server.domain.goods.dto.response.GoodsPageResponse;
import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.repository.GoodsRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchGoodsByNameUseCase {

    private final GoodsRepositoryCustom goodsRepositoryCustom;

    public SearchGoodsByNameUseCase(GoodsRepositoryCustom goodsRepositoryCustom) {
        this.goodsRepositoryCustom = goodsRepositoryCustom;
    }

    public Page<GoodsPageResponse> execute(String goodsNm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return convertToDtoPage(goodsRepositoryCustom.searchGoodsByName(goodsNm, pageable));
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
