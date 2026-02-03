package com.minhthien.web.shop.repository.pay;

import com.minhthien.web.shop.entity.pay.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
}
