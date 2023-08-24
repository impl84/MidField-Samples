
package trial.desktop;

import java.io.Serializable;

/*----------------------------------------------------------------------------*/
/**
 * Sample code for MidField API: DesktopMessage
 *
 * Date Modified: 2021.10.26
 *
 */
@SuppressWarnings("serial")
class DesktopMessage
    implements
        Serializable
{
    // - PACKAGE CONSTANT VALUE ------------------------------------------------
    static final String START_CONTROL    = "Start-Control";
    static final String CONTROL_ACCEPTED = "Control-Accepted";
    static final String CONTROL_REFUSED  = "Control-Refused";
    static final String CONTROL_MESSAGE  = "Control-Message";
    static final String STOP_CONTROL     = "Stop-Control";
    
    // - PACKAGE ENUM ----------------------------------------------------------
    static enum Action
    {
        KEY_PRESS,
        KEY_RELEASE,
        MOUSE_MOVE,
        MOUSE_PRESS,
        MOUSE_RELEASE,
        MOUSE_WHEEL,
        UNKNOWN
    }
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final Action       action;
    private final Serializable object;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    DesktopMessage(Serializable object)
    {
        this.action = Action.UNKNOWN;
        this.object = object;
    }
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    DesktopMessage(Action action, Serializable object)
    {
        this.action = action;
        this.object = object;
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    Action getAction()
    {
        return this.action;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    Object getObject()
    {
        return this.object;
    }
}
