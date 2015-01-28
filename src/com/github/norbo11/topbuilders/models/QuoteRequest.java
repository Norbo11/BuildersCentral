package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuoteRequest extends AbstractModel {
	
	public static final String DB_TABLE_NAME = "quoteRequests";

    @Override
    public void add() {
        // TODO Auto-generated method stub
        
    }
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadFromResult(ResultSet result, String... columns) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDbTableName() {
		return DB_TABLE_NAME;
	}

}
