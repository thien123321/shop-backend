package com.minhthien.web.shop.controller.cart;

import com.minhthien.web.shop.dto.cart.OrderResponse;
import com.minhthien.web.shop.entity.pay.Order;
import com.minhthien.web.shop.entity.product.Product;
import com.minhthien.web.shop.service.cart.CartService;
import com.minhthien.web.shop.service.pay.OrderService;
import com.minhthien.web.shop.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;
    private final ProductService productService;

    @PostMapping("/items")
    public ResponseEntity<?> addItem(
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(
                cartService.addItem(productId, quantity)
        );
    }


    @GetMapping
    public ResponseEntity<?> getCart() {
        return ResponseEntity.ok(
                cartService.getItems()
        );
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(
                cartService.updateQuantity(id, quantity)
        );
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
        return ResponseEntity.ok("REMOVED");
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok("CLEARED");
    }

    @PostMapping("/from-cart")
    public ResponseEntity<OrderResponse> createOrderFromCart() {
        Order order = orderService.createOrderFromCart();
        return ResponseEntity.ok(OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .amount(order.getAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .paidAt(order.getPaidAt())
                .build());
    }

}
