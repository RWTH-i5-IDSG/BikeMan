package de.rwth.idsg.bikeman.psinterface.log;

import lombok.Getter;
import org.apache.commons.io.input.TeeInputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Taken from https://github.com/isrsal/spring-mvc-logger and modified
 *
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 09.10.2015
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    @Getter private String prefix;
    @Getter private boolean stationHeaderMissing;

    public RequestWrapper(String prefix, HttpServletRequest request, boolean stationHeaderMissing) {
        super(request);
        this.prefix = prefix;
        this.stationHeaderMissing = stationHeaderMissing;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private TeeInputStream tee = new TeeInputStream(RequestWrapper.super.getInputStream(), bos);

            @Override
            public int read() throws IOException {
                return tee.read();
            }

            @Override
            public boolean isFinished() {
                // Auto-generated method stub
                return false;
            }

            @Override
            public boolean isReady() {
                // Auto-generated method stub
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                // Auto-generated method stub
            }
        };
    }

    public byte[] toByteArray() {
        return bos.toByteArray();
    }
}