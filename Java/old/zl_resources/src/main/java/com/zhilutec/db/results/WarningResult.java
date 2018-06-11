package com.zhilutec.db.results;

public class WarningResult {

	private String level;
    private int amount;
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "WorningRs [level=" + level + ", amount=" + amount + "]";
	}

		
	
}
