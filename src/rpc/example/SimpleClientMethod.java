
package rpc.example;

import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.ErrorObject;
import com.midfield_system.rpc.api.ResponseObject;
import com.midfield_system.rpc.api.RpcClient;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.10.31
 *
 */
public class SimpleClientMethod
{
    private final RpcClient rpcClient;
    
    SimpleClientMethod(RpcClient rpcClient)
    {
        this.rpcClient = rpcClient;
    }
    
    String echo(String echoString)
    {
        Object result = null;
        try {
            // サーバのメソッドを呼び出し，RPC応答を取得する．
            // (InterruptedException, ExecutionException, TimeoutException)
            ResponseObject response = this.rpcClient.invoke(
                "Simple.echo",  // 呼び出すメソッド名
                echoString,     // メソッドの引数
                2000            // 応答取得までのタイムアウト時間
            );
            // 結果が null の場合はエラー情報を出力する．
            result = response.getResult();
            if (result == null) {
                ErrorObject error = response.getError();
                Log.message("error: %s", error);
            }
        }
        catch (Exception ex) {
            Log.error(ex);
        }
        return (String)result;
    }
}
