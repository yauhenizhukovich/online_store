package com.gmail.yauhenizhukovich.app.repository.impl;

import com.gmail.yauhenizhukovich.app.repository.CommentRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {
}
