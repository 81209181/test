package com.hkt.btu.common.generator;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface BtuCSVGenerator {

    File generateCSV(String filePath, Character separator, List<Map<String, Object>> content, String dateFormat);

}
