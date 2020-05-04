package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.ArticleService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserAccessDeniedException;
import com.gmail.yauhenizhukovich.app.service.model.article.AddArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.ArticlesDTO;
import com.gmail.yauhenizhukovich.app.service.model.article.UpdateArticleDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    public static final String DELETE_ARTICLE_FAIL_MESSAGE = "This article doesnt exist.";
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {this.articleService = articleService;}

    @GetMapping
    public String getArticles(
            @RequestParam(defaultValue = "1") Integer page,
            Model model
    ) {
        List<ArticlesDTO> articles = articleService.getArticlesByPage(page);
        model.addAttribute("articles", articles);
        int pages = articleService.getCountOfPages();
        model.addAttribute("pages", pages);
        return "articles";
    }

    @GetMapping("/{id}")
    public String getArticle(
            @PathVariable Long id,
            Model model
    ) {
        ArticleDTO article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        model.addAttribute("updatedArticle", article);
        return "article";
    }

    @PostMapping("/{id}/delete")
    public String deleteArticle(
            @PathVariable Long id
    ) {
        boolean isDeleted = articleService.deleteArticleById(id);
        if (!isDeleted) {
            return "redirect:/welcome?message=" + DELETE_ARTICLE_FAIL_MESSAGE;
        }
        return "redirect:/articles";
    }

    @PostMapping("/{id}")
    public String updateArticle(
            @PathVariable Long id,
            @ModelAttribute(name = "updatedArticle") @Valid UpdateArticleDTO updatedArticle,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            ArticleDTO article = articleService.getArticleById(id);
            model.addAttribute("article", article);
            model.addAttribute("update", true);
            return "article";
        }
        updatedArticle.setId(id);
        ArticleDTO article = articleService.updateArticle(updatedArticle);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/add")
    public String getAddArticlePage(
            Model model
    ) {
        AddArticleDTO article = new AddArticleDTO();
        model.addAttribute("article", article);
        return "add_article";
    }

    @PostMapping
    public String addArticle(
            @ModelAttribute(name = "article") @Valid AddArticleDTO article,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "add_article";
        }
        try {
            ArticleDTO addedArticle = articleService.addArticle(article);
            model.addAttribute("article", addedArticle);
            return "article";
        } catch (AnonymousUserException | UserAccessDeniedException e) {
            return "redirect:/articles?message=" + e.getMessage();
        }

    }

    @PostMapping("/{articleId}/comments/{commentId}")
    public String deleteComment(
            @PathVariable Long articleId,
            @PathVariable Long commentId
    ) {
        articleService.deleteCommentByArticleIdAndCommentId(articleId, commentId);
        return "redirect:/articles/" + articleId;
    }

}
