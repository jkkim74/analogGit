package com.skplanet.bisportal.service.bip;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.bip.CommonCode;
import com.skplanet.bisportal.model.bip.Favorite;
import com.skplanet.bisportal.repository.bip.CommonCodeRepository;
import com.skplanet.bisportal.repository.bip.FavoriteRepository;

/**
 * The FavoriteServiceImpl class.
 * 
 * @author sjune
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {
	@Autowired
	private FavoriteRepository favoriteRepository;

	@Autowired
	private CommonCodeRepository commonCodeRepository;

	@Override
	public List<Favorite> getFavoritesByUsername(String username) {
		return favoriteRepository.getFavoritesByUsername(username);
	}

	@Override
	@Transactional
	public void addFavorites(List<Favorite> favorites) throws Exception {
		for (Favorite favorite : favorites) {
			// count 조회 후, 등록되지 않은 상태라면 추가한다.
			Favorite oldFavorite = favoriteRepository.getFavorite(favorite);
			if (oldFavorite == null) {
				addFavorite(favorite);
			}
		}
	}

	@Override
	@Transactional
	public void deleteFavorite(Long id) {
		favoriteRepository.deleteFavorite(id);
	}

	@Override
	public Favorite getFavorite(Favorite favorite) {
		return favoriteRepository.getFavorite(favorite);
	}

	@Override
	@Transactional
	public Favorite addFavorite(Favorite favorite) throws Exception {
		CommonCode code = commonCodeRepository.getCommonCodeByName(favorite.getCommonCodeName());
		favorite.setCommonCodeId(code.getId());
		favorite.setLastUpdate(Utils.getCreateDate());
		return favoriteRepository.createFavorite(favorite);
	}
}
