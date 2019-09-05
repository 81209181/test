package com.hkt.btu.common.genrator;

import java.util.List;

public interface BtuCSVGenrator {

    void generateCSV(String filePath, String sperator, List<String[]> content);

    void generateCSV(String filePath, String sperator, String[] content);
}
