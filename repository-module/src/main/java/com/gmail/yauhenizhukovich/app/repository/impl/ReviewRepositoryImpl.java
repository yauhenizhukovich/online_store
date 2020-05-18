package com.gmail.yauhenizhukovich.app.repository.impl;

import com.gmail.yauhenizhukovich.app.repository.ReviewRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {
}
