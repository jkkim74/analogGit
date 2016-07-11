package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.model.bip.BpmMthStcPrst;
import com.skplanet.bisportal.model.bip.BpmWkStcPrst;
import com.skplanet.bisportal.service.bip.BpmResultSumService;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <pre>
 *     The BpmResultSumController class.
 *     경영 실적 상세 조회 controller
 * </pre>
 *
 * @author sjune
 */
@Controller
@RequestMapping("/bpmResultSum")
public class BpmResultSumController {
	@Autowired
	private BpmResultSumService bpmResultSumServiceImpl;

	/**
	 * 경영실적 일별 조회
	 *
	 * @param whereCondition
	 * @return list of BpmDlyPrst
	 */
	@RequestMapping(value = "/daily", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<BpmDlyPrst> getBpmDailyResultSums(WhereCondition whereCondition) {
		JqGridResponse<BpmDlyPrst> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(bpmResultSumServiceImpl.getBpmDailyResultSums(whereCondition));
		return jqGridResponse;
	}

	/**
	 * 경영실적 주별 조회
	 * 
	 * @param whereCondition
	 * @return list of BpmWkStcPrst
	 */
	@RequestMapping(value = "/weekly", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<BpmWkStcPrst> getBpmWeeklyResultSums(WhereCondition whereCondition) {
		JqGridResponse<BpmWkStcPrst> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(bpmResultSumServiceImpl.getBpmWeeklyResultSums(whereCondition));
		return jqGridResponse;
	}

	/**
	 * 경영실적 월별 조회
	 *
	 * @param whereCondition
	 * @return list of BpmMthStcPrst
	 */
	@RequestMapping(value = "/monthly", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<BpmMthStcPrst> getBpmMonthlyResultSums(WhereCondition whereCondition) {
		JqGridResponse<BpmMthStcPrst> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(bpmResultSumServiceImpl.getBpmMonthlyResultSums(whereCondition));
		return jqGridResponse;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/downloadExcelForbpmResultSum/day", method = RequestMethod.POST)
	public ModelAndView downloadExcelForbpmResultSumDay(WhereCondition whereCondition) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("bpmResultSumExcelView");
		List<BpmDlyPrst> bpmDlyPrstList = bpmResultSumServiceImpl.getBpmDailyResultSums(whereCondition);
		if (StringUtils.equals(whereCondition.getPivotFlag(), Constants.NO)) {
			ComparatorChain chain = new ComparatorChain();
			chain.addComparator(comparatordlyStrdDt);
			chain.addComparator(comparatorIdxClCdGrpNm);
			chain.addComparator(comparatorIdxClCdVal);
			chain.addComparator(comparatorIdxCttCdVal);
			Collections.sort(bpmDlyPrstList, chain);
		} else if (StringUtils.equals(whereCondition.getPivotFlag(), Constants.YES)) {
			ComparatorChain chain = new ComparatorChain();
			chain.addComparator(comparatorIdxClCdGrpNm);
			chain.addComparator(comparatorIdxClCdVal);
			chain.addComparator(comparatorIdxCttCdVal);
			chain.addComparator(comparatordlyStrdDt);
			Collections.sort(bpmDlyPrstList, chain);
		}
		modelAndView.addObject("bpmResultSumData", bpmDlyPrstList);
		return modelAndView;
	}

	public static final Comparator<BpmDlyPrst> comparatordlyStrdDt = new Comparator<BpmDlyPrst>() {
		@Override
		public int compare(BpmDlyPrst o1, BpmDlyPrst o2) {
			return o1.getDlyStrdDt().compareTo(o2.getDlyStrdDt());
		}
	};

	public static final Comparator<BpmDlyPrst> comparatorIdxClCdGrpNm = new Comparator<BpmDlyPrst>() {
		@Override
		public int compare(BpmDlyPrst o1, BpmDlyPrst o2) {
			return o1.getIdxClCdGrpNm().compareTo(o2.getIdxClCdGrpNm());
		}
	};

	public static final Comparator<BpmDlyPrst> comparatorIdxClCdVal = new Comparator<BpmDlyPrst>() {
		@Override
		public int compare(BpmDlyPrst o1, BpmDlyPrst o2) {
			return o1.getIdxClCdVal().compareTo(o2.getIdxClCdVal());
		}
	};

	public static final Comparator<BpmDlyPrst> comparatorIdxCttCdVal = new Comparator<BpmDlyPrst>() {
		@Override
		public int compare(BpmDlyPrst o1, BpmDlyPrst o2) {
			return o1.getIdxCttCdVal().compareToIgnoreCase(o2.getIdxCttCdVal());
		}
	};

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/downloadExcelForbpmResultSum/week", method = RequestMethod.POST)
	public ModelAndView downloadExcelForbpmResultSumWeek(WhereCondition whereCondition) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("bpmResultSumExcelView");
		List<BpmWkStcPrst> bpmWkStcPrstList = bpmResultSumServiceImpl.getBpmWeeklyResultSums(whereCondition);
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(comWkStrdVal);
		chain.addComparator(comWkIdxClCdGrpNm);
		chain.addComparator(comWkIdxClCdVal);
		chain.addComparator(comWkIdxCttCdVal);
		Collections.sort(bpmWkStcPrstList, chain);
		modelAndView.addObject("bpmResultSumData", bpmWkStcPrstList);
		return modelAndView;
	}

	public static final Comparator<BpmWkStcPrst> comWkStrdVal = new Comparator<BpmWkStcPrst>() {
		@Override
		public int compare(BpmWkStcPrst o1, BpmWkStcPrst o2) {
			return o1.getWkStrdVal().compareTo(o2.getWkStrdVal());
		}
	};

	public static final Comparator<BpmWkStcPrst> comWkIdxClCdGrpNm = new Comparator<BpmWkStcPrst>() {
		@Override
		public int compare(BpmWkStcPrst o1, BpmWkStcPrst o2) {
			return o1.getIdxClCdGrpNm().compareTo(o2.getIdxClCdGrpNm());
		}
	};

	public static final Comparator<BpmWkStcPrst> comWkIdxClCdVal = new Comparator<BpmWkStcPrst>() {
		@Override
		public int compare(BpmWkStcPrst o1, BpmWkStcPrst o2) {
			return o1.getIdxClCdVal().compareTo(o2.getIdxClCdVal());
		}
	};

	public static final Comparator<BpmWkStcPrst> comWkIdxCttCdVal = new Comparator<BpmWkStcPrst>() {
		@Override
		public int compare(BpmWkStcPrst o1, BpmWkStcPrst o2) {
			return o1.getIdxCttCdVal().compareToIgnoreCase(o2.getIdxCttCdVal());
		}
	};

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/downloadExcelForbpmResultSum/month", method = RequestMethod.POST)
	public ModelAndView downloadExcelForbpmResultSumMonth(WhereCondition whereCondition) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("bpmResultSumExcelView");
		List<BpmMthStcPrst> bpmMthStcPrstList = bpmResultSumServiceImpl.getBpmMonthlyResultSums(whereCondition);
		ComparatorChain chain = new ComparatorChain();
		chain.addComparator(comMthStcStrdYm);
		chain.addComparator(comMthIdxClCdGrpNm);
		chain.addComparator(comMthIdxClCdVal);
		chain.addComparator(comMthIdxCttCdVal);
		Collections.sort(bpmMthStcPrstList, chain);
		modelAndView.addObject("bpmResultSumData", bpmMthStcPrstList);
		return modelAndView;
	}

	public static final Comparator<BpmMthStcPrst> comMthStcStrdYm = new Comparator<BpmMthStcPrst>() {
		@Override
		public int compare(BpmMthStcPrst o1, BpmMthStcPrst o2) {
			return o1.getMthStcStrdYm().compareTo(o2.getMthStcStrdYm());
		}
	};

	public static final Comparator<BpmMthStcPrst> comMthIdxClCdGrpNm = new Comparator<BpmMthStcPrst>() {
		@Override
		public int compare(BpmMthStcPrst o1, BpmMthStcPrst o2) {
			return o1.getIdxClCdGrpNm().compareTo(o2.getIdxClCdGrpNm());
		}
	};

	public static final Comparator<BpmMthStcPrst> comMthIdxClCdVal = new Comparator<BpmMthStcPrst>() {
		@Override
		public int compare(BpmMthStcPrst o1, BpmMthStcPrst o2) {
			return o1.getIdxClCdVal().compareTo(o2.getIdxClCdVal());
		}
	};

	public static final Comparator<BpmMthStcPrst> comMthIdxCttCdVal = new Comparator<BpmMthStcPrst>() {
		@Override
		public int compare(BpmMthStcPrst o1, BpmMthStcPrst o2) {
			return o1.getIdxCttCdVal().compareToIgnoreCase(o2.getIdxCttCdVal());
		}
	};
}
