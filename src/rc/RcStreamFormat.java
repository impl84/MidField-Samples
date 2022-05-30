
package rc;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcStreamFormat
 *
 * Date Modified: 2022.06.09
 *
 */
public class RcStreamFormat
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------    
    private final int    formatIndex;
    private final String format;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public int getFormatIndex()
    {
        return this.formatIndex;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String getFormat()
    {
        return this.format;
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    RcStreamFormat(int formatIndex, String format)
    {
        this.formatIndex = formatIndex;
        this.format = format;
    }
}
