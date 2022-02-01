
package remocon;

import com.midfield_system.api.system.rpc.RpcRequest;

/*----------------------------------------------------------------------------*/
/**
 * CltMfsNode
 *
 * Copyright (C) Koji Hashimoto
 *
 * Date Modified: 2021.09.03 Koji Hashimoto
 *
 */
public class CltMfsNode
{
// =============================================================================
// INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public CltMfsNode()
    {
        //
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest isRunning(String[] args)
    {
        // RPC要求を生成して返す．
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest lock(String[] args)
    {
        // RPC要求を生成して返す．
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest unlock(String[] args)
    {
        // RPC要求を生成して返す．
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], null);
        return rpcReq;
    }
}
