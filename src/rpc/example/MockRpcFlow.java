
package rpc.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.25
 *
 */
public class MockRpcFlow
{
    public static void main(String[] args)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        System.out.printf("■1. クライアント側の処理\n");
        MockRequestObject request = new MockRequestObject("echo", "hello", "1");
        System.out.printf("▼1-1 MockRequestObject を生成：\n%s\n\n", request);
        
        String requestObject = gson.toJson(request);
        System.out.printf("▼1-2 MockRequestObject から Request Object を生成：\n%s\n\n", requestObject);
        
        System.out.printf("▼1-3 Request Object をクライアントがサーバへ送信：\n%s\n\n", requestObject);
        
        System.out.printf("\n■2. サーバ側の処理\n");
        System.out.printf("▼2-1 Request Object をサーバがクライアントから受信：\n%s\n\n", requestObject);
        
        request = gson.fromJson(requestObject, MockRequestObject.class);
        System.out.printf("▼2-2 Request Object から MockRequestObject を生成：\n%s\n\n", request);
        
        MockResponseObject response = new MockResponseObject("hello", null, "1");
        System.out.printf("▼2-3 MockResponseObject を生成：\n%s\n\n", response);
        
        String responseObject = gson.toJson(response);
        System.out.printf("▼2-4 MockResponseObject から Response Object を生成\n%s\n\n", responseObject);
        
        System.out.printf("▼2-4 Response Object をサーバがクライアントへ送信：\n%s\n\n", responseObject);
        
        System.out.printf("\n■3. クライアント側の処理\n");
        System.out.printf("▼3-1 Response Object をクライアントがサーバから受信：\n%s\n\n", responseObject);
        
        response = gson.fromJson(responseObject, MockResponseObject.class);
        System.out.printf("▼3-2 Response Object から MockResponseObject を生成：\n%s\n\n", response);
        
    }
}

class MockRequestObject
{
    final String jsonrpc = "2.0";
    final String method;
    final Object params;
    final String id;
    
    MockRequestObject(String method, Object params, String id)
    {
        this.method = method;
        this.params = params;
        this.id = id;
    }
    
    @Override
    public String toString()
    {
        return "SimpleRequestObject{"
            + "jsonrpc=" + this.jsonrpc
            + ", method=" + this.method
            + ", params=" + this.params
            + ", id=" + this.id
            + "}";
    }
}

class MockResponseObject
{
    final String          jsonrpc = "2.0";
    final Object          result;
    final MockErrorObject error;
    final String          id;
    
    MockResponseObject(Object result, MockErrorObject error, String id)
    {
        this.result = result;
        this.error = error;
        this.id = id;
    }
    
    @Override
    public String toString()
    {
        return "SimpleResponseObject{"
            + "jsonrpc=" + this.jsonrpc
            + ", result=" + this.result
            + ", error=" + this.error
            + ", id=" + this.id
            + "}";
    }
}

class MockErrorObject
{
    final int    code;
    final String message;
    final Object data;
    
    MockErrorObject(int code, String message, Object data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    @Override
    public String toString()
    {
        return "SimpleErrorObject{"
            + "code=" + this.code
            + ", message=" + this.message
            + ", data=" + this.data
            + "}";
    }
}