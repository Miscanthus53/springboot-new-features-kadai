package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;


@Controller
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final ReviewService reviewService;
	
	public ReviewController(ReviewRepository reviewRepository, ReviewService reviewService, HouseRepository houseRepository) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
		this.reviewService = reviewService;
	}

	//レビュー一覧を表示
	@GetMapping("/review/{id}")
	public String show(@PathVariable(name = "id") Integer id, @PageableDefault(page = 0, size = 9, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
        
        Page<Review> reviewPage = reviewRepository.findByHouseIdOrderByCreatedAtDesc(id, pageable);
		
        model.addAttribute("reviewPage", reviewPage);    
        return "review/show";
	}
	
	//レビューフォーム作成ページ
	@GetMapping("/review/post/{id}")
	public String post(@PathVariable(name = "id") Integer id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("reviewPostForm", new ReviewPostForm());
        return "review/post";		
	}
	
	//レビュフォーム投稿
	@PostMapping("/review/post/{id}")
	public String submit(@PathVariable(name = "id") Integer id,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
			@ModelAttribute @Validated ReviewPostForm reviewPostForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "review/post";
        }
        House house = houseRepository.getReferenceById(id);
        User user = userDetailsImpl.getUser(); 
        
        
        reviewService.create(reviewPostForm, house, user);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");    
        
        return "redirect:/houses/" + id;
    }  
	
//	レビューフォーム編集ページ
	@GetMapping("/review/edit/{id}")
	public String editReview(@PathVariable("id") Integer id, Model model) {
	    Review review = reviewRepository.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + id));
	    
	    model.addAttribute("review", review);
	    return "review/edit";
	}
	
//	レビュ-フォーム編集
	@PostMapping("/review/edit/{id}")
	public String submitEdit(@PathVariable(name = "id") Integer reviewId,
	                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
	                         @ModelAttribute @Validated ReviewPostForm reviewPostForm, 
	                         BindingResult bindingResult, 
	                         RedirectAttributes redirectAttributes) {        
	    if (bindingResult.hasErrors()) {
	        return "review/post";
	    }

	    // レビューIDからReviewエンティティを取得
	    Review review = reviewRepository.findById(reviewId)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));

	    // Reviewエンティティから関連するHouseエンティティを取得
	    House house = review.getHouse();

	    // 現在のログインユーザーを取得
	    User user = userDetailsImpl.getUser(); 

	    // レビュー更新処理（サービスメソッドを呼ぶ）
	    reviewService.update(review, reviewPostForm, house, user);

	    // 成功メッセージを追加
	    redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");    

	    // 編集後に関連するHouseの詳細ページへリダイレクト
	    return "redirect:/houses/" + house.getId();
	}
	
	@PostMapping("review/delete/{id}")
	public String deleteReview(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		
		Review review = reviewRepository.findById(id)
		        .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + id));
		
		reviewRepository.deleteById(id);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。"); 
		
		return "redirect:/houses/" + review.getHouse().getId();
	}
		
}
