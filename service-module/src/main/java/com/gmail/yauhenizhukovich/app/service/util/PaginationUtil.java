package com.gmail.yauhenizhukovich.app.service.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PaginationUtil {

    public static final int COUNT_OF_USERS_BY_PAGE = 10;
    public static final int COUNT_OF_REVIEWS_BY_PAGE = 10;
    public static final int COUNT_OF_ARTICLES_BY_PAGE = 10;

    public static int getStartPositionByPageNumber(int pageNumber, int objectsByPage) {
        return (pageNumber - 1) * objectsByPage;
    }

    public static List<Integer> getCountOfPages(Long countOfObjectsLong, int objectsByPage) {
        int countOfObjects = Math.toIntExact(countOfObjectsLong);
        double countOfPagesDouble = (double) countOfObjects / objectsByPage;
        int countOfPages = (int) Math.ceil(countOfPagesDouble);
        return IntStream.range(1, countOfPages + 1)
                .boxed()
                .collect(Collectors.toList());
    }

}
