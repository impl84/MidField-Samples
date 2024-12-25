
package grpc.v1.performer;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.midfield_system.grpc.v1.DisableControlRequest;
import com.midfield_system.grpc.v1.EnableControlRequest;
import com.midfield_system.grpc.v1.NodeControlGrpc;
import com.midfield_system.grpc.v1.NodeControlGrpc.NodeControlBlockingStub;
import com.midfield_system.grpc.v1.NodeEventNotification;
import com.midfield_system.grpc.v1.NodeEventRequest;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

abstract class MfsGrpcExampleBase
{
    private final ManagedChannel          channel;
    private final NodeControlBlockingStub nodeControl;
    
    MfsGrpcExampleBase(String serverAddress, int portNumber)
    {
        var target = serverAddress + ":" + portNumber;
        
        var channelCredentials = InsecureChannelCredentials.create();
        
        channel = Grpc.newChannelBuilder(target, channelCredentials)
            .build();
        
        this.nodeControl = NodeControlGrpc.newBlockingStub(channel);
        
        var response = this.nodeControl.enableControl(
            EnableControlRequest
                .newBuilder()
                .build()
        );
        System.out.println(response);
        
        var iterator = this.nodeControl.subscribeNodeEvent(
            NodeEventRequest
                .newBuilder()
                .build()
        );
        new Thread(() -> handleNodeEvent(iterator)).start();
    }
    
    abstract void execute();
    
    void shutdown()
    {
        var response = this.nodeControl.disableControl(
            DisableControlRequest
                .newBuilder()
                .build()
        );
        System.out.println(response);
        
        try {
            this.channel
                .shutdownNow()
                .awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
    }
    
    protected ManagedChannel getManagedChannel()
    {
        return this.channel;
    }
    
    private void handleNodeEvent(Iterator<NodeEventNotification> iterator)
    {
        while (iterator.hasNext()) {
            var response = iterator.next();
            
            switch (response.getEventTypeCase()) {
            case LOG_MESSAGE_EVENT:
                System.out.print(response.getLogMessageEvent().getEventMessage());
                break;
            case NODE_METRICS_EVENT:
                System.out.println(response.getNodeMetricsEvent().getEventMessage());
                break;
            case NODE_EXCEPTION_EVENT:
                System.out.println(response.getNodeExceptionEvent().getEventMessage());
                break;
            case EVENTTYPE_NOT_SET:
                System.out.println(response.getEventTypeCase());
                break;
            
            }
        }
    }
}
