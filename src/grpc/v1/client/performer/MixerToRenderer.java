
package grpc.v1.client.performer;

import grpc.v1.client.ExampleBase;
import grpc.v1.client.PerformerControlClient;
import grpc.v1.client.PerformerIoClient;
import grpc.v1.client.PerformerManagerClient;

public class MixerToRenderer
    extends
        ExampleBase
{
    private static final String MIXER_NAME = "Experimental Mixer";
    
    public MixerToRenderer(String host, int port)
    {
        super(host, port);
    }
    
    @Override
    public void execute()
    {
        var performerManager = new PerformerManagerClient(getManagedChannel());
        var instanceId       = performerManager.registerPerformer("MixerToRenderer");
        
        var performerIo = new PerformerIoClient(getManagedChannel());
        performerIo.configureMixerSource(instanceId, MIXER_NAME);
        performerIo.configureRenderer(instanceId);
        
        var performerControl = new PerformerControlClient(getManagedChannel());
        performerControl.controlPerformer(instanceId);
    }
}
