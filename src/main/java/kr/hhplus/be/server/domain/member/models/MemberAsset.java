package kr.hhplus.be.server.domain.member.models;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.AssetStateCd;
import kr.hhplus.be.server.common.AssetTypeCd;
import lombok.Getter;

@Getter
@Entity
@Table(name = "member_asset")
public class MemberAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mbr_no")
    private long mbrNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type_cd")
    private AssetTypeCd assetTypeCd;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_stat_cd")
    private AssetStateCd assetStateCd;


}
