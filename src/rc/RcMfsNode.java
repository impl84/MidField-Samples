
package rc;

import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_CODE;
import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_DATA;
import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_MESSAGE;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.midfield_system.api.rpc.ErrorResponseHandler;
import com.midfield_system.api.rpc.RpcClient;
import com.midfield_system.api.rpc.RpcResponse;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcMfsNode
 *
 * Date Modified: 2022.06.09
 *
 */
public class RcMfsNode
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final long RESPONSE_TIMEOUT = 8000;  // [msec]
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final RpcClient rpcClient;
    
    private RcDeviceInfoManager devInfMgr = null;
    private RcStreamInfoManager stmInfMgr = null;
    private RcSegmentIo         segIo     = null;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public RcMfsNode(String serverName, int serverPort, ErrorResponseHandler handler)
        throws UnknownHostException,
            IOException
    {
        // �����ŗ^����ꂽ�T�[�o�Ɛڑ����邽�߂� RpcClient �𐶐�����D
        // (UnknownHostException, IOException)
        rpcClient = new RpcClient(
            serverName,     // RPC�T�[�o���܂���IP�A�h���X
            serverPort,     // RPC�T�[�o�̃|�[�g�ԍ�
            false,          // JSON�I�u�W�F�N�g(������)�𐮌`���邩�ۂ�
            true,           // JSON�I�u�W�F�N�g(������)�����O�o�͂��邩�ۂ�
            handler         // ErrorResponseHandler �̃C���X�^���X
        );
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: 
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void open()
    {
        // RpcClient �̏������J�n����D
        this.rpcClient.open();
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void close()
    {
        // RpcClient �̏������I������D
        this.rpcClient.close();
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: 
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RcDeviceInfoManager getRcDeviceInfoManager()
    {
        if (this.devInfMgr == null) {
            this.devInfMgr = new RcDeviceInfoManager(this);
        }
        return this.devInfMgr;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RcStreamInfoManager getRcStreamInfoManager()
    {
        if (this.stmInfMgr == null) {
            this.stmInfMgr = new RcStreamInfoManager(this);
        }
        return this.stmInfMgr;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RcSegmentIo newRcSegmentIo()
        throws RemoteControlException
    {
        if (this.segIo == null) {
            this.segIo = new RcSegmentIo(this);
        }
        this.segIo.reset();
        
        return this.segIo;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RcStreamPerformer newRcStreamPerformer(RcSegmentIo segIo)
        throws RemoteControlException
    {
        RcStreamPerformer pfmr = new RcStreamPerformer(this);
        
        return pfmr;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public boolean isRunning()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        Object result = invoke("MfsNode.isRunning", null);
        
        // ���ʂ� boolean �ɕϊ����ĕԂ��D
        return Boolean.parseBoolean((String)result);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String lock()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        Object result = invoke("MfsNode.lock", null);
        
        // ���ʂ� String �ɕϊ����ĕԂ��D
        return (String)result;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public String unlock()
        throws RemoteControlException
    {
        // �Ή����鉓�u���\�b�h���Ăяo���D
        // (RemoteControlException)
        Object result = invoke("MfsNode.unlock", null);
        
        // ���ʂ� String �ɕϊ����ĕԂ��D
        return (String)result;
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    Object invoke(String method, Object params)
        throws RemoteControlException
    {
        Object result = null;
        try {
            // ���u���\�b�h���Ăяo���CRPC�������擾����D
            // (InterruptedException, ExecutionException, TimeoutException)
            RpcResponse response = this.rpcClient.invoke(
                method, params, RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS
            );
            // ���ʂ� null ���ǂ����m�F����D
            result = response.getResult();
            if (result == null) {
                // ���ʂ� null �̏ꍇ�́C�G���[�����擾����D
                Map<String, Object> error  = response.getError();
                String              errMsg = String.format(
                    "error: code: %s, message: %s, data: %s",
                    error.get(ERR_CODE), error.get(ERR_MESSAGE), error.get(ERR_DATA)
                );
                // RemoteControlException �𐶐����ē�����D
                throw new RemoteControlException(errMsg);
            }
        }
        catch (InterruptedException | ExecutionException | TimeoutException cause) {
            // ���u���\�b�h�Ăяo�����ɗ�O�����������ꍇ�C
            // ���̗�O�������Ƃ��� RemoteControlException �𐶐����ē�����D
            throw new RemoteControlException(
                "���u���\�b�h�Ăяo�����ɗ�O���������܂����D", cause
            );
        }
        return result;
    }
}
