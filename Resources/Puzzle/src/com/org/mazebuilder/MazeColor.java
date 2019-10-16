package com.org.mazebuilder;

public class MazeColor {
	private Double r;
	private Double g;
	private Double b;
	private Double opacity;

	public MazeColor(Double r, Double g, Double b, Double opacity) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
		this.opacity = opacity;
	}
	
	public Double getR() {
		return r;
	}
	public void setR(Double r) {
		this.r = r;
	}
	public Double getG() {
		return g;
	}
	public void setG(Double g) {
		this.g = g;
	}
	public Double getB() {
		return b;
	}
	public void setB(Double b) {
		this.b = b;
	}
	public Double getOpacity() {
		return opacity;
	}
	public void setOpacity(Double opacity) {
		this.opacity = opacity;
	}
	
	
}
