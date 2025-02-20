package kr.hhplus.be.server.domain.goods.models;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.BaseEntity;
import kr.hhplus.be.server.common.GoodsStateCd;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
//@Table(name = "goods", indexes = @Index(name ="idx_goods_no", columnList = "goods_no"))
@Table(name = "goods")
public class Goods extends BaseEntity {

    @Id
    @Column(name = "goods_no")
    private long goodsNo;

    @Column(name = "goods_state_cd")
    @Enumerated(EnumType.STRING)
    private GoodsStateCd goodsStateCd;
    @Column(name = "goods_nm")
    private String goodsNm;
    @Column(name = "stock_quantity")
    private long stockQuantity;
    @Column(name = "sale_price")
    private long salePrice;

    @Builder
    public Goods(long goodsNo, GoodsStateCd goodsStateCd, String goodsNm, long stockQuantity, long salePrice) {
        this.goodsNo = goodsNo;
        this.goodsStateCd = goodsStateCd;
        this.goodsNm = goodsNm;
        this.stockQuantity = stockQuantity;
        this.salePrice = salePrice;
    }
    public Goods() {

    }
}
