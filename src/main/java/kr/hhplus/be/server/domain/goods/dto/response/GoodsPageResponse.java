package kr.hhplus.be.server.domain.goods.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class GoodsPageResponse {
    private final long goodsNo;
    private final String goodsNm;
    private final long salePrice;

    public GoodsPageResponse(long goodsNo, String goodsNm, long salePrice) {
        this.goodsNo = goodsNo;
        this.goodsNm = goodsNm;
        this.salePrice = salePrice;
    }
}
