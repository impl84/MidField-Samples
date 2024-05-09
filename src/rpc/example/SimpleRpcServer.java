
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
public class SimpleRpcServer
{
    public static void main(String[] args)
    {
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        RpcServer rpcServer = null;
        try {
            Log.message("> SimpleRpcServer: RpcServer �̏������J�n���܂��D");
            
            // RpcServer �𐶐�����D
            // (UnknownHostException)
            rpcServer = new RpcServer(
                null,       // RPC�T�[�o���܂���IP�A�h���X(���C���h�J�[�h�A�h���X)
                60202,      // RPC�T�[�o�̃|�[�g�ԍ�
                600 * 1000, // �R�l�N�V�������ɋ��e����ő�̃A�C�h������(msec)
                false,      // JSON�I�u�W�F�N�g(������)�𐮌`���邩�ۂ�
                true,       // JSON�I�u�W�F�N�g(������)�����O�o�͂��邩�ۂ�
                factory     // ServerMethodFactory �̎���
            );
            // RpcServer �̏������J�n����D
            rpcServer.open();
            
            // �R���\�[������̓��͂�҂D
            Log.message("> SimpleRpcServer: �I������ۂ� Enter �L�[�������Ă��������D");
            System.in.read();
        }
        catch (Exception ex) {
            // RpcServer �̓��쒆�ɗ�O�����������D
            Log.message("> SimpleRpcServer: RpcServer �̎��s���ɗ�O���������܂����D");
            ex.printStackTrace();
        }
        finally {
            // RpcServer �̏������I������D
            if (rpcServer != null) {
                Log.message("> SimpleRpcServer: RpcServer �̏������I�����܂��D");
                rpcServer.close();
            }
        }
    }
    
    // RegisterableArrayFactory�FServerMethodFactory �N���X�Ƃ��Ă̎���
    static RegisterableArrayFactory factory = new ServerMethodFactory();
    
    static class ServerMethodFactory
        implements
            RegisterableArrayFactory
    {
        @Override
        public Registerable[] createRegisterableArray()
        {
            Registerable[] registerableArray = new Registerable[] {
                new SimpleServerMethod()
            };
            return registerableArray;
        }
    }
    
    // ���Q�l�FRegisterableArrayFactory �̎����i�����N���X�j
    static RegisterableArrayFactory factory_ex0 = new RegisterableArrayFactory()
    {
        @Override
        public Registerable[] createRegisterableArray()
        {
            return new Registerable[] {new SimpleServerMethod()};
        }
    };
    
    // ���Q�l�FRegisterableArrayFactory �̎����i�����_���j
    static RegisterableArrayFactory factory_ex1 = () -> { return new Registerable[] {new SimpleServerMethod()}; };
    
    // ���Q�l�FRegisterableArrayFactory �̎����i�\�Ȍ���ȗ����������_���j
    static RegisterableArrayFactory factory_ex2 = () -> new Registerable[] {new SimpleServerMethod()};
    
}
