package com.skplanet.bisportal.service.ocb;

import java.util.List;

import com.skplanet.bisportal.repository.sankey.SankeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.bisportal.model.sankey.SankeyRequest;
import com.skplanet.bisportal.model.sankey.SankeyResponse;

/**
 * Created by cookatrice on 2015. 8. 11..
 */
@Service
public class SankeyServiceImpl implements SankeyService {
	@Autowired
	private SankeyRepository sankeyRepository;

	@Override
	public List<SankeyResponse> getSankeyDataSet(SankeyRequest sankeyRequest){
		return sankeyRepository.getSankeyDataSet(sankeyRequest);
	}
}
