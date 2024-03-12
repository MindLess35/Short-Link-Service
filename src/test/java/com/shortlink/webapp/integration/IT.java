package com.shortlink.webapp.integration;

import com.shortlink.webapp.WebappApplicationTests;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Transactional
@ActiveProfiles("test")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = WebappApplicationTests.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public @interface IT {
}
