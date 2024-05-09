
package rpc.example;

import java.util.LinkedHashMap;
import java.util.Map;

import com.midfield_system.rpc.api.Registerable;
import com.midfield_system.rpc.api.RequestObject;
import com.midfield_system.rpc.api.ResponseObject;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.24
 *
 */
public class DemoServerMethod
    implements
        Registerable
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String METHOD_PREFIX = "Demo";
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC�v�������̗�
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getBooleanResult(RequestObject request)
    {
        boolean result = true;
        
        // Boolean �l�����ʂƂ���RPC�����𐶐�����D
        ResponseObject response = ResponseObject.booleanResponse(
            request, result
        );
        // RPC������Ԃ��D
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getSuccessResult(RequestObject request)
    {
        boolean isSuccess = true;
        
        // "success" (�܂��� "failure") �����ʂƂ���RPC�����𐶐�����D
        ResponseObject response = ResponseObject.successResponse(
            request, isSuccess
        );
        // RPC������Ԃ��D
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getResponseWithResult(RequestObject request)
    {
        Object result = new String("Any resulting object.");
        
        // ���炩�̃I�u�W�F�N�g�����ʂƂ���RPC�����𐶐�����D
        ResponseObject response = ResponseObject.responseWithResult(
            request, result
        );
        // RPC������Ԃ��D
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getParseError(RequestObject request)
    {
        String data = new String(
            "Invalid JSON was received by the server."
                + "An error occurred on the server while parsing the JSON text."
        );
        // ��̓G���[��������RPC�����𐶐�����D
        ResponseObject response = ResponseObject.parseError(
            data
        );
        // RPC������Ԃ��D
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getInvalidRequest(RequestObject request)
    {
        // �����ȃ��N�G�X�g����M�����ꍇ��RPC�����𐶐�����D
        ResponseObject response = ResponseObject.invalidRequest(
            request
        );
        // RPC������Ԃ��D
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getMethodNotFound(RequestObject request)
    {
        // ���\�b�h�����݂��Ȃ�/���p�ł��Ȃ��ꍇ��RPC�����𐶐�����D
        ResponseObject response = ResponseObject.methodNotFound(
            request
        );
        // RPC������Ԃ��D
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getInvalidParams(RequestObject request)
    {
        String data = new String("Invalid method parameter(s).");
        
        // ���\�b�h�̃p�����[�^�������ȏꍇ��RPC�����𐶐�����D
        ResponseObject response = ResponseObject.invalidParams(
            request, data
        );
        // RPC������Ԃ��D
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getInternalError(RequestObject request)
    {
        String data = new String("Internal JSON-RPC error.");
        
        // �����G���[�����������ꍇ��RPC�����𐶐�����D
        ResponseObject response = ResponseObject.internalError(
            request, data
        );
        // RPC������Ԃ��D
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject getServerError(RequestObject request)
    {
        String data = new String("Reserved for implementation-defined server-errors.");
        
        // �T�[�o���ŉ��炩�̃G���[�����������ꍇ��RPC�����𐶐�����D
        ResponseObject response = ResponseObject.serverError(
            request, data
        );
        // RPC������Ԃ��D
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
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
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject orderProducts(RequestObject request)
    {
        // RPC�v�����̃p�����[�^���擾����D
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>)request.getParams();
        
        // RPC�����̌��ʂ𐶐�����D
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        
        int productNo  = Integer.parseInt((String)params.get("productNo"));
        int quantity   = Integer.parseInt((String)params.get("quantity"));
        int unitPrice  = 100;
        int totalPrice = quantity * unitPrice;
        
        result.put("productNo", Integer.toString(productNo));
        result.put("quantity", Integer.toString(quantity));
        result.put("unitPrice", Integer.toString(unitPrice));
        result.put("totalPrice", Integer.toString(totalPrice));
        
        // ���ʂ��܂�RPC�����𐶐�����D
        ResponseObject response = ResponseObject.responseWithResult(
            request, result
        );
        // RPC������Ԃ��D
        return response;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public ResponseObject notification(RequestObject request)
    {
        // Notification �̏ꍇ�́Cnull ��Ԃ��D
        ResponseObject response = null;
        return response;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: IMPLEMENTS: Registerable
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: Registerable
    //
    @Override
    public String getMethodPrefix()
    {
        return METHOD_PREFIX;
    }
}
