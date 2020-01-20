package com.example.redis.token.api.jpa.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "tbl_order")
public class OrderEntity implements Serializable {

    @Id
    @GenericGenerator(name = "jpa_uuid",strategy = "uuid")
    @GeneratedValue(generator = "jpa_uuid",strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, columnDefinition = "varchar(64) comment '订单id'")
    private String orderId;

    @Column(nullable = false, columnDefinition = "varchar(100) comment '订单名称'")
    private String orderName;

    @Column(nullable = false, columnDefinition = "varchar(255) comment '订单描述'")
    private String orderDesc;

    @Column(columnDefinition = "varchar(255) comment '订单备注'")
    private String orderRemark;
    @Column(nullable = false, columnDefinition = "datetime default current_timestamp() comment '创建时间'")
    private Date createTime;

    @Column(nullable = false, columnDefinition = "datetime default current_timestamp() comment '更新时间'")
    private Date updateTime;

    @Column(columnDefinition = "datetime comment '删除时间'")
    private Date deleteTime;

    @Column(nullable = false, columnDefinition = "int(4) default 1 comment '订单状态：0已完成，已支付， 1 创建，待支付，2过期，订单超时未支付，3删除，取消'")
    private Integer orderStatus;

}
