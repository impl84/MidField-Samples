
package rpc.example;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
public class DemoClientMethod
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final RpcClient rpcClient;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    DemoClientMethod(RpcClient rpcClient)
    {
        this.rpcClient = rpcClient;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void invokeAll()
    {
        exampleSimpleRpc();
        exampleErrorResponse();
        exampleUsingFuture();
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
        
        Object result = invoke("Demo.getBooleanResult", null);
        printResult(result);
        
        result = invoke("Demo.getSuccessResult", null);
        printResult(result);
        
        result = invoke("Demo.getResponseWithResult", null);
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleErrorResponse()
    {
        // ���e��G���[�������̗�F
        printTitle("�e��G���[�������̗�F");
        
        Object result = invoke("Demo.getParseError", null);
        printResult(result);
        
        result = invoke("Demo.getInvalidRequest", null);
        printResult(result);
        
        result = invoke("Demo.getMethodNotFound", null);
        printResult(result);
        
        result = invoke("Demo.getInvalidParams", null);
        printResult(result);
        
        result = invoke("Demo.getInternalError", null);
        printResult(result);
        
        result = invoke("Demo.getServerError", null);
        printResult(result);
        
        result = invoke(null, null);
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleUsingFuture()
    {
        // ��Future<ResponseObject> �𗘗p����RPC�v���Ɖ����̗�F
        printTitle("Future<ResponseObject> �𗘗p����RPC�v���Ɖ����̗�F");
        
        Future<ResponseObject> future0 = sendRequest("Demo.getBooleanResult", null);
        Future<ResponseObject> future1 = sendRequest("Demo.getSuccessResult", null);
        
        Object result = waitResponse(future0);
        printResult(result);
        
        result = waitResponse(future1);
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleMethodWithStringParam()
    {
        // ����������p�����[�^�Ƃ���RPC�v���Ɖ����̗�F
        printTitle("��������p�����[�^�Ƃ���RPC�v���Ɖ����̗�F");
        
        String result = (String)invoke("Demo.echo", "hello");
        printResult(result);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleMethodWithMapParam()
    {
        // �������̃p�����[�^�𔺂�RPC�v���Ɖ����̗�F
        printTitle("�����̃p�����[�^�𔺂�RPC�v���Ɖ����̗�F");
        
        // RPC�̈����ƂȂ�}�b�v�𐶐�����D
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        
        int productNo = 1;
        int quantity  = 20;
        
        params.put("productNo", Integer.toString(productNo));
        params.put("quantity", Integer.toString(quantity));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>)invoke("Demo.orderProducts", params);
        if (result != null) {
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
        else {
            printResult("null");
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void exampleNotification()
    {
        // ��RPC�v��(Notification)�̗�F
        printTitle("Notification �̗�F");
        
        notify("Demo.notification", null);
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private Object invoke(String method, Object params)
    {
        Object result = null;
        try {
            Log.message("��invoke: %s(%s)", method, params);
            
            // �T�[�o�̃��\�b�h���Ăяo���CRPC�������擾����D
            // (InterruptedException, ExecutionException, TimeoutException)            
            ResponseObject response = this.rpcClient.invoke(method, params, 2000);
            
            // ���ʂ� null �̏ꍇ�̓G���[�����o�͂���D
            result = response.getResult();
            if (result == null) {
                ErrorObject error = response.getError();
                Log.message("�� error: %s", error);
            }
        }
        catch (Exception ex) {
            Log.error(ex);
        }
        return result;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private Future<ResponseObject> sendRequest(String method, Object params)
    {
        Log.message("��request: %s(%s)", method, params);
        
        // RPC�v�����T�[�o�֑���D
        Future<ResponseObject> future = this.rpcClient.request(method, params);
        return future;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private Object waitResponse(Future<ResponseObject> future)
    {
        Object result = null;
        try {
            // RPC�������擾����D
            // (InterruptedException, ExecutionException, TimeoutException)
            ResponseObject response = future.get(2000, TimeUnit.MILLISECONDS);
            
            // ���ʂ� null �̏ꍇ�̓G���[�����o�͂���D
            result = response.getResult();
            if (result == null) {
                ErrorObject error = response.getError();
                Log.message("�� error: %s", error);
            }
        }
        catch (Exception ex) {
            Log.error(ex);
        }
        return result;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void notify(String method, Object params)
    {
        Log.message("��notify: %s(%s)", method, params);
        
        // RPC�v��(Notification)���T�[�o�֑���D
        this.rpcClient.notification(method, params);
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
    private void printResult(Object result)
    {
        Log.message("��result: %s", result);
        Log.message();
    }
}
