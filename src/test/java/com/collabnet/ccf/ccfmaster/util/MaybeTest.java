package com.collabnet.ccf.ccfmaster.util;

import java.util.Iterator;

import org.junit.jupiter.api.Test;
import static com.collabnet.ccf.ccfmaster.util.Maybe.*;
import static org.junit.jupiter.api.Assertions.*;

public class MaybeTest {
    @Test
    public void callMapOnNoneIsNone() {
        Maybe<Object> m = none().map(identity());
        assertTrue(m instanceof None, "none().map didn't return None");
    }

    @Test
    public void callMapOnSome() {
        Maybe<Object> m = some(new Object());
        Maybe<Integer> n = m.map(constant(1));
        assertTrue(n instanceof Some, "some().map didn't return Some");
    }

    @Test
    public void getOnNoneThrows() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {    
            none().get();
                });
    }

    @Test
    public void getOnSomeReturnsValue() {
        Object o = new Object();
        Maybe<Object> m = some(o);
        assertSame(o, m.get());
    }

    @Test
    public void increaseCoreCoverageEquals() {
        Integer i1 = new Integer(1);
        Integer i2 = new Integer(1);
        assertNotSame(i1, i2);
        assertEquals(i1.hashCode(), i2.hashCode());
        Maybe<Integer> m1 = some(i1);
        Maybe<Integer> m2 = some(i2);
        Maybe<Integer> n1 = none();
        Maybe<Integer> n2 = none();
        assertTrue(m1.equals(m2) && m2.equals(m1));
        assertTrue(n1.equals(n2) && n2.equals(n1));
        assertFalse(m1.equals(n1) || n1.equals(m1));
        Maybe<Integer> mNull = some(null);
        assertFalse(mNull.equals(m1) || mNull.equals(n1));
        assertFalse(m1.equals(mNull) || n1.equals(mNull));
    }

    @Test
    public void increaseCoreCoverageHashCodes() {
        Maybe<Integer> m = some(1);
        assertEquals(7 * m.get().hashCode(), m.hashCode());
        m = some(null);
        assertEquals(7 * 23, m.hashCode());
        m = none();
        assertEquals(23, m.hashCode());
    }

    @Test
    public void increaseCoreCoverageNoneCallNext() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {    
            Iterator<Object> it = none().iterator();
            it.next();
                });
    }

    @Test
    public void increaseCoreCoverageSomeCallNextTwice() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {    
            Iterator<Integer> it = some(1).iterator();
            it.next();
            it.next();
                });
    }

    @Test
    public void mapToNullReturnsNone() {
        Maybe<Object> m = some(new Object());
        Maybe<Integer> n = m.map(constant((Integer) null));
        assertTrue(n instanceof None, "some().map didn't return None");
    }

    @Test
    public void maybeNullReturnsNone() {
        Maybe<Integer> m = maybe(null);
        assertTrue(m instanceof None, "maybe(null) didn't return None");
    }

    @Test
    public void maybeNullReturnsSome() {
        Maybe<Integer> m = maybe(1);
        assertTrue(m instanceof Some, "maybe(1) didn't return None");
    }

    @Test
    public void noneGetOrThrowException() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {    
            Maybe<Integer> s = none();
            s.getOrThrow(new Exception("should be thrown."));
                });
    }

    @Test
    public void noneGetOrThrowRuntimeException() /* no throws needed :) */{
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {    
            Maybe<Integer> s = none();
            s.getOrThrow(new RuntimeException("should be thrown."));
                });
    }

    @Test
    public void someGetOrThrowException() throws Exception {
        final Integer i = 24;
        Maybe<Integer> s = some(i);
        assertEquals(s.getOrThrow(new Exception("shouldn't be thrown.")), i);
    }

    @Test
    public void someGetOrThrowRuntimeException() /* no throws needed :) */{
        final Integer i = 24;
        Maybe<Integer> s = some(i);
        assertEquals(
                s.getOrThrow(new RuntimeException("shouldn't be thrown.")), i);
    }

    @Test
    public void testEquals() {
        final Integer i = new Integer(1);
        final Integer j = new Integer(1);
        Maybe<Integer> s1 = some(i);
        Maybe<Integer> s2 = some(j);
        assertNotSame(i, j);
        assertEquals(s1, s2);
    }

    @Test
    public void testGetOrElse() {
        Integer i = 1;
        Maybe<Integer> some = some(i);
        assertEquals(i, some.getOrElse(-1));
        Maybe<Integer> none = none();
        assertEquals(Integer.valueOf(-1), none.getOrElse(-1));
    }

    @Test
    public void testIsSomeAndIsNone() {
        Integer i = 1;
        Maybe<Integer> some = some(i);
        assertTrue(some.isSome(), "some.isSome didn't return true");
        assertFalse(some.isNone(), "some.isNone didn't return false");
        Maybe<?> none = none();
        assertFalse(none.isSome(), "none.isSome didn't return false");
        assertTrue(none.isNone(), "none.isNone didn't return true");
    }

    static <T, U> Transformer<T, U> constant(final U result) {
        return new Transformer<T, U>() {
            @Override
            public U transform(T t) {
                return result;
            }
        };
    }

    static <T> Transformer<T, T> identity() {
        return new Transformer<T, T>() {
            @Override
            public T transform(T t) {
                return t;
            }
        };
    }
}
