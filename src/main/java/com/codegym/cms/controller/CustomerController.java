package com.codegym.cms.controller;

import com.codegym.cms.model.Customer;
import com.codegym.cms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    //Phương thức Get trả về list danh sách Customer.
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    //ResponseEntity: Đại diện cho toàn bộ phản hồi http.
    public ResponseEntity<List<Customer>> listAllCustomer(){
        List<Customer> customers = customerService.findAll();
        //Kiểm tra : nếu khách hàng rỗng thì báo lỗi. Ngược lại trả về trạng thái đã ok.
        if (customers.isEmpty()){
            return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<List<Customer>>(customers,HttpStatus.OK);
        }
    }
    //Lấy danh sách 1 khách hàng theo id.
    //produces = MediaType.APPLICATION_JSON_VALUE: Định dạng kiểu dữ liệu về kiểu json.
    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> getOneCustomer(@PathVariable Long id){
        Customer customer = customerService.findById(id);
        if (customer == null){
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<Customer>(customer,HttpStatus.OK);
        }
    }
    //Thêm mới Customers.
    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer, UriComponentsBuilder builder){
        customerService.save(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("customers/{id}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    //Update Customers.
    @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer){
        Customer currentCustomer = customerService.findById(id);
        if (currentCustomer == null){
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
        currentCustomer.setFirstName(customer.getFirstName());
        currentCustomer.setLastName(customer.getLastName());
        currentCustomer.setId(customer.getId());

        customerService.save(currentCustomer);
        return new ResponseEntity<Customer>(HttpStatus.OK);
    }
    //Delete Customer.
    @RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Long id){
        Customer customer = customerService.findById(id);
        if (customer == null){
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }else {
            customerService.delete(id);
            return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
        }


    }






}
