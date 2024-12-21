package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;


@Controller
public class FavoriteController {
	
	private final HouseRepository houseRepository;   
    private final FavoriteRepository favoriteRepository;
    private final FavoriteService favoriteService;
    
    public FavoriteController(HouseRepository houseRepository, FavoriteRepository favoriteRepository, FavoriteService favoriteService) {
        this.houseRepository = houseRepository;     
        this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoriteService;
    }  
	
	@GetMapping("/house/{id}/favorite")
	public String favorite(@PathVariable(name = "id") Integer id, 
    				       @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    					   Model model) {
		House house = houseRepository.getReferenceById(id);
		User user = null;
        if (userDetailsImpl != null) {
            user = userDetailsImpl.getUser();
        }
		
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setHouse(house);
        favoriteRepository.save(favorite);
        
		return "redirect:/houses/" + id;
	}
	
	@GetMapping("/house/{id}/unfavorite")
	public String unFavorite(@PathVariable(name = "id") Integer id, 
    				       @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    					   Model model) {
		House house = houseRepository.getReferenceById(id);
		User user = null;
        if (userDetailsImpl != null) {
            user = userDetailsImpl.getUser();
        }
		
        favoriteService.removeFavorite(house.getId(), user.getId());        
		return "redirect:/houses/" + id;
	}
	
	@GetMapping("favorites")
	public String listFavorites(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			                    @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, 
			                    Model model) { 
		
		User user = userDetailsImpl.getUser();
		
		Page<House> housePage = favoriteService.getFavoriteHousesForUser(user, pageable);
        
		model.addAttribute("housePage", housePage);
		
		return "favorite/index";
	}

	
	
	
	
	
	

}
