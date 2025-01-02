package kr.hhplus.be.server.domain.pay.models;


import jakarta.persistence.*;
import kr.hhplus.be.server.common.AssetTypeCd;
import kr.hhplus.be.server.common.PayStateCd;
import lombok.Getter;

@Getter
@Entity
@Table(name = "pay_info")
public class PayInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_no")
    private long payNo;

    @Column(name = "ord_no")
    private long ordNo;

    @Column(name = "asset_type_cd")
    private AssetTypeCd assetTypeCd;

    @Column(name = "pay_amount")
    private long payAmount;

    @Column(name = "pay_stat_cd")
    private PayStateCd payStateCd;

}
