package me.noteme.headhunting.core.annotation;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "1")
public @interface CustomMockUser {
}
