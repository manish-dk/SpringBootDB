package com.example.DatabaseConnection.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.example.DatabaseConnection.exception.ResourceNotFoundException;
import com.example.DatabaseConnection.model.Order;
import com.example.DatabaseConnection.repository.MySpringBootRepository;
import com.example.DatabaseConnection.repository.OrderRepository;

@RestController
public class PersonController {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private MySpringBootRepository mySpringBootRepository;
	
	@GetMapping("/person/{personId}/orders")
	public Page<Order> getAllOrdersByPersonId(@PathVariable (value = "person_id") Long personId, Pageable pageable) {
		return orderRepository.findByPersonId(personId,  pageable);
	}
	
	@PostMapping("/person/{personId}/orders")
	public Order createComment(@PathVariable (value="personId") Long personId, @Valid @RequestBody Order order) {
		return mySpringBootRepository.findById(personId).map(mySpringBootDataModel -> {
			order.setPerson(mySpringBootDataModel);
			return orderRepository.save(order);
		}).orElseThrow(() -> new ResourceNotFoundException("Person","id",order));
	}
	
	@PutMapping("/person/{personId}/orders/{orderId}")
	public Order updateOrder(@PathVariable (value="personId") Long personId, @PathVariable (value="orderId") Long orderId, @Valid @RequestBody Order orderRequest) {
		if(!mySpringBootRepository.existsById(personId)) {
			throw new ResourceNotFoundException("Person","id", orderRequest);
		}
		return orderRepository.findById(orderId).map(order -> {
			order.setTitle(orderRequest.getTitle());
			return orderRepository.save(order);
		}).orElseThrow(() -> new ResourceNotFoundException("Person","id", orderRequest));
	}
	
	@DeleteMapping
}
