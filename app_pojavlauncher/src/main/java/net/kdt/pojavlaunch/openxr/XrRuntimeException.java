package net.kdt.pojavlaunch.openxr;

public class XrRuntimeException extends RuntimeException {
    public final int result;

    public XrRuntimeException(int result, String s) {
        super(s);
        this.result = result;
    }
}