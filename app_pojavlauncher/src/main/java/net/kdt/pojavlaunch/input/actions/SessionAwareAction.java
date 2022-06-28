package net.kdt.pojavlaunch.input.actions;

import net.kdt.pojavlaunch.openxr.OpenXRSession;
import net.kdt.pojavlaunch.openxr.XrException;

public interface SessionAwareAction {

    void createHandleSession(OpenXRSession session) throws XrException;

    void destroyHandleSession();
}
