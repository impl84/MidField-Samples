
package rpc.example;

import java.util.LinkedHashMap;
import java.util.Map;

import com.midfield_system.rpc.api.Registerable;
import com.midfield_system.rpc.api.RequestObject;
import com.midfield_system.rpc.api.ResponseObject;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.24
 *
 */
public class DemoServerMethod
    implements
        Registerable
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String METHOD_PREFIX = "Demo";
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC要求処理の例
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getBooleanResult(RequestObject request)
    {
        boolean result = true;
        
        // Boolean 値を結果とするRPC応答を生成する．
        ResponseObject response = ResponseObject.booleanResponse(
            request, result
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getSuccessResult(RequestObject request)
    {
        boolean isSuccess = true;
        
        // "success" (または "failure") を結果とするRPC応答を生成する．
        ResponseObject response = ResponseObject.successResponse(
            request, isSuccess
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getResponseWithResult(RequestObject request)
    {
        Object result = new String("Any resulting object.");
        
        // 何らかのオブジェクトを結果とするRPC応答を生成する．
        ResponseObject response = ResponseObject.responseWithResult(
            request, result
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getParseError(RequestObject request)
    {
        String data = new String(
            "Invalid JSON was received by the server."
                + "An error occurred on the server while parsing the JSON text."
        );
        // 解析エラー発生時のRPC応答を生成する．
        ResponseObject response = ResponseObject.parseError(
            data
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getInvalidRequest(RequestObject request)
    {
        // 無効なリクエストを受信した場合のRPC応答を生成する．
        ResponseObject response = ResponseObject.invalidRequest(
            request
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getMethodNotFound(RequestObject request)
    {
        // メソッドが存在しない/利用できない場合のRPC応答を生成する．
        ResponseObject response = ResponseObject.methodNotFound(
            request
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getInvalidParams(RequestObject request)
    {
        String data = new String("Invalid method parameter(s).");
        
        // メソッドのパラメータが無効な場合のRPC応答を生成する．
        ResponseObject response = ResponseObject.invalidParams(
            request, data
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getInternalError(RequestObject request)
    {
        String data = new String("Internal JSON-RPC error.");
        
        // 内部エラーが発生した場合のRPC応答を生成する．
        ResponseObject response = ResponseObject.internalError(
            request, data
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getServerError(RequestObject request)
    {
        String data = new String("Reserved for implementation-defined server-errors.");
        
        // サーバ側で何らかのエラーが発生した場合のRPC応答を生成する．
        ResponseObject response = ResponseObject.serverError(
            request, data
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject echo(RequestObject request)
    {
        // RPC要求内のパラメータ(エコー文字列)を取得する．
        String echoString = (String)request.getParams();
        
        // エコー文字列を結果とするRPC応答を生成する．
        ResponseObject response = ResponseObject.responseWithResult(
            request, echoString
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject orderProducts(RequestObject request)
    {
        // RPC要求内のパラメータを取得する．
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>)request.getParams();
        
        // RPC応答の結果を生成する．
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        
        int productNo  = Integer.parseInt((String)params.get("productNo"));
        int quantity   = Integer.parseInt((String)params.get("quantity"));
        int unitPrice  = 100;
        int totalPrice = quantity * unitPrice;
        
        result.put("productNo", Integer.toString(productNo));
        result.put("quantity", Integer.toString(quantity));
        result.put("unitPrice", Integer.toString(unitPrice));
        result.put("totalPrice", Integer.toString(totalPrice));
        
        // 結果を含むRPC応答を生成する．
        ResponseObject response = ResponseObject.responseWithResult(
            request, result
        );
        // RPC応答を返す．
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject notification(RequestObject request)
    {
        // Notification の場合は，null を返す．
        ResponseObject response = null;
        return response;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: IMPLEMENTS: Registerable
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: Registerable
    //
    @Override
    public String getMethodPrefix()
    {
        return METHOD_PREFIX;
    }
}
