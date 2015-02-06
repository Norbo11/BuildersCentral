package com.github.norbo11.topbuilders.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuoteRequest extends AbstractModel {
	
	public static final String DB_TABLE_NAME = "quoteRequests";

    @Override
    public int add() {
        return 0;
        // TODO Auto-generated method stub
        
    }
	
	@Override
	public void update() {
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
