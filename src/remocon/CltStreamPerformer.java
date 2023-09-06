
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
        // RPC�v���𐶐����ĕԂ��D
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest open(String[] args)
    {
        // open �R�}���h�v���𐶐����ĕԂ��D
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest start(String[] args)
    {
        // start �R�}���h�v���𐶐����ĕԂ��D
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest stop(String[] args)
    {
        // stop �R�}���h�v���𐶐����ĕԂ��D
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest close(String[] args)
    {
        // close �R�}���h�v���𐶐����ĕԂ��D
        return newRpcCommandRequest(args);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RpcRequest delete(String[] args)
    {
        // delete �R�}���h�v���𐶐����ĕԂ��D
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
        
        // RPC�v���ɕK�v�ƂȂ�����̐����m�F����D
        if (args.length >= 2) {
            // RPC�v���p�̃}�b�v�𐶐�����D
            map = new TreeMap<String, Object>();
            
            // StreamPerformer �̃C���f�b�N�X�ԍ����}�b�v�ɐݒ肷��D
            map.put(PERFORMER_INDEX, args[1]);
        }
        // RPC�v���𐶐����ĕԂ��D
        RpcRequest rpcReq = RpcRequest.createRequest(args[0], map);
        return rpcReq;
    }
    
}