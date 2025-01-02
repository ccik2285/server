package kr.hhplus.be.server.api.domain.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MockController {
    @PostMapping("/member/charge")
    public ResponseEntity<Map<String, Object>> chargeBalance(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        if ("POINT".equals(request.get("asset_type_cd"))) {
            response.put("status", "success");
            response.put("message", "잔액이 충전되었습니다.");
            response.put("new_balance", 1500);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "failure");
            response.put("message", "충전 실패. 자산 유형을 확인하세요.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/goods/{goods_no}")
    public ResponseEntity<Map<String, Object>> getGoods(@PathVariable("goods_no") int goodsNo) {
        Map<String, Object> response = new HashMap<>();
        if (goodsNo == 101) {
            Map<String, Object> goods = new HashMap<>();
            goods.put("goods_no", 101);
            goods.put("goods_nm", "상품 1");
            goods.put("sale_price", 10000);
            goods.put("stock_quantity", 50);
            response.put("status", "success");
            response.put("goods", goods);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "failure");
            response.put("message", "잘못된 상품 번호입니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/coupon/issue")
    public ResponseEntity<Map<String, String>> issueCoupon(@RequestBody Map<String, Object> request) {
        Map<String, String> response = new HashMap<>();
        if ("COUPON".equals(request.get("asset_type_cd"))) {
            response.put("status", "success");
            response.put("message", "쿠폰이 발행되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "failure");
            response.put("message", "쿠폰이 소진되었습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> completePayment(@RequestBody Map<String, Object> request) {
        Map<String, String> response = new HashMap<>();
        if (request.get("payment_amount") != null && Integer.parseInt(request.get("payment_amount").toString()) >= 20000) {
            response.put("status", "success");
            response.put("message", "결제가 완료되었습니다.");
            response.put("pay_no", "789012");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "failure");
            response.put("message", "결제 실패. 잔액이 부족합니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/goods/top")
    public ResponseEntity<Map<String, Object>> getTopGoods() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> topGoods = new ArrayList<>();
        Map<String, Object> goods1 = new HashMap<>();
        goods1.put("goods_no", 101);
        goods1.put("goods_nm", "상품 1");
        goods1.put("sale_price", 10000);
        goods1.put("sales_count", 200);
        Map<String, Object> goods2 = new HashMap<>();
        goods2.put("goods_no", 102);
        goods2.put("goods_nm", "상품 2");
        goods2.put("sale_price", 20000);
        goods2.put("sales_count", 150);
        topGoods.add(goods1);
        topGoods.add(goods2);
        response.put("status", "success");
        response.put("top_goods", topGoods);
        return ResponseEntity.ok(response);
    }

}
