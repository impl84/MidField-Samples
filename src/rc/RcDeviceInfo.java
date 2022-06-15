
package rc;

import java.util.ArrayList;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcDeviceInfo
 *
 * Date Modified: 2022.06.08
 *
 */
public class RcDeviceInfo
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------    
    private final int               deviceIndex;
    private final String            deviceName;
    private final ArrayList<String> formatList;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public int getDeviceIndex()
    {
        return this.deviceIndex;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String getDeviceName()
    {
        return this.deviceName;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ArrayList<String> getFormatList()
    {
        return this.formatList;
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    RcDeviceInfo(int deviceIndex, String deviceName, ArrayList<String> formatList)
    {
        this.deviceIndex = deviceIndex;
        this.deviceName = deviceName;
        this.formatList = formatList;
    }
}
