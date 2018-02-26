package kr.co.daegu.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.collections.SynchronizedStack;

@WebServlet("*.boa")
public class BoardFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardDTO boardDTO;
	private BoardDAO boardDAO;
	private ArrayList<BoardDTO> boardList;


	public BoardFrontController() {
		boardDTO = new BoardDTO();
		boardDAO = new BoardDAO();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());

		if(command.equals("/boardList.boa")) {//�Խ��� ��ü���

			boardList = boardDAO.boardList(boardDTO);
			RequestDispatcher dispatcher = request.getRequestDispatcher("board.jsp");
			request.setAttribute("boardList", boardList);
			//dispatcher.include(request, response);
			dispatcher.forward(request, response);
			
		}//�Խ��� ��ü���
		
		else if(command.equals("/boardWrite.boa")) {//�۾���
			//boardList = boardDAO.boardList(boardDTO);
			String title=request.getParameter("title");
			String author=request.getParameter("author");
			String content=request.getParameter("content");
			String nal=request.getParameter("nal");
			//String icon=request.getParameter("icon");
			
			boardDTO.setTitle(title);
			boardDTO.setAuthor(author);
			boardDTO.setContent(content);
			//boardDTO.setIcon(icon);
			boardDTO.setWritedate(nal);
			boardDTO.setReadcount(0);
			boardDAO.boardWrite(boardDTO);
			
			boardList = boardDAO.boardList(boardDTO);
			
			response.sendRedirect("boardList.boa");

		}//�۾���
		
		else if(command.equals("/boardSearch.boa")) {//�Խñ� �˻�
			
			String setop=request.getParameter("setop");
			String search=request.getParameter("search");
		
			if(setop.equals("all")) {//��ü
				boardDTO.setTitle(search);
				boardDTO.setAuthor(search);
			}
			else if(setop.equals("title")) {//����
				boardDTO.setTitle(search);
			}
			else if(setop.equals("author")) {//�ۼ���
				boardDTO.setAuthor(search);
			}
			boardList = boardDAO.boardSearch(boardDTO, setop);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("board.jsp");
			request.setAttribute("boardList", boardList);
			dispatcher.forward(request, response);
						
		}//�Խñ� �˻�
			
		else if(command.equals("/boardDelete.boa")) {//�Խñ� ����
			String no = request.getParameter("no");
			boardDTO.setNo(Integer.parseInt(no));
	
			boardDAO.boardDelete(boardDTO);

			response.sendRedirect("boardList.boa");		
			
		}//�Խñ� ����
		
		else if(command.equals("/boardUpdate.boa")) {//�Խñ� ����
			String no = request.getParameter("no");
			String title = request.getParameter("title");
			String author = request.getParameter("author");
			String content = request.getParameter("content");
			
			boardDTO.setNo(Integer.parseInt(no));
			boardDTO.setTitle(title);
			boardDTO.setAuthor(author);
			boardDTO.setContent(content);
			boardDAO.boardUpdate(boardDTO);
			
			response.sendRedirect("boardList.boa");	
			
		}//�Խñ� ����
		
		else if(command.equals("/boardContent.boa")) {//�Խñ� ���� ����
			
			String no = request.getParameter("no");
			boardDTO.setNo(Integer.parseInt(no));
			boardDTO = boardDAO.boardContent(boardDTO);
			boardDAO.boardReadCount(boardDTO);
			RequestDispatcher dispatcher = request.getRequestDispatcher("boardContent.jsp");
			request.setAttribute("boardDTO", boardDTO);
			dispatcher.forward(request, response);
			
		}//�Խñ� ���� ����
	}

}
