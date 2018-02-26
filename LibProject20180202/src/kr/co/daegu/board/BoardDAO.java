package kr.co.daegu.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class BoardDAO {
	
	public DataSource dataFactory;
	private Connection conn;
	private String sql;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private ArrayList<BoardDTO> boardList;
	private int cnt;
	
	public BoardDAO() {//������ ����

			try {
				Context ctx = new InitialContext();
				dataFactory = (DataSource)ctx.lookup("java:comp/env/jdbc/Oracle11g");
				/*dataFactory = (DataSource)ctx.lookup("java:comp/env/jdbc/kdh0115");*/
				conn=dataFactory.getConnection();
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}//������ ��
	
	public void boardClose() {
		try {
			conn.close();
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<BoardDTO> boardList(BoardDTO boardDTO) {//info�� �Խ��� ��ü���
		try {

			sql="select no,title,author,writedate,readcount from board order by no desc";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			boardList = new ArrayList<BoardDTO>();
			while(rs.next()) {
				boardDTO = new BoardDTO();
				
				boardDTO.setNo(rs.getInt("no"));
				boardDTO.setTitle(rs.getString("title"));
				boardDTO.setAuthor(rs.getString("author"));
				boardDTO.setWritedate(rs.getString("writedate"));
				boardDTO.setReadcount(rs.getInt("readcount"));
				
				boardList.add(boardDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return boardList;
	}//info�� �Խ��� ��ü���
	
	public void boardSqlInsert() {
		sql = "select max(no) as no from board";
	}
	
	public void boardBunho() {//DB �Խñ� ��ȣ
		try {
			boardSqlInsert();
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()) 
				cnt=rs.getInt("no");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//DB �Խñ� ��ȣ
	
	public void boardWrite(BoardDTO boardDTO) {//�۾���
		
		try {
			boardBunho();
			boardDTO.setNo(cnt+1);
			sql="insert into board(no,title,content,author,writedate,readcount) values(?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardDTO.getNo());
			pstmt.setString(2, boardDTO.getTitle());
			pstmt.setString(3, boardDTO.getContent());
			pstmt.setString(4, boardDTO.getAuthor());
			pstmt.setString(5, boardDTO.getWritedate());
			pstmt.setInt(6, boardDTO.getReadcount());
			cnt=pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//�۾���
	
	public ArrayList<BoardDTO> boardSearch(BoardDTO boardDTO, String setop) {//�Խñ� �˻�

	try {
			if(setop.equals("all")) {
				sql="select no,title,author,writedate,readcount from board where title like ? or author like ? order by no desc";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, "%"+boardDTO.getTitle()+"%");
				pstmt.setString(2, "%"+boardDTO.getAuthor()+"%");
			}
			else if(setop.equals("title")) {
				sql="select no,title,author,writedate,readcount from board where title like ? order by no desc";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, "%"+boardDTO.getTitle()+"%");
			} 
			else if(setop.equals("author")) {
				sql="select no,title,author,writedate,readcount from board where author like ? order by no desc";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, "%"+boardDTO.getAuthor()+"%");
			}

			rs=pstmt.executeQuery();
			
			boardList = new ArrayList<BoardDTO>();

			while(rs.next()) {
				boardDTO = new BoardDTO();
				
				boardDTO.setNo(rs.getInt("no"));
				boardDTO.setTitle(rs.getString("title"));
				boardDTO.setAuthor(rs.getString("author"));
				boardDTO.setWritedate(rs.getString("writedate"));
				boardDTO.setReadcount(rs.getInt("readcount"));
				
				boardList.add(boardDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return boardList;
	}//�Խñ� �˻�
	
	public BoardDTO boardContent(BoardDTO boardDTO){// �Խñ� ���뺸��
		try {
			sql="select no,title,content,author,writedate,readcount from board where no=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, boardDTO.getNo());
			rs=pstmt.executeQuery();
			boardList = new ArrayList<BoardDTO>();
			while(rs.next()) {
				boardDTO = new BoardDTO();
				
				boardDTO.setNo(rs.getInt("no"));
				boardDTO.setTitle(rs.getString("title"));
				boardDTO.setContent(rs.getString("content"));
				boardDTO.setAuthor(rs.getString("author"));
				boardDTO.setWritedate(rs.getString("writedate"));
				boardDTO.setReadcount(rs.getInt("readcount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return boardDTO;
	}// �Խñ� ���뺸��
	
	public void boardDelete(BoardDTO boardDTO) {//�Խñ� ����
		
		try {
			sql="delete from board where no=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, boardDTO.getNo());
			cnt=pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//�Խñ� ����
	
	public void boardUpdate(BoardDTO boardDTO) {//�Խñ� ����
		
		try {
			sql="update board set title=?, content=?, author=?, writedate=? where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardDTO.getTitle());
			pstmt.setString(2, boardDTO.getContent());
			pstmt.setString(3, boardDTO.getAuthor());
			pstmt.setInt(4, boardDTO.getNo());
			cnt=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}//�Խñ� ����
	
	public void boardReadCount(BoardDTO boardDTO) {//��ȸ��
		
		try {
			sql="update board set readcount=? where no=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardDTO.getReadcount()+1);
			pstmt.setInt(2, boardDTO.getNo());
			cnt=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}//��ȸ��
	
}
