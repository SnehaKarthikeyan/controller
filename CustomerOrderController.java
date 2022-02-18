package com.blueose.Employee.Management.Controller;

import java.util.List;

import com.blueose.Employee.Management.Model.Bill;
import com.blueose.Employee.Management.Model.CashBox;
import com.blueose.Employee.Management.Model.CustomerOrder;
import com.blueose.Employee.Management.ServiceInterface.BillServiceInterface;
import com.blueose.Employee.Management.ServiceInterface.CustomerOrderServiceInterface;
import com.blueose.Employee.Management.Utils.Util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/customerOrder")
public class CustomerOrderController {
    Logger logger = LoggerFactory.getLogger(CustomerOrderController.class);

    @Autowired
    CustomerOrderServiceInterface service;

    @Autowired
    BillServiceInterface billService;

    @GetMapping
    public ResponseEntity<Object> getAllCustomerOrder() {
        List<CustomerOrder> customerOrders = service.getAllCustomerOrder();
        if (customerOrders.isEmpty()) {
            return Util.generateResponse("Customer Order was not found", HttpStatus.NO_CONTENT, null);
        }
        return Util.generateResponse("Successfully retrieved Customer Order!", HttpStatus.OK, customerOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerOrderById(@PathVariable long id) {
        CustomerOrder customerOrder = service.getCustomerOrderById(id);
        if (customerOrder == null) {
            return Util.generateResponse("Customer Order " + id + " not found", HttpStatus.NO_CONTENT, null);
        }
        return Util.generateResponse("Successfully retrieved Customer Order!", HttpStatus.OK, customerOrder);
    }

    @PostMapping
    public ResponseEntity<Object> createCustomerOrder(@RequestBody CustomerOrder customerOrder) {
        Bill bill = billService.getById(customerOrder.getBillNo());
        for(CashBox cashBox : customerOrder.getCashBoxList()) {
            bill.setTotal(bill.getTotal() - cashBox.getAmount());
        }
        billService.updateBill(bill);
        return Util.generateResponse("Successfully created an order", HttpStatus.CREATED,
                service.addCustomerOrder(customerOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCustomerOrder(@PathVariable Long id, @RequestBody CustomerOrder updateOrder) {
        CustomerOrder customerOrder = service.getCustomerOrderById(id);
        if (customerOrder == null) {
            return Util.generateResponse("Customer order " + id + " was not found ", HttpStatus.NOT_FOUND, null);
        }
        BeanUtils.copyProperties(updateOrder, customerOrder);
        customerOrder.setId(id);
        return Util.generateResponse("successfully Updated Customer Order!", HttpStatus.OK,
                service.updateCustomerOrder(customerOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomerOrder(@PathVariable Long id) {
        CustomerOrder customerOrder = service.getCustomerOrderById(id);
        if (customerOrder == null) {
            return Util.generateResponse("Customer Order " + id + " was not found", HttpStatus.BAD_REQUEST, null);
        }
        customerOrder.setStatus("cancelled");
        service.updateCustomerOrder(customerOrder);
        return Util.generateResponse("Successfully Deleted!", HttpStatus.OK, id);
    }

}
