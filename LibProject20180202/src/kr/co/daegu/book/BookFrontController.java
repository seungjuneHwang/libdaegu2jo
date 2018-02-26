package kr.co.daegu.book;

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
import javax.servlet.http.Part;


@WebServlet("*.book")
/*@MultipartConfig(maxFileSize=1024*1024*2,location="C:\\Develop\\workspacejsp\\LibProject20180123\\WebContent\\images\\book")*/
@MultipartConfig(maxFileSize = 1024 * 1024 * 2, location = "/home/hosting_users/kdh0115/tomcat/webapps/ROOT/images/book")
public class BookFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public BookDTO bookDTO;
	public BookDAO bookDAO;
	private int cnt;
	private ArrayList<BookDTO> bookList;
	
    public BookFrontController() {
//    	bookDTO = new BookDTO();
//    	bookDAO = new BookDAO();
    }

  //���ϸ� ���
  	private String getFilename(Part part) {
  		String fileName = null;
  		String contentDispositionHeader=part.getHeader("content-disposition");
  		String[] elements = contentDispositionHeader.split(";");
  		for(String element:elements) {
  			if(element.trim().startsWith("filename")) {
  				fileName = element.substring(element.indexOf('=')+1);
  				fileName = fileName.trim().replace("\"", "");
  			}
  		}
  		return fileName;
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


		if(command.equals("/bookRegister.book")) {//�������
			
			String title = request.getParameter("title");
			String sutitle = request.getParameter("sutitle");
			String author = request.getParameter("author");
			String publisher = request.getParameter("publisher");
			String publishing = request.getParameter("publishing");
			String bookPage = request.getParameter("bookPage");
			String bookInf = request.getParameter("bookInf");
			String authorInf = request.getParameter("authorInf");
			String tableOfCon = request.getParameter("tableOfCon");
			String tableOfConTwo = request.getParameter("tableOfConTwo");
			String inBook = request.getParameter("inBook");
			String publishReview = request.getParameter("publishReview");
			
			//���� ���ε�
			Part part = request.getPart("icon");
			String fileName = getFilename(part);
		
			bookDTO.setTitle(title);
			bookDTO.setSutitle(sutitle);
			bookDTO.setAuthor(author);
			bookDTO.setPublisher(publisher);
			bookDTO.setPublishing(publishing);
			bookDTO.setBookPage(Integer.parseInt(bookPage));
			bookDTO.setBookInf(bookInf);
			bookDTO.setAuthorInf(authorInf);
			bookDTO.setTableOfCon(tableOfCon);
			bookDTO.setTableOfConTwo(tableOfConTwo);
			bookDTO.setInBook(inBook);
			bookDTO.setPublishReview(publishReview);
			bookDTO.setIcon(fileName);
			cnt=bookDAO.bookInsert(bookDTO);
			
			if(fileName!=null&&!fileName.isEmpty()) {
				//part.write("c:\\upload\\"+fileName);
				part.write(fileName);
			}

			out.print("<a href='index.jsp'><img src='images/header_logo.gif' height='50px'></a><br>");
			if(cnt==1) {
				out.print(cnt+"���� ������ ��ϵǾ����ϴ�.<br>");
			}else {
				out.print(cnt+"���� ������ ��ϵǾ����ϴ�.<br>������ �߻��߽��ϴ�.<br>�ٽ� �õ� ���ּ���.<br>���� �߻��ϴ� ����: �ʹ� ���� ������ ����<br>");
			}
			out.print("<a href='bookManage.jsp'>��������</a>");
		}//�������
		
		else if(command.equals("/bookDelete.book")) {//��������
			String title  = request.getParameter("title");
			String author  = request.getParameter("author");
			bookDTO.setTitle(title);
			bookDTO.setAuthor(author);
			cnt=bookDAO.bookDelete(bookDTO);
			out.print("<a href='index.jsp'><img src='images/header_logo.gif' height='50px'></a><br>");
			if(cnt>0) {
				out.print(cnt+"���� ������ �����Ǿ����ϴ�.<br>");
			}else {
				out.print("��ġ�ϴ� ������ �����ϴ�.<br>");
			}
			out.print("<a href='bookManage.jsp'>��������</a>");
		}//��������		
		
		else if(command.equals("/bookUpdateConfirm.book")) {//�������� - DB�� ����Ǿ� �ִ� ���� ���� ��������
			//��������
			String title=request.getParameter("title");
			String author=request.getParameter("author");
			bookDTO.setTitle(title);
			bookDTO.setAuthor(author);
			bookDTO=bookDAO.bookConfirm(bookDTO);
			
			if(bookDTO.getRegistNum()==null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("bookUpdate.jsp");
				request.setAttribute("bookcheck", false);
				dispatcher.forward(request, response);
				
			}else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("bookUpdateForm.jsp");
				request.setAttribute("bookDTO", bookDTO);
				dispatcher.forward(request, response);
			}
		}//�������� - DB�� ����Ǿ� �ִ� ���� ���� ��������
		
		
		if(command.equals("/bookBest.book")) { //�α⵵��
			
			String title = request.getParameter("title");
			bookDTO = new BookDTO();
			bookDTO.setTitle(title);
			System.out.println(title);
			bookDTO=bookDAO.bookBest(bookDTO);
			RequestDispatcher dispatcher = request.getRequestDispatcher("bookInformation.jsp");
			request.setAttribute("bookDTO", bookDTO);
			dispatcher.forward(request, response);
		
		}//�α⵵��
		
		else if(command.equals("/bookUpdate.book")) {//��������
			//��������
			String title = request.getParameter("title"); 
			String sutitle = request.getParameter("sutitle");
			String author = request.getParameter("author");
			String publisher = request.getParameter("publisher");
			String publishing  = request.getParameter("publishing");
			String bookPage = request.getParameter("bookPage");
			String bookInf = request.getParameter("bookInf");
			String authorInf = request.getParameter("authorInf");
			String tableOfCon = request.getParameter("tableOfCon");
			String tableOfConTwo = request.getParameter("tableOfConTwo");
			String inBook = request.getParameter("inBook");
			String publishReview = request.getParameter("publishReview");
			String registNum = request.getParameter("registNum");
			
			//���� ���ε�
			Part part = request.getPart("icon");
			String fileName = getFilename(part);
			
			bookDTO.setTitle(title);
			bookDTO.setSutitle(sutitle);
			bookDTO.setAuthor(author);
			bookDTO.setIcon(fileName);
			bookDTO.setPublishing(publishing);
			bookDTO.setPublisher(publisher);
			bookDTO.setBookPage(Integer.parseInt(bookPage));
			bookDTO.setBookInf(bookInf);
			bookDTO.setAuthorInf(authorInf);
			bookDTO.setTableOfCon(tableOfCon);
			bookDTO.setTableOfConTwo(tableOfConTwo);
			bookDTO.setInBook(inBook);
			bookDTO.setPublishReview(publishReview);
			bookDTO.setRegistNum(registNum);
			
			cnt=bookDAO.bookUpdate(bookDTO);

			if(fileName!=null&&!fileName.isEmpty()) {
				//part.write("c:\\upload\\"+fileName);
				part.write(fileName);
			}
			
			out.print("<a href='index.jsp'><img src='images/header_logo.gif' height='50px'></a><br>");
			if(cnt==1) {
				out.print(cnt+"���� ������ �����Ǿ����ϴ�.<br>");
			}else {
				out.print(cnt+"���� ������ �����Ǿ����ϴ�.<br>������ �߻��߽��ϴ�.<br>�ٽ� �õ� ���ּ���.<br>���� �߻��ϴ� ����: �ʹ� ���� ������ ����<br>");
			}
			out.print("<a href='bookManage.jsp'>��������</a>");

		}//��������


		else if(command.equals("/bookSearch.book")) {//�����˻�

			String search = request.getParameter("content");
			search = search.replaceAll(" ", "+");
			System.out.println("=========================");
			System.out.println(search);
			System.out.println("=========================");
//			String setop=request.getParameter("");
//			//�˻� �ɼ� ����
//			String content = request.getParameter("content");
//			//�˻��� ����
//			String option = null;
//
//			
//			if(setop.equals("all")) {//�˻� �ɼ��� all�� ���
//				bookDTO.setTitle(content);
//				bookDTO.setAuthor(content);	
//				option="��ü";
//			}
//			else if(setop.equals("title")) {//�˻� �ɼ��� title�� ���
//				bookDTO.setTitle(content);
//				option="������";
//			}
//			else if(setop.equals("author")) {//�˻� �ɼ��� author�� ���
//				bookDTO.setAuthor(content);
//				option="����";
//			}
//			else if(setop.equals("titleNauthor")) {//�˻� �ɼ��� titleNauthor�� ���
//				bookDTO.setTitle(content);
//				bookDTO.setAuthor(content);				
//				option="������+����";
//			}
//	
//
//			bookList = bookDAO.bookSearch(bookDTO, setop);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("bookSearchResult.jsp");
			request.setAttribute("search", search);
//			request.setAttribute("option", option);
//			request.setAttribute("content", content);
//			request.setAttribute("cnt", bookList.size());
			dispatcher.forward(request, response);

		}//�����˻� ��
		
		else if(command.equals("/bookList.book")) {//å��ü���
			bookList=bookDAO.bookList(bookDTO);
			RequestDispatcher dispatcher = request.getRequestDispatcher("bookList.jsp");
			request.setAttribute("bookList", bookList);
			//dispatcher.include(request, response);
			dispatcher.forward(request, response);
			
		}//å��ü���
		
		
		
		

	}
}
