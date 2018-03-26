package com.skplanet.web.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Resources;
import com.ptsapi.client.ApiClient;
import com.pts.api.client.VexApiClient;
import com.skplanet.web.exception.BizException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PtsService {

	@Value("${app.enable.pts}")
	private boolean enabled;
    public void sendOld(String filename, String ptsUsername) {
        log.info("{} Sending file [{}]", ptsUsername, filename);

        if (!enabled) {
            log.debug("disabled");
            return;
        }

        String ptsProperties = Resources.getResource("config/PTS.properties").getPath();
        log.debug("PTS.properties location={}", ptsProperties);

        BufferedReader in = null;
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            in = new BufferedReader(new FileReader(ptsProperties));
            fw = new FileWriter(ptsProperties + "." + ptsUsername);
            bw = new BufferedWriter(fw);

            String line = null;
            int cnt = 0;
            while ((line = in.readLine()) != null) {
                if (cnt == 0)
                    bw.write(line + filename.substring(0, filename.lastIndexOf("/")));
                else
                    bw.write(line);

                bw.newLine();
                cnt++;
            }

            in.close();
            bw.close();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        log.info("Sending file [{}]", filename.substring(filename.lastIndexOf("/") + 1, filename.length()));
        log.info("Sending pts dir [{}]", ptsProperties + "." + ptsUsername);

        String[] command = { filename.substring(filename.lastIndexOf("/") + 1, filename.length()),
                ptsProperties + "." + ptsUsername };

        try {
            ApiClient.main(command);
        } catch (IOException e) {
            throw new BizException("PTS 전송 실패", e);
        }
    }


    public void send(String filename, String ptsUsername) {
        log.info("{} Sending file [{}]", ptsUsername, filename);

        if (!enabled) {
            log.debug("disabled");
            return;
        }

        String[] command = { filename };

        //로그 기록 위치 (DEFAULT: System.getProperty(user.dir) + EXAPI.log)
        System.setProperty("vex_log", "/app/log/tomcat/EXAPI.log") ;
        //설정파일 위치 (DEFAULT: System.getProperty(user.dir) + VEXAPI.propertis)
        System.setProperty("vex_cnf", "/app/docroot/tomcat/svr_KHUBc-ocbpisapp1/ROOT/WEB-INF/classes/config/VEXAPI.properties") ;

        VexApiClient.main(command);

    }
}
