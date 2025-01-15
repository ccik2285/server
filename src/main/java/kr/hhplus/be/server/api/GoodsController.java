package kr.hhplus.be.server.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.hhplus.be.server.domain.goods.dto.response.GoodsPageResponse;
import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.service.GoodsService;
import kr.hhplus.be.server.domain.goods.service.SearchGoodsService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    private final SearchGoodsService searchGoodsService;
    private final GoodsService goodsService;

    public GoodsController(SearchGoodsService searchGoodsService, GoodsService goodsService) {
        this.searchGoodsService = searchGoodsService;
        this.goodsService = goodsService;
    }

    @GetMapping
    @Operation(summary = "상품페이지 단위 조회", description = "상품페이지 단위 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품페이지 단위 조회 성공"),
            @ApiResponse(responseCode = "400", description = "상품페이지 단위 조회 실패")
    })
    public ResponseEntity<Page<GoodsPageResponse>> getGoodsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<GoodsPageResponse> goodsList = searchGoodsService.getGoodsList(page, size);
        return ResponseEntity.ok(goodsList);
    }

    @GetMapping("/{goodsNo}")
    @Operation(summary = "상품상세 조회", description = "상품상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "상품상세 조회 실패")
    })
    public ResponseEntity<Goods> getGoodsDetail(@PathVariable Long goodsNo) {
        Goods goods = searchGoodsService.getGoodsDetail(goodsNo);
        return ResponseEntity.ok(goods);
    }

    @GetMapping("/searchGoods/{goodsNm}")
    @Operation(summary = "상품이름으로 조회", description = "상품이름으로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품이름으로 조회 성공"),
            @ApiResponse(responseCode = "400", description = "상품이름으로 조회 실패")
    })
    public ResponseEntity<Page<GoodsPageResponse>> searchGoodsByName(
            @PathVariable String goodsNm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<GoodsPageResponse> searchResults = searchGoodsService.searchGoodsByName(goodsNm, page, size);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/findTopSellingGoods")
    @Operation(summary = "판매순위 상위 3개 상품 조회", description = "판매순위 상위 3개 상품 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "판매순위 상위 3개 상품 조회 성공"),
            @ApiResponse(responseCode = "400", description = "판매순위 상위 3개 상품 조회 실패")
    })
    public ResponseEntity<List<Goods>> findTopSellingGoods(){
        return ResponseEntity.ok(goodsService.findTopSellingGoods());
    }
}
