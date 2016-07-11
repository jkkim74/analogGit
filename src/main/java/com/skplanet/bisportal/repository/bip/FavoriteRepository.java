package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.model.bip.Favorite;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * The FavoriteRepository repository class.
 * 
 * @author sjune
 */

@Repository
public class FavoriteRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * username으로 즐겨찾기 목록을 조회한다.
	 * 
	 * @param username
	 * @return list of favorite
	 */
	public List<Favorite> getFavoritesByUsername(String username) {
		return sqlSession.selectList("getFavoritesByUsername", username);
	}

	/**
	 * username으로 즐겨찾기 목록을 조회한다.
	 *
	 * @param favorite
	 * @return list of favorite
	 */
	public int getFavoriteCountByCommonCodeIdMenuId(Favorite favorite) {
		return sqlSession.selectOne("getFavoriteCountByCommonCodeIdMenuId", favorite);
	}

	/**
	 * 즐겨찾기를 조회한다.
	 * 
	 * @param favorite
	 * @return favorite
	 */
	public Favorite getFavorite(Favorite favorite) {
		return sqlSession.selectOne("getFavorite", favorite);
	}

	/**
	 * 즐겨찾기 생성
	 * 
	 * @param favorite
	 * @return favorite
	 */
	public Favorite createFavorite(Favorite favorite) {
		sqlSession.insert("createFavorite", favorite);
		return favorite;
	}

	/**
	 * 즐겨찾기 제거
	 * 
	 * @param id
	 * @return result
	 */
	public int deleteFavorite(Long id) {
		return sqlSession.delete("deleteFavorite", id);
	}

	/**
	 * 즐겨찾기 제거
	 *
	 * @param favorite
	 * @return result
	 */
	public int deleteFavoriteByCommonCodeIdMenuId(Favorite favorite) {
		return sqlSession.delete("deleteFavoriteByCommonCodeIdMenuId", favorite);
	}
}
