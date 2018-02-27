package kr.co.daegu.book;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

/**
 * Servlet implementation class BookBestseller
 */
@WebServlet(name = "bestsellerapi", urlPatterns = { "/bestsellerapi" })
public class BookBestseller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookBestseller() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		Document doc = Jsoup.connect("http://book.naver.com/bestsell/bestseller_list.nhn?cp=kyobo").get();
		Elements contents;
		
		StringBuffer sb = new StringBuffer();
		
		Elements img = doc.select("div.thumb_type2 img[src]");
		int idx = 0;
		ArrayList<BookInfoDTO> list = new ArrayList<>();
		for (Element eTimg : img) {
			if (idx++ == 10) {
				break;
			}
			String isrc = eTimg.attr("src");
			String title = eTimg.attr("alt");
			String [] imgSrc = isrc.split("\\?");
			BookInfoDTO bi = new BookInfoDTO();
			bi.setTitle(title);
			bi.setImgSrc(imgSrc[0]);
			list.add(bi);
		}
		Gson gson = new Gson();
		String listJson = gson.toJson(list);
//		System.out.println(listJson);
		response.setContentType("application/json; charset=UTF-8");
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = response.getWriter();
		// Assuming your json object is **jsonObject**, perform the following, it will return your json object  
		out.print(listJson);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
