package kr.co.daegu.book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Newsapi
 */
@WebServlet(name = "bookapi", urlPatterns = { "/bookapi" })
public class BookApi extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BookApi() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		String search = request.getParameter("search");
		String clientId = "네이버에서 등록 해서 사용 하세요";// 애플리케이션 클라이언트 아이디값";
		String clientSecret = "네이버 개발자 센터 참고";// 애플리케이션 클라이언트 시크릿값";
		try {
			String text = URLEncoder.encode(search, "UTF-8");
			String apiURL = "https://openapi.naver.com/v1/search/book?query=" + text; // json 결과
			// String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text;
			// // xml 결과
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer sb = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			br.close();
			//System.out.println(sb.toString());
			response.setContentType("application/json; charset=UTF-8");
			// Get the printwriter object from response to write the required json object to the output stream      
			PrintWriter out = response.getWriter();
			// Assuming your json object is **jsonObject**, perform the following, it will return your json object  
			out.print(sb.toString());
			out.flush();
			//System.out.println(sb.toString());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
