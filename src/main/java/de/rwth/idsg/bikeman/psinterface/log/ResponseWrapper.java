package de.rwth.idsg.bikeman.psinterface.log;

import lombok.Getter;
import org.apache.commons.io.output.TeeOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Taken from https://github.com/isrsal/spring-mvc-logger and modified
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2015
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private PrintWriter writer = new PrintWriter(bos);

    @Getter private String prefix;

    public ResponseWrapper(String prefix, HttpServletResponse response) {
        super(response);
        this.prefix = prefix;
    }

    @Override
    public ServletResponse getResponse() {
        return this;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            private TeeOutputStream tee = new TeeOutputStream(ResponseWrapper.super.getOutputStream(), bos);

            @Override
            public void write(int b) throws IOException {
                tee.write(b);
            }

            @Override
            public boolean isReady() {
                // Auto-generated method stub
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
                // Auto-generated method stub
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new TeePrintWriter(super.getWriter(), writer);
    }

    public byte[] toByteArray(){
        return bos.toByteArray();
    }
}

