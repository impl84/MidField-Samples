
package rc;

import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_CODE;
import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_DATA;
import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_MESSAGE;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.midfield_system.api.rpc.ErrorResponseHandler;
import com.midfield_system.api.rpc.RpcClient;
import com.midfield_system.api.rpc.RpcResponse;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcMfsNode
 *
 * Date Modified: 2022.06.09
 *
 */
public class RcMfsNode
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final long RESPONSE_TIMEOUT = 8000;  // [msec]
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final RpcClient rpcClient;
    
    private RcDeviceInfoManager devInfMgr = null;
    private RcStreamInfoManager stmInfMgr = null;
    private RcSegmentIo         segIo     = null;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public RcMfsNode(String serverName, int serverPort, ErrorResponseHandler handler)
        throws UnknownHostException,
            IOException
    {
        // 引数で与えられたサーバと接続するための RpcClient を生成する．
        // (UnknownHostException, IOException)
        rpcClient = new RpcClient(
            serverName,     // RPCサーバ名またはIPアドレス
            serverPort,     // RPCサーバのポート番号
            false,          // JSONオブジェクト(文字列)を整形するか否か
            true,           // JSONオブジェクト(文字列)をログ出力するか否か
            handler         // ErrorResponseHandler のインスタンス
        );
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: 
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void open()
    {
        // RpcClient の処理を開始する．
        this.rpcClient.open();
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void close()
    {
        // RpcClient の処理を終了する．
        this.rpcClient.close();
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: 
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RcDeviceInfoManager getRcDeviceInfoManager()
    {
        if (this.devInfMgr == null) {
            this.devInfMgr = new RcDeviceInfoManager(this);
        }
        return this.devInfMgr;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RcStreamInfoManager getRcStreamInfoManager()
    {
        if (this.stmInfMgr == null) {
            this.stmInfMgr = new RcStreamInfoManager(this);
        }
        return this.stmInfMgr;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RcSegmentIo newRcSegmentIo()
        throws RemoteControlException
    {
        if (this.segIo == null) {
            this.segIo = new RcSegmentIo(this);
        }
        this.segIo.reset();
        
        return this.segIo;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RcStreamPerformer newRcStreamPerformer(RcSegmentIo segIo)
        throws RemoteControlException
    {
        RcStreamPerformer pfmr = new RcStreamPerformer(this);
        
        return pfmr;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public boolean isRunning()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        Object result = invoke("MfsNode.isRunning", null);
        
        // 結果を boolean に変換して返す．
        return Boolean.parseBoolean((String)result);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String lock()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        Object result = invoke("MfsNode.lock", null);
        
        // 結果を String に変換して返す．
        return (String)result;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String unlock()
        throws RemoteControlException
    {
        // 対応する遠隔メソッドを呼び出す．
        // (RemoteControlException)
        Object result = invoke("MfsNode.unlock", null);
        
        // 結果を String に変換して返す．
        return (String)result;
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    Object invoke(String method, Object params)
        throws RemoteControlException
    {
        Object result = null;
        try {
            // 遠隔メソッドを呼び出し，RPC応答を取得する．
            // (InterruptedException, ExecutionException, TimeoutException)
            RpcResponse response = this.rpcClient.invoke(
                method, params, RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS
            );
            // 結果が null かどうか確認する．
            result = response.getResult();
            if (result == null) {
                // 結果が null の場合は，エラー情報を取得する．
                Map<String, Object> error  = response.getError();
                String              errMsg = String.format(
                    "error: code: %s, message: %s, data: %s",
                    error.get(ERR_CODE), error.get(ERR_MESSAGE), error.get(ERR_DATA)
                );
                // RemoteControlException を生成して投げる．
                throw new RemoteControlException(errMsg);
            }
        }
        catch (InterruptedException | ExecutionException | TimeoutException cause) {
            // 遠隔メソッド呼び出し時に例外が発生した場合，
            // その例外を原因とする RemoteControlException を生成して投げる．
            throw new RemoteControlException(
                "遠隔メソッド呼び出し時に例外が発生しました．", cause
            );
        }
        return result;
    }
}
