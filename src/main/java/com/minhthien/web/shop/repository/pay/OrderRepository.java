package com.minhthien.web.shop.repository.pay;

import com.minhthien.web.shop.entity.pay.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    Optional<Order> findByOrderCode(Long orderCode);
}
