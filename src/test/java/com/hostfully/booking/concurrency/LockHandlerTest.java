package com.hostfully.booking.concurrency;

import com.hostfully.booking.base.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class LockHandlerTest extends TestBase {

    private static final String LOCK_KEY = "TEST_LOCK_KEY";
    private final LockHandler lockHandler = new LockHandler();

    @Test
    @DisplayName("Should lock and unlock without exception when id is not locked")
    void shouldLockAndUnlockWithoutExceptionWhenIdIsNotLocked() {
        Long id = 1L;
        lockHandler.lockAndExecuteSupplier(LOCK_KEY, id, () -> null);
    }

    @Test
    @DisplayName("Should throw IllegalStateException when id is already locked")
    void shouldThrowIllegalStateExceptionWhenIdIsAlreadyLocked() throws InterruptedException {
        Long id = 1L;
        Runnable one = () -> {
            lockHandler.lockAndExecuteSupplier(LOCK_KEY, id, () -> {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
        };
        new Thread(one).start();
        Thread.sleep(1000L);
        assertThrows(IllegalStateException.class, () -> lockHandler.lockAndExecuteSupplier(LOCK_KEY, id, () -> null));
    }
}
