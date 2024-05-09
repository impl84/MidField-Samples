
package rpc.example;

import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.InternalErrorListener;
import com.midfield_system.rpc.api.RpcClient;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.25
 *
 */
public class DemoRpcClient
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String SERVER_NAME = "127.0.0.1";
    private static final int    SERVER_PORT = 60202;
    
// =============================================================================
// CLASS METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static void main(String[] args)
    {
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RpcClient rpcClient = null;
        try {
            Log.message("> DemoRpcClient: RpcClient �̏������J�n���܂��D");
            
            // InternalErrorListener �̃C���X�^���X
            InternalErrorListener listener = error -> Log.error(error.toString());
            
            // RpcClient �𐶐�����D
            // (UnknownHostException, IOException)
            rpcClient = new RpcClient(
                SERVER_NAME,    // RPC�T�[�o���܂���IP�A�h���X
                SERVER_PORT,    // RPC�T�[�o�̃|�[�g�ԍ�
                false,          // JSON�I�u�W�F�N�g(������)�𐮌`���邩�ۂ�
                true,           // JSON�I�u�W�F�N�g(������)�����O�o�͂��邩�ۂ�
                listener        // InternalErrorListener
            );
            // RpcClient �̏������J�n����D
            rpcClient.open();
            
            // �N���C�A���g���̃��\�b�h�̗�����s����D
            DemoClientMethod clientMethod = new DemoClientMethod(rpcClient);
            clientMethod.invokeAll();
        }
        catch (Exception ex) {
            // RpcClient �̓��쒆�ɗ�O�����������D
            Log.message("> DemoRpcClient: RpcClient �̎��s���ɗ�O���������܂����D");
            ex.printStackTrace();
        }
        finally {
            // RpcClient �̏������I������D
            if (rpcClient != null) {
                Log.message("> DemoRpcClient: RpcClient �̏������I�����܂��D");
                rpcClient.close();
            }
        }
    }
}
