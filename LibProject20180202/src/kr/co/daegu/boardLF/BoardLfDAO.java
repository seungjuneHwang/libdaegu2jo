package kr.co.daegu.boardLF;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class BoardLfDAO {
	public DataSource dataFactory;
	private String sql;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private ArrayList<BoardLfDTO> lfList;
	private int cnt;

	public BoardLfDAO() {//������
		try {
			Context ctx = new InitialContext();
			dataFactory = (DataSource) ctx.lookup("java:comp/env/jdbc/Oracle11g");
			/*dataFactory = (DataSource)ctx.lookup("java:comp/env/jdbc/kdh0115");*/
			conn = dataFactory.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<BoardLfDTO> lfList(BoardLfDTO lfDTO) {// ��ü���
		try {
			sql = "select no,icon,place,nal,author from boardlf where visible=1 order by no desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			lfList = new ArrayList<BoardLfDTO>();
			while (rs.next()) {
				lfDTO = new BoardLfDTO();
				lfDTO.setNo(rs.getInt("no"));
				lfDTO.setIcon(rs.getString("icon"));
				lfDTO.setPlace(rs.getString("place"));
				lfDTO.setNal(rs.getString("nal"));
				lfDTO.setAuthor(rs.getString("author"));
				lfList.add(lfDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lfList;
	}// ��ü���

	public int totalCount() {// ����¡ó��: ��ü���ڵ� ���� ���ϱ�
		int count = 0;
		try {
			sql = "select count(*) from boardlf where visible=1";
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

	public PageToLF page(int curPage) {// ����������
		PageToLF pageTo = new PageToLF();
		int totalCount = totalCount();
		ArrayList<BoardLfDTO> list = new ArrayList<BoardLfDTO>();
		try {
			sql = "select no,icon,place,content,nal,author from boardlf where visible=1 order by no desc";
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
				String place = rs.getString("place");
				String author = rs.getString("author");
				String icon = rs.getString("icon");
				String content = rs.getString("content");
				String nal = rs.getString("nal");

				BoardLfDTO data = new BoardLfDTO();
				data.setNo(no);
				data.setIcon(icon);
				data.setPlace(place);
				data.setAuthor(author);
				data.setContent(content);
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

	public void lfSqlInsert() {
		sql = "select max(no) as no from boardlf";
	}

	public void lfBunho() {
		try {
			lfSqlInsert();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
			while (rs.next()) {
				cnt = rs.getInt("no");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void lfWrite(BoardLfDTO lfDTO) {// �۾���
		lfBunho();
		lfDTO.setNo(cnt + 1);
		sql = "insert into boardlf(no,icon,place,content,author,nal) values(?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, lfDTO.getNo());
			pstmt.setString(2, lfDTO.getIcon());
			pstmt.setString(3, lfDTO.getPlace());
			pstmt.setString(4, lfDTO.getContent());
			pstmt.setString(5, lfDTO.getAuthor());
			pstmt.setString(6, lfDTO.getNal());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// �۾���

	public ArrayList<BoardLfDTO> lfSearch(BoardLfDTO lfDTO, String choice) {// �˻�
		try {
			if (choice.equals("T")) {//���
				sql = "select no,icon,place,author,nal from boardlf where visible=1 and place like ? order by no desc";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + lfDTO.getPlace() + "%");
			} else if (choice.equals("C")) {//�ۼ���
				sql = "select no,icon,place,author,nal from boardlf where visible=1 and author like ? order by no desc";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + lfDTO.getAuthor() + "%");
			}
			rs = pstmt.executeQuery();
			lfList = new ArrayList<BoardLfDTO>();
			while (rs.next()) {
				lfDTO = new BoardLfDTO();
				lfDTO.setNo(rs.getInt("no"));
				lfDTO.setIcon(rs.getString("icon"));
				lfDTO.setPlace(rs.getString("place"));
				lfDTO.setAuthor(rs.getString("author"));
				lfDTO.setNal(rs.getString("nal"));
				lfList.add(lfDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lfList;
	}// �˻�

	public BoardLfDTO lfContent(BoardLfDTO lfDTO) {// ���뺸��
		sql = "select no,icon,place,content,author,nal from boardlf where no = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, lfDTO.getNo());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				lfDTO = new BoardLfDTO();
				lfDTO.setNo(rs.getInt("no"));
				lfDTO.setIcon(rs.getString("icon"));
				lfDTO.setPlace(rs.getString("place"));
				lfDTO.setContent(rs.getString("content"));
				lfDTO.setAuthor(rs.getString("author"));
				lfDTO.setNal(rs.getString("nal"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lfDTO;

	}// ���뺸��

	public void lfDelete(BoardLfDTO lfDTO) {// ����
		try {
			//sql = "delete from boardlf where no=?";
			sql="update boardlf set visible=0 where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, lfDTO.getNo());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// ����

	public void lfUpdate(BoardLfDTO lfDTO) {// �Խñۼ���
		try {
			sql = "update boardlf set content=?,icon=?,author=?,place=? where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, lfDTO.getContent());
			pstmt.setString(2, lfDTO.getIcon());
			pstmt.setString(3, lfDTO.getAuthor());
			pstmt.setString(4, lfDTO.getPlace());
			pstmt.setInt(5, lfDTO.getNo());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// �Խñۼ���
}
