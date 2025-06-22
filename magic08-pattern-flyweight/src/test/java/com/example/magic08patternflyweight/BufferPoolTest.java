package com.example.magic08patternflyweight;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class BufferPoolTest {

    @Test
    void testSingleThreadAllocateDeallocate() throws InterruptedException {
        BufferPool pool = new BufferPool(1024, 256);

        ByteBuffer buffer1 = pool.allocate(256, 1000);
        assertNotNull(buffer1);
        assertEquals(256, buffer1.capacity());

        pool.deallocate(buffer1);

        ByteBuffer buffer2 = pool.allocate(256, 1000);
        assertSame(buffer1, buffer2);
    }

    @Test
    void testAllocateMoreThanTotalShouldThrow() {
        BufferPool pool = new BufferPool(1024, 256);
        assertThrows(RuntimeException.class, () -> pool.allocate(2048, 1000));
    }

    @Test
    void testMultiThreadedAllocationBlocking() throws InterruptedException, ExecutionException, TimeoutException {
        BufferPool pool = new BufferPool(512, 256);

        ByteBuffer b1 = pool.allocate(256, 1000);
        ByteBuffer b2 = pool.allocate(256, 1000);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ByteBuffer> future = executor.submit(() -> pool.allocate(256, 10000));

        Thread.sleep(1000);
        assertFalse(future.isDone());

        pool.deallocate(b1);
        ByteBuffer b3 = future.get(100, TimeUnit.MILLISECONDS);
        assertNotNull(b3);

        executor.shutdownNow();
    }

}
