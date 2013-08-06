package com.jiangdx.stream.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.jiangdx.stream.util.IoUtil;

/**
 * File reserved servlet, mainly reading the request parameter and its file
 * part, stored it.
 */
public class StreamServlet extends HttpServlet {
	private static final long serialVersionUID = -8619685235661387895L;
	static final String FILE_FIELD = "file";
	static final String KEY_FIELD = "key";
	static final String FILE_NAME_FIELD = "name";
	/** when the has increased to 10kb, then flush it to the hard-disk. */
	static final int BUFFER_LENGTH = 10240;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		final String key = req.getParameter(KEY_FIELD);
		final String fileName = req.getParameter(FILE_NAME_FIELD);

		OutputStream out = null;
		InputStream content = null;
		final PrintWriter writer = response.getWriter();

		try {
			File f = IoUtil.getFile(key);
			out = new FileOutputStream(f);
			content = req.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[BUFFER_LENGTH];
			while ((read = content.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			
			long start = f.length();
			StringBuilder buf = new StringBuilder("{");
			buf.append("start:").append(start).append("}");
			writer.write(buf.toString());
		} catch (FileNotFoundException fne) {
			writer.println("<br/> ERROR: " + fne.getMessage());
		} finally {
			IoUtil.close(out);
			IoUtil.close(content);
			IoUtil.close(writer);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
