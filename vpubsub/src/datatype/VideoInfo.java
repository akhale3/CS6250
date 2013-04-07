package datatype;

import java.io.Serializable;

public class VideoInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	long ts;
	double x;
	double y;
	byte[] thumbnail;
	String[] keywords;

	public VideoInfo(long ts, double x, double y, byte[] thumbnail, String[] keywords) {
		super();
		this.ts = ts;
		this.x = x;
		this.y = y;
		this.thumbnail = thumbnail;
		this.keywords = keywords;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

}
