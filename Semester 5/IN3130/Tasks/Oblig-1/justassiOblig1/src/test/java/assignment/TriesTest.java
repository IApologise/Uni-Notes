package assignment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

public class TriesTest {
    @Nested
    public class TestClass1 {

        Trie trie;

        @BeforeEach
        public void setUp() {
            trie = new Trie();
            trie.insert("interweb");
            trie.insert("internet");
            trie.insert("inter");
            trie.insert("inside");
        }

        @Test
        public void testLookUpInterweb() {
            assertTrue(trie.lookUp("interweb"));
        }

        @Test
        public void testLookUpInter() {
            assertTrue(trie.lookUp("inter"));
        }

        @Test
        public void testLookUpIn() {
            assertFalse(trie.lookUp("in"));
        }

        @Test
        public void testPrintTree() {
            assertEquals("(in(side)(ter(net)(web)))", trie.printTree());
        }
    }

    @Nested
    public class TestClass2 {

        Trie trie;

        @BeforeEach
        public void setUp() {
            trie = new Trie();

            trie.insert("internet");
            trie.insert("interview");
            trie.insert("internally");
            trie.insert("algorithm");
            trie.insert("all");
            trie.insert("web");
            trie.insert("world");
        }

        @Test
        public void testLookUpInter() {
            assertFalse(trie.lookUp("inter"));
        }

        @Test
        public void testLookUpAll() {
            assertTrue(trie.lookUp("all"));
        }

        @Test
        public void testLookUpAl() {
            assertFalse(trie.lookUp("al"));
        }

        @Test
        public void testLookUpAlgorithm() {
            assertTrue(trie.lookUp("algorithm"));
        }

        @Test
        public void testLookUpW() {
            assertFalse(trie.lookUp("w"));
        }

        @Test
        public void testLookUpInterney() {
            assertFalse(trie.lookUp("interney"));
        }

        @Test
        public void testPrintTree() {
            assertEquals("((al(gorithm)(l))(inter(n(ally)(et))(view))(w(eb)(orld)))", trie.printTree());
        }
    }
}
