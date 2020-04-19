package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.ArticleService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserAccessDeniedException;
import com.gmail.yauhenizhukovich.app.service.model.ArticleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class ArticleAPIController {

    public static final String DELETE_ARTICLE_MESSAGE = "Article successfully deleted.";
    private final ArticleService articleService;

    public ArticleAPIController(ArticleService articleService) {this.articleService = articleService;}

    @GetMapping
    public List<ArticleDTO> getArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public ArticleDTO getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Object addArticle(
            @RequestBody @Valid ArticleDTO article,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            List<ObjectError> bindingResultErrors = bindingResult.getAllErrors();
            bindingResultErrors.forEach(error -> errors.add(error.getDefaultMessage()));
            return errors;
        }
        try {
            return articleService.addArticle(article);
        } catch (AnonymousUserException | UserAccessDeniedException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/{id}")
    public String deleteArticleById(@PathVariable Long id) {
        articleService.deleteArticleById(id);
        return DELETE_ARTICLE_MESSAGE;
    }

}
