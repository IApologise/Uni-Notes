package assignment;

import assignment.BottomUpSos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class BottomUpSosTest {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class TestClass1 {
        BottomUpSos bottomUpSos;

        @BeforeAll
        void init() {
            bottomUpSos = new BottomUpSos(new int[] { 1, 5, 6, 3, 10 });
        }

        @Test
        void testCheckSum1() {
            int k = 11;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum2() {
            int k = 2;
            assertNull(bottomUpSos.checkSum(k));
        }

        @Test
        void testCheckSum3() {
            int k = 14;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass2 {
        BottomUpSos bottomUpSos;

        @BeforeAll
        void init() {
            bottomUpSos = new BottomUpSos(new int[] { 2, 4, 6, 8, 10 });
        }

        @Test
        void testCheckSum1() {
            int k = 11;
            assertNull(bottomUpSos.checkSum(k));
        }

        @Test
        void testCheckSum2() {
            int k = 12;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum3() {
            int k = 20;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass3 {
        BottomUpSos bottomUpSos;

        @BeforeAll
        void init() {
            bottomUpSos = new BottomUpSos(new int[] { 1 });
        }

        @Test
        void testCheckSum1() {
            int k = 1;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass4 {
        BottomUpSos bottomUpSos;

        @BeforeAll
        void init() {
            bottomUpSos = new BottomUpSos(new int[] { 2 });
        }

        @Test
        void testCheckSum1() {
            int k = 2;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum2() {
            int k = 1;
            assertNull(bottomUpSos.checkSum(k));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass5 {
        BottomUpSos bottomUpSos;

        @BeforeAll
        void init() {
            bottomUpSos = new BottomUpSos(new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        }

        @Test
        void testCheckSum1() {
            int k = 1;
            assertNull(bottomUpSos.checkSum(k));
        }

        @Test
        void testCheckSum2() {
            int k = 11;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum3() {
            int k = 29;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass6 {
        BottomUpSos bottomUpSos;

        @BeforeAll
        void init() {
            bottomUpSos = new BottomUpSos(new int[] { 7, 7, 7, 7, 7, 7, 7 });
        }

        @Test
        void testCheckSum1() {
            int k = 7;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum2() {
            int k = 21;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum3() {
            int k = 2;
            assertNull(bottomUpSos.checkSum(k));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestClass7 {
        BottomUpSos bottomUpSos;

        @BeforeAll
        void init() {
            bottomUpSos = new BottomUpSos(new int[] { 3, 2, 5, 11, 9, 19, 16 });
        }

        @Test
        void testCheckSum1() {
            int k = 17;
            assertNotNull(bottomUpSos.checkSum(k));
            assertEquals(k, Arrays.stream(bottomUpSos.checkSum(k)).sum());
        }

        @Test
        void testCheckSum2() {
            int k = 6;
            assertNull(bottomUpSos.checkSum(k));
        }

    }

}