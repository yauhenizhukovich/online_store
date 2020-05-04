package com.gmail.yauhenizhukovich.app.web.controller.api;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.ArticleService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserAccessDeniedException;
import com.gmail.yauhenizhukovich.app.service.model.article.AddArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
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

import static com.gmail.yauhenizhukovich.app.web.controller.ArticleController.DELETE_ARTICLE_FAIL_MESSAGE;

@RestController
@RequestMapping("/api/articles")
public class ArticleAPIController {

    public static final String DELETE_ARTICLE_MESSAGE = "Article successfully deleted.";
    private final ArticleService articleService;

    public ArticleAPIController(ArticleService articleService) {this.articleService = articleService;}

    @GetMapping
    public List<ArticlesDTO> getArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public Object getArticle(
            @PathVariable Long id
    ) {
        ArticleDTO article = articleService.getArticleById(id);
        if (article == null) {
            return "This article doesnt exist.";
        }
        return article;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Object addArticle(
            @RequestBody @Valid AddArticleDTO article,
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
        boolean isDeleted = articleService.deleteArticleById(id);
        if (!isDeleted) {
            return DELETE_ARTICLE_FAIL_MESSAGE;
        }
        return DELETE_ARTICLE_MESSAGE;
    }

}
