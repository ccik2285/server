package kr.hhplus.be.server.domain.goods.service;

import kr.hhplus.be.server.domain.goods.dto.response.GoodsPageResponse;
import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.repository.GoodsRepositoryCustom;
import kr.hhplus.be.server.domain.goods.usecase.GetGoodsDetailUseCase;
import kr.hhplus.be.server.domain.goods.usecase.GetGoodsListUseCase;
import kr.hhplus.be.server.domain.goods.usecase.SearchGoodsByNameUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchGoodsService {

    private final GetGoodsListUseCase getGoodsListUseCase;
    private final GetGoodsDetailUseCase getGoodsDetailUseCase;
    private final SearchGoodsByNameUseCase searchGoodsByNameUseCase;

    public SearchGoodsService(GetGoodsListUseCase getGoodsListUseCase,
                              GetGoodsDetailUseCase getGoodsDetailUseCase,
                              SearchGoodsByNameUseCase searchGoodsByNameUseCase) {
        this.getGoodsListUseCase = getGoodsListUseCase;
        this.getGoodsDetailUseCase = getGoodsDetailUseCase;
        this.searchGoodsByNameUseCase = searchGoodsByNameUseCase;
    }

    public Page<GoodsPageResponse> getGoodsList(int page, int size) {
        return getGoodsListUseCase.execute(page, size);
    }

    public Goods getGoodsDetail(Long goodsNo) {
        return getGoodsDetailUseCase.execute(goodsNo);
    }

    public Page<GoodsPageResponse> searchGoodsByName(String goodsNm, int page, int size) {
        return searchGoodsByNameUseCase.execute(goodsNm, page, size);
    }

}
