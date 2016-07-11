package com.skplanet.bisportal.view;

import static com.skplanet.bisportal.common.utils.Objects.uncheckedCast;

import java.util.Iterator;
import java.util.List;

import net.sf.reportengine.config.HorizAlign;
import net.sf.reportengine.in.AbstractReportInput;
import net.sf.reportengine.in.ColumnMetadata;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import org.apache.commons.lang.StringUtils;

/**
 * Created by lko on 2014-12-10.
 */
public class CustomInput extends AbstractReportInput {
	private List<Object> nextRow;

	private boolean hasMoreRows = false;

	@SuppressWarnings("rawtypes")
	private Iterator itF;

	public CustomInput(Object model) {
		List<BpmDlyPrst> bpmDlyPrstList = uncheckedCast(model);
		itF = bpmDlyPrstList.iterator();
	}

	public void open() {
		super.open();
		List<ColumnMetadata> columnMetadata = Lists.newArrayList();
		columnMetadata.add(new ColumnMetadata("SVC_ID", "SVC_ID", HorizAlign.LEFT));
		columnMetadata.add(new ColumnMetadata("IDX_CL_GRP_CD", "IDX_CL_GRP_CD", HorizAlign.LEFT));
		columnMetadata.add(new ColumnMetadata("IDX_CL_CD", "IDX_CL_CD", HorizAlign.LEFT));
		columnMetadata.add(new ColumnMetadata("IDX_CTT_CD", "IDX_CTT_CD", HorizAlign.LEFT));
		columnMetadata.add(new ColumnMetadata("IDX_CL_CD_GRP_NM", "IDX_CL_CD_GRP_NM", HorizAlign.LEFT));
		columnMetadata.add(new ColumnMetadata("IDX_CL_CD_VAL", "IDX_CL_CD_VAL", HorizAlign.RIGHT));
		columnMetadata.add(new ColumnMetadata("IDX_CTT_CD_VAL", "IDX_CTT_CD_VAL", HorizAlign.RIGHT));
		columnMetadata.add(new ColumnMetadata("DLY_STRD_DT", "DLY_STRD_DT", HorizAlign.CENTER));
		columnMetadata.add(new ColumnMetadata("DLY_RSLT_VAL", "DLY_RSLT_VAL", HorizAlign.RIGHT));
		setColumnMetadata(columnMetadata);
		hasMoreRows = true;
	}

	@Override
	public List<Object> nextRow() {
		if (hasMoreRows()) {
			nextRow = Lists.newArrayList();
			BpmDlyPrst bpmDlyPrsts = (BpmDlyPrst) itF.next();
			nextRow.add(bpmDlyPrsts.getSvcId());
			nextRow.add(bpmDlyPrsts.getIdxClGrpCd());
			nextRow.add(bpmDlyPrsts.getIdxClCd());
			nextRow.add(bpmDlyPrsts.getIdxCttCd());
			nextRow.add(bpmDlyPrsts.getIdxClCdGrpNm().replaceAll("^[1-2]+", StringUtils.EMPTY));
			nextRow.add(bpmDlyPrsts.getIdxClCdVal().replaceAll("^[1-2]+", StringUtils.EMPTY));
			nextRow.add(bpmDlyPrsts.getIdxCttCdVal().replaceAll("^[1-2]+", StringUtils.EMPTY));
			nextRow.add(bpmDlyPrsts.getDlyStrdDt());
			nextRow.add(bpmDlyPrsts.getDlyRsltVal());
		} else {
			nextRow = null;
		}
		hasMoreRows = itF.hasNext();
		return nextRow;
	}

	@Override
	public boolean hasMoreRows() {
		return hasMoreRows;
	}
}
