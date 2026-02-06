package com.minhthien.web.shop.service.pay;

import com.minhthien.web.shop.entity.Auth.User;
import com.minhthien.web.shop.entity.cart.Cart;
import com.minhthien.web.shop.entity.cart.CartItem;
import com.minhthien.web.shop.entity.pay.Order;
import com.minhthien.web.shop.entity.pay.OrderItem;
import com.minhthien.web.shop.entity.product.Product;
import com.minhthien.web.shop.enums.OrderStatus;
import com.minhthien.web.shop.enums.OrderType;
import com.minhthien.web.shop.repository.cart.CartItemRepository;
import com.minhthien.web.shop.repository.cart.CartRepository;
import com.minhthien.web.shop.repository.pay.OrderItemRepository;
import com.minhthien.web.shop.repository.pay.OrderRepository;
import com.minhthien.web.shop.service.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    private Long getCurrentUserId() {
        return 1L;
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Optional<Order>  getByOrderCode(Long orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }
    @Transactional
    public Order cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Cannot cancel paid order");
        }

        // Hoàn stock
        for (OrderItem item : order.getOrderItems()) {

            Product product = item.getProduct();

            product.setStock(
                    product.getStock() + item.getQuantity()
            );

            productService.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCanceledAt(LocalDateTime.now());

        return orderRepository.save(order);
    }


    @Transactional
    public Order createOrderFromCart() {

        Long userId = getCurrentUserId();

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        order.setUser(user);
        order.setOrderType(OrderType.CART);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderCode(System.currentTimeMillis());
        order.setOrderItems(new ArrayList<>());

        BigDecimal totalAmount = BigDecimal.ZERO;

        // ===== CHECK + TRỪ STOCK =====
        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            if (item.getQuantity() > product.getStock()) {
                throw new RuntimeException(
                        product.getName() + " out of stock"
                );
            }

            // Trừ stock
            product.setStock(
                    product.getStock() - item.getQuantity()
            );
            productService.save(product);

            BigDecimal itemTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(item.getQuantity())
                            );

            totalAmount = totalAmount.add(itemTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());

            order.getOrderItems().add(orderItem);
        }

        order.setAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // clear cart
        cartItemRepository.deleteByCartId(cart.getId());

        return savedOrder;
    }

    @Transactional
    public Order createBuyNowOrder(
            User user,
            Product product,
            int quantity
    ) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (quantity > product.getStock()) {
            throw new RuntimeException("Product out of stock");
        }

        // Trừ stock
        product.setStock(product.getStock() - quantity);
        productService.save(product);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderType(OrderType.BUY_NOW);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderCode(System.currentTimeMillis());
        order.setOrderItems(new ArrayList<>());

        BigDecimal total =
                product.getPrice()
                        .multiply(BigDecimal.valueOf(quantity));

        order.setAmount(total);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());

        order.getOrderItems().add(item);

        return orderRepository.save(order);
    }




}
