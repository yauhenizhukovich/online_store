package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.List;

import com.gmail.yauhenizhukovich.app.service.ArticleService;
import com.gmail.yauhenizhukovich.app.service.model.ArticleDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {this.articleService = articleService;}

    @GetMapping
    public String getArticles(@RequestParam(required = false) Integer page, Model model) {
        if (page == null) {
            page = 1;
        }
        List<ArticleDTO> articles = articleService.getArticlesByPage(page);
        model.addAttribute("articles", articles);
        List<Integer> pages = articleService.getPages();
        model.addAttribute("pages", pages);
        return "articles";
    }

    @GetMapping("/{id}")
    public String getArticlePage(
            @PathVariable Long id,
            Model model
    ) {
        ArticleDTO article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article";
    }

}
