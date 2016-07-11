package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.model.bip.MenuSearchOption;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * The MenuSearchOptionRepository class.
 *
 * @author sjune
 */
@Slf4j
@Repository
public class MenuSearchOptionRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * menuId로 menuSearchOption을 조회한다.
	 * 
	 * @param menuId
	 * @return
	 */
	public MenuSearchOption getMenuSearchOptionByMenuId(Long menuId) {
		return sqlSession.selectOne("getMenuSearchOptionByMenuId", menuId);
	}

	/**
	 * menuSearchOption을 갱신한다. (merge)
	 * 
	 * @param menuSearchOption
	 */
	public void updateMenuSearchOption(MenuSearchOption menuSearchOption) {
		sqlSession.update("updateMenuSearchOption", menuSearchOption);
	}

	/**
	 * MenuSearchOptionBack 테이블에 삽입.
	 *
	 * @param menuSearchOption
	 */
	public void createMenuSearchOptionBack(MenuSearchOption menuSearchOption) {
		sqlSession.update("createMenuSearchOptionBack", menuSearchOption);
	}

	/**
	 * menuSearchOption을 제거한다.
	 * 
	 * @param menuId
	 */
	public void deleteMenuSearchOption(Long menuId) {
		sqlSession.delete("deleteMenuSearchOption", menuId);
	}
}
