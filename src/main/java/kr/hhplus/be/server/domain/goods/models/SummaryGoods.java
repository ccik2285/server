package kr.hhplus.be.server.domain.goods.models;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "summary_goods")
public class SummaryGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_no")
    private long goodsNo;

    @Column(name = "goods_nm")
    private String goodsNm;



}
