package me.mneri.offer.test.answer;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class UnsupportedOperationAnswer implements Answer<Object> {
    @Override
    public Object answer(InvocationOnMock invocation) {
        throw new UnsupportedOperationException(invocation.getMethod().getName() + " is not stubbed.");
    }
}