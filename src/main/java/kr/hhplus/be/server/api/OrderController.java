package kr.hhplus.be.server.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.hhplus.be.server.domain.order.dto.request.CreateOrderRequest;
import kr.hhplus.be.server.domain.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/create")
    @Operation(summary = "주문서 생성", description = "주문서 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문서 생성 성공"),
            @ApiResponse(responseCode = "400", description = "주문서 생성 실패")
    })
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Long ordNo = orderService.createOrder(request.getMbrNo(), request.getOrderDetails());
            return ResponseEntity.ok(ordNo);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
