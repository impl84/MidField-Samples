
package remocon;

import static com.midfield_system.api.rpc.ParamName.PERFORMER_INDEX;

import java.util.Map;
import java.util.TreeMap;

import com.midfield_system.api.rpc.RpcRequest;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: CltStreamPerformer
 * 
 * Date Modified: 2022.08.18
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
    public RpcRequest newInstance(String[] args)
    {
        // RPC要求を生成して返す．
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest open(String[] args)
    {
        // open コマンド要求を生成して返す．
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest start(String[] args)
    {
        // start コマンド要求を生成して返す．
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest stop(String[] args)
    {
        // stop コマンド要求を生成して返す．
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest close(String[] args)
    {
        // close コマンド要求を生成して返す．
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest delete(String[] args)
    {
        // delete コマンド要求を生成して返す．
        return newRpcCommandRequest(args);
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private RpcRequest newRpcCommandRequest(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 2) {
            // RPC要求用のマップを生成する．
            map = new TreeMap<String, Object>();
            
            // StreamPerformer のインデックス番号をマップに設定する．
            map.put(PERFORMER_INDEX, args[1]);
        }
        // RPC要求を生成して返す．
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], map);
        return rpcReq;
    }
    
}