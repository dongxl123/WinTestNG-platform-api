package com.winbaoxian.testng.platform.model.enums;

public enum PlatformSysSettingsKey {

    qualityReportEmailTos("quality.report.email.tos"),
    qualityScoreProjectShow("quality.score.project.show"),
    qualityScoreTestCasesShow("quality.score.testcases.show"),
    qualityScoreReportProjectShow("quality.score.report.project.show"),
    ;

    private String keyName;

    PlatformSysSettingsKey(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return this.keyName;
    }

}
