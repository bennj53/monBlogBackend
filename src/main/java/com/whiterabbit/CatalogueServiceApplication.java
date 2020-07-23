package com.whiterabbit;

import com.whiterabbit.dao.ArticleRepository;
import com.whiterabbit.dao.CategoryRepository;
import com.whiterabbit.entities.Article;
import com.whiterabbit.entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.stream.Stream;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.whiterabbit.dao")
public class CatalogueServiceApplication {

	Logger log = LoggerFactory.getLogger(CatalogueServiceApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(CatalogueServiceApplication.class, args);

	}
////////
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/articles/add").allowedOrigins("*");
				registry.addMapping("/articles").allowedOrigins("*");
			}
		};
	}
//////////


	//@Bean
	CommandLineRunner start(CategoryRepository categoryRepository, ArticleRepository articleRepository){

		log.info("*****************************************************");
		CommandLineRunner cl= args -> {
			categoryRepository.deleteAll();
			Stream.of("C1 Developpement", "C2 Actualite").forEach(c->{
				categoryRepository.save(new Category(c.split(" ")[0],c.split(" ")[1],new ArrayList<>()));
			});
			categoryRepository.findAll().forEach(System.out::println);

			Category category = categoryRepository.findById("C1").get();
			articleRepository.deleteAll();
			Stream.of("a1","a2","a3","a4","a5","a6").forEach(name->{
				Article article = articleRepository.save(new Article(null,name,"bennj53", null, null, "resume", "contenu", null, null, category));
				category.getArticles().add(article);
				categoryRepository.save(category);
			});

			Category category2 = categoryRepository.findById("C2").get();
			Stream.of("a7","a8","a9","a10","a11","a12").forEach(name->{
				Article article = articleRepository.save(new Article(null,name,"bennj53", null, null, "resume", "contenu", null, null, category2));
				category2.getArticles().add(article);
				categoryRepository.save(category2);
			});

			articleRepository.findAll().forEach(System.out::println);
		};
		log.info("*****************************************************");
		return cl;
	}
}
