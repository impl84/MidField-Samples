
package rc;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RemoteControlException
 *
 * Date Modified: 2022.06.08
 *
 */
@SuppressWarnings("serial")
public class RemoteControlException
    extends
        Exception
{
// =============================================================================
// INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public RemoteControlException(String msg)
    {
        super(msg);
    }
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public RemoteControlException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
