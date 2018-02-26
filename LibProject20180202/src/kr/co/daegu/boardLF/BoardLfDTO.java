package kr.co.daegu.boardLF;

import java.io.Serializable;

public class BoardLfDTO implements Serializable {
	
	private int no; //�Խù�ȣ
	private String icon; //�̹���
	private String place; //���
	private String content; //����
	private String nal; //��¥
	private String author; //�ۼ���
	private int visible; // 0�� �� �Խñ� ����, 1�� �� �Խñ� �����ֱ�
 
	public BoardLfDTO() {
		super();
	}
	public BoardLfDTO(int no, String icon, String place, String content, String nal, String author, int visible) {
		super();
		this.no = no;
		this.icon = icon;
		this.place = place;
		this.content = content;
		this.nal = nal;
		this.author = author;
		this.visible = visible;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNal() {
		return nal;
	}
	public void setNal(String nal) {
		this.nal = nal;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	@Override
	public String toString() {
		return "BoardLfDTO [no=" + no + ", icon=" + icon + ", place=" + place + ", content=" + content + ", nal=" + nal
				+ ", author=" + author + ", visible=" + visible + "]";
	}

}
