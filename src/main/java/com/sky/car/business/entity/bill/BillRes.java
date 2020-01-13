package com.sky.car.business.entity.bill;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class BillRes implements Comparable<BillRes> {

    // 1:消费；2充值
    private Integer type;

    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date time;

    private BigDecimal price;

    private BigDecimal balance;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public int compareTo(BillRes o) {
        if (this.getTime().compareTo(o.getTime()) > 0){
            return 1;
        }
        return 0;
    }
}
