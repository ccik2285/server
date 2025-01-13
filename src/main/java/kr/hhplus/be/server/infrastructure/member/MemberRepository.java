package kr.hhplus.be.server.infrastructure.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.member.models.QMemberPoint;
import kr.hhplus.be.server.domain.member.repository.MemberPointRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository implements MemberPointRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private static final QMemberPoint memberPoint = QMemberPoint.memberPoint;

    public MemberRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long getBalance(Long mbrNo) {
        return queryFactory.select(memberPoint.availablePoint)
                .from(memberPoint)
                .where(memberPoint.mbrNo.eq(mbrNo))
                .fetchOne();
    }

    @Override
    public void chargeBalance(Long mbrNo, Long amount) {
        queryFactory.update(memberPoint)
                .set(memberPoint.availablePoint, memberPoint.availablePoint.add(amount))
                .where(memberPoint.mbrNo.eq(mbrNo))
                .execute();
    }

    @Override
    public void useBalance(Long mbrNo, Long amount) {
        queryFactory.update(memberPoint)
                .set(memberPoint.availablePoint, memberPoint.availablePoint.subtract(amount))
                .where(memberPoint.mbrNo.eq(mbrNo))
                .execute();
    }
}
