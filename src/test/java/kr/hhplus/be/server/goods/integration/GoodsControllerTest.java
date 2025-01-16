package kr.hhplus.be.server.goods.integration;

import kr.hhplus.be.server.api.GoodsController;
import kr.hhplus.be.server.common.GoodsStateCd;
import kr.hhplus.be.server.domain.goods.dto.response.GoodsPageResponse;
import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.service.GoodsService;
import kr.hhplus.be.server.domain.goods.service.SearchGoodsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GoodsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SearchGoodsService searchGoodsService;

    @Mock
    private GoodsService goodsService;

    @InjectMocks
    private GoodsController goodsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(goodsController).build();
    }

    @Test
    void 상품리스트검색테스트() throws Exception {
        GoodsPageResponse goodsResponse = new GoodsPageResponse(1L, "Test Goods", 1000L);
        Page<GoodsPageResponse> mockPage = new PageImpl<>(List.of(goodsResponse), PageRequest.of(0, 10), 1);

        Mockito.when(searchGoodsService.getGoodsList(anyInt(), anyInt())).thenReturn(mockPage);

        mockMvc.perform(get("/goods")
                        .header("Authorization", "Bearer valid-token")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].goodsNo").value(1))
                .andExpect(jsonPath("$.content[0].goodsNm").value("Test Goods"))
                .andExpect(jsonPath("$.content[0].salePrice").value(1000));
    }

    @Test
    void 상품상세테스트() throws Exception {
        Goods mockGoods = new Goods(1L, GoodsStateCd.ON_SALE,"Test Goods", 1,1000L);

        Mockito.when(searchGoodsService.getGoodsDetail(anyLong())).thenReturn(mockGoods);

        mockMvc.perform(get("/goods/1")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goodsNo").value(1))
                .andExpect(jsonPath("$.goodsNm").value("Test Goods"))
                .andExpect(jsonPath("$.salePrice").value(1000));
    }

    @Test
    void 상품이름으로검색테스트() throws Exception {
        GoodsPageResponse goodsResponse = new GoodsPageResponse(1L, "Test Goods", 1000L);
        Page<GoodsPageResponse> mockPage = new PageImpl<>(List.of(goodsResponse), PageRequest.of(0, 10), 1);

        Mockito.when(searchGoodsService.searchGoodsByName(anyString(), anyInt(), anyInt())).thenReturn(mockPage);

        mockMvc.perform(get("/goods/searchGoods/Test")
                        .header("Authorization", "Bearer valid-token")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].goodsNo").value(1))
                .andExpect(jsonPath("$.content[0].goodsNm").value("Test Goods"))
                .andExpect(jsonPath("$.content[0].salePrice").value(1000));
    }

    @Test
    void 상위판매상품조회테스트() throws Exception {
        Goods goods1 = new Goods(1L, GoodsStateCd.ON_SALE,"Goods A", 1,500L);
        Goods goods2 = new Goods(2L, GoodsStateCd.ON_SALE,"Goods B",2, 1000L);
        Goods goods3 = new Goods(3L, GoodsStateCd.ON_SALE,"Goods C", 3,1500L);

        Mockito.when(goodsService.findTopSellingGoods()).thenReturn(List.of(goods1, goods2, goods3));

        mockMvc.perform(get("/goods/findTopSellingGoods")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].goodsNo").value(1))
                .andExpect(jsonPath("$[0].goodsNm").value("Goods A"))
                .andExpect(jsonPath("$[0].salePrice").value(500))
                .andExpect(jsonPath("$[1].goodsNo").value(2))
                .andExpect(jsonPath("$[1].goodsNm").value("Goods B"))
                .andExpect(jsonPath("$[1].salePrice").value(1000))
                .andExpect(jsonPath("$[2].goodsNo").value(3))
                .andExpect(jsonPath("$[2].goodsNm").value("Goods C"))
                .andExpect(jsonPath("$[2].salePrice").value(1500));
    }
}
