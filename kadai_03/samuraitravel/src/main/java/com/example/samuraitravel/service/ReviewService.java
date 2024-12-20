package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.ReviewRepository;

@Service
public class ReviewService {
	public final ReviewRepository reviewRepository;
	
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}
	
	//レビューをデータベースに反映
	 @Transactional
	    public void create(ReviewPostForm reviewPostForm, House house, User user) {
	        Review review = new Review();        

	        review.setHouse(house);
	        review.setUser(user);
	        review.setStar(reviewPostForm.getStar());
	        review.setComment(reviewPostForm.getComment());
	                    
	        reviewRepository.save(review);
	        
	    } 
	 
	//レビューをデータベースに反映
	 @Transactional
	    public void update(Review review, ReviewPostForm reviewPostForm, House house, User user) {
	              
	        review.setHouse(house);
	        review.setUser(user);
	        review.setStar(reviewPostForm.getStar());
	        review.setComment(reviewPostForm.getComment());
	                    
	        reviewRepository.save(review);
	 }
	 
	
	
	

}
