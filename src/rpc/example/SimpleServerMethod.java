
package rpc.example;

import com.midfield_system.json_rpc.api.Registerable;
import com.midfield_system.json_rpc.api.RequestObject;
import com.midfield_system.json_rpc.api.ResponseObject;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.25
 *
 */
public class SimpleServerMethod
    implements
        Registerable
{
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
    
    @Override
    public String getMethodPrefix()
    {
        return "Simple";
    }
}