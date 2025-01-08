package kr.hhplus.be.server.domain.member.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.hhplus.be.server.common.BaseEntity;
import lombok.Getter;

@Entity
@Getter
@Table(name = "member_point")
public class MemberPoint extends BaseEntity {

    @Id
    @Column(name = "mbr_no")
    private long mbrNo;
    @Column(name = "available_point")
    private long AvailablePoint;
}
