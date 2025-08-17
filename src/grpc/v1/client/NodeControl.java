
package grpc.v1.client;

import java.io.IOException;
import java.util.Iterator;

import com.midfield_system.grpc.v1.DisableControlRequest;
import com.midfield_system.grpc.v1.EnableControlRequest;
import com.midfield_system.grpc.v1.NodeControlGrpc;
import com.midfield_system.grpc.v1.NodeControlGrpc.NodeControlBlockingStub;
import com.midfield_system.grpc.v1.NodeEventNotification;
import com.midfield_system.grpc.v1.NodeEventRequest;

class NodeControl
    extends
        MfsGrpcExampleBase
{
    private final NodeControlBlockingStub nodeControl;
    
    NodeControl(String serverAddress, int portNumber)
    {
        super(serverAddress, portNumber);
        
        this.nodeControl = NodeControlGrpc.newBlockingStub(getManagedChannel());
    }
    
    @Override
    public void execute()
    {
        var enableControlResponse = this.nodeControl.enableControl(
            EnableControlRequest.newBuilder()
                .build()
        );
        System.out.println(enableControlResponse);
        
        var iterator = this.nodeControl.subscribeNodeEvent(
            NodeEventRequest.newBuilder()
                .build()
        );
        new Thread(() -> handleNodeEvent(iterator)).start();
        
        try {
            System.out.print("> Enter キーの入力を待ちます．");
            System.in.read();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        
        var disableControlResponse = this.nodeControl.disableControl(
            DisableControlRequest.newBuilder()
//                .setRequestMessage("Shutdown MfsNode")
                .build()
        );
        System.out.println(disableControlResponse);
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
