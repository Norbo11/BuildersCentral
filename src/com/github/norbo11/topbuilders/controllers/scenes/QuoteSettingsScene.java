package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.tabs.QuotesTab;
import com.github.norbo11.topbuilders.models.QuoteSetting;
import com.github.norbo11.topbuilders.models.enums.QuoteSettingType;
import com.github.norbo11.topbuilders.util.Settings;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;


public class QuoteSettingsScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/QuoteSettingsScene.fxml";

    @FXML private CheckBox groupsEnabled, jobDescriptionsEnabled, materialsEnabled, splitPrice;
    private Settings<QuoteSetting> settings;
    private QuotesTab quotesTab;
    
    public void setSettings(Settings<QuoteSetting> settings) {
        this.settings = settings;
    }
    
    public void setQuotesTab(QuotesTab quotesTab) {
        this.quotesTab = quotesTab;
    }

    @FXML
    public void save(ActionEvent event) {
        settings.set(QuoteSettingType.GROUPS_ENABLED, groupsEnabled.isSelected());
        settings.set(QuoteSettingType.JOB_DESCRIPTIONS_ENABLED, jobDescriptionsEnabled.isSelected());
        settings.set(QuoteSettingType.MATERIALS_ENABLED, materialsEnabled.isSelected());
        settings.set(QuoteSettingType.SPLIT_PRICE, splitPrice.isSelected());
        settings.save();
        
        quotesTab.updateColumns();
        
        //In case the group enabled setting changed
        quotesTab.updateJobGroups(); 
        quotesTab.updateAddGroupControl();
        quotesTab.updateAddJobControl();
        
        discard(event);
    }
    
    @FXML
    public void discard(ActionEvent event) {
        SceneUtil.closeScene((Node) event.getSource());
    }

    public void updateAll() {
        groupsEnabled.setSelected(settings.getBoolean(QuoteSettingType.GROUPS_ENABLED));
        jobDescriptionsEnabled.setSelected(settings.getBoolean(QuoteSettingType.JOB_DESCRIPTIONS_ENABLED));
        materialsEnabled.setSelected(settings.getBoolean(QuoteSettingType.MATERIALS_ENABLED));
        splitPrice.setSelected(settings.getBoolean(QuoteSettingType.SPLIT_PRICE));
    }
}
