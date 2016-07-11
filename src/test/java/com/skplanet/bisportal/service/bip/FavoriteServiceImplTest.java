package com.skplanet.bisportal.service.bip;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.bip.Favorite;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.skplanet.bisportal.testsupport.builder.FavoriteBuilder.aFavorite;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * The FavoriteServiceTest class
 * 
 * @author sjune
 */
@Slf4j
@Transactional
public class FavoriteServiceImplTest extends AbstractContextLoadingTest {

	@Autowired
	private FavoriteService favoriteServiceImpl;

	@Test
	public void testAddFavorites() throws Exception {
		// Given
		List<Favorite> before = favoriteServiceImpl.getFavoritesByUsername("PP00000");

		Favorite f1 = aFavorite().commonCodeId(4l).commonCodeName("BM").menuCode("test1").username("PP00000")
				.menuId("75").lastUpdate(Utils.getCreateDate()).build();
		Favorite f2 = aFavorite().commonCodeId(4l).commonCodeName("BM").menuCode("test2").username("PP00000")
				.menuId("76").lastUpdate(Utils.getCreateDate()).build();
		Favorite f3 = aFavorite().commonCodeId(4l).commonCodeName("BM").menuCode("test3").username("PP00000")
				.menuId("82").lastUpdate(Utils.getCreateDate()).build();
		ArrayList<Favorite> favorites = Lists.newArrayList(f1, f2, f3);

		// When
		favoriteServiceImpl.addFavorites(favorites);

		// Then
		List<Favorite> actual = favoriteServiceImpl.getFavoritesByUsername("PP00000");
		assertThat(actual.size(), is(before.size() + 3));
	}

	@Test
	public void testAddFavoritesWithDuplicateFavorite() throws Exception {
		// Given
		List<Favorite> before = favoriteServiceImpl.getFavoritesByUsername("PP00000");

		Favorite f1 = aFavorite().commonCodeId(7l).commonCodeName("RM").serviceCode("test1").categoryCode("01")
				.menuCode("0101").username("PP00000").menuId("44").lastUpdate(Utils.getCreateDate()).build();
		Favorite f2 = aFavorite().commonCodeId(7l).commonCodeName("RM").serviceCode("test1").categoryCode("01")
				.menuCode("0101").username("PP00000").menuId("63").lastUpdate(Utils.getCreateDate()).build();
		Favorite f3 = aFavorite().commonCodeId(7l).commonCodeName("RM").serviceCode("test1").categoryCode("01")
				.menuCode("0101").username("PP00000").menuId("736").lastUpdate(Utils.getCreateDate()).build();
		ArrayList<Favorite> favorites = Lists.newArrayList(f1, f2, f3);

		// When
		favoriteServiceImpl.addFavorites(favorites);

		// Then
		List<Favorite> actual = favoriteServiceImpl.getFavoritesByUsername("PP00000");
		assertThat(actual.size(), is(before.size() + 1));
	}

}
