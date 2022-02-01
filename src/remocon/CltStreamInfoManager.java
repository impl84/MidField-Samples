
package remocon;

import static com.midfield_system.api.system.rpc.ParamName.SOURCE_NODE_ADDRESS;

import java.util.HashMap;
import java.util.Map;

import com.midfield_system.api.system.rpc.RpcRequest;

/*----------------------------------------------------------------------------*/
/**
 * CltStreamInfoManager
 *
 * Copyright (C) Koji Hashimoto
 *
 * Date Modified: 2021.09.02 Koji Hashimoto
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
    public RpcRequest fetchSourceStreamInfoList(String[] args)
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
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], map);
        return rpcReq;
    }
}