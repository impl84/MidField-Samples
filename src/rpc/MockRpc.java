
package rpc;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.midfield_system.api.system.rpc.RpcRequest;
import com.midfield_system.api.system.rpc.RpcResponse;
import com.midfield_system.api.util.Constants;
import com.midfield_system.api.util.Log;

import util.ConsolePrinter;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: MockRpc
 *
 * Date Modified: 2021.11.30
 *
 */
public class MockRpc
{
// =============================================================================
// CLASS METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static void main(String[] args)
    {
        ConsolePrinter printer = ConsolePrinter.getInstance();
        Log.setLogPrinter(printer);
        
        boolean isPrettyPrinting = true;
        MockRpc mockRpc          = new MockRpc(isPrettyPrinting);
        mockRpc.invokeMockRpcMethods();
    }
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final boolean isPrettyPrinting;
    private final Gson    gson;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    MockRpc(boolean isPrettyPrinting)
    {
        this.isPrettyPrinting = isPrettyPrinting;
        GsonBuilder builder = new GsonBuilder();
        if (this.isPrettyPrinting) {
            builder.setPrettyPrinting();
        }
        this.gson = builder.create();
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void invokeMockRpcMethods()
    {
        exampleSimpleRpc();
        exampleErrorResponse();
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
        
        // クライアント：RPC要求を生成する．
        RpcRequest request = RpcRequest.createRequest("Ex.getBooleanResult", null);
        
        // クライアント：RPC要求をJSON形式にシリアライズする．
        // (これをサーバへ送信する．)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // サーバ：JSON形式からRPC要求へデシリアライズする．
        request = this.gson.fromJson(jsonRequest, RpcRequest.class);
        
        // サーバ：RPC要求を処理してRPC応答を生成する．
        RpcResponse response = RpcResponse.booleanResponse(request, true);
        
        // サーバ：RPC応答をJSON形式にシリアライズする．
        // (これをクライアントへ送信する．)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // クライアント：JSON形式からRPC応答へデシリアライズする．
        response = this.gson.fromJson(jsonResponse, RpcResponse.class);
        
        // クライアント：RPC応答から結果を取得する．
        String result = (String)response.getResult();
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleErrorResponse()
    {
        // ■エラー発生時の例：
        printTitle("エラー発生時の例：");
        
        // クライアント：RPC要求を生成する．
        RpcRequest request = RpcRequest.createRequest("Ex.getMethodNotFound", null);
        
        // クライアント：RPC要求をJSON形式にシリアライズする．
        // (これをサーバへ送信する．)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // サーバ：JSON形式からRPC要求へデシリアライズする．
        request = this.gson.fromJson(jsonRequest, RpcRequest.class);
        
        // サーバ：RPC要求を処理してRPC応答を生成する．
        RpcResponse response = RpcResponse.methodNotFound(request);
        
        // サーバ：RPC応答をJSON形式にシリアライズする．
        // (これをクライアントへ送信する．)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // クライアント：JSON形式からRPC応答へデシリアライズする．
        response = this.gson.fromJson(jsonResponse, RpcResponse.class);
        
        // クライアント：RPC応答から結果を取得する．
        String result = (String)response.getResult();
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleMethodWithStringParam()
    {
        // ■文字列をパラメータとするRPC要求と応答の例：
        printTitle("文字列をパラメータとするRPC要求と応答の例：");
        
        // クライアント：パラメータ付きのRPC要求(エコー要求)を生成する．
        RpcRequest request = RpcRequest.createRequest("Ex.echo", "hello");
        
        // クライアント：RPC要求をJSON形式にシリアライズする．
        // (これをサーバへ送信する．)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // サーバ：JSON形式からRPC要求へデシリアライズする．
        request = this.gson.fromJson(jsonRequest, RpcRequest.class);
        
        // サーバ：RPC要求内のパラメータ(エコー文字列)を取得する．
        String echoString = (String)request.getParams();
        
        // サーバ：エコー文字列を結果とするRPC応答を生成する．
        RpcResponse response = RpcResponse.responseWithResult(request, echoString);
        
        // サーバ：RPC応答をJSON形式にシリアライズする．
        // (これをクライアントへ送信する．)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // クライアント：JSON形式からRPC応答へデシリアライズする．
        response = this.gson.fromJson(jsonResponse, RpcResponse.class);
        
        // クライアント：RPC応答から結果を取得する．
        String result = (String)response.getResult();
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    @SuppressWarnings("unchecked")
    private void exampleMethodWithMapParam()
    {
        // ■複数のパラメータを伴うRPC要求と応答の例：
        printTitle("複数のパラメータを伴うRPC要求と応答の例：");
        
        // クライアント：RPCの引数となるマップを生成する．
        int productNo = 1;
        int quantity  = 20;
        
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("productNo", Integer.toString(productNo));
        params.put("quantity", Integer.toString(quantity));
        
        // クライアント：複数のパラメータを伴うRPC要求を生成する．
        RpcRequest request = RpcRequest.createRequest("Ex.orderProducts", params);
        
        // クライアント：RPC要求をJSON形式にシリアライズする．
        // (これをサーバへ送信する．)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // サーバ：JSON形式からRPC要求へデシリアライズする．
        request = this.gson.fromJson(jsonRequest, RpcRequest.class);
        
        // サーバ：RPC要求内のパラメータを取得する．
        params = (Map<String, Object>)request.getParams();
        
        // サーバ：RPC応答の結果を生成する．
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        productNo = Integer.parseInt((String)params.get("productNo"));
        quantity = Integer.parseInt((String)params.get("quantity"));
        int unitPrice  = 100;
        int totalPrice = quantity * unitPrice;
        
        result.put("productNo", Integer.toString(productNo));
        result.put("quantity", Integer.toString(quantity));
        result.put("unitPrice", Integer.toString(unitPrice));
        result.put("totalPrice", Integer.toString(totalPrice));
        
        // サーバ：結果を含むRPC応答を生成する．
        RpcResponse response = RpcResponse.responseWithResult(request, result);
        
        // サーバ：RPC応答をJSON形式にシリアライズする．
        // (これをクライアントへ送信する．)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // クライアント：JSON形式からRPC応答へデシリアライズする．
        response = this.gson.fromJson(jsonResponse, RpcResponse.class);
        
        // クライアント：RPC応答から結果を取得する．
        result = (Map<String, Object>)response.getResult();
        
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
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleNotification()
    {
        // ■RPC要求(Notification)の例：
        printTitle("Notification の例：");
        
        // クライアント：RPC要求(Notification)を生成する．
        RpcRequest request = RpcRequest.createNotification("Ex.notification", null);
        
        // クライアント：RPC要求をJSON形式にシリアライズする．
        // (これをサーバへ送信する．)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // サーバ：JSON形式からRPC要求へデシリアライズする．
        request = this.gson.fromJson(jsonRequest, RpcRequest.class);
        
        // サーバ：RPC要求(Notification)を処理する．
        // (ここでは何もしない．Notification に対しては応答しない．)
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
    private void printJsonString(String jsonString)
    {
        if (this.isPrettyPrinting) {
            Log.message(Constants.STR_NEW_LINE + jsonString);
        }
        else {
            Log.message(jsonString);
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void printResult(Object result)
    {
        Log.message("△result: %s", result);
        Log.message();
    }
}
