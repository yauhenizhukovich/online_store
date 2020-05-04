package com.gmail.yauhenizhukovich.app.service.util;

public class PaginationUtil {

    public static final int COUNT_OF_USERS_BY_PAGE = 10;
    public static final int COUNT_OF_REVIEWS_BY_PAGE = 10;
    public static final int COUNT_OF_ARTICLES_BY_PAGE = 10;
    public static final int COUNT_OF_ITEMS_BY_PAGE = 10;

    public static int getStartPositionByPageNumber(int pageNumber, int objectsByPage) {
        return (pageNumber - 1) * objectsByPage;
    }

    public static int getCountOfPagesByCountOfObjects(Long countOfObjectsLong, int objectsByPage) {
        int countOfObjects = Math.toIntExact(countOfObjectsLong);
        double countOfPagesDouble = (double) countOfObjects / objectsByPage;
        return (int) Math.ceil(countOfPagesDouble);
    }

}
