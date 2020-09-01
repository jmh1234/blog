package com.github.hcsp.dao;

import com.github.hcsp.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestDao {

    List<Order> getOrderRank();
}
