package com.hkt.btu.common.core.service;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface BtuCsvService {

    File generateCSV(String filePath, Character separator, List<Map<String, Object>> content);

}
