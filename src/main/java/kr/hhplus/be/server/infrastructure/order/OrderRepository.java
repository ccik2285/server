package kr.hhplus.be.server.infrastructure.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;

    public OrderRepository(JPAQueryFactory jpaQueryFactory, EntityManager entityManager) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Long createOrder(Long mbrNo) {

        Order newOrder = Order.builder()
                .mbrNo(mbrNo)
                .totalAmt(0L)
                .build();
        entityManager.persist(newOrder);
        entityManager.flush();
        return newOrder.getOrderNo();
    }

    @Override
    public void createOrderDetail(Long ordNo, OrderDetailRequest orderData) {
        jpaQueryFactory.insert(orderDtl)
                .columns(orderDtl.orderNo, orderDtl.goodsNo, orderDtl.orderQuantity, orderDtl.createdAt, orderDtl.updatedAt)
                .values(ordNo, orderData.getGoodsNo(), orderData.getOrderQuantity(), LocalDateTime.now(), LocalDateTime.now())
                .execute();
    }

    @Override
    public boolean updateOrderStatus(Long orderDtlNo, OrderStateCd orderStateCd) {
        long updatedRows = jpaQueryFactory.update(orderDtl)
                .set(orderDtl.orderStateCd, orderStateCd)
                .where(orderDtl.orderDtlNo.eq(orderDtlNo))
                .execute();

        return updatedRows > 0;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Long goodsNo, Long quantity) {

        Goods goods = jpaQueryFactory.selectFrom(QGoods.goods)
                .where(QGoods.goods.goodsNo.eq(goodsNo))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();

        if (goods == null || goods.getStockQuantity() < quantity) {
            return false;
        }

        long updatedRows = jpaQueryFactory.update(QGoods.goods)
                .set(QGoods.goods.stockQuantity, QGoods.goods.stockQuantity.subtract(quantity))
                .where(QGoods.goods.goodsNo.eq(goodsNo))
                .where(QGoods.goods.stockQuantity.goe(quantity))
                .execute();
        return updatedRows > 0;
    }
}
