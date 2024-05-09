
package rpc.example;

import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.Registerable;
import com.midfield_system.rpc.api.RegisterableArrayFactory;
import com.midfield_system.rpc.api.RpcServer;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.25
 *
 */
public class DemoRpcServer
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String SERVER_NAME   = null;       // ���C���h�J�[�h�A�h���X
    private static final int    PORT_NUMBER   = 60202;
    private static final int    MAX_IDLE_TIME = 600 * 1000; // [ms]
    
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
        
        RpcServer rpcServer = null;
        try {
            Log.message("> DemoRpcServer: RpcServer �̏������J�n���܂��D");
            
            // RegisterableArrayFactory �̃C���X�^���X
            RegisterableArrayFactory factory = () -> new Registerable[] {new DemoServerMethod()};
            
            // RpcServer �𐶐�����D
            // (UnknownHostException)
            rpcServer = new RpcServer(
                SERVER_NAME,    // RPC�T�[�o���܂���IP�A�h���X
                PORT_NUMBER,    // RPC�T�[�o�̃|�[�g�ԍ�
                MAX_IDLE_TIME,  // �R�l�N�V�������ɋ��e����ő�̃A�C�h������
                false,          // JSON�I�u�W�F�N�g(������)�𐮌`���邩�ۂ�
                true,           // JSON�I�u�W�F�N�g(������)�����O�o�͂��邩�ۂ�
                factory         // RegisterableArrayFactory
            );
            // RpcServer �̏������J�n����D
            rpcServer.open();
            
            // �R���\�[������̓��͂�҂D
            Log.message("> DemoRpcServer: �I������ۂ� Enter �L�[�������Ă��������D");
            System.in.read();
        }
        catch (Exception ex) {
            // RpcServer �̓��쒆�ɗ�O�����������D
            Log.message("> DemoRpcServer: RpcServer �̎��s���ɗ�O���������܂����D");
            ex.printStackTrace();
        }
        finally {
            // RpcServer �̏������I������D
            if (rpcServer != null) {
                Log.message("> DemoRpcServer: RpcServer �̏������I�����܂��D");
                rpcServer.close();
            }
        }
    }
}
