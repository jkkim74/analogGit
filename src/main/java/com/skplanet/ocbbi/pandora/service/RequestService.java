package com.skplanet.ocbbi.pandora.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.ocb.model.AutoMap;
import com.skplanet.ocb.model.UploadProgress;
import com.skplanet.ocb.service.PtsService;
import com.skplanet.ocb.util.Constant;
import com.skplanet.ocb.util.CsvCreatorTemplate;
import com.skplanet.ocb.util.Helper;
import com.skplanet.ocbbi.pandora.repository.oracle.OracleRepository;

@Service
public class RequestService {

	@Autowired
	private PtsService ptsService;

	@Autowired
	private OracleRepository oracleRepository;

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

				if ((uploadProgress.getPageId()).equals("pan0101")) {
					List<String> keyList = new ArrayList<String>();
					keyList.add("회원ID");
					keyList.add("OCB닷컴 로그인ID");
					keyList.add("CI번호");
					keyList.add("한글성명");
					keyList.add("카드번호");
					keyList.add("시럽스마트월렛회원ID");
					keyList.add("11번가회원ID");
					keyList.add("NO");
					printer.printRecord(keyList);
				} else if ((uploadProgress.getPageId()).equals("pan0103")) {
					List<String> keyList = new ArrayList<String>();
					keyList.add("회원ID");
					keyList.add("카드번호");
					keyList.add("CI번호");
					keyList.add("한글성명");
					keyList.add("생년월일");
					keyList.add("성별");
					keyList.add("OCB카드여부");
					keyList.add("CI일치여부");
					keyList.add("성명일치여부");
					keyList.add("생년월일일치여부");
					keyList.add("성별일치여부");
					keyList.add("불일치항목포함여부");
					keyList.add("NO");
					printer.printRecord(keyList);
				} else if ((uploadProgress.getPageId()).equals("pan0105")) {
					List<String> keyList = new ArrayList<String>();
					keyList.add("접수일시");
					keyList.add("접수번호");
					keyList.add("승인일시");
					keyList.add("대표 승인번호");
					keyList.add("승인번호");
					keyList.add("매출일시");
					keyList.add("회원ID");
					keyList.add("카드코드");
					keyList.add("카드번호");
					keyList.add("발생제휴사");
					keyList.add("발생가맹점");
					keyList.add("정산가맹점");
					keyList.add("포인트종류");
					keyList.add("전표");
					keyList.add("매출금액");
					keyList.add("적립포인트");
					keyList.add("제휴사연회비");
					keyList.add("수수료");
					keyList.add("지불수단");
					keyList.add("기관");
					keyList.add("유종");
					keyList.add("주유량");
					keyList.add("쿠폰");
					keyList.add("회원한글명");
					keyList.add("NO");
					printer.printRecord(keyList);
				} else if ((uploadProgress.getPageId()).equals("pan0106")) {
					List<String> keyList = new ArrayList<String>();
					keyList.add("회원ID");
					keyList.add("OCB닷컴 United ID");
					keyList.add("성별");
					keyList.add("연령");
					keyList.add("자택 행정동 대그룹명");
					keyList.add("자택 행정동 중그룹명");
					keyList.add("직장 행정동 대그룹명");
					keyList.add("직장 행정동 중그룹명");
					keyList.add("결혼 여부");
					keyList.add("마케팅 동의 여부");
					keyList.add("이메일 수신 동의 여부");
					keyList.add("광고성 SMS 수신 동의 여부");
					keyList.add("정보성 SMS 수신 동의 여부");
					keyList.add("앱 푸시 수신 동의 여부");
					keyList.add("혜택/모바일전단 푸시 동의 여부");
					keyList.add("포인트사용적립 푸시 동의 여부");
					keyList.add("친구와 함께쓰기 푸시 동의 여부");
					keyList.add("코인알림 푸시 동의 여부");
					keyList.add("위치활용 동의 여부");
					keyList.add("모바일전단매장 동의 여부");
					keyList.add("모바일전단상권 동의 여부");
					keyList.add("OCB닷컴 가입 여부");
					keyList.add("OCB앱 가입 여부");
					keyList.add("OCB플러스 가입 여부");
					keyList.add("629세그먼트코드");
					keyList.add("트래픽세그먼트코드");
					keyList.add("OCB닷컴 최종 로그인 일자");
					keyList.add("OCB앱 최종 사용 일자");
					keyList.add("앱출석 최종 사용 일자");
					keyList.add("룰렛 최종 사용 일자");
					keyList.add("게임 최종 사용 일자");
					keyList.add("OCB플러스 최종 유실적 일자");
					keyList.add("라커0 최종 유실적 일자");
					keyList.add("미리줌 최종 유실적 일자");
					keyList.add("더줌 최종 유실적 일자");
					keyList.add("상품전단 최종 유실적 일자");
					keyList.add("모바일카드 보유 여부");
					keyList.add("SKP카드 보유 여부");
					keyList.add("신용카드 보유 여부");
					keyList.add("체크카드 보유 여부");
					keyList.add("통신카드 보유 여부");
					keyList.add("엔크린보너스카드 보유 여부");
					keyList.add("화물복지카드 보유 여부");
					keyList.add("주유전용카드 보유 여부");
					keyList.add("주유 최종 유실적 일자");
					keyList.add("통신 최종 유실적 일자");
					keyList.add("금융 최종 유실적 일자");
					keyList.add("기타 최종 유실적 일자");
					keyList.add("온라인 최종 유실적 일자");
					keyList.add("온라인쿠폰 최종 유실적 일자");
					keyList.add("가용포인트");
					keyList.add("NO");
					printer.printRecord(keyList);
				} else {

					Set<?> keyList = list.get(0).keySet();
					printer.printRecord(keyList);
				}
			}

			@Override
			protected void printRecord(CSVPrinter printer, AutoMap map) throws IOException {

				if ((uploadProgress.getPageId()).equals("pan0101")) {

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

				} else if ((uploadProgress.getPageId()).equals("pan0103")) {

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

				} else if ((uploadProgress.getPageId()).equals("pan0105")) {

					List<String> dataList = new ArrayList<String>();

					if (map.get("rcvDt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("rcvDt").toString());
					}
					if (map.get("rcvSeq") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("rcvSeq").toString());
					}
					if (map.get("apprDttm") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("apprDttm").toString());
					}
					if (map.get("repApprNo") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("repApprNo").toString());
					}
					if (map.get("apprNo") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("apprNo").toString());
					}
					if (map.get("saleDttm") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("saleDttm").toString());
					}
					if (map.get("mbrId") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mbrId").toString());
					}
					if (map.get("cardDtlGrp") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("cardDtlGrp").toString());
					}
					if (map.get("cardNo") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("cardNo").toString());
					}
					if (map.get("alcmpn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("alcmpn").toString());
					}
					if (map.get("mcnt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mcnt").toString());
					}
					if (map.get("stlmtMcnt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("stlmtMcnt").toString());
					}
					if (map.get("pntKnd") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("pntKnd").toString());
					}
					if (map.get("slip") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("slip").toString());
					}
					if (map.get("saleAmt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("saleAmt").toString());
					}
					if (map.get("pnt") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("pnt").toString());
					}
					if (map.get("csMbrCmmsn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("csMbrCmmsn").toString());
					}
					if (map.get("cmmsn") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("cmmsn").toString());
					}
					if (map.get("pmntWayCd") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("pmntWayCd").toString());
					}
					if (map.get("org") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("org").toString());
					}
					if (map.get("oilPrdctSgrp") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("oilPrdctSgrp").toString());
					}
					if (map.get("saleQty") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("saleQty").toString());
					}
					if (map.get("cpnPrd") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("cpnPrd").toString());
					}
					if (map.get("mbrKorNm") == null) {
						dataList.add(" ");
					} else {
						dataList.add(map.get("mbrKorNm").toString());
					}
					if (map.get("rnum") == null) {
						dataList.add(" ");
					} else {
						dataList.add((String) map.get("rnum").toString());
					}

					printer.printRecord(dataList);

				} else if ((uploadProgress.getPageId()).equals("pan0106")) {
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
		csvCreator.create(filePath);

		return filePath.toFile().getAbsolutePath();
	}

}
