package com.wai.seifan.dto;

public class QuestInfo {
	private long no;
	private String title;
	private long step;
	private long reputationPoint;
	private long completePercent;
	private long manaCost;
	private long expGain;
	private long manaHad;
	private long manaTotal;	
	private long attackHad;
	private long attackTotal;
	private long defenseHad;
	private long defenseTotal;
	private long expCurrent;
	private long expTotal;
	private boolean isPath;
	private double ratioNeeded;
	private double ratioHad;
	private String type;

	public double getRatioNeededNext() {
		return (double) (manaHad - manaCost) / (expCurrent + expGain);
	}
	
	public long getWaitTime() {
		return (manaCost - manaHad) * 2;
	}
	
	public long getExpWithMana(long mana) {
		return (mana/this.manaCost)*this.expGain;
	}
	
	public long getExpToLevelUp() {
		return this.expTotal - this.expCurrent;
	}
	
	public long getNo() {
		return no;
	};
	public void setNo(long no) {
		this.no = no;
	}
	public long getReputationPoint() {
		return reputationPoint;
	}
	public void setReputationPoint(long reputationPoint) {
		this.reputationPoint = reputationPoint;
	}
	public long getCompletePercent() {
		return completePercent;
	}
	public void setCompletePercent(long completePercent) {
		this.completePercent = completePercent;
	}
	public long getManaCost() {
		return manaCost;
	}
	public void setManaCost(long manaCost) {
		this.manaCost = manaCost;
	}
	public long getExpGain() {
		return expGain;
	}
	public void setExpGain(long expGain) {
		this.expGain = expGain;
	}
	public long getManaHad() {
		return manaHad;
	}
	public void setManaHad(long manaHad) {
		this.manaHad = manaHad;
	}
	public long getManaTotal() {
		return manaTotal;
	}
	public void setManaTotal(long manaTotal) {
		this.manaTotal = manaTotal;
	}
	public long getExpCurrent() {
		return expCurrent;
	}
	public void setExpCurrent(long expCurrent) {
		this.expCurrent = expCurrent;
	}
	public long getExpTotal() {
		return expTotal;
	}
	public void setExpTotal(long expTotal) {
		this.expTotal = expTotal;
	}
	public boolean isPath() {
		return isPath;
	}
	public void setPath(boolean isPath) {
		this.isPath = isPath;
	}
	public double getRatioNeeded() {
		return ratioNeeded;
	}
	public void setRatioNeeded(double ratioNeeded) {
		this.ratioNeeded = ratioNeeded;
	}
	public double getRatioHad() {
		return ratioHad;
	}
	public void setRatioHad(double ratioHad) {
		this.ratioHad = ratioHad;
	}

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step = step;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getAttackHad() {
		return attackHad;
	}

	public void setAttackHad(long attackHad) {
		this.attackHad = attackHad;
	}

	public long getAttackTotal() {
		return attackTotal;
	}

	public void setAttackTotal(long attackTotal) {
		this.attackTotal = attackTotal;
	}

	public long getDefenseHad() {
		return defenseHad;
	}

	public void setDefenseHad(long defenseHad) {
		this.defenseHad = defenseHad;
	}

	public long getDefenseTotal() {
		return defenseTotal;
	}

	public void setDefenseTotal(long defenseTotal) {
		this.defenseTotal = defenseTotal;
	}
	
}
