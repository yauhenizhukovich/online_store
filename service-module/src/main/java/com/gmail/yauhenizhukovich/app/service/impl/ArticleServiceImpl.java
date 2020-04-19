package com.gmail.yauhenizhukovich.app.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.ArticleRepository;
import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Article;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.service.ArticleService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserAccessDeniedException;
import com.gmail.yauhenizhukovich.app.service.model.ArticleDTO;
import com.gmail.yauhenizhukovich.app.service.model.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.util.ArticleConversionUtil;
import com.gmail.yauhenizhukovich.app.service.util.PaginationUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.service.model.AppUser.ROLE_PREFIX;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.COUNT_OF_ARTICLES_BY_PAGE;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<Integer> getPages() {
        Long countOfArticles = articleRepository.getCountOfObjects();
        return PaginationUtil.getCountOfPages(countOfArticles, COUNT_OF_ARTICLES_BY_PAGE);
    }

    @Override
    @Transactional
    public List<ArticleDTO> getArticlesByPage(Integer page) {
        int startPosition = PaginationUtil.getStartPositionByPageNumber(page, COUNT_OF_ARTICLES_BY_PAGE);
        List<Article> articles = articleRepository.getObjectsByStartPositionAndMaxResult(startPosition, COUNT_OF_ARTICLES_BY_PAGE);
        return articles.stream()
                .map(ArticleConversionUtil::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.getById(id);
        return ArticleConversionUtil.convertDatabaseObjectToDTO(article);
    }

    @Override
    @Transactional
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.getAll();
        return articles.stream()
                .map(ArticleConversionUtil::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteArticleById(Long id) {
        Article article = articleRepository.getById(id);
        articleRepository.delete(article);
    }

    @Override
    @Transactional
    public ArticleDTO addArticle(ArticleDTO articleDTO) throws AnonymousUserException, UserAccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (email.equals("anonymousUser")) {
            throw new AnonymousUserException("You should log in to add article.");
        }
        boolean isSecureAPIUser = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> Objects.equals(auth.getAuthority(), ROLE_PREFIX + RoleEnumService.SECURE_API_USER));
        if (!isSecureAPIUser) {
            throw new UserAccessDeniedException("Sorry, but you do not have access to this feature.");
        }
        Article article = ArticleConversionUtil.convertDTOToDatabaseObject(articleDTO);
        article.setDate(LocalDate.now());
        User user = userRepository.getUserByEmail("admin@gmail.com");
        article.setAuthor(user);
        article = articleRepository.add(article);
        articleDTO = ArticleConversionUtil.convertDatabaseObjectToDTO(article);
        return articleDTO;
    }

}
