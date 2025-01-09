package kr.hhplus.be.server.domain.order.dto.request;

import lombok.Data;

@Data
public class OrderDetailRequest {
    private Long goodsNo;
    private Long orderQuantity;
    private Long priceAmt;
}
