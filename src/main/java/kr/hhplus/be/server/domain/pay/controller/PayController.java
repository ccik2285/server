package kr.hhplus.be.server.domain.pay.controller;


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
    public ResponseEntity<String> processPayments(
            @RequestBody PayRequest paymentRequest) {
        try {
            payService.processPayments(paymentRequest.getOrdNo(), paymentRequest.getPayDetailRequests());
            return ResponseEntity.ok("결제 처리 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
