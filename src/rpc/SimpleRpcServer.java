
package rpc;

import com.midfield_system.api.system.rpc.Registerable;
import com.midfield_system.api.system.rpc.RegisterableArrayFactory;
import com.midfield_system.api.system.rpc.RpcServer;
import com.midfield_system.api.util.Log;

import util.ConsolePrinter;
import util.ConsoleReader;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: SimpleRpcServer
 *
 * Date Modified: 2021.09.06
 *
 */

//==============================================================================
public class SimpleRpcServer
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String SERVER_NAME = null; // ���C���h�J�[�h�A�h���X
	private static final int PORT_NUMBER = 60202;
	private static final int MAX_IDLE_TIME = 600 * 1000;	// [ms]
	
	// RegisterableArrayFactory �̗�
	private static final RegisterableArrayFactory FACTORY = () -> {
		// RPC�v���������\�b�h���܂� Registerable �̔z��𐶐����ĕԂ��D
		Registerable example = new ServerMethodExample();
		Registerable[] registerableArray = {
			example
		};
		return registerableArray;
	};
	
//==============================================================================
//  CLASS METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC STATIC METHOD ---------------------------------------------------
	//
	public static void main(String[] args)
	{
		ConsoleReader reader = ConsoleReader.getInstance();
		ConsolePrinter printer = ConsolePrinter.getInstance();
		Log.setLogPrinter(printer);
		
		RpcServer rpcServer = null;
		try {
			printer.println("> SimpleRpcServer: RpcServer �̏������J�n���܂��D");
			printer.println("> SimpleRpcServer: �I������ۂ� Enter �L�[�������Ă��������D");
			printer.println("");
			
			// RpcServer �𐶐�����D
			rpcServer = new RpcServer(
				SERVER_NAME,	// RPC�T�[�o���܂���IP�A�h���X
				PORT_NUMBER,	// RPC�T�[�o�̃|�[�g�ԍ�
				MAX_IDLE_TIME,	// �R�l�N�V�������ɋ��e����ő�̃A�C�h������
				false,			// JSON�I�u�W�F�N�g(������)�𐮌`���邩�ۂ�
				true,			// JSON�I�u�W�F�N�g(������)�����O�o�͂��邩�ۂ�
				FACTORY			// RegisterableArrayFactory �̃C���X�^���X
			);	// UnknownHostException
			
			// RpcServer �̏������J�n����D
			rpcServer.open();
			
			// �R���\�[������̓��͂�҂D
			reader.readLine();
		}
		catch (Exception ex) {
			// RpcServer �̓��쒆�ɗ�O�����������D
			printer.println("> SimpleRpcServer: RpcServer �̎��s���ɗ�O���������܂����D");
			ex.printStackTrace();
		}
		finally {
			// RpcServer �̏������I������D
			if (rpcServer != null) {
				rpcServer.close();
			}
			// ConsoleReader ���������D
			reader.release();
			
			printer.println("");
			printer.println("> SimpleRpcServer: RpcServer �̏������I�����܂��D");
		}
	}
}
