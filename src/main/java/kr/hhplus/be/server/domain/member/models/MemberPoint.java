package kr.hhplus.be.server.domain.member.models;


import jakarta.persistence.*;
import kr.hhplus.be.server.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "member_point")
public class MemberPoint extends BaseEntity {

    @Id
    @Column(name = "mbr_no")
    private long mbrNo;
    @Column(name = "available_point")
    private long availablePoint;

    @Version
    @Column(name = "version")
    private Long version;

    @Builder
    public MemberPoint(Long mbrNo,Long availablePoint){
        this.mbrNo = mbrNo;
        this.availablePoint = availablePoint;
    }
}
