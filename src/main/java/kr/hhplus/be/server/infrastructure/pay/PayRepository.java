package kr.hhplus.be.server.infrastructure.pay;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.PayStateCd;
import kr.hhplus.be.server.common.PayTypeCd;
import kr.hhplus.be.server.domain.pay.models.QPayInfo;
import kr.hhplus.be.server.domain.pay.repository.PayRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
public class PayRepository implements PayRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QPayInfo payInfo = QPayInfo.payInfo;

    public PayRepository(JPAQueryFactory queryFactory, EntityManager entityManager) {
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public boolean payOrder(Long ordNo, PayTypeCd payTypeCd, Long payAmount, PayStateCd payStateCd) {
        return queryFactory.insert(payInfo)
                .columns(payInfo.orderDtlNo, payInfo.payTypeCd, payInfo.payAmount, payInfo.payStateCd)
                .values(ordNo, payTypeCd, payAmount, payStateCd)
                .execute() > 0;
    }

}
