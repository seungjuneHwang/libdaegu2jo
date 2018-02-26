package kr.co.daegu.borrow;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.daegu.book.BookDAO;
import kr.co.daegu.book.BookDTO;
import kr.co.daegu.member.MemberDAO;
import kr.co.daegu.member.MemberDTO;

@WebServlet("*.borrow")
public class BorrowFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public BorrowDTO borrowDTO;
	public BorrowDAO borrowDAO;
	public BookDAO bookDAO;
	public BookDTO bookDTO;
	public MemberDAO memberDAO;
	public MemberDTO memberDTO;
	private ArrayList<BorrowDTO> borrowList;

	public BorrowFrontController() {
		borrowDTO = new BorrowDTO();
		borrowDAO = new BorrowDAO();
		bookDTO = new BookDTO();
		bookDAO = new BookDAO();
		memberDTO = new MemberDTO();
		memberDAO = new MemberDAO();
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

		if (command.equals("/register.borrow")) {// ���� ���

			String registNum = request.getParameter("registernum");
			String id = request.getParameter("id");
			boolean check = bookDAO.borrowBookCheck(registNum);// å�� �ִ��� Ȯ��
			boolean bookstate = bookDAO.borrowBookState(registNum);// ���������� �ƴ��� Ȯ��

			if (check == true && bookstate == true) {// å�� �ְ�, ���Ⱑ�� �� ��
				borrowDTO.setRegistNum(registNum);
				borrowDTO.setId(id);
				borrowDAO.borrowInsert(borrowDTO); // ����������, id�� ��Ϲ�ȣ�� �ʿ���
				bookDAO.bookStateUpdate(registNum);// �������º���
				out.print("����Ϸ�");
				out.print("<a href='index.jsp'>��������</a>");

			} else if (check == false) {// ��Ϲ�ȣ�� ��ġ�� å�� ���� ��
				RequestDispatcher dis = request.getRequestDispatcher("borrow.jsp");
				memberDTO.setId(id);
				request.setAttribute("check", check);
				request.setAttribute("memberDTO", memberDTO);
				dis.forward(request, response);

			} else if (bookstate == false) {// ������ ���� ��
				RequestDispatcher dis = request.getRequestDispatcher("borrow.jsp");
				memberDTO.setId(id);
				request.setAttribute("bookstate", bookstate);
				request.setAttribute("memberDTO", memberDTO);
				dis.forward(request, response);
			}

		} // ���� ���

		if (command.equals("/retrun.borrow")) {// ���� �ݳ�
			String registNum = request.getParameter("registernum");
			boolean check = bookDAO.borrowBookCheck(registNum);// å�� �ִ��� Ȯ��
			boolean bookstate = bookDAO.borrowBookState(registNum);// ���������� �ƴ��� Ȯ��

			if (check == true && bookstate == false) { // ������ �����ϰ�, ���� ��
				borrowDTO.setRegistNum(registNum);
				borrowDAO.borrowRetrun(borrowDTO);// �����ݳ�
				bookDAO.bookStatePossibleUpdate(registNum);// ���� ���Ⱑ������ ���º���
				out.print("�ݳ��Ϸ�");
				out.print("<a href='index.jsp'>��������</a>");

			} else if (check == false) {// ��Ϲ�ȣ�� ��ġ�� å�� ���� ��
				RequestDispatcher dis = request.getRequestDispatcher("borrowRetrun.jsp");
				request.setAttribute("check", check);
				dis.forward(request, response);

			} else if (bookstate == true) {// ������ ���Ⱑ��
				RequestDispatcher dis = request.getRequestDispatcher("borrowRetrun.jsp");
				request.setAttribute("bookstate", bookstate);
				dis.forward(request, response);
			}

		} // ����ݳ�

		else if (command.equals("/list.borrow")) {// ȸ���� ���� ������Ȳ
			HttpSession session = request.getSession(false);
			String id = (String) session.getAttribute("id");
			borrowDTO.setId(id);
			borrowList = borrowDAO.borrowView(borrowDTO);
			RequestDispatcher dis = request.getRequestDispatcher("bookBorrow.jsp");
			request.setAttribute("borrowList", borrowList);
			dis.forward(request, response);
		} // ȸ���� ���� ������Ȳ

		else if (command.equals("/allList.borrow")) {// ȸ�� - ��ü������
			HttpSession session = request.getSession(false);
			String id = (String) session.getAttribute("id");
			borrowDTO.setId(id);
			borrowList = borrowDAO.borrowAllView(borrowDTO);
			RequestDispatcher dis = request.getRequestDispatcher("bookAllBorrow.jsp");
			request.setAttribute("borrowList", borrowList);
			dis.forward(request, response);
		} // ȸ��-��ü������

	}

}
