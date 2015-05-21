package entity;

public class ImageDrone extends AbstractEntity {
	private Position position;
	private int width;
	private int height;
	private String encoding;
	private String image;

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String strImage) {
		this.image = strImage;
	}
}