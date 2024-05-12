package com.patrykdankowski.financeflock.common;


import org.slf4j.LoggerFactory;

public class Logger {

    public static org.slf4j.Logger getLogger(Class<?> clazz) {

        return LoggerFactory.getLogger(clazz);
    }

}
