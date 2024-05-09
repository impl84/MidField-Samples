
package rpc.example;

import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.InternalError;
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
public class SimpleRpcClient
{
    public static void main(String[] args)
    {
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RpcClient rpcClient = null;
        try {
            Log.message("> SimpleRpcClient: RpcClient �̏������J�n���܂��D");
            
            // RpcClient �𐶐�����D
            // (UnknownHostException, IOException)
            rpcClient = new RpcClient(
                "127.0.0.1",    // RPC�T�[�o���܂���IP�A�h���X(���[�v�o�b�N�A�h���X)
                60202,          // RPC�T�[�o�̃|�[�g�ԍ�
                false,          // JSON�I�u�W�F�N�g(������)�𐮌`���邩�ۂ�
                true,           // JSON�I�u�W�F�N�g(������)�����O�o�͂��邩�ۂ�
                listener        // InternalErrorListener �̎���
            );
            // RpcClient �̏������J�n����D
            rpcClient.open();
            
            // ���\�b�h���Ăяo���D
            SimpleClientMethod clientMethod = new SimpleClientMethod(rpcClient);
            String             result       = clientMethod.echo("hello");
            Log.message("> SimpleRpcClient: echo ���\�b�h�̌��ʁF" + result);
        }
        catch (Exception ex) {
            // RpcClient �̓��쒆�ɗ�O�����������D
            Log.message("> SimpleRpcClient: RpcClient �̎��s���ɗ�O���������܂����D");
            ex.printStackTrace();
        }
        finally {
            // RpcClient �̏������I������D
            if (rpcClient != null) {
                Log.message("> SimpleRpcClient: RpcClient �̏������I�����܂��D");
                rpcClient.close();
            }
        }
    }
    
    // InternalErrorListener�FClientErrorListener �N���X�Ƃ��Ă̎���
    static InternalErrorListener listener = new ClientErrorListener();
    
    static class ClientErrorListener
        implements
            InternalErrorListener
    {
        @Override
        public void onInternalError(InternalError error)
        {
            Log.error(error.toString());
        }
    }
    
    // ���Q�l�FInternalErrorListener �̎����i�����N���X�j    
    static InternalErrorListener listener_ex0 = new InternalErrorListener()
    {
        @Override
        public void onInternalError(InternalError error)
        {
            Log.error(error.toString());
        }
    };
    
    // ���Q�l�FInternalErrorListener �̎����i�����_���j
    static InternalErrorListener listener_ex1 = (error) -> { Log.error(error.toString()); };
    
    // ���Q�l�FInternalErrorListener �̎����i�\�Ȍ���ȗ����������_���j
    static InternalErrorListener listener_ex2 = error -> Log.error(error.toString());
}
