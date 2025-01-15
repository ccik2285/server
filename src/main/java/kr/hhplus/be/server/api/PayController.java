package kr.hhplus.be.server.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.hhplus.be.server.domain.pay.dto.request.PayRequest;
import kr.hhplus.be.server.domain.pay.service.PayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/pay")
public class PayController {
    private  final PayService payService;

    public PayController(PayService payService) {
        this.payService = payService;
    }

    @PostMapping("/process")
    @Operation(summary = "결제 생성", description = "결제 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 생성 성공"),
            @ApiResponse(responseCode = "400", description = "결제 생성 실패")
    })
    public ResponseEntity<String> processPayments(
            @RequestBody PayRequest paymentRequest) {
        try {
            payService.processPayments(paymentRequest.getMbrNo(),paymentRequest.getOrdNo(), paymentRequest.getPayDetailRequests());
            return ResponseEntity.ok("결제 처리 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
