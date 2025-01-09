package kr.hhplus.be.server.domain.member.controller;

import kr.hhplus.be.server.domain.member.service.MemberPointService;
import kr.hhplus.be.server.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberPointService memberPointService;

    public MemberController(MemberService memberService, MemberPointService memberPointService) {
        this.memberService = memberService;
        this.memberPointService = memberPointService;
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

}
