package com.unidt.helper.common;

import java.util.UUID;

public class GetUUID32 {
    public static String getUUID32(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
