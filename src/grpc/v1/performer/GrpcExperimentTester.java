
package grpc.v1.performer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.midfield_system.grpc.v1.ExperimentalRequest;
import com.midfield_system.grpc.v1.ExperimentalResponse;
import com.midfield_system.grpc.v1.GrpcExperimentGrpc;
import com.midfield_system.grpc.v1.GrpcExperimentGrpc.GrpcExperimentBlockingStub;
import com.midfield_system.grpc.v1.GrpcExperimentGrpc.GrpcExperimentStub;

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
            System.out.println("> GrpcExperimentTester#execute() を開始します．");
            
            this.interactiveLoop(reader);
            
            System.out.println("> GrpcExperimentTester#execute() を終了します．");
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
                    "Enter a command (1: Request Stream, 2: Response Stream, 3: Round Trip, q: Quit): "
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
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid command. Please try again.");
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
                System.out.println("Stream completed.");
            }
            
            @Override
            public void onError(Throwable t)
            {
                System.err.println("Error: " + t.getMessage());
            }
            
            @Override
            public void onNext(ExperimentalResponse value)
            {
                System.out.println("Received response: " + value.getResponseMessage());
            }
        };
        
        var requestObserver = this.stub
            .experimentalRequestStream(responseObserver);
        
        for (int i = 0; i < 5; i++) {
            var request = ExperimentalRequest
                .newBuilder()
                .setRequestMessage("Request message " + i)
                .build();
            requestObserver.onNext(request);
        }
        
        requestObserver.onCompleted();
    }
    
    private void callExperimentalResponseStream()
    {
        var responseObserver = new StreamObserver<ExperimentalResponse>()
        {
            @Override
            public void onCompleted()
            {
                System.out.println("Stream completed.");
            }
            
            @Override
            public void onError(Throwable t)
            {
                System.err.println("Error: " + t.getMessage());
            }
            
            @Override
            public void onNext(ExperimentalResponse value)
            {
                System.out.println("Received response: " + value.getResponseMessage());
            }
        };
        
        var request = ExperimentalRequest
            .newBuilder()
            .setRequestMessage("Request message")
            .build();
        
        this.stub.experimentalResponseStream(request, responseObserver);
    }
    
    private void callExperimentalRoundTrip()
    {
        var response = this.blockingStub.experimentalRoundTrip(
            ExperimentalRequest
                .newBuilder()
                .setRequestMessage("Request message")
                .build()
        );
        System.out.println(response.getResponseMessage());
    }
}
