package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.model.bip.Favorite;

import java.util.List;

/**
 * The FavoriteService interface
 * 
 * @author sjune
 */
public interface FavoriteService {
	/**
	 * username으로 즐겨찾기 목록을 조회한다.
	 * 
	 * @param username
	 * @return list of favorite
	 */
	List<Favorite> getFavoritesByUsername(String username);

	/**
	 * 즐겨찾기 추가 (multi)
	 * 
	 * @param favorites
	 */
	void addFavorites(List<Favorite> favorites) throws Exception;

	/**
	 * 즐겨찾기 제거
	 * 
	 * @param id
	 */
	void deleteFavorite(Long id);

	/**
	 * 즐겨찾기를 조회한다.
	 * 
	 * @param favorite
	 * @return favorite
	 */
	Favorite getFavorite(Favorite favorite);


    /**
     * 즐겨찾기 추가 (single)
     * @param favorite
     * @return
     */
    Favorite addFavorite(Favorite favorite) throws Exception;
}
