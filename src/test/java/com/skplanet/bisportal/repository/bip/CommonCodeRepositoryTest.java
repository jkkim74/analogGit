package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.model.bip.CommonCode;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


/**
 * The CodeRepositoryTest class.
 *
 * @author sjune
 */
@Slf4j
@Transactional
public class CommonCodeRepositoryTest extends AbstractContextLoadingTest {

	@Autowired
	private CommonCodeRepository repository;

    @Test
    public void testGetCodeByName() throws Exception {
        // Given
        String name = "RM";

        // When
        CommonCode commonCode = repository.getCommonCodeByName(name);

        // Then
        assertThat("RM", is(commonCode.getName()));
    }
}
