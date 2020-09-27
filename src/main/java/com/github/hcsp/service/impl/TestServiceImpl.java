package com.github.hcsp.service.impl;

import com.github.hcsp.dao.TestDao;
import com.github.hcsp.entity.Order;
import com.github.hcsp.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {

    @Resource
    private TestDao testDao;

    @Override
    public List<Order> getOrderRank() {
        List<Order> orderRank = testDao.getOrderRank();
        return orderRank.stream().peek(order -> {
            if (order.getTotalPrice() == null) {
                order.setTotalPrice(0);
            }
        }).collect(Collectors.toList());
    }
}
