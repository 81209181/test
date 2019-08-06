package com.hkt.btu.sd;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({
        "classpath*:btuBeans.xml",
        "classpath*:daoBeans.xml",
        "classpath*:serviceBeans.xml",
        "classpath*:facadeBeans.xml"
})
public class SdXmlConfig {

}
