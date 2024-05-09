
package rpc.example;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.midfield_system.api.util.Constants;
import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.RequestObject;
import com.midfield_system.rpc.api.ResponseObject;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.24
 *
 */
public class MockRpcExample
{
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
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MockRpcExample example = new MockRpcExample();
        example.invokeAll();
    }
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final Gson gson;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    MockRpcExample()
    {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void invokeAll()
    {
        exampleSimpleRpc();
        exampleErrorResponse();
        exampleMethodWithStringParam();
        exampleMethodWithMapParam();
        exampleNotification();
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleSimpleRpc()
    {
        // ���P���ȃ��\�b�h�Ăяo���̗�F
        printTitle("�P���ȃ��\�b�h�Ăяo���̗�F");
        
        // �N���C�A���g�FRPC�v���𐶐�����D
        RequestObject request = RequestObject.createRequest("Mock.getBooleanResult", null);
        
        // �N���C�A���g�FRPC�v����JSON�`���ɃV���A���C�Y����D
        // (������T�[�o�֑��M����D)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // �T�[�o�FJSON�`������RPC�v���փf�V���A���C�Y����D
        request = this.gson.fromJson(jsonRequest, RequestObject.class);
        
        // �T�[�o�FRPC�v������������RPC�����𐶐�����D
        ResponseObject response = ResponseObject.booleanResponse(request, true);
        
        // �T�[�o�FRPC������JSON�`���ɃV���A���C�Y����D
        // (������N���C�A���g�֑��M����D)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // �N���C�A���g�FJSON�`������RPC�����փf�V���A���C�Y����D
        response = this.gson.fromJson(jsonResponse, ResponseObject.class);
        
        // �N���C�A���g�FRPC�������猋�ʂ��擾����D
        String result = (String)response.getResult();
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleErrorResponse()
    {
        // ���G���[�������̗�F
        printTitle("�G���[�������̗�F");
        
        // �N���C�A���g�FRPC�v���𐶐�����D
        RequestObject request = RequestObject.createRequest("Mock.getMethodNotFound", null);
        
        // �N���C�A���g�FRPC�v����JSON�`���ɃV���A���C�Y����D
        // (������T�[�o�֑��M����D)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // �T�[�o�FJSON�`������RPC�v���փf�V���A���C�Y����D
        request = this.gson.fromJson(jsonRequest, RequestObject.class);
        
        // �T�[�o�FRPC�v������������RPC�����𐶐�����D
        ResponseObject response = ResponseObject.methodNotFound(request);
        
        // �T�[�o�FRPC������JSON�`���ɃV���A���C�Y����D
        // (������N���C�A���g�֑��M����D)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // �N���C�A���g�FJSON�`������RPC�����փf�V���A���C�Y����D
        response = this.gson.fromJson(jsonResponse, ResponseObject.class);
        
        // �N���C�A���g�FRPC�������猋�ʂ��擾����D
        String result = (String)response.getResult();
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleMethodWithStringParam()
    {
        // ����������p�����[�^�Ƃ���RPC�v���Ɖ����̗�F
        printTitle("��������p�����[�^�Ƃ���RPC�v���Ɖ����̗�F");
        
        // �N���C�A���g�F�p�����[�^�t����RPC�v��(�G�R�[�v��)�𐶐�����D
        RequestObject request = RequestObject.createRequest("Mock.echo", "hello");
        
        // �N���C�A���g�FRPC�v����JSON�`���ɃV���A���C�Y����D
        // (������T�[�o�֑��M����D)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // �T�[�o�FJSON�`������RPC�v���փf�V���A���C�Y����D
        request = this.gson.fromJson(jsonRequest, RequestObject.class);
        
        // �T�[�o�FRPC�v�����̃p�����[�^(�G�R�[������)���擾����D
        String echoString = (String)request.getParams();
        
        // �T�[�o�F�G�R�[����������ʂƂ���RPC�����𐶐�����D
        ResponseObject response = ResponseObject.responseWithResult(request, echoString);
        
        // �T�[�o�FRPC������JSON�`���ɃV���A���C�Y����D
        // (������N���C�A���g�֑��M����D)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // �N���C�A���g�FJSON�`������RPC�����փf�V���A���C�Y����D
        response = this.gson.fromJson(jsonResponse, ResponseObject.class);
        
        // �N���C�A���g�FRPC�������猋�ʂ��擾����D
        String result = (String)response.getResult();
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    @SuppressWarnings("unchecked")
    private void exampleMethodWithMapParam()
    {
        // �������̃p�����[�^�𔺂�RPC�v���Ɖ����̗�F
        printTitle("�����̃p�����[�^�𔺂�RPC�v���Ɖ����̗�F");
        
        // �N���C�A���g�FRPC�̈����ƂȂ�}�b�v�𐶐�����D
        int productNo = 1;
        int quantity  = 20;
        
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("productNo", Integer.toString(productNo));
        params.put("quantity", Integer.toString(quantity));
        
        // �N���C�A���g�F�����̃p�����[�^�𔺂�RPC�v���𐶐�����D
        RequestObject request = RequestObject.createRequest("Mock.orderProducts", params);
        
        // �N���C�A���g�FRPC�v����JSON�`���ɃV���A���C�Y����D
        // (������T�[�o�֑��M����D)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // �T�[�o�FJSON�`������RPC�v���փf�V���A���C�Y����D
        request = this.gson.fromJson(jsonRequest, RequestObject.class);
        
        // �T�[�o�FRPC�v�����̃p�����[�^���擾����D
        params = (Map<String, Object>)request.getParams();
        
        // �T�[�o�FRPC�����̌��ʂ𐶐�����D
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        productNo = Integer.parseInt((String)params.get("productNo"));
        quantity = Integer.parseInt((String)params.get("quantity"));
        int unitPrice  = 100;
        int totalPrice = quantity * unitPrice;
        
        result.put("productNo", Integer.toString(productNo));
        result.put("quantity", Integer.toString(quantity));
        result.put("unitPrice", Integer.toString(unitPrice));
        result.put("totalPrice", Integer.toString(totalPrice));
        
        // �T�[�o�F���ʂ��܂�RPC�����𐶐�����D
        ResponseObject response = ResponseObject.responseWithResult(request, result);
        
        // �T�[�o�FRPC������JSON�`���ɃV���A���C�Y����D
        // (������N���C�A���g�֑��M����D)
        String jsonResponse = this.gson.toJson(response);
        printJsonString(jsonResponse);
        
        // �N���C�A���g�FJSON�`������RPC�����փf�V���A���C�Y����D
        response = this.gson.fromJson(jsonResponse, ResponseObject.class);
        
        // �N���C�A���g�FRPC�������猋�ʂ��擾����D
        result = (Map<String, Object>)response.getResult();
        
        printResult(
            String.format(
                "productNo: %s, quantity: %s, unitPrice: \\%s, totalPrice: \\%s",
                result.get("productNo"),
                result.get("quantity"),
                result.get("unitPrice"),
                result.get("totalPrice")
            )
        );
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleNotification()
    {
        // ��RPC�v��(Notification)�̗�F
        printTitle("Notification �̗�F");
        
        // �N���C�A���g�FRPC�v��(Notification)�𐶐�����D
        RequestObject request = RequestObject.createNotification("Mock.notification", null);
        
        // �N���C�A���g�FRPC�v����JSON�`���ɃV���A���C�Y����D
        // (������T�[�o�֑��M����D)
        String jsonRequest = this.gson.toJson(request);
        printJsonString(jsonRequest);
        
        // �T�[�o�FJSON�`������RPC�v���փf�V���A���C�Y����D
        request = this.gson.fromJson(jsonRequest, RequestObject.class);
        
        // �T�[�o�FRPC�v��(Notification)����������D
        // (�����ł͉������Ȃ��DNotification �ɑ΂��Ă͉������Ȃ��D)
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void printTitle(String title)
    {
        Log.message(Log.LINE_SEPARATOR);
        Log.message("��%s", title);
        Log.message(Log.LINE_SEPARATOR);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void printJsonString(String jsonString)
    {
        Log.message(Constants.STR_NEW_LINE + jsonString);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void printResult(Object result)
    {
        Log.message("��result: %s", result);
        Log.message();
    }
}
