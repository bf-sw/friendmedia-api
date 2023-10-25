package org.product.common;

import lombok.AllArgsConstructor;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor  
public enum DateFormat {
    yyyymmmdd(DateTimeFormatter.ofPattern("yyyyMMdd")),
    yyyymmdd_with_dot(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
    yyyymmdd_with_dash(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    yyyymmdd_with_slash(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    yyyymmmddhhmiss_with_dot_and_colon(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")),
    yyyymmmddhhmiss_with_dash_and_colon(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    yyyymmmddhhmiss(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
    hhm(DateTimeFormatter.ofPattern("HH:m")),
    ;

    public DateTimeFormatter format;
}
