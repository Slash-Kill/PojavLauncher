package net.kdt.pojavlaunch;

public class PojavLauncherNativeLoad {
    static {
        System.loadLibrary("mcxr_loader");
    }

    public static native long getJVMPtr();
    public static native long getApplicationActivityPtr();
    public static native void renderImage(int colorAttachment, int index);
}
