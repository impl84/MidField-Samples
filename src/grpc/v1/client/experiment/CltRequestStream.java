
package grpc.v1.client.experiment;

import java.util.concurrent.TimeUnit;

import com.midfield_system.api.util.Pauser;
import com.midfield_system.grpc.v1.ExperimentalRequest;
import com.midfield_system.grpc.v1.ExperimentalResponse;
import com.midfield_system.grpc.v1.GrpcExperimentGrpc;

import grpc.v1.client.Reporter;
import io.grpc.ManagedChannel;
import io.grpc.stub.ClientCallStreamObserver;

class CltRequestStream
    implements
        Experimenter
{
    private final GrpcExperimentGrpc.GrpcExperimentStub asyncStub;
    
    CltRequestStream(ManagedChannel channel)
    {
        this.asyncStub = GrpcExperimentGrpc.newStub(channel);
    }
    
    @Override
    public void doExperiments()
    {
        Reporter.message();
        Reporter.message("◆◆◆◆ Experiments for Request Stream ◆◆◆");
        
        Reporter.message();
        Reporter.message("▼allAsyncRequestStreams");
        allAsyncRequestStreams(this.asyncStub);
        
        Pauser.forDuration(5000);
    }
    
    private void allAsyncRequestStreams(
        GrpcExperimentGrpc.GrpcExperimentStub stub
    )
    {
        ClientCallStreamObserver<ExperimentalRequest> requestObserver = null;

        // 通常のRPC呼び出し
        requestObserver = asyncRequestStream(4, stub, "Do something");
        requestObserver.onCompleted();

        // サーバ側でエラー（RuntimeException）が発生する場合
        requestObserver = asyncRequestStream(4, stub, "RuntimeException in server side");
        requestObserver.onCompleted();

        // 通常のRPC呼び出し
        requestObserver = asyncRequestStream(4, stub, "Do something");
        requestObserver.onCompleted();
        
        // サーバ側でエラー（ABORTED）が発生する場合
        requestObserver = asyncRequestStream(4, stub, "ABORTED in server side");
        requestObserver.onCompleted();

        // 通常のRPC呼び出し
        requestObserver = asyncRequestStream(4, stub, "Do something");
        requestObserver.onCompleted();

        // クライアント側で2秒後にキャンセルする．サーバ側では4秒間待機する．
        requestObserver = asyncRequestStream(4, stub, "Time-consuming task");
        Pauser.forDuration(2000);
        RuntimeException ex = new RuntimeException("RuntimeExceptionのメッセージ");
        requestObserver.cancel("StatusRuntimeExceptionのメッセージ", ex);

        // 通常のRPC呼び出し
        requestObserver = asyncRequestStream(4, stub, "Do something");
        requestObserver.onCompleted();
        
        // 2秒のタイムアウトを設定したスタブを生成して利用する．
        // サーバ側では4秒間待機する．
        requestObserver = asyncRequestStream(
            2, stub.withDeadlineAfter(4, TimeUnit.SECONDS), "Time-consuming task"
        );
        requestObserver.onCompleted();

        // 通常のRPC呼び出し
        requestObserver = asyncRequestStream(4, stub, "Do something");
        requestObserver.onCompleted();

        // クライアント側でエラーが発生する場合
        requestObserver = asyncRequestStream(4, stub, "Do something");
        Pauser.forDuration(2000);
        Error err = new Error("Errorのメッセージ");
        requestObserver.onError(err);

        // 通常のRPC呼び出し
        requestObserver = asyncRequestStream(4, stub, "Do something");
        requestObserver.onCompleted();
    }
    
    private ClientCallStreamObserver<ExperimentalRequest> asyncRequestStream(
        int numRequests, GrpcExperimentGrpc.GrpcExperimentStub stub,
        String message
    )
    {
        var requestObserver = (ClientCallStreamObserver<ExperimentalRequest>)stub
            .experimentalRequestStream(
                new CltSingleResponseObserver(message)
            );
        
        for (int i = 0; i < numRequests; i++) {
            var request = ExperimentalRequest.newBuilder()
                .setRequestMessage(message)
                .build();
            
            requestObserver.onNext(request);
        }
        return requestObserver;
    }
}

class CltSingleResponseObserver
    implements
        io.grpc.stub.StreamObserver<ExperimentalResponse>
{
    private final String message;
    
    CltSingleResponseObserver(String message)
    {
        this.message = message;
    }
    
    @Override
    public void onCompleted()
    {
        Reporter.message("onCompleted> for message: " + this.message);
    }
    
    @Override
    public void onError(Throwable th)
    {
        Reporter.error("onError> for message: " + this.message, th);
    }
    
    @Override
    public void onNext(ExperimentalResponse response)
    {
        Reporter.message("onNext> in：" + response.getResponseMessage());
    }
}
