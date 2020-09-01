package com.github.hcsp.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Order implements Serializable{
    Integer id;
    Integer goods_id;
    Integer user_id;
    Integer price;
    Integer quantity;
    Integer totalPrice;
    Goods goods;
}
