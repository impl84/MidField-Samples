
package grpc.v1.client.experiment;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.midfield_system.api.util.Pauser;
import com.midfield_system.grpc.v1.ExperimentalRequest;
import com.midfield_system.grpc.v1.ExperimentalResponse;
import com.midfield_system.grpc.v1.GrpcExperimentGrpc;

import grpc.v1.client.Reporter;
import io.grpc.Context;
import io.grpc.ManagedChannel;

class CltResponseStream
    implements
        Experimenter
{
    private final GrpcExperimentGrpc.GrpcExperimentStub         asyncStub;
    private final GrpcExperimentGrpc.GrpcExperimentBlockingStub blockingStub;
    
    CltResponseStream(ManagedChannel channel)
    {
        this.asyncStub    = GrpcExperimentGrpc.newStub(channel);
        this.blockingStub = GrpcExperimentGrpc.newBlockingStub(channel);
    }
    
    @Override
    public void doExperiments()
    {
        Reporter.message();
        Reporter.message("◆◆◆◆ Experiments for Response Stream ◆◆◆");
        
        Reporter.message();
        Reporter.message("▼allBlockingResponseStreams");
        allBlockingResponseStreams(this.blockingStub);
        
        Pauser.forDuration(5000);
        
        Reporter.message();
        Reporter.message("▼allAsyncResponseStreams");
        allAsyncResponseStreams(this.asyncStub);
        
        Pauser.forDuration(5000);
    }
    
    private void allAsyncResponseStreams(
        GrpcExperimentGrpc.GrpcExperimentStub stub
    )
    {
        // 通常のRPC呼び出し
        asyncResponseStream(stub, "Do something");
        
        // サーバ側でエラー（RuntimeException）が発生する場合
        asyncResponseStream(stub, "RuntimeException in server side");
        
        // 通常のRPC呼び出し
        asyncResponseStream(stub, "Do something");
        
        // サーバ側でエラー（ABORTED）が発生する場合
        asyncResponseStream(stub, "ABORTED in server side");
        
        // 通常のRPC呼び出し
        asyncResponseStream(stub, "Do something");
        
        // クライアント側でCancellableContext を利用して2秒後にキャンセルする．
        // サーバ側では4秒間待機する．
        var context   = Context.current().withCancellation();
        var canceller = new ContextCanceller(context, "キャンセル(非同期)", 2000);
        canceller.start();
        context.run(() -> asyncResponseStream(stub, "Time-consuming task"));
        canceller.waitCancelTime();
        context.close();
        
        // 通常のRPC呼び出し
        asyncResponseStream(stub, "Do something");
        
        // 2秒のタイムアウトを設定したスタブを生成して利用する．
        // サーバ側では4秒間待機する．
        asyncResponseStream(
            stub.withDeadlineAfter(2, TimeUnit.SECONDS), "Time-consuming task"
        );
        
        // 通常のRPC呼び出し
        asyncResponseStream(stub, "Do something");
    }
    
    private void allBlockingResponseStreams(
        GrpcExperimentGrpc.GrpcExperimentBlockingStub stub
    )
    {
        // 通常のRPC呼び出し
        blockingResponseStream(stub, "Do something");
        
        // サーバ側でエラー（RuntimeException）が発生する場合
        blockingResponseStream(stub, "RuntimeException in server side");
        
        // 通常のRPC呼び出し
        blockingResponseStream(stub, "Do something");
        
        // サーバ側でエラー（ABORTED）が発生する場合
        blockingResponseStream(stub, "ABORTED in server side");
        
        // 通常のRPC呼び出し
        blockingResponseStream(stub, "Do something");
        
        // クライアント側でCancellableContext を利用して2秒後にキャンセルする．
        // サーバ側では4秒間待機する．
        var context   = Context.current().withCancellation();
        var canceller = new ContextCanceller(context, "キャンセル(同期)", 2000);
        canceller.start();
        context.run(() -> blockingResponseStream(stub, "Time-consuming task"));
        canceller.waitCancelTime();
        context.close();
        
        // 通常のRPC呼び出し
        blockingResponseStream(stub, "Do something");
        
        // 2秒のタイムアウトを設定したスタブを生成して利用する．
        // サーバ側では4秒間待機する．
        blockingResponseStream(
            stub.withDeadlineAfter(2, TimeUnit.SECONDS), "Time-consuming task"
        );
        
        // 通常のRPC呼び出し
        blockingResponseStream(stub, "Do something");
    }
    
    private void asyncResponseStream(
        GrpcExperimentGrpc.GrpcExperimentStub stub, String message
    )
    {
        try {
            var request = ExperimentalRequest.newBuilder()
                .setRequestMessage(message)
                .build();
            
            stub.experimentalResponseStream(
                request, new CltMultipleResponseObserver(message)
            );
        }
        catch (Throwable th) {
           Reporter.error("asyncResponseStream(" + message + ")", th);
        }
    }
    
    private void blockingResponseStream(
        GrpcExperimentGrpc.GrpcExperimentBlockingStub stub, String message
    )
    {
        try {
            var request = ExperimentalRequest.newBuilder()
                .setRequestMessage(message)
                .build();
            
            Iterator<ExperimentalResponse> it = stub.experimentalResponseStream(request);
            Reporter.message();
            
            for (int i = 0; it.hasNext(); i++) {
                var response = it.next();
                Reporter.message("msg> in(%d)：%s\n", i + 1, response.getResponseMessage());
            }
        }
        catch (Throwable th) {
            Reporter.error("blockingResponseStream(" + message + ")", th);
        }
    }
}

class CltMultipleResponseObserver
    implements
        io.grpc.stub.StreamObserver<ExperimentalResponse>
{
    private final String message;
    
    CltMultipleResponseObserver(String message)
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
