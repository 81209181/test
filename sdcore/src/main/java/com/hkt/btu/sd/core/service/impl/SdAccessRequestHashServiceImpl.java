package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.mapper.SdAccessRequestHashMapper;
import com.hkt.btu.sd.core.dao.mapper.SdAccessRequestMapper;
import com.hkt.btu.sd.core.service.SdAccessRequestHashService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;
import java.util.Random;

public class SdAccessRequestHashServiceImpl implements SdAccessRequestHashService {
    private static final Logger LOG = LogManager.getLogger(SdAccessRequestHashServiceImpl.class);

    @Resource
    SdAccessRequestMapper sdAccessRequestMapper;
    @Resource
    SdAccessRequestHashMapper sdAccessRequestHashMapper;

    private Random random;

    @Override
    public void generateHashId() {
        final int AVAILABLE_KEY_THRESHOLD = 2000;
        final int RANDOM_TRAIL_LIMIT = 6000;
        final int LOG_INTERVAL = 500;

        // get current id info
        Integer maxUsedId = sdAccessRequestMapper.getMaxAccessRequestId();
        if(maxUsedId==null) maxUsedId = 0;
        LOG.info("Max Used Id: " + maxUsedId);

        Integer maxHashedId = sdAccessRequestHashMapper.getMaxHashedId();
        if(maxHashedId==null) maxHashedId = 0;
        LOG.info("Max Hashed Id: " + maxHashedId);

        int hashedIdLead = maxHashedId - maxUsedId;
        if(hashedIdLead > AVAILABLE_KEY_THRESHOLD){
            LOG.info("Skip hash generation. (availableIdCount: " + hashedIdLead + ")");
            return;
        }

        Integer targetAccessRequestId = 1 + maxHashedId;
        for(int i=0; i<RANDOM_TRAIL_LIMIT; i++){
            if( i%LOG_INTERVAL == 0 ){
                LOG.info("Trail count: " + i + "/" + RANDOM_TRAIL_LIMIT);
            }

            try {
                tryGenerateOneHashId(targetAccessRequestId);
            } catch (DuplicateKeyException e) {
                // continue re-try with collided hashed id
                continue;
            }

            // move on to next id
            targetAccessRequestId++;
        }
        LOG.info( "Access request id of last generated hash: " + (targetAccessRequestId-1) );
    }

    private void tryGenerateOneHashId(Integer accessRequestId) {
        Integer hashedId = generateSevenDigitRandomInt();
        sdAccessRequestHashMapper.insertHashedId(accessRequestId, hashedId);
    }

    @Override
    public Integer getHashedId(Integer accessRequestId) {
        return sdAccessRequestHashMapper.getHashedId(accessRequestId);
    }

    @Override
    public Integer getAccessRequestId(Integer hashedId) {
        return sdAccessRequestHashMapper.getAccessRequestId(hashedId);
    }

    private Integer generateSevenDigitRandomInt(){
        if(random==null){
            random = new Random();
        }
        return random.nextInt(9999998) + 1;
    }
}
