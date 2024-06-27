
package rpc.example;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.midfield_system.api.log.Log;
import com.midfield_system.json_rpc.api.ErrorObject;
import com.midfield_system.json_rpc.api.ResponseObject;
import com.midfield_system.json_rpc.api.RpcClient;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.10.31
 *
 */
public class DemoClientMethod
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final RpcClient rpcClient;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    DemoClientMethod(RpcClient rpcClient)
    {
        this.rpcClient = rpcClient;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void invokeAll()
    {
        exampleSimpleRpc();
        exampleErrorResponse();
        exampleUsingFuture();
        exampleMethodWithStringParam();
        exampleMethodWithMapParam();
        exampleNotification();
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleSimpleRpc()
    {
        // ■単純なメソッド呼び出しの例：
        printTitle("単純なメソッド呼び出しの例：");
        
        Object result = invoke("Demo.getBooleanResult", null);
        printResult(result);
        
        result = invoke("Demo.getSuccessResult", null);
        printResult(result);
        
        result = invoke("Demo.getResponseWithResult", null);
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleErrorResponse()
    {
        // ■各種エラー発生時の例：
        printTitle("各種エラー発生時の例：");
        
        Object result = invoke("Demo.getParseError", null);
        printResult(result);
        
        result = invoke("Demo.getInvalidRequest", null);
        printResult(result);
        
        result = invoke("Demo.getMethodNotFound", null);
        printResult(result);
        
        result = invoke("Demo.getInvalidParams", null);
        printResult(result);
        
        result = invoke("Demo.getInternalError", null);
        printResult(result);
        
        result = invoke("Demo.getServerError", null);
        printResult(result);
        
        result = invoke(null, null);
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleUsingFuture()
    {
        // ■Future<ResponseObject> を利用したRPC要求と応答の例：
        printTitle("Future<ResponseObject> を利用したRPC要求と応答の例：");
        
        Future<ResponseObject> future0 = sendRequest("Demo.getBooleanResult", null);
        Future<ResponseObject> future1 = sendRequest("Demo.getSuccessResult", null);
        
        Object result = waitResponse(future0);
        printResult(result);
        
        result = waitResponse(future1);
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleMethodWithStringParam()
    {
        // ■文字列をパラメータとするRPC要求と応答の例：
        printTitle("文字列をパラメータとするRPC要求と応答の例：");
        
        String result = (String)invoke("Demo.echo", "hello");
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleMethodWithMapParam()
    {
        // ■複数のパラメータを伴うRPC要求と応答の例：
        printTitle("複数のパラメータを伴うRPC要求と応答の例：");
        
        // RPCの引数となるマップを生成する．
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        int productNo = 1;
        int quantity  = 20;
        
        params.put("productNo", Integer.toString(productNo));
        params.put("quantity", Integer.toString(quantity));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>)invoke("Demo.orderProducts", params);
        if (result != null) {
            printResult(
                String.format(
                    "productNo: %s, quantity: %s, unitPrice: \\%s, totalPrice: \\%s",
                    result.get("productNo"),
                    result.get("quantity"),
                    result.get("unitPrice"),
                    result.get("totalPrice")
                )
            );
        }
        else {
            printResult("null");
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleNotification()
    {
        // ■RPC要求(Notification)の例：
        printTitle("Notification の例：");
        
        notify("Demo.notification", null);
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private Object invoke(String method, Object params)
    {
        Object result = null;
        try {
            Log.message("▼invoke: %s(%s)", method, params);
            
            // サーバのメソッドを呼び出し，RPC応答を取得する．
            // (InterruptedException, ExecutionException, TimeoutException)            
            ResponseObject response = this.rpcClient.invoke(method, params, 2000);
            
            // 結果が null の場合はエラー情報を出力する．
            result = response.getResult();
            if (result == null) {
                ErrorObject error = response.getError();
                Log.message("△ error: %s", error);
            }
        }
        catch (Exception ex) {
            Log.error(ex);
        }
        return result;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private Future<ResponseObject> sendRequest(String method, Object params)
    {
        Log.message("▼request: %s(%s)", method, params);
        
        // RPC要求をサーバへ送る．
        Future<ResponseObject> future = this.rpcClient.request(method, params);
        return future;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private Object waitResponse(Future<ResponseObject> future)
    {
        Object result = null;
        try {
            // RPC応答を取得する．
            // (InterruptedException, ExecutionException, TimeoutException)
            ResponseObject response = future.get(2000, TimeUnit.MILLISECONDS);
            
            // 結果が null の場合はエラー情報を出力する．
            result = response.getResult();
            if (result == null) {
                ErrorObject error = response.getError();
                Log.message("△ error: %s", error);
            }
        }
        catch (Exception ex) {
            Log.error(ex);
        }
        return result;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void notify(String method, Object params)
    {
        Log.message("◆notify: %s(%s)", method, params);
        
        // RPC要求(Notification)をサーバへ送る．
        this.rpcClient.notification(method, params);
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void printTitle(String title)
    {
        Log.message(Log.LINE_SEPARATOR);
        Log.message("□%s", title);
        Log.message(Log.LINE_SEPARATOR);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void printResult(Object result)
    {
        Log.message("△result: %s", result);
        Log.message();
    }
}
