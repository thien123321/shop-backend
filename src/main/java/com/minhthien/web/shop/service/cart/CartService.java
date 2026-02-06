package com.minhthien.web.shop.service.cart;

import com.minhthien.web.shop.entity.cart.Cart;
import com.minhthien.web.shop.entity.cart.CartItem;
import com.minhthien.web.shop.entity.product.Product;
import com.minhthien.web.shop.repository.cart.CartItemRepository;
import com.minhthien.web.shop.repository.cart.CartRepository;
import com.minhthien.web.shop.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    // demo: tạm coi userId = 1
    private Long getCurrentUserId() {
        return 1L;
    }

    public Cart getOrCreateCart() {
        Long userId = getCurrentUserId();

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    return cartRepository.save(cart);
                });
    }

    public CartItem addItem(Long productId, Integer quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be > 0");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = getOrCreateCart();

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        int newQuantity = quantity;

        if (item != null) {
            newQuantity = item.getQuantity() + quantity;
        }

        // ===== CHECK STOCK =====
        if (newQuantity > product.getStock()) {
            throw new RuntimeException(
                    "Stock not enough. Available: " + product.getStock()
            );
        }

        if (item != null) {
            item.setQuantity(newQuantity);
            return cartItemRepository.save(item);
        }

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);

        return cartItemRepository.save(newItem);
    }


    public List<CartItem> getItems() {
        Cart cart = getOrCreateCart();
        return cartItemRepository.findByCartId(cart.getId());
    }

    public CartItem updateQuantity(Long itemId, Integer quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be > 0");
        }

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Product product = item.getProduct();

        // ===== CHECK STOCK =====
        if (quantity > product.getStock()) {
            throw new RuntimeException(
                    "Stock not enough. Available: " + product.getStock()
            );
        }

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }
    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public void clearCart() {
        Cart cart = getOrCreateCart();
        cartItemRepository.deleteByCartId(cart.getId());
    }
}
