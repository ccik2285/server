package kr.hhplus.be.server.infrastructure.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.OrderStateCd;
import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.domain.goods.models.QGoods;
import kr.hhplus.be.server.domain.order.dto.request.OrderDetailRequest;
import kr.hhplus.be.server.domain.order.models.Order;
import kr.hhplus.be.server.domain.order.models.QOrder;
import kr.hhplus.be.server.domain.order.models.QOrderDtl;
import kr.hhplus.be.server.domain.order.repository.OrderRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class OrderRepository implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrder order = QOrder.order;
    private final QOrderDtl orderDtl = QOrderDtl.orderDtl;

    public OrderRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    @Transactional
    public Long createOrder(Long mbrNo, LocalDateTime createdAt) {
        jpaQueryFactory.insert(order)
                .columns(order.mbrNo, order.createdAt, order.updatedAt)
                .values(mbrNo, createdAt, createdAt)
                .execute();
        Order lastOrder = jpaQueryFactory.selectFrom(order)
                .where(order.mbrNo.eq(mbrNo))
                .orderBy(order.createdAt.desc())
                .limit(1)
                .fetchOne();

        if (lastOrder != null) {
            return lastOrder.getOrderNo(); // 주문번호 반환
        }
        throw new RuntimeException("주문 생성에 실패했습니다.");
    }

    @Override
    @Transactional
    public Long createOrderDetail(Long ordNo, OrderDetailRequest orderData) {
        return jpaQueryFactory.insert(orderDtl)
                .columns(orderDtl.orderNo, orderDtl.goodsNo, orderDtl.orderQuantity, orderDtl.orderStateCd,orderDtl.createdAt, orderDtl.updatedAt)
                .values(ordNo, orderData.getGoodsNo(), orderData.getOrderQuantity(), OrderStateCd.ON_PROGRESS,LocalDateTime.now(), LocalDateTime.now())
                .execute();
    }

    @Override
    public void updateOrder() {

    }

    @Override
    public boolean decreaseStock(Long goodsNo, Long quantity) {
        Goods goods = jpaQueryFactory.selectFrom(QGoods.goods)
                .where(QGoods.goods.goodsNo.eq(goodsNo))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();

        if (goods == null || goods.getStockQuantity() < quantity) {
            return false; // 재고 부족
        }

        long affectedRows = jpaQueryFactory.update(QGoods.goods)
                .set(QGoods.goods.stockQuantity, QGoods.goods.stockQuantity.subtract(quantity))
                .where(QGoods.goods.goodsNo.eq(goodsNo))
                .where(QGoods.goods.stockQuantity.goe(quantity))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .execute();
        return affectedRows > 0; // 감소 성공
    }
}
