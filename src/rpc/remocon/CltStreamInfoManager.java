
package rpc.remocon;

import static com.midfield_system.json_rpc.server.ParamName.SOURCE_NODE_ADDRESS;

import java.util.HashMap;
import java.util.Map;

import com.midfield_system.json_rpc.api.RequestObject;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2022.06.08
 *
 */
public class CltStreamInfoManager
{
// =============================================================================
// INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public CltStreamInfoManager()
    {
        //
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject fetchSourceStreamInfoList(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 2) {
            // RPC要求用のマップを生成する．
            map = new HashMap<String, Object>();
            
            // ソースノードアドレスをマップに設定する．
            map.put(SOURCE_NODE_ADDRESS, args[1]);
        }
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
}