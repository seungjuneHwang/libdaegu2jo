package kr.co.daegu.boardnotice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.notice")
public class BoardNoticeFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardNoticeDAO noticeDAO;
	private BoardNoticeDTO noticeDTO;
	private ArrayList<BoardNoticeDTO> noticeList;

	public BoardNoticeFrontController() {
		noticeDAO = new BoardNoticeDAO();
		noticeDTO = new BoardNoticeDTO();
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

		if (command.equals("/noticeList.notice")) {// ��ü���
			int curPage = 1;
			if (request.getParameter("curPage") != null) {
				curPage = Integer.parseInt(request.getParameter("curPage"));
			}
			PageTo list = noticeDAO.page(curPage);
			RequestDispatcher dis = request.getRequestDispatcher("boardNotice.jsp");
			request.setAttribute("page", list);
			request.setAttribute("list", list.getList());
			dis.forward(request, response);
		} // ��ü���

		else if (command.equals("/noticeWrite.notice")) {// �۾���
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String author = request.getParameter("author");
			String nal = request.getParameter("nal");
			noticeDTO.setTitle(title);
			noticeDTO.setContent(content);
			noticeDTO.setAuthor(author);
			noticeDTO.setNal(nal);
			noticeDTO.setReadcount(0);
			noticeDAO.noticeWrite(noticeDTO);
			noticeList = noticeDAO.noticeList(noticeDTO);
			response.sendRedirect("noticeList.notice");
		} // �۾���

		else if (command.equals("/noticeSearch.notice")) {// �۰˻�

			int curPage = 1;
			if (request.getParameter("curPage") != null) {
				curPage = Integer.parseInt(request.getParameter("curPage"));
			}
			PageTo list = noticeDAO.page(curPage);
			String choice = request.getParameter("choice");
			String search = request.getParameter("search");
			String option = null;
			if (choice.equals("A")) {//��ü
				noticeDTO.setTitle(search);
				noticeDTO.setAuthor(search);
				option = "����+�ۼ���";
			} else if (choice.equals("T")) {//����
				noticeDTO.setTitle(search);
				option = "����";
			} else if (choice.equals("C")) {//�ۼ���
				noticeDTO.setAuthor(search);
				option = "�ۼ���";
			}
			noticeList = noticeDAO.noticeSearch(noticeDTO, choice);
			RequestDispatcher dis = request.getRequestDispatcher("boardNotice.jsp");
			request.setAttribute("list", noticeList);
			request.setAttribute("page", list);

			dis.forward(request, response);
		} // �۰˻�
		
		else if (command.equals("/noticeDelete.notice")) {// �� ����
			String no = request.getParameter("no");
			noticeDTO.setNo(Integer.parseInt(no));
			noticeDAO.noticeDelete(noticeDTO);
			response.sendRedirect("noticeList.notice");
		} // �� ����

		else if (command.equals("/noticeUpdate.notice")) {// �Խñ� ����
			String no = request.getParameter("no");
			String title = request.getParameter("title");
			String author = request.getParameter("author");
			String content = request.getParameter("content");
			noticeDTO.setNo(Integer.parseInt(no));
			noticeDTO.setTitle(title);
			noticeDTO.setAuthor(author);
			noticeDTO.setContent(content);
			noticeDAO.noticeUpdate(noticeDTO);
			response.sendRedirect("noticeList.notice");
		} // �Խñۼ���
		
		else if (command.equals("/noticeContent.notice")) {// �Խñ� ����
			String no = request.getParameter("no");
			noticeDTO.setNo(Integer.parseInt(no));
			noticeDTO = noticeDAO.noticeContent(noticeDTO);
			noticeDAO.noticeReadCount(noticeDTO);
			RequestDispatcher dis = request.getRequestDispatcher("noticeContent.jsp");
			request.setAttribute("noticeDTO", noticeDTO);
			dis.forward(request, response);
		} // �Խñ� ����
	}

}
