package net.kdt.pojavlaunch;

import net.kdt.pojavlaunch.openxr.OpenXRState;
import net.kdt.pojavlaunch.input.ControllerPoses;
import net.kdt.pojavlaunch.openxr.PojavRenderer;
import net.kdt.pojavlaunch.rendering.VrFirstPersonRenderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class PojavLauncherPlayClient {

    public static Logger LOGGER = LogManager.getLogger("pojav");

    public static final OpenXRState OPEN_XR_STATE = new OpenXRState();
    public static final PojavRenderer POJAV_RENDERER = new PojavRenderer();

    public static PojavLauncherPlayClient INSTANCE;
    public static PojavGuiManager pojavGuiManager = new PojavGuiManager();
    public VrFirstPersonRenderer vrFirstPersonRenderer = new VrFirstPersonRenderer(pojavGuiManager);
    public static final ControllerPoses viewSpacePoses = new ControllerPoses();

    //Stage space => Unscaled Physical Space => Physical Space => Pojav Space
    //OpenXR         GUI                        Roomscale Logic   Pojav Space Logic
    //      Rotated + Translated           Scaled          Translated

    public static boolean heightAdjustStand = false;

    /**
     * The yaw rotation of STAGE space in physical space
     * Used to let the user turn
     */
    public static float stageTurn = 0;

    /**
     * The position of STAGE space in physical space
     * Used to let the user turn around one physical space position and
     * to let the user snap to the player entity position when roomscale movement is off
     */
    public static Vector3f stagePosition = new Vector3f(0, 0, 0);

    /**
     * The position of physical space in Minecraft space
     * xrOrigin = camaraEntity.pos - playerPhysicalPosition
     */
    public static Vector3d xrOrigin = new Vector3d(0, 0, 0);

    /**
     * The position of the player entity in physical space
     * If roomscale movement is disabled this vector is zero (meaning the player is at xrOrigin)
     * This is used to calculate xrOrigin
     */
    public static Vector3d playerPhysicalPosition = new Vector3d();

}
