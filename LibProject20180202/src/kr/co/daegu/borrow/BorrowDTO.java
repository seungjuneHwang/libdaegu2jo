package kr.co.daegu.borrow;

import java.io.Serializable;

public class BorrowDTO implements Serializable {

	private String givedate; // ������, default sysdate
	private String takedate; // �ݳ� ���� ��, default sysdate+7
	private String returndate; // �ݳ���, default NULL
	private int extension; // ���� ���� Ƚ��, default 0
	private int delaydate; // ��ü��, default 0
	private String registNum; // ��Ϲ�ȣ, NOT NULL
	private String id; // ���̵�, NOT NULL
	private int returnCheck; // �ݳ�����, default 0

	private String title;// ����, ���� ���� ���� ��� Ȯ�� �� �� �ʿ��� ����
	private String author;// ����, ���� ���� ���� ��� Ȯ�� �� �� �ʿ��� ����

	public BorrowDTO() {
		super();
	}

	public BorrowDTO(String givedate, String takedate, String returndate, int extension, int delaydate,
			String registNum, String id, int returnCheck, String title, String author) {
		super();
		this.givedate = givedate;
		this.takedate = takedate;
		this.returndate = returndate;
		this.extension = extension;
		this.delaydate = delaydate;
		this.registNum = registNum;
		this.id = id;
		this.returnCheck = returnCheck;
		this.title = title;
		this.author = author;
	}

	public String getGivedate() {
		return givedate;
	}

	public void setGivedate(String givedate) {
		this.givedate = givedate;
	}

	public String getTakedate() {
		return takedate;
	}

	public void setTakedate(String takedate) {
		this.takedate = takedate;
	}

	public String getReturndate() {
		return returndate;
	}

	public void setReturndate(String returndate) {
		this.returndate = returndate;
	}

	public int getExtension() {
		return extension;
	}

	public void setExtension(int extension) {
		this.extension = extension;
	}

	public int getDelaydate() {
		return delaydate;
	}

	public void setDelaydate(int delaydate) {
		this.delaydate = delaydate;
	}

	public String getRegistNum() {
		return registNum;
	}

	public void setRegistNum(String registNum) {
		this.registNum = registNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getReturnCheck() {
		return returnCheck;
	}

	public void setReturnCheck(int returnCheck) {
		this.returnCheck = returnCheck;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "BorrowDTO [givedate=" + givedate + ", takedate=" + takedate + ", returndate=" + returndate
				+ ", extension=" + extension + ", delaydate=" + delaydate + ", registNum=" + registNum + ", id=" + id
				+ ", returnCheck=" + returnCheck + ", title=" + title + ", author=" + author + "]";
	}

}