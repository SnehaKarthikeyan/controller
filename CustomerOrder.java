package com.blueose.Employee.Management.Model;

import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMER_ORDER")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String orderName;

    private long partyId;

    private String orderDate;

    private long deliveryBy;
    private String deliveryDate;
    private long billNo;

    private long client;

    private long customer;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cash_box", referencedColumnName = "id")
    private List<CashBox> cashBoxList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_order_items", referencedColumnName = "id")
    private List<CustomerOrderItems> items;

    @Builder.Default
    private String status = "ordered";
}
