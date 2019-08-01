package com.lsfly.exception;

public class testExcepiton {
    public static void main(String[] args) {
        m(1);
    }
    public static void m(int i){
        if (i==1){
            throw  new ServiceException("嘻嘻嘻嘻嘻");
        }else {
            System.out.println("11111111");
        }

    }
}
