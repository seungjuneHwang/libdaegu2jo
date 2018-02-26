package kr.co.daegu.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class BookDAO {

	public DataSource dataFactory;
	private Connection conn;
	private String sql;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private ArrayList<BookDTO> bookList;
	private int cnt;

	public BookDAO() {// ������ ����
		try {
			Context ctx = new InitialContext();
			/*dataFactory = (DataSource)ctx.lookup("java:comp/env/jdbc/kdh0115");*/
			dataFactory = (DataSource) ctx.lookup("java:comp/env/jdbc/Oracle11g");
			conn = dataFactory.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// ������ ��

	public ArrayList<BookDTO> infoList(BookDTO infoDTO) {// info�� �α⵵�� ���
		try {

			sql = "select no,title,icon,publishing,author,registNum from book order by no desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			bookList = new ArrayList<BookDTO>();
			while (rs.next()) {
				infoDTO = new BookDTO();
				infoDTO.setNo(rs.getInt("no"));
				infoDTO.setTitle(rs.getString("title"));
				infoDTO.setIcon(rs.getString("icon"));
				infoDTO.setPublishing(rs.getString("publishing"));
				infoDTO.setAuthor(rs.getString("author"));
				infoDTO.setRegistNum(rs.getString("registNum"));

				bookList.add(infoDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookList;
	}// info�� �α⵵�� ���

	public void bookSqlInsert() {
		sql = "select max(no) as no from book";
	}

	public int bookBunho() {// DB ������ȣ
		try {
			bookSqlInsert();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next())
				cnt = rs.getInt("no");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt + 1;
	}// DB ������ȣ

	public String bookRegistNum(int a) {// ��Ϲ�ȣ

		// a�� DB���� �޾ƿ� no
		String str[] = { "0", "0", "0", "0", "0", "M" };// ���ڸ� String ������ ����

		int j = 0;// no�� �ڸ� ���� �����ϱ� ���� ����

		while (a != 0) {
			str[j] = String.valueOf(a % 10);// int���� String������ ����
			a /= 10;
			j++;
		}

		String arr = str[j - 1];

		for (int k = j - 2; k >= 0; k--) {
			arr = arr.concat(str[k]);
		}

		if (j == 5) {
			arr = str[5].concat(arr);
		} else if (j == 4) {
			arr = str[5].concat("0" + arr);
		} else if (j == 3) {
			arr = str[5].concat("00" + arr);
		} else if (j == 2) {
			arr = str[5].concat("000" + arr);
		} else if (j == 1) {
			arr = str[5].concat("0000" + arr);
		}

		System.out.println("arr : " + arr);
		return arr;
	}// ��Ϲ�ȣ

	public int bookInsert(BookDTO bookDTO) {// �������

		try {
			cnt = bookBunho();
			bookDTO.setRegistNum(bookRegistNum(cnt));
			bookDTO.setNo(cnt);
			sql = "insert into book(no,title,sutitle,author,icon,publisher,publishing,bookPage,bookInf,authorInf,tableOfCon,tableOfConTwo,inBook,publishReview,registNum) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookDTO.getNo());
			pstmt.setString(2, bookDTO.getTitle());
			pstmt.setString(3, bookDTO.getSutitle());
			pstmt.setString(4, bookDTO.getAuthor());
			pstmt.setString(5, bookDTO.getIcon());
			pstmt.setString(6, bookDTO.getPublisher());
			pstmt.setString(7, bookDTO.getPublishing());
			pstmt.setInt(8, bookDTO.getBookPage());
			pstmt.setString(9, bookDTO.getBookInf());
			pstmt.setString(10, bookDTO.getAuthorInf());
			pstmt.setString(11, bookDTO.getTableOfCon());
			pstmt.setString(12, bookDTO.getTableOfConTwo());
			pstmt.setString(13, bookDTO.getInBook());
			pstmt.setString(14, bookDTO.getPublishReview());
			pstmt.setString(15, bookDTO.getRegistNum());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}// �������

	public int bookDelete(BookDTO bookDTO) {// ��������
		try {
			// sql="delete from book where title=? and author=?";
			sql = "update book set state=?, bookExistence=0 where title=? and author=? and bookExistence=1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "����Ұ���");
			pstmt.setString(2, bookDTO.getTitle());
			pstmt.setString(3, bookDTO.getAuthor());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cnt;

	}// ��������

	public int bookUpdate(BookDTO bookDTO) {// ���� ����
		// ���� ���� ����

		try {
			if (bookDTO.getIcon().equals("")) {// ���� �̹����� �������� �ʾ��� ��

				sql = "update book set title=?,sutitle=?,author=?,publisher=?,publishing=?,bookPage=?,bookInf=?,authorInf=?,tableOfCon=?,tableOfConTwo=?,inBook=?,publishReview=? where registNum=? and bookExistence=1";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, bookDTO.getTitle());
				pstmt.setString(2, bookDTO.getSutitle());
				pstmt.setString(3, bookDTO.getAuthor());
				pstmt.setString(4, bookDTO.getPublisher());
				pstmt.setString(5, bookDTO.getPublishing());
				pstmt.setInt(6, bookDTO.getBookPage());
				pstmt.setString(7, bookDTO.getBookInf());
				pstmt.setString(8, bookDTO.getAuthorInf());
				pstmt.setString(9, bookDTO.getTableOfCon());
				pstmt.setString(10, bookDTO.getTableOfConTwo());
				pstmt.setString(11, bookDTO.getInBook());
				pstmt.setString(12, bookDTO.getPublishReview());
				pstmt.setString(13, bookDTO.getRegistNum());
			} else {// ���� �̹����� �������� ��
				sql = "update book set title=?,sutitle=?,author=?,icon=?,publisher=?,publishing=?,bookPage=?,bookInf=?,authorInf=?,tableOfCon=?,tableOfConTwo=?,inBook=?,publishReview=? where registNum=? and bookExistence=1";

				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, bookDTO.getTitle());
				pstmt.setString(2, bookDTO.getSutitle());
				pstmt.setString(3, bookDTO.getAuthor());
				pstmt.setString(4, bookDTO.getIcon());
				pstmt.setString(5, bookDTO.getPublisher());
				pstmt.setString(6, bookDTO.getPublishing());
				pstmt.setInt(7, bookDTO.getBookPage());
				pstmt.setString(8, bookDTO.getBookInf());
				pstmt.setString(9, bookDTO.getAuthorInf());
				pstmt.setString(10, bookDTO.getTableOfCon());
				pstmt.setString(11, bookDTO.getTableOfConTwo());
				pstmt.setString(12, bookDTO.getInBook());
				pstmt.setString(13, bookDTO.getPublishReview());
				pstmt.setString(14, bookDTO.getRegistNum());

			}
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}// ��������

	public BookDTO bookBest(BookDTO bookDTO) {// �ڷ�˻� - �α⵵��, ����Ʈ���� ������

		String sql = "select title,sutitle,author,icon,publisher,publishing,bookPage,bookInf,authorInf,tableOfCon,tableOfConTwo,inBook,publishReview,registNum from book where title like ? and bookExistence=1";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + bookDTO.getTitle() + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				bookDTO.setTitle(rs.getString("title"));
				bookDTO.setSutitle(rs.getString("sutitle"));
				bookDTO.setAuthor(rs.getString("author"));
				bookDTO.setIcon(rs.getString("icon"));
				bookDTO.setPublisher(rs.getString("publisher"));
				bookDTO.setPublishing(rs.getString("publishing"));
				bookDTO.setBookPage(rs.getInt("bookPage"));
				bookDTO.setBookInf(rs.getString("bookInf"));
				bookDTO.setAuthorInf(rs.getString("authorInf"));
				bookDTO.setTableOfCon(rs.getString("tableOfCon"));
				bookDTO.setTableOfConTwo(rs.getString("tableOfConTwo"));
				bookDTO.setInBook(rs.getString("inBook"));
				bookDTO.setPublishReview(rs.getString("publishReview"));
				bookDTO.setRegistNum(rs.getString("registNum"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bookDTO;
	}// �ڷ�˻� - �α⵵��, ����Ʈ���� ������

	public BookDTO bookConfirm(BookDTO bookDTO) {// �������� - DB�� ����Ǿ� �ִ� ���� ���� ��������

		String sql = "select registNum,title,sutitle,author,icon,publisher,publishing,bookPage,bookInf,authorInf,tableOfCon,tableOfConTwo,inBook,publishReview from book where title=? and author=? and bookExistence=1";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookDTO.getTitle());
			pstmt.setString(2, bookDTO.getAuthor());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				bookDTO.setRegistNum(rs.getString("registNum"));
				bookDTO.setTitle(rs.getString("title"));
				bookDTO.setSutitle(rs.getString("sutitle"));
				bookDTO.setAuthor(rs.getString("author"));
				bookDTO.setIcon(rs.getString("icon"));
				bookDTO.setPublisher(rs.getString("publisher"));
				bookDTO.setPublishing(rs.getString("publishing"));
				bookDTO.setBookPage(rs.getInt("bookPage"));
				bookDTO.setBookInf(rs.getString("bookInf"));
				bookDTO.setAuthorInf(rs.getString("authorInf"));
				bookDTO.setTableOfCon(rs.getString("tableOfCon"));
				bookDTO.setTableOfConTwo(rs.getString("tableOfConTwo"));
				bookDTO.setInBook(rs.getString("inBook"));
				bookDTO.setPublishReview(rs.getString("publishReview"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bookDTO;
	}// �������� - DB�� ����Ǿ� �ִ� ���� ���� ��������

	public ArrayList<BookDTO> bookSearch(BookDTO bookDTO, String setop) {// ���� �˻�

		try {

			/* setop�� �Ƿ����� �� : title, author, titleNauthor */
			// ��� : ��Ϲ�ȣ, ������, ����, �����̹���, ���ǿ���, ��������
			if (setop.equals("all")) {
				sql = "select registNum,title,author,icon,publishing,state from book where title like ? or author like ? order by registNum";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + bookDTO.getTitle() + "%");
				pstmt.setString(2, "%" + bookDTO.getAuthor() + "%");
			} else if (setop.equals("title")) {
				sql = "select registNum,title,author,icon,publishing,state from book where title like ? order by registNum";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + bookDTO.getTitle() + "%");
			} else if (setop.equals("author")) {
				sql = "select registNum,title,author,icon,publishing,state from book where author like ? order by registNum";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + bookDTO.getAuthor() + "%");
			} else if (setop.equals("titleNauthor")) {
				sql = "select registNum,title,author,icon,publishing,state from book where title like ? or author like ? order by registNum";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + bookDTO.getTitle() + "%");
				pstmt.setString(2, "%" + bookDTO.getAuthor() + "%");
			}

			rs = pstmt.executeQuery();

			bookList = new ArrayList<BookDTO>();

			while (rs.next()) {

				bookDTO = new BookDTO();
				// ��� : ��Ϲ�ȣ, ������, ����, �����̹���, ���ǿ���, ��������
				bookDTO.setRegistNum(rs.getString("registNum"));
				bookDTO.setTitle(rs.getString("title"));
				bookDTO.setAuthor(rs.getString("author"));
				bookDTO.setIcon(rs.getString("icon"));
				bookDTO.setPublishing(rs.getString("publishing"));
				bookDTO.setState(rs.getString("state"));

				bookList.add(bookDTO);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bookList;
	}// �����˻� ��

	public ArrayList<BookDTO> bookList(BookDTO bookDTO) {// å��ü���
		try {
			sql = "select registNum,title,author,icon,publishing,publisher,bookPage,state from book order by registNum";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			bookList = new ArrayList<BookDTO>();
			while (rs.next()) {
				bookDTO = new BookDTO();
				bookDTO.setRegistNum(rs.getString("registNum"));
				bookDTO.setTitle(rs.getString("title"));
				bookDTO.setAuthor(rs.getString("author"));
				bookDTO.setIcon(rs.getString("icon"));
				bookDTO.setPublishing(rs.getString("publishing"));
				bookDTO.setPublisher(rs.getString("publisher"));
				bookDTO.setBookPage(rs.getInt("bookPage"));
				bookDTO.setState(rs.getString("state"));

				bookList.add(bookDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookList;
	}// å��ü���

	public boolean borrowBookCheck(String registNum) {// å�� �ִ��� Ȯ��, �Ķ���ʹ� ��Ϲ�ȣ

		sql = "select registNum from book where registNum=?";
		boolean check = false;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, registNum);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				check = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return check;

	}// å�� �ִ��� Ȯ��

	public boolean borrowBookState(String registNum) { // ���Ⱑ������ Ȯ��

		sql = "select state from book where registNum=?";

		boolean bookstate = false;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, registNum);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				if (rs.getString("state").equals("���Ⱑ��")) {
					bookstate = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bookstate;
	}// ���Ⱑ������ Ȯ��

	public void bookStateUpdate(String registNum) {// å ���� ���������� ����
		try {
			sql = "update book set state=? where registNum=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "������");
			pstmt.setString(2, registNum);
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// å ���� ���������� ����

	public void bookStatePossibleUpdate(String registNum) {// å ���� ���Ⱑ������ ����
		try {
			sql = "update book set state=? where registNum=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "���Ⱑ��");
			pstmt.setString(2, registNum);
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// å ���� ���Ⱑ������ ����

}
