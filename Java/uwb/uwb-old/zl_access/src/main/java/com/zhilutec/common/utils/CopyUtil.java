package com.zhilutec.common.utils;

import java.io.*;
import java.util.Collection;
import java.util.List;

public class CopyUtil {

    @SuppressWarnings("unchecked")
    public static <T> List<T> deepCopyList(List<T> src) {
        List<T> dest = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List<T>) in.readObject();
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }
        return dest;
    }

    public static <T> Collection<T> deepCopy(Collection<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        Collection<T> dest = (Collection<T>) in.readObject();
        return dest;
    }
}
