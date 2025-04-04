
package grpc.v1.performer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.midfield_system.grpc.v1.ExperimentalRequest;
import com.midfield_system.grpc.v1.ExperimentalResponse;
import com.midfield_system.grpc.v1.GrpcExperimentGrpc;
import com.midfield_system.grpc.v1.GrpcExperimentGrpc.GrpcExperimentBlockingStub;
import com.midfield_system.grpc.v1.GrpcExperimentGrpc.GrpcExperimentStub;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class GrpcExperimentTester
    extends
        MfsGrpcExampleBase
{
    private final GrpcExperimentBlockingStub blockingStub;
    private final GrpcExperimentStub         stub;
    
    GrpcExperimentTester(String serverAddress, int portNumber)
    {
        super(serverAddress, portNumber);
        
        this.blockingStub = GrpcExperimentGrpc.newBlockingStub(getManagedChannel());
        this.stub         = GrpcExperimentGrpc.newStub(getManagedChannel());
    }
    
    @Override
    public void execute()
    {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("[clt] > GrpcExperimentTester#execute() を開始します．");
            
            this.interactiveLoop(reader);
            
            System.out.println("[clt] > GrpcExperimentTester#execute() を終了します．");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void interactiveLoop(BufferedReader reader)
    {
        while (true) {
            try {
                System.out.println(
                    "[clt] Enter a command (1: Request Stream, 2: Response Stream, 3: Round Trip, q: Quit): "
                );
                String command = reader.readLine();
                
                switch (command) {
                case "1":
                    callExperimentalRequestStream();
                    break;
                case "2":
                    callExperimentalResponseStream();
                    break;
                case "3":
                    callExperimentalRoundTrip();
                    break;
                case "q":
                    System.out.println("[clt] Exiting...");
                    return;
                default:
                    System.out.println("[clt] Invalid command. Please try again.");
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void callExperimentalRequestStream()
    {
        var responseObserver = new StreamObserver<ExperimentalResponse>()
        {
            @Override
            public void onCompleted()
            {
                System.out.println("[clt] Stream completed.");
            }
            
            @Override
            public void onError(Throwable t)
            {
                System.err.println("[clt] Error: " + t.getMessage());
            }
            
            @Override
            public void onNext(ExperimentalResponse value)
            {
                System.out.println("[clt] Received response: " + value.getResponseMessage());
            }
        };
        
        var requestObserver = this.stub.experimentalRequestStream(responseObserver);
        
        try {
            for (int i = 0; i < 5; i++) {
                var request = ExperimentalRequest
                    .newBuilder()
                    .setRequestMessage("[clt] Request message " + i)
                    .build();
                
                requestObserver.onNext(request);
                System.out.println("[clt] 　　" + request.getRequestMessage());
            }
            System.out.println("[clt] ■■■■#0");
/*
 * try {
 * Thread.sleep(2000); // Simulate some delay
 * }
 * catch (InterruptedException ex) {
 * Thread.currentThread().interrupt(); // Restore interrupt flag
 * }
 * requestObserver.onError(
 * Status.ABORTED.withDescription("■Simulated error").asRuntimeException()
 * );
 */
            requestObserver.onCompleted();
            System.out.println("[clt] ■■■■#1");
        }
        catch (Throwable ex) {
            // Handle error
            System.err.println("[clt] Throwable: " + ex.getMessage());
        }
    }
    
    private void callExperimentalResponseStream()
    {
        var request = ExperimentalRequest
            .newBuilder()
            .setRequestMessage("[clt] Request message")
            .build();
        
        // Blocking call
        var iterator = this.blockingStub.experimentalResponseStream(request);
        while (iterator.hasNext()) {
            var response = iterator.next();
            System.out.println("[clt] blockingStub: " + response.getResponseMessage());
        }
        
        // Non-blocking call
        var responseObserver = new StreamObserver<ExperimentalResponse>()
        {
            @Override
            public void onCompleted()
            {
                System.out.println("[clt] stub: Stream completed.");
            }
            
            @Override
            public void onError(Throwable t)
            {
                System.err.println("[clt] stub: Error: " + t.getMessage());
            }
            
            @Override
            public void onNext(ExperimentalResponse value)
            {
                System.out
                    .println("[clt] stub: Received response: " + value.getResponseMessage());
            }
        };
        
        this.stub.experimentalResponseStream(request, responseObserver);
    }
    
    private void callExperimentalRoundTrip()
    {
        var request = ExperimentalRequest
            .newBuilder()
            .setRequestMessage("[clt] Request message")
            .build();
        
        // Blocking call
        var response = this.blockingStub.experimentalRoundTrip(request);
        System.out.println("[clt] blockingStub: " + response.getResponseMessage());
        
        // Non-blocking call
        var responseObserver = new StreamObserver<ExperimentalResponse>()
        {
            @Override
            public void onCompleted()
            {
                System.out.println("[clt] stub: Round trip completed.");
            }
            
            @Override
            public void onError(Throwable t)
            {
                System.err.println("[clt] stub: Error: " + t.getMessage());
            }
            
            @Override
            public void onNext(ExperimentalResponse value)
            {
                System.out
                    .println("[clt] stub: Received response: " + value.getResponseMessage());
            }
        };
        
        this.stub.experimentalRoundTrip(request, responseObserver);
    }
}
