package com.hostfully.booking.base;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;

import java.util.ArrayList;
import java.util.List;

public class VerifyStubsExtension implements BeforeAllCallback, AfterAllCallback, AfterEachCallback {

    private final VerifyStubsListener listener = new VerifyStubsListener();

    @Override
    public void beforeAll(ExtensionContext context) {
        Mockito.framework().addListener(listener);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        listener.validateMocks();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        Mockito.framework().removeListener(listener);
    }

    public static class VerifyStubsListener implements MockCreationListener {

        private final List<Object> mocks = new ArrayList<>();

        @Override
        public void onMockCreated(Object mock, MockCreationSettings settings) {
            mocks.add(mock);
        }

        public void validateMocks() {
            try {
                if (!mocks.isEmpty()) {
                    Mockito.verifyNoMoreInteractions(mocks.toArray());
                }
            } finally {
                mocks.clear();
            }
        }
    }
}
