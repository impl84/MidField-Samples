
package grpc.performer;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.midfield_system.grpc.v1.ActivateOperationRequest;
import com.midfield_system.grpc.v1.DeactivateOperationRequest;
import com.midfield_system.grpc.v1.MfsNodeGrpc;
import com.midfield_system.grpc.v1.MfsNodeGrpc.MfsNodeBlockingStub;
import com.midfield_system.grpc.v1.NodeEvent;
import com.midfield_system.grpc.v1.ReportNodeEventRequest;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

/**
 * Abstract class that provides a framework for interacting with a gRPC server using the
 * MfsNodeGrpc service.
 * Manages the lifecycle of a gRPC channel and provides methods to activate and deactivate
 * operations on the server.
 */
abstract class MfsGrpcExample
{
    private final ManagedChannel      channel;
    private final MfsNodeBlockingStub mfsNode;
    
    /**
     * Constructs an MfsGrpcExample instance, initializes the gRPC channel and blocking
     * stub,
     * activates the operation on the server, and starts a thread to handle incoming
     * NodeEvent messages.
     *
     * @param serverAddress
     *        the address of the gRPC server
     * @param portNumber
     *        the port number of the gRPC server
     */
    MfsGrpcExample(String serverAddress, int portNumber)
    {
        // Construct the target address using the server address and port number
        var target = serverAddress + ":" + portNumber;
        
        // Create insecure channel credentials
        var channelCredentials = InsecureChannelCredentials.create();
        
        // Build the gRPC channel with the target address and credentials
        channel = Grpc.newChannelBuilder(target, channelCredentials)
            .build();
        
        // Create a blocking stub for the MfsNodeGrpc service
        this.mfsNode = MfsNodeGrpc.newBlockingStub(channel);
        
        // Send an ActivateOperationRequest to the server and print the response
        var response = this.mfsNode.activateOperation(
            ActivateOperationRequest
                .newBuilder()
                .build()
        );
        System.out.println(response);
        
        // Start a new thread to handle incoming NodeEvent messages
        var iterator = this.mfsNode.reportNodeEvent(
            ReportNodeEventRequest
                .newBuilder()
                .build()
        );
        new Thread(() -> handleNodeEvent(iterator)).start();
    }
    
    /**
     * Abstract method to be implemented by subclasses to define custom behavior.
     */
    abstract void execute();
    
    /**
     * Deactivates the operation on the server and shuts down the gRPC channel.
     * Waits up to 5 seconds for the channel to terminate.
     */
    void shutdown()
    {
        // Send a DeactivateOperationRequest to the server and print the response
        var response = this.mfsNode.deactivateOperation(
            DeactivateOperationRequest
                .newBuilder()
                .build()
        );
        System.out.println(response);
        
        try {
            // Shut down the gRPC channel and wait up to 5 seconds for it to terminate
            this.channel
                .shutdownNow()
                .awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex) {
            // Handle the InterruptedException by printing the stack trace
            ex.printStackTrace();
        }
        
    }
    
    /**
     * Returns the managed gRPC channel.
     *
     * @return the managed gRPC channel
     */
    protected ManagedChannel getManagedChannel()
    {
        return this.channel;
    }
    
    /**
     * Processes incoming NodeEvent messages in a loop.
     * Checks the type of each event and prints the corresponding message or event
     * details.
     *
     * @param iterator
     *        the iterator for NodeEvent messages
     */
    private void handleNodeEvent(Iterator<NodeEvent> iterator)
    {
        while (iterator.hasNext()) {
            var response = iterator.next();
            
            switch (response.getEventTypeCase()) {
            case LOG_MESSAGE_EVENT:
                System.out.print(response.getLogMessageEvent().getMessage());
                break;
            case NODE_STATE_EVENT:
                System.out.println(response.getNodeStateEvent().getDescription());
                break;
            case NODE_METRICS_EVENT:
                System.out.println(response.getNodeMetricsEvent().getDescription());
                break;
            case NODE_EXCEPTION_EVENT:
                System.out.println(response.getNodeExceptionEvent());
                break;
            case PERFORMER_STATE_EVENT:
                System.out.println(response.getPerformerStateEvent());
                break;
            case PERFORMER_METRICS_EVENT:
                System.out.println(response.getPerformerMetricsEvent());
                break;
            case PERFORMER_EXCEPTION_EVENT:
                System.out.println(response.getPerformerExceptionEvent());
                break;
            case FLOW_UPDATE_EVENT:
                System.out.println(response.getFlowUpdateEvent());
                break;
            case MIXER_UPDATE_EVENT:
                System.out.println(response.getMixerUpdateEvent());
                break;
            case EVENTTYPE_NOT_SET:
                System.out.println(response.getEventTypeCase());
                break;
            
            }
        }
    }
}
