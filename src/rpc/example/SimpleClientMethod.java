
package rpc.example;

import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.ErrorObject;
import com.midfield_system.rpc.api.ResponseObject;
import com.midfield_system.rpc.api.RpcClient;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.10.31
 *
 */
public class SimpleClientMethod
{
    private final RpcClient rpcClient;
    
    SimpleClientMethod(RpcClient rpcClient)
    {
        this.rpcClient = rpcClient;
    }
    
    String echo(String echoString)
    {
        Object result = null;
        try {
            // �T�[�o�̃��\�b�h���Ăяo���CRPC�������擾����D
            // (InterruptedException, ExecutionException, TimeoutException)
            ResponseObject response = this.rpcClient.invoke(
                "Simple.echo",  // �Ăяo�����\�b�h��
                echoString,     // ���\�b�h�̈���
                2000            // �����擾�܂ł̃^�C���A�E�g����
            );
            // ���ʂ� null �̏ꍇ�̓G���[�����o�͂���D
            result = response.getResult();
            if (result == null) {
                ErrorObject error = response.getError();
                Log.message("error: %s", error);
            }
        }
        catch (Exception ex) {
            Log.error(ex);
        }
        return (String)result;
    }
}
