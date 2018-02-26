package kr.co.daegu.book;

import java.io.Serializable;

public class BookDTO implements Serializable {

	private int no;//��ȣ

	private String title;//������
	private String sutitle;//����
	private String author;//����
	private String icon;//�����̹���
	private String publisher; //���ǻ�
	private String publishing;//���ǿ���
	private int bookPage;//������
	
	private String bookInf;//å����
	private String authorInf; //���ڼҰ�
	private String tableOfCon;//����
	private String tableOfConTwo;//���� ������
	private String inBook; //å ������
	private String publishReview;//���ǻ��� ����
	
	private String registNum; //��Ϲ�ȣ, �⺻Ű
	private String state;//��������, DEFAULT '���Ⱑ��'
	private int countting;//��ȸ��, DEFAULT 0
	
	private int bookExistence;//å ����, DEFAULT 1

	public BookDTO() {
		super();
	}

	public BookDTO(int no, String title, String sutitle, String author, String icon, String publisher,
			String publishing, int bookPage, String bookInf, String authorInf, String tableOfCon, String tableOfConTwo,
			String inBook, String publishReview, String registNum, String state, int countting, int bookExistence) {
		super();
		this.no = no;
		this.title = title;
		this.sutitle = sutitle;
		this.author = author;
		this.icon = icon;
		this.publisher = publisher;
		this.publishing = publishing;
		this.bookPage = bookPage;
		this.bookInf = bookInf;
		this.authorInf = authorInf;
		this.tableOfCon = tableOfCon;
		this.tableOfConTwo = tableOfConTwo;
		this.inBook = inBook;
		this.publishReview = publishReview;
		this.registNum = registNum;
		this.state = state;
		this.countting = countting;
		this.bookExistence = bookExistence;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSutitle() {
		return sutitle;
	}

	public void setSutitle(String sutitle) {
		this.sutitle = sutitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublishing() {
		return publishing;
	}

	public void setPublishing(String publishing) {
		this.publishing = publishing;
	}

	public int getBookPage() {
		return bookPage;
	}

	public void setBookPage(int bookPage) {
		this.bookPage = bookPage;
	}

	public String getBookInf() {
		return bookInf;
	}

	public void setBookInf(String bookInf) {
		this.bookInf = bookInf;
	}

	public String getAuthorInf() {
		return authorInf;
	}

	public void setAuthorInf(String authorInf) {
		this.authorInf = authorInf;
	}

	public String getTableOfCon() {
		return tableOfCon;
	}

	public void setTableOfCon(String tableOfCon) {
		this.tableOfCon = tableOfCon;
	}

	public String getTableOfConTwo() {
		return tableOfConTwo;
	}

	public void setTableOfConTwo(String tableOfConTwo) {
		this.tableOfConTwo = tableOfConTwo;
	}

	public String getInBook() {
		return inBook;
	}

	public void setInBook(String inBook) {
		this.inBook = inBook;
	}

	public String getPublishReview() {
		return publishReview;
	}

	public void setPublishReview(String publishReview) {
		this.publishReview = publishReview;
	}

	public String getRegistNum() {
		return registNum;
	}

	public void setRegistNum(String registNum) {
		this.registNum = registNum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getCountting() {
		return countting;
	}

	public void setCountting(int countting) {
		this.countting = countting;
	}

	public int getBookExistence() {
		return bookExistence;
	}

	public void setBookExistence(int bookExistence) {
		this.bookExistence = bookExistence;
	}

	@Override
	public String toString() {
		return "BookDTO [no=" + no + ", title=" + title + ", sutitle=" + sutitle + ", author=" + author + ", icon="
				+ icon + ", publisher=" + publisher + ", publishing=" + publishing + ", bookPage=" + bookPage
				+ ", bookInf=" + bookInf + ", authorInf=" + authorInf + ", tableOfCon=" + tableOfCon
				+ ", tableOfConTwo=" + tableOfConTwo + ", inBook=" + inBook + ", publishReview=" + publishReview
				+ ", registNum=" + registNum + ", state=" + state + ", countting=" + countting + ", bookExistence="
				+ bookExistence + "]";
	}

}
