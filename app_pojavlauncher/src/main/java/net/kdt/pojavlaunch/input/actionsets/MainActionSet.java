package net.kdt.pojavlaunch.input.actionsets;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import net.kdt.pojavlaunch.input.actions.Action;
import net.kdt.pojavlaunch.input.actions.BoolAction;
import net.kdt.pojavlaunch.input.actions.Vec2fAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oshi.util.tuples.Pair;

public class MainActionSet extends ActionSet{
    public BoolAction select = new BoolAction("select"); // Select what the controller is point at
    public Vec2fAction scroll = new Vec2fAction("scroll");
    public BoolAction back = new BoolAction("back");
    public BoolAction resetGUI = new BoolAction("reset_gui");

    @RequiresApi(api = Build.VERSION_CODES.R)
    public final List<Action> actions = List.of(
            select,
            scroll,
            back,
            resetGUI
    );

    public MainActionSet() {
        super("main", 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public List<Action> actions() {
        return actions;
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getDefaultBindings(HashMap<String, List<Pair<Action, String>>> map) {
        map.computeIfAbsent("/interaction_profiles/oculus/touch_controller", aLong -> new ArrayList<>()).addAll(
                List.of(
                        new Pair<>(select, "/user/hand/right/input/trigger/value"),
                        new Pair<>(scroll, "/user/hand/right/input/thumbstick"),
                        new Pair<>(resetGUI, "/user/hand/left/input/thumbstick/click"),
                        new Pair<>(back, "/user/hand/right/input/b/click")
                ));
    }
}