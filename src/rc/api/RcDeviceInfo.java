
package rc.api;

import java.util.ArrayList;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcDeviceInfo
 *
 * Date Modified: 2022.06.21
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
    private final int               preferredIndex;
    
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
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public int getPreferredIndex()
    {
        return this.preferredIndex;
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    RcDeviceInfo(
        int deviceIndex, String deviceName,
        ArrayList<String> formatList, int preferredIndex
    )
    {
        this.deviceIndex = deviceIndex;
        this.deviceName = deviceName;
        this.formatList = formatList;
        this.preferredIndex = preferredIndex;
    }
}
