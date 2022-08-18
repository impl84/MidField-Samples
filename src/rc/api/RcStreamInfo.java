
package rc.api;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcStreamInfo
 *
 * Date Modified: 2022.06.09
 *
 */
public class RcStreamInfo
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------    
    private final int    streamInfoIndex;
    private final String videoConnection;
    private final String videoDescription;
    private final String videoFormat;
    private final String audioConnection;
    private final String audioDescription;
    private final String audioFormat;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public int getStreamInfoIndex()
    {
        return this.streamInfoIndex;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String getVideoConnection()
    {
        return this.videoConnection;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String getVideoDescription()
    {
        return this.videoDescription;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String getVideoFormat()
    {
        return this.videoFormat;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String getAudioConnection()
    {
        return this.audioConnection;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String getAudioDescription()
    {
        return this.audioDescription;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String getAudioFormat()
    {
        return this.audioFormat;
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    RcStreamInfo(
        int streamInfoIndex,
        String videoConnection, String videoDescription, String videoFormat,
        String audioConnection, String audioDescription, String audioFormat
    )
    {
        this.streamInfoIndex = streamInfoIndex;
        this.videoConnection = videoConnection;
        this.videoDescription = videoDescription;
        this.videoFormat = videoFormat;
        this.audioConnection = audioConnection;
        this.audioDescription = audioDescription;
        this.audioFormat = audioFormat;
    }
}
