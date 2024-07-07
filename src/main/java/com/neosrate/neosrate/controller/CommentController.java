package com.neosrate.neosrate.controller;

import com.neosrate.neosrate.data.dto.post.CommentDto;
import com.neosrate.neosrate.data.model.Comment;
import com.neosrate.neosrate.repository.CommentRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    final
    CommentRepository commentRepository;

    ModelMapper modelMapper = new ModelMapper();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<String> createComment(@RequestBody CommentDto commentData, @PathVariable Integer userId) {
        Set<ConstraintViolation<CommentDto>> val = validator.validate(commentData);

        if (!val.isEmpty()) {
            String err = val.iterator().next().getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
        }

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        commentData.setCreatedAt(formattedDate);

        commentData.setUserId(userId);

        commentData.setComment(commentData.getComment().lines()
                .map(line -> line.trim().replaceAll("\\s+", " "))
                .filter(line -> !line.isEmpty())
                .reduce((acc, line) -> acc + "\n" + line)
                .orElse(""));

        commentRepository.save(modelMapper.map(commentData, Comment.class));

        return ResponseEntity.status(HttpStatus.OK).body("Comment created.");
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllComment() {
        Iterable<Comment> allComment = commentRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(allComment);
    }

    @GetMapping("/get/all/{community}")
    public ResponseEntity<?> getAllCommentByCommunity(@PathVariable String community) {
        var allCommentByCommunity = commentRepository.findByCommunity(community);
        return ResponseEntity.status(HttpStatus.OK).body(allCommentByCommunity);
    }

    @DeleteMapping("/delete/{commentId}/{ownerId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        Optional<Comment> commentExist = commentRepository.findById(commentId);

        if (commentExist.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment don't exist.");
        }

        commentRepository.deleteById(commentId);

        return ResponseEntity.status(HttpStatus.OK).body("Comment deleted.");
    }
}
