package kr.hhplus.be.server.domain.order.models;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_no")
    private long orderNo;

    @Column(name = "mbr_no")
    private long mbrNo;

}
