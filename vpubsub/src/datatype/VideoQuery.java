package datatype;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VideoQuery implements Serializable{
	double xStart, xEnd;
	double yStart, yEnd;
	String[] keywords;
	Filter filter;

	public VideoQuery(double xStart, double xEnd, double yStart, double yEnd, String[] keywords, Filter filter) {
		super();
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.yStart = yStart;
		this.yEnd = yEnd;
		this.keywords = keywords;
	}

	public double getxStart() {
		return xStart;
	}
	public void setxStart(double xStart) {
		this.xStart = xStart;
	}
	public double getxEnd() {
		return xEnd;
	}
	public void setxEnd(double xEnd) {
		this.xEnd = xEnd;
	}
	public double getyStart() {
		return yStart;
	}
	public void setyStart(double yStart) {
		this.yStart = yStart;
	}
	public double getyEnd() {
		return yEnd;
	}
	public void setyEnd(double yEnd) {
		this.yEnd = yEnd;
	}
	public String[] getKeywords() {
		return keywords;
	}
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
}