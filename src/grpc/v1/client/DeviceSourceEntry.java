
package grpc.v1.client;

public class DeviceSourceEntry
{
    private final int deviceSourceIndex;
    private final int formatIndex;
    
    DeviceSourceEntry(int deviceSourceIndex, int formatIndex)
    {
        this.deviceSourceIndex = deviceSourceIndex;
        this.formatIndex       = formatIndex;
    }
    
    int getDeviceSourceIndex()
    {
        return this.deviceSourceIndex;
    }
    
    int getFormatIndex()
    {
        return this.formatIndex;
    }
}