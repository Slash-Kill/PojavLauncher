package net.kdt.pojavlaunch.input.actions;

import net.kdt.pojavlaunch.openxr.OpenXRSession;

public interface InputAction {
    void sync(OpenXRSession session);
}
