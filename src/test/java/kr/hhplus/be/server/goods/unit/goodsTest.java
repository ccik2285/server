package kr.hhplus.be.server.goods.unit;


import kr.hhplus.be.server.common.GoodsStateCd;
import kr.hhplus.be.server.domain.goods.dto.response.GoodsPageResponse;
import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.repository.GoodsRepositoryCustom;
import kr.hhplus.be.server.domain.goods.service.SearchGoodsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class goodsTest {
    @Mock
    private GoodsRepositoryCustom goodsRepositoryCustom;

    @InjectMocks
    private SearchGoodsService searchGoodsService;

    @Test
    public void 상품리스트조회() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Goods> goodsList = List.of(
                new Goods(1L, GoodsStateCd.ON_SALE, "Product A", 100L, 1000L),
                new Goods(2L, GoodsStateCd.ON_SALE, "Product B", 150L, 1500L)
        );
        Page<Goods> goodsPage = new PageImpl<>(goodsList, pageable, goodsList.size());

        when(goodsRepositoryCustom.findGoodsList(pageable)).thenReturn(goodsPage);

        Page<GoodsPageResponse> result = searchGoodsService.getGoodsList(0, 10);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Product A", result.getContent().get(0).getGoodsNm());
        assertEquals(1000L, result.getContent().get(0).getSalePrice());
    }

    @Test
    public void 상품이름검색() {
        String goodsNm = "Product";
        Pageable pageable = PageRequest.of(0, 10);
        List<Goods> goodsList = List.of(
                new Goods(1L, GoodsStateCd.ON_SALE, "Product A", 100L, 1000L),
                new Goods(2L, GoodsStateCd.ON_SALE, "Product B", 150L, 1500L)
        );
        Page<Goods> goodsPage = new PageImpl<>(goodsList, pageable, goodsList.size());

        when(goodsRepositoryCustom.searchGoodsByName(goodsNm, pageable)).thenReturn(goodsPage);

        Page<GoodsPageResponse> result = searchGoodsService.searchGoodsByName(goodsNm, 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Product A", result.getContent().get(0).getGoodsNm());
        assertEquals(1000L, result.getContent().get(0).getSalePrice());
    }

    @Test
    public void 상품상세조회() {
        Long goodsNo = 1L;
        Goods goods = new Goods(goodsNo, GoodsStateCd.ON_SALE, "Product A", 100L, 1000L);

        when(goodsRepositoryCustom.findGoodsDetail(goodsNo)).thenReturn(goods);

        Goods result = searchGoodsService.getGoodsDetail(goodsNo);

        assertNotNull(result);
        assertEquals("Product A", result.getGoodsNm());
        assertEquals(1000L, result.getSalePrice());
    }
}
