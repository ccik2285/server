package kr.hhplus.be.server.goods.integration;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.OrderStateCd;
import kr.hhplus.be.server.domain.goods.models.Goods;
import kr.hhplus.be.server.common.GoodsStateCd;
import kr.hhplus.be.server.domain.goods.repository.JpaGoodsRepository;
import kr.hhplus.be.server.domain.goods.service.GoodsService;
import kr.hhplus.be.server.domain.order.models.OrderDtl;
import kr.hhplus.be.server.domain.order.repository.JpaOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GoodsIndexPerformanceTest {


    @Autowired
    private GoodsService goodsService;

    private static final int TEST_DATA_COUNT = 100000;

    @Test
    void testPerformanceWithAndWithoutIndex() {
        long startTime = System.currentTimeMillis();
        List<Goods> result = goodsService.findTopSellingGoods();
        long endTime= System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("index test: " + duration + " ms");
        assertThat(result).hasSize(3);

    }


}