package com.woerry.wrbootjdbc.Data.Constant;

public enum OSType {
    DOS(1,"Windows系统"),UNIX(2,"Unix系统"),MAC(3,"Mac系统"){

    };
    private int value;
    private String name;

   private OSType(int i, String os) {
       this.value=i;
       this.name=os;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}

