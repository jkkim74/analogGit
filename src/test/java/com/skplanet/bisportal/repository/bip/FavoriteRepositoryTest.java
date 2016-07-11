package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.bip.CommonCode;
import com.skplanet.bisportal.model.bip.Favorite;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.skplanet.bisportal.testsupport.builder.FavoriteBuilder.aFavorite;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * The FavoriteRepositoryTest class
 * 
 * @author sjune
 */
@Slf4j
@Transactional
public class FavoriteRepositoryTest extends AbstractContextLoadingTest {

	@Autowired
	FavoriteRepository repository;

	@Autowired
	CommonCodeRepository commonCodeRepository;

	private Favorite givenFavorite() throws Exception {
		CommonCode commonCode = commonCodeRepository.getCommonCodeByName("RM");

		return aFavorite().serviceCode("ocb").categoryCode("02").menuCode("0201").commonCodeId(commonCode.getId())
				.commonCodeName(commonCode.getName()).menuId("28").serviceName("Ok Cashbag").categoryName("방문").menuName("방문개요")
				.username("PP00000").orderIdx(31).lastUpdate(Utils.getCreateDate()).build();
	}

	@Test
	public void testGetFavoritesByUsername() throws Exception {
		// Given
		Favorite favorite = givenFavorite();
		repository.createFavorite(favorite);

		// When & Then
		List<Favorite> actual = repository.getFavoritesByUsername("PP00000");
		assertNotNull(actual);
	}

	@Test
	public void testGetFavorite() throws Exception {
		// Given
		Favorite favorite = givenFavorite();
		repository.createFavorite(favorite);

		// When
		Favorite selectFavorite = aFavorite().serviceCode("ocb").categoryCode("02").menuCode("0201")
				.commonCodeName("RM").username("PP00000").menuId("28").build();

		// Then
		assertNotNull(repository.getFavorite(selectFavorite));

	}

	@Test
	public void testCreateFavorite() throws Exception {
		// Given
		Favorite favorite = givenFavorite();

		// When
		repository.createFavorite(favorite);

		// Then
		assertNotNull(favorite.getId());
	}

	@Test
	public void testDeleteFavorite() throws Exception {
		// Given
		Favorite favorite = givenFavorite();
		repository.createFavorite(favorite);

		// When & Then
		assertThat(repository.deleteFavorite(favorite.getId()), is(1));
	}
}
