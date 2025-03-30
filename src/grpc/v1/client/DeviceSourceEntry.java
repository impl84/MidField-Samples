
package grpc.v1.client;

public class DeviceSourceEntry
{
    private final String deviceSourceId;
    private final int    formatIndex;
    
    DeviceSourceEntry(String deviceSourceId, int formatIndex)
    {
        this.deviceSourceId = deviceSourceId;
        this.formatIndex    = formatIndex;
    }
    
    String getDeviceSourceId()
    {
        return this.deviceSourceId;
    }
    
    int getFormatIndex()
    {
        return this.formatIndex;
    }
}