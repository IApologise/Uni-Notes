package assignment;

import assignment.TopDownSos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class TopDownSosTest {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class TestClass1 {
        TopDownSos topDownSos;

        @BeforeAll
        void init() {
            topDownSos = new TopDownSos(new int[] { 1, 5, 6, 3, 10 });
        }

        @Test
        void testCheckSum1() {
            int k = 11;
            assertNotNull(topDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(topDownSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum2() {
            int k = 2;
            assertNull(topDownSos.checkSum(k));
        }

        @Test
        void testCheckSum3() {
            int k = 14;
            assertNotNull(topDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(topDownSos.checkSum(k)).sum());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass2 {
        TopDownSos topDownSos;

        @BeforeAll
        void init() {
            topDownSos = new TopDownSos(new int[] { 2, 4, 6, 8, 10 });
        }

        @Test
        void testCheckSum1() {
            int k = 11;
            assertNull(topDownSos.checkSum(k));
        }

        @Test
        void testCheckSum2() {
            int k = 12;
            assertNotNull(topDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(topDownSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum3() {
            int k = 20;
            assertNotNull(topDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(topDownSos.checkSum(k)).sum());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass3 {
        TopDownSos toppDownSos;

        @BeforeAll
        void init() {
            toppDownSos = new TopDownSos(new int[] { 1 });
        }

        @Test
        void testCheckSum1() {
            int k = 1;
            assertNotNull(toppDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(toppDownSos.checkSum(k)).sum());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass4 {
        TopDownSos toppDownSos;

        @BeforeAll
        void init() {
            toppDownSos = new TopDownSos(new int[] { 2 });
        }

        @Test
        void testCheckSum1() {
            int k = 2;
            assertNotNull(toppDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(toppDownSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum2() {
            int k = 1;
            assertNull(toppDownSos.checkSum(k));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass5 {
        TopDownSos topDownSos;

        @BeforeAll
        void init() {
            topDownSos = new TopDownSos(new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        }

        @Test
        void testCheckSum1() {
            int k = 1;
            assertNull(topDownSos.checkSum(k));
        }

        @Test
        void testCheckSum2() {
            int k = 11;
            assertNotNull(topDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(topDownSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum3() {
            int k = 29;
            assertNotNull(topDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(topDownSos.checkSum(k)).sum());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass6 {
        TopDownSos topDownSos;

        @BeforeAll
        void init() {
            topDownSos = new TopDownSos(new int[] { 7, 7, 7, 7, 7, 7, 7 });
        }

        @Test
        void testCheckSum1() {
            int k = 7;
            assertNotNull(topDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(topDownSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum2() {
            int k = 21;
            assertNotNull(topDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(topDownSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum3() {
            int k = 2;
            assertNull(topDownSos.checkSum(k));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass7 {
        TopDownSos topDownSos;

        @BeforeAll
        void init() {
            topDownSos = new TopDownSos(new int[] { 3, 2, 5, 11, 9, 19, 16 });
        }

        @Test
        void testCheckSum1() {
            int k = 17;
            assertNotNull(topDownSos.checkSum(k));
            assertEquals(k, Arrays.stream(topDownSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum2() {
            int k = 6;
            assertNull(topDownSos.checkSum(k));
        }

    }

}