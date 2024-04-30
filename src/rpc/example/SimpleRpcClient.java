
package rpc.example;

import com.midfield_system.api.util.Log;
import com.midfield_system.json_rpc.api.InternalError;
import com.midfield_system.json_rpc.api.InternalErrorListener;
import com.midfield_system.json_rpc.api.RpcClient;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.25
 *
 */
public class SimpleRpcClient
{
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RpcClient rpcClient = null;
        try {
            Log.message("> SimpleRpcClient: RpcClient の処理を開始します．");
            
            // RpcClient を生成する．
            // (UnknownHostException, IOException)
            rpcClient = new RpcClient(
                "127.0.0.1",    // RPCサーバ名またはIPアドレス(ループバックアドレス)
                60202,          // RPCサーバのポート番号
                false,          // JSONオブジェクト(文字列)を整形するか否か
                true,           // JSONオブジェクト(文字列)をログ出力するか否か
                listener        // InternalErrorListener の実装
            );
            // RpcClient の処理を開始する．
            rpcClient.open();
            
            // メソッドを呼び出す．
            SimpleClientMethod clientMethod = new SimpleClientMethod(rpcClient);
            String             result       = clientMethod.echo("hello");
            Log.message("> SimpleRpcClient: echo メソッドの結果：" + result);
        }
        catch (Exception ex) {
            // RpcClient の動作中に例外が発生した．
            Log.message("> SimpleRpcClient: RpcClient の実行中に例外が発生しました．");
            ex.printStackTrace();
        }
        finally {
            // RpcClient の処理を終了する．
            if (rpcClient != null) {
                Log.message("> SimpleRpcClient: RpcClient の処理を終了します．");
                rpcClient.close();
            }
        }
    }
    
    // InternalErrorListener：ClientErrorListener クラスとしての実装
    static InternalErrorListener listener = new ClientErrorListener();
    
    static class ClientErrorListener
        implements
            InternalErrorListener
    {
        @Override
        public void onInternalError(InternalError error)
        {
            Log.error(error.toString());
        }
    }
    
    // ▼参考：InternalErrorListener の実装（無名クラス）    
    static InternalErrorListener listener_ex0 = new InternalErrorListener()
    {
        @Override
        public void onInternalError(InternalError error)
        {
            Log.error(error.toString());
        }
    };
    
    // ▼参考：InternalErrorListener の実装（ラムダ式）
    static InternalErrorListener listener_ex1 = (error) -> { Log.error(error.toString()); };
    
    // ▼参考：InternalErrorListener の実装（可能な限り省略したラムダ式）
    static InternalErrorListener listener_ex2 = error -> Log.error(error.toString());
}
