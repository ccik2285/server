package kr.hhplus.be.server.infrastructure.goods;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.common.OrderStateCd;
import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.models.QGoods;
import kr.hhplus.be.server.domain.goods.repository.GoodsRepositoryCustom;
import kr.hhplus.be.server.domain.order.models.OrderDtl;
import kr.hhplus.be.server.domain.order.models.QOrderDtl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GoodsRepository implements GoodsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QGoods goods = QGoods.goods;
    private final QOrderDtl orderDtl = QOrderDtl.orderDtl;

    public GoodsRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Goods> findGoodsList(Pageable pageable) {

        List<Goods> content = jpaQueryFactory
                .selectFrom(goods)
                .orderBy(goods.goodsNm.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(
                jpaQueryFactory
                        .select(goods.count())
                        .from(goods)
                        .fetchOne()
        ).orElse(0L);


        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Goods findGoodsDetail(Long goodsNo) {

        return jpaQueryFactory
                .selectFrom(goods)
                .where(goods.goodsNo.eq(goodsNo))
                .fetchOne();
    }

    @Override
    public Page<Goods> searchGoodsByName(String goodsNm, Pageable pageable) {

        List<Goods> content = jpaQueryFactory
                .selectFrom(goods)
                .where(goods.goodsNm.containsIgnoreCase(goodsNm))
                .orderBy(goods.goodsNm.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(jpaQueryFactory
                .select(goods.count())
                .from(goods)
                .where(goods.goodsNm.containsIgnoreCase(goodsNm))
                .fetchOne()).orElse(0L);


        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public List<Goods> findTopSellingGoods() {
        return jpaQueryFactory
                .select(goods)
                .from(orderDtl)
                .join(goods).on(orderDtl.goodsNo.eq(goods.goodsNo))
                .where(orderDtl.orderStateCd.eq(OrderStateCd.COMPLETED))
                .groupBy(goods.goodsNo)
                .orderBy(orderDtl.orderQuantity.sum().desc())
                .limit(3)
                .fetch();
    }
}
