package kr.hhplus.be.server.domain.pay.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PayRequest {
    private Long mbrNo;
    private Long ordNo;
    private List<PayDetailRequest> payDetailRequests;
}
