package com.whiterabbit;

import com.whiterabbit.dao.ArticleRepository;
import com.whiterabbit.dao.CategoryRepository;
import com.whiterabbit.entities.Article;
import com.whiterabbit.entities.Category;
import com.whiterabbit.services.ArticleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.stream.Stream;

@SpringBootApplication
public class CatalogueServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(CatalogueServiceApplication.class, args);

	}
////////
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/articles/add").allowedOrigins("http://localhost:4200/*");
			}
		};
	}
//////////

	@Bean
	CommandLineRunner start(CategoryRepository categoryRepository, ArticleRepository articleRepository){

		System.out.println("*****************************************************");
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
		System.out.println("*****************************************************");
		return cl;
	}

	@Bean
	CommandLineRunner startScraping(ArticleService articleService, ArticleRepository articleRepository){
		CommandLineRunner cl= args -> {
			articleService.startScraping("JDG");
			articleService.startScraping("LESNUMS");
			articleService.startScraping("01NET");
			articleService.startScraping("MAC4EVER");
		};

		articleRepository.findAll().forEach(System.out::println);
		return cl;
	}

}
