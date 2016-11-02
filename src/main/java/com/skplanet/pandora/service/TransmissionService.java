package com.skplanet.pandora.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.skplanet.pandora.repository.oracle.OracleRepository;
import com.skplanet.web.model.AutoMap;
import com.skplanet.web.model.UploadProgress;
import com.skplanet.web.service.PtsService;
import com.skplanet.web.util.Constant;
import com.skplanet.web.util.CsvCreatorTemplate;
import com.skplanet.web.util.Helper;

@Service
public class TransmissionService {

	@Autowired
	private OracleRepository oracleRepository;

	@Autowired
	private PtsService ptsService;

	@Value("${app.files.encoding.pts}")
	private String encoding;

	public void sendToPts(String ptsUsername, boolean ptsMasking, UploadProgress uploadProgress) {
		String csvFile = createCsvFile(ptsUsername, ptsMasking, uploadProgress);

		ptsService.send(csvFile, ptsUsername);
	}

	private String createCsvFile(String ptsUsername, final boolean ptsMasking, final UploadProgress uploadProgress) {

		CsvCreatorTemplate<AutoMap> csvCreator = new CsvCreatorTemplate<AutoMap>() {

			int offset = 0;
			int limit = 10000;

			@Override
			protected List<AutoMap> nextList() {
				List<AutoMap> list = oracleRepository.selectMembers(uploadProgress, offset, limit, ptsMasking);
				offset += limit;
				return list;
			}

			@Override
			protected void printHeader(CSVPrinter printer, List<AutoMap> list) throws IOException {

				if ("PAN0101".equals(uploadProgress.getPageId())) {
					printer.printRecord("회원ID", "OCB닷컴 로그인ID", "CI번호", "한글성명", "카드번호", "시럽스마트월렛회원ID", "11번가회원ID", "NO");
				} else if ("PAN0103".equals(uploadProgress.getPageId())) {
					printer.printRecord("회원ID", "카드번호", "CI번호", "한글성명", "생년월일", "성별", "OCB카드여부", "CI일치여부", "성명일치여부",
							"생년월일일치여부", "성별일치여부", "불일치항목포함여부", "NO");
				} else if ("PAN0105".equals(uploadProgress.getPageId())) {
					if ("TR_MBR_KOR_NM".equals(uploadProgress.getColumnName())) {
						printer.printRecord("접수일시", "승인일시", "대표 승인번호", "승인번호", "매출일시", "회원ID", "카드코드", "카드코드명", "카드번호",
								"정산제휴사코드", "정산제휴사명", "정산가맹점코드", "정산가맹점명", "발생제휴사코드", "발생제휴사명", "발생가맹점코드", "발생가맹점명",
								"포인트종류코드", "포인트종류명", "전표코드", "전표명", "매출금액", "포인트", "제휴사연회비", "수수료", "지불수단코드", "지불수단명",
								"기관코드", "기관명", "유종코드", "유종명", "쿠폰코드", "쿠폰명", "회원한글명");
					} else {
						printer.printRecord("접수일시", "승인일시", "대표 승인번호", "승인번호", "매출일시", "회원ID", "카드코드", "카드코드명", "카드번호",
								"정산제휴사코드", "정산제휴사명", "정산가맹점코드", "정산가맹점명", "발생제휴사코드", "발생제휴사명", "발생가맹점코드", "발생가맹점명",
								"포인트종류코드", "포인트종류명", "전표코드", "전표명", "매출금액", "포인트", "제휴사연회비", "수수료", "지불수단코드", "지불수단명",
								"기관코드", "기관명", "유종코드", "유종명", "쿠폰코드", "쿠폰명");
					}
				} else if ("PAN0106".equals(uploadProgress.getPageId())) {
					printer.printRecord("회원ID", "OCB닷컴 United ID", "성별", "연령", "자택 행정동 대그룹명", "자택 행정동 중그룹명",
							"직장 행정동 대그룹명", "직장 행정동 중그룹명", "결혼 여부", "마케팅 동의 여부", "이메일 수신 동의 여부", "광고성 SMS 수신 동의 여부",
							"정보성 SMS 수신 동의 여부", "앱 푸시 수신 동의 여부", "혜택/모바일전단 푸시 동의 여부", "포인트사용적립 푸시 동의 여부",
							"친구와 함께쓰기 푸시 동의 여부", "코인알림 푸시 동의 여부", "위치활용 동의 여부", "BLE 동의 여부", "Geo-Fencing 동의 여부",
							"OCB닷컴 가입 여부", "OCB앱 가입 여부", "OCB플러스 가입 여부", "629세그먼트코드", "트래픽세그먼트코드", "OCB닷컴 최종 로그인 일자",
							"OCB앱 최종 사용 일자", "앱출석 최종 사용 일자", "룰렛 최종 사용 일자", "게임 최종 사용 일자", "OCB플러스 최종 유실적 일자",
							"라커0 최종 유실적 일자", "미리줌 최종 유실적 일자", "더줌 최종 유실적 일자", "상품전단 최종 유실적 일자", "모바일카드 보유 여부",
							"SKP카드 보유 여부", "신용카드 보유 여부", "체크카드 보유 여부", "통신카드 보유 여부", "엔크린보너스카드 보유 여부", "화물복지카드 보유 여부",
							"주유전용카드 보유 여부", "주유 최종 유실적 일자", "통신 최종 유실적 일자", "금융 최종 유실적 일자", "기타 최종 유실적 일자",
							"온라인 최종 유실적 일자", "온라인쿠폰 최종 유실적 일자", "가용포인트", "NO");
				} else {
					Set<?> keyList = list.get(0).keySet();
					printer.printRecord(keyList);
				}
			}

			@Override
			protected void printRecord(CSVPrinter printer, AutoMap map) throws IOException {

				if ("PAN0101".equals(uploadProgress.getPageId())) {

					List<String> dataList = new ArrayList<String>();

					if (map.get("mbrId") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mbrId").toString());
					}

					if (map.get("ocbcomLgnId") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("ocbcomLgnId").toString());
					}

					if (map.get("ciNo") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("ciNo").toString());
					}

					if (map.get("mbrKorNm") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("mbrKorNm").toString());
					}

					if (map.get("cardNo") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("cardNo").toString());
					}

					if (map.get("sywMbrId") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("sywMbrId").toString());
					}

					if (map.get("evsMbrId") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("evsMbrId").toString());
					}

					if (map.get("rnum") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("rnum").toString());
					}

					printer.printRecord(dataList);

				} else if ("PAN0103".equals(uploadProgress.getPageId())) {

					List<String> dataList = new ArrayList<String>();

					if (map.get("mbrId") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mbrId").toString());
					}

					if (map.get("cardNo") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("cardNo").toString());
					}

					if (map.get("ciNo") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("ciNo").toString());
					}

					if (map.get("mbrKorNm") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("mbrKorNm").toString());
					}

					if (map.get("leglBthdt") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("leglBthdt").toString());
					}

					if (map.get("leglGndrFgCd") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("leglGndrFgCd").toString());
					}

					if (map.get("cardNoYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("cardNoYn").toString());
					}

					if (map.get("ciNoYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("ciNoYn").toString());
					}

					if (map.get("mbrKorNmYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("mbrKorNmYn").toString());
					}

					if (map.get("leglBthdtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("leglBthdtYn").toString());
					}

					if (map.get("leglGndrFgYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("leglGndrFgYn").toString());
					}

					if (map.get("allYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("allYn").toString());
					}

					if (map.get("rnum") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("rnum").toString());
					}

					printer.printRecord(dataList);

				} else if ("PAN0106".equals(uploadProgress.getPageId())) {
					List<String> dataList = new ArrayList<String>();

					if (map.get("mbrId") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mbrId").toString());
					}
					if (map.get("unitedId") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("unitedId").toString());
					}
					if (map.get("gndr") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("gndr").toString());
					}
					if (map.get("age") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("age").toString());
					}
					if (map.get("homeHjdLgrp") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("homeHjdLgrp").toString());
					}
					if (map.get("homeHjdMgrp") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("homeHjdMgrp").toString());
					}
					if (map.get("jobpHjdLgrp") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("jobpHjdLgrp").toString());
					}
					if (map.get("jobpHjdMgrp") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("jobpHjdMgrp").toString());
					}
					if (map.get("mrrgYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mrrgYn").toString());
					}
					if (map.get("ocbMktngAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("ocbMktngAgrmtYn").toString());
					}
					if (map.get("emailRcvAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("emailRcvAgrmtYn").toString());
					}
					if (map.get("advtSmsRcvAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("advtSmsRcvAgrmtYn").toString());
					}
					if (map.get("ifrmtSmsRcvAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("ifrmtSmsRcvAgrmtYn").toString());
					}
					if (map.get("pushRcvAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("pushRcvAgrmtYn").toString());
					}
					if (map.get("bnftMlfPushAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("bnftMlfPushAgrmtYn").toString());
					}
					if (map.get("pntUseRsvngPushAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("pntUseRsvngPushAgrmtYn").toString());
					}
					if (map.get("tusePushAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("tusePushAgrmtYn").toString());
					}
					if (map.get("coinNotiPushAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("coinNotiPushAgrmtYn").toString());
					}
					if (map.get("locUtlzAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("locUtlzAgrmtYn").toString());
					}
					if (map.get("mlfShpAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mlfShpAgrmtYn").toString());
					}
					if (map.get("mlfTrdareaAgrmtYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mlfTrdareaAgrmtYn").toString());
					}
					if (map.get("ocbcomJoinYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("ocbcomJoinYn").toString());
					}
					if (map.get("ocbAppJoinYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("ocbAppJoinYn").toString());
					}
					if (map.get("ocbPlusJoinYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("ocbPlusJoinYn").toString());
					}
					if (map.get("seg629Cd") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("seg629Cd").toString());
					}
					if (map.get("trfcSegCd") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("trfcSegCd").toString());
					}
					if (map.get("ocbcomMth07FnlLgnDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("ocbcomMth07FnlLgnDt").toString());
					}
					if (map.get("ocbAppMth07FnlUseDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("ocbAppMth07FnlUseDt").toString());
					}
					if (map.get("appAtdcMth07FnlUseDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("appAtdcMth07FnlUseDt").toString());
					}
					if (map.get("rletMth07FnlUseDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("rletMth07FnlUseDt").toString());
					}
					if (map.get("gameMth07FnlUseDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("gameMth07FnlUseDt").toString());
					}
					if (map.get("ocbPlusMth07FnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("ocbPlusMth07FnlYachvDt").toString());
					}
					if (map.get("lckrMth07FnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("lckrMth07FnlYachvDt").toString());
					}
					if (map.get("mzmMth07FnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mzmMth07FnlYachvDt").toString());
					}
					if (map.get("azmMth07FnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("azmMth07FnlYachvDt").toString());
					}
					if (map.get("prdLflMth07FnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("prdLflMth07FnlYachvDt").toString());
					}
					if (map.get("mblCardPossYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mblCardPossYn").toString());
					}
					if (map.get("skpCardPossYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("skpCardPossYn").toString());
					}
					if (map.get("crdtCardPossYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("crdtCardPossYn").toString());
					}
					if (map.get("chkcrdPossYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("chkcrdPossYn").toString());
					}
					if (map.get("cmncnCardPossYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("cmncnCardPossYn").toString());
					}
					if (map.get("encleanBnsCardPossYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("encleanBnsCardPossYn").toString());
					}
					if (map.get("freiWlfrCardPossYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("freiWlfrCardPossYn").toString());
					}
					if (map.get("rflExcsCardPossYn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("rflExcsCardPossYn").toString());
					}
					if (map.get("rflMth07FnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("rflMth07FnlYachvDt").toString());
					}
					if (map.get("cmncnMth07FnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("cmncnMth07FnlYachvDt").toString());
					}
					if (map.get("fncMth07FnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("fncMth07FnlYachvDt").toString());
					}
					if (map.get("etcMth07FnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("etcMth07FnlYachvDt").toString());
					}
					if (map.get("onlnMth07FnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("onlnMth07FnlYachvDt").toString());
					}
					if (map.get("onlnMth07CpnFnlYachvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("onlnMth07CpnFnlYachvDt").toString());
					}
					if (map.get("avlPnt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("avlPnt").toString());
					}
					if (map.get("rnum") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("rnum").toString());
					}

					printer.printRecord(dataList);
				} else {
					printer.printRecord(map.values());
				}
			}

		};

		Path filePath = Paths.get(Constant.APP_FILE_DIR, Helper.uniqueCsvFilename("P140802BKhub_" + ptsUsername));
		csvCreator.create(filePath, Charset.forName(encoding));

		return filePath.toFile().getAbsolutePath();
	}

}
