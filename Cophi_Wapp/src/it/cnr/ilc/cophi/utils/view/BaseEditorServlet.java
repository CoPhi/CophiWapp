package it.cnr.ilc.cophi.utils.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class
 */
@WebServlet("/editor")
public class BaseEditorServlet extends HttpServlet implements BaseEditorServletInterface {
	private static final long serialVersionUID = 23487628746237841L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BaseEditorServlet() {
		super();
		System.out.println("*** " + getClass().getSimpleName() + "(): started");
	}

	private List<Object> _getTypes(List<EditorMenuItem> rt) {
		List<Object> rv = new ArrayList<Object>();
		for (Iterator<EditorMenuItem> i = rt.iterator(); i.hasNext();) {
			EditorMenuItem r = i.next();
			if (!r.isHidden()) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("titleHtml", r.getTitleHtml());
				m.put("descriptionHtml", r.getDescriptionHtml());
				m.put("color", r.getColor());
				m.put("styleClassName", r.getStyleClassName());
				rv.add(m);
			}
		}
		return rv;
	}

	private List<Object> _getSpecialChars(String chars) {
		List<Object> rv = new ArrayList<Object>();
		for (int i = 0; i < chars.length(); i++) {
			Map<String, Object> m = new HashMap<String, Object>();
			Character rr = chars.charAt(i);
			m.put("titleHtml", rr);
			m.put("descriptionHtml", rr);
			rv.add(m);
		}
		return rv;
	}

	private List<Object> _getInstances(List<EditorMenuItem> r) {
		List<Object> rv = new ArrayList<Object>();
		for (Iterator<EditorMenuItem> i = r.iterator(); i.hasNext();) {
			Map<String, Object> m = new HashMap<String, Object>();
			EditorMenuItem rr = i.next();
			m.put("titleHtml", rr.getTitleHtml());
			m.put("descriptionHtml", rr.getDescriptionHtml());
			rv.add(m);
		}
		return rv;
	}

	private void serveCss(HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/css");
		List<EditorMenuItem> notes = getNotes();
		List<EditorMenuItem> references = getReferences();
		if (notes != null)
			for (EditorMenuItem rt : notes)
				out.println("." + rt.getStyleClassName() + " {background-color: " + rt.getColor() + "}");
		if (references != null)
			for (EditorMenuItem rt : references)
				out.println("." + rt.getStyleClassName() + " {background-color: " + rt.getColor() + "}");
	}

	private void serveJson(HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		List<EditorMenuItem> notes = getNotes();
		List<EditorMenuItem> references = getReferences();
		Map<String, Object> rv = new HashMap<String, Object>();
		String chars = getSpecialChars();
		if (chars != null)
			rv.put("chars", _getSpecialChars(chars));
		if (notes != null)
			rv.put("notes", _getTypes(notes));
		if (references != null)
			rv.put("references", _getTypes(references));
		List<List<EditorMenuItem>> quotes = getQuotes();
		List<Object> quoteChildren = new ArrayList<Object>();
		if (null != quotes)
			for (Iterator<List<EditorMenuItem>> i = quotes.iterator(); i.hasNext();)
				quoteChildren.add(_getInstances(i.next()));
		rv.put("quotes", quoteChildren);
		String result = new Gson().toJson(rv);
		out.print(result);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setCharacterEncoding("utf-8");
			if (request.getPathInfo().endsWith("style.css"))
				serveCss(response);
			else if (request.getPathInfo().endsWith("setup.json"))
				serveJson(response);
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public String getSpecialChars() {
		return null;
	}

	@Override
	public List<EditorMenuItem> getNotes() {
		return null;
	}

	@Override
	public List<EditorMenuItem> getReferences() {
		return null;
	}

	@Override
	public List<List<EditorMenuItem>> getQuotes() {
		return null;
	}

}
