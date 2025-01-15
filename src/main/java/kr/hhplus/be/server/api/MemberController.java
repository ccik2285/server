package kr.hhplus.be.server.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.hhplus.be.server.domain.coupon.usecase.IssueCouponUseCase;
import kr.hhplus.be.server.domain.member.usecase.ChargeBalanceUseCase;
import kr.hhplus.be.server.domain.member.usecase.GetBalanceUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/member")
public class MemberController {
    private final ChargeBalanceUseCase chargeBalanceUseCase;
    private final GetBalanceUseCase getBalanceUseCase;
    private final IssueCouponUseCase issueCouponUseCase;

    public MemberController(ChargeBalanceUseCase chargeBalanceUseCase, GetBalanceUseCase getBalanceUseCase, IssueCouponUseCase issueCouponUseCase) {
        this.chargeBalanceUseCase = chargeBalanceUseCase;
        this.getBalanceUseCase = getBalanceUseCase;
        this.issueCouponUseCase = issueCouponUseCase;
    }


    @GetMapping("/{mbrNo}/balance")
    @Operation(summary = "포인트 조회", description = "포인트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "포인트 조회 실패")
    })
    public ResponseEntity<Long> getBalance(@PathVariable Long mbrNo) {
        Long balance = getBalanceUseCase.execute(mbrNo);
        System.out.println("hello every: " + balance);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/{mbrNo}/charge")
    @Operation(summary = "포인트 충전", description = "포인트 충전")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 충전 성공"),
            @ApiResponse(responseCode = "400", description = "포인트 충전 실패")
    })
    public ResponseEntity<String> rechargeBalance(@PathVariable Long mbrNo, @RequestBody Long amount) {
        chargeBalanceUseCase.execute(mbrNo, amount);
        return ResponseEntity.ok("충전 완료");
    }

    @PostMapping("/{mbrNo}/issue-coupon")
    @Operation(summary = "쿠폰 발급", description = "쿠폰 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공"),
            @ApiResponse(responseCode = "400", description = "쿠폰 발급 실패")
    })
    public ResponseEntity<String> issueCoupon(
            @PathVariable Long mbrNo,
            @RequestParam Long couponNo) {
        boolean success = issueCouponUseCase.execute(mbrNo, couponNo);
        if (success) {
            return ResponseEntity.ok("쿠폰 발급 성공");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("쿠폰 발급 실패: 재고 부족");
        }
    }
}
