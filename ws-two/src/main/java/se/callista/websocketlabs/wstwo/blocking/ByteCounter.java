package se.callista.websocketlabs.wstwo.blocking;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This doesn't do anything particularly useful - it just counts the total
 * number of bytes in a request body while demonstrating how to perform
 * old school blocking reads.
 */
public class ByteCounter extends HttpServlet {

	private static final Log logger = LogFactory.getLog(ByteCounter.class);

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	response.setContentType("text/plain");
    	response.setCharacterEncoding("UTF-8");

    	response.getWriter().println("Try again using a POST request.");
    }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		logger.debug("doPost called");

		long totalBytesRead = 0;
		byte[] b = new byte[1024];
		int len;

		ServletInputStream input = request.getInputStream();
		try {
			while ((len = input.read(b)) != -1) {
				totalBytesRead += len;
			}
		} finally {
			input.close();
		}

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		PrintWriter output = response.getWriter();
		try {
			output.println("Total bytes written = [" + totalBytesRead + "]");
		} finally {
			output.close();
		}

		logger.debug("doPost done, consumed [" + totalBytesRead + "] bytes");
    }
}