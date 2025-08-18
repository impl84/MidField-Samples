
package grpc.v1.client;

import java.util.Iterator;

import com.midfield_system.grpc.v1.DisableControlRequest;
import com.midfield_system.grpc.v1.EnableControlRequest;
import com.midfield_system.grpc.v1.NodeControlGrpc;
import com.midfield_system.grpc.v1.NodeControlGrpc.NodeControlBlockingStub;
import com.midfield_system.grpc.v1.NodeEventNotification;
import com.midfield_system.grpc.v1.NodeEventRequest;

class NodeControlExample
    extends
        ExampleBase
{
    private final NodeControlBlockingStub nodeControl;
    
    NodeControlExample(String serverAddress, int portNumber)
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
        Reporter.message("EnableControlResponse: " + enableControlResponse.getSuccess());
        
        var iterator = this.nodeControl.subscribeNodeEvent(
            NodeEventRequest.newBuilder()
                .build()
        );
        new Thread(() -> handleNodeEvent(iterator)).start();
        
        Reporter.waitForEnter();
        
        var disableControlResponse = this.nodeControl.disableControl(
            DisableControlRequest.newBuilder()
//                .setRequestMessage("Shutdown MfsNode")
                .build()
        );
        Reporter.message("DisableControlResponse: " + disableControlResponse.getSuccess());
    }
    
    private void handleNodeEvent(Iterator<NodeEventNotification> iterator)
    {
        while (iterator.hasNext()) {
            var response = iterator.next();
            
            switch (response.getEventTypeCase()) {
            case LOG_MESSAGE_EVENT:
                Reporter.message(response.getLogMessageEvent().getEventMessage());
                break;
            case NODE_METRICS_EVENT:
                Reporter.message(response.getNodeMetricsEvent().getEventMessage());
                break;
            case NODE_EXCEPTION_EVENT:
                Reporter.message(response.getNodeExceptionEvent().getEventMessage());
                break;
            case EVENTTYPE_NOT_SET:
                Reporter.message(response.getEventTypeCase().toString());
                break;
            
            }
        }
    }
}
