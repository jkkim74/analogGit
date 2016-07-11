package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.model.bip.Favorite;
import com.skplanet.bisportal.service.bip.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * The FavoriteController class.
 * 
 * @author sjune
 */
@Controller
@RequestMapping("/favorite")
public class FavoriteController {
	@Autowired
	private FavoriteService favoriteServiceImpl;

	/**
	 * username으로 즐겨찾기 목록을 조회한다.
	 * 
	 * @param username
	 * @return list of favorite
	 */
	@RequestMapping(value = "/list/{username}", method = RequestMethod.GET)
	public @ResponseBody List<Favorite> getFavoritesByUsername(@PathVariable String username) {
		return favoriteServiceImpl.getFavoritesByUsername(username);
	}

	/**
	 * 즐겨찾기 생성 (multi)
	 * 
	 * @param favorites
	 */
	@RequestMapping(value = "/adds", method = RequestMethod.POST)
	public @ResponseBody void addFavorites(@RequestBody List<Favorite> favorites) throws Exception {
		favoriteServiceImpl.addFavorites(favorites);
	}

	/**
	 * 즐겨찾기 생성 (single)
	 * 
	 * @param favorite
	 * @return Favorite
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody Favorite addFavorite(@RequestBody Favorite favorite) throws Exception {
		return favoriteServiceImpl.addFavorite(favorite);
	}

	/**
	 * 즐겨찾기 제거
	 * 
	 * @param id
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteFavorite(@PathVariable Long id) {
		favoriteServiceImpl.deleteFavorite(id);
	}

	/**
	 * 즐겨찾기 조회
	 * 
	 * @param favorite
	 * @return favorite
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Favorite getFavorite(Favorite favorite) {
		Favorite result = favoriteServiceImpl.getFavorite(favorite);
		if (result == null) {
			return Favorite.EMPTY;
		}
		return result;
	}
}
