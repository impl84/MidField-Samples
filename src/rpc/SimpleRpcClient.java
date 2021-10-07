
package rpc;

import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_CODE;
import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_DATA;
import static com.midfield_system.api.system.rpc.JsonRpcConstants.ERR_MESSAGE;

import java.util.Map;

import com.midfield_system.api.system.rpc.ErrorResponseHandler;
import com.midfield_system.api.system.rpc.RpcClient;
import com.midfield_system.api.util.Log;

import util.ConsolePrinter;
import util.ConsoleReader;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: SimpleRpcClient
 *
 * Date Modified: 2021.09.06
 *
 */

//==============================================================================
public class SimpleRpcClient
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String SERVER_NAME = "127.0.0.1";
	private static final int PORT_NUMBER = 60202;
	
	// ErrorResponseHandler �̗�
	private static final ErrorResponseHandler HANDLER = (reason, response, ex) -> {
		// RPC�����������ɔ��������G���[�����o�͂���D
		Log.warning(reason);
		if (response != null) {
			Map<String, Object> error = response.getError();
			Log.warning("error: code: %s, message: %s, data: %s",
				error.get(ERR_CODE), error.get(ERR_MESSAGE), error.get(ERR_DATA)
			);
		}
		if (ex != null) {
			Log.warning(ex);
		}
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
		
		RpcClient rpcClient = null;
		try {
			printer.println("> SimpleRpcClient: RpcClient �̏������J�n���܂��D");
			printer.println("> SimpleRpcClient: �I������ۂ� Enter �L�[�������Ă��������D");
			printer.println("");
			
			// RpcClient �𐶐�����D
			rpcClient = new RpcClient(
				SERVER_NAME,	// RPC�T�[�o���܂���IP�A�h���X
				PORT_NUMBER,	// RPC�T�[�o�̃|�[�g�ԍ�
				false,			// JSON�I�u�W�F�N�g(������)�𐮌`���邩�ۂ�
				true,			// JSON�I�u�W�F�N�g(������)�����O�o�͂��邩�ۂ�
				HANDLER			// ErrorResponseHandler �̃C���X�^���X
			);	// UnknownHostException
			
			// RpcClient �̏������J�n����D
			rpcClient.open();
				// IOException
			
			// �N���C�A���g���̃��\�b�h�̗�����s����D
			ClientMethodExample example = new ClientMethodExample(rpcClient);
			example.invokeRpcMethods();
			
			// �R���\�[������̓��͂�҂D
			reader.readLine();
		}
		catch (Exception ex) {
			// RpcClient �̓��쒆�ɗ�O�����������D
			printer.println("> SimpleRpcClient: RpcClient �̎��s���ɗ�O���������܂����D");
			ex.printStackTrace();
		}
		finally {
			// RpcClient �̏������I������D
			if (rpcClient != null) {
				rpcClient.close();
			}
			// ConsoleReader ���������D
			reader.release();
			
			printer.println("");
			printer.println("> SimpleRpcClient: RpcClient �̏������I�����܂��D");
		}
	}
}
