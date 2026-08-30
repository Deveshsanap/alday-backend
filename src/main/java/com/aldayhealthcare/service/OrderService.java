package com.aldayhealthcare.service;

import com.aldayhealthcare.dto.OrderItemRequest;
import com.aldayhealthcare.dto.OrderItemResponse;
import com.aldayhealthcare.dto.OrderRequest;
import com.aldayhealthcare.dto.OrderResponse;
import com.aldayhealthcare.exception.InsufficientStockException;
import com.aldayhealthcare.exception.ResourceNotFoundException;
import com.aldayhealthcare.model.Order;
import com.aldayhealthcare.model.OrderItem;
import com.aldayhealthcare.model.Product;
import com.aldayhealthcare.model.User;
import com.aldayhealthcare.repository.OrderRepository;
import com.aldayhealthcare.repository.ProductRepository;
import com.aldayhealthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    @Transactional
    public OrderResponse createOrder(OrderRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
     // Converts the Map into a valid JSON string (e.g., {"city":"Pune", "zip":"411001"})
        String addressJson = "{}";
        if (request.getShippingAddress() != null) {
            StringBuilder sb = new StringBuilder("{");
            request.getShippingAddress().forEach((key, value) -> {
                sb.append("\"").append(key).append("\":\"").append(value).append("\",");
            });
            if (sb.length() > 1) sb.setLength(sb.length() - 1); // remove trailing comma
            sb.append("}");
            addressJson = sb.toString();
        }
        order.setShippingAddress(addressJson);
        order.setStatus("PENDING");

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // Server-side calculation and stock verification
        for (OrderItemRequest itemRequest : request.getItems()) {
            
            // 1. Extract the string and strip the "prod-" prefix sent by React
            String rawId = itemRequest.getProductId(); 
            Long cleanId = Long.valueOf(rawId.replace("prod-", ""));

            // 2. Fetch using the cleaned numeric ID
            Product product = productRepository.findById(cleanId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + cleanId));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getTitle());
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice()); // Lock in price at purchase time

            orderItems.add(orderItem);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);
        
        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(this::mapToOrderResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::mapToOrderResponse).toList();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setShippingAddress(order.getShippingAddress());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> itemResponses = order.getItems().stream().map(item -> {
            OrderItemResponse itemRes = new OrderItemResponse();
            // Fix: Re-apply the "prod-" prefix for the frontend order history
            itemRes.setProductId("prod-" + item.getProduct().getId());
            itemRes.setTitle(item.getProduct().getTitle());
            itemRes.setQuantity(item.getQuantity());
            itemRes.setPriceAtPurchase(item.getPrice());
            return itemRes;
        }).toList();

        response.setItems(itemResponses);
        return response;
    }
}