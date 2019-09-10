package com.hkt.btu.common.genrator;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface BtuCSVGenrator {

    File generateCSV(String filePath, Character separator, List<Map<String, Object>> content, String dateFormat);

}
