
package rpc.example;

import com.midfield_system.rpc.api.Registerable;
import com.midfield_system.rpc.api.RequestObject;
import com.midfield_system.rpc.api.ResponseObject;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.25
 *
 */
public class SimpleServerMethod
    implements
        Registerable
{
    public ResponseObject echo(RequestObject request)
    {
        // RPC�v�����̃p�����[�^(�G�R�[������)���擾����D
        String echoString = (String)request.getParams();
        
        // �G�R�[����������ʂƂ���RPC�����𐶐�����D
        ResponseObject response = ResponseObject.responseWithResult(
            request, echoString
        );
        // RPC������Ԃ��D
        return response;
    }
    
    @Override
    public String getMethodPrefix()
    {
        return "Simple";
    }
}