
package grpc.v1.client;

import com.midfield_system.grpc.v1.ListDeviceSourcesRequest;
import com.midfield_system.grpc.v1.ListMixerSourcesRequest;
import com.midfield_system.grpc.v1.ListNetworkSourcesRequest;
import com.midfield_system.grpc.v1.MediaType;
import com.midfield_system.grpc.v1.MixerSource;
import com.midfield_system.grpc.v1.NetworkSource;
import com.midfield_system.grpc.v1.SourceInfoGrpc;
import com.midfield_system.grpc.v1.SourceInfoGrpc.SourceInfoBlockingStub;

import io.grpc.ManagedChannel;

public class SourceInfoClient
{
    private final SourceInfoBlockingStub blockingStub;
    
    public SourceInfoClient(ManagedChannel managedChannel)
    {
        this.blockingStub = SourceInfoGrpc.newBlockingStub(managedChannel);
    }
    
    public DeviceSourceEntry getDefaultDeviceSourceEntry(MediaType mediaType)
    {
        var request = ListDeviceSourcesRequest.newBuilder()
            .setMediaType(mediaType)
            .build();
        
        var response = this.blockingStub.listDeviceSources(request);
        if (response.getDeviceSourcesCount() == 0) {
            throw new RuntimeException("デバイスが見つかりません: " + mediaType);
        }
        var sourceId = response.getDefaultDeviceSourceId();
        
        var source = response.getDeviceSourcesList()
            .stream()
            .filter(ds -> ds.getDeviceSourceId().equals(sourceId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("デフォルトデバイスが見つかりません: " + mediaType));
        
        var formatIndex = source.getDefaultFormatIndex();
        
        return new DeviceSourceEntry(sourceId, formatIndex);
    }
    
    public MixerSource getFirstMixerSource()
    {
        var request = ListMixerSourcesRequest.newBuilder()
            .build();
        
        var response = this.blockingStub.listMixerSources(request);
        if (response.getMixerSourcesCount() == 0) {
            throw new RuntimeException("ミキサーソースが見つかりません");
        }
        var mixerSource = response.getMixerSourcesList().get(0);
        return mixerSource;
    }
    
    public NetworkSource getFirstNetworkSource(String targetNode)
    {
        var request = ListNetworkSourcesRequest.newBuilder()
            .setTargetNode(targetNode)
            .build();
        
        var response = this.blockingStub.listNetworkSources(request);
        if (response.getNetworkSourcesCount() == 0) {
            throw new RuntimeException("ネットワークソースが見つかりません: " + targetNode);
        }
        var networkSource = response.getNetworkSourcesList().get(0);
        
        return networkSource;
    }
}
