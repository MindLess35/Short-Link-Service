package com.shortlink.webapp.mapper;

public interface Mapper<From, To> {
    To map(From obj);
}
