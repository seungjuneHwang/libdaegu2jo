package kr.co.daegu.boardnotice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class BoardNoticeDAO {
	public DataSource dataFactory;
	private String sql;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private ArrayList<BoardNoticeDTO> noticeList;
	private int cnt;

	public BoardNoticeDAO() {
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
	
	public ArrayList<BoardNoticeDTO> noticeList(BoardNoticeDTO noticeDTO){//��ü���
		try {
			sql = "select no,title,nal,author,readcount from boardnotice where visible=1 order by no desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			noticeList = new ArrayList<BoardNoticeDTO>();
			while(rs.next()) {
				noticeDTO = new BoardNoticeDTO();
				noticeDTO.setNo(rs.getInt("no"));
				noticeDTO.setTitle(rs.getString("title"));
				noticeDTO.setNal(rs.getString("nal"));
				noticeDTO.setAuthor(rs.getString("author"));
				noticeDTO.setReadcount(rs.getInt("readcount"));
				noticeList.add(noticeDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return noticeList;
	}//��ü���
	
	public void noticeReadCount(BoardNoticeDTO noticeDTO) {//��ȸ������
		try {
			sql = "update boardnotice set readcount=? where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeDTO.getReadcount()+1);
			pstmt.setInt(2, noticeDTO.getNo());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//��ȸ������
	
	public int totalCount(){//����¡ó��: ��ü���ڵ� ���� ���ϱ�
	     int count=0;
	     try {
	      sql = "select count(*) from boardnotice where visible=1";
	      pstmt = conn.prepareStatement(sql);
	      rs = pstmt.executeQuery();
	      if(rs.next()){
	       count = rs.getInt(1);
	      }
	  } catch (SQLException e) {
	   e.printStackTrace();
	  }   
	     return count;
	    }//����¡ó��: ��ü���ڵ� ���� ���ϱ�
	
	public PageTo page(int curPage){//����������
	     PageTo pageTo = new PageTo();
	     int totalCount = totalCount();
	     ArrayList<BoardNoticeDTO> list = new ArrayList<BoardNoticeDTO>();
	     try {
	      sql = "select no,title,author,content,nal,readcount from boardnotice where visible=1 order by no desc";
	      pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	      //TYPE_SCROLL_INSENSITIVE:scroll�� �����ϳ�, ����� ������ ������� ����
	      //�����, ��ũ�� �� ������Ʈ �ݿ�����
	      //CONCUR_READ_ONLY :Ŀ���� ��ġ���� ���� ������Ʈ �Ұ�,ResultSet�� ������ �Ұ���
	      rs = pstmt.executeQuery();
	      int perPage = pageTo.getPerPage();//5
	      int skip = (curPage-1) * perPage;
	      if(skip>0){
	       rs.absolute(skip);
	      }
	      //ResultSet�� absolute�޼ҵ带 �̿��Ͽ� �ش� �������� Cursor �� ��ġ�� �̵�...
	      for(int i=0;i<perPage && rs.next();i++){
	       int no = rs.getInt("no");
	       String author = rs.getString("author");
	       String title = rs.getString("title");
	       String content = rs.getString("content");
	       int readCount = rs.getInt("readcount");
	       String nal = rs.getString("nal");
	       
	       BoardNoticeDTO data = new BoardNoticeDTO();
	       data.setNo(no);
	       data.setAuthor(author);
	       data.setTitle(title);
	       data.setContent(content);
	       data.setReadcount(readCount);
	       data.setNal(nal);
	       list.add(data);     
	      }
	      pageTo.setList(list);//ArrayList ����
	      pageTo.setTotalCount(totalCount);//��ü���ڵ尳��
	      pageTo.setCurPage(curPage);//����������
	  } catch (SQLException e) {
	   e.printStackTrace();
	  }
	     return pageTo;      
	    }//����������
	
	public void noticeSqlInsert() {
		sql = "select max(no) as no from boardnotice";
	}
	
	public void noticeBunho() {
		try {
			noticeSqlInsert();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
			while(rs.next()) {
				cnt = rs.getInt("no");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void noticeWrite(BoardNoticeDTO noticeDTO) {//�۾���
		noticeBunho();
		noticeDTO.setNo(cnt+1);
		sql = "insert into boardnotice(no,title,content,author,nal,readcount) values(?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeDTO.getNo());
			pstmt.setString(2, noticeDTO.getTitle());
			pstmt.setString(3, noticeDTO.getContent());
			pstmt.setString(4, noticeDTO.getAuthor());
			pstmt.setString(5, noticeDTO.getNal());
			pstmt.setInt(6, noticeDTO.getReadcount());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//�۾���
	
	public ArrayList<BoardNoticeDTO> noticeSearch(BoardNoticeDTO noticeDTO,String choice){//�˻�
		try {
			if(choice.equals("A")) {
				sql = "select no,title,author,nal,readcount from boardnotice where visible=1 and (title like ? or author like ?) order by no";			
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+noticeDTO.getTitle()+"%");
				pstmt.setString(2, "%"+noticeDTO.getAuthor()+"%");
			}
			else if(choice.equals("T")) {
				sql = "select no,title,author,nal,readcount from boardnotice where visible=1 and title like ? order by no";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+noticeDTO.getTitle()+"%");
			}
			else if(choice.equals("C")) {
				sql = "select no,title,author,nal,readcount from boardnotice where visible=1 and author like ? order by no";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%"+noticeDTO.getAuthor()+"%");
			}
			rs = pstmt.executeQuery();
			noticeList = new ArrayList<BoardNoticeDTO>();
			while(rs.next()) {
				noticeDTO = new BoardNoticeDTO();
				noticeDTO.setNo(rs.getInt("no"));
				noticeDTO.setTitle(rs.getString("title"));
				noticeDTO.setAuthor(rs.getString("author"));
				noticeDTO.setNal(rs.getString("nal"));
				noticeDTO.setReadcount(rs.getInt("readcount"));
				noticeList.add(noticeDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return noticeList;
	}//�˻�
	public BoardNoticeDTO noticeContent(BoardNoticeDTO noticeDTO) {//���뺸��
		sql = "select no,title,content,author,nal,readcount from boardnotice where no = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeDTO.getNo());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				noticeDTO = new BoardNoticeDTO();
				noticeDTO.setNo(rs.getInt("no"));
				noticeDTO.setTitle(rs.getString("title"));
				noticeDTO.setContent(rs.getString("content"));
				noticeDTO.setAuthor(rs.getString("author"));
				noticeDTO.setNal(rs.getString("nal"));
				noticeDTO.setReadcount(rs.getInt("readcount"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return noticeDTO;
	
	}//���뺸��
	
	public void noticeDelete(BoardNoticeDTO noticeDTO) {//����
		try {
			sql = "update boardnotice set visible=1 where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeDTO.getNo());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//����
	
	public void noticeUpdate(BoardNoticeDTO noticeDTO) {//�Խñۼ���
		try {
			sql = "update boardnotice set content=?,title=?,author=? where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, noticeDTO.getContent());
			pstmt.setString(2, noticeDTO.getTitle());
			pstmt.setString(3, noticeDTO.getAuthor());
			pstmt.setInt(4, noticeDTO.getNo());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}//�Խñۼ���
}
