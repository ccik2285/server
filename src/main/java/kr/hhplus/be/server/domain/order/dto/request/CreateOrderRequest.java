package kr.hhplus.be.server.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateOrderRequest {
    private Long mbrNo;
    private List<OrderDetailRequest> orderDetails;
}
