package com.source3g.hermes.entity.merchant;

public class ElectricMenuItem {
	private String title;
	private double price;
	private String picPath;
	/**
	 * 缩略图位置
	 */
	private String abstractPicPath;
	private String unit;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getAbstractPicPath() {
		return abstractPicPath;
	}

	public void setAbstractPicPath(String abstractPicPath) {
		this.abstractPicPath = abstractPicPath;
	}

}
