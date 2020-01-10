package com.sky.car.common;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractObject implements Serializable, Cloneable {
    public static final long serialVersionUID = 1L;

    public AbstractObject() {
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Object clone() {
        Object obj = null;

        try {
            obj = super.clone();
        } catch (CloneNotSupportedException var3) {
            var3.printStackTrace();
        }

        return obj;
    }
}
