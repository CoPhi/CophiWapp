package it.cnr.ilc.cophi.action.controller.content;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.cnr.ilc.cophi.utils.view.BaseEditorServlet;
import it.cnr.ilc.cophi.utils.view.BaseHtmlProcessorInterface;
import it.cnr.ilc.cophi.utils.view.PericopeEditorProcessor;

public class EditorServlet extends BaseEditorServlet {

	
	private final String[] tags = {"span","pre"};
	private final String[] classes = {"pericope", "nopericope", "focusedpericope", "root", "start", "end", "token", "add"};
	private final String[] attrs = {"id","rel","class"};
	private final BaseHtmlProcessorInterface processor = new PericopeEditorProcessor(tags, attrs, classes);

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setCharacterEncoding("utf-8");
			if (request.getPathInfo().endsWith("add.json"))
				serveAdd(response, request);
			else {
				super.doGet(request, response);	
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	private void serveAdd(HttpServletResponse response, HttpServletRequest request) throws IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		String html = request.getParameter("html");
		System.err.println("html " + html);
		processor.process(html);
		String processedHtml = processor.html();
		
		String result = new Gson().toJson(processedHtml);
		out.print(result);

	}

}
