package net.kdt.pojavlaunch.input.actionsets;

import net.kdt.pojavlaunch.input.actions.Action;
import net.kdt.pojavlaunch.input.actions.InputAction;
import net.kdt.pojavlaunch.openxr.OpenXRInstance;
import net.kdt.pojavlaunch.openxr.OpenXRSession;
import net.kdt.pojavlaunch.openxr.XrException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.openxr.XR10;
import org.lwjgl.openxr.XrActionSet;
import org.lwjgl.openxr.XrActionSetCreateInfo;
import org.lwjgl.system.MemoryStack;

import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;

import static org.lwjgl.system.MemoryStack.stackMallocPointer;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class ActionSet implements AutoCloseable {

    public final String name;
    private XrActionSet handle;
    private int priority;

    public ActionSet(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public abstract List<Action> actions();

    public boolean shouldSync() {
        return true;
    }

    public abstract void getDefaultBindings(HashMap<String, List<Pair<Action, String>>> map);

    public void sync(OpenXRSession session) {
        for (Action action : actions()) {
            if (action instanceof InputAction) {
                InputAction inputAction = (InputAction) this;
                inputAction.sync(session);
            }
        }
    }

    public final void createHandle(OpenXRInstance instance) throws XrException {
        try (MemoryStack stack = stackPush()) {
            String localizedName = "mcxr.actionset." + this.name;

            XrActionSetCreateInfo actionSetCreateInfo = XrActionSetCreateInfo.malloc(stack).set(XR10.XR_TYPE_ACTION_SET_CREATE_INFO,
                    NULL,
                    memUTF8("mcxr." + this.name),
                    memUTF8(localizedName),
                    priority
            );
            PointerBuffer pp = stackMallocPointer(1);
            instance.check(XR10.xrCreateActionSet(instance.handle, actionSetCreateInfo, pp), "xrCreateActionSet");
            handle = new XrActionSet(pp.get(0), instance.handle);

            for (Action action : actions()) {
                action.createHandle(handle, instance);
            }
        }
    }

    public final XrActionSet getHandle() {
        return handle;
    }

    public final void destroyHandles() {
        if (handle != null) {
            XR10.xrDestroyActionSet(handle);
        }
    }

    @Override
    public final void close() {
        destroyHandles();
        for (Action action : actions()) {
            action.close();
        }
    }
}