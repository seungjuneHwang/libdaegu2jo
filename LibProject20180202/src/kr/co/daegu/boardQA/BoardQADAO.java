package kr.co.daegu.boardQA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class BoardQADAO {
	
	public DataSource dataFactory;
	private String sql;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private ArrayList<BoardQADTO> qaList;
	private int cnt;

	public BoardQADAO() {
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
	}

	public ArrayList<BoardQADTO> qaList(BoardQADTO qaDTO) {// ��ü���
		try {
			sql = "select no,title,nal,author,readcount from boardqa where visible=1 order by no desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			qaList = new ArrayList<BoardQADTO>();
			while (rs.next()) {
				qaDTO = new BoardQADTO();
				qaDTO.setNo(rs.getInt("no"));
				qaDTO.setTitle(rs.getString("title"));
				qaDTO.setNal(rs.getString("nal"));
				qaDTO.setAuthor(rs.getString("author"));
				qaDTO.setReadcount(rs.getInt("readcount"));
				qaList.add(qaDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return qaList;
	}// ��ü���

	public void qaReadCount(BoardQADTO qaDTO) {// ��ȸ������
		try {
			sql = "update boardqa set readcount=? where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qaDTO.getReadcount() + 1);
			pstmt.setInt(2, qaDTO.getNo());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// ��ȸ������

	public int totalCount() {// ����¡ó��: ��ü���ڵ� ���� ���ϱ�
		int count = 0;
		try {
			sql = "select count(*) from boardqa where visible=1";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}// ����¡ó��: ��ü���ڵ� ���� ���ϱ�

	public PageToQA page(int curPage) {// ����������
		PageToQA pageTo = new PageToQA();
		int totalCount = totalCount();
		ArrayList<BoardQADTO> list = new ArrayList<BoardQADTO>();
		try {
			sql = "select no,title,author,content,nal,readcount from boardqa where visible=1 order by no desc";
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// TYPE_SCROLL_INSENSITIVE:scroll�� �����ϳ�, ����� ������ ������� ����
			// �����, ��ũ�� �� ������Ʈ �ݿ�����
			// CONCUR_READ_ONLY :Ŀ���� ��ġ���� ���� ������Ʈ �Ұ�,ResultSet�� ������ �Ұ���
			rs = pstmt.executeQuery();
			int perPage = pageTo.getPerPage();// 5
			int skip = (curPage - 1) * perPage;
			if (skip > 0) {
				rs.absolute(skip);
			}
			// ResultSet�� absolute�޼ҵ带 �̿��Ͽ� �ش� �������� Cursor �� ��ġ�� �̵�...
			for (int i = 0; i < perPage && rs.next(); i++) {
				int no = rs.getInt("no");
				String author = rs.getString("author");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int readCount = rs.getInt("readcount");
				String nal = rs.getString("nal");

				BoardQADTO data = new BoardQADTO();
				data.setNo(no);
				data.setAuthor(author);
				data.setTitle(title);
				data.setContent(content);
				data.setReadcount(readCount);
				data.setNal(nal);
				list.add(data);
			}
			pageTo.setList(list);// ArrayList ����
			pageTo.setTotalCount(totalCount);// ��ü���ڵ尳��
			pageTo.setCurPage(curPage);// ����������
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pageTo;
	}// ����������

	public void noticeSqlInsert() {
		sql = "select max(no) as no from boardqa";
	}

	public void noticeBunho() {
		try {
			noticeSqlInsert();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
			while (rs.next()) {
				cnt = rs.getInt("no");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void qaWrite(BoardQADTO qaDTO) {// �۾���
		noticeBunho();
		qaDTO.setNo(cnt + 1);
		sql = "insert into boardqa(no,title,content,author,nal,readcount) values(?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qaDTO.getNo());
			pstmt.setString(2, qaDTO.getTitle());
			pstmt.setString(3, qaDTO.getContent());
			pstmt.setString(4, qaDTO.getAuthor());
			pstmt.setString(5, qaDTO.getNal());
			pstmt.setInt(6, qaDTO.getReadcount());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// �۾���

	public ArrayList<BoardQADTO> qaSearch(BoardQADTO qaDTO, String choice) {// �˻�
		try {
			if (choice.equals("A")) {
				sql = "select no,title,author,nal,readcount from boardqa where visible=1 and (title like ? or author like ?) order by no";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + qaDTO.getTitle() + "%");
				pstmt.setString(2, "%" + qaDTO.getAuthor() + "%");
			} else if (choice.equals("T")) {
				sql = "select no,title,author,nal,readcount from boardqa where visible=1 and title like ? order by no";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + qaDTO.getTitle() + "%");
			} else if (choice.equals("C")) {
				sql = "select no,title,author,nal,readcount from boardqa where visible=1 and author like ? order by no";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + qaDTO.getAuthor() + "%");
			}
			rs = pstmt.executeQuery();
			qaList = new ArrayList<BoardQADTO>();
			while (rs.next()) {
				qaDTO = new BoardQADTO();
				qaDTO.setNo(rs.getInt("no"));
				qaDTO.setTitle(rs.getString("title"));
				qaDTO.setAuthor(rs.getString("author"));
				qaDTO.setNal(rs.getString("nal"));
				qaDTO.setReadcount(rs.getInt("readcount"));
				qaList.add(qaDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return qaList;
	}// �˻�

	public BoardQADTO qaContent(BoardQADTO qaDTO) {// ���뺸��
		sql = "select no,title,content,author,nal,readcount from boardqa where no = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qaDTO.getNo());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				qaDTO = new BoardQADTO();
				qaDTO.setNo(rs.getInt("no"));
				qaDTO.setTitle(rs.getString("title"));
				qaDTO.setContent(rs.getString("content"));
				qaDTO.setAuthor(rs.getString("author"));
				qaDTO.setNal(rs.getString("nal"));
				qaDTO.setReadcount(rs.getInt("readcount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return qaDTO;

	}// ���뺸��

	public void qaDelete(BoardQADTO qaDTO) {// ����
		try {
			sql = "update boardqa set visible=1 where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qaDTO.getNo());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// ����

	public void qaUpdate(BoardQADTO qaDTO) {// �Խñۼ���
		try {
			sql = "update boardqa set content=?,title=?,author=? where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, qaDTO.getContent());
			pstmt.setString(2, qaDTO.getTitle());
			pstmt.setString(3, qaDTO.getAuthor());
			pstmt.setInt(4, qaDTO.getNo());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// �Խñۼ���
}
