package com.example.magic03.patterndecorator.input;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 带有缓存功能的文件输入流，优化单字节读取的性能，减少io操作次数，提高性能，实现上使用装饰器模式
 */
public class BufferedFileInputStream extends InputStream {

    // 缓存
    private final byte[] buffer = new byte[8196];

    // 缓存中可被读取的下一个字节的索引位置
    private int position = -1;

    // 缓存中可读取的全量的字节数
    private int capacity = -1;

    private final FileInputStream fileInputStream;

    public BufferedFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    /**
     * 读取一个字节, 先从缓存中读取，如果缓存中没有，那么进行一次io操作，读取一批数据，并放入缓存中，然后再从缓存中读取
     *
     * @return
     * @throws IOException
     */
    @Override
    public int read() throws IOException {
        if (buffCanRead()) {
            return readFromBuffer();
        }
        // 从文件流中读取一批数据
        refreshBuffer();
        if (!buffCanRead()) {
            return -1;
        }

        return readFromBuffer();
    }

    private void refreshBuffer() throws IOException {
        capacity = fileInputStream.read(buffer);
        position = 0;
    }

    private int readFromBuffer() {
        return buffer[position++] & 0xff;
    }

    private boolean buffCanRead() {
        if (capacity == -1) {
            return false;
        }
        return position < capacity;
    }

    @Override
    public void close() throws IOException {
        this.fileInputStream.close();
    }
}
