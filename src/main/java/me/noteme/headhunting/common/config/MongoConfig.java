package me.noteme.headhunting.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "me.noteme.headhunting.domain.resume.repository")
public class MongoConfig {
}
