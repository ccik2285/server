package kr.hhplus.be.server.domain.member.controller;

import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.member.service.MemberPointService;
import kr.hhplus.be.server.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberPointService memberPointService;
    private final CouponService couponService;

    public MemberController(MemberService memberService, MemberPointService memberPointService, CouponService couponService) {
        this.memberService = memberService;
        this.memberPointService = memberPointService;
        this.couponService = couponService;
    }

    @GetMapping("/{mbrNo}/balance")
    public ResponseEntity<Long> getBalance(@PathVariable Long mbrNo) {
        Long balance = memberPointService.getBalance(mbrNo);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/{mbrNo}/charge")
    public ResponseEntity<String> rechargeBalance(@PathVariable Long mbrNo, @RequestBody Long amount) {
        memberPointService.chargeBalance(mbrNo, amount);
        return ResponseEntity.ok("충전 완료");
    }

    @PostMapping("/{mbrNo}/issue-coupon")
    public ResponseEntity<String> issueCoupon(
            @PathVariable Long mbrNo,
            @RequestParam Long couponNo) {
        boolean success = couponService.issueCoupon(mbrNo, couponNo);
        if (success) {
            return ResponseEntity.ok("쿠폰 발급 성공");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("쿠폰 발급 실패: 재고 부족");
        }
    }

}
