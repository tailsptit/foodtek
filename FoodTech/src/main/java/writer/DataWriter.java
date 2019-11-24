package main.java.writer;

import main.java.sol.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;

public class DataWriter {
    BufferedWriter writer;  // write data to output stream. Output stream can be terminal or file

    /**
     * Constructor for DataWriter class, assign BufferedWriter
     *
     * @param writer BufferedWriter is used to write data
     */
    public DataWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    /**
     * Write data to output stream
     *
     * @param user User need to write output
     * @throws IOException
     */
    public void write(User user) throws IOException {
        Iterator<String> iterator = user.getPermissions().keySet().iterator();
        if (iterator.hasNext())
            writer.write(iterator.next());
        while (iterator.hasNext())
            writer.write(", " + iterator.next());
        writer.flush();
    }

    public void write(String ln) throws IOException {
        writer.write(ln);
        writer.flush();
    }

    /**
     * Close output stream
     *
     * @throws IOException Exception when close file
     */
    public void close() throws IOException {
        writer.close();
    }
}
