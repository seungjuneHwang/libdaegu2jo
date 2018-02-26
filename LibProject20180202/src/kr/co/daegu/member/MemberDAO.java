package kr.co.daegu.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MemberDAO {
	public DataSource dataFactory;
	private String sql;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private int cnt;
	private ArrayList<MemberDTO> memberList;

	public MemberDAO() {
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

	public void memberInsert(MemberDTO memberDTO) {// ȸ������
		try {
			sql = "insert into bookmember(id,pw,pwch,irum,tel,email,birth,memImage,emailcheck,smscheck) values(?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.setString(2, memberDTO.getPw());
			pstmt.setString(3, memberDTO.getPwch());
			pstmt.setString(4, memberDTO.getIrum());
			pstmt.setString(5, memberDTO.getTel());
			pstmt.setString(6, memberDTO.getEmail());
			pstmt.setString(7, memberDTO.getBirth());
			pstmt.setString(8, memberDTO.getMemImage());
			pstmt.setString(9, memberDTO.getEmailcheck());
			pstmt.setString(10, memberDTO.getSmscheck());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// ȸ������

public boolean memberIdCheck(String id) {//id�ߺ�üũ
	boolean idcheck=false;
	sql = "select id from bookmember where id=?";	
	try {
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			String getId = rs.getString("id");
			if (getId.equals(id)) {
				idcheck=true;
			}
		}
		//idcheck = rs.next();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return idcheck;
}//id�ߺ�üũ

	public boolean memberLoginIdCheck(MemberDTO memberDTO) {// �α��� �� ��, ��ġ�ϴ� ���̵� ������ Ȯ��
		boolean loginIdCheck = false;
		sql = "select id from bookmember where id=? and visible=1";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loginIdCheck = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loginIdCheck;
	}// �α����� ��,
	
	
	public boolean memberDeleteIdCheck(MemberDTO memberDTO) {// Ż���� ���̵� ���� �ƴ���
		boolean loginDeleteCheck = false;
		sql = "select id from bookmember where id=? and visible=0";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				loginDeleteCheck = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loginDeleteCheck;
	}// Ż���� ���̵� ���� �ƴ���
	

	public boolean memberlogin(MemberDTO memberDTO) {// �α���
		boolean login = false;
		sql = "select id,pw from bookmember where id=? and pw=? and visible=1";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.setString(2, memberDTO.getPw());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				login = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return login;
	}// �α���
	
	public MemberDTO adminNameGet(MemberDTO memberDTO) {//������ �̸� ��������
		
		sql="select irum from bookmember where id=? and visible=1";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			rs=pstmt.executeQuery();
			while(rs.next()) {
				memberDTO.setIrum(rs.getString("irum"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberDTO;
		
	}//������ �̸� ��������
	

	public String memberPwSelect(MemberDTO memberDTO) {// ���ã��
		String pw = null;
		sql = "select pw from bookmember where id=? and birth=? and visible=1";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.setString(2, memberDTO.getBirth());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				pw = rs.getString("pw");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pw;
	}// ���ã��

	public ArrayList<MemberDTO> memberList(MemberDTO memberDTO) {// ȸ����ü���
		sql = "select id,irum,birth from bookmember where visible=1";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			memberList = new ArrayList<MemberDTO>();
			while (rs.next()) {
				memberDTO = new MemberDTO();
				String id = rs.getString("id");
				String irum = rs.getString("irum");
				String birth = rs.getString("birth");
				memberDTO.setId(id);
				memberDTO.setIrum(irum);
				memberDTO.setBirth(birth);
				memberList.add(memberDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberList;

	}// ȸ����ü���

	public MemberDTO memberUpdateView(MemberDTO memberDTO) {// �����

		sql = "select id,pw,pwch,irum,tel,email,birth,memImage,emailcheck,smscheck from bookmember where id=? and visible=1";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				String pw = rs.getString("pw");
				String pwch = rs.getString("pwch");
				String irum = rs.getString("irum");
				String tel = rs.getString("tel");
				String email = rs.getString("email");
				String birth = rs.getString("birth");
				String memImage = rs.getString("memImage");
				String emailcheck = rs.getString("emailcheck");
				String smscheck = rs.getString("smscheck");

				memberDTO.setId(id);
				memberDTO.setPw(pw);
				memberDTO.setPwch(pwch);
				memberDTO.setIrum(irum);
				memberDTO.setTel(tel);
				memberDTO.setEmail(email);
				memberDTO.setBirth(birth);
				memberDTO.setMemImage(memImage);
				memberDTO.setEmailcheck(emailcheck);
				memberDTO.setSmscheck(smscheck);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberDTO;
	}// �����

	public void memberUpdate(MemberDTO memberDTO) {// �������

		try {

			if (memberDTO.getMemImage().equals("")) {

				sql = "update bookmember set pw=?,pwch=?,irum=?,tel=?,email=?,emailcheck=?,smscheck=? where id=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, memberDTO.getPw());
				pstmt.setString(2, memberDTO.getPwch());
				pstmt.setString(3, memberDTO.getIrum());
				pstmt.setString(4, memberDTO.getTel());
				pstmt.setString(5, memberDTO.getEmail());
				pstmt.setString(6, memberDTO.getEmailcheck());
				pstmt.setString(7, memberDTO.getSmscheck());
				pstmt.setString(8, memberDTO.getId());
			} else {
				sql = "update bookmember set pw=?,pwch=?,irum=?,tel=?,email=?,memImage=?,emailcheck=?,smscheck=? where id=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, memberDTO.getPw());
				pstmt.setString(2, memberDTO.getPwch());
				pstmt.setString(3, memberDTO.getIrum());
				pstmt.setString(4, memberDTO.getTel());
				pstmt.setString(5, memberDTO.getEmail());
				pstmt.setString(6, memberDTO.getMemImage());
				pstmt.setString(7, memberDTO.getEmailcheck());
				pstmt.setString(8, memberDTO.getSmscheck());
				pstmt.setString(9, memberDTO.getId());
			}
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}// �������

	public void memberLeave(MemberDTO memberDTO) {// ���Ż��
		sql = "update bookmember set visible=0 where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			cnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean memberIdview(MemberDTO memberDTO) {// ���̵� ��ġ�ϴ��� �ƴ���
		boolean idcheck = false;

		try {
			sql = "select id from bookmember where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				idcheck = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idcheck;
	}// ���̵� ��ġ�ϴ��� �ƴ���

	public MemberDTO memberProfile(MemberDTO memberDTO) {// ȸ�� ������ ����

		sql = "select memImage from bookmember where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				memberDTO.setMemImage(rs.getString("memImage"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberDTO;

	}// ȸ�� ������ ����
}
