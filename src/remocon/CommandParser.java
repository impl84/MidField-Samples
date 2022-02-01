
package remocon;

import static com.midfield_system.api.util.Constants.STR_DOT;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.midfield_system.api.system.rpc.InvocableMethod;
import com.midfield_system.api.system.rpc.RpcRequest;

/*----------------------------------------------------------------------------*/
/**
 * CommandParser
 *
 * Copyright (C) Koji Hashimoto
 *
 * Date Modified: 2021.09.06 Koji Hashimoto
 *
 */
class CommandParser
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    static final String ARG_SEPARATOR = "\\s+";	//$NON-NLS-1$
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private Map<String, InvocableMethod> methodMap = null;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    CommandParser()
    {
        // コマンドを RpcRequest インスタンスに変換するための
        // パーサクラスのインスタンスを生成する．
        CltMfsNode           cltMfsNode = new CltMfsNode();
        CltDeviceInfoManager cltDiMgr   = new CltDeviceInfoManager();
        CltStreamInfoManager cltSiMgr   = new CltStreamInfoManager();
        CltSegmentIo         cltSegIo   = new CltSegmentIo();
        CltStreamPerformer   cltStmPfmr = new CltStreamPerformer();
        
        // パーサクラスのインスタンスを配列にする．
        Object[] parserInstanceArray = {
            cltMfsNode,
            cltDiMgr,
            cltSiMgr,
            cltSegIo,
            cltStmPfmr,
        };
        // パーサ用メソッド名をキーとし，
        // InvocableMethod インスタンスを値とするマップを生成する．
        this.methodMap = new TreeMap<String, InvocableMethod>();
        
        // パーサクラスのメソッドをマップに登録する．
        for (int i = 0; i < parserInstanceArray.length; i++) {
            registerMethod(parserInstanceArray[i], this.methodMap);
        }
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    Set<String> getMethodNameSet()
    {
        Set<String> nameSet = this.methodMap.keySet();
        return nameSet;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    RpcRequest parseCommand(String command)
        throws InvocationTargetException,
            IllegalAccessException
    {
        // コマンドをセパレータで区切り，文字列の配列にする．
        String[] args = command.split(ARG_SEPARATOR);
        if ((args == null) || (args.length < 1)) {
            // RPC要求に変換できる情報は無いが，
            // RpcRequest を生成し，
            // それをRPC要求(JSON文字列)に変換して戻る．
            RpcRequest rpcReq = requestNothing();
            return rpcReq;
        }
        // コマンド内のメソッド名に対応する InvocableMethod インスタンスを取得する．
        String          methodName = args[0];
        InvocableMethod method     = this.methodMap.get(methodName);
        if (method == null) {
            // メソッド名からメソッドインスタンスを取得できないが，
            // RpcRequest を生成し，
            // それをRPC要求(JSON文字列)に変換して戻る．
            RpcRequest rpcReq = unknownMethod(methodName);
            return rpcReq;
        }
        // 対応するメソッドを呼び出し，RPC要求を生成する．
        RpcRequest rpcReq = (RpcRequest)method.invoke(args);
        // InvocationTargetException, IllegalAccessException
        
        // RPC要求を返す．
        return rpcReq;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void registerMethod(Object parserObject, Map<String, InvocableMethod> map)
    {
        // パーサオブジェクトのクラスオブジェクトを取得する．
        Class<?> parserClass = parserObject.getClass();
        
        // パーサクラス内のメソッドの配列を取得する．
        Method[] methods = parserClass.getMethods();
        
        // パーサクラス内のメソッドを走査する．
        for (int i = 0; i < methods.length; i++) {
            // このメソッドが，与えられたパーサクラス内で定義されている
            // メソッドであることを確認する．
            Class<?> declaringClass = methods[i].getDeclaringClass();
            if (parserClass.equals(declaringClass) == false) {
                continue;
            }
            // このメソッドが public であるかを確認する．
            if ((methods[i].getModifiers() & Modifier.PUBLIC) != 1) {
                continue;
            }
            // このメソッドが RpcRequest を返すことを確認する．
            Class<?> returnType = methods[i].getReturnType();
            if (returnType.equals(RpcRequest.class) == false) {
                continue;
            }
            // このメソッドの引数が 1つであることを確認する．
            Class<?>[] paramTypes = methods[i].getParameterTypes();
            if (paramTypes.length != 1) {
                continue;
            }
            // 引数の型が String[] であることを確認する．
            Class<?> paramClass = paramTypes[0];
            if (paramClass.equals(String[].class) == false) {
                continue;
            }
            // 登録するメソッドのクラス名として，
            // 「Clt」で始まるクラス名の「Clt」(3文字)を取り除いた文字列を生成する．
            String name = declaringClass.getSimpleName().substring(3);
            
            // クラス名付きのメソッド名を生成する．
            name = name.concat(STR_DOT);
            name = name.concat(methods[i].getName());
            
            // メソッドと InvocableMethod のインスタンスをマップへ登録する．
            InvocableMethod method = new InvocableMethod(methods[i], parserObject);
            map.put(name, method);
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private RpcRequest requestNothing()
    {
        // メソッド名が無いRPC要求を生成する．
        RpcRequest rpcReq = RpcRequest.createRequest(null, null);
        
        // RPC要求を返す．
        return rpcReq;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private RpcRequest unknownMethod(String methodName)
    {
        // RPC要求を生成する．
        RpcRequest rpcReq = RpcRequest.createRequest(methodName, null);
        
        // RPC要求を返す．
        return rpcReq;
    }
}
