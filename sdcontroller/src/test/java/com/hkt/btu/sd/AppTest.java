package com.hkt.btu.sd;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
    @Autowired
    SdConfigParamMapper configParamMapper;

    @Test
    public void shouldAnswerWithTrue() {
        List<SdConfigParamEntity> entities = configParamMapper.getConfigGroupList();
        for (SdConfigParamEntity entity : entities) {
            System.out.println(entity.getConfigGroup());
        }
    }
}
