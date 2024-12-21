package com.example.samuraitravel.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;

import jakarta.transaction.Transactional;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Page<House> getFavoriteHousesForUser(User user, Pageable pageable) {
        Page<Favorite> favorites = favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return favorites.map(Favorite::getHouse); 
    }
    
    @Transactional
    public void removeFavorite(Integer houseId, Integer userId) {
        favoriteRepository.deleteByHouseIdAndUserId(houseId, userId);
    }
}