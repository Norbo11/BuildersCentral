package com.github.norbo11.topbuilders.models;

import com.github.norbo11.topbuilders.util.Settings;

public class QuoteSetting extends AbstractSetting {
    public static final String DB_TABLE_NAME = "quoteSettings";	

    public QuoteSetting() {
        super("projectId");
    }
    
    @Override
    public String getDbTableName() {
        return DB_TABLE_NAME;
    }
    
    /* Static methods */
    
    public static Settings<QuoteSetting> loadQuoteSettingsForProject(Project project) {
        return new Settings<QuoteSetting>(project, loadList(loadAllModelsWhere(DB_TABLE_NAME, "projectId", project.getId()), QuoteSetting.class), QuoteSetting.class);
    }
}
