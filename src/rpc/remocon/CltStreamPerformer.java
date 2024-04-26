
package rpc.remocon;

import static com.midfield_system.rpc.api.server.ParamName.PERFORMER_ID;

import java.util.Map;
import java.util.TreeMap;

import com.midfield_system.rpc.api.RequestObject;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.07
 *
 */
public class CltStreamPerformer
{
// =============================================================================
// INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public CltStreamPerformer()
    {
        //
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject newInstance(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject start(String[] args)
    {
        // start コマンド要求を生成して返す．
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject stop(String[] args)
    {
        // stop コマンド要求を生成して返す．
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject delete(String[] args)
    {
        // delete コマンド要求を生成して返す．
        return newRpcCommandRequest(args);
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private RequestObject newRpcCommandRequest(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 2) {
            // RPC要求用のマップを生成する．
            map = new TreeMap<String, Object>();
            
            // StreamPerformer のインデックス番号をマップに設定する．
            map.put(PERFORMER_ID, args[1]);
        }
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
    
}