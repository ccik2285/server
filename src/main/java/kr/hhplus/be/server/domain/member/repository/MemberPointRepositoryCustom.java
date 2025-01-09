package kr.hhplus.be.server.domain.member.repository;

public interface MemberPointRepositoryCustom {
    Long getBalance(Long mbrNo);
    void chargeBalance(Long mbrNo, Long amount);
    void useBalance(Long mbrNo, Long amount);
}
