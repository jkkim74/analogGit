package com.skplanet.web.repository.mysql;

import com.skplanet.web.model.PandoraLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cookatrice on 2017. 5. 24..
 */

@Repository
public interface PandoraLogRepository {

    void insertPandoraMenuAction(PandoraLog pandoraLog);

    List<PandoraLog> selectPandoraMenuActions();


}
