package com.hkt.btu.sd;

import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import org.apache.commons.lang3.StringUtils;
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
        List<String> entities = configParamMapper.getConfigGroupList();
        for (String entity : entities) {
            System.out.println(entity);
        }
    }

    @Test
    public void testStringUtils() {
        System.out.println(StringUtils.join("config key"," already exists."));;
    }
}
