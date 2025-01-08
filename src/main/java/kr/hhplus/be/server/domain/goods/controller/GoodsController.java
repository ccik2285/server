package kr.hhplus.be.server.domain.goods.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.domain.goods.dto.response.GoodsPageResponse;
import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.service.SearchGoodsService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final SearchGoodsService searchGoodsService;

    public GoodsController(SearchGoodsService searchGoodsService) {
        this.searchGoodsService = searchGoodsService;
    }

    /*----------viewStart------------*/

    @GetMapping
    public ResponseEntity<Page<GoodsPageResponse>> getGoodsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<GoodsPageResponse> goodsList = searchGoodsService.getGoodsList(page, size);
        return ResponseEntity.ok(goodsList);
    }

    @GetMapping("/{goodsNo}")
    public ResponseEntity<Goods> getGoodsDetail(@PathVariable Long goodsNo) {
        Goods goods = searchGoodsService.getGoodsDetail(goodsNo);
        return ResponseEntity.ok(goods);
    }

    @GetMapping("/searchGoods/{goodsNm}")
    public ResponseEntity<Page<GoodsPageResponse>> searchGoodsByName(
            @PathVariable String goodsNm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<GoodsPageResponse> searchResults = searchGoodsService.searchGoodsByName(goodsNm, page, size);
        return ResponseEntity.ok(searchResults);
    }


    /*----------viewEnd------------*/


    /*----------businessStart------------*/




    /*----------businessEnd------------*/
}
