package kr.co.daegu.member;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("*.member")
/*@MultipartConfig(maxFileSize = 1024 * 1024 * 2, location = "C:\\Develop\\workspacejsp\\LibProject20180123\\WebContent\\images\\profile")*/
@MultipartConfig(maxFileSize = 1024 * 1024 * 2, location = "/home/hosting_users/kdh0115/tomcat/webapps/ROOT/images/profile")

public class MemberFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MemberDTO memberDTO;
	public MemberDAO memberDAO;
	private ArrayList<MemberDTO> memberList;

	public MemberFrontController() {
		memberDTO = new MemberDTO();
		memberDAO = new MemberDAO();
	}

	// ���ϸ� ���
	private String getFilename(Part part) {
		String fileName = null;
		String contentDispositionHeader = part.getHeader("content-disposition");
		String[] elements = contentDispositionHeader.split(";");
		for (String element : elements) {
			if (element.trim().startsWith("filename")) {
				fileName = element.substring(element.indexOf('=') + 1);
				fileName = fileName.trim().replace("\"", "");
			}
		}
		return fileName;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());

		if (command.equals("/register.member")) {// ȸ������
			String id = request.getParameter("id");
			String pw = request.getParameter("pw");
			String pwch = request.getParameter("pwch");
			String irum = request.getParameter("irum");
			String tel1 = request.getParameter("tel1");
			String tel2 = request.getParameter("tel2");
			String tel3 = request.getParameter("tel3");
			String tel = tel1.concat("-" + tel2 + "-" + tel3);
			String email1 = request.getParameter("email1");
			String email2 = request.getParameter("email2");
			String email = email1.concat("@" + email2);
			String birth1 = request.getParameter("birth1");
			String birth2 = request.getParameter("birth2");
			String birth3 = request.getParameter("birth3");
			String birth = birth1.concat("-" + birth2 + "-" + birth3);
			// String memImage = request.getParameter("icon");
			String emailcheck = request.getParameter("emailcheck");
			String smscheck = request.getParameter("smscheck");

			// ���� ���ε�
			Part part = request.getPart("icon");
			String fileName = getFilename(part);

			if (fileName != null && !fileName.isEmpty()) {
				// part.write("c:\\upload\\"+fileName);
				part.write(fileName);
			}

			memberDTO.setId(id);
			memberDTO.setPw(pw);
			memberDTO.setPwch(pwch);
			memberDTO.setIrum(irum);
			memberDTO.setTel(tel);
			memberDTO.setEmail(email);
			memberDTO.setBirth(birth);
			memberDTO.setMemImage(fileName);
			memberDTO.setEmailcheck(emailcheck);
			memberDTO.setSmscheck(smscheck);
			memberDAO.memberInsert(memberDTO);

			out.print("ȸ�������� �Ϸ�Ǿ����ϴ�.");
			out.print("<a href='index.jsp'>��������</a>");
		} // ȸ������
		else if (command.equals("/idcheck.member")) {// ���̵��ߺ�
			String id = request.getParameter("id");

			memberDTO.setId(id);
			boolean idcheck = memberDAO.memberIdCheck(id);
			if(idcheck==true) {
				//out.print("���̵� �̹� ������Դϴ�.");
				out.print("NO");
			}else {
//				out.print("���̵� ��밡���մϴ�.");	
				out.print("OK");	
			}
			//out.print("<input type='button' value='����' onClick='self.close()'>");
		}//���̵��ߺ�
		else if(command.equals("/login.member")) {//�α���
			HttpSession session = request.getSession();
			String id = request.getParameter("id");
			String pw = request.getParameter("pw");
			memberDTO.setId(id);
			memberDTO.setPw(pw);
			boolean loginIdCheck = memberDAO.memberLoginIdCheck(memberDTO);// �α��� �ϴ� ���̵� �����ϴ°�
			boolean loginDeleteCheck = memberDAO.memberDeleteIdCheck(memberDTO);// ���̵� Ż���� ���̵�����
			boolean login = memberDAO.memberlogin(memberDTO);// ���̵�, ��й�ȣ�� ��ġ�Ҷ� �α���

			if(id.equals("admin") && login) {
				memberDTO=memberDAO.adminNameGet(memberDTO);
				session.setAttribute("irum", memberDTO.getIrum());
			}
			
			if (login == true) {
				session.setAttribute("id", id);
				response.sendRedirect("memberProfile.member");
			} else {
				RequestDispatcher dis = request.getRequestDispatcher("login.jsp");

				if (loginDeleteCheck == true) {
					request.setAttribute("loginDeleteCheck", loginDeleteCheck);					
				}
				else if (loginIdCheck == false) {
					request.setAttribute("loginIdCheck", loginIdCheck);
				} else if (login == false) {
					request.setAttribute("login", login);
				}
				dis.forward(request, response);
			}
		} // �α���
		else if (command.equals("/logout.member")) {// �α׾ƿ�
			HttpSession session = request.getSession(false);
			session.removeAttribute("id");
			response.sendRedirect("index.jsp");
		} // �α׾ƿ�
		else if (command.equals("/pw_select.member")) {// ���ã��
			String id = request.getParameter("id");
			String birth1 = request.getParameter("birth1");
			String birth2 = request.getParameter("birth2");
			String birth3 = request.getParameter("birth3");
			String birth = birth1.concat("-" + birth2 + "-" + birth3);
			memberDTO.setId(id);
			memberDTO.setBirth(birth);
			String pw = memberDAO.memberPwSelect(memberDTO);
			if (pw == null) {
				out.print("��й�ȣ�� ã�� ���Ͽ����ϴ�.");
			} else {
				out.print("ã���ô� ��й�ȣ��=" + pw);
			}
			out.print("<input type='button' value='����' onclick='self.close()'>");
		} // ���ã��
		else if (command.equals("/view.member")) {// �������

			String id = request.getParameter("id");
			String pw = request.getParameter("pw");
			String pwch = request.getParameter("pwch");
			String irum = request.getParameter("irum");
			String tel = request.getParameter("tel");
			String email = request.getParameter("email");
			// String memImage = request.getParameter("icon");
			String emailcheck = request.getParameter("emailcheck");
			String smscheck = request.getParameter("smscheck");

			// ���� ���ε�
			Part part = request.getPart("icon");
			String fileName = getFilename(part);

			memberDTO.setId(id);
			memberDTO.setPw(pw);
			memberDTO.setPwch(pwch);
			memberDTO.setIrum(irum);
			memberDTO.setTel(tel);
			memberDTO.setEmail(email);
			memberDTO.setMemImage(fileName);
			memberDTO.setEmailcheck(emailcheck);
			memberDTO.setSmscheck(smscheck);

			memberDAO.memberUpdate(memberDTO);

			if (fileName != null && !fileName.isEmpty()) {
				// part.write("c:\\upload\\"+fileName);
				part.write(fileName);
			}

			out.print("ȸ�������� �����Ǿ����ϴ�.");
			out.print("<a href='index.jsp'>��������</a>");
		} // �������

		else if (command.equals("/updateconfirm.member")) {// ���������Ʈ
			HttpSession session = request.getSession(false);
			String id = (String) session.getAttribute("id");
			memberDTO.setId(id);
			memberDTO = memberDAO.memberUpdateView(memberDTO);
			RequestDispatcher dis = request.getRequestDispatcher("memberChange.jsp");
			String str = memberDTO.getBirth();

			request.setAttribute("memberDTO", memberDTO);
			dis.forward(request, response);
		} // ���������Ʈ

		else if (command.equals("/list.member")) {// �������Ʈ
			memberList = memberDAO.memberList(memberDTO);
			RequestDispatcher dis = request.getRequestDispatcher("memberlist.jsp");
			request.setAttribute("memberList", memberList);
			dis.forward(request, response);
		} // �������Ʈ

		else if (command.equals("/leave.member")) {// ȸ��Ż��
			HttpSession session = request.getSession(false);
			String id = (String) session.getAttribute("id");
			memberDTO.setId(id);
			memberDAO.memberLeave(memberDTO);
			session.removeAttribute("id");
			out.print("ȸ��Ż�� ����!");
			out.print("<a href='index.jsp'>��������</a>");
		} // ȸ��Ż��

		else if (command.equals("/borrowid.member")) {// id
			String id = request.getParameter("id");
			memberDTO.setId(id);
			boolean idcheck = memberDAO.memberIdview(memberDTO);

			if (idcheck == true) {
				RequestDispatcher dis = request.getRequestDispatcher("borrow.jsp");
				request.setAttribute("memberDTO", memberDTO);
				dis.forward(request, response);
			} else {
				RequestDispatcher dis = request.getRequestDispatcher("borrowId.jsp");
				request.setAttribute("idcheck", idcheck);
				dis.forward(request, response);
			}

		} // id

		else if (command.equals("/memberProfile.member")) {

			HttpSession session = request.getSession(false);
			String id = (String) session.getAttribute("id");
			memberDTO.setId(id);
			memberDTO = memberDAO.memberProfile(memberDTO);

			RequestDispatcher dis = request.getRequestDispatcher("index.jsp");
			request.setAttribute("memberDTO", memberDTO);
			dis.forward(request, response);

		}

	}
}
