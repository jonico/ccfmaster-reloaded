package com.collabnet.ccf.compat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Feature detection for the TeamForge SDK.
 *
 * <p>The sources at HEAD call {@code Connection.supports65()} and
 * {@code IntegratedApplicationClient.setIntegratedApplicationIcon(String,String,String,String)}.
 * Neither member exists in any {@code extlib/tfapi.jar} ever committed to this repository -
 * all four historical versions top out at {@code supports63()} and expose no icon methods at
 * all - and the TeamForge SDK was never published to a public Maven repository. HEAD therefore
 * has never compiled against its own vendored dependency.
 *
 * <p>Rather than exclude the two affected files (a Spring context and
 * {@code CreateIntegratedAppStrategy} both reference them) or fabricate SDK members, these
 * calls are made reflectively. That is faithful to the original intent: {@code supports65()}
 * was a feature gate, so on an older SDK it is simply absent and the answer is {@code false}.
 * The icon upload is reached only from inside that gate, so with the shipped SDK it is
 * unreachable.
 */
public final class TeamForgeCompat {

    private TeamForgeCompat() {
    }

    /**
     * @return whether the connected TeamForge is 6.5 or newer; {@code false} when the SDK on
     *         the classpath predates the check, which is the case for the vendored jar.
     */
    public static boolean supports65(Object connection) {
        if (null == connection) {
            return false;
        }
        try {
            Method m = connection.getClass().getMethod("supports65");
            return Boolean.TRUE.equals(m.invoke(connection));
        } catch (NoSuchMethodException absentOnThisSdk) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        } catch (InvocationTargetException e) {
            return false;
        }
    }

    /**
     * Sets an integrated application icon, if the SDK supports it.
     *
     * @throws UnsupportedOperationException when the SDK predates the method. Reaching this
     *         requires {@link #supports65(Object)} to have returned true, so it cannot happen
     *         with the vendored jar.
     */
    public static void setIntegratedApplicationIcon(Object client, String plugId,
            String iconFileKey, String fileName, String mimeType) {
        try {
            Method m = client.getClass().getMethod("setIntegratedApplicationIcon",
                    String.class, String.class, String.class, String.class);
            m.invoke(client, plugId, iconFileKey, fileName, mimeType);
        } catch (NoSuchMethodException e) {
            throw new UnsupportedOperationException(
                    "the TeamForge SDK on the classpath has no setIntegratedApplicationIcon; "
                            + "this path should be gated behind TeamForgeCompat.supports65()", e);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(e.getCause());
        }
    }
}
