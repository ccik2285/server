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
    public void createOrder(Long mbrNo) {
try {
    jpaQueryFactory.insert(order)
            .columns(order.mbrNo)
            .values(mbrNo)
            .execute();
    entityManager.flush();
}
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long getOrderNo(Long mbrNo) {
        try {
            Order lastOrder = jpaQueryFactory.selectFrom(order)
                    .where(order.mbrNo.eq(mbrNo))
                    .orderBy(order.createdAt.desc())
                    .limit(1)
                    .fetchOne();
            return lastOrder.getOrderNo();
       } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
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
    public boolean updateOrderStatus(Long orderDtlNo, OrderStateCd orderStateCd) {
        long updatedRows = jpaQueryFactory.update(orderDtl)
                .set(orderDtl.orderStateCd, orderStateCd)
                .where(orderDtl.orderDtlNo.eq(orderDtlNo))
                .execute();

        return updatedRows > 0;
    }

    @Override
    public boolean decreaseStock(Long goodsNo, Long quantity) {
        Goods goods = jpaQueryFactory.selectFrom(QGoods.goods)
                .where(QGoods.goods.goodsNo.eq(goodsNo))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();

        if (goods == null || goods.getStockQuantity() < quantity) {
            return false;
        }

        long affectedRows = jpaQueryFactory.update(QGoods.goods)
                .set(QGoods.goods.stockQuantity, QGoods.goods.stockQuantity.subtract(quantity))
                .where(QGoods.goods.goodsNo.eq(goodsNo))
                .where(QGoods.goods.stockQuantity.goe(quantity))
                .execute();
        return affectedRows > 0;
    }
}
