package kr.hhplus.be.server;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;


import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testChargeBalance() {
        String url = "/api/member/charge";
        Map<String, Object> request = new HashMap<>();
        request.put("mbr_no", 12345);
        request.put("asset_type_cd", "POINT");
        request.put("amount", 1000);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        assertEquals("success", response.getBody().get("status"));
        assertEquals(1500, response.getBody().get("new_balance"));
    }

    @Test
    public void testGetGoods() {
        String url = "/api/goods/101";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        assertEquals("success", response.getBody().get("status"));
        assertEquals("상품 1", ((Map) response.getBody().get("goods")).get("goods_nm"));
    }

    @Test
    public void testIssueCoupon() {
        String url = "/api/coupon/issue";
        Map<String, Object> request = new HashMap<>();
        request.put("mbr_no", 12345);
        request.put("coupon_no", 12345);
        request.put("asset_type_cd", "COUPON");

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        assertEquals("success", response.getBody().get("status"));
        assertEquals("쿠폰이 발행되었습니다.", response.getBody().get("message"));
    }

    @Test
    public void testGetTopGoods() {
        String url = "/api/goods/top";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("상품 1", "상품 2");
    }

    @Test
    public void testPayment() {
        String url = "/api/pay";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestJson = """
            {
                "order_no": 123456,
                "payment_amount": 20000,
                "asset_type_cd": "COUPON"
            }
            """;

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("결제가 완료되었습니다.");
    }
}
