
package rpc.example;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.midfield_system.api.log.Log;
import com.midfield_system.api.util.Constants;
import com.midfield_system.json_rpc.api.RequestObject;
import com.midfield_system.json_rpc.api.ResponseObject;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.24
 *
 */
public class MockRpcExample
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
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MockRpcExample example = new MockRpcExample();
        example.invokeAll();
    }
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final Gson gson;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    MockRpcExample()
    {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void invokeAll()
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
        RequestObject request = RequestObject.createRequest("Mock.getBooleanResult", null);
        
        // クライアント：RPC要求をJSON形式にシリアライズする．
        // (これをサーバへ送信する．)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // サーバ：JSON形式からRPC要求へデシリアライズする．
        request = this.gson.fromJson(jsonRequest, RequestObject.class);
        
        // サーバ：RPC要求を処理してRPC応答を生成する．
        ResponseObject response = ResponseObject.booleanResponse(request, true);
        
        // サーバ：RPC応答をJSON形式にシリアライズする．
        // (これをクライアントへ送信する．)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // クライアント：JSON形式からRPC応答へデシリアライズする．
        response = this.gson.fromJson(jsonResponse, ResponseObject.class);
        
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
        RequestObject request = RequestObject.createRequest("Mock.getMethodNotFound", null);
        
        // クライアント：RPC要求をJSON形式にシリアライズする．
        // (これをサーバへ送信する．)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // サーバ：JSON形式からRPC要求へデシリアライズする．
        request = this.gson.fromJson(jsonRequest, RequestObject.class);
        
        // サーバ：RPC要求を処理してRPC応答を生成する．
        ResponseObject response = ResponseObject.methodNotFound(request);
        
        // サーバ：RPC応答をJSON形式にシリアライズする．
        // (これをクライアントへ送信する．)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // クライアント：JSON形式からRPC応答へデシリアライズする．
        response = this.gson.fromJson(jsonResponse, ResponseObject.class);
        
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
        RequestObject request = RequestObject.createRequest("Mock.echo", "hello");
        
        // クライアント：RPC要求をJSON形式にシリアライズする．
        // (これをサーバへ送信する．)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // サーバ：JSON形式からRPC要求へデシリアライズする．
        request = this.gson.fromJson(jsonRequest, RequestObject.class);
        
        // サーバ：RPC要求内のパラメータ(エコー文字列)を取得する．
        String echoString = (String)request.getParams();
        
        // サーバ：エコー文字列を結果とするRPC応答を生成する．
        ResponseObject response = ResponseObject.responseWithResult(request, echoString);
        
        // サーバ：RPC応答をJSON形式にシリアライズする．
        // (これをクライアントへ送信する．)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // クライアント：JSON形式からRPC応答へデシリアライズする．
        response = this.gson.fromJson(jsonResponse, ResponseObject.class);
        
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
        RequestObject request = RequestObject.createRequest("Mock.orderProducts", params);
        
        // クライアント：RPC要求をJSON形式にシリアライズする．
        // (これをサーバへ送信する．)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // サーバ：JSON形式からRPC要求へデシリアライズする．
        request = this.gson.fromJson(jsonRequest, RequestObject.class);
        
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
        ResponseObject response = ResponseObject.responseWithResult(request, result);
        
        // サーバ：RPC応答をJSON形式にシリアライズする．
        // (これをクライアントへ送信する．)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // クライアント：JSON形式からRPC応答へデシリアライズする．
        response = this.gson.fromJson(jsonResponse, ResponseObject.class);
        
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
        RequestObject request = RequestObject.createNotification("Mock.notification", null);
        
        // クライアント：RPC要求をJSON形式にシリアライズする．
        // (これをサーバへ送信する．)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // サーバ：JSON形式からRPC要求へデシリアライズする．
        request = this.gson.fromJson(jsonRequest, RequestObject.class);
        
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
        Log.message(Constants.STR_NEW_LINE + jsonString);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void printResult(Object result)
    {
        Log.message("△result: %s", result);
        Log.message();
    }
}
