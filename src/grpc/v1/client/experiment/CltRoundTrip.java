
package grpc.v1.client.experiment;

import java.util.concurrent.TimeUnit;

import com.midfield_system.api.log.Log;
import com.midfield_system.api.util.Pauser;
import com.midfield_system.grpc.v1.ExperimentalRequest;
import com.midfield_system.grpc.v1.ExperimentalResponse;
import com.midfield_system.grpc.v1.GrpcExperimentGrpc;

import io.grpc.Context;
import io.grpc.ManagedChannel;

class CltRoundTrip
    implements
        GrpcClientExperimenter
{
    private final GrpcExperimentGrpc.GrpcExperimentStub         asyncStub;
    private final GrpcExperimentGrpc.GrpcExperimentBlockingStub blockingStub;
    
    CltRoundTrip(ManagedChannel channel)
    {
        this.asyncStub    = GrpcExperimentGrpc.newStub(channel);
        this.blockingStub = GrpcExperimentGrpc.newBlockingStub(channel);
    }
    
    @Override
    public void doExperiments()
    {
        Log.message();
        Log.message("◆◆◆◆ Experiments for Round Trip ◆◆◆◆");
        
        Log.message();
        Log.message("▼allBlockingRoundTrips");
        allBlockingRoundTrips(this.blockingStub);
        
        Pauser.forDuration(5000);
        
        Log.message("▼allAsyncRoundTrips");
        allAsyncRoundTrips(this.asyncStub);
        Log.message();
        
        Pauser.forDuration(5000);
    }
    
    private void allAsyncRoundTrips(
        GrpcExperimentGrpc.GrpcExperimentStub stub
    )
    {
        // 通常のRPC呼び出し
        asyncRoundTrip(stub, "Do something");
        
        // サーバ側でエラー（RuntimeException）が発生する場合
        asyncRoundTrip(stub, "RuntimeException in server side");
        
        // 通常のRPC呼び出し
        asyncRoundTrip(stub, "Do something");
        
        // サーバ側でエラー（ABORTED）が発生する場合
        asyncRoundTrip(stub, "ABORTED in server side");
        
        // 通常のRPC呼び出し
        asyncRoundTrip(stub, "Do something");
        
        // クライアント側でCancellableContext を利用して2秒後にキャンセルする．
        // サーバ側では4秒間待機する．
        var context   = Context.current().withCancellation();
        var canceller = new ContextCanceller(context, "キャンセル(非同期)", 2000);
        canceller.start();
        context.run(() -> asyncRoundTrip(stub, "Time-consuming task"));
        canceller.waitCancelTime();
        context.close();
        
        // 通常のRPC呼び出し
        asyncRoundTrip(stub, "Do something");
        
        // 2秒のタイムアウトを設定したスタブを生成して利用する．
        // サーバ側では4秒間待機する．
        asyncRoundTrip(stub.withDeadlineAfter(2, TimeUnit.SECONDS), "Time-consuming task");
        
        // 通常のRPC呼び出し
        asyncRoundTrip(stub, "Do something");
    }
    
    private void allBlockingRoundTrips(
        GrpcExperimentGrpc.GrpcExperimentBlockingStub stub
    )
    {
        // 通常のRPC呼び出し
        blockingRoundTrip(stub, "Do something");
        
        // サーバ側でエラー（RuntimeException）が発生する場合
        blockingRoundTrip(stub, "RuntimeException in server side");
        
        // 通常のRPC呼び出し
        blockingRoundTrip(stub, "Do something");
        
        // サーバ側でエラー（ABORTED）が発生する場合
        blockingRoundTrip(stub, "ABORTED in server side");
        
        // 通常のRPC呼び出し
        blockingRoundTrip(stub, "Do something");
        
        // クライアント側でCancellableContext を利用して2秒後にキャンセルする．
        // サーバ側では4秒間待機する．
        var context   = Context.current().withCancellation();
        var canceller = new ContextCanceller(context, "キャンセル(同期)", 2000);
        canceller.start();
        context.run(() -> blockingRoundTrip(stub, "Time-consuming task"));
        canceller.waitCancelTime();
        context.close();
        
        // 通常のRPC呼び出し
        blockingRoundTrip(stub, "Do something");
        
        // 2秒のタイムアウトを設定したスタブを生成して利用する．
        // サーバ側では4秒間待機する．
        blockingRoundTrip(stub.withDeadlineAfter(2, TimeUnit.SECONDS), "Time-consuming task");
        
        // 通常のRPC呼び出し
        blockingRoundTrip(stub, "Do something");
    }
    
    private void asyncRoundTrip(
        GrpcExperimentGrpc.GrpcExperimentStub stub, String message
    )
    {
        try {
            var request = ExperimentalRequest.newBuilder()
                .setRequestMessage(message)
                .build();
            
            stub.experimentalRoundTrip(
                request, new CltRoundTripResponseObserver(message)
            );
        }
        catch (Throwable th) {
            Log.error("asyncRoundTrip(" + message + ")", th);
        }
    }
    
    private void blockingRoundTrip(
        GrpcExperimentGrpc.GrpcExperimentBlockingStub stub, String message
    )
    {
        try {
            var request = ExperimentalRequest.newBuilder()
                .setRequestMessage(message)
                .build();
            
            var response = stub.experimentalRoundTrip(request);
            Log.message();
            Log.message("msg> in: " + response.getResponseMessage());
        }
        catch (Throwable th) {
            Log.error("blockingRoundTrip(" + message + ")", th);
        }
    }
}

class CltRoundTripResponseObserver
    implements
        io.grpc.stub.StreamObserver<ExperimentalResponse>
{
    private final String message;
    
    CltRoundTripResponseObserver(String message)
    {
        this.message = message;
    }
    
    @Override
    public void onCompleted()
    {
        Log.message("onCompleted> for message: " + this.message);
    }
    
    @Override
    public void onError(Throwable th)
    {
        Log.error("onError> for message: " + this.message, th);
    }
    
    @Override
    public void onNext(ExperimentalResponse response)
    {
        Log.message("onNext> in：" + response.getResponseMessage());
    }
}
