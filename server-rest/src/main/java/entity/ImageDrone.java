package entity;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class ImageDrone extends AbstractEntity {
	private int width;
	private int height;
	private String encoding;
	private byte[] image;

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

	public byte[] getImage() {
		return image;
	}

	public void setImage(String strImage) throws DecoderException {
		
		this.image = Base64.decodeBase64(strImage);
	}
}