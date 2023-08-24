
package rpc.remocon;

import com.midfield_system.rpc.api.RequestObject;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2022.06.08
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
    public RequestObject isRunning(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject lock(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject unlock(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
}
