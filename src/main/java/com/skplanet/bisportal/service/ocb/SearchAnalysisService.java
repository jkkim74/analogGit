package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsSrchClickAlliSta;
import com.skplanet.bisportal.model.ocb.ObsSrchClickBnftSta;
import com.skplanet.bisportal.model.ocb.ObsSrchClickStoreSta;

import java.util.List;

/**
 * Created by cookatrice on 2014. 5. 19..
 */
public interface SearchAnalysisService {
    List<ObsSrchClickStoreSta> getSearchResultClickStoreForPivot(JqGridRequest jqGridRequest);

    List<ObsSrchClickStoreSta> getSearchResultClickStoreForGrid(JqGridRequest jqGridRequest);

    List<ObsSrchClickAlliSta> getSearchResultClickAlliForPivot(JqGridRequest jqGridRequest);

    List<ObsSrchClickAlliSta> getSearchResultClickAlliForGrid(JqGridRequest jqGridRequest);

	List<ObsSrchClickBnftSta> getSearchResultClickBnftForGrid(JqGridRequest jqGridRequest);
}
